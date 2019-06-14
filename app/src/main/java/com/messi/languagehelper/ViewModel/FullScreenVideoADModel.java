package com.messi.languagehelper.ViewModel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.util.ArrayList;
import java.util.List;

public class FullScreenVideoADModel {

    public Activity mContext;
    public SharedPreferences sp;
    public FrameLayout ad_layout;
    public TextView title;

    public TextView btn_detail;

    public FullScreenVideoADModel(Activity mContext){
        this.mContext = mContext;
        sp = Setings.getSharedPreferences(mContext);
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
                for (TTDrawFeedAd ad : list) {
                    ad.setActivityForDownloadApp(mContext);
                    ad.setCanInterruptVideoPlay(true);
                }
                if(ad_layout != null){
                    ad_layout.addView(list.get(0).getAdView());
                    initAdViewAndAction(list.get(0));
                }
            }
        });
    }

    private void initAdViewAndAction(TTDrawFeedAd ad){
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

    public void setAd_layout(FrameLayout ad_layout,TextView title,TextView btn_detail) {
        this.ad_layout = ad_layout;
        this.title = title;
        this.btn_detail = btn_detail;
        btn_detail.setVisibility(View.VISIBLE);
    }

}
