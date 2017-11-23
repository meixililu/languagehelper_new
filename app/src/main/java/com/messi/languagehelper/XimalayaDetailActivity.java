package com.messi.languagehelper;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.messi.languagehelper.util.TimeUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.messi.languagehelper.service.PlayerService.action_pause;
import static com.messi.languagehelper.service.PlayerService.action_start;

public class XimalayaDetailActivity extends BaseActivity implements IXmPlayerStatusListener, SeekBar.OnSeekBarChangeListener {


    @BindView(R.id.item_img)
    SimpleDraweeView itemImg;
    @BindView(R.id.announcer_icon)
    SimpleDraweeView announcer_icon;
    @BindView(R.id.announcer_info)
    TextView announcer_info;
    @BindView(R.id.album_title)
    TextView albumTitle;
    @BindView(R.id.track_content)
    TextView trackContent;
    @BindView(R.id.track_intro)
    TextView trackIntro;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.play_time_current)
    TextView playTimeCurrent;
    @BindView(R.id.play_time_duration)
    TextView playTimeDuration;
    @BindView(R.id.play_btn)
    ImageView playBtn;
    @BindView(R.id.play_previous)
    ImageView playPrevious;
    @BindView(R.id.play_next)
    ImageView playNext;
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
    private List<Track> trackList;
    private Track currentTrack;
    private int position;
    private NativeADDataRef nad;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ximalaya_detail_activity);
        ButterKnife.bind(this);
        registerBroadcast();
        setStatusbarColor(R.color.none);
        initData();
        initViews();
    }

    private void initData() {
        seekbar.setOnSeekBarChangeListener(this);
        position = getIntent().getIntExtra(KeyUtil.PositionKey, 10);
        trackList = (List<Track>) WXEntryActivity.dataMap.get(KeyUtil.List);
        currentTrack = trackList.get(position);
        setListPlayStatus();
        if (XmPlayerManager.getInstance(this).isPlaying()) {
            if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
                Track mTrack = (Track) XmPlayerManager.getInstance(this).getCurrSound();
                if (mTrack.getDataId() != trackList.get(position).getDataId()) {
                    XmPlayerManager.getInstance(this).playList(trackList, position);
                    setToPlay();
                } else {
                    playBtn.setImageResource(R.drawable.player_pause_selector);
                }
            }
        } else {
            XmPlayerManager.getInstance(this).playList(trackList, position);
            setToPlay();
        }
        XmPlayerManager.getInstance(this).addPlayerStatusListener(this);
    }

    private void setToPlay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationUtil.sendBroadcast(XimalayaDetailActivity.this,action_pause);
                NotificationUtil.showNotification(XimalayaDetailActivity.this,action_pause,
                        currentTrack.getTrackTitle(),
                        NotificationUtil.mes_type_xmly);
                playBtn.setImageResource(R.drawable.player_pause_selector);
            }
        }, 500);
    }

    private void setListPlayStatus() {
        for (Track mTrack : trackList) {
            mTrack.setUpdateStatus(false);
        }
        currentTrack.setUpdateStatus(true);
    }

    private void setToPN() {
        if (XmPlayerManager.getInstance(this).hasNextSound()) {
            playNext.setEnabled(true);
        } else {
            playNext.setEnabled(false);
        }
        if (XmPlayerManager.getInstance(this).hasPreSound()) {
            playPrevious.setEnabled(true);
        } else {
            playPrevious.setEnabled(false);
        }
    }

    private void initViews() {
        if(WXEntryActivity.musicSrv.isPlaying()){
            WXEntryActivity.musicSrv.pause();
        }
        loadAD();
        itemImg.setImageURI(currentTrack.getCoverUrlLarge());
        announcer_icon.setImageURI(currentTrack.getAnnouncer().getAvatarUrl());
        albumTitle.setText(currentTrack.getAlbum().getAlbumTitle());
        announcer_info.setText(currentTrack.getAnnouncer().getNickname());
        trackContent.setText(currentTrack.getTrackTitle());
        trackIntro.setText(currentTrack.getTrackIntro());
        seekbar.setMax(currentTrack.getDuration() * 1000);
        playTimeDuration.setText(TimeUtil.getDuration(currentTrack.getDuration()));
        setToPN();
    }

    private void loadAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(this, ADUtil.XXLAD, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onAdFailed(AdError arg0) {
                isShowAd(View.GONE);
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
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

    private void setAd(NativeADDataRef mNativeADDataRef) {
        closeAdAuto();
        isShowAd(View.VISIBLE);
        nad = mNativeADDataRef;
        adImg.setImageURI(nad.getImage());
        adTitle.setText(nad.getTitle());
        nad.onExposured(adLayout);
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
        LogUtil.DefalutLog("xima-updateUI:"+music_action);
        if(music_action.equals(action_start)){
            playBtn.setImageResource(R.drawable.player_play_selector);
        }else if (music_action.equals(PlayerService.action_pause)) {
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
    }

    @OnClick({R.id.play_btn, R.id.play_previous, R.id.play_next, R.id.ad_close, R.id.ad_btn,
            R.id.ad_img, R.id.ad_title,R.id.back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                playOrPause();
                break;
            case R.id.play_previous:
                playPrevious();
                break;
            case R.id.play_next:
                playNext();
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

    private void pause(){
        NotificationUtil.showNotification(this,action_start,currentTrack.getTrackTitle(),
                NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this,action_start);
        playBtn.setImageResource(R.drawable.player_play_selector);
        trackList.get(position).setUpdateStatus(false);
        XmPlayerManager.getInstance(this).pause();
        if (!adLayout.isShown()) {
            loadAD();
        }
    }

    private void play(){
        NotificationUtil.showNotification(this,action_pause,currentTrack.getTrackTitle(),
                NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this,action_pause);
        playBtn.setImageResource(R.drawable.player_pause_selector);
        trackList.get(position).setUpdateStatus(true);
        XmPlayerManager.getInstance(this).play();
    }

    private void playNext() {
        if (XmPlayerManager.getInstance(this).hasNextSound()) {
            position++;
            XmPlayerManager.getInstance(this).playList(trackList, position);
        } else {
            ToastUtil.diaplayMesShort(this, "sorry,没有了!");
        }
    }

    private void playPrevious() {
        if (XmPlayerManager.getInstance(this).hasPreSound()) {
            position--;
            XmPlayerManager.getInstance(this).playList(trackList, position);
        } else {
            ToastUtil.diaplayMesShort(this, "sorry,没有了!");
        }
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
        LogUtil.DefalutLog("onSoundPlayComplete");
        currentTrack.setUpdateStatus(false);
    }

    @Override
    public void onSoundPrepared() {
    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        LogUtil.DefalutLog("onSoundSwitch:" + position);
        position = XmPlayerManager.getInstance(this).getCurrentIndex();
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            currentTrack = (Track) XmPlayerManager.getInstance(this).getCurrSound();
        }
        setListPlayStatus();
        initViews();
        NotificationUtil.showNotification(this,action_pause,currentTrack.getTrackTitle(),
                NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this,action_pause);
        LogUtil.DefalutLog("onSoundSwitch:" + position);
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
        LogUtil.DefalutLog("currPos:" + currPos + "---duration:" + duration);
        playTimeCurrent.setText(TimeUtil.getDuration(currPos / 1000));
        seekbar.setProgress(currPos);
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        XmPlayerManager.getInstance(this).pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        XmPlayerManager.getInstance(this).seekTo(seekBar.getProgress());
        XmPlayerManager.getInstance(this).play();
        playBtn.setImageResource(R.drawable.player_pause_selector);
    }
}
