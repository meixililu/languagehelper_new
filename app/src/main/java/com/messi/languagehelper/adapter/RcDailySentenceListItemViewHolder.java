package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.PracticeActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.EveryDaySentence;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.MyPlayerListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.Setings;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcDailySentenceListItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView english_txt;
    private final TextView chinese_txt;
    private final SimpleDraweeView daily_sentence_list_item_img;
    private final ImageView play_img;
    private final FrameLayout daily_sentence_list_item_cover;
    private Context context;
    private List<EveryDaySentence> beans;
    private FragmentProgressbarListener mProgressbarListener;
    private RcDailySentenceListAdapter mAdapter;

    public RcDailySentenceListItemViewHolder(View convertView, List<EveryDaySentence> beans,
                                             FragmentProgressbarListener mProgressbarListener,
                                             RcDailySentenceListAdapter mAdapter) {
        super(convertView);
        this.context = convertView.getContext();
        this.beans = beans;
        this.mAdapter = mAdapter;
        this.mProgressbarListener = mProgressbarListener;
        daily_sentence_list_item_cover = convertView.findViewById(R.id.daily_sentence_list_item_cover);
        daily_sentence_list_item_img = convertView.findViewById(R.id.daily_sentence_list_item_img);
        play_img = convertView.findViewById(R.id.play_img);
        english_txt = convertView.findViewById(R.id.english_txt);
        chinese_txt = convertView.findViewById(R.id.chinese_txt);
        MyPlayer.getInstance(context).setListener(new MyPlayerListener() {
            @Override
            public void onStart() {
                if (mProgressbarListener != null) {
                    mProgressbarListener.hideProgressbar();
                }
            }
            @Override
            public void onFinish() {
                LogUtil.DefalutLog("----onfinish----");
                resetData();
            }
        });
    }

    public void render(final EveryDaySentence mBean) {
        chinese_txt.setText(mBean.getNote());
        english_txt.setText(mBean.getContent());
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
                playItem(mBean);
            }
        });
    }

    private void playItem(EveryDaySentence mBean) {
        if (mBean.isPlaying()) {
            resetData();
            MyPlayer.getInstance(context).stop();
        } else {
            if (mProgressbarListener != null) {
                mProgressbarListener.showProgressbar();
            }
            resetData();
            mBean.setPlaying(true);
            mAdapter.notifyDataSetChanged();
            MyPlayer.getInstance(context).start(mBean.getContent(),mBean.getTts());
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

    private void toViewImgActivity(EveryDaySentence mBean) {
        Record sampleBean = new Record(mBean.getContent(), mBean.getNote());
        sampleBean.setPh_en_mp3(mBean.getTts());
        BoxHelper.insert(sampleBean);
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(KeyUtil.IsNeedDelete, true);
        Setings.dataMap.put(KeyUtil.DialogBeanKey, sampleBean);
        context.startActivity(intent);
    }

}
