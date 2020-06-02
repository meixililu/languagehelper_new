package com.messi.languagehelper.ViewModel;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.messi.languagehelper.adapter.RcXVideoDetailListAdapter;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FullScreenVideoADModel {

    public WeakReference<Activity> mContext;
    public FrameLayout ad_layout;
    public TextView title;
    public TextView btn_detail;
    public AVObject mAVObject;
    public List mAVObjects;
    public boolean isShowAD;

    private RcXVideoDetailListAdapter videoAdapter;

    public FullScreenVideoADModel(Activity mContext){
        this.mContext = new WeakReference<>(mContext) ;
    }

    public void showAd(){
        if(ADUtil.IsShowAD){
            isShowAD = true;
            loadCSJAD();
        }
    }

    public void justLoadData(){
        isShowAD = false;
        loadCSJADTask();
    }

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD-Video");
        if(mAVObject.get(KeyUtil.VideoAD) == null){
            loadCSJADTask();
        }
    }

    public void loadCSJADTask(){
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(getContext());
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
                ad.setActivityForDownloadApp(getContext());
                ad.setCanInterruptVideoPlay(true);
                if(isShowAD){
                    if(mAVObject != null){
                        mAVObject.put(KeyUtil.VideoAD,ad);
                        mAVObject.put(AVOUtil.XVideo.img_url,ad.getVideoCoverImage().getImageUrl());
                        initAdViewAndAction(ad,ad_layout,title,btn_detail);
                    }
                }else {
                    AVObject item = new AVObject();
                    item.put(KeyUtil.VideoAD,ad);
                    item.put(AVOUtil.XVideo.img_url,ad.getVideoCoverImage().getImageUrl());
                    if(mAVObjects != null){
                        if (mAVObjects.size() > 3) {
                            mAVObjects.add(mAVObjects.size()-3,item);
                        } else {
                            mAVObjects.add(0,item);
                        }
                    }
                }
            }
        });
    }

    public static void initAdViewAndAction(TTDrawFeedAd ad,FrameLayout ad_layout,TextView title,TextView btn_detail){
        btn_detail.setVisibility(View.VISIBLE);
        ViewUtil.removeParentView(ad.getAdView());
        if(ad_layout != null){
            ad_layout.addView(ad.getAdView());
            if(title != null && btn_detail != null){
                title.setText(ad.getTitle());
                btn_detail.setText(ad.getButtonText());
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
    }

    public void setAd_layout(AVObject mAVObject, FrameLayout ad_layout,TextView title,TextView btn_detail) {
        this.ad_layout = ad_layout;
        this.title = title;
        this.btn_detail = btn_detail;
        this.mAVObject = mAVObject;
    }

    public void setAVObjects(List<AVObject> mAVObjects) {
        this.mAVObjects = mAVObjects;
    }

    public void setVideoAdapter(RcXVideoDetailListAdapter videoAdapter) {
        this.videoAdapter = videoAdapter;
    }
    public Activity getContext(){
        return mContext.get();
    }

}
