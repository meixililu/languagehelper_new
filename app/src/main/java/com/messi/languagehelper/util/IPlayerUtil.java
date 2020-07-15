package com.messi.languagehelper.util;

import android.content.Context;

import com.messi.languagehelper.aidl.IXBPlayer;
import com.messi.languagehelper.box.Reading;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.json.JSON;

import static com.messi.languagehelper.service.PlayerService.action_pause;
import static com.messi.languagehelper.service.PlayerService.action_restart;

public class IPlayerUtil {

    public static final String PlayerXMLY = "PlayerXMLY";
    public static final String PlayerXBKJ = "PlayerXBKJ";

    public static IXBPlayer musicSrv;

    public static int getPlayStatus() {
        try {
            if(musicSrv != null){
                return musicSrv.getPlayStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getLastPlayer() {
        try {
            if(musicSrv != null){
                return musicSrv.getLastPlayer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PlayerXBKJ;
    }

    public static void setLastPlayer(String player) {
        try {
            if(musicSrv != null){
                musicSrv.setLastPlayer(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAppExit(boolean isExit) {
        try {
            if(musicSrv != null){
                musicSrv.setAppExit(isExit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCurrentPosition() {
        try {
            if(musicSrv != null){
                return musicSrv.getCurrentPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDuration() {
        try {
            if(musicSrv != null){
                return musicSrv.getDuration();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void MPlayerPause() {
        try {
            if(musicSrv != null){
                musicSrv.MPlayerPause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean MPlayerIsPlaying(){
        try {
            if(musicSrv != null){
                return musicSrv.MPlayerIsPlaying();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void MPlayerRestart(){
        try {
            if(musicSrv != null){
                musicSrv.MPlayerRestart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void MPlayerSeekTo(int position){
        try {
            if(musicSrv != null){
                musicSrv.MPlayerSeekTo(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean MPlayerIsSameMp3(Reading song){
        try {
            if(musicSrv != null){
                return musicSrv.MPlayerIsSameMp3(song.getObject_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void initAndPlay(Reading song){
        try {
            LogUtil.DefalutLog("IPlayerUtil---initAndPlay---Reading:"+song);
            String data = JSON.toJSONString(song);
            musicSrv.initAndPlay(data,true,0);
        } catch (Exception e) {
            LogUtil.DefalutLog("RemoteException---initAndPlay");
            e.printStackTrace();
        }
    }

    public static void initAndPlay(Reading song,boolean isPlayList,long position){
        try {
            LogUtil.DefalutLog("IPlayerUtil---initAndPlay---musicSrv");
            String data = JSON.toJSONString(song);
            musicSrv.initAndPlay(data, isPlayList, position);
        } catch (Exception e) {
            LogUtil.DefalutLog("RemoteException---initAndPlay--isPlayList");
            e.printStackTrace();
        }
    }

    public static void initPlayList(List<Reading> list, int position){
        try {
            LogUtil.DefalutLog("IPlayerUtil---initPlayList---musicSrv");
            String lists = JSON.toJSONString(getMp3List(list,position));
            musicSrv.initPlayList(lists,0);
        } catch (Exception e) {
            LogUtil.DefalutLog("RemoteException---initPlayList");
            e.printStackTrace();
        }
    }

    public static void pauseAudioPlayer(Context context){
        try {
            if (IPlayerUtil.MPlayerIsPlaying()) {
                MPlayerPause();
            }
            if (XmPlayerManager.getInstance(context).isPlaying()) {
                String title = "告别说不出口的英语";
                if (XmPlayerManager.getInstance(context).getCurrSound() instanceof Track) {
                    Track mTrack = (Track) XmPlayerManager.getInstance(context).getCurrSound();
                    title = mTrack.getTrackTitle();
                }
                if (XmPlayerManager.getInstance(context).getCurrSound() instanceof Radio) {
                    Radio mRadio = (Radio) XmPlayerManager.getInstance(context).getCurrSound();
                    title = mRadio.getRadioName();
                }
                NotificationUtil.showNotification(context, action_restart,title,
                        NotificationUtil.mes_type_xmly);
                NotificationUtil.sendBroadcast(context, action_restart);
                XmPlayerManager.getInstance(context).pause();
            }
        }catch (Exception e){
            LogUtil.DefalutLog("RemoteException---pauseAudioPlayer");
            e.printStackTrace();
        }
    }

    public static void restartAudioPlayer(Context context){
        try {
            if (IPlayerUtil.PlayerXBKJ.equals(IPlayerUtil.getLastPlayer())){
                MPlayerRestart();
            }else {
                String title = "告别说不出口的英语";
                if (XmPlayerManager.getInstance(context).getCurrSound() instanceof Track) {
                    Track mTrack = (Track) XmPlayerManager.getInstance(context).getCurrSound();
                    title = mTrack.getTrackTitle();
                }
                if (XmPlayerManager.getInstance(context).getCurrSound() instanceof Radio) {
                    Radio mRadio = (Radio) XmPlayerManager.getInstance(context).getCurrSound();
                    title = mRadio.getRadioName();
                }
                XmPlayerManager.getInstance(context).play();
                NotificationUtil.sendBroadcast(context,action_pause);
                NotificationUtil.showNotification(context,action_pause, title,
                        NotificationUtil.mes_type_xmly);
            }
        }catch (Exception e){
            LogUtil.DefalutLog("RemoteException---pauseAudioPlayer");
            e.printStackTrace();
        }
    }

    public static List<Reading> getMp3List(List<Reading> list, int start){
        List<Reading> nList = new ArrayList<>();
        for (int i=start; i<list.size(); i++ ){
            if ("mp3".equals(list.get(i).getType())) {
                nList.add(list.get(i));
            }
        }
        return nList;
    }
}
