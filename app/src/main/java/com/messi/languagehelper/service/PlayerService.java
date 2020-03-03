package com.messi.languagehelper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NotificationUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;
import static com.messi.languagehelper.util.KeyUtil.MesType;
import static com.messi.languagehelper.util.KeyUtil.NotificationTitle;

/**
 * Created by luli on 05/05/2017.
 */

public class PlayerService extends Service {

    public static final String action_loading = "com.messi.languagehelper.music.loading";
    public static final String action_finish_loading = "com.messi.languagehelper.music.finish.loading";
    public static final String action_start = "com.messi.languagehelper.music.start";
    public static final String action_pause = "com.messi.languagehelper.music.pause";
    public static final String action_next = "com.messi.languagehelper.music.next";
    public static final String action_previous = "com.messi.languagehelper.music.previous";

    private AudioManager mAudioManager;
    private WifiManager.WifiLock mWifiLock;
    private SimpleExoPlayer mExoPlayer;
    // 0 default, 1 playing, 2 pause
    public int PlayerStatus;
    public String lastSongId = "";
    private Reading song;
    private boolean isPlayList;
    private List<Reading> list;
    private int currentPosition;
    private final IBinder musicBind = new MusicBinder();
    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();
    private MyIXmPlayerStatusListener mMyIXmPlayerStatusListener = new MyIXmPlayerStatusListener();

    public class MusicBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            NotificationUtil.sendBroadcast(PlayerService.this,action_finish_loading);
            LogUtil.DefalutLog("receive Handler:"+msg.what);
            if (msg.what == 1) {
                if(song != null){
                    startExoplayer(song);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.DefalutLog("PlayerService---onCreate---");
//        Setings.musicSrv = this;
        initExoplayer();
    }

    private void initExoplayer(){
        Context applicationContext = this.getApplicationContext();
        this.mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.mWifiLock = ((WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock");
        XmPlayerManager.getInstance(this).addPlayerStatusListener(mMyIXmPlayerStatusListener);
    }

    public void initPlayList(List<Reading> list,int position){
        LogUtil.DefalutLog("position:"+position);
        isPlayList = true;
        this.list = list;
        currentPosition = position;
        if(list != null && list.size() > position){
            Reading item = list.get(position);
            if(item.getType() != null && item.getType().equals("mp3")){
                LogUtil.DefalutLog("position:"+position);
                startToPlay(item);
            }else {
                currentPosition++;
                initPlayList(list,currentPosition);
            }
        }
    }

    public void initAndPlay(Reading song){
        isPlayList = false;
        startToPlay(song);
    }

    private void startToPlay(Reading song){
        if(XmPlayerManager.getInstance(this).isPlaying()){
            XmPlayerManager.getInstance(this).pause();
        }
        if(!isSameMp3(song)){
            checkMp3IsExit(song);
        }else {
            if(PlayerStatus == 1){
                pause();
            }else if(PlayerStatus == 2 || PlayerStatus == 0){
                restart();
            }
        }
    }

    private void checkMp3IsExit(Reading mAVObject) {
        if (mAVObject != null) {
            this.song = mAVObject;
            startExoplayer(mAVObject);
        }
    }

    private void startExoplayer(Reading song){
        try {
            LogUtil.DefalutLog("startExoplayer:"+song.getMedia_url());
            currentPosition++;
            this.song = song;
            lastSongId = song.getObject_id();
            PlayerStatus = 1;
            if (mExoPlayer == null) {
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
                mExoPlayer.addListener(mEventListener);
            }
            final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build();
            mExoPlayer.setAudioAttributes(audioAttributes);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                            Util.getUserAgent(this, "LanguageHelper"));
            MediaSource mediaSource = new ExtractorMediaSource
                    .Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(song.getMedia_url()));

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mWifiLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.DefalutLog("PlayerService---onStartCommand---");
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getStringExtra(MesType);
            String title = intent.getStringExtra(NotificationTitle);
            LogUtil.DefalutLog("onStartCommand:"+action+"---MesType:"+type);
            if (!TextUtils.isEmpty(action)) {
                if(NotificationUtil.mes_type_zyhy.equals(type)){
                    if (action.equals(action_start)) {
                        restart();
                    }else if(action.equals(action_pause)) {
                        pause();
                    }else if(action.equals(action_next)) {

                    }else if(action.equals(action_previous)) {

                    }
                }else if(NotificationUtil.mes_type_xmly.equals(type)){
                    if (action.equals(action_start)) {
                        xmly_play(title);
                    }else if(action.equals(action_pause)) {
                        xmly_pause(title);
                    }else if(action.equals(action_next)) {

                    }else if(action.equals(action_previous)) {

                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void xmly_play(String title){
        XmPlayerManager.getInstance(this).play();
        NotificationUtil.showNotification(this,action_pause,title, NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this,action_pause);
    }

    public void xmly_pause(String title){
        XmPlayerManager.getInstance(this).pause();
        NotificationUtil.showNotification(this,action_start,title, NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this,action_start);
    }

    public boolean isSameMp3(Reading song){
        return lastSongId.equals(song.getObject_id());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.DefalutLog("PlayerService---onBind---");
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.DefalutLog("PlayerService---onUnbind---");
        return false;
    }

    @Override
    public void onDestroy() {
        LogUtil.DefalutLog("PlayerService---onDestroy---");
        super.onDestroy();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
            mExoPlayer.removeListener(mEventListener);
            mExoPlayer = null;
        }
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    public void pause(){
        if(song != null && mExoPlayer != null){
            PlayerStatus = 2;
            mExoPlayer.setPlayWhenReady(false);
            NotificationUtil.showNotification(this,action_start,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this,action_start);
    }

    public void restart(){
        if(song != null && mExoPlayer != null){
            PlayerStatus = 1;
            mExoPlayer.setPlayWhenReady(true);
            NotificationUtil.showNotification(this,action_pause,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this,action_pause);
    }

    public boolean isPlaying(){
        return PlayerStatus == 1;
    }

    private final class ExoPlayerEventListener implements Player.EventListener {

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
                    LogUtil.DefalutLog("STATE_IDLE");
                case Player.STATE_BUFFERING:
                    LogUtil.DefalutLog("STATE_BUFFERING");
                    if(playWhenReady){
                        NotificationUtil.showNotification(PlayerService.this,action_pause,song.getTitle(),
                                NotificationUtil.mes_type_zyhy);
                        NotificationUtil.sendBroadcast(PlayerService.this,action_pause);
                    }
                case Player.STATE_READY:
                    LogUtil.DefalutLog("STATE_READY");
                    if(playWhenReady){
                        PlayerStatus = 1;
                        NotificationUtil.showNotification(PlayerService.this,action_pause,song.getTitle(),
                                NotificationUtil.mes_type_zyhy);
                        NotificationUtil.sendBroadcast(PlayerService.this,action_pause);
                    }
                    break;
                case Player.STATE_ENDED:
                    LogUtil.DefalutLog("STATE_ENDED:"+playWhenReady);
                    PlayerStatus = 0;
                    NotificationUtil.showNotification(PlayerService.this,action_start,song.getTitle(),
                            NotificationUtil.mes_type_zyhy);
                    NotificationUtil.sendBroadcast(PlayerService.this,action_start);
                    if(isPlayList){
                        initPlayList(list,currentPosition);
                    }else {
                        mExoPlayer.seekTo(0);
                        mExoPlayer.setPlayWhenReady(false);
                    }
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
            PlayerStatus = 0;
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

    public int getCurrentPosition(){
        if(mExoPlayer != null){
            return (int)mExoPlayer.getContentPosition();
        }
        return 0;
    }

    public int getDuration(){
        if(mExoPlayer != null){
            return (int)mExoPlayer.getDuration();
        }
        return 0;
    }

    public void seekTo(int position){
        if(mExoPlayer != null){
            mExoPlayer.seekTo(position);
        }
    }

    private final class MyIXmPlayerStatusListener implements IXmPlayerStatusListener {

        @Override
        public void onPlayStart() {

        }

        @Override
        public void onPlayPause() {

        }

        @Override
        public void onPlayStop() {
            NotificationUtil.sendBroadcast(PlayerService.this,action_start);
        }

        @Override
        public void onSoundPlayComplete() {

        }

        @Override
        public void onSoundPrepared() {

        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
            if (XmPlayerManager.getInstance(PlayerService.this).getCurrSound() instanceof Track) {
                Track currentTrack = (Track) XmPlayerManager.getInstance(PlayerService.this).getCurrSound();
                NotificationUtil.showNotification(PlayerService.this,action_pause,currentTrack.getTrackTitle(),
                        NotificationUtil.mes_type_xmly);
            }
            NotificationUtil.sendBroadcast(PlayerService.this,action_pause);
        }

        @Override
        public void onBufferingStart() {

        }

        @Override
        public void onBufferingStop() {

        }

        @Override
        public void onBufferProgress(int i) {

        }

        @Override
        public void onPlayProgress(int i, int i1) {

        }

        @Override
        public boolean onError(XmPlayerException e) {
            return false;
        }
    }

}
