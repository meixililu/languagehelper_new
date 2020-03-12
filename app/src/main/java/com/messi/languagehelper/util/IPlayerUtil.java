package com.messi.languagehelper.util;

import android.content.Context;
import android.os.RemoteException;

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

    public static void initAndPlay(Context mContext, Reading data){
        try {
            LogUtil.DefalutLog("IPlayerUtil---initAndPlay---Reading:"+data);
            musicSrv.initAndPlay(data);
        } catch (RemoteException e) {
            LogUtil.DefalutLog("RemoteException---initAndPlay");
            e.printStackTrace();
        }
    }

    public static void initPlayList(Context mContext, List<Reading> list, int position){
        try {
            LogUtil.DefalutLog("IPlayerUtil---initPlayList---list:"+list);
            musicSrv.initPlayList(list,position);
        } catch (RemoteException e) {
            LogUtil.DefalutLog("RemoteException---initPlayList");
            e.printStackTrace();
        }
    }

}
