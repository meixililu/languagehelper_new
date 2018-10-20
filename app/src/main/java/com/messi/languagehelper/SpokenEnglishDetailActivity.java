package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.bean.UserSpeakBean;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ScoreUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

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

public class SpokenEnglishDetailActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.evaluation_en_tv)
    TextView evaluation_en_tv;
    @BindView(R.id.voice_play_answer)
    ImageButton voice_play_answer;
    @BindView(R.id.record_answer_cover)
    FrameLayout record_answer_cover;
    @BindView(R.id.evaluation_zh_tv)
    TextView evaluation_zh_tv;
    @BindView(R.id.show_zh_img)
    FrameLayout show_zh_img;
    @BindView(R.id.previous_btn)
    FrameLayout previous_btn;
    @BindView(R.id.start_btn)
    TextView start_btn;
    @BindView(R.id.start_btn_cover)
    FrameLayout start_btn_cover;
    @BindView(R.id.next_btn)
    FrameLayout next_btn;
    @BindView(R.id.user_speak_content)
    TextView user_speak_content;
    @BindView(R.id.user_speak_score)
    TextView user_speak_score;
    @BindView(R.id.user_speak_cover)
    FrameLayout user_speak_cover;
    @BindView(R.id.sentence_cb)
    RadioButton sentence_cb;
    @BindView(R.id.sentence_cover)
    FrameLayout sentence_cover;
    @BindView(R.id.continuity_cb)
    RadioButton continuity_cb;
    @BindView(R.id.continuity_cover)
    FrameLayout continuity_cover;
    @BindView(R.id.conversation_cb)
    RadioButton conversation_cb;
    @BindView(R.id.conversation_cover)
    FrameLayout conversation_cover;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.user_voice_play_img)
    ImageView user_voice_play_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.record_animation_text)
    TextView record_animation_text;
    @BindView(R.id.record_animation_layout)
    LinearLayout record_animation_layout;

    private MyOnClickListener mEvaluationOnClickListener;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer recognizer;

    private boolean isNewIn = true;
    private boolean isFollow;
    private StringBuilder sbResult = new StringBuilder();
    private AVObject avObject;
    private String ECCode, ECLCode;
    private String content, EDCode;
    private String[] studyContent;
    private String[] userContent;
    private String mLastResult;
    private String userPcmPath;
    private MyThread mMyThread;
    private Thread mThread;

    private List<AVObject> avObjects;
    private int positin;
    private MediaPlayer mPlayer;
    private AnimatorListener mAnimatorListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (record_animation_layout != null) {
                record_animation_layout.setVisibility(View.GONE);
            }
            showIatDialog();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };
    private AnimatorListener mShowNextAnimator = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onClick(next_btn);
            onClick(start_btn_cover);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };
    private AnimatorListener mAnimatorListenerReward = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            record_animation_layout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };
    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());
            sbResult.append(text);
            if (isLast) {
                LogUtil.DefalutLog("isLast-------onResult:" + sbResult.toString());
                hideProgressbar();
                finishRecord();
                UserSpeakBean bean = null;
                if (!conversation_cb.isChecked()) {
                    bean = ScoreUtil.score(SpokenEnglishDetailActivity.this, sbResult.toString(), evaluation_en_tv.getText().toString());
                } else {
//                    bean = ScoreUtil.score(SpokenEnglishDetailActivity.this, sbResult.toString(), user_need_to_speak_content.getText().toString().split(":")[1].trim());
                }
                showResult(bean);
                animationReward(bean.getScoreInt());
                sbResult.setLength(0);
                mMyThread = AudioTrackUtil.getMyThread(userPcmPath);
                playNext();
            }
        }

        @Override
        public void onError(SpeechError error) {
            LogUtil.DefalutLog("onError:" + error.getErrorDescription());
            finishRecord();
            hideProgressbar();
            ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, error.getErrorDescription());
        }

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("evaluator end");
            finishRecord();
            showProgressbar();
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
        setContentView(R.layout.evaluation_detail_activity);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        getSupportActionBar().setTitle(getResources().getString(R.string.spoken_english_practice));
        mPlayer = new MediaPlayer();
        positin = getIntent().getIntExtra(KeyUtil.PositionKey, 0);
        avObjects = (List<AVObject>) Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        mSharedPreferences = Setings.getSharedPreferences(this);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
    }

    private void initView() {
        int ReadModelType = mSharedPreferences.getInt(KeyUtil.ReadModelType, 0);
        selectedFlowType(ReadModelType, false);
        sentence_cover.setOnClickListener(this);
        continuity_cover.setOnClickListener(this);
        conversation_cover.setOnClickListener(this);
        previous_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        start_btn_cover.setOnClickListener(this);
        show_zh_img.setOnClickListener(this);
        setDatas();
    }

    private void setDatas() {
        avObject = avObjects.get(positin);
        content = avObject.getString(AVOUtil.EvaluationDetail.EDContent);
        EDCode = avObject.getString(AVOUtil.EvaluationDetail.EDCode);
        if (!TextUtils.isEmpty(content)) {
            studyContent = content.split("#");
            if (studyContent != null) {
                if (studyContent.length > 0) {
                    evaluation_en_tv.setText(studyContent[0]);
                    mEvaluationOnClickListener = new MyOnClickListener(studyContent[0], voice_play_answer);
                    record_answer_cover.setOnClickListener(mEvaluationOnClickListener);
                }
            }
            changeZh();
        }
        if (conversation_cb.isChecked()) {
            if (positin + 1 < avObjects.size()) {
                AVObject userAvObject = avObjects.get(positin + 1);
                content = userAvObject.getString(AVOUtil.EvaluationDetail.EDContent);
                if (!TextUtils.isEmpty(content)) {
                    userContent = content.split("#");
                    if (userContent != null) {
                        if (userContent.length > 0) {

                        }
                    }
                }
            } else {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn_cover:
                showIatDialog();
                break;
            case R.id.sentence_cover:
                selectedFlowType(0, false);
                AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_sentence_btn");
                break;
            case R.id.continuity_cover:
                selectedFlowType(1, false);
                AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_continuity_btn");
                break;
            case R.id.conversation_cover:
                selectedFlowType(2, true);
                AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_conversation_btn");
                break;
            case R.id.previous_btn:
                previousItem();
                break;
            case R.id.next_btn:
                nextItem();
                break;
            case R.id.show_zh_img:
                showZH();
                break;
        }
    }

    private void selectedFlowType(int selectedNum, boolean isReset) {
        sentence_cb.setChecked(false);
        continuity_cb.setChecked(false);
        conversation_cb.setChecked(false);
        if (selectedNum == 0) {
            sentence_cb.setChecked(true);
        } else if (selectedNum == 1) {
            continuity_cb.setChecked(true);
        } else if (selectedNum == 2) {
            resetUserSpeakResult();
            conversation_cb.setChecked(true);
            if (isReset) {
                positin = 0;
                setDatas();
            }
        }
        Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.ReadModelType, selectedNum);
    }

    private void previousItem() {
        if (conversation_cb.isChecked() && positin > 1) {
            positin -= 2;
            setDatas();
        } else if (!conversation_cb.isChecked() && positin > 0) {
            positin--;
            setDatas();
        } else {
            ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, "到头了");
        }
        AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_previous_btn");
    }

    private void nextItem() {
        if (conversation_cb.isChecked() && positin < avObjects.size() - 2) {
            positin += 2;
            setDatas();
        } else if (!conversation_cb.isChecked() && positin < avObjects.size() - 1) {
            positin++;
            setDatas();
        } else {
            ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, "木有了");
        }
        AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_next_btn");
    }

    private void showZH() {
        if (TextUtils.isEmpty(evaluation_zh_tv.getText().toString())) {
            if (studyContent.length > 1) {
                evaluation_zh_tv.setText(studyContent[1]);
            } else {
                ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, "此句无译文，读者自行学习解决");
            }
        } else {
            evaluation_zh_tv.setText("");
        }
    }

    private void changeZh() {
        if (!TextUtils.isEmpty(evaluation_zh_tv.getText().toString())) {
            if (studyContent.length > 1) {
                evaluation_zh_tv.setText(studyContent[1]);
            } else {
                ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, "此句无译文，读者自行学习解决");
            }
        }
    }

    private void playUserPcm() {
        if (!TextUtils.isEmpty(userPcmPath)) {
            if (mMyThread != null) {
                if (mMyThread.isPlaying) {
                    AudioTrackUtil.stopPlayPcm(mThread);
                } else {
                    mThread = AudioTrackUtil.startMyThread(mMyThread);
                }
            } else {
                mMyThread = AudioTrackUtil.getMyThread(userPcmPath);
                mThread = AudioTrackUtil.startMyThread(mMyThread);
            }
        }
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        try {
            resetUserSpeakResult();
            if (recognizer != null) {
                if (!recognizer.isListening()) {
                    if (isNewIn) {
                        isNewIn = false;
                        isFollow = true;
                        showListen();
                        mEvaluationOnClickListener.onClick(voice_play_answer);
                    } else {
                        record_layout.setVisibility(View.VISIBLE);
                        start_btn.setText(this.getResources().getString(R.string.finish));
                        String path = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath);
                        userPcmPath = path + "/userpractice.pcm";
                        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, userPcmPath);
                        XFUtil.showSpeechRecognizer(this, mSharedPreferences, recognizer,
                                recognizerListener, XFUtil.VoiceEngineEN);
                    }
                } else {
                    showProgressbar();
                    finishRecord();
                }
            }
            AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_speak_btn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetUserSpeakResult() {
        user_voice_play_img.setVisibility(View.GONE);
        user_speak_content.setText("");
        user_speak_score.setText("");
    }

    /**
     * finish record
     */
    private void finishRecord() {
        if (recognizer != null) {
            if (recognizer.isListening()) {
                recognizer.stopListening();
            }
            isNewIn = true;
            isFollow = false;
            record_layout.setVisibility(View.GONE);
            record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
            start_btn.setText("Start");
        }
    }

    private void onfinishPlay() {
        if (isFollow) {
            isFollow = false;
            if (conversation_cb.isChecked() && positin == avObjects.size() - 1) {
                record_animation_layout.setVisibility(View.GONE);
                finishRecord();
            } else {
                record_animation_layout.setVisibility(View.VISIBLE);
                record_animation_text.setText(this.getResources().getString(R.string.your_turn));
                animation();
            }

        }
    }

    private void showListen() {
        record_animation_layout.setVisibility(View.VISIBLE);
        record_animation_text.setText("Listen");
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(record_animation_layout, "scaleX", 1f, 1f);
        mObjectAnimator.setDuration(800).start();
        ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(record_animation_layout, "scaleY", 1f, 1f);
        mObjectAnimator1.setDuration(800).start();
        ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(record_animation_layout, "alpha", 1, 1);
        mObjectAnimator2.start();
    }

    private void animation() {
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(record_animation_layout, "scaleX", 1f, 1.3f);
        mObjectAnimator.addListener(mAnimatorListener);
        mObjectAnimator.setDuration(800).start();
        ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(record_animation_layout, "scaleY", 1f, 1.3f);
        mObjectAnimator1.setDuration(800).start();
        ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(record_animation_layout, "alpha", 1, 0);
        mObjectAnimator2.setDuration(800).start();
    }

    private void playNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (continuity_cb.isChecked()) {
                    if (positin < avObjects.size() - 1) {
                        showNext();
                    } else {
                        ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, "很好，本节已完成！");
                    }
                } else if (conversation_cb.isChecked()) {
                    if (positin < avObjects.size() - 2) {
                        showNext();
                    } else {
                        ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, "很好，本节已完成！");
                    }
                }
            }
        }, 1000);
    }

    private void showNext() {
        record_animation_layout.setVisibility(View.VISIBLE);
        record_animation_text.setText("Next");
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(record_animation_layout, "scaleX", 1f, 1f);
        mObjectAnimator.addListener(mShowNextAnimator);
        mObjectAnimator.setDuration(800).start();
        ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(record_animation_layout, "scaleY", 1f, 1f);
        mObjectAnimator1.setDuration(800).start();
        ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(record_animation_layout, "alpha", 1, 0);
        mObjectAnimator2.setDuration(800).start();
    }

    private void showResult(UserSpeakBean mBean) {
        user_voice_play_img.setVisibility(View.VISIBLE);
        user_speak_content.setText(mBean.getContent());
        user_speak_score.setText(mBean.getScore());
        int color = this.getResources().getColor(mBean.getColor());
        user_speak_score.setTextColor(color);
        user_speak_score.setShadowLayer(1f, 1f, 1f, color);
        user_speak_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playUserPcm();
                AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_play_userpcm_btn");
            }
        });
    }

    private void animationReward(int score) {
        String word = "Nice";
        if (score > 90) {
            word = "Perfect";
        } else if (score > 70) {
            word = "Great";
        } else if (score > 59) {
            word = "Not bad";
        } else {
            word = "Try harder";
        }
        record_animation_layout.setVisibility(View.VISIBLE);
        record_animation_text.setText(word);
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(record_animation_layout, "alpha", 1, 0f);
        mObjectAnimator.addListener(mAnimatorListenerReward);
        mObjectAnimator.setStartDelay(300);
        mObjectAnimator.setDuration(1500).start();
    }

    private void playMp3(final String url, final String path, final String fileName, final AnimationDrawable animationDrawable) {
        onStart(animationDrawable);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String fullName = SDCardUtil.getDownloadPath(path) + fileName;
                if (!AudioTrackUtil.isFileExists(fullName)) {
                    e.onNext("showProgressbar");
                    DownLoadUtil.downloadFile(SpokenEnglishDetailActivity.this, url, path, fileName);
                    e.onNext("hideProgressbar");
                }
                playMp3(fullName, e);
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
                        if (s.equals("showProgressbar")) {
                            showProgressbar();
                        }
                        if (s.equals("hideProgressbar")) {
                            hideProgressbar();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        onPlayComplete(animationDrawable);
                    }
                });
    }

    private void playLocalPcm(final String path, final AnimationDrawable animationDrawable) {
        onStart(animationDrawable);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                AudioTrackUtil.createAudioTrack(path);
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
                        onPlayComplete(animationDrawable);
                    }
                });
    }

    private void onStart(AnimationDrawable animationDrawable) {
        if (!animationDrawable.isRunning()) {
            animationDrawable.setOneShot(false);
            animationDrawable.start();
        }
    }

    private void onPlayComplete(AnimationDrawable animationDrawable) {
        animationDrawable.setOneShot(true);
        animationDrawable.stop();
        animationDrawable.selectDrawable(0);
        onfinishPlay();
    }

    private void playMp3(String uriPath, final ObservableEmitter<String> subscriber) {
        try {
            mPlayer.reset();
            LogUtil.DefalutLog("uriPath:" + uriPath);
            Uri uri = Uri.parse(uriPath);
            mPlayer.setDataSource(this, uri);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    subscriber.onComplete();
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetVoicePlayButton() {
        resetVoiceAnimation(voice_play_answer);
    }

    private void resetVoiceAnimation(View voice_play) {
        AnimationDrawable animationDrawable = (AnimationDrawable) voice_play.getBackground();
        animationDrawable.setOneShot(true);
        animationDrawable.stop();
        animationDrawable.selectDrawable(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.DefalutLog("SpokenEnglishDetailActivity onDestroy");
        if (mSpeechSynthesizer != null) {
            if (mSpeechSynthesizer.isSpeaking()) {
                mSpeechSynthesizer.stopSpeaking();
            }
            mSpeechSynthesizer = null;
        }
        finishRecord();
        if (recognizer != null) {
            recognizer = null;
        }
    }

    public class MyOnClickListener implements OnClickListener {

        private String ev_content;
        private ImageButton voice_play;
        private AnimationDrawable animationDrawable;

        private MyOnClickListener(String evcon, ImageButton voice_play) {
            this.ev_content = evcon;
            this.voice_play = voice_play;
            this.animationDrawable = (AnimationDrawable) voice_play.getBackground();
        }

        @Override
        public void onClick(final View v) {
            resetVoicePlayButton();
            if (avObject.get(AVOUtil.EvaluationDetail.mp3) != null) {
                playMp3((String) avObject.get(AVOUtil.EvaluationDetail.mp3), SDCardUtil.SpokenEnglishPath, (EDCode + ".mp3"), animationDrawable);
            } else {
                String path = SDCardUtil.getDownloadPath(SDCardUtil.EvaluationPath);
                String filepath = path + EDCode + ".pcm";
                mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
                if (!AudioTrackUtil.isFileExists(filepath)) {
                    showProgressbar();
                    XFUtil.showSpeechSynthesizer(SpokenEnglishDetailActivity.this, mSharedPreferences,
                            mSpeechSynthesizer, ev_content, XFUtil.SpeakerEn,
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
                                    hideProgressbar();
                                    voice_play.setVisibility(View.VISIBLE);
                                    if (!animationDrawable.isRunning()) {
                                        animationDrawable.setOneShot(false);
                                        animationDrawable.start();
                                    }
                                }

                                @Override
                                public void onCompleted(SpeechError arg0) {
                                    if (arg0 != null) {
                                        ToastUtil.diaplayMesShort(SpokenEnglishDetailActivity.this, arg0.getErrorDescription());
                                    }
                                    animationDrawable.setOneShot(true);
                                    animationDrawable.stop();
                                    animationDrawable.selectDrawable(0);
                                    hideProgressbar();
                                    onfinishPlay();
                                }

                                @Override
                                public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                                }

                                @Override
                                public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

                                }
                            });
                } else {
                    playLocalPcm(filepath, animationDrawable);
                }
            }
            if (v.getId() == R.id.record_answer_cover) {
                AVAnalytics.onEvent(SpokenEnglishDetailActivity.this, "evaluationdetail_pg_play_result");
            }
        }
    }
}
