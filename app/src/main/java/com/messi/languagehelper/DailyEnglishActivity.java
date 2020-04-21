package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;

public class DailyEnglishActivity extends BaseActivity implements View.OnClickListener {

    private final String Hearder = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";

    @BindView(R.id.start_btn)
    TextView start_btn;
    @BindView(R.id.english_tv)
    TextView english_tv;
    @BindView(R.id.chinese_tv)
    TextView chinese_tv;
    @BindView(R.id.start_btn_cover)
    FrameLayout start_btn_cover;

    private int maxRandom = 90000;
    private int skip;
    private StringBuilder sbResult = new StringBuilder();
    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private SimpleExoPlayer mExoPlayer;
    private AVObject mAVObject;
    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_english_activity);
        ButterKnife.bind(this);
        initViews();
        queryData();
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.title_daily_english));
        mSharedPreferences = Setings.getSharedPreferences(this);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        mExoPlayer.addListener(mEventListener);
        mAVObject = new AVObject();
        start_btn_cover.setOnClickListener(this);
    }

    private void random(){
        skip = (int) Math.round(Math.random()*maxRandom);
    }

    private void queryData() {
        random();
        showProgressbar();
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationDetail.EvaluationDetail);
        //        query.whereEqualTo(AVOUtil.EvaluationDetail.ECCode, ECCode);
        query.whereEqualTo(AVOUtil.EvaluationDetail.EDIsValid, "1");
        query.limit(1);
        query.skip(skip);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                hideProgressbar();
                if (avObjects != null && avObjects.size() > 0) {
                    mAVObject = avObjects.get(0);
                }
            }
        });
    }

    private String getEnglishContent() {
        String temp = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent);
        String[] str = temp.split("#");
        if (str.length > 1) {
            return temp.split("#")[0];
        } else {
            return temp;
        }
    }

    private String getChineseContent() {
        String temp = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent);
        String[] str = temp.split("#");
        if (str.length > 1) {
            return temp.split("#")[1];
        } else {
            return "";
        }
    }

    private void onFinishPlay(){
        english_tv.setText(getEnglishContent());
        chinese_tv.setText(getChineseContent());
    }


    public void playItem() {
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.EvaluationDetail.mp3))) {
            playMp3(mAVObject.getString(AVOUtil.EvaluationDetail.mp3));
        } else {
            showProgressbar();
            XFUtil.showSpeechSynthesizer(DailyEnglishActivity.this, mSharedPreferences,
                    mSpeechSynthesizer, getEnglishContent(), XFUtil.SpeakerEn,
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
                                ToastUtil.diaplayMesShort(DailyEnglishActivity.this, arg0.getErrorDescription());
                            }
                            onFinishPlay();
                        }

                        @Override
                        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                        }

                        @Override
                        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                        }
                    });
        }
    }

    private void playMp3(String media_url) {
        final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(CONTENT_TYPE_MUSIC)
                .setUsage(USAGE_MEDIA)
                .build();
        mExoPlayer.setAudioAttributes(audioAttributes);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, Hearder));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(media_url));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn_cover:
                playItem();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XFUtil.closeSpeechSynthesizer(mSpeechSynthesizer);
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
            mExoPlayer.removeListener(mEventListener);
            mExoPlayer = null;
        }
    }

    public class ExoPlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            LogUtil.DefalutLog("---onTimelineChanged---");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            LogUtil.DefalutLog("---onTracksChanged---");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            LogUtil.DefalutLog("---onLoadingChanged---");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            LogUtil.DefalutLog("---onPlayerStateChanged---");
            switch (playbackState) {
                case Player.STATE_IDLE:

                    break;
                case Player.STATE_BUFFERING:

                    break;
                case Player.STATE_READY:

                    break;
                case Player.STATE_ENDED:
                    onFinishPlay();
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            LogUtil.DefalutLog("---onRepeatModeChanged---");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            LogUtil.DefalutLog("---onShuffleModeEnabledChanged---");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            LogUtil.DefalutLog("---onPositionDiscontinuity---");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            LogUtil.DefalutLog("---onPlaybackParametersChanged---");
        }

        @Override
        public void onSeekProcessed() {
            LogUtil.DefalutLog("---onSeekProcessed---");
        }
    }
}
