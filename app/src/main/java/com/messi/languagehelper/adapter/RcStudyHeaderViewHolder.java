package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.DailySentenceAndEssayActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.ReadingCategory;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TimeUtil;

import java.util.List;

import static com.messi.languagehelper.StudyFragment.getTabItem;


/**
 * Created by luli on 10/23/16.
 */

public class RcStudyHeaderViewHolder extends RecyclerView.ViewHolder {
    
    private FrameLayout study_daily_sentence;
    private TextView dailysentence_txt;
    private SimpleDraweeView daily_sentence_item_img;
    private ImageView play_img;
    private EveryDaySentence mEveryDaySentence;
    private MediaPlayer mPlayer;
    private String fileFullName;
    private boolean isInitMedia;
    private SharedPreferences mSharedPreferences;
    private Activity mContext;
    private FragmentProgressbarListener mProgressbarListener;
    private List<ReadingCategory> categories;

    public RcStudyHeaderViewHolder(View view,FragmentProgressbarListener mProgressbarListener, Activity mContext) {
        super(view);
        this.mProgressbarListener = mProgressbarListener;
        this.mContext = mContext;
        categories = getTabItem(mContext);
        mPlayer = new MediaPlayer();
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        study_daily_sentence = (FrameLayout)view.findViewById(R.id.study_daily_sentence);
        dailysentence_txt = (TextView)view.findViewById(R.id.dailysentence_txt);
        daily_sentence_item_img = (SimpleDraweeView)view.findViewById(R.id.daily_sentence_item_img);
        play_img = (ImageView)view.findViewById(R.id.play_img);
        setListener();
        getDailySentence();
        isLoadDailySentence();
    }
    
    private void setListener(){
        study_daily_sentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDailySentenceActivity();
                AVAnalytics.onEvent(mContext, "tab3_to_dailysentence");
            }
        });
        play_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEveryDaySentence != null){
                    int pos = mEveryDaySentence.getTts().lastIndexOf(SDCardUtil.Delimiter) + 1;
                    String fileName = mEveryDaySentence.getTts().substring(pos, mEveryDaySentence.getTts().length());
                    fileFullName = SDCardUtil.getDownloadPath(SDCardUtil.DailySentencePath) + fileName;
                    LogUtil.DefalutLog("fileName:"+fileName+"---fileFullName:"+fileFullName);
                    if(SDCardUtil.isFileExist(fileFullName)){
                        playMp3(fileFullName);
                        LogUtil.DefalutLog("FileExist");
                    }else{
                        LogUtil.DefalutLog("FileNotExist");
                        showProgressbar();
                        DownLoadUtil.downloadFile(mContext, mEveryDaySentence.getTts(), SDCardUtil.DailySentencePath, fileName, mHandler);
                    }
                }
                AVAnalytics.onEvent(mContext, "tab3_play_daily_sentence");
            }
        });
    }

    private void toDailySentenceActivity(){
        Intent intent = new Intent(mContext, DailySentenceAndEssayActivity.class);
        mContext.startActivity(intent);
    }

    private void getDailySentence(){
        List<EveryDaySentence> mList = DataBaseUtil.getInstance().getDailySentenceList(1);
        if(mList != null){
            if(mList.size() > 0){
                mEveryDaySentence = mList.get(0);
            }
        }
        setSentence();
        LogUtil.DefalutLog("StudyFragment-getDailySentence()");
    }

    private void isLoadDailySentence(){
        String todayStr = TimeUtil.getTimeDateLong(System.currentTimeMillis());
        long cid = NumberUtil.StringToLong(todayStr);
        boolean isExist = DataBaseUtil.getInstance().isExist(cid);
        if(!isExist){
            requestDailysentence();
        }
        LogUtil.DefalutLog("StudyFragment-isLoadDailySentence()");
    }

    private void requestDailysentence(){
        LogUtil.DefalutLog("StudyFragment-requestDailysentence()");
        LanguagehelperHttpClient.get(Settings.DailySentenceUrl, new UICallback(mContext){
            public void onResponsed(String responseString) {
                if(JsonParser.isJson(responseString)){
                    mEveryDaySentence = JsonParser.parseEveryDaySentence(responseString);
                    setSentence();
                }
            }
        });
    }

    private void setSentence(){
        LogUtil.DefalutLog("StudyFragment-setSentence()");
        if(mEveryDaySentence != null){
            dailysentence_txt.setText(mEveryDaySentence.getContent());
            daily_sentence_item_img.setImageURI(Uri.parse(mEveryDaySentence.getPicture2()));
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                hideProgressbar();
                playMp3(fileFullName);
            }else if(msg.what == 2){
                hideProgressbar();
            }
        }
    };

    private void playMp3(String uriPath){
        try {
            if(mEveryDaySentence != null && !isInitMedia){
                isInitMedia = true;
                play_img.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_48dp);
                Uri uri = Uri.parse(uriPath);
                mPlayer.setDataSource(mContext, uri);
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_img.setBackgroundResource(R.drawable.ic_play_circle_outline_white_48dp);
                    }
                });
                mPlayer.prepare();
                mPlayer.start();
            }else{
                if(mPlayer.isPlaying()){
                    play_img.setBackgroundResource(R.drawable.ic_play_circle_outline_white_48dp);
                    mPlayer.pause();
                }else{
                    play_img.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_48dp);
                    mPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressbar(){
        if(mProgressbarListener != null){
            mProgressbarListener.showProgressbar();
        }
    }

    public void hideProgressbar(){
        if(mProgressbarListener != null){
            mProgressbarListener.hideProgressbar();
        }
    }

}
