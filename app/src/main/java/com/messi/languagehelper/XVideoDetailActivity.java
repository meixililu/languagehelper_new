package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVObject;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.messi.languagehelper.ViewModel.FullScreenVideoADModel;
import com.messi.languagehelper.adapter.RcXVideoDetailListAdapter;
import com.messi.languagehelper.adapter.RcXVideoDetailListItemViewHolder;
import com.messi.languagehelper.bean.TTParseBean;
import com.messi.languagehelper.bean.TTParseDataBean;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DialogUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

public class XVideoDetailActivity extends BaseActivity implements Player.EventListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;

    private SimpleExoPlayer player;
    private List<AVObject> mAVObjects;
    private AVObject mAVObject;
    private int position;
    private ImageView cover_img;
    private String Url = "";
    private String media_url = "";
    private String type;

    private RcXVideoDetailListAdapter videoAdapter;
    private PlayerView player_view;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    public WebView mWebView;
    private boolean isWVPSuccess;
    private String[] interceptUrls;
    private boolean isIntercept;
    private boolean isAD;
    private FullScreenVideoADModel mFSVADModel;
    private boolean loading;
    private boolean hasMore = true;

    private String category;
    private String keyword;

    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visible = layoutManager.getChildCount();
            int total = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            LogUtil.DefalutLog("visible:"+visible+"-total:"+total+"-firstVisibleItem:"+firstVisibleItem);
            if (!loading && hasMore) {
                if ((visible + firstVisibleItem) >= total) {
//                    loadAD();
//                    RequestAsyncTask();
                    LogUtil.DefalutLog("should load data");
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                    if(player != null){
                        player.stop();
                    }
                    removeVideoView(player_view);
                    removeVideoView(mWebView);
                    View view = snapHelper.findSnapView(layoutManager);
                    RcXVideoDetailListItemViewHolder viewHolder = (RcXVideoDetailListItemViewHolder)recyclerView.getChildViewHolder(view);
                    if(viewHolder != null){
                        mAVObject = viewHolder.mAVObject;
                        mProgressbar = viewHolder.progress_bar;
                        cover_img = viewHolder.cover_img;
                        if(mAVObject != null){
                            Url = mAVObject.getString(AVOUtil.XVideo.source_url);
                            type = mAVObject.getString(AVOUtil.XVideo.type);
                            media_url = mAVObject.getString(AVOUtil.XVideo.media_url);
                            LogUtil.DefalutLog("Url:"+Url);
                            LogUtil.DefalutLog("type:"+type);
                            LogUtil.DefalutLog("media_url:"+media_url);
                            setData(viewHolder);
                        }
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.xvideo_detail_activity);
        setStatusbarColor(R.color.black);
        ButterKnife.bind(this);
        initData();
        initWebView();
        addListener();
    }

    private void initData() {
        try {
            category = getIntent().getStringExtra(KeyUtil.Category);
            keyword = getIntent().getStringExtra(KeyUtil.KeyWord);
            mAVObjects = new ArrayList<AVObject>();
            Setings.MPlayerPause();
            List<AVObject> list = (List<AVObject>) Setings.dataMap.get(KeyUtil.DataMapKey);
            if(list != null){
                mAVObjects.addAll(list);
                Setings.dataMap.clear();
                position = getIntent().getIntExtra(KeyUtil.PositionKey,0);
            }else {
                String serializedStr = getIntent().getStringExtra(KeyUtil.AVObjectKey);
                if(TextUtils.isEmpty(serializedStr)){
                    finish();
                }
                AVObject mAVObject = AVObject.parseAVObject(serializedStr);
                if (mAVObject == null) {
                    finish();
                }
                mAVObjects.add(mAVObject);
            }
            player_view = (PlayerView)LayoutInflater.from(this).inflate(R.layout.xvideo_detail_list_playerview,null);
            player = ExoPlayerFactory.newSimpleInstance(this);
            player.addListener(this);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player_view.setPlayer(player);
            snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(listview);
            videoAdapter = new RcXVideoDetailListAdapter();
            layoutManager = new LinearLayoutManager(XVideoDetailActivity.this, LinearLayoutManager.VERTICAL, false);
            videoAdapter.setItems(mAVObjects);
            listview.setLayoutManager(layoutManager);
            listview.setAdapter(videoAdapter);
            listview.scrollToPosition(position);
            mFSVADModel = new FullScreenVideoADModel(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addListener() {
        listview.addOnScrollListener(mListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onScrollStateChanged(listview,RecyclerView.SCROLL_STATE_IDLE);
            }
        },350);
    }

    private void setData(RcXVideoDetailListItemViewHolder viewHolder) {
        if(mAVObject.get(KeyUtil.ADKey) != null || mAVObject.get(KeyUtil.TXADView) != null){
            mFSVADModel.setAd_layout(viewHolder.player_view_layout,viewHolder.title,viewHolder.btn_detail);
            mFSVADModel.showAd();
            hideCoverImg();
            hideProgressbar();
            return;
        }
        if (!TextUtils.isEmpty(type)) {
            if ("url_api".equals(type)) {
                viewHolder.player_view_layout.addView(player_view);
                parseVideoUrl();
            } else if ("url_media".equals(type)) {
                viewHolder.player_view_layout.addView(player_view);
                exoplaer(media_url);
            } else if ("url_intercept".equals(type) ) {
                viewHolder.player_view_layout.addView(player_view);
                isIntercept = true;
                mWebView.loadUrl(Url);
            } else {
                viewHolder.player_view_layout.addView(mWebView);
                viewHolder.title.setText("");
                isIntercept = false;
                mWebView.loadUrl(Url);
            }
        } else {
            viewHolder.player_view_layout.addView(player_view);
            parseVideoUrl();
        }
    }

    private void initWebView(){
        SharedPreferences sp = Setings.getSharedPreferences(this);
        String intercepts = sp.getString(KeyUtil.InterceptUrls, "");
        LogUtil.DefalutLog("InterceptUrls:" + intercepts);
        if (!TextUtils.isEmpty(intercepts)) {
            if (intercepts.contains(",")) {
                interceptUrls = intercepts.split(",");
            }
        }

        mWebView = new WebView(this);
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
                hideProgressbar();
                hideCoverImg();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.performClick();
                    }
                },300);

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
                DialogUtil.OnReceivedSslError(XVideoDetailActivity.this,handler);
            }
        });
    }

    private void interceptUrl(String url) {
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
                XVideoDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exoplaer(url);
                    }
                });
            }
        }
    }

    private void parseVideoUrl() {
        LogUtil.DefalutLog("parseVideoUrl:"+Url);
        if(TextUtils.isEmpty(Url)){
            return;
        }
        showProgressbar();
        long timestamp = System.currentTimeMillis();
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
            }

            @Override
            public void onFinished() {
                hideProgressbar();
            }
        });
    }

    private void exoplaer(String media_url) {
        hideProgressbar();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "LanguageHelper"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(media_url));
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        LogUtil.DefalutLog("ACTION_setPlayWhenReady");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        LogUtil.DefalutLog("onPlayerStateChanged:" + playbackState + "-:playWhenReady:" + playWhenReady);
        if (playWhenReady && playbackState == Player.STATE_READY) {
            hideCoverImg();
        }
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

    public void releasePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
            player = null;
        }
    }

    @OnClick(R.id.back_btn)
    public void onClick() {
        onBackPressed();
    }

    private void hideCoverImg(){
        if(cover_img != null){
            cover_img.setVisibility(View.GONE);
        }
    }

    private void removeVideoView(View videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }
        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
        }
    }

}
