package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CollectedData;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.databinding.ReadingMp3LrcDetailActivityBinding;
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.TimeUtil;
import com.messi.languagehelper.util.utilLrc;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.List;

import cn.leancloud.json.JSON;

public class ReadingDetailLrcActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private Reading mAVObject;
    private List<Reading> mAVObjects;
    private List<utilLrc.Statement> list;
    private int currentIndex;
    private utilLrc.Statement currentItem;
    private double nextTopTime;
    private Handler handler = new Handler();
    private StringBuilder sb;
    private SharedPreferences mSharedPreferences;
    private ReadingMp3LrcDetailActivityBinding binding;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setSeekbarAndText();
            handler.postDelayed(this,50);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReadingMp3LrcDetailActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        registerBroadcast();
        initData();
        downloadLrc();
        setData();
        guide();
    }

    private void initData() {
        mSharedPreferences = Setings.getSharedPreferences(this);
        sb = new StringBuilder();
        int index = getIntent().getIntExtra(KeyUtil.IndexKey, 0);
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

    private void setData(){
        setActionBarTitle(" ");
        binding.seekbar.setOnSeekBarChangeListener(this);
        binding.title.setText(mAVObject.getTitle());
        binding.playbtnLayout.setOnClickListener(view -> onPlayBtnClick());
        binding.recognizeKnow.setOnClickListener(view -> playNext());
        binding.recognizeUnknow.setOnClickListener(view -> addUnknow());
        binding.playPrevious.setOnClickListener(view -> playPrevious());
        binding.subtitle.setOnClickListener(view -> {
            if (binding.content.isShown()) {
                binding.content.setVisibility(View.GONE);
            } else {
                binding.content.setVisibility(View.VISIBLE);
            }
        });
        if(IPlayerUtil.MPlayerIsSameMp3(mAVObject)){
            if(IPlayerUtil.getPlayStatus() == 1) {
                binding.btnPlay.setSelected(true);
                handler.postDelayed(mRunnable,50);
                downloadLrc();
            }
            setSeekbarAndText();
        }
        if(TextUtils.isEmpty(mAVObject.getStatus())){
            mAVObject.setStatus("1");
            BoxHelper.update(mAVObject);
        }
        if (BoxHelper.isCollected(mAVObject.getObject_id())) {
            mAVObject.setIsCollected("1");
        } else {
            mAVObject.setIsCollected("");
        }
    }

    private void setSeekbarAndText() {
        if(IPlayerUtil.MPlayerIsSameMp3(mAVObject)){
            int currentPosition = IPlayerUtil.getCurrentPosition();
            int mDuration = IPlayerUtil.getDuration();
            if(mDuration > 0){
                binding.seekbar.setMax(mDuration);
                binding.timeDuration.setText(TimeUtil.getDuration(mDuration / 1000));
            }
            binding.seekbar.setProgress(currentPosition);
            binding.timeCurrent.setText(TimeUtil.getDuration(currentPosition / 1000));
            if (currentPosition > nextTopTime && nextTopTime > 0 && currentIndex > 0) {
                binding.btnPlay.setSelected(false);
                IPlayerUtil.MPlayerPause();
            } else {
                if (nextTopTime < 1 || currentIndex < 1) {
                    initCurrentPosition(currentPosition);
                }
            }
            setSubtitle();
        }
    }

    private void initCurrentPosition(int currentPosition){
        if(NullUtil.isNotEmpty(list) && IPlayerUtil.MPlayerIsPlaying()){
            for(int i=0;i<list.size();i++){
                if(currentPosition < list.get(i).getTime()){
                    if(i-1 >= 0){
                        currentIndex = i - 1;
                        currentItem = list.get(currentIndex);
                        nextTopTime = list.get(i).getTime();
                        LogUtil.DefalutLog("initCurrentPosition:"+currentPosition+"---nextTopTime:"+nextTopTime+"---currentIndex:"+currentIndex);
                        break;
                    }
                }
            }
        }
    }

    private void setSubtitle(){
        if (currentItem != null) {
            String showText = currentItem.getLyric();
            if (!showText.equals(binding.content.getText().toString().trim())) {
                TextHandlerUtil.handlerText(this, binding.content, showText+" ");
            }
        }
    }

    private void addUnknow(){
        if (currentItem != null) {
            if (!sb.toString().contains(currentItem.getLyric())) {
                sb.append(currentItem.getLyric());
                addUnknowText(currentItem,currentIndex);
            }
            if (!IPlayerUtil.MPlayerIsPlaying()) {
                playCurrentItem();
            }
        }
    }

    private void playNext(){
        if(currentItem != null && list.size() > (currentIndex + 1)){
            currentIndex += 1;
            playCurrentItem();
        }
    }

    private void playPrevious(){
        if(currentItem != null && currentIndex > 1){
            currentIndex -= 1;
            playCurrentItem();
        }
    }

    private void playCurrentItem(){
        if(currentItem != null && NullUtil.isNotEmpty(list) && list.size() >= (currentIndex + 1)){
            if (list.size() > currentIndex) {
                currentItem = list.get(currentIndex);
            }
            if (list.size() > (currentIndex + 1)) {
                nextTopTime = list.get(currentIndex+1).getTime();
            } else {
                nextTopTime = IPlayerUtil.getDuration();
            }
            IPlayerUtil.MPlayerSeekTo((int)currentItem.getTime());
            IPlayerUtil.MPlayerRestart();
        }
    }

    private void addUnknowText(utilLrc.Statement item,int index){
        if (item != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.reading_lrc_unknow_item, null, false);
            TextView content = view.findViewById(R.id.content);
            View playBtn = view.findViewById(R.id.play_item);
            playBtn.setBackgroundColor(getResources().getColor(ColorUtil.getRadomColor()));
            playBtn.setTag(index);
            TextHandlerUtil.handlerText(this, content, item.getLyric()+" ");
            playBtn.setOnClickListener(vi -> {
                currentIndex = (int)playBtn.getTag();
                playCurrentItem();
            });
            binding.unknowLayout.addView(view,0,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            binding.scrollview.scrollTo(0,0);
        }
    }

    private void downloadLrc(){
        if(mAVObject != null && !TextUtils.isEmpty(mAVObject.getLrc_url())){
            final String name = mAVObject.getObject_id() + ".lrc";
            final String lrcfilepath = SDCardUtil.getDownloadPath(SDCardUtil.lrcPath) + name;
            if(!SDCardUtil.isFileExist(lrcfilepath)){
                LogUtil.DefalutLog("downloadLrc:"+mAVObject.getLrc_url());
                LanguagehelperHttpClient.get(mAVObject.getLrc_url(),new BgCallback(){
                    @Override
                    public void onFailured() {
                    }

                    @Override
                    public void onResponsed(String responseString) {
                        DownLoadUtil.saveFile(SDCardUtil.lrcPath,name,responseString.getBytes());
                        try {
                            utilLrc mutilLrc = new utilLrc(lrcfilepath);
                            setList(mutilLrc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                try {
                    utilLrc mutilLrc = new utilLrc(lrcfilepath);
                    setList(mutilLrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setList(utilLrc mutilLrc){
        if (mutilLrc != null) {
            list = mutilLrc.getLrcList();
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
                setCollectStatus();
                setMenuIcon(item);
                updateData();
                break;
        }
        return true;
    }

    private void setCollectStatus(){
        if(TextUtils.isEmpty(mAVObject.getIsCollected())){
            mAVObject.setIsCollected("1");
        } else {
            mAVObject.setIsCollected("");
        }
    }

    private void updateData(){
        new Thread(() -> {
            if(mAVObject != null){
                if(!TextUtils.isEmpty(mAVObject.getIsCollected())){
                    CollectedData cdata = new CollectedData();
                    cdata.setObjectId(mAVObject.getObject_id());
                    cdata.setName(mAVObject.getTitle());
                    cdata.setType(AVOUtil.Reading.Reading);
                    cdata.setJson(JSON.toJSONString(mAVObject));
                    BoxHelper.insert(cdata);
                }else {
                    CollectedData cdata = new CollectedData();
                    cdata.setObjectId(mAVObject.getObject_id());
                    BoxHelper.remove(cdata);
                }
                LiveEventBus.get(KeyUtil.UpdateCollectedData).post("");
            }
        }).start();
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

    public void onPlayBtnClick() {
        nextTopTime = 0;
        XmPlayerManager.getInstance(this).pause();
        IPlayerUtil.initAndPlay(mAVObject);
    }

    @Override
    public void updateUI(String music_action) {
        if(IPlayerUtil.MPlayerIsSameMp3(mAVObject)){
            if(PlayerService.action_restart.equals(music_action)){
                binding.btnPlay.setSelected(false);
                handler.removeCallbacks(mRunnable);
            }else if (PlayerService.action_pause.equals(music_action)) {
                binding.btnPlay.setSelected(true);
                handler.postDelayed(mRunnable,50);
            }
        }
        if(PlayerService.action_loading.equals(music_action)){
            showProgressbar();
        }else if(PlayerService.action_finish_loading.equals(music_action)){
            hideProgressbar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        if(handler != null){
            handler.removeCallbacks(mRunnable);
            handler = null;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mRunnable);
        IPlayerUtil.MPlayerPause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        nextTopTime = 0;
        IPlayerUtil.MPlayerSeekTo(seekBar.getProgress());
        IPlayerUtil.MPlayerRestart();
        handler.postDelayed(mRunnable,50);
    }

    private void guide() {
        if (!mSharedPreferences.getBoolean(KeyUtil.isReadingDetailGuideShow1, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("");
            builder.setMessage("点击英文单词即可查询词意。");
            builder.setPositiveButton("确认", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.isReadingDetailGuideShow1, true);
        }
    }
}
