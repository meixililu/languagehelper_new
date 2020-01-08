package com.messi.languagehelper.ViewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.BDADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.TXADUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class LeisureModel {

    public static boolean misVisibleToUser;
    public int counter;
    private WeakReference<Context> mContext;
    public SharedPreferences sp;
    private NativeDataRef mNativeADDataRef;
    private NativeExpressADView mTXADView;
    private long lastLoadAd;
    private boolean exposureXFAD;
    private String currentAD;
    private String BDADID = BDADUtil.BD_BANNer;
    private String XFADID = ADUtil.MRYJYSNRLAd;
    private String CSJADID = CSJADUtil.CSJ_XXLSP;

    public FrameLayout xx_ad_layout;
    public FrameLayout ad_layout;
    public TextView ad_sign;
    public SimpleDraweeView adImg;

    private TTAdNative mTTAdNative;
    private AdView adView;

    public LeisureModel(Context mContext){
        this.mContext = new WeakReference<>(mContext);
        sp = Setings.getSharedPreferences(mContext);
    }

    public void setViews(TextView ad_sign,SimpleDraweeView adImg,
                         FrameLayout xx_ad_layout,FrameLayout ad_layout){
        this.adImg = adImg;
        this.ad_sign = ad_sign;
        this.xx_ad_layout = xx_ad_layout;
        this.ad_layout = ad_layout;
    }

    public void showAd(){
        if(ADUtil.IsShowAD){
            xx_ad_layout.setVisibility(View.VISIBLE);
            loadAD();
        }else {
            xx_ad_layout.setVisibility(View.GONE);
        }
    }

    public void loadAD(){
        try {
            currentAD = ADUtil.getAdProvider(counter);
            lastLoadAd = System.currentTimeMillis();
            if(!TextUtils.isEmpty(currentAD)){
                LogUtil.DefalutLog("------ad-------"+currentAD);
                if(ADUtil.GDT.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.BD.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.CSJ.equals(currentAD)){
                    loadCSJAD();
                }else if(ADUtil.XF.equals(currentAD)){
                    loadXFAD();
                }else if(ADUtil.XBKJ.equals(currentAD)){
                    loadXBKJ();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAd(){
        counter++;
        loadAD();
    }

    public void loadXFAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(getContext(), XFADID, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("LeisureModel-onAdFailed:"+arg0.getErrorDescription());
                getAd();
            }
            @Override
            public void onAdLoaded(NativeDataRef nativeDataRef) {
                if(nativeDataRef != null){
                    exposureXFAD = false;
                    setAd(nativeDataRef);
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd();
    }

    public void setAd(NativeDataRef mADDataRef) {
        if (mADDataRef != null) {
            mNativeADDataRef = mADDataRef;
            ad_sign.setVisibility(View.VISIBLE);
            adImg.setVisibility(View.VISIBLE);
            ad_layout.setVisibility(View.GONE);
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse(mNativeADDataRef.getImgUrl()))
                    .build();
            adImg.setController(mDraweeController);
            xx_ad_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onXFADClick();
                }
            });
            exposedXFAD();
        }
    }

    public void onXFADClick(){
        if (mNativeADDataRef != null) {
            boolean onClicked = mNativeADDataRef.onClick(xx_ad_layout);
            LogUtil.DefalutLog("onClicked:" + onClicked);
        }
    }

    public void exposedXFAD() {
        if (!exposureXFAD && mNativeADDataRef != null) {
            exposureXFAD = mNativeADDataRef.onExposure(xx_ad_layout);
            LogUtil.DefalutLog("exposedAd-exposureXFAD:" + exposureXFAD);
        }
    }

    public void exposedAd() {
        LogUtil.DefalutLog("exposedAd");
        if (ADUtil.XF.equals(currentAD)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exposedXFAD();
                }
            }, 500);
        }
        if (misVisibleToUser && lastLoadAd > 0) {
            if (System.currentTimeMillis() - lastLoadAd > 16*1000) {
                currentAD = ADUtil.getAdProvider(counter);
                if(TextUtils.isEmpty(currentAD) ||
                        ADUtil.XBKJ.equals(currentAD) ||
                        ADUtil.XF.equals(currentAD)){
                    counter = 0;
                }
                loadAD();
            }
        }
    }

    private void loadTXAD() {
        TXADUtil.showBigImg(getContext(), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog("loadTXAD0-onNoAD");
                getAd();
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if (NullUtil.isNotEmpty(list)) {
                    if (mTXADView != null) {
                        mTXADView.destroy();
                    }
                    initFeiXFAD();
                    mTXADView = list.get(0);
                    ad_layout.addView(mTXADView);
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
                getAd();
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
            setAd(ADUtil.getRandomAd(getContext()));
        }else {
            ADUtil.loadAd(getContext());
        }
    }

    public void loadBDAD(){
        adView = new AdView(getContext(),BDADID);
        initFeiXFAD();
        adView.setListener(new AdViewListener(){
            @Override
            public void onAdReady(AdView adView) {
                LogUtil.DefalutLog("BDAD-onAdReady");
            }
            @Override
            public void onAdShow(JSONObject jsonObject) {
                LogUtil.DefalutLog("BDAD-onAdShow");
            }
            @Override
            public void onAdClick(JSONObject jsonObject) {
                LogUtil.DefalutLog("BDAD-onAdClick");
            }
            @Override
            public void onAdFailed(String s) {
                LogUtil.DefalutLog("BDAD-onAdFailed:"+s);
                getAd();
            }
            @Override
            public void onAdSwitch() {
                LogUtil.DefalutLog("BDAD-onAdSwitch");
            }
            @Override
            public void onAdClose(JSONObject jsonObject) {
                getAd();
                LogUtil.DefalutLog("BDAD-onAdClose");
            }
        });
        int height = (int)(SystemUtil.SCREEN_WIDTH / 2);
        LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH, height);
        ad_layout.addView(adView,rllp);
    }

    public void initFeiXFAD(){
        ad_sign.setVisibility(View.GONE);
        adImg.setVisibility(View.GONE);
        ad_layout.setVisibility(View.VISIBLE);
        ad_layout.removeAllViews();
    }

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD");
         mTTAdNative = CSJADUtil.get().createAdNative(getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(690, 388)
                .build();
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadCSJAD-onError:"+s);
                getAd();
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    getAd();
                    return;
                }
                TTFeedAd mTTFeedAd = ads.get(0);
                initFeiXFAD();
                XXLRootModel.setCSJDView(getContext(),mTTFeedAd,ad_layout);
            }
        });
    }

    public void onDestroy(){
        if (mTXADView != null) {
            mTXADView.destroy();
            mTXADView = null;
        }
        if(xx_ad_layout != null){
            xx_ad_layout.removeAllViews();
            xx_ad_layout = null;
        }
        if(ad_layout != null){
            ad_layout.removeAllViews();
            ad_layout = null;
        }
        if(mTTAdNative != null){
            mTTAdNative = null;
        }
        if(adView != null){
            adView.destroy();
            adView = null;
        }
    }

    public void setXFADID(String XFADID) {
        this.XFADID = XFADID;
    }

    public Context getContext() {
        return mContext.get();
    }

}
