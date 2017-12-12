package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.avos.avoscloud.okhttp.Response;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.bean.ChDic;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
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
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChineseDictionaryActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.btn_bushou)
    TextView btnBushou;
    @BindView(R.id.btn_pinyin)
    TextView btnPinyin;
    private EditText input_et;
    private FrameLayout submit_btn;
    private FrameLayout photo_tran_btn, copy_btn, share_btn;
    private FrameLayout clear_btn_layout;
    private Button voice_btn;
    private LinearLayout speak_round_layout;
    private TextView result_tv;
    private TextView question_tv;
    private ScrollView chdic_sv;
    private String word;
    private ChDic mRoot;
    /**
     * record
     **/
    private LinearLayout record_layout;
    private ImageView record_anim_img;

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

        chdic_sv = (ScrollView) findViewById(R.id.chdic_sv);
        result_tv = (TextView) findViewById(R.id.result_tv);
        question_tv = (TextView) findViewById(R.id.question_tv);
        input_et = (EditText) findViewById(R.id.input_et);
        submit_btn = (FrameLayout) findViewById(R.id.submit_btn);
        photo_tran_btn = (FrameLayout) findViewById(R.id.photo_tran_btn);
        copy_btn = (FrameLayout) findViewById(R.id.copy_btn);
        share_btn = (FrameLayout) findViewById(R.id.share_btn);
        speak_round_layout = (LinearLayout) findViewById(R.id.speak_round_layout);
        clear_btn_layout = (FrameLayout) findViewById(R.id.clear_btn_layout);
        record_layout = (LinearLayout) findViewById(R.id.record_layout);
        record_anim_img = (ImageView) findViewById(R.id.record_anim_img);
        voice_btn = (Button) findViewById(R.id.voice_btn);

        photo_tran_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        copy_btn.setOnClickListener(this);
        share_btn.setOnClickListener(this);
        speak_round_layout.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);
        btnBushou.setOnClickListener(this);
        btnPinyin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            hideIME(input_et);
            submit();
            AVAnalytics.onEvent(this, "tab1_submit_btn");
        } else if (v.getId() == R.id.speak_round_layout) {
            showIatDialog();
            AVAnalytics.onEvent(this, "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(this, "tab1_clear_btn");
        } else if (v.getId() == R.id.copy_btn) {
            copy();
        } else if (v.getId() == R.id.share_btn) {
            share();
        } else if (v.getId() == R.id.btn_bushou) {
            toBushouActivity();
        } else if (v.getId() == R.id.btn_pinyin) {
            toPinyinActivity();
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

    private void RequestBaiduApi(){
        showProgressbar();
        submit_btn.setEnabled(false);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String url = Settings.CHDicBaiduApi+word;
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
                        chdic_sv.scrollTo(0,0);
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

    private void RequestAsyncTask() {
        showProgressbar();
        submit_btn.setEnabled(false);
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "59ef16d2ca4ee5b590bde3976a8bf45f")
                .add("word", word)
                .build();
        LanguagehelperHttpClient.post(Settings.ChDicSearchUrl, formBody, new UICallback(this) {
            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(ChineseDictionaryActivity.this, ChineseDictionaryActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onFinishRequest();
            }

            @Override
            public void onResponsed(String responseString) {
                if (!TextUtils.isEmpty(responseString)) {
                    LogUtil.DefalutLog("responseString:" + responseString);
                    mRoot = JSON.parseObject(responseString, ChDic.class);
                    if (mRoot != null && mRoot.getError_code() == 0) {
                        mRoot.getResult().setResult();
                        result_tv.setText(mRoot.getResult().getResultForShow(word));
                    } else {
                        ToastUtil.diaplayMesShort(ChineseDictionaryActivity.this, mRoot.getReason());
                    }
                } else {
                    ToastUtil.diaplayMesShort(ChineseDictionaryActivity.this, ChineseDictionaryActivity.this.getResources().getString(
                            R.string.network_error));
                }
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
}
