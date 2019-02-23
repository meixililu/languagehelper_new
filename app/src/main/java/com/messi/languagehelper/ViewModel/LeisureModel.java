package com.messi.languagehelper.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.event.KaipingPageEvent;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TXADUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.wxapi.YYJMainActivity;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class LeisureModel {

    public int counter;
    public Activity mContext;
    public SharedPreferences sp;
    private NativeADDataRef mNativeADDataRef;
    private NativeExpressADView mTXADView;
    private long lastLoadAd;
    private boolean exposureXFAD;
    private boolean misVisibleToUser;
    private String currentAD;

    public FrameLayout xx_ad_layout;
    public TextView ad_sign;
    public SimpleDraweeView adImg;
    private static final int AD_TIME_OUT = 1500;


    public LeisureModel(Activity mContext){
        this.mContext = mContext;
        sp = Setings.getSharedPreferences(mContext);
    }

    public void setViews(TextView ad_sign,SimpleDraweeView adImg,
                         FrameLayout xx_ad_layout){
        this.ad_sign = ad_sign;
        this.xx_ad_layout = xx_ad_layout;
        this.adImg = adImg;
    }

    public void showAd(){
        if(ADUtil.IsShowAD){
            getAd();
        }
    }

    public void getAd(){
        currentAD = ADUtil.getAdProvider(counter);
        if(!TextUtils.isEmpty(currentAD)){
            if(!sp.getBoolean(KeyUtil.IsTXADPermissionReady,false)){
                if(ADUtil.GDT.equals(currentAD)){
                    counter++;
                    currentAD = ADUtil.getAdProvider(counter);
                }
            }
            LogUtil.DefalutLog("------ad-------"+currentAD);
            if(ADUtil.GDT.equals(currentAD)){
                loadTXAD();
            }else if(ADUtil.BD.equals(currentAD)){
                loadBDAD();
            }else if(ADUtil.CSJ.equals(currentAD)){
                loadCSJAD();
            }else if(ADUtil.XF.equals(currentAD)){
                loadXFAD();
            }else if(ADUtil.XBKJ.equals(currentAD)){
                loadXBKJ();
            }
        }
        counter++;
    }

    public void loadXFAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(mContext, ADUtil.MRYJYSNRLAd, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
                getAd();
            }
            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if (adList != null && adList.size() > 0) {
                    exposureXFAD = false;
                    setAd(adList.get(0));
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    public void setAd(NativeADDataRef mADDataRef) {
        if (mADDataRef != null) {
            mNativeADDataRef = mADDataRef;
            lastLoadAd = System.currentTimeMillis();
            ad_sign.setVisibility(View.VISIBLE);
            adImg.setImageURI(mNativeADDataRef.getImage());
            if (misVisibleToUser) {
                exposedXFAD();
            }
        }
    }

    public void onXFADClick(){
        if (mNativeADDataRef != null) {
            boolean onClicked = mNativeADDataRef.onClicked(xx_ad_layout);
            LogUtil.DefalutLog("onClicked:" + onClicked);
        }
    }

    public void exposedXFAD() {
        if (!exposureXFAD && mNativeADDataRef != null) {
            exposureXFAD = mNativeADDataRef.onExposured(xx_ad_layout);
            LogUtil.DefalutLog("exposedAd-exposureXFAD:" + exposureXFAD);
        }
    }

    public void exposedAd() {
        LogUtil.DefalutLog("exposedAd");
        if (currentAD.equals(ADUtil.Advertiser_XF)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exposedXFAD();
                }
            }, 500);

        }
        if (misVisibleToUser && lastLoadAd > 0) {
            if (System.currentTimeMillis() - lastLoadAd > 45000) {
                loadTXAD();
            }
        }
    }

    private void loadTXAD() {
        TXADUtil.showCDT(mContext, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog(adError.getErrorMsg());
                getAd();
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if (list != null && list.size() > 0) {
                    if (mTXADView != null) {
                        mTXADView.destroy();
                    }
                    ad_sign.setVisibility(View.GONE);
                    xx_ad_layout.setVisibility(View.VISIBLE);
                    xx_ad_layout.removeAllViews();
                    mTXADView = list.get(0);
                    lastLoadAd = System.currentTimeMillis();
                    xx_ad_layout.addView(mTXADView);
                    mTXADView.render();
                }
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                nativeExpressADView.render();
                LogUtil.DefalutLog("onRenderFail");
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onRenderSuccess");
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADExposure");
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClicked");
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClosed");
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADLeftApplication");
            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADOpenOverlay");
            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADCloseOverlay");
            }
        });
    }

    public void loadXBKJ() {
        if (ADUtil.isHasLocalAd()) {
            setAd(ADUtil.getRandomAd(mContext));
        }
    }

    SplashAdListener bdAdListener = new SplashAdListener() {
        @Override
        public void onAdPresent() {
            LogUtil.DefalutLog("BDAD-onAdPresent");

        }
        @Override
        public void onAdDismissed() {
            LogUtil.DefalutLog("BDAD-onAdDismissed");

        }
        @Override
        public void onAdFailed(String s) {
            LogUtil.DefalutLog("BDAD-onAdFailed："+s);
            getAd();
        }
        @Override
        public void onAdClick() {
            LogUtil.DefalutLog("BDAD-onAdClick");
        }
    };

    public void loadBDAD(){
        SplashAd.setMaxVideoCacheCapacityMb(30);
        new SplashAd(mContext,splash_container,bdAdListener,ADUtil.BD_Kaiping,true);
    }

    public void loadCSJAD(){
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADUtil.CSJ_KPID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                LogUtil.DefalutLog("CSJAD-onError:"+message);
                getAd();
            }

            @Override
            @MainThread
            public void onTimeout() {
                LogUtil.DefalutLog("CSJAD-onTimeout");
                getAd();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                LogUtil.DefalutLog("CSJAD-onSplashAdLoad");
                if (ad == null) {
                    getAd();
                    return;
                }
                splash_container.addView(ad.getSplashView());
                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        LogUtil.DefalutLog( "CSJAD-onAdClicked");
                        onClickAd();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        LogUtil.DefalutLog( "CSJAD-onAdShow");
                        cancleRunable();
                    }

                    @Override
                    public void onAdSkip() {
                        LogUtil.DefalutLog( "CSJAD-onAdSkip");
                        toNextPage();
                    }

                    @Override
                    public void onAdTimeOver() {
                        LogUtil.DefalutLog("CSJAD-onAdTimeOver");
                        if(!notJump){
                            toNextPage();
                        }
                    }
                });
            }
        }, AD_TIME_OUT);
    }

    public void setIsVisibleToUser(boolean misVisibleToUser) {
        this.misVisibleToUser = misVisibleToUser;
    }

    public void onDestroy(){
        if (mTXADView != null) {
            mTXADView.destroy();
        }
    }

}
