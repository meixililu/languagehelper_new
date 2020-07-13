package com.messi.languagehelper.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.MyPlayerListener;
import com.messi.languagehelper.impl.PCMAudioPlayerListener;

public class MyPlayer {

    public static MyPlayer myPlayer;
    public static String playUrl = "http://zyhy.mzxbkj.com/v1/word/sound/";
    private SimpleExoPlayer exoPlayer;
    private Context context;
    private String lastContent;
    private MyPlayerListener listener;
    private ExoPlayerEventListener mExoPlayerEventListener;
    private boolean isDownload;

    public static MyPlayer getInstance(Context context){
        if (myPlayer == null) {
            myPlayer = new MyPlayer(context);
        }
        return myPlayer;
    }

    public MyPlayer(Context mContext){
        isDownload = true;
        context = mContext.getApplicationContext();
        mExoPlayerEventListener = new ExoPlayerEventListener();
        exoPlayer = new SimpleExoPlayer.Builder(context).build();
        exoPlayer.addListener(mExoPlayerEventListener);
    }

    public void startMedia(String media_url){
        start("","",media_url,null);
    }

    public void startWithSpeaker(String content, String speaker){
        start(content,"",speaker,null);
    }

    public void start(String content){
        LogUtil.DefalutLog("isContainChinese:"+StringUtils.isContainChinese(content));
        start(content,"","",null);
    }

    public void start(String content, SynthesizerListener mListener){
        start(content,"","",mListener);
    }

    public void start(String content, String media_url){
        start(content, media_url,"", null);
    }

    public void start(String content, String media_url, String speaker, SynthesizerListener mListener){
        if (isPlaying()) {
            stop();
            if (!content.equals(lastContent)) {
                play(content, media_url, speaker, mListener);
            }
        } else {
            play(content, media_url, speaker, mListener);
        }
    }

    public void play(String content, String media_url, String speaker, SynthesizerListener mListener){
        LogUtil.DefalutLog("---play---");
        if (!TextUtils.isEmpty(content)) {
            lastContent = content;
            String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
            String md5 = MD5.encode(content);
            String fileName = md5 + ".mp3";
            String mp3Path = path + fileName;
            String pcmPath = path + md5 + ".pcm";
            if (SDCardUtil.isFileExist(mp3Path)) {
                playMediaUrl(mp3Path);
                return;
            }else if (SDCardUtil.isFileExist(pcmPath)) {
                if (!TextUtils.isEmpty(media_url)) {
                    playMediaUrl(media_url);
                    if (isDownload) {
                        DownLoadUtil.downloadFile(context, media_url, SDCardUtil.sdPath, fileName, null);
                    } else {
                        isDownload = true;
                    }
                } else {
                    playPcm(pcmPath);
                }
                return;
            }else if (!TextUtils.isEmpty(media_url)) {
                if (isDownload) {
                    DownLoadUtil.downloadFile(context, media_url, SDCardUtil.sdPath, fileName, null);
                } else {
                    isDownload = true;
                }
            }
        }
        if (!TextUtils.isEmpty(media_url)) {
            if(!TextUtils.isEmpty(content)){
                lastContent = content;
            }else {
                lastContent = media_url;
            }
            playMediaUrl(media_url);
        } else {
            if (StringUtils.isContainChinese(content)) {
                playTTS(content, speaker, mListener);
            } else {
                String url = playUrl + content;
                start(content,url,"",null);
            }

        }
    }

    public void playTTS(String content, String speaker, SynthesizerListener mListener){
        LogUtil.DefalutLog("---playTTS---");
        if (!TextUtils.isEmpty(content)) {
            lastContent = content;
            if (mListener == null) {
                showSpeechSynthesizer(context, content, speaker, mSynthesizerListener);
            } else {
                showSpeechSynthesizer(context, content, speaker, mListener);
            }
        }
    }

    public void playPcm(String filePath){
        LogUtil.DefalutLog("---playPcm---"+filePath);
        if (!TextUtils.isEmpty(filePath)) {
            PCMAudioPlayer.getInstance().setListener(new PCMAudioPlayerListener() {
                @Override
                public void onStart() {
                    MyPlayer.this.onStart();
                }

                @Override
                public void onFinish() {
                    MyPlayer.this.onFinish();
                }
            });
            PCMAudioPlayer.getInstance().startPlay(filePath);
        }
    }

    public void playMediaUrl(String url){
        LogUtil.DefalutLog("---playMediaUrl---"+url);
        if (!TextUtils.isEmpty(url)) {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, LanguagehelperHttpClient.Header);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    public boolean isPlaying(){
        return PCMAudioPlayer.getInstance().isPlaying()
                || ( exoPlayer.getPlaybackState() == Player.STATE_READY && exoPlayer.getPlayWhenReady() )
                || ( SpeechSynthesizer.getSynthesizer() != null && SpeechSynthesizer.getSynthesizer().isSpeaking() );
    }

    public void stop(){
        try {
            PCMAudioPlayer.getInstance().stopPlay();
            exoPlayer.stop();
            SpeechSynthesizer.getSynthesizer().stopSpeaking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        stop();
        listener = null;
    }

    public static void showSpeechSynthesizer(Context mContext,
                                             String source,
                                             String speaker,
                                             SynthesizerListener mSynthesizerListener) {
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        String filepath = path + MD5.encode(source) + ".pcm";
        SpeechSynthesizer mSpeechSynthesizer = SpeechSynthesizer.getSynthesizer();
        if (mSpeechSynthesizer == null) {
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext,null);
        }
        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
        if (TextUtils.isEmpty(speaker)) {
            StringUtils.setSpeaker(source);
            mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, Setings.role);
        } else {
            mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, speaker);
        }
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
                String.valueOf( Setings.getSharedPreferences(mContext).getInt("tts_speed", 50) ));
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");//离线 local
        mSpeechSynthesizer.startSpeaking(source, mSynthesizerListener);
    }

    public void setListener(MyPlayerListener listener){
        this.listener = listener;
    }

    public class ExoPlayerEventListener implements Player.EventListener {

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
                    LogUtil.DefalutLog("STATE_IDLE");
                    break;
                case Player.STATE_BUFFERING:
                    LogUtil.DefalutLog("STATE_BUFFERING");
                    break;
                case Player.STATE_READY:
                    LogUtil.DefalutLog("STATE_READY");
                    if (playWhenReady) {
                        onStart();
                    }
                    break;
                case Player.STATE_ENDED:
                    onFinish();
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

    private SynthesizerListener mSynthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            onStart();
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
            onFinish();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public void onStart(){
        LogUtil.DefalutLog("MyPlayer onStart");
        if (listener != null) {
            listener.onStart();
        }
    }

    public void onFinish(){
        LogUtil.DefalutLog("MyPlayer onFinish");
        if (listener != null) {
            listener.onFinish();
        }
    }

    public static void release(){
        try {
            if (myPlayer != null) {
                myPlayer.context = null;
                myPlayer.exoPlayer.stop();
                myPlayer.exoPlayer.release();
                if (SpeechSynthesizer.getSynthesizer() != null) {
                    SpeechSynthesizer.getSynthesizer().stopSpeaking();
                    SpeechSynthesizer.getSynthesizer().destroy();
                }
                myPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MediaSource getMediaSource(String url){
        return getMediaSource(url,"","");
    }

    public static MediaSource getMediaSource(String url, String bkurl, String sUrl){
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(
                LanguagehelperHttpClient.Header,
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);
        MediaSource mediaSource = null;
        if (url.contains("bilivideo")) {
            dataSourceFactory.getDefaultRequestProperties().set("range","bytes=0-");
            dataSourceFactory.getDefaultRequestProperties().set("referer",sUrl);
            dataSourceFactory.getDefaultRequestProperties().set("user-agent",LanguagehelperHttpClient.Header);
            if (!TextUtils.isEmpty(bkurl)) {
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(bkurl));
            }else {
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(url));
            }
        } else {
            mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
        }
        return mediaSource;
    }
}
