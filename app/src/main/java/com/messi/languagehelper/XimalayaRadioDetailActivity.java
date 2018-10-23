package com.messi.languagehelper;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NotificationUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.TXADUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.schedule.LiveAnnouncer;
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
import com.ximalaya.ting.android.opensdk.model.live.schedule.ScheduleList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.messi.languagehelper.service.PlayerService.action_pause;
import static com.messi.languagehelper.service.PlayerService.action_start;

public class XimalayaRadioDetailActivity extends BaseActivity implements IXmPlayerStatusListener {


    @BindView(R.id.item_img)
    SimpleDraweeView itemImg;
    @BindView(R.id.announcer_icon)
    SimpleDraweeView announcer_icon;
    @BindView(R.id.announcer_info)
    TextView announcer_info;
    @BindView(R.id.album_title)
    TextView albumTitle;
    @BindView(R.id.play_btn)
    ImageView playBtn;
    @BindView(R.id.ad_img)
    SimpleDraweeView adImg;
    @BindView(R.id.ad_close)
    ImageView adClose;
    @BindView(R.id.ad_title)
    TextView adTitle;
    @BindView(R.id.ad_btn)
    TextView adBtn;
    @BindView(R.id.ad_layout)
    LinearLayout adLayout;
    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.back_btn)
    FrameLayout backBtn;
    @BindView(R.id.source_name)
    TextView sourceName;
    @BindView(R.id.content_tv)
    LinearLayout contentTv;
    private Radio radio;
    private int position;
    private NativeADDataRef nad;
    private NativeExpressADView mTXADView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ximalaya_radio_detail_activity);
        ButterKnife.bind(this);
        registerBroadcast();
        setStatusbarColor(R.color.none);
        initData();
        initViews();
    }

    private void initData() {
        radio = getIntent().getParcelableExtra(KeyUtil.XmlyRadio);
        if (XmPlayerManager.getInstance(this).isPlaying()) {
            if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Radio) {
                Radio mRadio = (Radio) XmPlayerManager.getInstance(this).getCurrSound();
                if (mRadio.getDataId() != radio.getDataId()) {
                    XmPlayerManager.getInstance(this).playRadio(radio);
                    setToPlay();
                } else {
                    playBtn.setImageResource(R.drawable.player_pause_selector);
                }
            } else {
                XmPlayerManager.getInstance(this).pause();
                XmPlayerManager.getInstance(this).playRadio(radio);
                setToPlay();
            }
        } else {
            XmPlayerManager.getInstance(this).playRadio(radio);
            setToPlay();
        }
        XmPlayerManager.getInstance(this).addPlayerStatusListener(this);
    }

    private void setToPlay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationUtil.sendBroadcast(XimalayaRadioDetailActivity.this, action_pause);
                NotificationUtil.showNotification(XimalayaRadioDetailActivity.this, action_pause,
                        radio.getProgramName(),
                        NotificationUtil.mes_type_xmly);
                playBtn.setImageResource(R.drawable.player_pause_selector);
            }
        }, 500);
    }

    private void initViews() {
        getProgramList();
        if (Setings.musicSrv.isPlaying()) {
            Setings.musicSrv.pause();
        }
        loadAD();
        itemImg.setImageURI(radio.getCoverUrlLarge());
        announcer_icon.setImageURI(radio.getCoverUrlLarge());
        albumTitle.setText(radio.getRadioName());
        announcer_info.setText(radio.getProgramName());
        sourceName.setText(StringUtils.numToStrTimes(radio.getRadioPlayCount()));
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        sourceName.setCompoundDrawables(drawable, null, null, null);
    }

    private void loadAD(){
        if(ADUtil.IsShowAD){
            if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
                loadXFAD();
            }else {
                loadTXAD();
            }
        }
    }

    private void loadXFAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(this, ADUtil.XXLAD, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
                if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
                    loadTXAD();
                }else {
                    onADFaile();
                }
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if (adList != null && adList.size() > 0) {
                    setAd(adList.get(0));
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private void onADFaile(){
        if(ADUtil.isHasLocalAd()){
            setAd(ADUtil.getRandomAd(this));
        }else {
            isShowAd(View.GONE);
        }
    }

    private void setAd(NativeADDataRef mNativeADDataRef) {
        closeAdAuto();
        isShowAd(View.VISIBLE);
        nad = mNativeADDataRef;
        adImg.setImageURI(nad.getImage());
        adTitle.setText(nad.getTitle());
        boolean isExposure = nad.onExposured(adLayout);
        LogUtil.DefalutLog("isExposure:" + isExposure);
    }

    private void loadTXAD(){
        TXADUtil.showXXL_STXW(this, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog(adError.getErrorMsg());
                if(ADUtil.Advertiser.equals(ADUtil.Advertiser_TX)){
                    loadXFAD();
                }else {
                    onADFaile();
                }
            }
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if(list != null && list.size() > 0){
                    if(mTXADView != null){
                        mTXADView.destroy();
                    }
                    adLayout.setVisibility(View.VISIBLE);
                    adLayout.removeAllViews();
                    mTXADView = list.get(0);
                    closeAdAuto();
                    isShowAd(View.VISIBLE);
                    adLayout.addView(mTXADView);
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

    private void closeAdAuto() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isShowAd(View.GONE);
            }
        }, 7000);
    }

    private void isShowAd(int visiable) {
        adLayout.setVisibility(visiable);
        imgCover.setVisibility(visiable);
    }

    @Override
    public void updateUI(String music_action) {
        LogUtil.DefalutLog("xima-updateUI:" + music_action);
        if (music_action.equals(action_start)) {
            playBtn.setImageResource(R.drawable.player_play_selector);
        } else if (music_action.equals(PlayerService.action_pause)) {
            playBtn.setImageResource(R.drawable.player_pause_selector);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        if(mTXADView != null){
            mTXADView.destroy();
        }
    }

    @OnClick({R.id.play_btn, R.id.ad_close, R.id.ad_btn,
            R.id.ad_img, R.id.ad_title, R.id.back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                playOrPause();
                break;
            case R.id.ad_close:
                isShowAd(View.GONE);
                break;
            case R.id.ad_btn:
                clickAd();
                break;
            case R.id.ad_img:
                clickAd();
                break;
            case R.id.ad_title:
                clickAd();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    private void clickAd() {
        if (nad != null) {
            boolean onClicked = nad.onClicked(adLayout);
            LogUtil.DefalutLog("onClicked:" + onClicked);
        }
    }

    private void playOrPause() {
        if (XmPlayerManager.getInstance(this).isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void pause() {
        NotificationUtil.showNotification(this, action_start, radio.getProgramName(),
                NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this, action_start);
        playBtn.setImageResource(R.drawable.player_play_selector);
        XmPlayerManager.getInstance(this).pause();
        if (!adLayout.isShown()) {
            loadAD();
        }
    }

    private void play() {
        NotificationUtil.showNotification(this, action_pause, radio.getProgramName(),
                NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this, action_pause);
        playBtn.setImageResource(R.drawable.player_pause_selector);
        XmPlayerManager.getInstance(this).play();
    }

    private void getProgramList(){
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.RADIOID, String.valueOf(radio.getDataId()));
        CommonRequest.getSchedules(map, new IDataCallBack<ScheduleList>(){
            @Override
            public void onSuccess(@Nullable ScheduleList scheduleList) {
                hideProgressbar();
                if (scheduleList != null && scheduleList.getmScheduleList() != null) {
                    contentTv.removeAllViews();
                    for (Schedule mSchedule : scheduleList.getmScheduleList()){
                        contentTv.addView(initAlbum(mSchedule));
                        contentTv.addView(ViewUtil.getLine(XimalayaRadioDetailActivity.this));
                    }
                }
            }
            @Override
            public void onError(int i, String s) {
                hideProgressbar();
            }
        });
    }

    private View initAlbum(final Schedule mAVObject) {
        View view = LayoutInflater.from(this).inflate(R.layout.xmly_radio_schedulelist, null);
        FrameLayout layout_cover = (FrameLayout) view.findViewById(R.id.layout_cover);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView sub_title = (TextView) view.findViewById(R.id.sub_title);
        TextView radio_time = (TextView) view.findViewById(R.id.radio_time);
        TextView radio_replay = (TextView) view.findViewById(R.id.radio_replay);
        if(mAVObject.getRelatedProgram() != null){
            title.setText(mAVObject.getRelatedProgram().getProgramName());
            if(mAVObject.getRelatedProgram().getAnnouncerList() != null){
                String announces = "";
                for (LiveAnnouncer announcer : mAVObject.getRelatedProgram().getAnnouncerList()){
                    announces += announcer.getNickName() + " ";
                }
                sub_title.setText(announces);
            }
        }

        radio_time.setText(mAVObject.getStartTime()+"-"+mAVObject.getEndTime());
        radio_replay.setText("");
        return view;
    }

    @Override
    public void onPlayStart() {
    }

    @Override
    public void onPlayPause() {
    }

    @Override
    public void onPlayStop() {
    }

    @Override
    public void onSoundPlayComplete() {
    }

    @Override
    public void onSoundPrepared() {
    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int percent) {

    }

    @Override
    public void onPlayProgress(int currPos, int duration) {
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }

}
