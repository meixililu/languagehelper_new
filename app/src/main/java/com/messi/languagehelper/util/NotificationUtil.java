package com.messi.languagehelper.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.wxapi.YYJMainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.messi.languagehelper.service.PlayerService.action_pause;
import static com.messi.languagehelper.service.PlayerService.action_restart;

/**
 * Created by luli on 22/11/2017.
 */

public class NotificationUtil {

    public static final String CHannelID = "xbkj";
    public static final String mes_type_zyhy = "zyhy";
    public static final String mes_type_xmly = "xmly";

    public static void showNotification(Context mContext,String action,String title,String type){
        NotificationManager manager = getManager(mContext);
        manager.notify(Setings.NOTIFY_ID, getNotification(mContext,action,title,type));
    }

    public static NotificationManager getManager(Context mContext){
        NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(manager);
        return manager;
    }

    public static Notification getNotification(Context mContext,String action,String title,String type){
        Class toClass = WXEntryActivity.class;
        if(mContext.getPackageName().equals(Setings.application_id_yys)){
            toClass = WXEntryActivity.class;
        }else if(mContext.getPackageName().equals(Setings.application_id_yys_google)){
            toClass = WXEntryActivity.class;
        }else if(mContext.getPackageName().equals(Setings.application_id_yyj)){
            toClass = YYJMainActivity.class;
        }else if(mContext.getPackageName().equals(Setings.application_id_yyj_google)){
            toClass = YYJMainActivity.class;
        }else if(mContext.getPackageName().equals(Setings.application_id_ywcd)){

        }else if(mContext.getPackageName().equals(Setings.application_id_xbky)){

        }
        Intent notIntent = new Intent(mContext, toClass);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(
                mContext, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //新建意图，并设置action标记为"play"，用于接收广播时过滤意图信息
        Intent intentAction = new Intent(mContext,PlayerService.class);
        intentAction.setAction(action);
        intentAction.putExtra(KeyUtil.MesType,type);
        intentAction.putExtra(KeyUtil.NotificationTitle,title);
        PendingIntent pIntentAction = PendingIntent.getService(mContext.getApplicationContext(), 0, intentAction,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int img_id = R.drawable.ic_pause_grey;
        if(action.equals(action_pause)){
            img_id = R.drawable.ic_pause_grey;
        }else if (action.equals(action_restart)) {
            img_id = R.drawable.ic_play_grey;
        } else {

        }
        Intent intentClose = new Intent(mContext,PlayerService.class);
        intentClose.setAction(PlayerService.action_close);
        intentClose.putExtra(KeyUtil.MesType,NotificationUtil.mes_type_zyhy);
        PendingIntent pIntentClose = PendingIntent.getService(mContext.getApplicationContext(), 0, intentClose,
                PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(mContext.getPackageName(),R.layout.notification_layout);
        contentView.setTextViewText(R.id.notifi_title, title);
        contentView.setImageViewResource(R.id.notifi_action, img_id);
        contentView.setViewVisibility(R.id.notifi_previous, View.GONE);
        contentView.setViewVisibility(R.id.notifi_next, View.GONE);
        contentView.setOnClickPendingIntent(R.id.notifi_action, pIntentAction);
        contentView.setOnClickPendingIntent(R.id.notifi_close, pIntentClose);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,NotificationUtil.CHannelID);
        if(img_id == R.drawable.ic_play_grey){
            builder.setAutoCancel(true);
        }else {
            builder.setOngoing(true);
        }
        Notification notification = builder
                .setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(contentView)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        return notification;
    }

    public static void sendBroadcast(Context mContext,String music_action){
        Intent broadcast = new Intent(BaseActivity.UpdateMusicUIToStop);
        broadcast.putExtra(KeyUtil.MusicAction,music_action);
        mContext.sendBroadcast(broadcast);
    }

    public static void createNotificationChannel(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotificationUtil.CHannelID,
                    "study", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("play");
            manager.createNotificationChannel(channel);
        }
    }


}
