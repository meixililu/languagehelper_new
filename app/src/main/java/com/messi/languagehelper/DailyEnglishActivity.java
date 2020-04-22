package com.messi.languagehelper;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.flexbox.FlexboxLayout;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.databinding.DailyEnglishActivityBinding;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import java.util.List;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;

public class DailyEnglishActivity extends BaseActivity implements View.OnClickListener {

    private final String Hearder = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";

    private int maxRandom = 90000;
    private int skip;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private SimpleExoPlayer mExoPlayer;
    private AVObject mAVObject;
    private DailyEnglishActivityBinding binding;
    private int selectedNum;
    private StringBuilder sb;
    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DailyEnglishActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initViews();
        guide();
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.title_daily_english));
        sb = new StringBuilder();
        mSharedPreferences = Setings.getSharedPreferences(this);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        mExoPlayer.addListener(mEventListener);
        binding.startBtnCover.setOnClickListener(this);
        binding.retryTv.setOnClickListener(view -> init(false));
        binding.refreshTv.setOnClickListener(view -> queryData());
        binding.promptTv.setOnClickListener(view -> prompt());
        binding.contentLayout.setOnClickListener(view -> playItem());
        binding.chineseTv.setOnClickListener(view -> playItem());
    }

    private void guide() {
        if (!mSharedPreferences.getBoolean(KeyUtil.isDailyEnglishGuideShow, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("");
            builder.setCancelable(false);
            builder.setMessage("先听录音，然后拼写句子，每天练一练，不认识的单词点一下就懂了，英语水平涨涨涨，666！");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    queryData();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
//            Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.isDailyEnglishGuideShow, true);
        }else {
            queryData();
        }
    }

    private void random(){
        skip = (int) Math.round(Math.random()*maxRandom);
    }

    private void queryData() {
        binding.autoWrapLayout.setVisibility(View.GONE);
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
                    init(true);
                }
            }
        });
    }

    private void init(boolean isPlay){
        sb.setLength(0);
        selectedNum = 0;
        setWordName();
        wordToCharacter();
        if (isPlay) {
            playItem();
        }
    }

    private void prompt(){
        binding.chineseTv.setText(getEnglishContent());
        new Handler().postDelayed(() -> {
            binding.chineseTv.setText("");
        },3500);
    }

    private void setWordName(){
        binding.chineseTv.setText("");
        binding.englishTv.setTextColor(getResources().getColor(R.color.text_black));
        String content = getEnglishContent();
        int count = content.split(" ").length - selectedNum;
        TextHandlerUtil.handlerText(this, mProgressbar, binding.englishTv, sb.toString());
//        binding.englishTv.setText(sb.toString());
        if (count > 0) {
            for(int i = 0; i<count; i++){
                binding.englishTv.append("__  ");
            }
        } else {
            if (content.trim().equals(sb.toString().trim())) {
                binding.englishTv.setTextColor(getResources().getColor(R.color.theme_color_green));
                binding.startBtn.setText("Next");
                binding.chineseTv.setText(getChineseContent());
            } else {
                binding.englishTv.setTextColor(getResources().getColor(R.color.theme_color_red));
            }
        }

    }

    private void wordToCharacter(){
        binding.autoWrapLayout.removeAllViews();
        String content = getEnglishContent();
        String[] contents = content.split(" ");
        int[] nums = NumberUtil.getNumberOrderWithoutRepeat(contents.length,0, contents.length, false);
        for(int index : nums){
            binding.autoWrapLayout.addView(createNewFlexItemTextView(contents[index].trim()));
        }
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
        binding.autoWrapLayout.setVisibility(View.VISIBLE);
    }

    public void playItem() {
        if (mAVObject == null) {
            return;
        }
        binding.autoWrapLayout.setVisibility(View.GONE);
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
                if (binding.startBtn.getText().toString().equals("Next")) {
                    binding.startBtn.setText("Play");
                    queryData();
                } else {
                    playItem();
                }
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

    public void OnItemClick(String word) {
        selectedNum++;
        sb.append(word+" ");
        setWordName();
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
            LogUtil.DefalutLog("---onPlayerError---");
            queryData();
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

    private TextView createNewFlexItemTextView(final String word) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(word);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.text_black));
        textView.setOnClickListener(view -> {
            if (TextUtils.isEmpty((String)textView.getTag())) {
                textView.setTextColor(getResources().getColor(R.color.none));
                textView.setTag("shown");
                OnItemClick(word);
            }
        });
        int padding = ScreenUtil.dip2px(this,5);
        int paddingLeftAndRight = ScreenUtil.dip2px(this,7);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ScreenUtil.dip2px(this,5);
        int marginTop = ScreenUtil.dip2px(this,5);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }
}
