package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.adapter.RcPractiseListAdapter;
import com.messi.languagehelper.bean.UserSpeakBean;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.impl.MyPlayerListener;
import com.messi.languagehelper.impl.PractisePlayUserPcmListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.PCMAudioPlayer;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ScoreUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PracticeActivity extends BaseActivity implements OnClickListener, PractisePlayUserPcmListener {

    @BindView(R.id.voice_btn_cover)
    FrameLayout voice_btn_cover;
    @BindView(R.id.voice_btn)
    TextView voice_btn;
    @BindView(R.id.record_question_cover)
    FrameLayout record_question_cover;
    @BindView(R.id.record_answer_cover)
    FrameLayout record_answer_cover;
    @BindView(R.id.voice_play_answer)
    ImageButton voice_play_answer;
    @BindView(R.id.voice_play_question)
    ImageButton voice_play_question;

    @BindView(R.id.record_answer)
    TextView record_answer;
    @BindView(R.id.record_question)
    TextView record_question;
    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;
    @BindView(R.id.practice_prompt)
    TextView practice_prompt;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.record_animation_text)
    TextView record_animation_text;
    @BindView(R.id.record_animation_layout)
    LinearLayout record_animation_layout;

    private Record mBean;
    private MyOnClickListener mAnswerOnClickListener, mQuestionOnClickListener;
    private SpeechRecognizer recognizer;
    private SharedPreferences mSharedPreferences;
    private ArrayList<UserSpeakBean> mUserSpeakBeanList;
    private RcPractiseListAdapter adapter;
    private boolean isEnglish;
    private boolean isExchange;
    private boolean isNewIn = true;
    private boolean isFollow;
    private StringBuilder sbResult = new StringBuilder();

    private String userPcmPath;
    private boolean isNeedDelete;

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
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            hideProgressbar();
            ToastUtil.diaplayMesShort(PracticeActivity.this, err.getErrorDescription());
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            finishRecord();
            showProgressbar();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult---getResultString:" + results.getResultString());
            String text = JsonParser.parseIatResult(results.getResultString());
            sbResult.append(text);
            if (isLast) {
                LogUtil.DefalutLog("isLast-------onResult:" + sbResult.toString());
                hideProgressbar();
                finishRecord();
                UserSpeakBean bean = ScoreUtil.score(sbResult.toString(), record_answer.getText().toString());
                mUserSpeakBeanList.add(0, bean);
                adapter.notifyDataSetChanged();
                animationReward(bean.getScoreInt());
                sbResult.setLength(0);
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

    private AnimatorListener mAnimatorListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            record_animation_layout.setVisibility(View.GONE);
            PracticeActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(PracticeActivity.this);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_activity);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        try {
            mBean = (Record) Setings.dataMap.get(KeyUtil.DialogBeanKey);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.DefalutLog("类型错误");
            finish();
        }
        if(mBean != null){
            isEnglish = StringUtils.isEnglish(mBean.getEnglish());
        }else {
            finish();
        }
        isNeedDelete = getIntent().getBooleanExtra(KeyUtil.IsNeedDelete, false);
        mUserSpeakBeanList = new ArrayList<UserSpeakBean>();
        adapter = new RcPractiseListAdapter(this);
    }

    private void initView() {
        getSupportActionBar().setTitle("");
        mSharedPreferences = Setings.getSharedPreferences(this);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(this));
        recent_used_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        adapter.setItems(mUserSpeakBeanList);
        recent_used_lv.setAdapter(adapter);

        record_question.setText(mBean.getChinese());
        record_answer.setText(mBean.getEnglish());

        initSpeakLanguage();
        mAnswerOnClickListener = new MyOnClickListener(mBean, voice_play_answer, true);
        mQuestionOnClickListener = new MyOnClickListener(mBean, voice_play_question, false);

        record_question_cover.setOnClickListener(mQuestionOnClickListener);
        record_answer_cover.setOnClickListener(mAnswerOnClickListener);
        voice_btn_cover.setOnClickListener(this);
        isNeedExchange();
    }

    private void isNeedExchange(){
        String pats1 = "英.*\\[";
        Pattern pattern1 = Pattern.compile(pats1);
        if(pattern1.matcher(mBean.getEnglish()).find()){
            exchangeContentAndResult();
        }
    }

    private void initSpeakLanguage() {
        if (isEnglish) {
            practice_prompt.setText(this.getResources().getString(R.string.practice_prompt_english));
        } else {
            practice_prompt.setText(this.getResources().getString(R.string.practice_prompt_chinese));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice_btn_cover:
                PracticeActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
                AVAnalytics.onEvent(PracticeActivity.this, "practice_pg_speak_btn");
                break;
            default:
                break;
        }
    }

    @Override
    public void playOrStop() {
        playUserPcm();
    }

    private void playUserPcm() {
        if (!TextUtils.isEmpty(userPcmPath)) {
            PCMAudioPlayer.getInstance().startPlay(userPcmPath);
        }
    }

    private void exchangeContentAndResult() {
        if (!isExchange) {
            isExchange = true;
            mAnswerOnClickListener.setPlayResult(false);
            mQuestionOnClickListener.setPlayResult(true);
        } else {
            isExchange = false;
            mAnswerOnClickListener.setPlayResult(true);
            mQuestionOnClickListener.setPlayResult(false);
        }
        String tempAnswer = record_answer.getText().toString();
        String tempQuestion = record_question.getText().toString();
        record_answer.setText(tempQuestion);
        record_question.setText(tempAnswer);
        isEnglish = StringUtils.isEnglish(tempQuestion);
        initSpeakLanguage();
    }

    public void showIat() {
        if (recognizer != null) {
            if (!recognizer.isListening()) {
                if (isNewIn) {
                    isNewIn = false;
                    isFollow = true;
                    showListen();
                    practice_prompt.setVisibility(View.GONE);
                    mAnswerOnClickListener.onClick(voice_play_answer);
                } else {
                    record_layout.setVisibility(View.VISIBLE);
                    voice_btn.setText(this.getResources().getString(R.string.finish));
                    String path = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath);
                    userPcmPath = path + "userpractice.pcm";
                    recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, userPcmPath);
                    if (isEnglish) {
                        XFUtil.showSpeechRecognizer(this, mSharedPreferences, recognizer,
                                recognizerListener, XFUtil.VoiceEngineEN);
                    } else {
                        XFUtil.showSpeechRecognizer(this, mSharedPreferences, recognizer,
                                recognizerListener, XFUtil.VoiceEngineMD);
                    }
                }
            } else {
                showProgressbar();
                finishRecord();
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

    /**
     * finish record
     */
    private void finishRecord() {
        recognizer.stopListening();
        isNewIn = true;
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText("Start");
    }

    private void onfinishPlay() {
        if (isFollow) {
            isFollow = false;
            record_animation_layout.setVisibility(View.VISIBLE);
            record_animation_text.setText(this.getResources().getString(R.string.your_turn));
            animation();
        }
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

    private void animation() {
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(record_animation_layout, "scaleX", 1f, 1.3f);
        mObjectAnimator.addListener(mAnimatorListener);
        mObjectAnimator.setDuration(800).start();
        ObjectAnimator mObjectAnimator1 = ObjectAnimator.ofFloat(record_animation_layout, "scaleY", 1f, 1.3f);
        mObjectAnimator1.setDuration(800).start();
        ObjectAnimator mObjectAnimator2 = ObjectAnimator.ofFloat(record_animation_layout, "alpha", 1, 0);
        mObjectAnimator2.setDuration(800).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_swap:
                exchangeContentAndResult();
                AVAnalytics.onEvent(PracticeActivity.this, "practice_pg_exchange_btn");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetVoicePlayButton() {
        resetVoiceAnimation(voice_play_answer);
        resetVoiceAnimation(voice_play_question);
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
        if (recognizer != null) {
            recognizer.destroy();
            recognizer = null;
        }
        MyPlayer.getInstance(this).onDestroy();
        if (isNeedDelete) {
            BoxHelper.remove(mBean);
        }
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void showIatDialog() {
        showIat();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PracticeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    void onShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
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
        ToastUtil.diaplayMesShort(this,"拒绝录音权限，无法使用语音功能！");
    }

    public class MyOnClickListener implements OnClickListener {

        private Record mBean;
        private ImageButton voice_play;
        private AnimationDrawable animationDrawable;
        private boolean isPlayResult;

        private MyOnClickListener(Record bean, ImageButton voice_play, boolean isPlayResult) {
            this.mBean = bean;
            this.voice_play = voice_play;
            this.animationDrawable = (AnimationDrawable) voice_play.getBackground();
            this.isPlayResult = isPlayResult;
        }

        public void setPlayResult(boolean isPlayResult) {
            this.isPlayResult = isPlayResult;
        }

        @Override
        public void onClick(final View v) {
            resetVoicePlayButton();
            showProgressbar();
            MyPlayer.getInstance(PracticeActivity.this).setListener(new MyPlayerListener() {
                @Override
                public void onStart() {
                    hideProgressbar();
                    voice_play.setVisibility(View.VISIBLE);
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.setOneShot(false);
                        animationDrawable.start();
                    }
                }

                @Override
                public void onFinish() {
                    animationDrawable.setOneShot(true);
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);
                    onfinishPlay();
                }
            });

            String speakContent = "";
            if (isPlayResult) {
                speakContent = mBean.getEnglish();
            } else {
                speakContent = mBean.getChinese();
            }
            if (isPlayResult && !isExchange) {
                if (!TextUtils.isEmpty(mBean.getPh_en_mp3())) {
                    LogUtil.DefalutLog(mBean.getPh_en_mp3());
                    MyPlayer.getInstance(PracticeActivity.this).start(speakContent,mBean.getPh_en_mp3());
                } else {
                    MyPlayer.getInstance(PracticeActivity.this).start(speakContent);
                }
            } else {
                MyPlayer.getInstance(PracticeActivity.this).start(speakContent);
            }
        }
    }

}
