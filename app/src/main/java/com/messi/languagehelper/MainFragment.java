package com.messi.languagehelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.RcTranslateListAdapter;
import com.messi.languagehelper.dao.Iciba;
import com.messi.languagehelper.dao.IcibaNew;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFragment extends Fragment implements OnClickListener {

    public static int speed;
    public static boolean isSpeakYueyuNeedUpdate;
    public static boolean isRefresh;
    public static MainFragment mMainFragment;
    private EditText input_et;
    private FrameLayout submit_btn_cover;
    private FrameLayout photo_tran_btn;
    private FrameLayout clear_btn_layout;
    private Button voice_btn;
    private LinearLayout speak_round_layout;
    private RadioButton cb_speak_language_ch, cb_speak_language_en;
    private RecyclerView recent_used_lv;
    /**
     * record
     **/
    private LinearLayout record_layout;
    private ImageView record_anim_img;
    private record currentDialogBean;
    private LayoutInflater mInflater;
    private RcTranslateListAdapter mAdapter;
    private List<record> beans;
    private String dstString = "";
    private Animation fade_in, fade_out;
    // 识别对象
    private SpeechRecognizer recognizer;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    //合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;
    private boolean isSpeakYueyu;
    private Bundle bundle;
    private View view;
    private FragmentProgressbarListener mProgressbarListener;
    private boolean AutoClearInputAfterFinish;

    private String mCurrentPhotoPath;
    private Activity mActivity;
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
        mActivity = activity;
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_translate, null);
        LogUtil.DefalutLog("MainFragment-onCreateView");
        init();
        return view;
    }

    private void init() {
        mInflater = LayoutInflater.from(mActivity);
        mSharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mActivity, null);
        recognizer = SpeechRecognizer.createRecognizer(mActivity, null);

        recent_used_lv = (RecyclerView) view.findViewById(R.id.recent_used_lv);
        input_et = (EditText) view.findViewById(R.id.input_et);
        submit_btn_cover = (FrameLayout) view.findViewById(R.id.submit_btn_cover);
        photo_tran_btn = (FrameLayout) view.findViewById(R.id.photo_tran_btn);
        cb_speak_language_ch = (RadioButton) view.findViewById(R.id.cb_speak_language_ch);
        cb_speak_language_en = (RadioButton) view.findViewById(R.id.cb_speak_language_en);
        speak_round_layout = (LinearLayout) view.findViewById(R.id.speak_round_layout);
        clear_btn_layout = (FrameLayout) view.findViewById(R.id.clear_btn_layout);
        record_layout = (LinearLayout) view.findViewById(R.id.record_layout);
        record_anim_img = (ImageView) view.findViewById(R.id.record_anim_img);
        fade_in = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(mActivity, R.anim.fade_out);
        voice_btn = (Button) view.findViewById(R.id.voice_btn);

        beans = new ArrayList<record>();
        beans.addAll(DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset));
        AutoClearInputAfterFinish = mSharedPreferences.getBoolean(KeyUtil.AutoClearInputAfterFinish, true);
        boolean IsHasShowBaiduMessage = mSharedPreferences.getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
        if (!IsHasShowBaiduMessage) {
            initSample();
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsHasShowBaiduMessage, true);
        }
        mAdapter = new RcTranslateListAdapter(mSpeechSynthesizer, mSharedPreferences,beans);
        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(mActivity));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        mAdapter.setFooter(new Object());
        recent_used_lv.setAdapter(mAdapter);

        initLanguage();
        photo_tran_btn.setOnClickListener(this);
        submit_btn_cover.setOnClickListener(this);
        cb_speak_language_ch.setOnClickListener(this);
        cb_speak_language_en.setOnClickListener(this);
        speak_round_layout.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);

        getAccent();
        speed = mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50);
    }

    private void initSample() {
        record sampleBean = new record("Click the mic to speak", "点击话筒说话");
        DataBaseUtil.getInstance().insert(sampleBean);
        beans.add(0, sampleBean);
    }

    private void initLanguage() {
        if (mSharedPreferences != null) {
            String selectedLanguage = getSpeakLanguage();
            if (selectedLanguage.equals(XFUtil.VoiceEngineCH)) {
                XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineCH);
                cb_speak_language_ch.setChecked(true);
                cb_speak_language_en.setChecked(false);
            } else {
                XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineEN);
                cb_speak_language_ch.setChecked(false);
                cb_speak_language_en.setChecked(true);
            }
        }
    }

    private void resetLanguage() {
        if (mSharedPreferences != null && cb_speak_language_ch != null) {
            if (cb_speak_language_ch.isChecked()) {
                XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineCH);
            } else {
                XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineEN);
            }
            AutoClearInputAfterFinish = mSharedPreferences.getBoolean(KeyUtil.AutoClearInputAfterFinish, true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            hideIME();
            submit();
            AVAnalytics.onEvent(mActivity, "tab1_submit_btn");
        } else if (v.getId() == R.id.photo_tran_btn) {
//			photoSelectDialog();
        } else if (v.getId() == R.id.speak_round_layout) {
            showIatDialog();
            AVAnalytics.onEvent(mActivity, "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(mActivity, "tab1_clear_btn");
        } else if (v.getId() == R.id.cb_speak_language_ch) {
            cb_speak_language_en.setChecked(false);
            XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineCH);
            if (isSpeakYueyu) {
                ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_chinese));
            } else {
                ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_chinese));
            }
            AVAnalytics.onEvent(mActivity, "tab1_zh_sbtn");
        } else if (v.getId() == R.id.cb_speak_language_en) {
            cb_speak_language_ch.setChecked(false);
            XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_english));
            AVAnalytics.onEvent(mActivity, "tab1_en_sbtn");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("MainFragment-setUserVisibleHint");
        if (isVisibleToUser) {
            if (isRefresh) {
                isRefresh = false;
                new WaitTask().execute();
            }
            resetLanguage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.DefalutLog("MainFragment-onResume");
        if (isSpeakYueyuNeedUpdate) {
            getAccent();
            if (cb_speak_language_ch.isChecked()) {
                XFUtil.setSpeakLanguage(mActivity, mSharedPreferences, XFUtil.VoiceEngineCH);
            }
            isSpeakYueyuNeedUpdate = false;
        }
        setUserVisibleHint(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.DefalutLog("MainFragment-onPause");
        if (mSharedPreferences != null && cb_speak_language_ch != null) {
            if (cb_speak_language_ch.isChecked()) {
                Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.UserSelectLanguage, XFUtil.VoiceEngineCH);
            } else {
                Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.UserSelectLanguage, XFUtil.VoiceEngineEN);
            }
        }
    }

    private void getAccent() {
        isSpeakYueyu = mSharedPreferences.getBoolean(KeyUtil.SpeakPutonghuaORYueyu, false);
        if (isSpeakYueyu) {
            cb_speak_language_ch.setText("粤语");
        } else {
            cb_speak_language_ch.setText(mActivity.getResources().getString(R.string.chinese));
        }
    }

    /**
     * get defalut speaker
     *
     * @return
     */
    private String getSpeakLanguage() {
        return mSharedPreferences.getString(KeyUtil.UserSelectLanguage, XFUtil.VoiceEngineCH);
    }

    /**
     * send translate request
     */
    private void RequestJinShanNewAsyncTask() {
        loadding();
        submit_btn_cover.setEnabled(false);
        LanguagehelperHttpClient.postIcibaNew(new UICallback(mActivity) {
            @Override
            public void onResponsed(String mResult) {
                try {
                    if (!TextUtils.isEmpty(mResult)) {
                        if (JsonParser.isJson(mResult)) {
                            LogUtil.DefalutLog(mResult);
                            setJinshanNewApiData(JSON.parseObject(mResult, IcibaNew.class));
                        } else {
                            RequestAsyncTask();
                        }
                    } else {
                        RequestAsyncTask();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                showToast(mActivity.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onFinishRequest();
            }
        });
    }

    private void setJinshanNewApiData(IcibaNew mIciba) {
        try {
            if (mIciba != null && mIciba.getContent() != null) {
                if (mIciba.getStatus().equals("0")) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sbplay = new StringBuilder();
                    if (!TextUtils.isEmpty(mIciba.getContent().getPh_en())) {
                        sb.append("英[");
                        sb.append(mIciba.getContent().getPh_en());
                        sb.append("]    ");
                    }
                    if (!TextUtils.isEmpty(mIciba.getContent().getPh_am())) {
                        sb.append("美[");
                        sb.append(mIciba.getContent().getPh_am());
                        sb.append("]");
                    }
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    if (mIciba.getContent().getWord_mean() != null) {
                        for (String item : mIciba.getContent().getWord_mean()) {
                            sb.append(item.trim());
                            sb.append("\n");
                            sbplay.append(item);
                            sbplay.append(",");
                        }
                    }
                    String resultStr = sbplay.toString();
                    resultStr = resultStr.replace("n.", "");
                    resultStr = resultStr.replace("adj.", "");
                    resultStr = resultStr.replace("adv.", "");
                    resultStr = resultStr.replace("vi.", "");
                    resultStr = resultStr.replace("vt.", "");
                    resultStr = resultStr.replace("v.", "");
                    currentDialogBean = new record(sb.substring(0, sb.lastIndexOf("\n")), Settings.q);
                    currentDialogBean.setBackup1(resultStr);
                    if (!TextUtils.isEmpty(mIciba.getContent().getPh_tts_mp3())) {
                        currentDialogBean.setBackup3(mIciba.getContent().getPh_tts_mp3());
                    }
                    insertData();
                    autoClearAndautoPlay();
                } else if (mIciba.getStatus().equals("1")) {
                    currentDialogBean = new record(mIciba.getContent().getOut().replaceAll("<br/>", "").trim(), Settings.q);
                    insertData();
                    autoClearAndautoPlay();
                } else {
                    RequestJinShanAsyncTask();
                }
            } else {
                RequestJinShanAsyncTask();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * send translate request
     */
    private void RequestJinShanAsyncTask() {
        loadding();
        submit_btn_cover.setEnabled(false);
        LanguagehelperHttpClient.postIciba(new UICallback(mActivity) {
            @Override
            public void onResponsed(String mResult) {
                try {
                    if (!TextUtils.isEmpty(mResult)) {
                        if (JsonParser.isJson(mResult)) {
                            setJinShanResult(mResult);
                        } else {
                            RequestAsyncTask();
                        }
                    } else {
                        RequestAsyncTask();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                showToast(mActivity.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onFinishRequest();
            }
        });
    }

    private void onFinishRequest() {
        finishLoadding();
        submit_btn_cover.setEnabled(true);
    }

    private void setJinShanResult(String responseString) {
        try {
            Iciba mIciba = JSON.parseObject(responseString, Iciba.class);
            if (mIciba != null) {
                if (!TextUtils.isEmpty(mIciba.getRetcopy())) {
                    currentDialogBean = new record(mIciba.getRetcopy(), Settings.q);
                    insertData();
                    autoClearAndautoPlay();
                    LogUtil.DefalutLog("ciba---getRetcopy---mDataBaseUtil:" + currentDialogBean.toString());
                } else if (!TextUtils.isEmpty(mIciba.getRet())) {
                    String result = getHtmlContext(mIciba.getRet());
                    if (!TextUtils.isEmpty(result)) {
                        currentDialogBean = new record(result, Settings.q);
                        insertData();
                        autoClearAndautoPlay();
                        LogUtil.DefalutLog("ciba---getRet---mDataBaseUtil:" + currentDialogBean.toString());
                    } else {
                        LogUtil.DefalutLog("ciba getRet error,change to baidu translate");
                        RequestAsyncTask();
                    }
                } else {
                    LogUtil.DefalutLog("ciba error,change to baidu translate");
                    RequestAsyncTask();
                }
            } else {
                RequestAsyncTask();
            }
        } catch (Exception e) {
            RequestAsyncTask();
            e.printStackTrace();
        }
    }

    public void insertData() {
        long newRowId = DataBaseUtil.getInstance().insert(currentDialogBean);
        mAdapter.addEntity(0, currentDialogBean);
        recent_used_lv.scrollToPosition(0);
    }

    public void autoClearAndautoPlay() {
        if (AutoClearInputAfterFinish) {
            input_et.setText("");
        }
        if (mSharedPreferences.getBoolean(KeyUtil.AutoPlayResult, false)) {
            new AutoPlayWaitTask().execute();
        }
    }

    public String getHtmlContext(String html) {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("<span class=\"dd\">([^</span>]*)");//匹配<title>开头，</title>结尾的文档
        Matcher m = p.matcher(html);//开始编译
        int count = 0;
        while (m.find()) {
            if (count > 0) {
                sb.append("\n");
            }
            sb.append(m.group(1).trim());//获取被匹配的部分
            count++;
        }

        return sb.toString();
    }

    private void RequestAsyncTask() {
        loadding();
        submit_btn_cover.setEnabled(false);
        LanguagehelperHttpClient.postBaidu(new UICallback(mActivity) {
            @Override
            public void onFailured() {
                showToast(mActivity.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onFinishRequest();
            }

            @Override
            public void onResponsed(String responseString) {
                if (!TextUtils.isEmpty(responseString)) {
                    dstString = JsonParser.getTranslateResult(responseString);
                    if (dstString.contains("error_msg:")) {
                        showToast(dstString);
                    } else {
                        currentDialogBean = new record(dstString, Settings.q);
                        insertData();
                        autoClearAndautoPlay();
                        LogUtil.DefalutLog("baidu---mDataBaseUtil:" + currentDialogBean.toString());
                    }
                } else {
                    showToast(mActivity.getResources().getString(
                            R.string.network_error));
                }
            }
        });
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
        Settings.verifyStoragePermissions(mActivity,Settings.PERMISSIONS_RECORD_AUDIO);
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setBackgroundColor(mActivity.getResources().getColor(R.color.none));
            voice_btn.setText(mActivity.getResources().getString(R.string.finish));
            speak_round_layout.setBackgroundResource(R.drawable.round_light_blue_bgl);
            XFUtil.showSpeechRecognizer(mActivity, mSharedPreferences, recognizer, recognizerListener);
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
        speak_round_layout.setBackgroundResource(R.drawable.round_gray_bgl);
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
            RequestJinShanNewAsyncTask();
        } else {
            showToast(mActivity.getResources().getString(R.string.input_et_hint));
            finishLoadding();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer = null;
        }
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer = null;
        }
        if (mAdapter != null) {
            mAdapter.stopPlay();
        }
        LogUtil.DefalutLog("MainFragment-onDestroy");
    }

    class WaitTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadding();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                beans.clear();
                beans.addAll(DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            finishLoadding();
            mAdapter.notifyDataSetChanged();
        }
    }

    class AutoPlayWaitTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            autoPlay();
        }
    }

//	private void photoSelectDialog(){
//		String[] titles = {getResources().getString(R.string.take_photo),getResources().getString(R.string.photo_album)};
//		PopDialog mPhonoSelectDialog = new PopDialog(getContext(),titles);
//		mPhonoSelectDialog.setListener(new PopViewItemOnclickListener() {
//			@Override
//			public void onSecondClick(View v) {
//				getImageFromAlbum();
//			}
//			@Override
//			public void onFirstClick(View v) {
//				getImageFromCamera();
//			}
//		});
//		mPhonoSelectDialog.show();
//	}
//
//	public void getImageFromAlbum() {
//		Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");//相片类型
//        startActivityForResult(intent, CameraUtil.REQUEST_CODE_PICK_IMAGE);
//    }
//
//	public void getImageFromCamera() {
//		String state = Environment.getExternalStorageState();
//		if (state.equals(Environment.MEDIA_MOUNTED)) {
//			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
//				File photoFile = null;
//		        try {
//		            photoFile = CameraUtil.createImageFile();
//		            mCurrentPhotoPath = photoFile.getAbsolutePath();
//		        } catch (IOException ex) {
//		            ex.printStackTrace();
//		        }
//		        if (photoFile != null) {
//		            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
//		            startActivityForResult(takePictureIntent, CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA);
//		        }
//			} else {
//				ToastUtil.diaplayMesShort(getContext(), "请确认已经插入SD卡");
//			}
//		}
//	}
//
//	public void doCropPhoto(Uri uri) {
//		File photoTemp = null;
//        try {
//        	photoTemp = new File(CameraUtil.createTempFile());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		intent.putExtra("scale", true);
//		intent.putExtra("return-data", false);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		intent.putExtra("noFaceDetection",  false);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoTemp));
//		startActivityForResult(intent, CameraUtil.PHOTO_PICKED_WITH_DATA);
//	}
//
//	 @Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == CameraUtil.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//			if(data != null){
//				Uri uri = data.getData();
//				if(uri != null){
//					doCropPhoto(uri);
//				}
//			}
//		} else if (requestCode == CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA && resultCode == Activity.RESULT_OK) {
//			File f = new File(mCurrentPhotoPath);
//    	    Uri contentUri = Uri.fromFile(f);
//    	    doCropPhoto(contentUri);
//		}else if (requestCode == CameraUtil.PHOTO_PICKED_WITH_DATA && resultCode == Activity.RESULT_OK) {
//			sendBaiduOCR();
//		}
//	}
//
//	public void sendBaiduOCR(){
//		try {
//			loadding();
//			LanguagehelperHttpClient.postBaiduOCR(CameraUtil.createTempFile(), new UICallback(mActivity){
//				@Override
//				public void onResponsed(String responseString){
//					if (!TextUtils.isEmpty(responseString)) {
//						if(JsonParser.isJson(responseString)){
//							BaiduOcrRoot mBaiduOcrRoot = JSON.parseObject(responseString, BaiduOcrRoot.class);
//							if(mBaiduOcrRoot.getErrNum().equals("0")){
//								input_et.setText("");
//								input_et.setText(CameraUtil.getOcrResult(mBaiduOcrRoot));
//							}else{
//								ToastUtil.diaplayMesShort(getContext(), mBaiduOcrRoot.getErrMsg());
//							}
//						}else{
//							showToast(mActivity.getResources().getString(R.string.server_error));
//						}
//					}
//				}
//				@Override
//				public void onFailured() {
//					showToast(mActivity.getResources().getString(R.string.network_error));
//				}
//				@Override
//				public void onFinished() {
//					finishLoadding();
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
