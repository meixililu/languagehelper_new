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
import com.messi.languagehelper.ViewModel.XMLYDetailModel;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NotificationUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ViewUtil;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.messi.languagehelper.service.PlayerService.action_pause;
import static com.messi.languagehelper.service.PlayerService.action_restart;

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
    @BindView(R.id.xx_ad_layout)
    LinearLayout xx_ad_layout;
    @BindView(R.id.ad_layout)
    FrameLayout ad_layout;
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
    private XMLYDetailModel mXMLYDetailModel;


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
        mXMLYDetailModel = new XMLYDetailModel(this);
        mXMLYDetailModel.setViews(adTitle,adImg,adClose,adBtn,xx_ad_layout,ad_layout,imgCover);
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
        if (IPlayerUtil.MPlayerIsPlaying()) {
            IPlayerUtil.MPlayerPause();
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
        if(mXMLYDetailModel != null){
            mXMLYDetailModel.showAd();
        }
    }

    @Override
    public void updateUI(String music_action) {
        LogUtil.DefalutLog("xima-updateUI:" + music_action);
        if (music_action.equals(action_restart)) {
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
        XmPlayerManager.getInstance(this).removePlayerStatusListener(this);
        if(mXMLYDetailModel != null){
            mXMLYDetailModel.onDestroy();
        }
    }

    @OnClick({R.id.play_btn, R.id.back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                playOrPause();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
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
        NotificationUtil.showNotification(this, action_restart, radio.getProgramName(),
                NotificationUtil.mes_type_xmly);
        NotificationUtil.sendBroadcast(this, action_restart);
        playBtn.setImageResource(R.drawable.player_play_selector);
        XmPlayerManager.getInstance(this).pause();
        if(mXMLYDetailModel != null){
            mXMLYDetailModel.reLoadAD();
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
