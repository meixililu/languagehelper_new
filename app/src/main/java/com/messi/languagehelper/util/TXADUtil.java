package com.messi.languagehelper.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

/**
 * Created by luli on 2018/4/24.
 */

public class TXADUtil {

    public static final String appId = "1106863330";
    public static final String posId_Kaiping = "8070635391202695";
    public static final String posId_XXL_STXW = "8020132381266427";
    public static final String posId_XXL_SWXT = "9070133322784103";//error
    public static final String posId_XXL_ZWYT = "1090838321167493";
    public static final String posId_CDT = "7080330341367043";

    public static void showKaipingAD(Activity activity,
                                     ViewGroup adContainer,
                                     View skipContainer,
                                     SplashADListener listener){
        SplashAD splashAD = new SplashAD(activity, adContainer, skipContainer,
                appId, posId_Kaiping, listener, 3000);
    }

    public static void showCDT(Activity activity,
                                          NativeExpressAD.NativeExpressADListener listener){
        NativeExpressAD nativeExpressAD = new NativeExpressAD(activity,
                new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), appId, posId_CDT, listener);
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(1);
    }

    public static void showXXL(Activity activity,
                               NativeExpressAD.NativeExpressADListener listener){
        String postID = posId_XXL_ZWYT;
        if(NumberUtil.getRandomNumber(2) > 0){
            postID = posId_XXL_STXW;
        }
        NativeExpressAD nativeExpressAD = new NativeExpressAD(activity,
                new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), appId, postID, listener);
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(1);
    }

    public static void showXXL_ZWYT(Activity activity,
                               NativeExpressAD.NativeExpressADListener listener){
        NativeExpressAD nativeExpressAD = new NativeExpressAD(activity,
                new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), appId, posId_XXL_ZWYT, listener);
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(1);
    }
}
