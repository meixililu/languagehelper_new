package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcAiDialoguePracticeAdapter;
import com.messi.languagehelper.bean.UserSpeakBean;
import com.messi.languagehelper.impl.SpokenEnglishPlayListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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

public class AiDialogueFightActivity extends BaseActivity implements View.OnClickListener, SpokenEnglishPlayListener {

    @BindView(R.id.listview)
    RecyclerView studylist_lv;
    @BindView(R.id.previous_btn)
    FrameLayout previous_btn;
    @BindView(R.id.start_btn)
    TextView start_btn;
    @BindView(R.id.start_btn_cover)
    FrameLayout start_btn_cover;
    @BindView(R.id.next_btn)
    FrameLayout next_btn;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.voice_img)
    ImageButton voice_img;

    @BindView(R.id.conversation_layout)
    RelativeLayout conversationLayout;
    @BindView(R.id.speaker_content)
    TextView speakerContent;
    @BindView(R.id.user_content)
    TextView userContent;
    @BindView(R.id.speaker_img)
    SimpleDraweeView speakerImg;
    @BindView(R.id.user_img)
    TextView userImg;
    private RcAiDialoguePracticeAdapter mAdapter;
    private List<AVObject> avObjects;
    private int position;
    private boolean isNewIn = true;
    private boolean isFollow;
    private StringBuilder sbResult = new StringBuilder();
    private String userPcmPath;

    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer recognizer;
    private MediaPlayer mPlayer;
    private AnimationDrawable animationDrawable;
    private boolean userFirst;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_dialogue_fight_activity);
        setStatusbarColor(R.color.white);
        changeStatusBarTextColor(true);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setStatusbarColor(R.color.white_alph);
        mSharedPreferences = Setings.getSharedPreferences(this);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        mPlayer = new MediaPlayer();
        avObjects = (List<AVObject>) Setings.dataMap.get("avObjects");
        Setings.dataMap.clear();

        studylist_lv = (RecyclerView) findViewById(R.id.listview);
        mAdapter = new RcAiDialoguePracticeAdapter(avObjects, this);
        mAdapter.setItems(avObjects);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        studylist_lv.setLayoutManager(mLinearLayoutManager);
        studylist_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        studylist_lv.setAdapter(mAdapter);

        selectedFlowType(2, false);
        previous_btn.setOnClickListener(this);
        conversationLayout.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        start_btn_cover.setOnClickListener(this);
        animationDrawable = (AnimationDrawable) voice_img.getBackground();
    }

    private void startVoiceImgAnimation() {
        voice_img.setVisibility(View.VISIBLE);
        record_layout.setVisibility(View.GONE);
        if (!animationDrawable.isRunning()) {
            animationDrawable.setOneShot(false);
            animationDrawable.start();
        }
    }

    private void stopVoiceImgAnimation() {
        animationDrawable.setOneShot(true);
        animationDrawable.stop();
        animationDrawable.selectDrawable(0);
        voice_img.setVisibility(View.GONE);
        record_layout.setVisibility(View.VISIBLE);
    }

    private void selectedFlowType(int selectedNum, boolean isReset) {
        if (selectedNum == 0) {
            conversationLayout.setVisibility(View.GONE);
            setSelected(position);
        } else if (selectedNum == 1) {
            conversationLayout.setVisibility(View.GONE);
            setSelected(position);
        } else if (selectedNum == 2) {
            conversationLayout.setVisibility(View.VISIBLE);
            userFirst = false;
            position = 0;
            setConversationContent();
            changeConversationLayout(false);
        }
        Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.ReadModelType, selectedNum);
    }

    private void changeConversationLayout(boolean isUserTurn) {
        if (isUserTurn) {
            userContent.setVisibility(View.VISIBLE);
            speakerContent.setVisibility(View.GONE);
            userImg.setVisibility(View.VISIBLE);
            speakerImg.setVisibility(View.GONE);
        } else {
            speakerContent.setVisibility(View.VISIBLE);
            userContent.setVisibility(View.GONE);
            speakerImg.setVisibility(View.VISIBLE);
            userImg.setVisibility(View.GONE);
        }
    }

    private void previousItem() {
        if (position > 1) {
            position -= 2;
            setDatas();
        }else {
            ToastUtil.diaplayMesShort(AiDialogueFightActivity.this, "到头了");
        }
        AVAnalytics.onEvent(AiDialogueFightActivity.this, "evaluationdetail_pg_previous_btn");
    }

    private void nextItem() {
        if (position < avObjects.size() - 2) {
            position += 2;
            setDatas();
        }else {
            userContent.setText("");
            speakerContent.setText("");
        }
        AVAnalytics.onEvent(AiDialogueFightActivity.this, "evaluationdetail_pg_next_btn");
    }

    private void setDatas() {
        setConversationContent();
    }

    private void setConversationContent() {
        if (avObjects.size() > 0) {
            if (!userFirst) {
                speakerContent.setText(getEnglishContent(avObjects.get(position)));
                if ((position + 1) < avObjects.size()) {
                    userContent.setText(getEnglishContent(avObjects.get(position + 1)));
                } else {
                    userContent.setText("");
                }
            } else {
                userContent.setText(getEnglishContent(avObjects.get(position)));
                if ((position + 1) < avObjects.size()) {
                    speakerContent.setText(getEnglishContent(avObjects.get(position + 1)));
                } else {
                    speakerContent.setText("");
                }
            }
        }
    }

    public void showIatDialog() {
        try {
            if (recognizer != null) {
                if (!recognizer.isListening()) {
                    if (userFirst) {
                        changeConversationLayout(true);
                        startToRecord();
                    } else {
                        if (isNewIn) {
                            changeConversationLayout(false);
                            startToPlaySpeaker(position);
                        } else {
                            changeConversationLayout(true);
                            startToRecord();
                        }
                    }
                } else {
                    showProgressbar();
                    finishRecord();
                }
            }
            AVAnalytics.onEvent(AiDialogueFightActivity.this, "evaluationdetail_pg_speak_btn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startToPlaySpeaker(int index) {
        isNewIn = false;
        isFollow = true;
        start_btn.setText("");
        startVoiceImgAnimation();
        AVObject mBean = avObjects.get(index);
        mBean.put(KeyUtil.UserSpeakBean, new UserSpeakBean());
        mAdapter.notifyDataSetChanged();
        playItem(mBean);
    }

    private void startToRecord() {
        stopVoiceImgAnimation();
        start_btn.setText("");
        XFUtil.showSpeechRecognizer(this, mSharedPreferences, recognizer,
                recognizerListener, XFUtil.VoiceEngineEN);
//        String path = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath);
//        userPcmPath = path + "/userpractice.pcm";
//        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, userPcmPath);
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
            start_btn.setText(this.getResources().getString(R.string.start));
        }
    }

    private void onfinishPlay() {
        if (isFollow) {
            isFollow = false;
            if(userFirst){
                if(position < avObjects.size()-2){
                    showNext();
                }else {
                    stopVoiceImgAnimation();
                    finishRecord();
                    ToastUtil.diaplayMesShort(AiDialogueFightActivity.this, "很好，本节已完成！");
                }
            }else if(!userFirst && position == avObjects.size()-1){
                changeRoles();
            } else {
                showIatDialog();
            }
        }
    }

    private void resetData() {
        for (AVObject item : avObjects) {
            if (item.get(KeyUtil.PracticeItemIndex).equals("1")) {
                item.put(KeyUtil.PracticeItemIndex, "0");
            }
        }
    }

    private void setSelected(int index) {
        if(avObjects.size() > position){
            position = index;
            resetData();
            avObjects.get(index).put(KeyUtil.PracticeItemIndex, "1");
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getEnglishContent(AVObject avObject) {
        String temp = avObject.getString(AVOUtil.EvaluationDetail.EDContent);
        String[] str = temp.split("#");
        if (str.length > 1) {
            return temp.split("#")[0];
        } else {
            return temp;
        }
    }

    @Override
    public void playOrStop(int index) {
        setSelected(index);
        playItem(avObjects.get(index));
        AVAnalytics.onEvent(AiDialogueFightActivity.this, "evaluationdetail_pg_play_result");
    }

    public void playItem(AVObject avObject) {
        if (avObject.get(AVOUtil.EvaluationDetail.mp3) != null) {
            playMp3((String) avObject.get(AVOUtil.EvaluationDetail.mp3),
                    SDCardUtil.SpokenEnglishPath, (avObject.getString(AVOUtil.EvaluationDetail.EDCode) + ".mp3"));
        } else {
            String path = SDCardUtil.getDownloadPath(SDCardUtil.EvaluationPath);
            String filepath = path + avObject.getString(AVOUtil.EvaluationDetail.EDCode) + ".pcm";
            mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
            if (!AudioTrackUtil.isFileExists(filepath)) {
                showProgressbar();
                XFUtil.showSpeechSynthesizer(AiDialogueFightActivity.this, mSharedPreferences,
                        mSpeechSynthesizer, getEnglishContent(avObject), XFUtil.SpeakerEn,
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
                            }

                            @Override
                            public void onCompleted(SpeechError arg0) {
                                if (arg0 != null) {
                                    ToastUtil.diaplayMesShort(AiDialogueFightActivity.this, arg0.getErrorDescription());
                                }
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
                playLocalPcm(filepath);
            }
        }
    }

    private void playMp3(final String url, final String path, final String fileName) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String fullName = SDCardUtil.getDownloadPath(path) + fileName;
                if (!AudioTrackUtil.isFileExists(fullName)) {
                    e.onNext("showProgressbar");
                    DownLoadUtil.downloadFile(AiDialogueFightActivity.this, url, path, fileName);
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
                        onfinishPlay();
                    }
                });

    }

    private void playLocalPcm(final String path) {
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
                        onfinishPlay();
                    }
                });
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

    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());
            sbResult.append(text);
            if (isLast) {
                LogUtil.DefalutLog("isLast---onResult:" + sbResult.toString());
                hideProgressbar();
                finishRecord();
                sbResult.setLength(0);
                playNext();
            }
        }

        @Override
        public void onError(SpeechError error) {
            LogUtil.DefalutLog("onError:" + error.getErrorDescription());
            finishRecord();
            hideProgressbar();
            ToastUtil.diaplayMesShort(AiDialogueFightActivity.this, error.getErrorDescription());
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

    private void playNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (position < avObjects.size() - 2) {
                    if(userFirst){
                        changeConversationLayout(false);
                        startToPlaySpeaker(position+1);
                    }else {
                        showNext();
                    }
                } else {
                    if(userFirst && position < avObjects.size() - 1){
                        changeConversationLayout(false);
                        startToPlaySpeaker(position+1);
                    }else if (!userFirst) {
                        changeRoles();
                    } else {
                        ToastUtil.diaplayMesShort(AiDialogueFightActivity.this, "很好，本节已完成！");
                    }
                }
            }
        }, 800);
    }

    private void changeRoles(){
        stopVoiceImgAnimation();
        finishRecord();
        userFirst = true;
        position = 0;
        changeConversationLayout(true);
        setDatas();
        ToastUtil.diaplayMesLong(AiDialogueFightActivity.this, "交换角色");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn_cover:
                showIatDialog();
                break;
            case R.id.previous_btn:
                previousItem();
                break;
            case R.id.next_btn:
                nextItem();
                break;
        }
    }

    private void showNext() {
        onClick(next_btn);
        onClick(start_btn_cover);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XFUtil.closeSpeechRecognizer(recognizer);
        XFUtil.closeSpeechSynthesizer(mSpeechSynthesizer);
        AudioTrackUtil.closeMedia(mPlayer);
    }
}
