package com.messi.languagehelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.task.MyThread;

/**
 * Created by luli on 18/03/2017.
 */

public class PlayUtil {

    public static Context mContext;
    public static Thread mThread;
    public static MyThread mMyThread;
    public static Handler mHandler;
    public static SpeechSynthesizer mSpeechSynthesizer;
    public static SharedPreferences mSharedPreferences;

    public static String filepath = "";
    public static String speakContent;
    public static boolean isPlaying;
    public static AnimationDrawable currentAnimationDrawable;

    public static void initData(Context nContext,SpeechSynthesizer nSpeechSynthesizer,
                                SharedPreferences nSharedPreferences){
        mContext = nContext;
        mSpeechSynthesizer = nSpeechSynthesizer;
        mSharedPreferences = nSharedPreferences;
        mHandler = new Handler() {
            public void handleMessage(Message message) {
                if (message.what == MyThread.EVENT_PLAY_OVER) {
                    onFinishPlay();
                }
            }
        };
        mMyThread = new MyThread(mHandler);
    }

    public static void play(String nfilepath,String nSpeakContent,AnimationDrawable nDrawable,
                            SynthesizerListener nSynthesizerListener){
        LogUtil.DefalutLog("PlayUtil-isPlaying:"+isPlaying);
        if(!isPlaying){
            filepath = nfilepath;
            speakContent = nSpeakContent+".";
            currentAnimationDrawable = nDrawable;
            startToPlay(nSynthesizerListener);
        }else {
            stopPlay();
            if(!filepath.equals(nfilepath)){
                filepath = nfilepath;
                speakContent = nSpeakContent;
                currentAnimationDrawable = nDrawable;
                startToPlay(nSynthesizerListener);
            }
        }
    }


    public static void startToPlay(SynthesizerListener nSynthesizerListener){
        if (!AudioTrackUtil.isFileExists(filepath)) {
            mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
            XFUtil.showSpeechSynthesizer(
                    mContext,
                    mSharedPreferences,
                    mSpeechSynthesizer,
                    speakContent,
                    nSynthesizerListener);
        } else {
            onStartPlay();
            mMyThread.setDataUri(filepath);
            mThread = AudioTrackUtil.startMyThread(mMyThread);
        }
    }

    public static void stopPlay(){
        onFinishPlay();
        AudioTrackUtil.stopPlayOnline(mSpeechSynthesizer);
        AudioTrackUtil.stopPlayPcm(mThread,mMyThread);
    }

    public static void onStartPlay(){
        try{
            isPlaying = true;
            if(currentAnimationDrawable != null){
                if (!currentAnimationDrawable.isRunning()) {
                    currentAnimationDrawable.setOneShot(false);
                    currentAnimationDrawable.start();
                }
            }
        } catch(Exception e){
            currentAnimationDrawable = null;
            e.printStackTrace();
        }

    }

    public static void onFinishPlay(){
        try {
            isPlaying = false;
            mThread = null;
            if (currentAnimationDrawable != null) {
                currentAnimationDrawable.setOneShot(true);
                currentAnimationDrawable.stop();
                currentAnimationDrawable.selectDrawable(0);
            }
            currentAnimationDrawable = null;
        } catch (Exception e) {
            currentAnimationDrawable = null;
            e.printStackTrace();
        }
    }

    //WXEntryActivity onDestroy 方可调用，其他avtivity可调用 stopPlay()
    public static void onDestroy(){
        try {
            stopPlay();
            filepath = null;
            speakContent = null;
            mContext = null;
            mThread = null;
            mMyThread = null;
            mHandler = null;
            mSpeechSynthesizer = null;
            currentAnimationDrawable = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SharedPreferences getSP() {
        return mSharedPreferences;
    }

}
