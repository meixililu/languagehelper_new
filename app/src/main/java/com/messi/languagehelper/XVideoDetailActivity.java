package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.messi.languagehelper.ViewModel.FullScreenVideoADModel;
import com.messi.languagehelper.adapter.RcXVideoDetailListAdapter;
import com.messi.languagehelper.adapter.RcXVideoDetailListItemViewHolder;
import com.messi.languagehelper.bean.PVideoResult;
import com.messi.languagehelper.bean.ToutiaoVideoBean;
import com.messi.languagehelper.bean.ToutiaoWebRootBean;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.httpservice.RetrofitApiService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DialogUtil;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SignUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;
import cn.leancloud.json.JSON;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XVideoDetailActivity extends BaseActivity implements Player.EventListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;

    private SimpleExoPlayer player;
    private List<AVObject> mAVObjects;
    private int position;
    private SimpleDraweeView cover_img;

    private RcXVideoDetailListAdapter videoAdapter;
    private PlayerView player_view;
    private WebView mWebView;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private String[] interceptUrls;
    private boolean isIntercept;
    private boolean isWVPSuccess;
    private boolean isAD;
    private FullScreenVideoADModel mFSVADModel;
    private boolean loading;
    private boolean hasMore = true;

    private String category;
    private String keyword;

    private RcXVideoDetailListItemViewHolder viewHolder;

    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visible = layoutManager.getChildCount();
            int total = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            if (!loading && hasMore) {
                if ((visible + firstVisibleItem) >= total) {
                    LogUtil.DefalutLog("RecyclerView:onScrolled");
                    RequestAsyncTask();
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                    if(player != null){
                        player.setPlayWhenReady(false);
                    }
                    View view = snapHelper.findSnapView(layoutManager);
                    viewHolder = (RcXVideoDetailListItemViewHolder)recyclerView.getChildViewHolder(view);
                    if(viewHolder != null){
                        mProgressbar = viewHolder.progress_bar;
                        cover_img = viewHolder.cover_img;
                        if(viewHolder.mAVObject != null){
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
        transparentStatusbar();
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
            IPlayerUtil.pauseAudioPlayer(this);
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
                RequestAsyncTask();
            }
            player_view = (PlayerView)LayoutInflater.from(this).inflate(R.layout.xvideo_detail_list_playerview,null);
            player = new SimpleExoPlayer.Builder(this).build();
            player.addListener(this);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player_view.setPlayer(player);

            snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(listview);
            videoAdapter = new RcXVideoDetailListAdapter(player,player_view,mWebView);
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
        ViewUtil.removeParentView(player_view);
        if(viewHolder.mAVObject.get(KeyUtil.ADKey) != null || viewHolder.mAVObject.get(KeyUtil.TXADView) != null ||
                viewHolder.mAVObject.get(KeyUtil.VideoAD) != null){
            mFSVADModel.setAd_layout(viewHolder.mAVObject,viewHolder.player_view_layout,viewHolder.title,viewHolder.btn_detail);
            mFSVADModel.showAd();
            hideCoverImg();
            hideProgressbar();
            return;
        }
        LogUtil.DefalutLog("type:"+viewHolder.type);
        viewHolder.player_view_layout.addView(player_view);
        if(!TextUtils.isEmpty(viewHolder.mAVObject.getString(KeyUtil.VideoParseUrl))){
            exoplaer(viewHolder.mAVObject.getString(KeyUtil.VideoParseUrl));
            return;
        }
        if (!TextUtils.isEmpty(viewHolder.type)) {
            String vid = viewHolder.mAVObject.getString(AVOUtil.XVideo.vid);
            if (!TextUtils.isEmpty(vid)) {
                parseToutiaoVid(vid,viewHolder.Url);
            } else if ("url_api".equals(viewHolder.type)) {
                parseVideoUrl(viewHolder.Url);
            } else if ("url_media".equals(viewHolder.type)) {
                exoplaer(viewHolder.media_url);
            } else if ("url_intercept".equals(viewHolder.type) ) {
                isIntercept = true;
                if (viewHolder.Url != null && viewHolder.Url.contains("bilibili")) {
                    mWebView.getSettings().setUserAgentString(Setings.Hearder);
                }else {
                    mWebView.getSettings().setUserAgentString("");
                }
                mWebView.loadUrl(viewHolder.Url);
            } else {
                parseVideoUrl(viewHolder.Url);
            }
        } else {
            parseVideoUrl(viewHolder.Url);
        }
    }

    private void initWebView(){
        SharedPreferences sp = Setings.getSharedPreferences(this);
        String intercepts = sp.getString(KeyUtil.InterceptUrls, "");
        LogUtil.DefalutLog("InterceptUrls:" + intercepts);
        if (!TextUtils.isEmpty(intercepts)) {
            if (intercepts.contains(",")) {
                interceptUrls = intercepts.split(",");
            }else {
                interceptUrls = new String[]{intercepts};
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
                if (!isWVPSuccess && viewHolder != null) {
                    parseVideoUrl(viewHolder.Url);
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
                        if(viewHolder != null && viewHolder.mAVObject != null &&
                                "头条小视频".equals(viewHolder.mAVObject.getString(AVOUtil.XVideo.source_name))){
                            parseToutiaoWebApi(url);
                        }else {
                            exoplaer(url);
                        }
                    }
                });
            }
        }
    }

    private void parseToutiaoWebApi(String url){
        LogUtil.DefalutLog("parseToutiaoWebApi:"+url);
        LanguagehelperHttpClient.get(url,new UICallback(this){
            @Override
            public void onFailured() {
                LogUtil.DefalutLog("parseToutiaoWebApi-onFailured");
                if(viewHolder != null){
                    parseVideoUrl(viewHolder.Url);
                }
            }
            @Override
            public void onFinished() {
            }
            @Override
            public void onResponsed(String responseString) {
                String videoUrl = "";
                try {
                    if(!TextUtils.isEmpty(responseString)){
                        int start = responseString.indexOf("{");
                        int end = responseString.lastIndexOf("}");
                        if(start > 0 && end > 0){
                            String jstr = responseString.substring(start,end+1);
                            LogUtil.DefalutLog("jstr:"+jstr);
                            if(JsonParser.isJson(jstr)){
                                ToutiaoWebRootBean result = JSON.parseObject(jstr, ToutiaoWebRootBean.class);
                                if(result != null
                                        && result.getData() != null
                                        && result.getData().getVideo_list() != null
                                        && result.getData().getVideo_list().getVideo_1() != null){
                                    videoUrl = result.getData().getVideo_list().getVideo_1().getMain_url();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    videoUrl = "";
                    e.printStackTrace();
                } finally {
                    if(!TextUtils.isEmpty(videoUrl)){
                        exoplaer(videoUrl);
                    }else {
                        onFailured();
                    }
                }
            }
        });
    }

    private void parseVideoUrl(String url) {
        LogUtil.DefalutLog("parseVideoUrl:"+url);
        if(TextUtils.isEmpty(url)){
            return;
        }
        showProgressbar();
        String vid = "";
        if(viewHolder != null && viewHolder.mAVObject != null &&
                !TextUtils.isEmpty(viewHolder.mAVObject.getString(AVOUtil.XVideo.vid))){
            vid = viewHolder.mAVObject.getString(AVOUtil.XVideo.vid);
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String platform = SystemUtil.platform;
        String network = NetworkUtil.getNetworkType(this);
        String sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, url, platform, network);
        RetrofitApiService service = RetrofitApiService.getRetrofitApiService(Setings.PVideoApi,
                RetrofitApiService.class);
        Call<PVideoResult> call = service.getPVideoApi(url,network,platform,sign,timestamp,0, vid);
        call.enqueue(new Callback<PVideoResult>() {
                 @Override
                 public void onResponse(Call<PVideoResult> call, Response<PVideoResult> response) {
                     LogUtil.DefalutLog("onResponse:"+response+"---call:"+call);
                     if (response.isSuccessful()) {
                         PVideoResult mResult = response.body();
                         if (mResult != null && !TextUtils.isEmpty(mResult.getUrl())) {
                             if(viewHolder.mAVObject != null){
                                 viewHolder.mAVObject.put(KeyUtil.VideoParseUrl,mResult.getUrl());
                             }
                             exoplaer(mResult.getUrl());
                         }
                     }else {
                         hideProgressbar();
                         ToastUtil.diaplayMesShort(XVideoDetailActivity.this,"视频不见了，换一个吧");
                     }
                 }

                 @Override
                 public void onFailure(Call<PVideoResult> call, Throwable t) {
                     LogUtil.DefalutLog("onFailure:"+t.getMessage()+"---call:"+call.request().url());
                     hideProgressbar();
                 }
             }
        );
    }

    private void exoplaer(String media_url) {
        hideProgressbar();
        if(player == null){
            player = new SimpleExoPlayer.Builder(this).build();
        }
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(
                Setings.Hearder,
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
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

    private void RequestAsyncTask() {
        LogUtil.DefalutLog("should load data");
        int play_count = 0;
        if(mAVObjects != null && !mAVObjects.isEmpty()){
            play_count = mAVObjects.get(mAVObjects.size()-1).getNumber(AVOUtil.XVideo.play_count).intValue();
        }
        loading = true;
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XVideo.XVideo);
        LogUtil.DefalutLog("category:"+category);
        if(!TextUtils.isEmpty(category)){
            query.whereEqualTo(AVOUtil.XVideo.category,category);
        }
        if(!TextUtils.isEmpty(keyword)){
            final AVQuery<AVObject> priorityQuery = new AVQuery<>(AVOUtil.XVideo.XVideo);
            priorityQuery.whereContains(AVOUtil.XVideo.title, keyword);

            final AVQuery<AVObject> statusQuery = new AVQuery<>(AVOUtil.XVideo.XVideo);
            statusQuery.whereContains(AVOUtil.XVideo.tag, keyword);

            query = AVQuery.or(Arrays.asList(priorityQuery, statusQuery));
        }
        query.whereLessThan(AVOUtil.XVideo.play_count,play_count);
        query.orderByDescending(AVOUtil.XVideo.play_count);
        query.limit(Setings.page_size);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
            loading = false;
            if(list != null){
                if(list.size() == 0){
                    hasMore = false;
                }else {
                    mAVObjects.addAll(list);
                    loadAD();
                    if(list.size() < Setings.page_size){
                        hasMore = false;
                    }else {
                        hasMore = true;
                    }
                }
            }else{
                ToastUtil.diaplayMesShort(XVideoDetailActivity.this, "加载失败，下拉可刷新");
            }
            }
        }));
    }

    private void loadAD(){
        if(mFSVADModel != null){
            LogUtil.DefalutLog("should load ad");
            mFSVADModel.setAVObjects(mAVObjects);
            mFSVADModel.setVideoAdapter(videoAdapter);
            mFSVADModel.justLoadData();
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

    private void parseToutiaoVid(String vid,String backUrl){
        LogUtil.DefalutLog("vid:"+vid);
        String videoUrl = "";
        try{
//            String pattern = "\"vid\":\"(\\w*)\"";
            String randint = StringUtils.getRandomString(16);
            String videoid = "/video/urls/v/1/toutiao/mp4/"+vid+"?r="+randint;
            CRC32 crc32 = new CRC32();
            crc32.update(videoid.getBytes());
            long checksum = crc32.getValue();
            videoUrl = "http://i.snssdk.com" + videoid + "&s=" + checksum;
            LogUtil.DefalutLog("videoid:"+videoid+"-videoUrl:"+videoUrl);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            parseToutiaoApi(videoUrl,backUrl);
        }
    }

    private void parseToutiaoApi(String url,String backUrl){
        LogUtil.DefalutLog("parseToutiaoApi");
        LanguagehelperHttpClient.get(url,new UICallback(this){
            @Override
            public void onFailured() {
                parseVideoUrl(backUrl);
            }
            @Override
            public void onFinished() {
            }
            @Override
            public void onResponsed(String responseStr) {
//                LogUtil.DefalutLog("parseToutiaoApi-responseStr:"+responseStr);
                String videoUrl = "";
                try{
                    ToutiaoVideoBean toutiaoVideo = JSON.parseObject(responseStr,ToutiaoVideoBean.class);
                    if(toutiaoVideo.getCode() == 0){
                        videoUrl = new String(
                                Base64.decode(toutiaoVideo.getData().getVideo_list().
                                        getVideo_1().getMain_url().getBytes(), Base64.DEFAULT));
                    }
                }catch (Exception e){
                    onFailured();
                    e.printStackTrace();
                }finally {
                    if(!TextUtils.isEmpty(videoUrl)){
                        exoplaer(videoUrl);
                    }else {
                        onFailured();
                    }
                }
            }
        });
    }

}
