package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.messi.languagehelper.PracticeActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by luli on 10/23/16.
 */

public class RcTranslateLiatItemViewHolder extends RecyclerView.ViewHolder {

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

    private List<Record> beans;
    private RcTranslateListAdapter mAdapter;

    public RcTranslateLiatItemViewHolder(View convertView,
                                         List<Record> mBeans,
                                         RcTranslateListAdapter mAdapter) {
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

    public void render(final Record mBean) {
        if (mBean == null) return;
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

        String pats1 = "英.*\\[";
        Pattern pattern1 = Pattern.compile(pats1);
        if(pattern1.matcher(mBean.getEnglish()).find()){
            voice_play_layout.setOnClickListener(mQuestionOnClickListener);
        } else {
            voice_play_layout.setOnClickListener(mMyOnClickListener);
        }

        record_question_cover.setOnClickListener(mQuestionOnClickListener);
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
                String text = "";
                String pats1 = "英.*\\[";
                Pattern pattern1 = Pattern.compile(pats1);
                if(pattern1.matcher(mBean.getEnglish()).find()){
                    text = mBean.getChinese()+"\n"+mBean.getEnglish();
                }else{
                    text = mBean.getEnglish();
                }
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
                Intent intent = new Intent(context,PracticeActivity.class);
                Setings.dataMap.put(KeyUtil.DialogBeanKey, mBean);
                context.startActivity(intent);
                AVAnalytics.onEvent(context, "tab1_tran_to_practicepg");
            }
        });
    }

    public void deleteEntity(int position) {
        try{
            Record mBean = beans.remove(position);
            mAdapter.notifyItemRemoved(position);
            BoxHelper.remove(mBean);
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.dele_success));
            SDCardUtil.deleteContent(mBean.getEnglish(),mBean.getChinese());
            AVAnalytics.onEvent(context, "tab1_tran_delete");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateCollectedStatus(Record mBean){
        if (mBean.getIscollected().equals("0")) {
            mBean.setIscollected("1");
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_success));
            addToNewword(mBean);
        } else {
            mBean.setIscollected("0");
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_cancle));
        }
        mAdapter.notifyDataSetChanged();
        BoxHelper.update(mBean);
    }

    private void addToNewword(Record mBean){
        WordDetailListItem myNewWord = new WordDetailListItem();
        myNewWord.setName(mBean.getChinese());
        myNewWord.setDesc(mBean.getEnglish());
        String pats1 = "英.*\\[";
        Pattern pattern1 = Pattern.compile(pats1);
        String pats2 = "美.*\\[";
        Pattern pattern2 = Pattern.compile(pats2);
        if(pattern1.matcher(mBean.getEnglish()).find() || pattern2.matcher(mBean.getEnglish()).find()){
            String[] texts = mBean.getEnglish().split("\n");
            if (texts != null && texts.length > 0) {
                String symbol = texts[0];
                if(symbol.contains("英") || symbol.contains("美")){
                    String des = mBean.getEnglish().replace(symbol,"").trim();
                    myNewWord.setSymbol(symbol);
                    myNewWord.setDesc(des);
                }
            }
        }
        myNewWord.setSound(mBean.getPh_en_mp3());
        myNewWord.setNew_words("1");
        myNewWord.setType("search");
        BoxHelper.saveSearchResultToNewWord(myNewWord);
    }

    public class MyOnClickListener implements View.OnClickListener {

        private Record mBean;
        private boolean isPlayResult;

        private MyOnClickListener(Record bean, boolean isPlayResult){
            this.mBean = bean;
            this.isPlayResult = isPlayResult;
        }

        @Override
        public void onClick(final View v) {
            String speakContent = "";
            if (isPlayResult) {
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
                Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsShowQuestionUnread, true);
                speakContent = mBean.getChinese();
                MyPlayer.getInstance(context).start(speakContent, mBean.getPh_en_mp3());
            }
        }
    }
}
