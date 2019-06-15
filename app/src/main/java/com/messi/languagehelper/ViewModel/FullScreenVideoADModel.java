package com.messi.languagehelper.ViewModel;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class FullScreenVideoADModel {

    public Activity mContext;
    public FrameLayout ad_layout;
    public TextView title;
    private AVObject mAVObject;

    public TextView btn_detail;

    public FullScreenVideoADModel(Activity mContext){
        this.mContext = mContext;
    }

    public void showAd(){
        if(ADUtil.IsShowAD){
            loadAD();
        }
    }

    public void loadAD(){
        try {
            loadCSJAD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD-Video");
        if(mAVObject.get(KeyUtil.VideoAD) != null){
            Object object = mAVObject.get(KeyUtil.VideoAD);
            if(object instanceof TTDrawFeedAd){
                TTDrawFeedAd ad = (TTDrawFeedAd)object;
                initAdViewAndAction(ad);
            }
        }else {
            loadCSJADTask();
        }
    }

    public void loadCSJADTask(){
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADUtil.CSJ_DrawXXLSP)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setAdCount(1)
                .build();
        mTTAdNative.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadDrawFeedAd:"+s);
            }

            @Override
            public void onDrawFeedAdLoad(List<TTDrawFeedAd> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                TTDrawFeedAd ad = list.get(0);
                ad.setActivityForDownloadApp(mContext);
                ad.setCanInterruptVideoPlay(true);
                if(mAVObject != null){
                    mAVObject.put(KeyUtil.VideoAD,ad);
                }
                initAdViewAndAction(ad);
            }
        });
    }

    public void initAdViewAndAction(TTDrawFeedAd ad){
        if(ad_layout != null){
            ViewUtil.removeParentView(ad.getAdView());
            ad_layout.addView(ad.getAdView());
        }
        if(title != null && btn_detail != null){
            title.setText(ad.getTitle());
            btn_detail.setText(ad.getButtonText());
            LogUtil.DefalutLog("img:"+ad.getVideoCoverImage().getImageUrl());
            List<View> clickViews = new ArrayList<>();
            clickViews.add(title);
            List<View> creativeViews = new ArrayList<>();
            creativeViews.add(btn_detail);
            ad.registerViewForInteraction(ad_layout, clickViews, creativeViews, new TTNativeAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TTNativeAd ad) {
                    LogUtil.DefalutLog("onAdClicked");
                }

                @Override
                public void onAdCreativeClick(View view, TTNativeAd ad) {
                    LogUtil.DefalutLog("onAdCreativeClick");
                }

                @Override
                public void onAdShow(TTNativeAd ad) {
                    LogUtil.DefalutLog("onAdShow");
                }
            });
        }

    }

    public void setAd_layout(AVObject mAVObject, FrameLayout ad_layout,TextView title,TextView btn_detail) {
        this.ad_layout = ad_layout;
        this.title = title;
        this.btn_detail = btn_detail;
        this.mAVObject = mAVObject;
        btn_detail.setVisibility(View.VISIBLE);
    }

}
