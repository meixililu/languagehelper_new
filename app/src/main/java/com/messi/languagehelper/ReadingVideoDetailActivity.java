package com.messi.languagehelper;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadingVideoDetailActivity extends BaseActivity implements FragmentProgressbarListener {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    @BindView(R.id.title_tv)
    TextView title;
    @BindView(R.id.next_composition)
    LinearLayout next_composition;
    @BindView(R.id.player_view)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.app_bar)
    LinearLayout appBar;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;

    private SimpleExoPlayer player;
    private Reading mAVObject;
    private List<Reading> mAVObjects;
    private int index;

    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private int mResumeWindow;
    private long mResumePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusbarColor(R.color.black);
        setContentView(R.layout.reading_video_detail_activity);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        initData();
        setData();
        initFullscreenDialog();
        initFullscreenButton();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen) closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        LogUtil.DefalutLog("openFullscreenDialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        mFullScreenDialog.addContentView(simpleExoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_grey600_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        LogUtil.DefalutLog("closeFullscreenDialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        appBar.addView(simpleExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_grey600_24dp));
    }

    private void initFullscreenButton() {
        mFullScreenIcon = simpleExoPlayerView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = simpleExoPlayerView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initData() {
        Setings.MPlayerPause();
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

    private void exoplaer(String media_url) {
        player = ExoPlayerFactory.newSimpleInstance(this,
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        simpleExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            simpleExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "LanguageHelper"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(media_url));
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }

    private void setData() {
        if (mAVObject == null) {
            finish();
            return;
        }
        title.setText(mAVObject.getTitle());
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setUseArtwork(true);
        exoplaer(mAVObject.getMedia_url());
        addVideoNewsList();
        if (TextUtils.isEmpty(mAVObject.getStatus())) {
            mAVObject.setStatus("1");
            BoxHelper.update(mAVObject);
        }
    }

    private void addVideoNewsList(){
        Fragment fragment = ReadingFragment.newInstanceByType("video", 2000,true);
        if(getApplication().getPackageName().equals(Setings.application_id_yys) ||
                getApplication().getPackageName().equals(Setings.application_id_yys_google)){
            fragment = ReadingFragmentYYS.newInstance();
        } else if(getApplication().getPackageName().equals(Setings.application_id_ywcd)){
            fragment = ReadingFragmentYWCD.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.next_composition, fragment)
                .commit();
    }

    private void collected() {
        if (TextUtils.isEmpty(mAVObject.getIsCollected())) {
            mAVObject.setIsCollected("1");
            mAVObject.setCollected_time(System.currentTimeMillis());
        } else {
            mAVObject.setIsCollected("");
            mAVObject.setCollected_time(0);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }
        BoxHelper.update(mAVObject);
    }

    @Override
    public void onBackPressed() {
        releasePlayer();
        super.onBackPressed();
    }

    public void releasePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
            player = null;
        }
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
