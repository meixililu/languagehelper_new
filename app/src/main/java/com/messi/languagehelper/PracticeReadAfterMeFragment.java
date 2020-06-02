package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcPractiseListAdapter;
import com.messi.languagehelper.bean.UserSpeakBean;
import com.messi.languagehelper.impl.PracticeProgressListener;
import com.messi.languagehelper.impl.PractisePlayUserPcmListener;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.task.PublicTask;
import com.messi.languagehelper.task.PublicTask.PublicTaskListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AnimationUtil;
import com.messi.languagehelper.util.AudioTrackUtil;
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
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

public class PracticeReadAfterMeFragment extends BaseFragment implements OnClickListener, PractisePlayUserPcmListener {

    private FrameLayout record_answer_cover, repeat_time_minus_cover, repeat_time_plus_cover;
    private ImageButton voice_play_answer;
    private TextView record_question, record_answer, practice_prompt, record_animation_text;
    private TextView repeat_time, repeat_time_minus, repeat_time_plus;
    private RecyclerView recent_used_lv;
    private ImageView record_anim_img;
    private FrameLayout check_btn_cover;
    private TextView check_btn;
    private LinearLayout record_layout, record_animation_layout;

    private MyOnClickListener mAnswerOnClickListener;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SpeechRecognizer recognizer;
    private SharedPreferences mSharedPreferences;
    private ArrayList<UserSpeakBean> mUserSpeakBeanList;
    private RcPractiseListAdapter adapter;
    private boolean isNewIn = true;
    private boolean isFollow;

    private String[] cn, en;
    private String content;
    private String videoPath;
    private int resultPosition;
    private int repeatTimes;
    private int times;
    private PracticeProgressListener mPracticeProgress;
    private StringBuilder sbResult = new StringBuilder();
    private MyThread mMyThread;
    private Thread mThread;
    private String userPcmPath;
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
            times++;
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            hideProgressbar();
            isEnoughTimes();
            ToastUtil.diaplayMesShort(getActivity(), err.getErrorDescription());
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            finishRecord();
            showProgressbar();
            isEnoughTimes();
        }


        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult---getResultString:" + results.getResultString());
            String text = JsonParser.parseIatResult(results.getResultString()).toLowerCase();
            sbResult.append(text);
            if (isLast) {
                hideProgressbar();
                finishRecord();
                UserSpeakBean bean = ScoreUtil.score(getActivity(), sbResult.toString().toLowerCase(),
                        record_answer.getText().toString().toLowerCase());
                mUserSpeakBeanList.add(0, bean);
                adapter.notifyDataSetChanged();
                animationReward(bean.getScoreInt());
                isEnoughTimes();
                sbResult.setLength(0);
                mMyThread = AudioTrackUtil.getMyThread(userPcmPath);
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
            showIatDialog();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };

    public static PracticeReadAfterMeFragment newInstace(String content, PracticeProgressListener mPracticeProgress, String videoPath,
                                                         SharedPreferences mSharedPreferences, SpeechSynthesizer mSpeechSynthesizer) {
        PracticeReadAfterMeFragment fragment = new PracticeReadAfterMeFragment();
        fragment.setData(content, mPracticeProgress, videoPath, mSharedPreferences, mSpeechSynthesizer);
        return fragment;
    }

    public void setData(String content, PracticeProgressListener mPracticeProgress, String videoPath,
                        SharedPreferences mSharedPreferences, SpeechSynthesizer mSpeechSynthesizer) {
        this.content = content;
        this.mPracticeProgress = mPracticeProgress;
        getContent();
        this.videoPath = SDCardUtil.getDownloadPath(videoPath);
        this.mSharedPreferences = mSharedPreferences;
        this.mSpeechSynthesizer = mSpeechSynthesizer;
    }

    private void getContent() {
        String temp[] = content.split("#");
        cn = temp[0].split(",");
        en = temp[1].split(",");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.practice_read_after_me_fragmenty, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        mUserSpeakBeanList = new ArrayList<UserSpeakBean>();
        adapter = new RcPractiseListAdapter(this);
    }

    private void initView() {
        mSharedPreferences = Setings.getSharedPreferences(getActivity());
        recognizer = SpeechRecognizer.createRecognizer(getActivity(), null);
        repeatTimes = mSharedPreferences.getInt(KeyUtil.ReadRepeatTime, 2);

        record_answer_cover = (FrameLayout) getView().findViewById(R.id.record_answer_cover);
        repeat_time_minus_cover = (FrameLayout) getView().findViewById(R.id.repeat_time_minus_cover);
        repeat_time_plus_cover = (FrameLayout) getView().findViewById(R.id.repeat_time_plus_cover);
        repeat_time = (TextView) getView().findViewById(R.id.repeat_time);
        repeat_time_minus = (TextView) getView().findViewById(R.id.repeat_time_minus);
        repeat_time_plus = (TextView) getView().findViewById(R.id.repeat_time_plus);

        practice_prompt = (TextView) getView().findViewById(R.id.practice_prompt);
        record_answer = (TextView) getView().findViewById(R.id.record_answer);
        record_question = (TextView) getView().findViewById(R.id.record_question);
        voice_play_answer = (ImageButton) getView().findViewById(R.id.voice_play_answer);
        check_btn_cover = (FrameLayout) getView().findViewById(R.id.check_btn_cover);
        check_btn = (TextView) getView().findViewById(R.id.check_btn);
        record_anim_img = (ImageView) getView().findViewById(R.id.record_anim_img);
        record_layout = (LinearLayout) getView().findViewById(R.id.record_layout);
        record_animation_layout = (LinearLayout) getView().findViewById(R.id.record_animation_layout);
        record_animation_text = (TextView) getView().findViewById(R.id.record_animation_text);
        recent_used_lv = (RecyclerView) getView().findViewById(R.id.recent_used_lv);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(getContext()));
        recent_used_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        adapter.setItems(mUserSpeakBeanList);
        recent_used_lv.setAdapter(adapter);

        setPracticeContent();
        initSpeakLanguage();
        repeat_time.setText("跟读 " + repeatTimes + " 次");
        check_btn_cover.setOnClickListener(this);
        repeat_time_minus_cover.setOnClickListener(this);
        repeat_time_plus_cover.setOnClickListener(this);

    }

    private void initSpeakLanguage() {
        practice_prompt.setText(getActivity().getResources().getString(R.string.practice_prompt_english));
    }

    private void setPracticeContent() {
        record_answer.setText(en[resultPosition]);
        record_question.setText(cn[resultPosition]);
        mAnswerOnClickListener = new MyOnClickListener(en[resultPosition], voice_play_answer, true);
        record_answer_cover.setOnClickListener(mAnswerOnClickListener);
        mUserSpeakBeanList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_btn_cover:
                isNewIn = true;
                showIatDialog();
                AVAnalytics.onEvent(getActivity(), "readafterme_pg_speak_btn");
                break;
            case R.id.repeat_time_minus_cover:
                if (repeatTimes > 1) {
                    repeatTimes--;
                    repeat_time.setText("跟读 " + repeatTimes + " 次");
                }
                Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.ReadRepeatTime, repeatTimes);
                AVAnalytics.onEvent(getActivity(), "readafterme_pg_minus_btn");
                break;
            case R.id.repeat_time_plus_cover:
                repeatTimes++;
                repeat_time.setText("跟读 " + repeatTimes + " 次");
                Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.ReadRepeatTime, repeatTimes);
                AVAnalytics.onEvent(getActivity(), "readafterme_pg_plus_btn");
                break;
            default:
                break;
        }
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        if (!recognizer.isListening()) {
            if (times >= repeatTimes) {
                if (resultPosition < en.length - 1) {
                    resultPosition++;
                    times = 0;
                    check_btn.setText(getActivity().getResources().getString(R.string.practice_start));
                    setPracticeContent();
                } else {
                    if (mPracticeProgress != null) {
                        mPracticeProgress.toNextPage();
                    }
                }
            } else {
                if (isNewIn) {
                    setProgressPromptText("Listen");
                    AnimationUtil.ScaleXYAndAlpha(record_animation_layout, null, 1f, 1f, 1f, 1f, 0, 800);
                    isNewIn = false;
                    isFollow = true;
                    practice_prompt.setVisibility(View.GONE);
                    check_btn_cover.setEnabled(false);
                    record_answer_cover.performClick();
                } else {
                    record_layout.setVisibility(View.VISIBLE);
                    check_btn.setText(getActivity().getResources().getString(R.string.finish));
                    String path = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath);
                    userPcmPath = path + "/userpractice.pcm";
                    recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, userPcmPath);
                    XFUtil.showSpeechRecognizer(getActivity(), mSharedPreferences, recognizer,
                            recognizerListener, XFUtil.VoiceEngineEN);
                }
            }
        } else {
            showProgressbar();
            finishRecord();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        recognizer.stopListening();
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        check_btn.setText(getActivity().getResources().getString(R.string.practice_start));
    }

    private void setProgressPromptText(String text) {
        record_animation_layout.setVisibility(View.VISIBLE);
        record_animation_text.setText(text);
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
        setProgressPromptText(word);
        animationAlphaReward();
    }

    private void animationAlphaReward() {
        AnimationUtil.Alpha(record_animation_layout, mAnimatorListenerReward, 1, 0, 300, 1800);
    }

    /**
     * on finish play
     */
    private void onfinishPlay() {
        check_btn_cover.setEnabled(true);
        if (isFollow) {
            isFollow = false;
            setProgressPromptText(getActivity().getResources().getString(R.string.your_turn));
            AnimationUtil.ScaleXYAndAlpha(record_animation_layout, mAnimatorListener, 1.4f, 1f, 0, 800);
        }
    }

    private void isEnoughTimes() {
        if (times >= repeatTimes) {
            if (resultPosition < en.length) {
                check_btn.setText("继续，下一个");
            } else {
                check_btn.setText("继续，下一关");
            }
        }
    }

    @Override
    public void playOrStop() {
        playUserPcm();
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

    private void playLocalPcm(final String path, final AnimationDrawable animationDrawable) {
        PublicTask mPublicTask = new PublicTask();
        mPublicTask.setmPublicTaskListener(new PublicTaskListener() {
            @Override
            public void onPreExecute() {
                if (!animationDrawable.isRunning()) {
                    animationDrawable.setOneShot(false);
                    animationDrawable.start();
                }
            }

            @Override
            public Object doInBackground() {
                AudioTrackUtil.createAudioTrack(path);
                return null;
            }

            @Override
            public void onFinish(Object resutl) {
                animationDrawable.setOneShot(true);
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
                onfinishPlay();
            }
        });
        mPublicTask.execute();
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

    public void showProgressbar() {
        if (mPracticeProgress != null) {
            mPracticeProgress.onLoading();
        }
    }

    public void hideProgressbar() {
        if (mPracticeProgress != null) {
            mPracticeProgress.onCompleteLoading();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.destroy();
        }
        if (recognizer != null) {
            recognizer.cancel();
        }
    }

    public class MyOnClickListener implements OnClickListener {

        private String content;
        private ImageButton voice_play;
        private AnimationDrawable animationDrawable;
        private boolean isPlayResult;

        private MyOnClickListener(String content, ImageButton voice_play, boolean isPlayResult) {
            this.content = content;
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
            String filepath = videoPath + resultPosition + ".pcm";
            if (!AudioTrackUtil.isFileExists(filepath)) {
                showProgressbar();
                mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
                XFUtil.showSpeechSynthesizer(getActivity(), mSharedPreferences, mSpeechSynthesizer, content,
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
                                LogUtil.DefalutLog("---onCompleted");
                                if (arg0 != null) {
                                    ToastUtil.diaplayMesShort(getActivity(), arg0.getErrorDescription());
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
            AVAnalytics.onEvent(getActivity(), "readafterme_pg_playresult");
        }
    }

}
