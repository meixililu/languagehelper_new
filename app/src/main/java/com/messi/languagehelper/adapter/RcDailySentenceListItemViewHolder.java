package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.PracticeActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by luli on 10/23/16.
 */

public class RcDailySentenceListItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView english_txt;
    private final TextView chinese_txt;
    private final SimpleDraweeView daily_sentence_list_item_img;
    private final ImageView play_img;
    private final FrameLayout daily_sentence_list_item_cover;
    private Activity context;
    private List<EveryDaySentence> beans;
    private MediaPlayer mPlayer;
    private FragmentProgressbarListener mProgressbarListener;
    private RcDailySentenceListAdapter mAdapter;
    private boolean isLoading;

    public RcDailySentenceListItemViewHolder(View convertView, Activity context, List<EveryDaySentence> beans,
                                             MediaPlayer mPlayer, FragmentProgressbarListener mProgressbarListener,
                                             RcDailySentenceListAdapter mAdapter) {
        super(convertView);
        this.context = context;
        this.beans = beans;
        this.mPlayer = mPlayer;
        this.mAdapter = mAdapter;
        this.mProgressbarListener = mProgressbarListener;
        daily_sentence_list_item_cover = (FrameLayout) convertView.findViewById(R.id.daily_sentence_list_item_cover);
        daily_sentence_list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.daily_sentence_list_item_img);
        play_img = (ImageView) convertView.findViewById(R.id.play_img);
        english_txt = (TextView) convertView.findViewById(R.id.english_txt);
        chinese_txt = (TextView) convertView.findViewById(R.id.chinese_txt);
    }

    public void render(final EveryDaySentence mBean) {
        chinese_txt.setText(mBean.getNote());
        TextHandlerUtil.handlerText(context, mProgressbarListener, english_txt, mBean.getContent());

        daily_sentence_list_item_img.setImageURI(Uri.parse(mBean.getPicture2()));

        daily_sentence_list_item_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toViewImgActivity(mBean);
            }
        });
        if (mBean.isPlaying()) {
            play_img.setBackgroundResource(R.drawable.ic_stop_circle_outline_white_48dp);
        } else {
            play_img.setBackgroundResource(R.drawable.ic_play_circle_outline_white_48dp);
        }
        play_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    if (mPlayer.isPlaying() && mBean.isPlaying()) {
                        mPlayer.stop();
                        resetData();
                    } else if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                        resetData();
                        prepareData(mBean);
                    } else {
                        prepareData(mBean);
                    }
                }

            }
        });
    }

    private void prepareData(EveryDaySentence mBean) {
        int pos = mBean.getTts().lastIndexOf(SDCardUtil.Delimiter) + 1;
        String fileName = mBean.getTts().substring(pos, mBean.getTts().length());
        String fileFullName = SDCardUtil.getDownloadPath(SDCardUtil.DailySentencePath) + fileName;
        LogUtil.DefalutLog("fileName:" + fileName + "---fileFullName:" + fileFullName);
        mBean.setPlaying(true);
        mAdapter.notifyDataSetChanged();
        if (SDCardUtil.isFileExist(fileFullName)) {
            playMp3(fileFullName);
            LogUtil.DefalutLog("FileExist");
        } else {
            LogUtil.DefalutLog("FileNotExist");
            downloadFile(context, mBean.getTts(), fileName);
        }
    }

    private void resetData() {
        for (EveryDaySentence mBean : beans) {
            if (mBean.isPlaying()) {
                mBean.setPlaying(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void playMp3(String uriPath) {
        try {
            mPlayer.reset();
            Uri uri = Uri.parse(uriPath);
            mPlayer.setDataSource(context, uri);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    resetData();
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(final Context mContext, String url, final String suffix) {
        LogUtil.DefalutLog("---url:" + url);
        if (mProgressbarListener != null) {
            mProgressbarListener.showProgressbar();
        }
        isLoading = true;
        LanguagehelperHttpClient.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFinish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DownLoadUtil.saveFile(context, SDCardUtil.DailySentencePath, suffix, response.body().bytes());
                String fileFullName = SDCardUtil.getDownloadPath(SDCardUtil.DailySentencePath) + suffix;
                onFinish();
                playMp3(fileFullName);
            }

        });
    }

    private void onFinish() {
        isLoading = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressbarListener != null) {
                    mProgressbarListener.hideProgressbar();
                }
            }
        });
    }

    private void toViewImgActivity(EveryDaySentence mBean) {
        record sampleBean = new record(mBean.getContent(), mBean.getNote());
        DataBaseUtil.getInstance().insert(sampleBean);
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(KeyUtil.IsNeedDelete, true);
        WXEntryActivity.dataMap.put(KeyUtil.DialogBeanKey, sampleBean);
        context.startActivity(intent);
    }

}
