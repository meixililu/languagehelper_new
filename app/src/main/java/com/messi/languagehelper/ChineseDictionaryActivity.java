package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.okhttp.Response;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ShareUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ChineseDictionaryActivity extends BaseActivity {

    @BindView(R.id.btn_bushou)
    TextView btnBushou;
    @BindView(R.id.btn_pinyin)
    TextView btnPinyin;
    @BindView(R.id.input_et)
    EditText input_et;
    @BindView(R.id.submit_btn)
    FrameLayout submit_btn;
    @BindView(R.id.clear_btn_layout)
    FrameLayout clear_btn_layout;
    @BindView(R.id.question_tv)
    TextView question_tv;
    @BindView(R.id.question_tv_cover)
    FrameLayout question_tv_cover;
    @BindView(R.id.result_tv)
    TextView result_tv;
    @BindView(R.id.result_tv_cover)
    FrameLayout result_tv_cover;
    @BindView(R.id.copy_btn)
    FrameLayout copy_btn;
    @BindView(R.id.share_btn)
    FrameLayout share_btn;
    @BindView(R.id.chdic_sv)
    ScrollView chdic_sv;
    @BindView(R.id.voice_btn)
    Button voice_btn;
    @BindView(R.id.speak_round_layout)
    LinearLayout speak_round_layout;
    @BindView(R.id.layout_bottom)
    RelativeLayout layout_bottom;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    private String word;

    // 识别对象
    private SpeechRecognizer recognizer;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    //合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;

    private FragmentProgressbarListener mProgressbarListener;
    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(ChineseDictionaryActivity.this, err.getErrorDescription());
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ch_dictionary);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.style6_color1);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_two));
        mSharedPreferences = getSharedPreferences(getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
    }



    private void play(String content) {
        if (!mSpeechSynthesizer.isSpeaking()) {
            XFUtil.showSpeechSynthesizer(
                    this,
                    mSharedPreferences,
                    mSpeechSynthesizer,
                    content,
                    new SynthesizerListener() {
                        @Override
                        public void onSpeakBegin() {

                        }

                        @Override
                        public void onBufferProgress(int i, int i1, int i2, String s) {

                        }

                        @Override
                        public void onSpeakPaused() {

                        }

                        @Override
                        public void onSpeakResumed() {

                        }

                        @Override
                        public void onSpeakProgress(int i, int i1, int i2) {

                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {

                        }

                        @Override
                        public void onEvent(int i, int i1, int i2, Bundle bundle) {

                        }
                    });
        } else {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    private void toPinyinActivity() {
        Intent intent = new Intent(this, ChDicBushouPinyinActivity.class);
        intent.putExtra(KeyUtil.CHDicType, ChDicBushouPinyinActivity.pinyin);
        startActivity(intent);
    }

    private void toBushouActivity() {
        Intent intent = new Intent(this, ChDicBushouPinyinActivity.class);
        intent.putExtra(KeyUtil.CHDicType, ChDicBushouPinyinActivity.bushou);
        startActivity(intent);
    }

    private void copy() {
        String content = question_tv.getText().toString() + result_tv.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            ShareUtil.copy(this, content);
        }
    }

    private void share() {
        String content = question_tv.getText().toString() + result_tv.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            ShareUtil.shareText(this, content);
        }
    }

    private void onFinishRequest() {
        hideProgressbar();
        submit_btn.setEnabled(true);
    }

    private void RequestBaiduApi() {
        showProgressbar();
        submit_btn.setEnabled(false);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String url = Settings.CHDicBaiduApi + word;
                Response mResponse = LanguagehelperHttpClient.get(url);
                if (mResponse != null && mResponse.isSuccessful()) {
                    e.onNext(HtmlParseUtil.parseCHDicBaiduHtml(mResponse.body().string()));
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
                        chdic_sv.scrollTo(0, 0);
                        input_et.setText("");
                        question_tv.setText(word);
                        result_tv.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        onFinishRequest();
                    }
                });

    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setBackgroundColor(this.getResources().getColor(R.color.none));
            voice_btn.setText(this.getResources().getString(R.string.finish));
            speak_round_layout.setBackgroundResource(R.drawable.round_light_green_bgl);
            XFUtil.showSpeechRecognizer(this, mSharedPreferences, recognizer,
                    recognizerListener, XFUtil.VoiceEngineMD);
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
        speak_round_layout.setBackgroundResource(R.drawable.round_gray_green_bgl);
    }

    /**
     * submit request task
     */
    private void submit() {
        word = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(word)) {
            String last = word.substring(word.length() - 1);
            if (",.?!;:'，。？！‘；：".contains(last)) {
                word = word.substring(0, word.length() - 1);
            }
            RequestBaiduApi();
        } else {
            ToastUtil.diaplayMesShort(this, this.getResources().getString(R.string.ch_dic_input_hint));
            hideProgressbar();
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
        LogUtil.DefalutLog("MainFragment-onDestroy");
    }

    @OnClick({R.id.submit_btn, R.id.clear_btn_layout, R.id.question_tv_cover, R.id.result_tv_cover, R.id.copy_btn, R.id.share_btn, R.id.btn_bushou, R.id.btn_pinyin, R.id.speak_round_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                hideIME(input_et);
                submit();
                AVAnalytics.onEvent(this, "chdic_submit_btn");
                break;
            case R.id.clear_btn_layout:
                input_et.setText("");
                AVAnalytics.onEvent(this, "chdic_clear_btn");
                break;
            case R.id.question_tv_cover:
                play(question_tv.getText().toString());
                AVAnalytics.onEvent(this, "chdic_play_question");
                break;
            case R.id.result_tv_cover:
                play(result_tv.getText().toString());
                AVAnalytics.onEvent(this, "chdic_play_result");
                break;
            case R.id.copy_btn:
                copy();
                AVAnalytics.onEvent(this, "chdic_copy");
                break;
            case R.id.share_btn:
                share();
                AVAnalytics.onEvent(this, "chdic_share");
                break;
            case R.id.btn_bushou:
                toBushouActivity();
                AVAnalytics.onEvent(this, "chdic_to_bushou");
                break;
            case R.id.btn_pinyin:
                toPinyinActivity();
                AVAnalytics.onEvent(this, "chdic_to_pinyin");
                break;
            case R.id.speak_round_layout:
                showIatDialog();
                AVAnalytics.onEvent(this, "chdic_speak_btn");
                break;
        }
    }
}
