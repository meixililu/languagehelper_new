package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class WordStudySummaryDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.xx_ad_layout)
    RelativeLayout xx_ad_layout;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private AVObject mAVObject;
    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_summary_detail_activity);
        ButterKnife.bind(this);
        initData();
        setData();
    }

    private void initData() {
        mAVObject = (AVObject) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        WXEntryActivity.dataMap.clear();
        if (mAVObject == null) {
            finish();
        }
    }

    private void setData() {
        String titleStr = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
        toolbar_layout.setTitle(titleStr);
        title.setText(titleStr);
        scrollview.scrollTo(0, 0);
        TextHandlerUtil.handlerText(this, mProgressbar, content, mAVObject.getString(AVOUtil.HJWordStudyCList.word_des));

        mXFYSAD = new XFYSAD(this, xx_ad_layout, ADUtil.NewsDetail);
        mXFYSAD.setDirectExPosure(false);
        mXFYSAD.showAD();
        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (xx_ad_layout.isShown()) {
                    if (XFYSAD.isInScreen(WordStudySummaryDetailActivity.this, xx_ad_layout)) {
                        LogUtil.DefalutLog("onScrollChange---isInScreen");
                        mXFYSAD.ExposureAD();
                    }
                }
            }
        });
    }
}
