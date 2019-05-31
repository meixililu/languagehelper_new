package com.messi.languagehelper;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.bean.BaiduOcrRoot;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OrcResultListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AnimationUtil;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainFragment extends BaseFragment implements OnClickListener, OrcResultListener {

    public static MainFragment mMainFragment;
    @BindView(R.id.content_layout)
    LinearLayout content_layout;
    @BindView(R.id.input_type_btn)
    ImageView input_type_btn;

    @BindView(R.id.mic_layout)
    LinearLayout mic_layout;
    @BindView(R.id.input_et)
    AppCompatEditText input_et;
    @BindView(R.id.clear_btn_layout)
    FrameLayout clear_btn_layout;
    @BindView(R.id.submit_btn)
    TextView submit_btn;
    @BindView(R.id.submit_btn_cover)
    CardView submit_btn_cover;
    @BindView(R.id.keybord_layout)
    LinearLayout keybord_layout;
    @BindView(R.id.input_type_layout)
    LinearLayout input_type_layout;
    @BindView(R.id.more_tools_layout)
    LinearLayout more_tools_layout;
    @BindView(R.id.voice_btn)
    TextView voice_btn;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.voice_btn_cover)
    CardView voice_btn_cover;
    @BindView(R.id.input_layout)
    LinearLayout input_layout;
    @BindView(R.id.speak_language_tv)
    TextView speakLanguageTv;
    @BindView(R.id.speak_language_layout)
    LinearLayout speakLanguageLayout;
    @BindView(R.id.more_tools_layout_mic)
    LinearLayout more_tools_layout_mic;
    @BindView(R.id.action_layout)
    LinearLayout actionLayout;
    @BindView(R.id.bottom_layout)
    CardView bottomLayout;
    @BindView(R.id.action_photo_tran_btn)
    CardView actionPhotoTranBtn;
    @BindView(R.id.more_tools_img_mic)
    ImageView moreToolsImgMic;
    @BindView(R.id.more_tools_img)
    ImageView moreToolsImg;
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    // 识别对象
    private SpeechRecognizer recognizer;
    private FragmentProgressbarListener mProgressbarListener;
    private OrcHelper mOrcHelper;

    public long lastSubmitTiem;
    public int currentTabIndex;
    public MainTabTran mMainTabTran;
    public DictionaryFragmentOld mDictionaryFragmentOld;
    public ChineseDictionaryFragment mChDicFragment;
    public JuhaiFragment mJuhaiFragment;
    public EnDicFragment mEnDicFragment;

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

    public static MainFragment getInstance(FragmentProgressbarListener listener) {
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
        }
        return mMainFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        isRegisterBus = true;
        recognizer = SpeechRecognizer.createRecognizer(getContext(), null);
        initTablayout();
        setSpeakLanguageTv();
        initFragment();
        submit_btn_cover.setOnClickListener(this);
        speakLanguageLayout.setOnClickListener(this);
        voice_btn_cover.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);
        input_layout.setOnClickListener(this);
        input_type_layout.setOnClickListener(this);
        more_tools_layout.setOnClickListener(this);
        more_tools_layout_mic.setOnClickListener(this);
        actionLayout.setOnClickListener(this);
        actionPhotoTranBtn.setOnClickListener(this);

        if (PlayUtil.getSP().getBoolean(KeyUtil.IsShowTranKeybordLayout, false)) {
            showKeybordLayout();
        } else {
            showMicLayout();
        }
        input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    submit_btn_cover.setVisibility(View.VISIBLE);
                    more_tools_layout.setVisibility(View.GONE);
                } else {
                    more_tools_layout.setVisibility(View.VISIBLE);
                    submit_btn_cover.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
        mChDicFragment = ChineseDictionaryFragment.getInstance(mProgressbarListener);
        mJuhaiFragment = JuhaiFragment.getInstance(mProgressbarListener);
        mEnDicFragment = EnDicFragment.getInstance(mProgressbarListener);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout,mMainTabTran)
                .add(R.id.content_layout,mDictionaryFragmentOld)
                .add(R.id.content_layout,mChDicFragment)
                .add(R.id.content_layout, mJuhaiFragment)
                .add(R.id.content_layout, mEnDicFragment)
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

    private void setPomptAndShowFragment(){
        hideAllFragment();
        tablayout.getTabAt(currentTabIndex).select();
        switch (currentTabIndex){
            case 0:
                input_et.setHint(getString(R.string.input_et_hint));
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            submit();
            AVAnalytics.onEvent(getContext(), "tab1_submit_btn");
        } else if (v.getId() == R.id.action_photo_tran_btn) {
            resetImg();
            MainFragmentPermissionsDispatcher.showORCDialogWithPermissionCheck(this);
        } else if (v.getId() == R.id.voice_btn_cover) {
            MainFragmentPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
            AVAnalytics.onEvent(getContext(), "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(getContext(), "tab1_clear_btn");
        } else if (v.getId() == R.id.speak_language_layout) {
            resetImg();
            changeSpeakLanguage();
        } else if (v.getId() == R.id.input_type_layout) {
            resetImg();
            changeInputType();
        } else if (v.getId() == R.id.more_tools_layout || v.getId() == R.id.more_tools_layout_mic) {
            if (actionLayout.isShown()) {
                AnimationUtil.rotate(moreToolsImgMic,45,0);
                AnimationUtil.rotate(moreToolsImg,45,0);
                actionLayout.setVisibility(View.GONE);
            } else {
                AnimationUtil.rotate(moreToolsImgMic,0, 45);
                AnimationUtil.rotate(moreToolsImg,0, 45);
                actionLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void resetImg(){
        try {
            actionLayout.setVisibility(View.GONE);
            AnimationUtil.rotate(moreToolsImgMic,0,0);
            AnimationUtil.rotate(moreToolsImg,0,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showORCDialog() {
        actionLayout.setVisibility(View.GONE);
        if (mOrcHelper == null) {
            mOrcHelper = new OrcHelper(this, this, mProgressbarListener);
        }
        mOrcHelper.photoSelectDialog();
    }

    private void changeSpeakLanguage() {
        if (PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD).equals(XFUtil.VoiceEngineMD)) {
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(getContext(), getActivity().getResources().getString(R.string.speak_english));
        } else {
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD);
            ToastUtil.diaplayMesShort(getContext(), getActivity().getResources().getString(R.string.speak_chinese));
        }
        setSpeakLanguageTv();
        AVAnalytics.onEvent(getContext(), "tab1_lan_sbtn");
    }

    private void setSpeakLanguageTv() {
        speakLanguageTv.setText(XFUtil.getVoiceEngineText(PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD)));
    }

    private void changeInputType() {
        if (keybord_layout.isShown()) {
            showMicLayout();
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowTranKeybordLayout, false);
            hideIME();
        } else {
            showKeybordLayout();
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowTranKeybordLayout, true);
            showIME();
            input_et.requestFocus();
        }
    }

    private void showKeybordLayout() {
        input_type_btn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_mic));
        keybord_layout.setVisibility(View.VISIBLE);
        mic_layout.setVisibility(View.GONE);
    }

    private void showMicLayout() {
        input_type_btn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_keybord_btn));
        keybord_layout.setVisibility(View.GONE);
        mic_layout.setVisibility(View.VISIBLE);
    }

    public void autoClearAndautoPlay() {
        if (PlayUtil.getSP().getBoolean(KeyUtil.AutoClearInput, true)) {
            input_et.setText("");
        }
    }

    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(getContext(), toastString);
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void showIatDialog() {
        showIat();
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    public void showRationaleForRecord(final PermissionRequest request){
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
    public void showRecordDenied(){
        ToastUtil.diaplayMesShort(getContext(),"拒绝授权，将无法使用部分功能！");
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showCameraDenied(){
        ToastUtil.diaplayMesShort(getContext(),"拒绝授权，将无法使用部分功能！");
    }

    public void showIat() {
        try {
            if (!recognizer.isListening()) {
                record_layout.setVisibility(View.VISIBLE);
                input_et.setText("");
                voice_btn.setText(getActivity().getResources().getText(R.string.click_and_finish));
                XFUtil.showSpeechRecognizer(getContext(), PlayUtil.getSP(), recognizer, recognizerListener,
                        PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD));
            } else {
                finishRecord();
                recognizer.stopListening();
                showProgressbar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText(getActivity().getResources().getText(R.string.click_and_speak));
    }

    /**
     * 点击翻译之后隐藏输入法
     */
    private void hideIME() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(submit_btn_cover.getWindowToken(), 0);
    }

    /**
     * 点击编辑之后显示输入法
     */
    private void showIME() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
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
            showToast(getContext().getResources().getString(R.string.input_et_hint));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishEvent event){
        LogUtil.DefalutLog("FinishEvent");
        autoClearAndautoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mOrcHelper != null){
            mOrcHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void ShowResult(BaiduOcrRoot mBaiduOcrRoot) {
        showKeybordLayout();
        input_et.setText("");
        input_et.setText(CameraUtil.getOcrResult(mBaiduOcrRoot));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
