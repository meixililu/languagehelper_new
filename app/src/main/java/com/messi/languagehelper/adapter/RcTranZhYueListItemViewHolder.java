package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.messi.languagehelper.PracticeYYSActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.TranResultZhYue;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcTranZhYueListItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView record_question;
    public TextView record_answer;
    public FrameLayout record_answer_cover;
    public FrameLayout record_to_practice;
    public FrameLayout record_question_cover;
    public FrameLayout voice_play_layout;
    public FrameLayout delete_btn;
    public FrameLayout collected_btn;
    public FrameLayout weixi_btn;
    public ImageView unread_dot_answer;
    public ImageView unread_dot_question;
    public CheckBox collected_cb;

    private List<TranResultZhYue> beans;
    private RcTranZhYueListAdapter mAdapter;

    public RcTranZhYueListItemViewHolder(View convertView,
                                         List<TranResultZhYue> mBeans,
                                         RcTranZhYueListAdapter mAdapter) {
        super(convertView);
        this.context = convertView.getContext();
        this.beans = mBeans;
        this.mAdapter = mAdapter;
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        record_to_practice = (FrameLayout) convertView.findViewById(R.id.record_to_practice);
        voice_play_layout = (FrameLayout) convertView.findViewById(R.id.voice_play_layout);
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
        unread_dot_answer = (ImageView) convertView.findViewById(R.id.unread_dot_answer);
        unread_dot_question = (ImageView) convertView.findViewById(R.id.unread_dot_question);
        collected_cb = (CheckBox) convertView.findViewById(R.id.collected_cb);
        delete_btn = (FrameLayout) convertView.findViewById(R.id.delete_btn);
        collected_btn = (FrameLayout) convertView.findViewById(R.id.collected_btn);
        weixi_btn = (FrameLayout) convertView.findViewById(R.id.weixi_btn);
    }

    public void render(final TranResultZhYue mBean) {
        MyOnClickListener mMyOnClickListener = new MyOnClickListener(mBean,true);
        MyOnClickListener mQuestionOnClickListener = new MyOnClickListener(mBean,false);
        if(mBean.getIscollected().equals("0")){
            collected_cb.setChecked(false);
        }else{
            collected_cb.setChecked(true);
        }
        if (getLayoutPosition() == 0) {
            if (!PlayUtil.getSP().getBoolean(KeyUtil.IsShowAnswerUnread, false)) {
                unread_dot_answer.setVisibility(View.VISIBLE);
            } else {
                unread_dot_answer.setVisibility(View.GONE);
            }
        } else {
            unread_dot_answer.setVisibility(View.GONE);
            unread_dot_question.setVisibility(View.GONE);
        }
        record_question.setText(mBean.getChinese());
        record_answer.setText(mBean.getEnglish());

        record_question_cover.setOnClickListener(mQuestionOnClickListener);
        voice_play_layout.setOnClickListener(mMyOnClickListener);
        record_answer_cover.setOnClickListener(mMyOnClickListener);

        record_answer_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context,mBean.getEnglish());
                return true;
            }
        });
        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context,mBean.getChinese());
                return true;
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntity(getLayoutPosition());
            }
        });
        weixi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mBean.getEnglish();
                Setings.share(context,text);
                AVAnalytics.onEvent(context, "tab1_share_btn");
            }
        });
        collected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCollectedStatus(mBean);
                AVAnalytics.onEvent(context, "tab1_tran_collected");
            }
        });
        record_to_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeYYSActivity.class);
                Setings.dataMap.put(KeyUtil.DialogBeanKey, mBean);
                context.startActivity(intent);
                AVAnalytics.onEvent(context, "tab1_tran_to_practicepg");
            }
        });
    }

    public void deleteEntity(int position) {
        try{
            TranResultZhYue mBean = beans.remove(position);
            mAdapter.notifyItemRemoved(position);
            BoxHelper.remove(mBean);
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.dele_success));
            SDCardUtil.deleteContent(mBean.getChinese(),mBean.getEnglish());
            AVAnalytics.onEvent(context, "tab1_tran_delete");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateCollectedStatus(TranResultZhYue mBean){
        if (mBean.getIscollected().equals("0")) {
            mBean.setIscollected("1");
            ToastUtil.diaplayMesShort(context,"已收藏");
        } else {
            mBean.setIscollected("0");
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_cancle));
        }
        mAdapter.notifyDataSetChanged();
        BoxHelper.update(mBean);
    }

    public class MyOnClickListener implements View.OnClickListener {

        private TranResultZhYue mBean;
        private boolean isPlayResult;
        boolean isNotify = false;

        private MyOnClickListener(TranResultZhYue bean, boolean isPlayResult){
            this.mBean = bean;
            this.isPlayResult = isPlayResult;
        }
        @Override
        public void onClick(final View v) {
            String speakContent = "";
            String speaker = "";
            if (isPlayResult) {
                if(mBean.getBackup3().equals(Setings.yue)){
                    speaker = XFUtil.SpeakerHk;
                }else{
                    speaker = XFUtil.SpeakerZh;
                }
                if (!PlayUtil.getSP().getBoolean(KeyUtil.IsShowAnswerUnread, false)) {
                    mAdapter.notifyDataSetChanged();
                }
                Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowAnswerUnread, true);
                if (!TextUtils.isEmpty(mBean.getBackup1())) {
                    speakContent = mBean.getBackup1();
                } else {
                    speakContent = mBean.getEnglish();
                }
                MyPlayer.getInstance(context).start(speakContent);
            } else {
                if(mBean.getBackup3().equals(Setings.yue)){
                    speaker = XFUtil.SpeakerZh;
                }else{
                    speaker = XFUtil.SpeakerHk;
                }
                Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowQuestionUnread, true);
                speakContent = mBean.getChinese();
                MyPlayer.getInstance(context).startWithSpeaker(speakContent, speaker);
            }
        }
    }
}
