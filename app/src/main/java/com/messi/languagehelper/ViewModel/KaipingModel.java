package com.messi.languagehelper.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.event.KaipingPageEvent;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.BDADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TXADUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.wxapi.YYJMainActivity;
import com.qq.e.ads.splash.SplashADListener;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class KaipingModel {

    public int counter;
    private WeakReference<Activity> mContext;
    public SharedPreferences sp;
    public boolean isAdClicked;
    public boolean isAdExposure;
    public boolean notJump;
    public Handler mHandler;

    public TextView ad_source;
    public TextView skip_view;
    public SimpleDraweeView ad_img;
    public FrameLayout splash_container;
    private static final int AD_TIME_OUT = 1500;


    public KaipingModel(Activity mContext){
        this.mContext = new WeakReference<>(mContext);
        sp = Setings.getSharedPreferences(mContext);
        mHandler = new Handler();
    }

    public void setViews(TextView ad_source,TextView skip_view,SimpleDraweeView ad_img,
                         FrameLayout splash_container){
        this.ad_source = ad_source;
        this.skip_view = skip_view;
        this.ad_img = ad_img;
        this.splash_container = splash_container;
    }

    public void showAd(){
        if(ADUtil.IsShowAD){
            getAd();
            startTask();
        }else {
            DelayToNextPage(1500);
        }
    }

    public void getAd(){
        try {
            String ad = ADUtil.getAdProvider(counter);
            if(!TextUtils.isEmpty(ad)){
                counter++;
                LogUtil.DefalutLog("------ad-------"+ad);
                LogUtil.DefalutLog("counter:"+counter);
                if(ADUtil.GDT.equals(ad)){
                    if(!sp.getBoolean(KeyUtil.IsTXADPermissionReady,false)){
                        getAd();
                    }else {
                        loadTXAD();
                    }
                }else if(ADUtil.BD.equals(ad)){
                    loadBDAD();
                }else if(ADUtil.CSJ.equals(ad)){
                    loadCSJAD();
                }else if(ADUtil.XF.equals(ad)){
                    loadXFAD();
                }else if(ADUtil.XBKJ.equals(ad)){
                    loadXBKJ();
                }
            }
        } catch (Exception e) {
            DelayToNextPage(1500);
            e.printStackTrace();
        }
    }

    //启动页加载总时常，防止广告一直加载中等待过久
    public void startTask() {
        mHandler.postDelayed(m3Runnable, 3700);
    }

    public Runnable m3Runnable = new Runnable() {
        @Override
        public void run() {
            LogUtil.DefalutLog("LoadingActivity---m3Runnable---isAdExposure:" + isAdExposure);
            toNextPage();
        }
    };

    public void cancleRunable() {
        if (m3Runnable != null) {
            mHandler.removeCallbacks(m3Runnable);
        }
    }

    public void loadXFAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(getContext(), ADUtil.KaiPingYSAD, mListener);
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd();
    }

    IFLYNativeListener mListener = new IFLYNativeListener() {
        @Override
        public void onAdFailed(AdError error) { // 广告请求失败
            LogUtil.DefalutLog("KaipingModel-onAdFailed:"+error.getErrorCode());
            getAd();
        }

        @Override
        public void onAdLoaded(NativeDataRef nativeDataRef) {
            cancleRunable();
            onAdReceive();
            setADData(nativeDataRef);
        }
        @Override
        public void onCancel() { // 下载类广告，下载提示框取消
            toNextPage();
        }

        @Override
        public void onConfirm() { // 下载类广告，下载提示框确认
            toNextPage();
        }
    };

    private void onAdReceive() {
        ad_source.setVisibility(View.VISIBLE);
        skip_view.setVisibility(View.VISIBLE);
        isAdExposure = true;
        DelayToNextPage(4000);
    }

    public void setADData(NativeDataRef mNativeADDataRef) {
        if (mNativeADDataRef != null) {
            if (mNativeADDataRef != null) {
                setAD(mNativeADDataRef);
            }
        }
    }

    public void setAD(final NativeDataRef mNativeADDataRef) {
        DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(Uri.parse(mNativeADDataRef.getImgUrl()))
                .build();
        ad_img.setController(mDraweeController);
        boolean loadingExposure = mNativeADDataRef.onExposure(ad_img);
        LogUtil.DefalutLog("loadingExposure：" + loadingExposure);
        skip_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNextPage();
            }
        });
        ad_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAd();
                boolean onClicked = mNativeADDataRef.onClick(view);
                LogUtil.DefalutLog("NativeDataRef-onClick:" + onClicked);
            }
        });
    }

    public void onClickAd() {
        isAdClicked = true;
        notJump = true;
        cancleRunable();
        AVAnalytics.onEvent(getContext(), "ad_click_kaiping");
    }

    public void loadTXAD() {
        TXADUtil.showKaipingAD(getContext(), splash_container, skip_view,
                new SplashADListener() {
                    @Override
                    public void onADDismissed() {
                        LogUtil.DefalutLog("onADDismissed");
                        if(!notJump){
                            toNextPage();
                        }
                    }

                    @Override
                    public void onNoAD(com.qq.e.comm.util.AdError adError) {
                        LogUtil.DefalutLog("loadTXAD-onNoAD");
                        getAd();
                    }

                    @Override
                    public void onADPresent() {
                        LogUtil.DefalutLog("onADPresent");
                        cancleRunable();
                        skip_view.setVisibility(View.VISIBLE);
                        isAdExposure = true;
                    }

                    @Override
                    public void onADClicked() {
                        LogUtil.DefalutLog("onADClicked");
                        onClickAd();
                    }

                    @Override
                    public void onADTick(long l) {
                        LogUtil.DefalutLog("onADTick:"+l);
                    }

                    @Override
                    public void onADExposure() {
                        LogUtil.DefalutLog("onADExposure");
                    }

                    @Override
                    public void onADLoaded(long l) {

                    }
                });
    }

    public void loadXBKJ() {
        if (ADUtil.isHasLocalAd()) {
            onAdReceive();
            setAD(ADUtil.getRandomAd(getContext()));
        } else {
            DelayToNextPage(1200);
        }
    }

    public void DelayToNextPage(int delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!notJump){
                    toNextPage();
                }
            }
        },delay);
    }

    public void toNextPage() {
        Class mclass = WXEntryActivity.class;
        if(getContext().getPackageName().equals(Setings.application_id_yyj) ||
                getContext().getPackageName().equals(Setings.application_id_yyj_google)){
            mclass = YYJMainActivity.class;
        }
        Intent intent = new Intent(getContext(), mclass);
        getContext().startActivity(intent);
        EventBus.getDefault().post(new KaipingPageEvent("finish"));
    }

    SplashAdListener bdAdListener = new SplashAdListener() {
        @Override
        public void onAdPresent() {
            LogUtil.DefalutLog("BDAD-onAdPresent");
            cancleRunable();
            isAdExposure = true;
        }
        @Override
        public void onAdDismissed() {
            LogUtil.DefalutLog("BDAD-onAdDismissed");
            if(!notJump){
                toNextPage();
            }
        }
        @Override
        public void onAdFailed(String s) {
            LogUtil.DefalutLog("BDAD-onAdFailed："+s);
            getAd();
        }
        @Override
        public void onAdClick() {
            LogUtil.DefalutLog("BDAD-onAdClick");
            onClickAd();
        }
    };

    public void loadBDAD(){
        AdSettings.setSupportHttps(false);
        new SplashAd(getContext(),splash_container,bdAdListener, BDADUtil.BD_KPID,true);
    }

    public void loadCSJAD(){
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(getContext());
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

    public Activity getContext() {
        return mContext.get();
    }

}
