package com.messi.languagehelper.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.messi.languagehelper.R;
import com.messi.languagehelper.aidl.IXBPlayer;
import com.messi.languagehelper.bean.PVideoResult;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.httpservice.RetrofitApiService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.NotificationUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SignUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;
import cn.leancloud.json.JSON;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;
import static com.messi.languagehelper.util.KeyUtil.NotificationTitle;

/**
 * Created by luli on 05/05/2017.
 */

public class PlayerService extends Service {

    private final String Hearder = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";
    public static final String action_loading = "com.messi.languagehelper.music.loading";
    public static final String action_finish_loading = "com.messi.languagehelper.music.finish.loading";
    public static final String action_restart = "com.messi.languagehelper.music.restart";
    public static final String action_start = "com.messi.languagehelper.music.start";
    public static final String action_pause = "com.messi.languagehelper.music.pause";
    public static final String action_next = "com.messi.languagehelper.music.next";
    public static final String action_previous = "com.messi.languagehelper.music.previous";
    public static final String action_close = "com.messi.languagehelper.music.close";

    private WifiManager.WifiLock mWifiLock;
    private SimpleExoPlayer mExoPlayer;
    // 0 default, 1 playing, 2 pause
    public int PlayerStatus;
    public String lastSongId = "";
    public String lastPlayer = IPlayerUtil.PlayerXBKJ;
    private Reading song;
    private boolean isAppExit;
    private boolean isPlayList;
    private boolean isForeground;
    private List<Reading> list;
    private long lastLoadDataTime;
    private long currentPlayPosition;
    private int currentPosition;
    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();
    private MyIXmPlayerStatusListener mMyIXmPlayerStatusListener = new MyIXmPlayerStatusListener();

    public IBinder musicBind = new IXBPlayer.Stub(){

        @Override
        public void initAndPlay(String song, boolean isPlayList, long cPosition) throws RemoteException {
            Reading data = JSON.parseObject(song,Reading.class);
            currentPlayPosition = cPosition;
            InitAndPlay(data, isPlayList);
        }

        @Override
        public void initPlayList(String lists, int position) throws RemoteException {
            isPlayList = true;
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

        @Override
        public void setLastPlayer(String player) throws RemoteException {
            lastPlayer = player;
        }

        @Override
        public String getLastPlayer() throws RemoteException {
            return lastPlayer;
        }

        @Override
        public void setAppExit(boolean isExit) throws RemoteException {
            isAppExit = isExit;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.DefalutLog("PlayerService---onCreate---");
        initExoplayer();
    }

    private void initExoplayer(){
        Context applicationContext = this.getApplicationContext();
        this.mWifiLock = ((WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock");
        XmPlayerManager.getInstance(this).init();
        XmPlayerManager.getInstance(this).addPlayerStatusListener(mMyIXmPlayerStatusListener);
        initForeground();
    }

    private void initForeground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.getManager(this);
            Notification notification = NotificationUtil.getNotification(this,
                    action_restart,getString(R.string.app_des), NotificationUtil.mes_type_zyhy);
            startForeground(Setings.NOTIFY_ID,notification);
        }
        isForeground = true;
    }

    private void checkIsForeground(){
        LogUtil.DefalutLog("---checkIsForeground---");
        if (!isForeground) {
            initForeground();
        }
    }

    private void clearNotification(){
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Setings.NOTIFY_ID);
    }

    private void stopXMLYPlayer(){
        XmPlayerManager.getInstance(this).pause();
    }

    public void InitPlayList(List<Reading> list,int position){
        lastPlayer = IPlayerUtil.PlayerXBKJ;
        LogUtil.DefalutLog("position:"+position+"---list:"+list.size());
        checkIsForeground();
        stopXMLYPlayer();
        this.list = list;
        currentPosition = position;
        if(list != null && list.size() > position){
            Reading item = list.get(position);
            currentPosition++;
            if(item != null){
                startToPlay(item);
            }else {
                InitPlayList(list,currentPosition);
            }
        }else {
            loadMoreData();
        }
    }

    private void loadMoreData(){
        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InitAndPlay(Reading song, boolean isList){
        LogUtil.DefalutLog("initAndPlay---isList:"+isList);
        stopXMLYPlayer();
        if (song != null) {
            if(isSameMp3(song)){
                playOrPause();
            }else {
                isPlayList = isList;
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
        try {
            LogUtil.DefalutLog("title:" + song.getTitle() +"---startExoplayer:"+song.getMedia_url());
            if ("mp3".equals(song.getType()) && TextUtils.isEmpty(song.getMedia_url())) {
                LogUtil.DefalutLog("mp3,media url is null.");
                InitPlayList(list,currentPosition);
                return;
            }
            this.song = song;
            lastSongId = song.getObject_id();
            if (TextUtils.isEmpty(song.getMedia_url()) || "media_url".equals(song.getMedia_url())) {
                LogUtil.DefalutLog("title:" + song.getTitle() + "---parseUrl:"+song.getSource_url());
                parseVideoUrl(song.getSource_url());
                return;
            }

            String media_url = song.getMedia_url();
            PlayerStatus = 1;
            LogUtil.DefalutLog("media_url:" + media_url);
            if (mExoPlayer == null) {
                mExoPlayer = new SimpleExoPlayer.Builder(this).build();
                mExoPlayer.addListener(mEventListener);
            }
            final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build();
            mExoPlayer.setAudioAttributes(audioAttributes);
            MediaSource mediaSource = MyPlayer.getMediaSource(media_url,song.getBackup1(),song.getSource_url());
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
                        clearNotification();
                        stopForeground(true);
                        isForeground = false;
                        if (isAppExit) {
                            stopSelf();
                        }
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
        if(song != null && mExoPlayer != null){
            PlayerStatus = 2;
            mExoPlayer.setPlayWhenReady(false);
            NotificationUtil.showNotification(this, action_restart,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this, action_restart);
    }

    public void restart(){
        LogUtil.DefalutLog("song:"+song+"--mExoPlayer:"+mExoPlayer);
        if(song != null && mExoPlayer != null){
            PlayerStatus = 1;
            mExoPlayer.setPlayWhenReady(true);
            NotificationUtil.showNotification(this,action_pause,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }else {
            LogUtil.DefalutLog("PackageName:"+getPackageName());
            if (getPackageName().equals(Setings.application_id_zyhy)){
                loadMoreData();
                NotificationUtil.sendBroadcast(this,action_pause);
            }else {
                NotificationUtil.sendBroadcast(this,action_restart);
            }
        }
    }

    public boolean isPlaying(){
        return mExoPlayer.getPlaybackState() == Player.STATE_READY && mExoPlayer.getPlayWhenReady();
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
                    LogUtil.DefalutLog("STATE_IDLE");
                    break;
                case Player.STATE_BUFFERING:
                    LogUtil.DefalutLog("STATE_BUFFERING");
                    if(playWhenReady){
                        NotificationUtil.showNotification(PlayerService.this,action_pause,song.getTitle(),
                                NotificationUtil.mes_type_zyhy);
                        NotificationUtil.sendBroadcast(PlayerService.this,action_pause);
                    }
                    break;
                case Player.STATE_READY:
                    LogUtil.DefalutLog("STATE_READY");
                    if(playWhenReady){
                        PlayerStatus = 1;
                        NotificationUtil.showNotification(PlayerService.this,action_pause,song.getTitle(),
                                NotificationUtil.mes_type_zyhy);
                        NotificationUtil.sendBroadcast(PlayerService.this,action_pause);
                        if (currentPlayPosition > 0) {
                            mExoPlayer.seekTo(currentPlayPosition);
                            mExoPlayer.setPlayWhenReady(true);
                            currentPlayPosition = 0;
                        }
                    }
                    break;
                case Player.STATE_ENDED:
                    LogUtil.DefalutLog("STATE_ENDED:");
                    PlayerStatus = 0;
                    NotificationUtil.sendBroadcast(PlayerService.this, action_restart);
                    NotificationUtil.showNotification(PlayerService.this, action_restart,song.getTitle(),
                            NotificationUtil.mes_type_zyhy);
                    if(isPlayList){
                        InitPlayList(list,currentPosition);
                    }else {
                        mExoPlayer.seekTo(0);
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

    public int getCPosition(){
        if(mExoPlayer != null){
            return (int)mExoPlayer.getContentPosition();
        }
        return 0;
    }

    public int getDurationNum(){
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
            NotificationUtil.sendBroadcast(PlayerService.this, action_restart);
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

    public void getData() throws Exception{
        if ((System.currentTimeMillis() - lastLoadDataTime) > 1000*3) {
            LogUtil.DefalutLog("load more data");
            lastLoadDataTime = System.currentTimeMillis();
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
            if(song != null && !TextUtils.isEmpty(song.getBackup2())){
                LogUtil.DefalutLog("------1------");
                query.whereEqualTo(AVOUtil.Reading.type, song.getType());
                query.whereEqualTo(AVOUtil.Reading.category_2, song.getBackup2());
                if(!TextUtils.isEmpty(song.getItem_id())){
                    query.whereGreaterThan(AVOUtil.Reading.item_id, Long.parseLong(song.getItem_id()));
                }
                query.addAscendingOrder(AVOUtil.Reading.item_id);
            }else if (song != null && !TextUtils.isEmpty(song.getBoutique_code())) {
                LogUtil.DefalutLog("------2------");
                query.whereEqualTo(AVOUtil.Reading.type, song.getType());
                query.whereEqualTo(AVOUtil.Reading.boutique_code, song.getBoutique_code());
                if(!TextUtils.isEmpty(song.getPublish_time())){
                    query.whereLessThan(AVOUtil.Reading.publish_time, new Date(Long.parseLong(song.getPublish_time())));
                }
                query.addDescendingOrder(AVOUtil.Reading.publish_time);
                query.addDescendingOrder(AVOUtil.Reading.createdAt);
            } else {
                LogUtil.DefalutLog("------3------");
                if(song != null && !TextUtils.isEmpty(song.getPublish_time())){
                    LogUtil.DefalutLog("------4------");
                    query.whereLessThanOrEqualTo(AVOUtil.Reading.publish_time, new Date(Long.parseLong(song.getPublish_time())));
                }
                query.whereEqualTo(AVOUtil.Reading.type, "mp3");
                query.addDescendingOrder(AVOUtil.Reading.publish_time);
                query.addDescendingOrder(AVOUtil.Reading.createdAt);
            }
            query.limit(30);
            query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> avObjects, AVException avException) {
                    LogUtil.DefalutLog("findInBackground---done:"+avObjects.size());
                    if (NullUtil.isNotEmpty(avObjects)) {
                        if (!NullUtil.isNotEmpty(list)) {
                            list = new ArrayList<>();
                        }
                        if (song != null) {
                            if(song.getObject_id().equals(avObjects.get(0).getObjectId())){
                                avObjects.remove(0);
                                LogUtil.DefalutLog("song.getObject_id().equals(avObjects.get(0).getObjectId())");
                            }
                        }
                        isPlayList = true;
                        DataUtil.changeDataToReading(avObjects,list,false);
                        InitPlayList(list,currentPosition);
                    }else {
                        if (mExoPlayer != null) {
                            mExoPlayer.seekTo(0);
                        }
                    }
                }
            }));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LogUtil.DefalutLog("PlayerService---onDestroy---");
            stopForeground(true);
            isForeground = false;
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(false);
                mExoPlayer.release();
                mExoPlayer.removeListener(mEventListener);
                mExoPlayer = null;
            }
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseVideoUrl(String sourceUrl) {
        LogUtil.DefalutLog("service parseVideoUrl");
        String vid = "";
        if (song != null && !TextUtils.isEmpty(song.getVid())) {
            vid = song.getVid();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String platform = "android";
        String network = NetworkUtil.getNetworkType(this);
        String sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, sourceUrl, platform, network);
        RetrofitApiService service = RetrofitApiService.getRetrofitApiService(Setings.PVideoApi,
                RetrofitApiService.class);
        Call<PVideoResult> call = service.getPVideoApi(sourceUrl, network, platform, sign, timestamp, 0, vid);
        call.enqueue(new Callback<PVideoResult>() {
                 @Override
                 public void onResponse(Call<PVideoResult> call, Response<PVideoResult> response) {
                     LogUtil.DefalutLog("onResponse:"+response);
                     if (response.isSuccessful()) {
                         PVideoResult mResult = response.body();
                         if (mResult != null && !TextUtils.isEmpty(mResult.getUrl())) {
                             if (song != null) {
                                 song.setMedia_url(mResult.getUrl());
                                 if (!TextUtils.isEmpty(mResult.getMp3Url())) {
                                     song.setBackup1(mResult.getMp3Url());
                                 }
                                 startExoplayer(song);
                             } else {
                                 playNext();
                             }
                         }else {
                             playNext();
                         }
                     }else {
                         playNext();
                     }
                 }

                 @Override
                 public void onFailure(Call<PVideoResult> call, Throwable t) {
                     LogUtil.DefalutLog("onFailure:"+t.getMessage()+"---call:"+call.request().url());
                     playNext();
                 }
             }
        );
    }

    private void playNext(){
        if(isPlayList){
            InitPlayList(list,currentPosition);
        }
    }

}
