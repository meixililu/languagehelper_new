package com.messi.languagehelper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.messi.languagehelper.ViewModel.VideoADModel;
import com.messi.languagehelper.bean.TTParseBean;
import com.messi.languagehelper.bean.TTParseDataBean;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.DialogUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.util.DigestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;


public class ReadDetailTouTiaoActivity extends BaseActivity implements FragmentProgressbarListener,
        Player.EventListener {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    @BindView(R.id.refreshable_webview)
    WebView mWebView;
    @BindView(R.id.player_view)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.video_ly)
    FrameLayout videoLayout;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.next_composition)
    LinearLayout nextComposition;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.webview_layout)
    FrameLayout webview_layout;
    @BindView(R.id.collect_btn)
    ImageView collect_btn;
    private String Url;
    private Reading mAVObject;
    private SimpleExoPlayer player;

    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;
    private int mResumeWindow;
    private long mResumePosition;
    private VideoADModel mVideoADModel;
    private int status;
    private boolean isWVPSuccess;
    private String[] interceptUrls;
    private boolean isIntercept;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_detail_toutiao_activity);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        initViews();
        initFullscreenDialog();
        initFullscreenButton();
    }

    private void initViews() {
        SharedPreferences sp = Setings.getSharedPreferences(this);
        String intercepts = sp.getString(KeyUtil.InterceptUrls, "");
        LogUtil.DefalutLog("InterceptUrls:" + intercepts);
        if (!TextUtils.isEmpty(intercepts)) {
            if (intercepts.contains(",")) {
                interceptUrls = intercepts.split(",");
            }
        }
        setStatusbarColor(R.color.black);
        Object data = Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        if (data instanceof Reading) {
            mAVObject = (Reading) data;
        } else {
            finish();
            return;
        }
        titleTv.setText(mAVObject.getTitle());
        setCollected();
        Setings.MPlayerPause();
        Url = mAVObject.getSource_url();
        LogUtil.DefalutLog("Url:" + Url);
        mVideoADModel = new VideoADModel(this, xx_ad_layout);
        setWebView();
        setData();
        addVideoNewsList();
    }

    private void setCollected() {
        if (TextUtils.isEmpty(mAVObject.getIsCollected())) {
            collect_btn.setImageResource(R.drawable.uncollected);
        } else {
            collect_btn.setImageResource(R.drawable.collect_d);
        }
    }

    private void setData() {
        if (!TextUtils.isEmpty(mAVObject.getContent_type())) {
            if ("url_api".equals(mAVObject.getContent_type())) {
                parseVideoUrl();
            } else if ("url_media".equals(mAVObject.getContent_type())) {
                exoplaer(mAVObject.getMedia_url());
            } else if ("url".equals(mAVObject.getContent_type()) || "url_intercept".equals(mAVObject.getContent_type()) ) {
                showProgressbar();
                isIntercept = true;
                mWebView.loadUrl(Url);
            } else {
                showWebView();
            }
        } else {
            exoplaer(mAVObject.getMedia_url());
        }
    }

    private void showWebView(){
        hideProgressbar();
        isIntercept = false;
        isWVPSuccess = true;
        webview_layout.setVisibility(View.VISIBLE);
        videoLayout.setVisibility(View.GONE);
        mWebView.loadUrl(Url);
    }

    private void setWebView() {
        mWebView.requestFocus();
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.DefalutLog("WebViewClient:onPageStarted");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.DefalutLog("WebViewClient:onPageFinished");
                if (!isWVPSuccess) {
                    parseVideoUrl();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.DefalutLog("shouldOverrideUrlLoading:" + url);
                if (url.contains("bilibili:")) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
                LogUtil.DefalutLog("shouldInterceptRequest:" + url);
                interceptUrl(url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                DialogUtil.OnReceivedSslError(ReadDetailTouTiaoActivity.this,handler);
            }
        });
    }

    private void interceptUrl(String url) {
        if(isIntercept){
            boolean isPlay = false;
            if (interceptUrls != null) {
                for (String str : interceptUrls) {
                    if (url.contains(str)) {
                        isWVPSuccess = true;
                        isPlay = true;
                        break;
                    }
                }
                if (isPlay) {
                    ReadDetailTouTiaoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exoplaer(url);
                        }
                    });
                }
            }
        }
    }

    private void parseVideoUrl() {
        LogUtil.DefalutLog("parseVideoUrl");
        showProgressbar();
        Long timestamp = System.currentTimeMillis();
        String sign = DigestUtils.md5Hex(Url + timestamp + Setings.TTParseClientSecretKey);
        FormBody formBody = new FormBody.Builder()
                .add("link", Url)
                .add("timestamp", timestamp + "")
                .add("sign", sign)
                .add("client", Setings.TTParseClientId)
                .build();
        LanguagehelperHttpClient.post(Setings.TTParseApi, formBody, new UICallback(this) {
            @Override
            public void onResponsed(String responseString) {
                LogUtil.DefalutLog("parseVideoUrl-responseString:" + responseString);
                String videoUrl = "";
                try {
                    if (JsonParser.isJson(responseString)) {
                        TTParseBean result = JSON.parseObject(responseString, TTParseBean.class);
                        if (result != null && result.getSucc() && result.getData() != null) {
                            TTParseDataBean videoBean = result.getData();
                            if (videoBean != null && !TextUtils.isEmpty(videoBean.getVideo())) {
                                videoUrl = videoBean.getVideo();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!TextUtils.isEmpty(videoUrl)) {
                        exoplaer(videoUrl);
                    } else {
                        onFailured();
                    }
                }
            }

            @Override
            public void onFailured() {
                showWebView();
            }

            @Override
            public void onFinished() {
                hideProgressbar();
            }
        });
    }

    private void exoplaer(String media_url) {
        LogUtil.DefalutLog("status:" + status);
        hideProgressbar();
        if (status == 0) {
            status = 1;
            webview_layout.setVisibility(View.GONE);
            videoLayout.setVisibility(View.VISIBLE);
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
            player.addListener(this);
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
            LogUtil.DefalutLog("ACTION_setPlayWhenReady");
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        LogUtil.DefalutLog("onPlayerStateChanged:" + playbackState + "-:playWhenReady:" + playWhenReady);
        if (playWhenReady && playbackState == Player.STATE_ENDED) {
            mVideoADModel.showAd();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        mFullScreenDialog.addContentView(simpleExoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_grey600_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        videoLayout.addView(simpleExoPlayerView);
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

    private void addVideoNewsList() {
        Fragment fragment = ReadingFragment.newInstanceByType("video", 2000, true);
        if (getApplication().getPackageName().equals(Setings.application_id_yys) ||
                getApplication().getPackageName().equals(Setings.application_id_yys_google)) {
            fragment = ReadingFragmentYYS.newInstance();
        } else if (getApplication().getPackageName().equals(Setings.application_id_ywcd)) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
        status = 1;
        releasePlayer();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        releasePlayer();
        onBackPressed();
    }

    public void releasePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
            player = null;
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog.dismiss();
        }
    }

    @OnClick(R.id.collect_btn)
    public void onClick() {
        if(mAVObject != null){
            if(TextUtils.isEmpty(mAVObject.getIsCollected())){
                mAVObject.setIsCollected("1");
                mAVObject.setCollected_time(System.currentTimeMillis());
            }else {
                mAVObject.setIsCollected("");
                mAVObject.setCollected_time(0);
            }
            setCollected();
            BoxHelper.update(mAVObject);
        }
    }
}
