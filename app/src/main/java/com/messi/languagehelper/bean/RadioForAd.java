package com.messi.languagehelper.bean;


import com.iflytek.voiceads.conn.NativeDataRef;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;

/**
 * Created by luli on 10/11/2017.
 */

public class RadioForAd extends Radio {

    private boolean isAd;
    private boolean isAdShow;
    private NativeDataRef mNativeADDataRef;
    private NativeExpressADView mTXADView;

    public NativeExpressADView getmTXADView() {
        return mTXADView;
    }
    public void setmTXADView(NativeExpressADView mTXADView) {
        this.mTXADView = mTXADView;
    }

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

    public NativeDataRef getmNativeADDataRef() {
        return mNativeADDataRef;
    }

    public void setmNativeADDataRef(NativeDataRef mNativeADDataRef) {
        this.mNativeADDataRef = mNativeADDataRef;
    }
}
