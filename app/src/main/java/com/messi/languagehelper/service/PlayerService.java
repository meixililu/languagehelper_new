package com.messi.languagehelper.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NotificationUtil;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import static com.messi.languagehelper.util.KeyUtil.MesType;
import static com.messi.languagehelper.util.KeyUtil.NotificationTitle;

/**
 * Created by luli on 05/05/2017.
 */

public class PlayerService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    public static final String action_loading = "com.messi.languagehelper.music.loading";
    public static final String action_finish_loading = "com.messi.languagehelper.music.finish.loading";
    public static final String action_start = "com.messi.languagehelper.music.start";
    public static final String action_pause = "com.messi.languagehelper.music.pause";
    public static final String action_next = "com.messi.languagehelper.music.next";
    public static final String action_previous = "com.messi.languagehelper.music.previous";

    public static final int NOTIFY_ID = 1;
    //media player
    private MediaPlayer player;
    // 0 default, 1 playing, 2 pause
    public int PlayerStatus;
    public String lastSongId = "";
    private Reading song;
    private final IBinder musicBind = new MusicBinder();

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
                    startToPlay(song);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void initAndPlay(Reading song){
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
            startToPlay(mAVObject);
//            String downLoadUrl = mAVObject.getMedia_url();
//            int pos = downLoadUrl.lastIndexOf(SDCardUtil.Delimiter) + 1;
//            String fileName = downLoadUrl.substring(pos, downLoadUrl.length());
//            String rootUrl = SDCardUtil.ReadingPath +
//                    mAVObject.getObject_id() + SDCardUtil.Delimiter;
//            String fileFullName = SDCardUtil.getDownloadPath(rootUrl) + fileName;
//            LogUtil.DefalutLog("fileName:" + fileName + "---fileFullName:" + fileFullName);
//            if (SDCardUtil.isFileExist(fileFullName)) {
//
//                LogUtil.DefalutLog("FileExist");
//            } else {
//                sendBroadcast(action_loading);
//                LogUtil.DefalutLog("FileNotExist");
//                DownLoadUtil.downloadFile(this, downLoadUrl, rootUrl, fileName, mHandler);
//            }
        }
    }

    public void startToPlay(Reading song){
        this.song = song;
        lastSongId = song.getObject_id();
        PlayerStatus = 1;
        player.reset();
        Uri uri = Uri.parse(song.getMedia_url());
        try{
            player.setDataSource(getApplicationContext(), uri);
            player.prepareAsync();
        } catch(Exception e){
            LogUtil.DefalutLog("MUSIC SERVICE Error setting data source");
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFY_ID);
        player.stop();
        player.release();
        return false;
    }

    public void pause(){
        if(song != null){
            PlayerStatus = 2;
            player.pause();
            NotificationUtil.showNotification(this,action_start,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this,action_start);
    }

    public void restart(){
        if(song != null){
            PlayerStatus = 1;
            player.start();
            NotificationUtil.showNotification(this,action_pause,song.getTitle(),
                    NotificationUtil.mes_type_zyhy);
        }
        NotificationUtil.sendBroadcast(this,action_pause);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        PlayerStatus = 0;
        NotificationUtil.showNotification(this,action_start,song.getTitle(),
                NotificationUtil.mes_type_zyhy);
        NotificationUtil.sendBroadcast(this,action_start);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        NotificationUtil.showNotification(this,action_pause,song.getTitle(),
                NotificationUtil.mes_type_zyhy);
        NotificationUtil.sendBroadcast(this,action_pause);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        PlayerStatus = 0;
        player.reset();
        return false;
    }

    public boolean isPlaying(){
        return PlayerStatus == 1;
    }

}
