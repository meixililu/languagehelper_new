package com.messi.languagehelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SymbolDetailActivity extends BaseActivity implements OnClickListener {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    @BindView(R.id.videoplayer)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.symbol_en)
    TextView symbol_en;
    @BindView(R.id.symbol_des)
    TextView symbol_des;
    @BindView(R.id.symbol_play_img)
    ImageButton symbol_play_img;
    @BindView(R.id.symbol_cover)
    FrameLayout symbol_cover;
    @BindView(R.id.play_img)
    ImageView play_img;
    @BindView(R.id.teacher_play_img)
    ImageButton teacher_play_img;
    @BindView(R.id.teacher_cover)
    FrameLayout teacher_cover;
    @BindView(R.id.symbol_info)
    TextView symbol_info;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.app_bar)
    LinearLayout appBar;
    @BindView(R.id.error_txt)
    TextView error_txt;

    private SimpleExoPlayer player;
    private MediaPlayer mPlayer;
    private String audioPath;
    private SymbolListDao avObject;
    private String SDAudioMp3FullName, SDTeacherMp3FullName;
    private String SDAudioMp3Name, SDTeacherMp3Name;
    private String SDAudioMp3Url, SDTeacherMp3Url;
    private String currentFileFullName;
    private int loopTime;

    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private LinearLayout back_btn;
    private Dialog mFullScreenDialog;
    private int mResumeWindow;
    private long mResumePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symbol_study_activity);
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

    private void initData() {
        mPlayer = new MediaPlayer();
        avObject = (SymbolListDao) Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        symbol_cover.setOnClickListener(this);
        teacher_cover.setOnClickListener(this);
        if (avObject != null) {
            audioPath = SDCardUtil.SymbolPath + avObject.getSDCode() + SDCardUtil.Delimiter;
        } else {
            finish();
        }
    }

    private void setData() {
        try {
            symbol_en.setText(avObject.getSDName());
            symbol_des.setText(avObject.getSDDes());
            symbol_info.setText(avObject.getSDInfo());
            SDAudioMp3Url = avObject.getSDAudioMp3Url();
            SDAudioMp3Name = SDAudioMp3Url.substring(SDAudioMp3Url.lastIndexOf("/") + 1);
            SDAudioMp3FullName = SDCardUtil.getDownloadPath(audioPath) + SDAudioMp3Name;
            if (!SDCardUtil.isFileExist(SDAudioMp3FullName)) {
                DownLoadUtil.downloadFile(this, SDAudioMp3Url, audioPath, SDAudioMp3Name, null);
            }
            SDTeacherMp3Url = avObject.getSDTeacherMp3Url();
            SDTeacherMp3Name = SDTeacherMp3Url.substring(SDTeacherMp3Url.lastIndexOf("/") + 1);
            SDTeacherMp3FullName = SDCardUtil.getDownloadPath(audioPath) + SDTeacherMp3Name;
            if (!SDCardUtil.isFileExist(SDTeacherMp3FullName)) {
                DownLoadUtil.downloadFile(this, SDTeacherMp3Url, audioPath, SDTeacherMp3Name, null);
            }
            if(!TextUtils.isEmpty(avObject.getBackup1())){
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                simpleExoPlayerView.setUseArtwork(true);
                exoplaer(avObject.getBackup1());
//                avObject.getBackup2()
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @SuppressLint("SourceLockedOrientationActivity")
    private void closeFullscreenDialog() {
        LogUtil.DefalutLog("closeFullscreenDialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        appBar.addView(simpleExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_white));
    }

    private void initFullscreenButton() {
        back_btn = simpleExoPlayerView.findViewById(R.id.back_btn);
        mFullScreenIcon = simpleExoPlayerView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = simpleExoPlayerView.findViewById(R.id.exo_fullscreen_button);
        back_btn.setOnClickListener(view -> onBack_btn());
        mFullScreenButton.setOnClickListener(view -> {
            if (!mExoPlayerFullscreen) {
                openFullscreenDialog();
            } else {
                closeFullscreenDialog();
            }
        });
    }

    private void exoplaer(String media_url) {
        IPlayerUtil.pauseAudioPlayer(this);
        player = ExoPlayerFactory.newSimpleInstance(this);
        simpleExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            simpleExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "LanguageHelper"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(media_url));
        player.prepare(videoSource);
        player.setPlayWhenReady(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void openFullscreenDialog() {
        LogUtil.DefalutLog("openFullscreenDialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        mFullScreenDialog.addContentView(simpleExoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_white));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                hideProgressbar();
                initUri(currentFileFullName);
            }
        }
    };

    private void playMp3(final String uriPath) {
        try {
            if (mPlayer.isPlaying()) {
                play_img.setImageResource(R.drawable.ic_play_circle_outline);
                if (!uriPath.equals(currentFileFullName)) {
                    mPlayer.stop();
                    initUri(uriPath);
                } else {
                    mPlayer.pause();
                }
                currentFileFullName = uriPath;
            } else {
                if (TextUtils.isEmpty(currentFileFullName) || !uriPath.equals(currentFileFullName)) {
                    initUri(uriPath);
                } else {
                    if (uriPath.equals(SDTeacherMp3FullName)) {
                        play_img.setImageResource(R.drawable.ic_pause_circle_outline);
                    }
                    mPlayer.start();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUri(final String uriPath) {
        try {
            if (uriPath.equals(SDTeacherMp3FullName)) {
                play_img.setImageResource(R.drawable.ic_pause_circle_outline);
            }
            mPlayer.reset();
            Uri uri = Uri.parse(uriPath);
            mPlayer.setDataSource(this, uri);
            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (uriPath.equals(SDTeacherMp3FullName)) {
                        play_img.setImageResource(R.drawable.ic_play_circle_outline);
                    } else {
                        replay();
                    }
                }
            });
            mPlayer.prepare();
            mPlayer.start();
            if (uriPath.equals(SDTeacherMp3FullName)) {
                play_img.setImageResource(R.drawable.ic_pause_circle_outline);
            }
            currentFileFullName = uriPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replay() {
        loopTime++;
        if (loopTime < 3) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    onClick(symbol_cover);
                }
            }, 1000);
        } else {
            loopTime = 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symbol_cover:
                if (SDCardUtil.isFileExist(SDTeacherMp3FullName)) {
                    playMp3(SDAudioMp3FullName);
                } else {
                    currentFileFullName = SDAudioMp3FullName;
                    DownLoadUtil.downloadFile(this, SDAudioMp3Url, audioPath, SDAudioMp3Name, mHandler);
                }
                AVAnalytics.onEvent(SymbolDetailActivity.this, "symbol_pg_play_mp3");
                break;
            case R.id.teacher_cover:
                IPlayerUtil.pauseAudioPlayer(this);
                if (SDCardUtil.isFileExist(SDTeacherMp3FullName)) {
                    playMp3(SDTeacherMp3FullName);
                } else {
                    currentFileFullName = SDTeacherMp3FullName;
                    DownLoadUtil.downloadFile(this, SDTeacherMp3Url, audioPath, SDTeacherMp3Name, mHandler);
                }
                AVAnalytics.onEvent(SymbolDetailActivity.this, "symbol_pg_play_teacher_mp3");
                break;
            default:
                break;
        }
    }

    public void onBack_btn() {
        if (!mExoPlayerFullscreen) {
            onBackPressed();
        }else {
            closeFullscreenDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        if (mHandler != null) {
            mHandler = null;
        }
        releasePlayer();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
            player = null;
        }
        if(mFullScreenDialog != null){
            mFullScreenDialog.dismiss();
        }
    }

}
