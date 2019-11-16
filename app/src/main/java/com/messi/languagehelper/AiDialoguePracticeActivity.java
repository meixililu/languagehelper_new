package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
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
import com.messi.languagehelper.util.ScoreUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import java.util.ArrayList;
import java.util.List;

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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AiDialoguePracticeActivity extends BaseActivity implements View.OnClickListener, SpokenEnglishPlayListener {

    @BindView(R.id.listview)
    RecyclerView studylist_lv;
    @BindView(R.id.start_btn)
    TextView start_btn;
    @BindView(R.id.start_btn_cover)
    FrameLayout start_btn_cover;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.voice_img)
    ImageButton voice_img;
    @BindView(R.id.start_to_fight)
    FrameLayout startToFight;
    private RcAiDialoguePracticeAdapter mAdapter;
    private List<AVObject> avObjects;
    private String ECCode;
    private int position;
    private boolean isNewIn = true;
    private boolean isFollow;
    private StringBuilder sbResult = new StringBuilder();

    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer recognizer;
    private MediaPlayer mPlayer;
    private AnimationDrawable animationDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_dialogue_practice_activity);
        ButterKnife.bind(this);
        initViews();
        getDataTask();
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.title_Practice));
        mSharedPreferences = Setings.getSharedPreferences(this);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        mPlayer = new MediaPlayer();
        avObjects = new ArrayList<AVObject>();
        ECCode = getIntent().getStringExtra(AVOUtil.EvaluationCategory.ECCode);
        studylist_lv = (RecyclerView) findViewById(R.id.listview);
        mAdapter = new RcAiDialoguePracticeAdapter(avObjects, this);
        mAdapter.setItems(avObjects);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        studylist_lv.setLayoutManager(mLinearLayoutManager);
        studylist_lv.setAdapter(mAdapter);
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

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void showIatDialog() {
        try {
            if (recognizer != null) {
                if (!recognizer.isListening()) {
                    if (isNewIn) {
                        startToPlaySpeaker(position);
                    } else {
                        startToRecord();
                    }
                } else {
                    showProgressbar();
                    finishRecord();
                }
            }
            AVAnalytics.onEvent(AiDialoguePracticeActivity.this, "evaluationdetail_pg_speak_btn");
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
            start_btn.setText(this.getResources().getString(R.string.start_to_follow));
        }
    }

    private void onfinishPlay() {
        if (isFollow) {
            isFollow = false;
            AiDialoguePracticeActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
        }
    }

    private void getDataTask() {
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                queryData();
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
                        onQueryDataFinish();
                    }
                });

    }

    private void queryData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationDetail.EvaluationDetail);
        query.whereEqualTo(AVOUtil.EvaluationDetail.ECCode, ECCode);
        query.whereEqualTo(AVOUtil.EvaluationDetail.EDIsValid, "1");
        query.orderByAscending(AVOUtil.EvaluationDetail.ECLCode);
        try {
            List<AVObject> avObject = query.find();
            if (avObject != null && avObject.size() > 0) {
                for (int i = 0; i < avObject.size(); i++) {
                    if (i == 0) {
                        avObject.get(i).put(KeyUtil.PracticeItemIndex, "1");
                    } else {
                        avObject.get(i).put(KeyUtil.PracticeItemIndex, "0");
                    }
                }
                avObjects.addAll(avObject);
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    private void onQueryDataFinish() {
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
        mAdapter.notifyDataSetChanged();
    }

    private void resetData() {
        for (AVObject item : avObjects) {
            if (item.get(KeyUtil.PracticeItemIndex).equals("1")) {
                item.put(KeyUtil.PracticeItemIndex, "0");
            }
        }
    }

    private void setSelected(int index) {
        if (avObjects.size() > position) {
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
        isNewIn = false;
        isFollow = true;
        setSelected(index);
        playItem(avObjects.get(index));
        AVAnalytics.onEvent(AiDialoguePracticeActivity.this, "evaluationdetail_pg_play_result");
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
                XFUtil.showSpeechSynthesizer(AiDialoguePracticeActivity.this, mSharedPreferences,
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
                                    ToastUtil.diaplayMesShort(AiDialoguePracticeActivity.this, arg0.getErrorDescription());
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
                    DownLoadUtil.downloadFile(AiDialoguePracticeActivity.this, url, path, fileName);
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
                AVObject mSpeakItem = avObjects.get(position);
                UserSpeakBean bean = ScoreUtil.score(AiDialoguePracticeActivity.this, sbResult.toString(), getEnglishContent(mSpeakItem), 0);
                mSpeakItem.put(KeyUtil.UserSpeakBean, bean);
                mAdapter.notifyDataSetChanged();
                sbResult.setLength(0);
            }
        }

        @Override
        public void onError(SpeechError error) {
            LogUtil.DefalutLog("onError:" + error.getErrorDescription());
            finishRecord();
            hideProgressbar();
            ToastUtil.diaplayMesShort(AiDialoguePracticeActivity.this, error.getErrorDescription());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn_cover:
                AiDialoguePracticeActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
                break;
        }
    }

    @OnClick(R.id.start_to_fight)
    public void onViewClicked() {
        initData();
        Setings.dataMap.put("avObjects",avObjects);
        toActivity(AiDialogueFightActivity.class,null);
    }

    private void initData(){
        for (int i = 0; i < avObjects.size(); i++) {
            if (i == 0) {
                avObjects.get(i).put(KeyUtil.PracticeItemIndex, "1");
            } else {
                avObjects.get(i).put(KeyUtil.PracticeItemIndex, "0");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AiDialoguePracticeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XFUtil.closeSpeechRecognizer(recognizer);
        XFUtil.closeSpeechSynthesizer(mSpeechSynthesizer);
        AudioTrackUtil.closeMedia(mPlayer);
    }
}
