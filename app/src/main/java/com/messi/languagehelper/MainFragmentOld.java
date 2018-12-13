package com.messi.languagehelper;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.bean.BaiduOcrRoot;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OrcResultListener;
import com.messi.languagehelper.util.CameraUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.OrcHelper;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainFragmentOld extends BaseFragment implements OnClickListener, OrcResultListener {

    public static MainFragmentOld mMainFragment;
    public static String base64;
    private EditText input_et;
    private FrameLayout submit_btn_cover;
    private FrameLayout photo_tran_btn;
    private FrameLayout clear_btn_layout;
    private Button voice_btn;
    private LinearLayout speak_round_layout;
    private TextView cb_speak_language_ch, cb_speak_language_en;
    private TextView submit_btn;
    private TabLayout tablayout;

    public long lastSubmitTiem;
    public int currentTabIndex;
    public MainTabTran mMainTabTran;
    public DictionaryFragmentOld mDictionaryFragmentOld;
    public ChineseDictionaryFragment mChDicFragment;
    public JuhaiFragment mJuhaiFragment;
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


    public static MainFragmentOld getInstance(FragmentProgressbarListener listener) {
        if (mMainFragment == null) {
            mMainFragment = new MainFragmentOld();
            mMainFragment.setmProgressbarListener(listener);
        }
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_translate_old, null);
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
        cb_speak_language_ch = (TextView) view.findViewById(R.id.cb_speak_language_ch);
        cb_speak_language_en = (TextView) view.findViewById(R.id.cb_speak_language_en);
        speak_round_layout = (LinearLayout) view.findViewById(R.id.speak_round_layout);
        clear_btn_layout = (FrameLayout) view.findViewById(R.id.clear_btn_layout);
        record_layout = (LinearLayout) view.findViewById(R.id.record_layout);
        record_anim_img = (ImageView) view.findViewById(R.id.record_anim_img);
        voice_btn = (Button) view.findViewById(R.id.voice_btn);
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        submit_btn = (TextView) view.findViewById(R.id.submit_btn);

        photo_tran_btn.setOnClickListener(this);
        submit_btn_cover.setOnClickListener(this);
        cb_speak_language_ch.setOnClickListener(this);
        cb_speak_language_en.setOnClickListener(this);
        speak_round_layout.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);

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
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_dictionary)));
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_sentence)));
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_english_dic)));
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.title_chinese_dic)));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabIndex = tab.getPosition();
                setPomptAndShowFragment();
                Setings.saveSharedPreferences(PlayUtil.getSP(),
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

    private void initFragment(){
        mMainTabTran = MainTabTran.getInstance(mProgressbarListener);
        mDictionaryFragmentOld = DictionaryFragmentOld.getInstance(mProgressbarListener);
        mJuhaiFragment = JuhaiFragment.getInstance(mProgressbarListener);
        mEnDicFragment = EnDicFragment.getInstance(mProgressbarListener);
        mChDicFragment = ChineseDictionaryFragment.getInstance(mProgressbarListener);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout,mMainTabTran)
                .add(R.id.content_layout,mDictionaryFragmentOld)
                .add(R.id.content_layout, mJuhaiFragment)
                .add(R.id.content_layout, mEnDicFragment)
                .add(R.id.content_layout,mChDicFragment)
                .commit();
        hideAllFragment();
    }

    private void hideAllFragment(){
        getChildFragmentManager()
                .beginTransaction()
                .hide(mMainTabTran)
                .hide(mDictionaryFragmentOld)
                .hide(mChDicFragment)
                .hide(mJuhaiFragment)
                .hide(mEnDicFragment)
                .commit();
    }

    private void initLanguage() {
        if (PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD).equals(XFUtil.VoiceEngineMD)) {
            cb_speak_language_ch.setBackgroundResource(R.drawable.language_btn_bg_s);
            cb_speak_language_ch.setTextColor(getResources().getColor(R.color.white_alph));
            cb_speak_language_en.setBackgroundResource(R.drawable.language_btn_bg_n);
            cb_speak_language_en.setTextColor(getResources().getColor(R.color.text_black_alph));
        } else {
            cb_speak_language_ch.setBackgroundResource(R.drawable.language_btn_bg_n);
            cb_speak_language_ch.setTextColor(getResources().getColor(R.color.text_black_alph));
            cb_speak_language_en.setBackgroundResource(R.drawable.language_btn_bg_s);
            cb_speak_language_en.setTextColor(getResources().getColor(R.color.white_alph));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            submit();
            hideIME();
            AVAnalytics.onEvent(getContext(), "tab1_submit_btn");
        } else if (v.getId() == R.id.photo_tran_btn) {
            MainFragmentOldPermissionsDispatcher.showORCDialogWithPermissionCheck(this);
        } else if (v.getId() == R.id.speak_round_layout) {
            MainFragmentOldPermissionsDispatcher.showRecordWithPermissionCheck(this);
            AVAnalytics.onEvent(getContext(), "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(getContext(), "tab1_clear_btn");
        } else if (v.getId() == R.id.cb_speak_language_ch) {
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD);
            initLanguage();
            ToastUtil.diaplayMesShort(getContext(), getContext().getResources().getString(R.string.speak_chinese));
            AVAnalytics.onEvent(getContext(), "tab1_zh_sbtn");
        } else if (v.getId() == R.id.cb_speak_language_en) {
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineEN);
            initLanguage();
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
                input_et.setHint(getString(R.string.input_et_hint));
                submit_btn.setText(getString(R.string.translate));
                getChildFragmentManager()
                        .beginTransaction().show(mMainTabTran).commit();
                isChangeTabNeedSearch();
                break;
            case 1:
                input_et.setHint(getString(R.string.input_et_hint_dictionary));
                getChildFragmentManager()
                        .beginTransaction().show(mDictionaryFragmentOld).commit();
                isChangeTabNeedSearch();
                break;
            case 2:
                input_et.setHint(getString(R.string.input_et_hint_dictionary));
                getChildFragmentManager()
                        .beginTransaction().show(mJuhaiFragment).commit();
                isChangeTabNeedSearch();
                break;
            case 3:
                input_et.setHint(getString(R.string.input_et_hint_english));
                getChildFragmentManager()
                        .beginTransaction().show(mEnDicFragment).commit();
                isChangeTabNeedSearch();
                break;
            case 4:
                input_et.setHint(getString(R.string.input_et_hint_chinese));
                getChildFragmentManager()
                        .beginTransaction().show(mChDicFragment).commit();
                isChangeTabNeedSearch();
                break;
        }
    }

    private void isChangeTabNeedSearch(){
        if (System.currentTimeMillis() - lastSubmitTiem < 1000 * 10){
            if (PlayUtil.getSP().getBoolean(KeyUtil.AutoClearInput, true)) {
                input_et.setText(Setings.q);
                input_et.setSelection(Setings.q.length());
            }
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showORCDialog() {
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
                mDictionaryFragmentOld.refresh();
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

    public void showIat() {
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setBackgroundColor(getContext().getResources().getColor(R.color.none));
            voice_btn.setText(getContext().getResources().getString(R.string.finish));
            speak_round_layout.setBackgroundResource(R.drawable.round_light_blue_bgl);
            XFUtil.showSpeechRecognizer(getContext(), PlayUtil.getSP(), recognizer, recognizerListener,
                    PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD));
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
        Setings.q = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(Setings.q)) {
            String last = Setings.q.substring(Setings.q.length() - 1);
            if (",.!;:'，。！‘；：".contains(last)) {
                Setings.q = Setings.q.substring(0, Setings.q.length() - 1);
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
                mDictionaryFragmentOld.submit();
                break;
            case 2:
                mJuhaiFragment.submit();
                break;
            case 3:
                mEnDicFragment.submit();
                break;
            case 4:
                mChDicFragment.submit();
                break;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void showRecord() {
        showIat();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainFragmentOldPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    void onShowRationale(final PermissionRequest request) {
        showRationaleDialog(request);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleFoCamera(final PermissionRequest request){
        showRationaleDialog(request);
    }

    public void showRationaleDialog(PermissionRequest request){
        new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("温馨提示")
                .setMessage("需要授权才能使用。")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).show();
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    void onPerDenied() {
        ToastUtil.diaplayMesShort(getContext(),"拒绝录音权限，无法使用语音功能！");
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showCameraDenied(){
        ToastUtil.diaplayMesShort(getContext(),"拒绝授权，将无法使用部分功能！");
    }
}

