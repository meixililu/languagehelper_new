package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
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
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.TimeUtil;
import com.messi.languagehelper.util.utilLrc;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadingDetailLrcActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

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
    private utilLrc mutilLrc;
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
        if (mAVObject == null) {
            finish();
            return;
        }
    }

    private void setData() {
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
                downloadLrc();
            }
            setSeekbarAndText();
        }
        if(mAVObject.getType().equals("text")){
            player_layout.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(mAVObject.getStatus())){
            mAVObject.setStatus("1");
            BoxHelper.update(mAVObject);
        }
        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!mAVObject.getType().equals("text")){
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
//        LogUtil.DefalutLog("Duration:"+mDuration);
            if(mDuration > 0){
                seekbar.setMax(mDuration);
                time_duration.setText(TimeUtil.getDuration(mDuration / 1000));
            }
            seekbar.setProgress(currentPosition);
            time_current.setText(TimeUtil.getDuration(currentPosition / 1000));
            if(mutilLrc != null){
                List<utilLrc.Statement> list = mutilLrc.getLrcList();
                if(list != null && list.size() > 0){
                    if(content != null){
                        content.setTextColor(getResources().getColor(R.color.green_500));
                    }
                    for(int i=0;i<list.size();i++){
                        if(currentPosition < list.get(i).getTime()){
                            if(i-1 >= 0){
                                if(content != null){
                                    content.setText(list.get(i-1).getLyric());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void downloadLrc(){
        LogUtil.DefalutLog("downloadLrc:"+mAVObject.getLrc_url());
        if(!TextUtils.isEmpty(mAVObject.getLrc_url())){
            final String name = mAVObject.getObject_id() + ".lrc";
            final String lrcfilepath = SDCardUtil.getDownloadPath(SDCardUtil.lrcPath) + name;
            LogUtil.DefalutLog("lrcfilepath:"+lrcfilepath);
            LogUtil.DefalutLog("isExist:"+SDCardUtil.isFileExist(lrcfilepath));
            if(!SDCardUtil.isFileExist(lrcfilepath)){
                LanguagehelperHttpClient.get(mAVObject.getLrc_url(),new BgCallback(){
                    @Override
                    public void onFailured() {
                    }

                    @Override
                    public void onResponsed(String responseString) {
                        DownLoadUtil.saveFile(ReadingDetailLrcActivity.this,
                                SDCardUtil.lrcPath,name,responseString.getBytes());
                        try {
                            mutilLrc = new utilLrc(lrcfilepath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                try {
                    mutilLrc = new utilLrc(lrcfilepath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
        downloadLrc();
    }

    @Override
    public void updateUI(String music_action) {
        if(Setings.MPlayerIsSameMp3(mAVObject)){
            if(music_action.equals(PlayerService.action_start)){
                btn_play.setImageResource(R.drawable.ic_play_circle_outline_grey600_36dp);
                handler.removeCallbacks(mRunnable);
            }else if (music_action.equals(PlayerService.action_pause)) {
                btn_play.setImageResource(R.drawable.ic_pause_circle_outline_grey600_36dp);
                handler.postDelayed(mRunnable,300);
            }
        }
        if(music_action.equals(PlayerService.action_loading)){
            showProgressbar();
        }else if(music_action.equals(PlayerService.action_finish_loading)){
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
