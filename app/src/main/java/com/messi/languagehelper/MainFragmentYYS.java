package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.bean.BaiduOcrRoot;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OrcResultListener;
import com.messi.languagehelper.util.CameraUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.OrcHelper;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;
import com.mindorks.nybus.annotation.Subscribe;

public class MainFragmentYYS extends BaseFragment implements OnClickListener, OrcResultListener {

    public static MainFragmentYYS mMainFragment;
    private EditText input_et;
    private FrameLayout submit_btn_cover;
    private FrameLayout photo_tran_btn;
    private FrameLayout clear_btn_layout;
    private Button voice_btn;
    private LinearLayout speak_round_layout;
    private RadioButton cb_speak_language_ch, cb_speak_language_en;
    private TextView submit_btn;
    private TabLayout tablayout;
    private RadioButton rb_to_yue,rb_to_zh;
    private RadioGroup zh_yue_layout;

    public long lastSubmitTiem;
    public int currentTabIndex;
    public MainTabTranZhYue mMainTabTran;
    public ChineseDictionaryFragment mChDicFragment;
    public EnDicFragment mEnDicFragment;

    private LinearLayout record_layout;
    private ImageView record_anim_img;
    // 识别对象
    private SpeechRecognizer recognizer;
    private OrcHelper mOrcHelper;
    private View view;
    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(getContext(), err.getErrorDescription());
            hideProgressbar();
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            showProgressbar();
            finishRecord();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult");
            String text = JsonParser.parseIatResult(results.getResultString());
            input_et.append(text.toLowerCase());
            input_et.setSelection(input_et.length());
            if (isLast) {
                finishRecord();
                JudgeSpeakTranslateLan();
                submit();
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onVolumeChanged(int volume, byte[] arg1) {
            if (volume < 4) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
            } else if (volume < 8) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_2);
            } else if (volume < 12) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_3);
            } else if (volume < 16) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_4);
            } else if (volume < 20) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_5);
            } else if (volume < 24) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_6);
            } else if (volume < 31) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_7);
            }
        }

    };


    public static MainFragmentYYS getInstance(FragmentProgressbarListener listener) {
        if (mMainFragment == null) {
            mMainFragment = new MainFragmentYYS();
            mMainFragment.setmProgressbarListener(listener);
        }
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_translate_yys, null);
        LogUtil.DefalutLog("MainFragmentOld-onCreateView");
        init();
        return view;
    }

    private void init() {
        isRegisterBus = true;
        recognizer = SpeechRecognizer.createRecognizer(getContext(), null);

        input_et = (EditText) view.findViewById(R.id.input_et);
        submit_btn_cover = (FrameLayout) view.findViewById(R.id.submit_btn_cover);
        photo_tran_btn = (FrameLayout) view.findViewById(R.id.photo_tran_btn);
        cb_speak_language_ch = (RadioButton) view.findViewById(R.id.cb_speak_language_ch);
        cb_speak_language_en = (RadioButton) view.findViewById(R.id.cb_speak_language_en);
        speak_round_layout = (LinearLayout) view.findViewById(R.id.speak_round_layout);
        clear_btn_layout = (FrameLayout) view.findViewById(R.id.clear_btn_layout);
        record_layout = (LinearLayout) view.findViewById(R.id.record_layout);
        record_anim_img = (ImageView) view.findViewById(R.id.record_anim_img);
        voice_btn = (Button) view.findViewById(R.id.voice_btn);
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        submit_btn = (TextView) view.findViewById(R.id.submit_btn);
        rb_to_yue = (RadioButton) view.findViewById(R.id.rb_to_yue);
        rb_to_zh = (RadioButton) view.findViewById(R.id.rb_to_zh);
        zh_yue_layout = (RadioGroup) view.findViewById(R.id.zh_yue_layout);

        photo_tran_btn.setOnClickListener(this);
        submit_btn_cover.setOnClickListener(this);
        cb_speak_language_ch.setOnClickListener(this);
        cb_speak_language_en.setOnClickListener(this);
        speak_round_layout.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);
        rb_to_yue.setOnClickListener(this);
        rb_to_zh.setOnClickListener(this);

        initTranslateSelected();
        initTablayout();
        initFragment();
        initLanguage();
        setPomptAndShowFragment();
    }

    private void initTablayout(){
        if(SystemUtil.lan.equals("en")){
            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        currentTabIndex = PlayUtil.getSP().getInt(KeyUtil.MainFragmentIndex,0);
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_translate)));
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_chinese_dic)));
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_english_dic)));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabIndex = tab.getPosition();
                setPomptAndShowFragment();
                Settings.saveSharedPreferences(PlayUtil.getSP(),
                        KeyUtil.MainFragmentIndex,
                        currentTabIndex);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initTranslateSelected(){
        boolean IsTranslateYue = PlayUtil.getSP().getBoolean(KeyUtil.IsTranslateYueKey, true);
        if(IsTranslateYue){
            rb_to_yue.setChecked(true);
            rb_to_zh.setChecked(false);
        }else{
            rb_to_yue.setChecked(false);
            rb_to_zh.setChecked(true);
        }
    }

    private void initFragment(){
        mMainTabTran = MainTabTranZhYue.getInstance(mProgressbarListener);
        mChDicFragment = ChineseDictionaryFragment.getInstance(mProgressbarListener);
        mEnDicFragment = EnDicFragment.getInstance(mProgressbarListener);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout,mMainTabTran)
                .add(R.id.content_layout,mChDicFragment)
                .add(R.id.content_layout, mEnDicFragment)
                .commit();
        hideAllFragment();
    }

    private void hideAllFragment(){
        getChildFragmentManager()
                .beginTransaction()
                .hide(mMainTabTran)
                .hide(mChDicFragment)
                .hide(mEnDicFragment)
                .commit();
    }

    private void initLanguage() {
        if (PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguageYYS, XFUtil.VoiceEngineMD).equals(XFUtil.VoiceEngineMD)) {
            cb_speak_language_ch.setChecked(true);
            cb_speak_language_en.setChecked(false);
        } else {
            cb_speak_language_ch.setChecked(false);
            cb_speak_language_en.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            JudgeBtnTranslateLan();
            submit();
            hideIME();
            AVAnalytics.onEvent(getContext(), "tab1_submit_btn");
        } else if (v.getId() == R.id.photo_tran_btn) {
            showORCDialog();
        } else if (v.getId() == R.id.speak_round_layout) {
            showIatDialog();
            AVAnalytics.onEvent(getContext(), "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(getContext(), "tab1_clear_btn");
        } else if (v.getId() == R.id.cb_speak_language_ch) {
            cb_speak_language_en.setChecked(false);
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguageYYS, XFUtil.VoiceEngineMD);
            ToastUtil.diaplayMesShort(getContext(), getContext().getResources().getString(R.string.speak_chinese));
            AVAnalytics.onEvent(getContext(), "tab1_zh_sbtn");
        } else if (v.getId() == R.id.cb_speak_language_en) {
            cb_speak_language_ch.setChecked(false);
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguageYYS, XFUtil.VoiceEngineHK);
            ToastUtil.diaplayMesShort(getContext(), getContext().getResources().getString(R.string.speak_english));
            AVAnalytics.onEvent(getContext(), "tab1_en_sbtn");
        }
    }

    private void setPomptAndShowFragment(){
        hideAllFragment();
        tablayout.getTabAt(currentTabIndex).select();
        submit_btn.setText(getString(R.string.query));
        switch (currentTabIndex){
            case 0:
                zh_yue_layout.setVisibility(View.VISIBLE);
                input_et.setHint(getString(R.string.input_et_zh_yue_hint));
                submit_btn.setText(getString(R.string.translate));
                getChildFragmentManager()
                        .beginTransaction().show(mMainTabTran).commit();
                isChangeTabNeedSearch();
                break;
            case 1:
                zh_yue_layout.setVisibility(View.GONE);
                input_et.setHint(getString(R.string.input_et_hint_chinese));
                getChildFragmentManager()
                        .beginTransaction().show(mChDicFragment).commit();
                isChangeTabNeedSearch();
                break;
            case 2:
                zh_yue_layout.setVisibility(View.GONE);
                input_et.setHint(getString(R.string.input_et_hint_english));
                getChildFragmentManager()
                        .beginTransaction().show(mEnDicFragment).commit();
                isChangeTabNeedSearch();
                break;
        }
    }

    private void isChangeTabNeedSearch(){
        if (System.currentTimeMillis() - lastSubmitTiem < 1000 * 10){
            if (PlayUtil.getSP().getBoolean(KeyUtil.AutoClearInput, true)) {
                input_et.setText(Settings.q);
                input_et.setSelection(Settings.q.length());
            }
        }
    }

    private void showORCDialog() {
        if (mOrcHelper == null) {
            mOrcHelper = new OrcHelper(this, this, mProgressbarListener);
        }
        mOrcHelper.photoSelectDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("MainFragment-setUserVisibleHint");
        if (isVisibleToUser) {
            refresh();
        }
    }

    private void refresh() {
        if (getContext() != null && mMainTabTran != null) {
            refreshFragment();
        }
    }

    private void refreshFragment(){
        switch (currentTabIndex){
            case 0:
                mMainTabTran.refresh();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    public void autoClearAndautoPlay() {
        if (PlayUtil.getSP().getBoolean(KeyUtil.AutoClearInput, true)) {
            input_et.setText("");
        }
    }

    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(getContext(), toastString);
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        Settings.verifyStoragePermissions(getActivity(), Settings.PERMISSIONS_RECORD_AUDIO);
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setBackgroundColor(getContext().getResources().getColor(R.color.none));
            voice_btn.setText(getContext().getResources().getString(R.string.finish));
            speak_round_layout.setBackgroundResource(R.drawable.round_light_blue_bgl);
            XFUtil.showSpeechRecognizer(getContext(), PlayUtil.getSP(), recognizer, recognizerListener,
                    PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguageYYS, XFUtil.VoiceEngineMD));
        } else {
            finishRecord();
            recognizer.stopListening();
            showProgressbar();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText("");
        voice_btn.setBackgroundResource(R.drawable.ic_voice_padded_normal);
        speak_round_layout.setBackgroundResource(R.drawable.round_gray_bgl_old);
    }

    /**
     * 点击翻译之后隐藏输入法
     */
    private void hideIME() {
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(submit_btn_cover.getWindowToken(), 0);
    }

    /**
     * 点击编辑之后显示输入法
     */
    private void showIME() {
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * submit request task
     */
    private void submit() {
        lastSubmitTiem = System.currentTimeMillis();
        Settings.q = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(Settings.q)) {
            String last = Settings.q.substring(Settings.q.length() - 1);
            if (",.!;:'，。！‘；：".contains(last)) {
                Settings.q = Settings.q.substring(0, Settings.q.length() - 1);
            }
            submit_to_fragment();
        } else {
            showToast(getContext().getResources().getString(R.string.input_et_empty));
            hideProgressbar();
        }
    }

    private void submit_to_fragment(){
        switch (currentTabIndex){
            case 0:
                mMainTabTran.submit();
                break;
            case 1:
                mChDicFragment.submit();
                break;
            case 2:
                mEnDicFragment.submit();
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private void JudgeBtnTranslateLan(){
        LogUtil.DefalutLog("JudgeBtnTranslateLan");
        if(rb_to_yue.isChecked()){
            LanguagehelperHttpClient.setTranslateLan(true);
        }else{
            LanguagehelperHttpClient.setTranslateLan(false);
        }
    }

    private void JudgeSpeakTranslateLan(){
        LogUtil.DefalutLog("JudgeSpeakTranslateLan");
        if(cb_speak_language_ch.isChecked()){
            LanguagehelperHttpClient.setTranslateLan(true);
        }else{
            LanguagehelperHttpClient.setTranslateLan(false);
        }
    }

    private CharSequence getBtnText(String text){
        CharSequence btnText = "";
        switch (text){
            case KeyUtil.Btn_Tran:
                btnText = getText(R.string.title_translate);
                break;
            case KeyUtil.Btn_Sentence:
                btnText = getText(R.string.title_sentence);
                break;
            case KeyUtil.Btn_Dictionary:
                btnText = getText(R.string.title_dictionary);
                break;
            case KeyUtil.Btn_ENENDic:
                btnText = getText(R.string.title_english_dic);
                break;
            case KeyUtil.Btn_CHCHDic:
                btnText = getText(R.string.title_chinese_dic);
                break;
        }
        return btnText;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer = null;
        }
    }

    @Subscribe
    public void onEvent(FinishEvent event){
        LogUtil.DefalutLog("FinishEvent");
        autoClearAndautoPlay();
    }

    @Override
    public void ShowResult(BaiduOcrRoot mBaiduOcrRoot) {
        input_et.setText("");
        input_et.setText(CameraUtil.getOcrResult(mBaiduOcrRoot));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOrcHelper.onActivityResult(requestCode, resultCode, data);
    }
}

