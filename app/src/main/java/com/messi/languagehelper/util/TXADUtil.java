package com.messi.languagehelper.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

/**
 * Created by luli on 2018/4/24.
 */

public class TXADUtil {

    public static final String appId = "1106754401";
    public static final String posId_Kaiping = "2040339301207632";

    public static void showKaipingAD(Activity activity,
                                     ViewGroup adContainer,
                                     View skipContainer,
                                     SplashADListener listener){
        SplashAD splashAD = new SplashAD(activity, adContainer, skipContainer,
                appId, posId_Kaiping, listener, 3000);
    }
}
