package com.messi.languagehelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import com.messi.languagehelper.service.PlayerService.MusicBinder;

public class ReadingDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.xx_ad_layout)
    RelativeLayout xx_ad_layout;
    @BindView(R.id.next_composition)
    LinearLayout next_composition;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    @BindView(R.id.play_btn)
    FloatingActionButton fab;
    @BindView(R.id.item_img)
    SimpleDraweeView pimgview;
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;

    private Reading mAVObject;
    private List<Reading> mAVObjects;
    private SharedPreferences mSharedPreferences;
    private int index;
    private XFYSAD mXFYSAD;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideProgressbar();
            if (msg.what == 1) {
                playMp3();
            } else if (msg.what == 3) {
                mAVObject.setMedia_url("");
                playContent();
            } else {
                fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
            if (msg.what == MyThread.EVENT_PLAY_OVER) {
                fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composition_detail_activity);
        ButterKnife.bind(this);
        initData();
        setData();
        guide();
    }

    private void initData() {
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        mAVObjects = (List<Reading>) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        index = getIntent().getIntExtra(KeyUtil.IndexKey, 0);
        mAVObject = mAVObjects.get(index);
        WXEntryActivity.dataMap.clear();
        if (mAVObject == null) {
            finish();
        }
    }

    private void setData() {
        videoplayer.setVisibility(View.GONE);
        pimgview.setVisibility(View.GONE);
        toolbar_layout.setTitle(mAVObject.getTitle());
        title.setText(mAVObject.getTitle());
        scrollview.scrollTo(0, 0);
        TextHandlerUtil.handlerText(this, mProgressbar, content, mAVObject.getContent());
        if (!TextUtils.isEmpty(mAVObject.getType()) &&
                mAVObject.getType().equals("video")) {
            videoplayer.setVisibility(View.VISIBLE);
            videoplayer.setUp(mAVObject.getMedia_url(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
            if (!TextUtils.isEmpty(mAVObject.getImg_url())) {
                Glide.with(this)
                        .load(mAVObject.getImg_url())
                        .into(videoplayer.thumbImageView);
            }
            fab.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(mAVObject.getImg_url())) {
            pimgview.setVisibility(View.VISIBLE);
            pimgview.setImageURI(Uri.parse(mAVObject.getImg_url()));
        }
        if(WXEntryActivity.musicSrv.isSameMp3(mAVObject)){
            if(WXEntryActivity.musicSrv.PlayerStatus == 1) {
                fab.setImageResource(R.drawable.ic_stop_white_48dp);
            }
        }

        if(mAVObject.getType().equals("text")){
            fab.setVisibility(View.GONE);
        }
        int[] random = NumberUtil.getRandomNumberLimit(mAVObjects.size(), 0, 5, index);
        next_composition.removeAllViews();
        for (int i : random) {
            next_composition.addView(ViewUtil.getLine(this));
            next_composition.addView(getView(mAVObjects.get(i)));
        }
        mXFYSAD = new XFYSAD(this, xx_ad_layout, ADUtil.NewsDetail);
        mXFYSAD.setDirectExPosure(false);
        mXFYSAD.showAD();
        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (xx_ad_layout.isShown()) {
                    if (XFYSAD.isInScreen(ReadingDetailActivity.this, xx_ad_layout)) {
                        LogUtil.DefalutLog("onScrollChange---isInScreen");
                        mXFYSAD.ExposureAD();
                    }
                }
            }
        });
        if(TextUtils.isEmpty(mAVObject.getStatus())){
            mAVObject.setStatus("1");
            DataBaseUtil.getInstance().update(mAVObject);
        }
    }

    private void playContent() {
        String type = mAVObject.getType();
        if (type.equals("mp3") && !TextUtils.isEmpty(mAVObject.getMedia_url())) {
            playByMp3();
        }
    }

    private void playByMp3() {
        if (mAVObject != null) {
            String downLoadUrl = mAVObject.getMedia_url();
            int pos = downLoadUrl.lastIndexOf(SDCardUtil.Delimiter) + 1;
            String fileName = downLoadUrl.substring(pos, downLoadUrl.length());
            String rootUrl = SDCardUtil.ReadingPath +
                    mAVObject.getObject_id() + SDCardUtil.Delimiter;
            String fileFullName = SDCardUtil.getDownloadPath(rootUrl) + fileName;
            LogUtil.DefalutLog("fileName:" + fileName + "---fileFullName:" + fileFullName);
            if (SDCardUtil.isFileExist(fileFullName)) {
                playMp3();
                LogUtil.DefalutLog("FileExist");
            } else {
                LogUtil.DefalutLog("FileNotExist");
                showProgressbar();
                DownLoadUtil.downloadFile(this, downLoadUrl, rootUrl, fileName, mHandler);
            }
        }
    }

    private void playMp3() {
        WXEntryActivity.musicSrv.initAndPlay(mAVObject);
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
                }else {
                    mAVObject.setIsCollected("");
                }
                setMenuIcon(item);
                DataBaseUtil.getInstance().update(mAVObject);
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
            Settings.share(this, sb.toString());
        } else {
            Settings.copy(this, sb.toString());
        }
    }

    @OnClick(R.id.play_btn)
    public void onClick() {
        if (WXEntryActivity.musicSrv != null) {
            if(WXEntryActivity.musicSrv.PlayerStatus == 0){
                fab.setImageResource(R.drawable.ic_stop_white_48dp);
                playContent();
            } else if(WXEntryActivity.musicSrv.PlayerStatus == 1){
                if(WXEntryActivity.musicSrv.isSameMp3(mAVObject)){
                    fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    WXEntryActivity.musicSrv.pause();
                }else {
                    fab.setImageResource(R.drawable.ic_stop_white_48dp);
                    playContent();
                }
            }else if(WXEntryActivity.musicSrv.PlayerStatus == 2){
                fab.setImageResource(R.drawable.ic_stop_white_48dp);
                if(WXEntryActivity.musicSrv.isSameMp3(mAVObject)){
                    WXEntryActivity.musicSrv.restart();
                }else {
                    playContent();
                }
            }
        }
    }

    public View getView(final Reading mObject) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.composition_list_item, null);
        FrameLayout layout_cover = (FrameLayout) convertView.findViewById(R.id.layout_cover);
        LinearLayout list_item_img_parent = (LinearLayout) convertView.findViewById(R.id.list_item_img_parent);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView source_name = (TextView) convertView.findViewById(R.id.source_name);
        TextView type_name = (TextView) convertView.findViewById(R.id.type_name);
        SimpleDraweeView list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);

        if (!mObject.isAd()) {
            title.setText(mObject.getTitle());
            source_name.setText(mObject.getSource_name());
            type_name.setText(mObject.getType_name());
            String img_url = "";
            if (mObject.getImg_type().equals("url")) {
                img_url = mObject.getImg_url();
            }
            if (!TextUtils.isEmpty(img_url)) {
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                list_item_img.setImageURI(Uri.parse(img_url));
            } else {
                list_item_img_parent.setVisibility(View.GONE);
                list_item_img.setVisibility(View.GONE);
            }
            layout_cover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = mAVObjects.indexOf(mObject);
                    mAVObject = mObject;
                    setData();
                }
            });
        } else {
            final NativeADDataRef mNativeADDataRef = mObject.getmNativeADDataRef();
            title.setText(mNativeADDataRef.getSubTitle());
            type_name.setText(mNativeADDataRef.getTitle());
            source_name.setText("VoiceAds广告");
            list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImage()));
            mNativeADDataRef.onExposured(layout_cover);
            layout_cover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNativeADDataRef.onClicked(v);
                }
            });
        }
        return convertView;
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void guide() {
        if (!mSharedPreferences.getBoolean(KeyUtil.isReadingDetailGuideShow, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage("点击英文单词即可查询词意。");
            builder.setPositiveButton("确认", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.isReadingDetailGuideShow, true);
        }
    }

}
