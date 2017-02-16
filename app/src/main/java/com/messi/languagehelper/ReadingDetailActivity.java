package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
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

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.widgets.Dialog;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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


    private AVObject mAVObject;
    private List<AVObject> mAVObjects;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private Thread mThread;
    private MyThread mMyThread;
    private int index;
    private MediaPlayer mPlayer;
    private String fileFullName;
    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composition_detail_activity);
        ButterKnife.bind(this);
        initData();
        initViews();
        setData();
        guide();
    }

    private void guide() {
        if (!mSharedPreferences.getBoolean(KeyUtil.isReadingDetailGuideShow, false)) {
            Dialog dialog = new Dialog(this, "友情提示", "点击英文单词即可查询词意");
            dialog.addAcceptButton("OK");
            dialog.show();
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.isReadingDetailGuideShow, true);
        }
    }

    private void initData() {
        mAVObjects = (List<AVObject>) BaseApplication.dataMap.get(KeyUtil.DataMapKey);
        index = getIntent().getIntExtra(KeyUtil.IndexKey, 0);
        mAVObject = mAVObjects.get(index);
        BaseApplication.dataMap.clear();
        if (mAVObject == null) {
            finish();
        }
    }

    private void initViews() {
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mMyThread = new MyThread(mHandler);
    }

    private void setData() {
        videoplayer.setVisibility(View.GONE);
        pimgview.setVisibility(View.GONE);
        toolbar_layout.setTitle(mAVObject.getString(AVOUtil.Reading.title));
        title.setText(mAVObject.getString(AVOUtil.Reading.title));
        scrollview.scrollTo(0, 0);
        TextHandlerUtil.handlerText(this, mProgressbar, content, mAVObject.getString(AVOUtil.Reading.content));
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.Reading.type)) &&
                mAVObject.getString(AVOUtil.Reading.type).equals("video")){
            videoplayer.setVisibility(View.VISIBLE);
            videoplayer.setUp(mAVObject.getString(AVOUtil.Reading.media_url),JCVideoPlayerStandard.SCREEN_LAYOUT_LIST,"");
            if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.Reading.img_url))) {
                Glide.with(this)
                        .load(mAVObject.getString(AVOUtil.Reading.img_url))
                        .into(videoplayer.thumbImageView);
            }
        }else if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.Reading.img_url))) {
            pimgview.setVisibility(View.VISIBLE);
            pimgview.setImageURI(Uri.parse(mAVObject.getString(AVOUtil.Reading.img_url)));
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
                if(xx_ad_layout.isShown()){
                    if(XFYSAD.isInScreen(ReadingDetailActivity.this,xx_ad_layout)){
                        LogUtil.DefalutLog("onScrollChange---isInScreen");
                        mXFYSAD.ExposureAD();
                    }
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideProgressbar();
            if (msg.what == 1) {
                playMp3(fileFullName);
            } else if (msg.what == 3) {
                mAVObject.put(AVOUtil.Reading.media_url, "");
                playContent();
                mAVObject.saveInBackground();
            } else {
                fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
            if (msg.what == MyThread.EVENT_PLAY_OVER) {
                fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
        }
    };

    private void playContent() {
        String type = mAVObject.getString(AVOUtil.Reading.type);
        if (type.equals("mp3") && !TextUtils.isEmpty(mAVObject.getString(AVOUtil.Reading.media_url))) {
            playByMp3();
        } else {
            playByPcm();
        }
    }

    private void playByMp3() {
        if (mAVObject != null) {
            fab.setImageResource(R.drawable.ic_stop_white_48dp);
            String downLoadUrl = mAVObject.getString(AVOUtil.Reading.media_url);
            int pos = downLoadUrl.lastIndexOf(SDCardUtil.Delimiter) + 1;
            String fileName = downLoadUrl.substring(pos, downLoadUrl.length());
            String rootUrl = SDCardUtil.ReadingPath +
                    mAVObject.getString(AVOUtil.Reading.objectId) + SDCardUtil.Delimiter;
            fileFullName = SDCardUtil.getDownloadPath(rootUrl) + fileName;
            LogUtil.DefalutLog("fileName:" + fileName + "---fileFullName:" + fileFullName);
            if (SDCardUtil.isFileExist(fileFullName)) {
                playMp3(fileFullName);
                LogUtil.DefalutLog("FileExist");
            } else {
                LogUtil.DefalutLog("FileNotExist");
                showProgressbar();
                DownLoadUtil.downloadFile(this, downLoadUrl, rootUrl, fileName, mHandler);
            }
        }
    }

    private void playMp3(String uriPath) {
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
                fab.setImageResource(R.drawable.ic_stop_white_48dp);
                Uri uri = Uri.parse(uriPath);
                mPlayer.setDataSource(this, uri);
                mPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    }
                });
                mPlayer.prepare();
                mPlayer.start();
            } else {
                if (mPlayer.isPlaying()) {
                    fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    mPlayer.pause();
                } else {
                    fab.setImageResource(R.drawable.ic_stop_white_48dp);
                    mPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playByPcm() {
        fab.setImageResource(R.drawable.ic_stop_white_48dp);
        String filepath = SDCardUtil.getDownloadPath(SDCardUtil.CompositionPath) +
                String.valueOf(mAVObject.getNumber(AVOUtil.Reading.item_id)) + ".pcm";
        if (!AudioTrackUtil.isFileExists(filepath)) {
            showProgressbar();
            mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
            XFUtil.showSpeechSynthesizer(this, mSharedPreferences, mSpeechSynthesizer,
                    mAVObject.getString(AVOUtil.Reading.content), XFUtil.SpeakerEn,
                    new SynthesizerListener() {
                        @Override
                        public void onSpeakResumed() {
                        }

                        @Override
                        public void onSpeakProgress(int arg0, int arg1, int arg2) {
                        }

                        @Override
                        public void onSpeakPaused() {
                        }

                        @Override
                        public void onSpeakBegin() {
                            hideProgressbar();
                        }

                        @Override
                        public void onCompleted(SpeechError arg0) {
                            hideProgressbar();
                            fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                        }

                        @Override
                        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                        }

                        @Override
                        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                        }
                    });
        } else {
            mMyThread.setDataUri(filepath);
            mThread = AudioTrackUtil.startMyThread(mMyThread);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.composition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_share:
                copyOrshare(0);
                break;
            case R.id.action_copy:
                copyOrshare(1);
                break;
        }
        return true;
    }

    private void copyOrshare(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(mAVObject.getString(AVOUtil.Reading.title));
        sb.append("\n");
        sb.append(mAVObject.getString(AVOUtil.Reading.content));
        if (i == 0) {
            Settings.share(this, sb.toString());
        } else {
            Settings.copy(this, sb.toString());
        }
    }

    @OnClick(R.id.play_btn)
    public void onClick() {
        if (!isPlaying()) {
            playContent();
        } else {
            fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }
    }

    private boolean isPlaying() {
        boolean isPlaying = false;
        if (mSpeechSynthesizer.isSpeaking()) {
            mSpeechSynthesizer.stopSpeaking();
            isPlaying = true;
        }
        if (mMyThread.isPlaying) {
            AudioTrackUtil.stopPlayPcm(mThread);
            isPlaying = true;
        }
        return isPlaying;
    }

    public View getView(final AVObject mObject) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.composition_list_item, null);
        FrameLayout layout_cover = (FrameLayout) convertView.findViewById(R.id.layout_cover);
        LinearLayout list_item_img_parent = (LinearLayout) convertView.findViewById(R.id.list_item_img_parent);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView source_name = (TextView) convertView.findViewById(R.id.source_name);
        TextView type_name = (TextView) convertView.findViewById(R.id.type_name);
        SimpleDraweeView list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);

        final NativeADDataRef mNativeADDataRef = (NativeADDataRef) mObject.get(KeyUtil.ADKey);
        if (mNativeADDataRef == null) {
            title.setText(mObject.getString(AVOUtil.Reading.title));
            source_name.setText(mObject.getString(AVOUtil.Reading.source_name));
            type_name.setText(mObject.getString(AVOUtil.Reading.type_name));
            String img_url = "";
            if (mObject.getString(AVOUtil.Reading.img_type).equals("url")) {
                img_url = mObject.getString(AVOUtil.Reading.img_url);
            } else {
                AVFile mAVFile = mObject.getAVFile(AVOUtil.Reading.img);
                img_url = mAVFile.getUrl();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioTrackUtil.stopPlayOnline(mSpeechSynthesizer);
        AudioTrackUtil.stopPlayPcm(mThread);
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
    }

}
