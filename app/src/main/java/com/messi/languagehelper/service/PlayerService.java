package com.messi.languagehelper.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.util.List;

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

    private static final int NOTIFY_ID = 1;
    //media player
    private MediaPlayer player;
    // 0 default, 1 playing, 2 pause
    public int PlayerStatus;
    public String lastSongId = "";
    private Reading song;
    private NotificationManager manager;
    private final IBinder musicBind = new MusicBinder();

    public class MusicBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sendBroadcast(action_finish_loading);
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
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void initAndPlay(Reading song){
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
            String downLoadUrl = mAVObject.getMedia_url();
            int pos = downLoadUrl.lastIndexOf(SDCardUtil.Delimiter) + 1;
            String fileName = downLoadUrl.substring(pos, downLoadUrl.length());
            String rootUrl = SDCardUtil.ReadingPath +
                    mAVObject.getObject_id() + SDCardUtil.Delimiter;
            String fileFullName = SDCardUtil.getDownloadPath(rootUrl) + fileName;
            LogUtil.DefalutLog("fileName:" + fileName + "---fileFullName:" + fileFullName);
            if (SDCardUtil.isFileExist(fileFullName)) {
                startToPlay(mAVObject);
                LogUtil.DefalutLog("FileExist");
            } else {
                sendBroadcast(action_loading);
                LogUtil.DefalutLog("FileNotExist");
                DownLoadUtil.downloadFile(this, downLoadUrl, rootUrl, fileName, mHandler);
            }
        }
    }

    public void startToPlay(Reading song){
        this.song = song;
        lastSongId = song.getObject_id();
        PlayerStatus = 1;
        player.reset();
        Uri uri = Uri.parse(DownLoadUtil.getLocalFilePath(song));
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
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(action_start)) {
                    restart();
                }else if(action.equals(action_pause)) {
                    pause();
                }else if(action.equals(action_next)) {

                }else if(action.equals(action_previous)) {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
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
        manager.cancel(NOTIFY_ID);
        player.stop();
        player.release();
        return false;
    }

    public void pause(){
        PlayerStatus = 2;
        player.pause();
        showNotification(action_start);
        sendBroadcast(action_start);
    }

    public void restart(){
        PlayerStatus = 1;
        player.start();
        showNotification(action_pause);
        sendBroadcast(action_pause);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        PlayerStatus = 0;
        showNotification(action_start);
        sendBroadcast(action_start);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        showNotification(action_pause);
        sendBroadcast(action_pause);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        PlayerStatus = 0;
        player.reset();
        return false;
    }

    private void showNotification(String action){
        Intent notIntent = new Intent(this, WXEntryActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentAction = new Intent(action);//新建意图，并设置action标记为"play"，用于接收广播时过滤意图信息
        PendingIntent pIntentAction = PendingIntent.getService(getApplicationContext(), 0, intentAction,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int img_id = R.drawable.ic_pause_grey;
        if(action.equals(action_pause)){
            img_id = R.drawable.ic_pause_grey;
        }else if (action.equals(action_start)) {
            img_id = R.drawable.ic_play_grey;
        } else {

        }

        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notification_layout);
        contentView.setTextViewText(R.id.notifi_title, song.getTitle());
        contentView.setImageViewResource(R.id.notifi_action, img_id);
        contentView.setViewVisibility(R.id.notifi_previous, View.GONE);
        contentView.setViewVisibility(R.id.notifi_next, View.GONE);
        contentView.setOnClickPendingIntent(R.id.notifi_action, pIntentAction);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setContent(contentView)
                .setAutoCancel(true)
                .build();
        manager.notify(NOTIFY_ID, notification);
    }

    private void sendBroadcast(String music_action){
        Intent broadcast = new Intent(BaseActivity.UpdateMusicUIToStop);
        broadcast.putExtra(KeyUtil.MusicAction,music_action);
        sendBroadcast(broadcast);
    }

}
