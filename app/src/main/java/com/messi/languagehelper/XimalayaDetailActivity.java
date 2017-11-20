package com.messi.languagehelper;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
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

public class XimalayaDetailActivity extends BaseActivity implements IXmPlayerStatusListener {


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
    private List<Track> trackList;
    private Track currentTrack;
    private int position;
    private long play_times;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ximalaya_detail_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.none);
        initData();
        initViews();
    }

    private void initData() {
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
                }else {
                    playBtn.setImageResource(R.drawable.player_pause_selector);
                }
            }
        } else {
            XmPlayerManager.getInstance(this).playList(trackList, position);
            setToPlay();
        }
        XmPlayerManager.getInstance(this).addPlayerStatusListener(this);
    }

    private void setToPlay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playBtn.setImageResource(R.drawable.player_pause_selector);
            }
        },500);
    }

    private void setListPlayStatus(){
        for (Track mTrack : trackList) {
            mTrack.setUpdateStatus(false);
        }
        currentTrack.setUpdateStatus(true);
    }

    private void setToPN(){
        if(XmPlayerManager.getInstance(this).hasNextSound()){
            playNext.setEnabled(true);
        }else {
            playNext.setEnabled(false);
        }
        if(XmPlayerManager.getInstance(this).hasPreSound()){
            playPrevious.setEnabled(true);
        }else {
            playPrevious.setEnabled(false);
        }
    }

    private void initViews() {
        itemImg.setImageURI(currentTrack.getCoverUrlLarge());
        announcer_icon.setImageURI(currentTrack.getAnnouncer().getAvatarUrl());
        albumTitle.setText(currentTrack.getAlbum().getAlbumTitle());
        announcer_info.setText(currentTrack.getAnnouncer().getNickname());
        trackContent.setText(currentTrack.getTrackTitle());
        trackIntro.setText(currentTrack.getTrackIntro());
        setToPN();
    }


    @Override
    public void updateUI(String music_action) {
        if (music_action.equals(PlayerService.action_loading)) {
            showProgressbar();
        } else if (music_action.equals(PlayerService.action_finish_loading)) {
            hideProgressbar();
        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterBroadcast();
    }

    @OnClick({R.id.play_btn, R.id.play_previous, R.id.play_next})
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
        }
    }

    private void playOrPause(){
        if (XmPlayerManager.getInstance(this).isPlaying()) {
            playBtn.setImageResource(R.drawable.player_play_selector);
            trackList.get(position).setUpdateStatus(false);
            XmPlayerManager.getInstance(this).pause();
        }else {
            playBtn.setImageResource(R.drawable.player_pause_selector);
            trackList.get(position).setUpdateStatus(true);
            XmPlayerManager.getInstance(this).play();
        }
    }

    private void playNext(){
        if(XmPlayerManager.getInstance(this).hasNextSound()){
            position++;
            XmPlayerManager.getInstance(this).playList(trackList, position);
        }else {
            ToastUtil.diaplayMesShort(this,"sorry,没有了!");
        }
    }
    private void playPrevious(){
        if(XmPlayerManager.getInstance(this).hasPreSound()){
            position--;
            XmPlayerManager.getInstance(this).playList(trackList, position);
        }else {
            ToastUtil.diaplayMesShort(this,"sorry,没有了!");
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
        LogUtil.DefalutLog("onSoundSwitch:"+position);
        position = XmPlayerManager.getInstance(this).getCurrentIndex();
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            currentTrack = (Track) XmPlayerManager.getInstance(this).getCurrSound();
        }
        setListPlayStatus();
        initViews();
        LogUtil.DefalutLog("onSoundSwitch:"+position);
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
        LogUtil.DefalutLog("currPos:"+currPos+"---duration:"+duration);
        playTimeDuration.setText( TimeUtil.getDuration(duration/1000) );
        playTimeCurrent.setText( TimeUtil.getDuration(currPos/1000) );
        seekbar.setMax(duration);
        seekbar.setProgress(currPos);
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
}
