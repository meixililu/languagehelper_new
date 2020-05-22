package com.messi.languagehelper.util;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;

/**
 * Created by luli on 27/05/2017.
 */

public class KaiPinAdUIModel {

    private Activity mContext;
    private RelativeLayout ad_layout;
    private View contentLayout;
    private TextView ad_source;
    private TextView progressTv;
    private SimpleDraweeView ad_img;
    private NumberProgressBar numberProgressBar;


    public KaiPinAdUIModel(Activity mContext, TextView ad_source,SimpleDraweeView ad_img,
                           RelativeLayout ad_layout,View contentLayout,
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

        timer.start();
    }

    private void onAdReceive(NativeDataRef mNativeADDataRef){
        ad_source.setVisibility(View.VISIBLE);

    }

    public void toNextPage(){
        ad_layout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }

    private CountDownTimer timer = new CountDownTimer(2000, 10) {

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
