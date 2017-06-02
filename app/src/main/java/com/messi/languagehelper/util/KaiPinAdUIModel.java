package com.messi.languagehelper.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;

import java.util.List;

/**
 * Created by luli on 27/05/2017.
 */

public class KaiPinAdUIModel {

    private Activity mContext;
    private RelativeLayout ad_layout;
    private LinearLayout contentLayout;
    private TextView ad_source;
    private TextView progressTv;
    private SimpleDraweeView ad_img;
    private NumberProgressBar numberProgressBar;

    IFLYNativeListener mListener = new IFLYNativeListener() {
        @Override
        public void onAdFailed(AdError error) { }
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

    public KaiPinAdUIModel(Activity mContext, TextView ad_source,SimpleDraweeView ad_img,
                           RelativeLayout ad_layout,LinearLayout contentLayout,
                           NumberProgressBar numberProgressBar,TextView progressTv){
        this.mContext = mContext;
        this.ad_source = ad_source;
        this.ad_img = ad_img;
        this.ad_layout = ad_layout;
        this.contentLayout = contentLayout;
        this.progressTv = progressTv;
        this.numberProgressBar = numberProgressBar;
        ad_layout.setVisibility(View.VISIBLE);
        numberProgressBar.setMax(4000);
        init();
    }

    private void init(){
        IFLYNativeAd nativeAd = new IFLYNativeAd(mContext, ADUtil.KaiPingYSAD, mListener);
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
        timer.start();
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
                ad_source.setText(mNativeADDataRef.getAdSourceMark()+"广告");
            }
        }
    }

    public void toNextPage(){
        ad_layout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }

    private CountDownTimer timer = new CountDownTimer(4500, 10) {

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int)(4500 - millisUntilFinished);
            numberProgressBar.setProgress(progress);
            if(progress > 2700){
                progressTv.setText("一切OK，等您来闯");
            }else if(progress > 1700){
                progressTv.setText("随机分配关卡");
            }else if(progress > 1100){
                progressTv.setText("获取关卡数据");
            }
        }

        @Override
        public void onFinish() {
            toNextPage();
        }
    };

}
