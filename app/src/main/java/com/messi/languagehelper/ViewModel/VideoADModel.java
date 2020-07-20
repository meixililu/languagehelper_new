package com.messi.languagehelper.ViewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.lang.ref.WeakReference;
import java.util.List;

public class VideoADModel {

    public int counter;
    private WeakReference<Context> mContext;
    public SharedPreferences sp;

    public FrameLayout xx_ad_layout;
    public FrameLayout ad_layout;
    public ImageView ad_close_btn;

    public VideoADModel(Context mContext,FrameLayout xx_ad_layout){
        this.mContext = new WeakReference<>(mContext);
        sp = Setings.getSharedPreferences(mContext);
        this.xx_ad_layout = xx_ad_layout;
        ad_layout = xx_ad_layout.findViewById(R.id.ad_layout);
        ad_close_btn = xx_ad_layout.findViewById(R.id.ad_close_btn);
        ad_close_btn.setOnClickListener(v -> xx_ad_layout.setVisibility(View.GONE));
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
            loadCSJAD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD-Video");
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADUtil.CSJ_XXLSP)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(690, 388)
                .build();
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadCSJAD-onError:"+s);
                xx_ad_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    xx_ad_layout.setVisibility(View.GONE);
                    return;
                }
                XXLRootModel.setCSJDView(getContext(),ads.get(0),ad_layout);
            }
        });
    }

    public Context getContext() {
        return mContext.get();
    }

}
