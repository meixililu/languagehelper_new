package com.messi.languagehelper.bean;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.qq.e.ads.nativ.NativeExpressADView;

public class ADBean {

    //1-ok, 0-error
    public int status;
    public String type;
    public NativeDataRef mNativeADDataRef;
    public NativeExpressADView mTXADView;
    public TTFeedAd mTTFeedAd;
    public AdView adView;

    public ADBean(int status, String type){
        this.status = status;
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NativeDataRef getNativeADDataRef() {
        return mNativeADDataRef;
    }

    public void setNativeADDataRef(NativeDataRef mNativeADDataRef) {
        this.mNativeADDataRef = mNativeADDataRef;
    }

    public NativeExpressADView getTXADView() {
        return mTXADView;
    }

    public void setTXADView(NativeExpressADView mTXADView) {
        this.mTXADView = mTXADView;
    }

    public TTFeedAd getTTFeedAd() {
        return mTTFeedAd;
    }

    public void setTTFeedAd(TTFeedAd mTTFeedAd) {
        this.mTTFeedAd = mTTFeedAd;
    }

    public AdView getAdView() {
        return adView;
    }

    public void setAdView(AdView adView) {
        this.adView = adView;
    }
}
