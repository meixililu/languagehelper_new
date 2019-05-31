package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.ViewModel.LeisureModel;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.TimeUtil;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadingDetailActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.ad_sign)
    TextView ad_sign;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.ad_layout)
    FrameLayout ad_layout;
    @BindView(R.id.ad_img)
    SimpleDraweeView ad_img;
    @BindView(R.id.next_composition)
    LinearLayout next_composition;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    @BindView(R.id.player_layout)
    LinearLayout player_layout;
    @BindView(R.id.btn_play)
    ImageView btn_play;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.time_current)
    TextView time_current;
    @BindView(R.id.time_duration)
    TextView time_duration;

    private Reading mAVObject;
    private List<Reading> mAVObjects;
    private SharedPreferences mSharedPreferences;
    private int index;
    private LeisureModel mLeisureModel;

    private Handler handler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setSeekbarAndText();
            handler.postDelayed(this,300);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composition_detail_activity);
        registerBroadcast();
        ButterKnife.bind(this);
        initData();
        setData();
        guide();
    }

    private void initData() {
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        index = getIntent().getIntExtra(KeyUtil.IndexKey, 0);
        Object data =  Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        if(data instanceof List){
            mAVObjects = (List<Reading>) data;
            if(mAVObjects != null && mAVObjects.size() > index){
                mAVObject = mAVObjects.get(index);
            }
        }
    }

    private void setData() {
        if (mAVObject == null) {
            finish();
            return;
        }
        seekbar.setOnSeekBarChangeListener(this);
        toolbar_layout.setTitle(mAVObject.getTitle());
        title.setText(mAVObject.getTitle());
        scrollview.scrollTo(0, 0);
        TextHandlerUtil.handlerText(this, mProgressbar, content, mAVObject.getContent());
        if (!TextUtils.isEmpty(mAVObject.getImg_url())) {
            ad_sign.setVisibility(View.GONE);
            ad_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
        }else {
            mLeisureModel = new LeisureModel(this);
            mLeisureModel.setXFADID(ADUtil.NewsDetail);
            mLeisureModel.setViews(ad_sign,ad_img,xx_ad_layout,ad_layout);
            mLeisureModel.showAd();
        }
        if(Setings.MPlayerIsSameMp3(mAVObject)){
            if(Setings.musicSrv.PlayerStatus == 1) {
                btn_play.setImageResource(R.drawable.ic_pause_circle_outline_grey600_36dp);
                handler.postDelayed(mRunnable,300);
            }
            setSeekbarAndText();
        }
        if("text".equals(mAVObject.getType())){
            player_layout.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(mAVObject.getStatus())){
            mAVObject.setStatus("1");
            BoxHelper.update(mAVObject);
        }
        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!"text".equals(mAVObject.getType())){
                    if(scrollY - oldScrollY > 12){
                        if(player_layout.isShown()){
                           player_layout.setVisibility(View.GONE);
                           ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(player_layout, "alpha", 1, 0);
                           mObjectAnimator.setDuration(500).start();
                        }
                    }else if(scrollY - oldScrollY < -8){
                        if(!player_layout.isShown()){
                            player_layout.setVisibility(View.VISIBLE);
                            ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(player_layout, "alpha", 0, 1);
                            mObjectAnimator.setDuration(500).start();
                        }
                    }
                }
            }
        });
    }

    private void setSeekbarAndText(){
        if(Setings.musicSrv != null){
            int currentPosition = Setings.musicSrv.getCurrentPosition();
            int mDuration = Setings.musicSrv.getDuration();
            LogUtil.DefalutLog("ContentPosition:"+currentPosition);
            LogUtil.DefalutLog("Duration:"+mDuration);
            if(mDuration > 0){
                seekbar.setMax(mDuration);
                time_duration.setText(TimeUtil.getDuration(mDuration / 1000));
            }
            seekbar.setProgress(currentPosition);
            time_current.setText(TimeUtil.getDuration(currentPosition / 1000));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.composition, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu.size() > 1){
            setMenuIcon(menu.getItem(1));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_share:
                copyOrshare(0);
                break;
            case R.id.action_collected:
                if(TextUtils.isEmpty(mAVObject.getIsCollected())){
                    mAVObject.setIsCollected("1");
                    mAVObject.setCollected_time(System.currentTimeMillis());
                }else {
                    mAVObject.setIsCollected("");
                    mAVObject.setCollected_time(0);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                }
                setMenuIcon(item);
                BoxHelper.update(mAVObject);
                break;
        }
        return true;
    }

    private void setMenuIcon(MenuItem item){
        if(TextUtils.isEmpty(mAVObject.getIsCollected())){
            item.setIcon(this.getResources().getDrawable(R.drawable.ic_uncollected_white));
        }else{
            item.setIcon(this.getResources().getDrawable(R.drawable.ic_collected_white));
        }
    }

    private void copyOrshare(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(mAVObject.getTitle());
        sb.append("\n");
        sb.append(mAVObject.getContent());
        if (i == 0) {
            Setings.share(this, sb.toString());
        } else {
            Setings.copy(this, sb.toString());
        }
    }

    @OnClick(R.id.btn_play)
    public void onClick() {
        if (Setings.musicSrv != null) {
            Setings.musicSrv.initAndPlay(mAVObject);
        }
    }

    @Override
    public void updateUI(String music_action) {
        if(Setings.MPlayerIsSameMp3(mAVObject)){
            if(PlayerService.action_start.equals(music_action)){
                btn_play.setImageResource(R.drawable.ic_play_circle_outline_grey600_36dp);
                handler.removeCallbacks(mRunnable);
            }else if (PlayerService.action_pause.equals(music_action)) {
                btn_play.setImageResource(R.drawable.ic_pause_circle_outline_grey600_36dp);
                handler.postDelayed(mRunnable,300);
            }
        }
        if(PlayerService.action_loading.equals(music_action)){
            showProgressbar();
        }else if(PlayerService.action_finish_loading.equals(music_action)){
            hideProgressbar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        if(handler != null){
            handler.removeCallbacks(mRunnable);
        }
        if(mLeisureModel != null){
            mLeisureModel.onDestroy();
        }
    }

    private void guide() {
        if (!mSharedPreferences.getBoolean(KeyUtil.isReadingDetailGuideShow, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("");
            builder.setMessage("点击英文单词即可查询词意。");
            builder.setPositiveButton("确认", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.isReadingDetailGuideShow, true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mRunnable);
        Setings.MPlayerPause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Setings.MPlayerSeekTo(seekBar.getProgress());
        Setings.MPlayerRestart();
        handler.postDelayed(mRunnable,300);
    }
}
