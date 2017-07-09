package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ScrollView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcDictionaryListAdapter;
import com.messi.languagehelper.bean.BaiduOcrRoot;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.DicHelperListener;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OrcResultListener;
import com.messi.languagehelper.util.CameraUtil;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.OrcHelper;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.views.DividerItemDecoration;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DictionaryFragmentOld extends Fragment implements OnClickListener,
        DictionaryTranslateListener, DicHelperListener, OrcResultListener {

    public static Dictionary mDictionaryBean;
    public static DictionaryFragmentOld mMainFragment;

    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;
    @BindView(R.id.clear_btn_layout)
    FrameLayout clear_btn_layout;
    @BindView(R.id.submit_btn_cover)
    FrameLayout submit_btn_cover;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.dic_result_layout)
    LinearLayout dicResultLayout;
    @BindView(R.id.cidian_result_layout)
    ScrollView cidianResultLayout;
    @BindView(R.id.input_et)
    EditText input_et;
    @BindView(R.id.cb_speak_language_ch)
    RadioButton cb_speak_language_ch;
    @BindView(R.id.cb_speak_language_en)
    RadioButton cb_speak_language_en;
    @BindView(R.id.voice_btn)
    Button voice_btn;
    @BindView(R.id.speak_round_layout)
    LinearLayout speak_round_layout;
    @BindView(R.id.photo_tran_btn)
    FrameLayout photoTranBtn;
    /**
     * record
     **/
    private RcDictionaryListAdapter mAdapter;
    private List<Dictionary> beans;
    // 识别对象
    private SpeechRecognizer recognizer;
    private Bundle bundle;
    private FragmentProgressbarListener mProgressbarListener;
    private Activity mActivity;
    private OrcHelper mOrcHelper;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onFinishRequest();
            if (msg.what == 1) {
                setData();
            } else {
                showToast(mActivity.getResources().getString(R.string.network_error));
            }
        }
    };

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

    public static DictionaryFragmentOld getInstance(Bundle bundle, Activity mActivity) {
        if (mMainFragment == null) {
            mMainFragment = new DictionaryFragmentOld();
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
        mActivity = activity;
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.DefalutLog("MainFragment-onCreateView");
        View view = inflater.inflate(R.layout.fragment_dictionary_old, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        recognizer = SpeechRecognizer.createRecognizer(mActivity, null);
        beans = new ArrayList<Dictionary>();
        beans.addAll(DataBaseUtil.getInstance().getDataListDictionary(0, Settings.offset));
        mAdapter = new RcDictionaryListAdapter(mActivity, beans, this);

        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(mActivity));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        mAdapter.setFooter(new Object());
        recent_used_lv.setAdapter(mAdapter);
        clear_btn_layout.setOnClickListener(this);
        submit_btn_cover.setOnClickListener(this);
        speak_round_layout.setOnClickListener(this);
        cb_speak_language_ch.setOnClickListener(this);
        cb_speak_language_en.setOnClickListener(this);
        photoTranBtn.setOnClickListener(this);
        initLanguage();
        isShowRecentList(true);
    }

    private void initLanguage() {
        if (PlayUtil.getSP().getString(KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD).equals(XFUtil.VoiceEngineMD)) {
            cb_speak_language_ch.setChecked(true);
            cb_speak_language_en.setChecked(false);
        } else {
            cb_speak_language_ch.setChecked(false);
            cb_speak_language_en.setChecked(true);
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
        if (Settings.isDictionaryFragmentNeedRefresh) {
            Settings.isDictionaryFragmentNeedRefresh = false;
            reloadData();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            submit();
            hideIME();
            AVAnalytics.onEvent(mActivity, "tab2_submit_btn");
        } else if (v.getId() == R.id.photo_tran_btn) {
            showORCDialog();
        } else if (v.getId() == R.id.speak_round_layout) {
            showIatDialog();
            AVAnalytics.onEvent(mActivity, "tab2_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(mActivity, "tab2_clear_btn");
        } else if (v.getId() == R.id.cb_speak_language_ch) {
            cb_speak_language_en.setChecked(false);
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_chinese));
            AVAnalytics.onEvent(mActivity, "tab2_zh_sbtn");
        } else if (v.getId() == R.id.cb_speak_language_en) {
            cb_speak_language_ch.setChecked(false);
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_english));
            AVAnalytics.onEvent(mActivity, "tab2_en_sbtn");
        }
    }

    private void showORCDialog() {
        if (mOrcHelper == null) {
            mOrcHelper = new OrcHelper(this, this, mProgressbarListener);
        }
        mOrcHelper.photoSelectDialog();
    }

    private void isShowRecentList(boolean value) {
        if (value) {
            recent_used_lv.setVisibility(View.VISIBLE);
            cidianResultLayout.setVisibility(View.GONE);
        } else {
            cidianResultLayout.setVisibility(View.VISIBLE);
            recent_used_lv.setVisibility(View.GONE);
        }
    }

    /**
     * send translate request
     * showapi dictionary api
     */
    private void RequestTranslateApiTask() {
        loadding();
        submit_btn_cover.setEnabled(false);
        TranslateUtil.Translate_init(mActivity, mHandler);
    }

    private void setData() {
        mDictionaryBean = (Dictionary) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        WXEntryActivity.dataMap.clear();
        setBean();
    }

    private void setBean() {
        isShowRecentList(false);
        cidianResultLayout.scrollTo(0, 0);
        DictionaryHelper.addDicContent(mActivity, dicResultLayout, mDictionaryBean, this);
        if (PlayUtil.getSP().getBoolean(KeyUtil.AutoClearInput, true)) {
            input_et.setText("");
        }
    }

    private void onFinishRequest() {
        finishLoadding();
        submit_btn_cover.setEnabled(true);
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
            voice_btn.setBackgroundColor(mActivity.getResources().getColor(R.color.none));
            voice_btn.setText(mActivity.getResources().getString(R.string.finish));
            speak_round_layout.setBackgroundResource(R.drawable.round_light_blue_bgl);
            XFUtil.showSpeechRecognizer(mActivity, PlayUtil.getSP(), recognizer, recognizerListener,
                    PlayUtil.getSP().getString(KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD));
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
        voice_btn.setText("");
        voice_btn.setBackgroundResource(R.drawable.ic_voice_padded_normal);
        speak_round_layout.setBackgroundResource(R.drawable.round_gray_bgl_old);
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
            if (",.?!;:'，。？！‘；：".contains(last)) {
                Settings.q = Settings.q.substring(0, Settings.q.length() - 1);
            }
            RequestTranslateApiTask();
        } else {
            showToast(mActivity.getResources().getString(R.string.input_et_hint_dictionary));
            finishLoadding();
        }
    }

    @Override
    public void showItem(Dictionary word) {
        mDictionaryBean = word;
        setBean();
    }

    private void reloadData() {
        loadding();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                beans.clear();
                beans.addAll(DataBaseUtil.getInstance().getDataListDictionary(0, Settings.offset));
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

    @Override
    public void playPcm(Dictionary mObject, boolean isPlayResult, String result) {
        playResult(mObject, isPlayResult, result);
    }

    private void playResult(final Dictionary mBean, boolean isPlayResult, String result) {
        String filepath = "";
        String speakContent = "";
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        if (isPlayResult) {
            if (TextUtils.isEmpty(mBean.getResultVoiceId())) {
                mBean.setResultVoiceId(System.currentTimeMillis() + 5 + "");
            }
            filepath = path + mBean.getResultVoiceId() + ".pcm";
            speakContent = result;
        } else {
            if (TextUtils.isEmpty(mBean.getQuestionVoiceId())) {
                mBean.setQuestionVoiceId(System.currentTimeMillis() + "");
            }
            filepath = path + mBean.getQuestionVoiceId() + ".pcm";
            speakContent = mBean.getWord_name();
        }
        PlayUtil.play(filepath, speakContent, null,
                new SynthesizerListener() {
                    @Override
                    public void onSpeakResumed() {
                    }

                    @Override
                    public void onSpeakProgress(int arg0, int arg1, int arg2) {
                    }

                    @Override
                    public void onSpeakPaused() {
                    }

                    @Override
                    public void onSpeakBegin() {
                        finishLoadding();
                    }

                    @Override
                    public void onCompleted(SpeechError arg0) {
                        LogUtil.DefalutLog("---onCompleted");
                        if (arg0 != null) {
                            ToastUtil.diaplayMesShort(mActivity, arg0.getErrorDescription());
                        }
                        DataBaseUtil.getInstance().update(mBean);
                    }

                    @Override
                    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                        if (arg0 < 10) {
                            loadding();
                        }
                    }

                    @Override
                    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                    }
                });
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
        mOrcHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void ShowResult(BaiduOcrRoot mBaiduOcrRoot) {
        input_et.setText("");
        input_et.setText(CameraUtil.getOcrResult(mBaiduOcrRoot));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
