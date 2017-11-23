package com.messi.languagehelper.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.messi.languagehelper.service.PlayerService.action_pause;
import static com.messi.languagehelper.service.PlayerService.action_start;
import static com.youdao.sdk.app.YouDaoApplication.getApplicationContext;

/**
 * Created by luli on 22/11/2017.
 */

public class NotificationUtil {

    public static final String mes_type_zyhy = "zyhy";
    public static final String mes_type_xmly = "xmly";

    public static void showNotification(Context mContext,String action,String title,String type){
        NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        Intent notIntent = new Intent(mContext, WXEntryActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(mContext, 0, notIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentAction = new Intent(action);//新建意图，并设置action标记为"play"，用于接收广播时过滤意图信息
        intentAction.putExtra(KeyUtil.MesType,type);
        intentAction.putExtra(KeyUtil.NotificationTitle,title);
        PendingIntent pIntentAction = PendingIntent.getService(getApplicationContext(), 0, intentAction,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int img_id = R.drawable.ic_pause_grey;
        if(action.equals(action_pause)){
            img_id = R.drawable.ic_pause_grey;
        }else if (action.equals(action_start)) {
            img_id = R.drawable.ic_play_grey;
        } else {

        }

        RemoteViews contentView = new RemoteViews(mContext.getPackageName(),R.layout.notification_layout);
        contentView.setTextViewText(R.id.notifi_title, title);
        contentView.setImageViewResource(R.id.notifi_action, img_id);
        contentView.setViewVisibility(R.id.notifi_previous, View.GONE);
        contentView.setViewVisibility(R.id.notifi_next, View.GONE);
        contentView.setOnClickPendingIntent(R.id.notifi_action, pIntentAction);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        Notification notification = builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setContent(contentView)
                .setAutoCancel(true)
                .build();
        manager.notify(PlayerService.NOTIFY_ID, notification);
    }

    public static void sendBroadcast(Context mContext,String music_action){
        Intent broadcast = new Intent(BaseActivity.UpdateMusicUIToStop);
        broadcast.putExtra(KeyUtil.MusicAction,music_action);
        mContext.sendBroadcast(broadcast);
    }


}
