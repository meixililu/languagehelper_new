package com.messi.languagehelper.util;

import android.app.Activity;
import android.content.Context;
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

    public static String appId = "1106863330";
    public static String posId_Kaiping = "8070635391202695";
    public static String posId_XXL_STXW = "8020132381266427";
    public static String posId_XXL_SWXT = "9070133322784103";
    public static String posId_XXL_ZWYT = "1090838321167493";
    public static String posId_CDT = "7080330341367043";

    public static void initTXADID(Context mContext){
        if(mContext.getPackageName().equals(Settings.application_id_yyj) ||
                mContext.getPackageName().equals(Settings.application_id_yyj_google)){
            appId = "1106957016";
            posId_Kaiping = "7080135419839958";
            posId_XXL_STXW = "1030736419937999";
            posId_XXL_SWXT = "2000636429344052";
            posId_XXL_ZWYT = "9000337459847020";
            posId_CDT = "7090539489345031";
            LogUtil.DefalutLog("application_id_yyj");
        }else if (mContext.getPackageName().equals(Settings.application_id_yys) ||
                mContext.getPackageName().equals(Settings.application_id_yys_google)) {
            appId = "1106957022";
            posId_Kaiping = "3040833560655805";
            posId_XXL_STXW = "5030230580451876";
            posId_XXL_SWXT = "6080439550951887";
            posId_XXL_ZWYT = "5010330570751838";
            posId_CDT = "8030136520951889";
        } else if (mContext.getPackageName().equals(Settings.application_id_yycd)) {

        } else if (mContext.getPackageName().equals(Settings.application_id_xbky)) {

        } else {
            //zyhy id
            appId = "1106863330";
            posId_Kaiping = "8070635391202695";
            posId_XXL_STXW = "8020132381266427";
            posId_XXL_SWXT = "9070133322784103";
            posId_XXL_ZWYT = "1090838321167493";
            posId_CDT = "7080330341367043";
            LogUtil.DefalutLog("application_id_zyhy");
        }
    }

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

    public static void showXXL_STXW(Activity activity,
                                    NativeExpressAD.NativeExpressADListener listener){
        NativeExpressAD nativeExpressAD = new NativeExpressAD(activity,
                new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), appId, posId_XXL_STXW, listener);
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(1);
    }
}
