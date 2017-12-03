package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.adapter.RcTranslateListAdapter;
import com.messi.languagehelper.bean.BaiduOcrRoot;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OnTranslateFinishListener;
import com.messi.languagehelper.impl.OrcResultListener;
import com.messi.languagehelper.util.AnimationUtil;
import com.messi.languagehelper.util.CameraUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.OrcHelper;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.views.DividerItemDecoration;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.youdao.sdk.ydtranslate.Translate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment implements OnClickListener, OrcResultListener {

    public static MainFragment mMainFragment;
    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;
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
    @BindView(R.id.action_setting)
    CardView actionSetting;
    @BindView(R.id.action_collected)
    CardView actionCollected;
    @BindView(R.id.action_layout)
    LinearLayout actionLayout;
    @BindView(R.id.bottom_layout)
    CardView bottomLayout;
    @BindView(R.id.action_col_all)
    ImageView action_col_all;
    @BindView(R.id.action_photo_tran_btn)
    CardView actionPhotoTranBtn;
    @BindView(R.id.more_tools_img_mic)
    ImageView moreToolsImgMic;
    @BindView(R.id.more_tools_img)
    ImageView moreToolsImg;

    //translate
    private record currentDialogBean;
    private RcTranslateListAdapter mAdapter;
    private List<record> beans;
    private String dstString = "";
    // 识别对象
    private SpeechRecognizer recognizer;
    //合成对象.

    private Bundle bundle;
    private FragmentProgressbarListener mProgressbarListener;

    private boolean isShowCollected = true;
    private Activity mActivity;
    private OrcHelper mOrcHelper;

    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(mActivity, err.getErrorDescription());
            finishLoadding();
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            loadding();
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

    public static MainFragment getInstance(Bundle bundle, Activity mActivity) {
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            mMainFragment.bundle = bundle;
            mMainFragment.setmActivity(mActivity);
        }
        return mMainFragment;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = activity;
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
        recognizer = SpeechRecognizer.createRecognizer(mActivity, null);
        beans = new ArrayList<record>();
        beans.addAll(DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset));
        boolean IsHasShowBaiduMessage = PlayUtil.getSP().getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
        if (!IsHasShowBaiduMessage) {
            initSample();
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsHasShowBaiduMessage, true);
        }
        mAdapter = new RcTranslateListAdapter(beans);
        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(mActivity));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        recent_used_lv.setAdapter(mAdapter);

        setSpeakLanguageTv();
        submit_btn_cover.setOnClickListener(this);
        speakLanguageLayout.setOnClickListener(this);
        voice_btn_cover.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);
        input_layout.setOnClickListener(this);
        input_type_layout.setOnClickListener(this);
        more_tools_layout.setOnClickListener(this);
        more_tools_layout_mic.setOnClickListener(this);
        actionLayout.setOnClickListener(this);
        actionSetting.setOnClickListener(this);
        actionCollected.setOnClickListener(this);
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
    }

    private void initSample() {
        record sampleBean = new record("Click the mic to speak", "点击话筒说话");
        DataBaseUtil.getInstance().insert(sampleBean);
        beans.add(0, sampleBean);
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
        if (Settings.isMainFragmentNeedRefresh) {
            Settings.isMainFragmentNeedRefresh = false;
            reloadData();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            submit();
            AVAnalytics.onEvent(mActivity, "tab1_submit_btn");
        } else if (v.getId() == R.id.action_photo_tran_btn) {
            resetImg();
            showORCDialog();
        } else if (v.getId() == R.id.voice_btn_cover) {
            showIatDialog();
            AVAnalytics.onEvent(mActivity, "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(mActivity, "tab1_clear_btn");
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
        } else if (v.getId() == R.id.action_setting) {
            resetImg();
            showMoreTools();
        } else if (v.getId() == R.id.action_collected) {
            resetImg();
            if (isShowCollected) {
                action_col_all.setImageResource(R.drawable.ic_uncollected_grey);
            } else {
                action_col_all.setImageResource(R.drawable.ic_collected_grey);
            }
            getCollectedDataTask();
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

    private void showORCDialog() {
        actionLayout.setVisibility(View.GONE);
        if (mOrcHelper == null) {
            mOrcHelper = new OrcHelper(this, this, mProgressbarListener);
        }
        mOrcHelper.photoSelectDialog();
    }

    private void changeSpeakLanguage() {
        if (PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD).equals(XFUtil.VoiceEngineMD)) {
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_english));
        } else {
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_chinese));
        }
        setSpeakLanguageTv();
        AVAnalytics.onEvent(mActivity, "tab1_lan_sbtn");
    }

    private void setSpeakLanguageTv() {
        speakLanguageTv.setText(XFUtil.getVoiceEngineText(PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD)));
    }

    private void changeInputType() {
        if (keybord_layout.isShown()) {
            showMicLayout();
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowTranKeybordLayout, false);
            hideIME();
        } else {
            showKeybordLayout();
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowTranKeybordLayout, true);
            showIME();
            input_et.requestFocus();
        }
    }

    private void showKeybordLayout() {
        input_type_btn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_mic));
        keybord_layout.setVisibility(View.VISIBLE);
        mic_layout.setVisibility(View.GONE);
    }

    private void showMicLayout() {
        input_type_btn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_keybord_btn));
        keybord_layout.setVisibility(View.GONE);
        mic_layout.setVisibility(View.VISIBLE);
    }

    private void translateController(){
        if(NetworkUtil.isNetworkConnected(getContext())){
            LogUtil.DefalutLog("online");
            try {
                RequestJinShanNewAsyncTask();
            } catch (Exception e) {
                LogUtil.DefalutLog("online exception");
                e.printStackTrace();
            }
        }else {
            LogUtil.DefalutLog("offline");
            translateOffline();
        }
    }

    private void translateOffline(){
        loadding();
        Observable.create(new ObservableOnSubscribe<Translate>() {
            @Override
            public void subscribe(ObservableEmitter<Translate> e) throws Exception {
                TranslateUtil.offlineTranslate(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translate>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Translate translate) {
                        parseOfflineData(translate);
                    }
                    @Override
                    public void onError(Throwable e) {
                        onComplete();
                    }
                    @Override
                    public void onComplete() {
                        finishLoadding();
                    }
                });
    }

    private void parseOfflineData(Translate translate){
        if(translate != null){
            if(translate.getErrorCode() == 0){
                StringBuilder sb = new StringBuilder();
                TranslateUtil.addSymbol(translate,sb);
                for(String tran : translate.getTranslations()){
                    sb.append(tran);
                    sb.append("\n");
                }
                currentDialogBean = new record(sb.substring(0, sb.lastIndexOf("\n")), Settings.q);
                insertData();
                autoClearAndautoPlay();
            }
        }else{
            showToast("没找到离线词典，请到更多页面下载！");
        }
    }

    /**
     * send translate request
     */
    private void RequestJinShanNewAsyncTask() throws Exception {
        loadding();
        submit_btn_cover.setEnabled(false);
        TranslateUtil.Translate(new OnTranslateFinishListener() {
            @Override
            public void OnFinishTranslate(record mRecord) {
                onFinishRequest();
                if(mRecord == null){
                    showToast(mActivity.getResources().getString(R.string.network_error));
                }else {
                    currentDialogBean = mRecord;
                    insertData();
                    autoClearAndautoPlay();
                }
            }
        });
    }

    private void onFinishRequest() {
        finishLoadding();
        submit_btn_cover.setEnabled(true);
    }

    public void insertData() {
        long newRowId = DataBaseUtil.getInstance().insert(currentDialogBean);
        mAdapter.addEntity(0, currentDialogBean);
        recent_used_lv.scrollToPosition(0);
    }

    public void autoClearAndautoPlay() {
        if (PlayUtil.getSP().getBoolean(KeyUtil.AutoClearInput, true)) {
            input_et.setText("");
        }
        if (PlayUtil.getSP().getBoolean(KeyUtil.AutoPlayResult, false)) {
            AutoPlayWaitTask();
        }
    }

    private void autoPlay() {
        View mView = recent_used_lv.getChildAt(0);
        final FrameLayout record_answer_cover = (FrameLayout) mView.findViewById(R.id.record_answer_cover);
        record_answer_cover.post(new Runnable() {
            @Override
            public void run() {
                record_answer_cover.performClick();
            }
        });
    }

    /**
     * toast message
     *
     * @param toastString
     */
    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(mActivity, toastString);
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        Settings.verifyStoragePermissions(mActivity, Settings.PERMISSIONS_RECORD_AUDIO);
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setText(mActivity.getResources().getText(R.string.click_and_finish));
            XFUtil.showSpeechRecognizer(mActivity, PlayUtil.getSP(), recognizer, recognizerListener,
                    PlayUtil.getSP().getString(KeyUtil.TranUserSelectLanguage, XFUtil.VoiceEngineMD));
        } else {
            finishRecord();
            recognizer.stopListening();
            loadding();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText(mActivity.getResources().getText(R.string.click_and_speak));
    }

    /**
     * 点击翻译之后隐藏输入法
     */
    private void hideIME() {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(submit_btn_cover.getWindowToken(), 0);
    }

    /**
     * 点击编辑之后显示输入法
     */
    private void showIME() {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void loadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.showProgressbar();
        }
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void finishLoadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.hideProgressbar();
        }
    }

    /**
     * submit request task
     */
    private void submit() {
        Settings.q = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(Settings.q)) {
            String last = Settings.q.substring(Settings.q.length() - 1);
            if (",.!;:'，。！‘；：".contains(last)) {
                Settings.q = Settings.q.substring(0, Settings.q.length() - 1);
            }
            translateController();
        } else {
            showToast(mActivity.getResources().getString(R.string.input_et_hint));
            finishLoadding();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer = null;
        }
    }

    private void getCollectedDataTask() {
        loadding();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                beans.clear();
                if (isShowCollected) {
                    isShowCollected = false;
                    beans.addAll(DataBaseUtil.getInstance().getDataListCollected(0, Settings.offset));
                } else {
                    isShowCollected = true;
                    beans.addAll(DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset));
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        finishLoadding();
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void reloadData() {
        loadding();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                beans.clear();
                beans.addAll(DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset));
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        finishLoadding();
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void AutoPlayWaitTask() {
        Observable.just("1")
                .delay(100, TimeUnit.MILLISECONDS)//延迟发送
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        autoPlay();
                    }
                });
    }

    //more tools
    private void showMoreTools() {
        hideIME();
        final DialogPlus dialog = DialogPlus.newDialog(mActivity)
                .setContentHolder(new ViewHolder(R.layout.tran_more_tools_layout))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setContentHeight(900)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .create();
        initMoreToolsView(dialog.getHolderView(), dialog);
        dialog.show();
    }

    private void initMoreToolsView(View view, final DialogPlus dialog) {
        final TextView seekbar_text = (TextView) view.findViewById(R.id.seekbar_text);
        SeekBar seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        FrameLayout auto_play = (FrameLayout) view.findViewById(R.id.setting_auto_play);
        final CheckBox auto_play_cb = (CheckBox) view.findViewById(R.id.setting_auto_play_cb);
        FrameLayout auto_clear = (FrameLayout) view.findViewById(R.id.setting_auto_clear);
        final CheckBox auto_clear_cb = (CheckBox) view.findViewById(R.id.setting_auto_clear_cb);
        FrameLayout clear_all = (FrameLayout) view.findViewById(R.id.setting_clear_all);

        seekbar_text.setText(this.getResources().getString(R.string.play_speed_text) +
                PlayUtil.getSP().getInt(getString(R.string.preference_key_tts_speed), 50));
        seekbar.setProgress(PlayUtil.getSP().getInt(getString(R.string.preference_key_tts_speed), 50));
        boolean autoPlay = PlayUtil.getSP().getBoolean(KeyUtil.AutoPlayResult, false);
        boolean AutoClear = PlayUtil.getSP().getBoolean(KeyUtil.AutoClearTran, false);
        auto_play_cb.setChecked(autoPlay);
        auto_clear_cb.setChecked(AutoClear);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_text.setText(mActivity.getResources().getString(R.string.play_speed_text) + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.saveSharedPreferences(PlayUtil.getSP(),
                        getString(R.string.preference_key_tts_speed),
                        seekBar.getProgress());
                AVAnalytics.onEvent(mActivity, "tran_tools_change_speed");
            }
        });

        auto_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_play_cb.setChecked(!auto_play_cb.isChecked());
                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AutoPlayResult, auto_play_cb.isChecked());
                AVAnalytics.onEvent(mActivity, "tran_tools_auto_play");
            }
        });
        auto_play_cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AutoPlayResult, auto_play_cb.isChecked());
                AVAnalytics.onEvent(mActivity, "tran_tools_auto_play");
            }
        });

        auto_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_clear_cb.setChecked(!auto_clear_cb.isChecked());
                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AutoClearTran, auto_clear_cb.isChecked());
                AVAnalytics.onEvent(mActivity, "tran_tools_auto_clear");
            }
        });
        auto_clear_cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AutoClearTran, auto_clear_cb.isChecked());
                AVAnalytics.onEvent(mActivity, "tran_tools_auto_clear");
            }
        });

        clear_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseUtil.getInstance().clearAllTran();
                SDCardUtil.deleteOldFile();
                dialog.dismiss();
                beans.clear();
                mAdapter.notifyDataSetChanged();
                ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.clear_success));
                AVAnalytics.onEvent(mActivity, "setting_pg_clear_all");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOrcHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void ShowResult(BaiduOcrRoot mBaiduOcrRoot) {
        showKeybordLayout();
        input_et.setText("");
        input_et.setText(CameraUtil.getOcrResult(mBaiduOcrRoot));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
