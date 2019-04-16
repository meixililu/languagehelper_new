package com.messi.languagehelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.messi.languagehelper.ViewModel.VideoADModel;
import com.messi.languagehelper.bean.TTParseBean;
import com.messi.languagehelper.bean.TTParseDataBean;
import com.messi.languagehelper.bean.TTparseVideoBean;
import com.messi.languagehelper.bean.ToutiaoVideoBean;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.ximalaya.ting.android.opensdk.util.DigestUtils;

import java.util.zip.CRC32;

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
    @BindView(R.id.video_layout)
    LinearLayout videoLayout;
    @BindView(R.id.video_ly)
    FrameLayout video_ly;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.next_composition)
    LinearLayout nextComposition;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
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
        parseToutiaoHtml();
        initFullscreenDialog();
        initFullscreenButton();
    }

    private void parseToutiaoHtml(){
        String vid = Url.substring(Url.indexOf("group/")+6,Url.length()-1);
        String tempUrl = "http://www.365yg.com/a"+vid;
        LanguagehelperHttpClient.get(tempUrl,new UICallback(this){
            @Override
            public void onFailured() {
                parseVideoUrl();
            }
            @Override
            public void onFinished() {
            }
            @Override
            public void onResponsed(String responseStr) {
                try{
                    String vid = responseStr.substring(responseStr.indexOf("videoId: '")+10,responseStr.indexOf("videoId: '")+42);
                    String randint = StringUtils.getRandomString(16);
                    String videoid = "/video/urls/v/1/toutiao/mp4/"+vid+"?r="+randint;
                    CRC32 crc32 = new CRC32();
                    crc32.update(videoid.getBytes());
                    long checksum = crc32.getValue();
                    String videoUrl = "http://i.snssdk.com" + videoid + "&s=" + checksum;
//                    LogUtil.DefalutLog("videoUrl:"+videoUrl);
                    parseToutiaoApi(videoUrl);
                }catch (Exception e){
                    parseVideoUrl();
                }
            }
        });
    }

    private void parseToutiaoApi(String url){
        LanguagehelperHttpClient.get(url,new UICallback(this){
            @Override
            public void onFailured() {
                parseVideoUrl();
            }
            @Override
            public void onFinished() {
            }
            @Override
            public void onResponsed(String responseStr) {
                try{
                    ToutiaoVideoBean toutiaoVideo = JSON.parseObject(responseStr,ToutiaoVideoBean.class);
                    String videoUrl = new String(
                            Base64.decode(toutiaoVideo.getData().getVideo_list().
                                            getVideo_1().getMain_url().getBytes(), Base64.DEFAULT));
//                    LogUtil.DefalutLog("videoUrl:"+videoUrl);
                    exoplaer(videoUrl);
                }catch (Exception e){
                    parseVideoUrl();
                }
            }
        });
    }

    private void parseVideoUrl() {
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
                try {
//                    LogUtil.DefalutLog("TTParseApi:" + responseString);
                    if (JsonParser.isJson(responseString)) {
                        TTParseBean result = JSON.parseObject(responseString, TTParseBean.class);
                        if (result != null && result.getSucc() && result.getData() != null) {
                            TTParseDataBean dataBean = result.getData();
                            if (dataBean.getVideo() != null && dataBean.getVideo().size() > 0) {
                                TTparseVideoBean videoBean = dataBean.getVideo().get(0);
                                if (videoBean != null && !TextUtils.isEmpty(videoBean.getUrl())) {
                                    exoplaer(videoBean.getUrl());
                                }
                            }
                        }
                        if(!videoLayout.isShown()){
                            showWebView();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        videoLayout.setVisibility(View.VISIBLE);
        titleTv.setText(mAVObject.getTitle());
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
        player.addListener(this);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        LogUtil.DefalutLog("ACTION_setPlayWhenReady");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        LogUtil.DefalutLog("onPlayerStateChanged:"+playbackState+"-:playWhenReady:"+playWhenReady);
        if(playWhenReady && playbackState == 4){
            mVideoADModel.showAd();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    private void showWebView(){
        videoLayout.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(Url);
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
//        LogUtil.DefalutLog("openFullscreenDialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        mFullScreenDialog.addContentView(simpleExoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_grey600_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
//        LogUtil.DefalutLog("closeFullscreenDialog");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        video_ly.addView(simpleExoPlayerView);
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

    private void initViews() {
        setStatusbarColor(R.color.black);
        Object data =  Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        if(data instanceof Reading){
            mAVObject = (Reading) data;
        }else {
            finish();
            return;
        }
        Setings.MPlayerPause();
        Url = mAVObject.getSource_url();
        mVideoADModel = new VideoADModel(this,xx_ad_layout);
        mWebView.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        mWebView.getSettings().setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript。
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //当前页面加载
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.DefalutLog("WebViewClient:onPageStarted");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.DefalutLog("failingUrl:" + failingUrl);
                if (failingUrl.contains("openapp.jdmobile") || failingUrl.contains("taobao")) {
                    Uri uri = Uri.parse(failingUrl);
                    view.loadUrl("");
                    ADUtil.toAdActivity(ReadDetailTouTiaoActivity.this, uri);
                    ReadDetailTouTiaoActivity.this.finish();
                } else {
                    view.loadUrl("");
                }
                LogUtil.DefalutLog("WebViewClient:onReceivedError---" + errorCode);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.DefalutLog("WebViewClient:onPageFinished");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("openapp.jdmobile") || url.contains("taobao")) {
                    Uri uri = Uri.parse(url);
                    view.loadUrl("");
                    ADUtil.toAdActivity(ReadDetailTouTiaoActivity.this, uri);
                    ReadDetailTouTiaoActivity.this.finish();
                    return true;
                } else if (url.contains("bilibili:")) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReadDetailTouTiaoActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        addVideoNewsList();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
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
        if(mFullScreenDialog != null){
            mFullScreenDialog.dismiss();
        }
    }
}
