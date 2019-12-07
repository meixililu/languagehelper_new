package com.messi.languagehelper.bean;


import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
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
    private TTFeedAd csjTTFeedAd;
    private AdView bdAdView;
    private int bdHeight;

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

    public TTFeedAd getCsjTTFeedAd() {
        return csjTTFeedAd;
    }

    public void setCsjTTFeedAd(TTFeedAd csjTTFeedAd) {
        this.csjTTFeedAd = csjTTFeedAd;
    }

    public AdView getBdAdView() {
        return bdAdView;
    }

    public void setBdAdView(AdView bdAdView) {
        this.bdAdView = bdAdView;
    }

    public int getBdHeight() {
        return bdHeight;
    }

    public void setBdHeight(int bdHeight) {
        this.bdHeight = bdHeight;
    }
}
