package com.messi.languagehelper;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVObject;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.messi.languagehelper.bean.TTParseBean;
import com.messi.languagehelper.bean.TTParseDataBean;
import com.messi.languagehelper.bean.TTparseVideoBean;
import com.messi.languagehelper.bean.ToutiaoVideoBean;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ToastUtil;
import com.ximalaya.ting.android.opensdk.util.DigestUtils;

import java.util.zip.CRC32;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

public class XVideoDetailActivity extends BaseActivity {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";


    @BindView(R.id.player_view)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;

    private SimpleExoPlayer player;
    private AVObject mAVObject;
    private String Url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransparentStatusbar();
        setContentView(R.layout.xvideo_detail_activity);
        ButterKnife.bind(this);
        initData();
        parseToutiaoHtml();
    }

    public void TransparentStatusbar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initData() {
        try {
            Setings.musicSrv.pause();
            String serializedStr = getIntent().getStringExtra(KeyUtil.AVObjectKey);
            mAVObject = AVObject.parseAVObject(serializedStr);
            if (mAVObject == null) {
                finish();
            }
            title.setText(mAVObject.getString(AVOUtil.XVideo.title));
            Url = "http://www.365yg.com/a"+mAVObject.getString(AVOUtil.XVideo.group_id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseToutiaoHtml(){
        LogUtil.DefalutLog(Url);
        LogUtil.DefalutLog("tempUrl:"+Url);
        LanguagehelperHttpClient.get(Url,new UICallback(this){
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
                    LogUtil.DefalutLog("videoUrl:"+videoUrl);
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
                    LogUtil.DefalutLog("videoUrl:"+videoUrl);
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
                    LogUtil.DefalutLog("TTParseApi:" + responseString);
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(XVideoDetailActivity.this,"视频不见了。。。");
            }

            @Override
            public void onFinished() {
                hideProgressbar();
            }
        });
    }

    private void exoplaer(String media_url) {
        simpleExoPlayerView.hideController();
        simpleExoPlayerView.setUseArtwork(true);
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayerView.setPlayer(player);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "LanguageHelper"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(media_url));
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
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
