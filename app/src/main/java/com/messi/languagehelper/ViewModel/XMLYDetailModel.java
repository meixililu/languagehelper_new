package com.messi.languagehelper.ViewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTBannerAd;
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
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.TXADUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class XMLYDetailModel {

    public static boolean misVisibleToUser;
    public int counter;
    private WeakReference<Context> mContext;
    public SharedPreferences sp;
    private NativeExpressADView mTXADView;
    private long lastLoadAd;
    private String currentAD;
    private String XFADID = ADUtil.XXLAD;
    public String BDADID = BDADUtil.BD_BANNer_XXL;

    public LinearLayout xx_ad_layout;
    public ImageView imgCover;
    public SimpleDraweeView adImg;
    public ImageView adClose;
    public TextView adTitle;
    public TextView ad_btn;
    public FrameLayout ad_layout;

    public XMLYDetailModel(Context mContext){
        this.mContext = new WeakReference<>(mContext);
        sp = Setings.getSharedPreferences(mContext);
    }

    public void setViews(TextView adTitle,SimpleDraweeView adImg,ImageView adClose,TextView ad_btn,
                         LinearLayout xx_ad_layout,FrameLayout ad_layout,ImageView imgCover){
        this.xx_ad_layout = xx_ad_layout;
        this.imgCover = imgCover;
        this.adImg = adImg;
        this.adClose = adClose;
        this.adTitle = adTitle;
        this.ad_btn = ad_btn;
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
            isShowAd(View.VISIBLE);
            if(!TextUtils.isEmpty(currentAD)){
                LogUtil.DefalutLog("------ad-------"+currentAD);
                if(ADUtil.GDT.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.BD.equals(currentAD)){
                    loadTXAD2();
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
                LogUtil.DefalutLog("XMLYDetailModel-onAdFailed");
                getAd();
            }
            @Override
            public void onAdLoaded(NativeDataRef nativeDataRef) {
                if(nativeDataRef != null){
                    setAd(nativeDataRef);
                }
            }

        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd();
    }

    public void setAd(NativeDataRef nad) {
        if (nad != null) {
            adImg.setVisibility(View.VISIBLE);
            adClose.setVisibility(View.VISIBLE);
            adTitle.setVisibility(View.VISIBLE);
            ad_btn.setVisibility(View.VISIBLE);
            ad_layout.setVisibility(View.GONE);
            closeAdAuto();
            adImg.setImageURI(nad.getImgUrl());
            adTitle.setText(nad.getTitle());
            boolean isExposure = nad.onExposure(xx_ad_layout);
            LogUtil.DefalutLog("isExposure:" + isExposure);
            xx_ad_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nad.onClick(xx_ad_layout);
                }
            });
            adClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isShowAd(View.GONE);
                }
            });
        }
    }

    private void loadTXAD() {
        TXADUtil.showXXL_STXW(getContext(), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog("onNoAD");
                getAd();
            }
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if(NullUtil.isNotEmpty(list)){
                    if(mTXADView != null){
                        mTXADView.destroy();
                    }
                    ad_layout.setVisibility(View.VISIBLE);
                    ad_layout.removeAllViews();
                    mTXADView = list.get(0);
                    closeAdAuto();
                    ad_layout.addView(mTXADView);
                    mTXADView.render();
                }
            }
            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
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

    private void loadTXAD2() {
        TXADUtil.showBigImg(getContext(), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog("loadTXAD0-onNoAD");
                getAd();
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if(NullUtil.isNotEmpty(list)){
                    if(mTXADView != null){
                        mTXADView.destroy();
                    }
                    ad_layout.setVisibility(View.VISIBLE);
                    ad_layout.removeAllViews();
                    mTXADView = list.get(0);
                    closeAdAuto();
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
            isShowAd(View.GONE);
        }
    }

    public void loadBDAD(){
        AdView adView = new AdView(getContext(),BDADID);
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
                getAd();
                LogUtil.DefalutLog("BDAD-onAdFailed");
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
        initFeiXFAD();
        int height = (int)(SystemUtil.SCREEN_WIDTH / 1.5);
        int margin = ScreenUtil.dip2px(getContext(),70);
        int margin1 = ScreenUtil.dip2px(getContext(),40);
        LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-margin, height-margin1);
        ad_layout.addView(adView,rllp);
        closeAdAuto();
    }

    public void initFeiXFAD(){
        adImg.setVisibility(View.GONE);
        adClose.setVisibility(View.GONE);
        adTitle.setVisibility(View.GONE);
        ad_btn.setVisibility(View.GONE);
        ad_layout.setVisibility(View.VISIBLE);
        ad_layout.removeAllViews();
    }

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD");
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADUtil.CSJ_BANNer2ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(690, 388)
                .build();
        mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadCSJAD-onError:"+s);
                getAd();
            }
            @Override
            public void onBannerAdLoad(TTBannerAd ad) {
                if (ad == null) {
                    getAd();
                    return;
                }
                View bannerView = ad.getBannerView();
                if (bannerView == null) {
                    getAd();
                    return;
                }
                initFeiXFAD();
                int height = (int)(SystemUtil.SCREEN_WIDTH / 1.77);
                int margin = ScreenUtil.dip2px(getContext(),70);
                LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-margin,
                        height);
                ad_layout.addView(bannerView,rllp);
                closeAdAuto();
                //设置广告互动监听回调
                ad.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected(int position, String value) {
                        getAd();
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onRefuse() {
                    }
                });
            }
        });
    }

    public void closeAdAuto() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isShowAd(View.GONE);
            }
        }, 7000);
    }

    public void isShowAd(int visiable) {
        xx_ad_layout.setVisibility(visiable);
        imgCover.setVisibility(visiable);
    }

    public void reLoadAD(){
        if (!xx_ad_layout.isShown()) {
            getAd();
        }
    }

    public void onDestroy(){
        if (mTXADView != null) {
            mTXADView.destroy();
            mTXADView = null;
        }
    }

    public void setXFADID(String XFADID) {
        this.XFADID = XFADID;
    }

    public Context getContext() {
        return mContext.get();
    }

}
