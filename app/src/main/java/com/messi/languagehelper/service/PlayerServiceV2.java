package com.messi.languagehelper.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.messi.languagehelper.aidl.IXBPlayer;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NotificationUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.messi.languagehelper.util.KeyUtil.NotificationTitle;

/**
 * Created by luli on 05/05/2017.
 */

public class PlayerServiceV2 extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    public static final String action_loading = "com.messi.languagehelper.music.loading";
    public static final String action_finish_loading = "com.messi.languagehelper.music.finish.loading";
    public static final String action_restart = "com.messi.languagehelper.music.restart";
    public static final String action_start = "com.messi.languagehelper.music.start";
    public static final String action_pause = "com.messi.languagehelper.music.pause";
    public static final String action_next = "com.messi.languagehelper.music.next";
    public static final String action_previous = "com.messi.languagehelper.music.previous";
    public static final String action_close = "com.messi.languagehelper.music.close";

    private AudioManager mAudioManager;
    private WifiManager.WifiLock mWifiLock;
    private MediaPlayer player;
    // 0 default, 1 playing, 2 pause
    public int PlayerStatus;
    public String lastSongId = "";
    private Reading song;
    private boolean isPlayList;
    private List<Reading> list;
    private long lastLoadDataTime;
    private int currentPosition;
    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();
    private MyIXmPlayerStatusListener mMyIXmPlayerStatusListener = new MyIXmPlayerStatusListener();

    public IBinder musicBind = new IXBPlayer.Stub(){

        @Override
        public void initAndPlay(String song,boolean isPlayList) throws RemoteException {
            Reading data = JSON.parseObject(song,Reading.class);
            InitAndPlay(data);
        }

        @Override
        public void initPlayList(String lists, int position) throws RemoteException {
            List<Reading> list = JSON.parseArray(lists, Reading.class);
            InitPlayList(list,position);
        }

        @Override
        public int getPlayStatus() throws RemoteException {
            return PlayerStatus;
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return getCPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return getDurationNum();
        }

        @Override
        public boolean MPlayerIsPlaying() throws RemoteException {
            return isPlaying();
        }

        @Override
        public void MPlayerPause() throws RemoteException {
            pause();
        }

        @Override
        public void MPlayerRestart() throws RemoteException {
            restart();
        }

        @Override
        public void MPlayerSeekTo(int position) throws RemoteException {
            seekTo(position);
        }

        @Override
        public boolean MPlayerIsSameMp3(String oid) throws RemoteException {
            return isSameMp3(oid);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            NotificationUtil.sendBroadcast(PlayerServiceV2.this,action_finish_loading);
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
        initPlayer();
    }

    private void initPlayer(){
        player = new MediaPlayer();
        Context applicationContext = this.getApplicationContext();
        this.mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.mWifiLock = ((WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock");
        XmPlayerManager.getInstance(this).addPlayerStatusListener(mMyIXmPlayerStatusListener);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    private void stopXMLYPlayer(){
        if(XmPlayerManager.getInstance(this).isPlaying()){
            XmPlayerManager.getInstance(this).pause();
        }
    }

    public void InitPlayList(List<Reading> list,int position){
        LogUtil.DefalutLog("position:"+position+"---list:"+list.size());
        stopXMLYPlayer();
        isPlayList = true;
        this.list = list;
        currentPosition = position;
        if(list != null && list.size() > position){
            Reading item = list.get(position);
            currentPosition++;
            if(item != null && "mp3".equals(item.getType())){
                startToPlay(item);
            }else {
                InitPlayList(list,currentPosition);
            }
        }else {
            LogUtil.DefalutLog("finish play,load more data.");
            try {
                getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void InitAndPlay(Reading song){
        LogUtil.DefalutLog("initAndPlay---Reading:"+song);
        stopXMLYPlayer();
        if (song != null) {
            if(isSameMp3(song)){
                playOrPause();
            }else {
                isPlayList = true;
                List<Reading> list = new ArrayList<>();
                list.add(song);
                InitPlayList(list,0);
            }
        }

    }

    private void startToPlay(Reading song){
        if(!isSameMp3(song)){
            startExoplayer(song);;
        }else {
            playOrPause();
        }
    }

    private void playOrPause(){
        if(PlayerStatus == 1){
            pause();
        }else if(PlayerStatus == 2 || PlayerStatus == 0){
            restart();
        }
    }

    private void startExoplayer(Reading song){
        try{
            LogUtil.DefalutLog("startExoplayer:"+song.getMedia_url());
            this.song = song;
            lastSongId = song.getObject_id();
            setPlayingBroadcast();
            player.reset();
            player.setDataSource(song.getMedia_url());
            player.prepareAsync();
//            mWifiLock.acquire();
        } catch(Exception e){
            LogUtil.DefalutLog("MUSIC SERVICE Error setting data source");
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.DefalutLog("PlayerService---onStartCommand---");
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getStringExtra(KeyUtil.MesType);
            String title = intent.getStringExtra(NotificationTitle);
            LogUtil.DefalutLog("onStartCommand:"+action+"---MesType:"+type);
            if (!TextUtils.isEmpty(action)) {
                if(NotificationUtil.mes_type_zyhy.equals(type)){
                    if (action.equals(action_restart)) {
                        restart();
                    }else if(action.equals(action_start)) {

                    }else if(action.equals(action_pause)) {
                        pause();
                    }else if(action.equals(action_next)) {

                    }else if(action.equals(action_previous)) {

                    }else if(action.equals(action_close)) {
                        XmPlayerManager.getInstance(this).pause();
                        pause();
                        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Setings.NOTIFY_ID);
                    }
                }else if(NotificationUtil.mes_type_xmly.equals(type)){
                    if (action.equals(action_restart)) {
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
        NotificationUtil.showNotification(this, action_restart,title, NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this, action_restart);
    }

    public boolean isSameMp3(Reading song){
        return lastSongId.equals(song.getObject_id());
    }

    public boolean isSameMp3(String oid){
        return lastSongId.equals(oid);
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

    public void pause(){
        if(song != null && player != null){
            setPause();
            NotificationUtil.showNotification(this, action_restart,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this, action_restart);
    }

    public void setPause(){
        PlayerStatus = 2;
        player.pause();
    }

    public void restart(){
        if(song != null && player != null){
            PlayerStatus = 1;
            player.start();
            NotificationUtil.showNotification(this,action_pause,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this,action_pause);
    }

    public boolean isPlaying(){
        return PlayerStatus == 1;
    }

    private void setPlayingBroadcast(){
        PlayerStatus = 1;
        NotificationUtil.showNotification(PlayerServiceV2.this,action_pause,song.getTitle(),
                NotificationUtil.mes_type_zyhy);
        NotificationUtil.sendBroadcast(PlayerServiceV2.this,action_pause);
    }

    private void setPauseBroadcast(){
        PlayerStatus = 0;
        NotificationUtil.sendBroadcast(PlayerServiceV2.this, action_restart);
        NotificationUtil.showNotification(PlayerServiceV2.this, action_restart,song.getTitle(),
                NotificationUtil.mes_type_zyhy);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setPauseBroadcast();
        if(isPlayList){
            InitPlayList(list,currentPosition);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        setPauseBroadcast();
        player.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        setPlayingBroadcast();
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
                        NotificationUtil.showNotification(PlayerServiceV2.this,action_pause,song.getTitle(),
                                NotificationUtil.mes_type_zyhy);
                        NotificationUtil.sendBroadcast(PlayerServiceV2.this,action_pause);
                    }
                case Player.STATE_READY:
                    LogUtil.DefalutLog("STATE_READY");
                    if(playWhenReady){

                    }
                    break;
                case Player.STATE_ENDED:
                    LogUtil.DefalutLog("STATE_ENDED:");

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

    public int getCPosition(){
        if(player != null){
            return (int)player.getCurrentPosition();
        }
        return 0;
    }

    public int getDurationNum(){
        if(player != null){
            return (int)player.getDuration();
        }
        return 0;
    }

    public void seekTo(int position){
        if(player != null){
            player.seekTo(position);
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
            NotificationUtil.sendBroadcast(PlayerServiceV2.this, action_restart);
        }

        @Override
        public void onSoundPlayComplete() {

        }

        @Override
        public void onSoundPrepared() {

        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
            if (XmPlayerManager.getInstance(PlayerServiceV2.this).getCurrSound() instanceof Track) {
                Track currentTrack = (Track) XmPlayerManager.getInstance(PlayerServiceV2.this).getCurrSound();
                NotificationUtil.showNotification(PlayerServiceV2.this,action_pause,currentTrack.getTrackTitle(),
                        NotificationUtil.mes_type_xmly);
            }
            NotificationUtil.sendBroadcast(PlayerServiceV2.this,action_pause);
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

    public void getData() throws Exception{
        if ((System.currentTimeMillis() - lastLoadDataTime) > 1000*3) {
            lastLoadDataTime = System.currentTimeMillis();
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
            query.whereEqualTo(AVOUtil.Reading.type, "mp3");
            if(song != null && !TextUtils.isEmpty(song.getCategory_2())){
                query.whereEqualTo(AVOUtil.Reading.category_2, song.getCategory_2());
                if(!TextUtils.isEmpty(song.getItem_id())){
                    query.whereGreaterThan(AVOUtil.Reading.item_id, Long.parseLong(song.getItem_id()));
                }
                query.addAscendingOrder(AVOUtil.Reading.item_id);
            }else {
                if(song != null && !TextUtils.isEmpty(song.getPublish_time())){
                    query.whereLessThan(AVOUtil.Reading.publish_time, new Date(Long.parseLong(song.getPublish_time())));
                }
                query.addDescendingOrder(AVOUtil.Reading.publish_time);
            }
            query.limit(30);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> avObjects, AVException avException) {
                    LogUtil.DefalutLog("findInBackground---done:"+avObjects.size());
                    if (NullUtil.isNotEmpty(avObjects) && NullUtil.isNotEmpty(list)) {
                        DataUtil.changeDataToReading(avObjects,list,false);
                        InitPlayList(list,currentPosition);
                    }else {
                        player.seekTo(0);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.DefalutLog("PlayerService---onDestroy---");
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

}
