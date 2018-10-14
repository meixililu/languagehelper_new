package com.messi.languagehelper.util;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.impl.OnFinishListener;
import com.qq.e.ads.splash.SplashADListener;

import java.util.List;

/**
 * Created by luli on 27/05/2017.
 */

public class KaiPinAdUIModelCustom {

    private Activity mContext;
    private RelativeLayout ad_layout;
    private View contentLayout;
    private TextView ad_source;
    private TextView progressTv;
    private SimpleDraweeView ad_img;
    private NumberProgressBar numberProgressBar;
    private OnFinishListener mFinishListener;
    private ViewGroup adContainer;
    private View skipContainer;

    IFLYNativeListener mListener = new IFLYNativeListener() {
        @Override
        public void onAdFailed(AdError error) {
            if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
                initTXAD();
            }
        }
        @Override
        public void onADLoaded(List<NativeADDataRef> lst) { // 广告请求成功
            onAdReceive(lst);
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

    public KaiPinAdUIModelCustom(Activity mContext, TextView ad_source, SimpleDraweeView ad_img,
                                 RelativeLayout ad_layout, View contentLayout,
                                 NumberProgressBar numberProgressBar, TextView progressTv,
                                 ViewGroup adContainer, View skipContainer){
        this.mContext = mContext;
        this.ad_source = ad_source;
        this.ad_img = ad_img;
        this.ad_layout = ad_layout;
        this.contentLayout = contentLayout;
        this.progressTv = progressTv;
        this.adContainer = adContainer;
        this.skipContainer = skipContainer;
        this.numberProgressBar = numberProgressBar;
        ad_layout.setVisibility(View.VISIBLE);
        numberProgressBar.setMax(4000);
        initAD();
        timer.start();
    }

    private void initAD(){
        if(ADUtil.Advertiser.equals(ADUtil.Advertiser_TX)){
            initTXAD();
        }else {
            initXFAD();
        }
    }

    private void initXFAD(){
        IFLYNativeAd nativeAd = new IFLYNativeAd(mContext, ADUtil.KaiPingYSAD, mListener);
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private void initTXAD(){
        TXADUtil.showKaipingAD(mContext, adContainer, skipContainer,
                new SplashADListener() {
                    @Override
                    public void onADDismissed() {
                        LogUtil.DefalutLog("onADDismissed");
                        toNextPage();
                    }

                    @Override
                    public void onNoAD(com.qq.e.comm.util.AdError adError) {
                        LogUtil.DefalutLog(adError.getErrorMsg());
                        if(ADUtil.Advertiser.equals(ADUtil.Advertiser_TX)){
                            initXFAD();
                        }
                    }

                    @Override
                    public void onADPresent() {
                        LogUtil.DefalutLog("onADPresent");
                        skipContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onADClicked() {
                        LogUtil.DefalutLog("onADClicked");
                    }

                    @Override
                    public void onADTick(long l) {
                    }

                    @Override
                    public void onADExposure() {

                    }
                });
    }

    private void onAdReceive(List<NativeADDataRef> lst){
        ad_source.setVisibility(View.VISIBLE);
        if(lst != null && lst.size() > 0){
            final NativeADDataRef mNativeADDataRef = lst.get(0);
            if(mNativeADDataRef != null){
                ad_img.setImageURI(mNativeADDataRef.getImage());
                mNativeADDataRef.onExposured(ad_img);
                ad_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean onClicked = mNativeADDataRef.onClicked(view);
                        LogUtil.DefalutLog("onClicked:"+onClicked);
                    }
                });
                ad_source.setText("广告");
            }
        }
    }

    public void toNextPage(){
        ad_layout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
        if(mFinishListener != null){
            mFinishListener.OnFinish();
        }
    }

    private CountDownTimer timer = new CountDownTimer(4500, 10) {

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int)(4500 - millisUntilFinished);
            numberProgressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            toNextPage();
        }
    };

    public void setOnFinishListener(OnFinishListener mFinishListener){
        this.mFinishListener = mFinishListener;
    }
}
