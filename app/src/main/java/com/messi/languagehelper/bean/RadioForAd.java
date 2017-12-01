package com.messi.languagehelper.bean;

import com.iflytek.voiceads.NativeADDataRef;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;

/**
 * Created by luli on 10/11/2017.
 */

public class RadioForAd extends Radio {

    private boolean isAd;
    private boolean isAdShow;
    private NativeADDataRef mNativeADDataRef;

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public boolean isAdShow() {
        return isAdShow;
    }

    public void setAdShow(boolean adShow) {
        isAdShow = adShow;
    }

    public NativeADDataRef getmNativeADDataRef() {
        return mNativeADDataRef;
    }

    public void setmNativeADDataRef(NativeADDataRef mNativeADDataRef) {
        this.mNativeADDataRef = mNativeADDataRef;
    }
}
