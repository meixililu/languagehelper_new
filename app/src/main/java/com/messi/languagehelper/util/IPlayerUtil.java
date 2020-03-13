package com.messi.languagehelper.util;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.aidl.IXBPlayer;
import com.messi.languagehelper.box.Reading;

import java.util.List;

public class IPlayerUtil {

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
            return true;
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
            musicSrv.initAndPlay(data);
        } catch (Exception e) {
            LogUtil.DefalutLog("RemoteException---initAndPlay");
            e.printStackTrace();
        }
    }

    public static void initPlayList(List<Reading> list, int position){
        try {
            LogUtil.DefalutLog("IPlayerUtil---initPlayList---list:"+list);
            String lists = JSON.toJSONString(list);
            musicSrv.initPlayList(lists,position);
        } catch (Exception e) {
            LogUtil.DefalutLog("RemoteException---initPlayList");
            e.printStackTrace();
        }
    }

}
