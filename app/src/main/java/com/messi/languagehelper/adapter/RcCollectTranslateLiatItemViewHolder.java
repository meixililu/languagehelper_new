package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcCollectTranslateLiatItemViewHolder extends RecyclerView.ViewHolder {

    public TextView record_question;
    public TextView record_answer;
    public FrameLayout record_answer_cover;
    public FrameLayout record_question_cover;
    public FrameLayout delete_btn;
    public FrameLayout weixi_btn;
    public ImageButton voice_play;
    public FrameLayout voice_play_layout;
    public ProgressBar play_content_btn_progressbar;
    private Context context;

    private List<WordDetailListItem> beans;
    private SharedPreferences mSharedPreferences;
    private RcCollectTranslateListAdapter mAdapter;

    public RcCollectTranslateLiatItemViewHolder(View convertView,
                                                List<WordDetailListItem> mBeans,
                                                SharedPreferences mSharedPreferences,
                                                RcCollectTranslateListAdapter mAdapter) {
        super(convertView);
        this.context = convertView.getContext();
        this.beans = mBeans;
        this.mAdapter = mAdapter;
        this.mSharedPreferences = mSharedPreferences;
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
        voice_play = (ImageButton) convertView.findViewById(R.id.voice_play);
        voice_play_layout = (FrameLayout) convertView.findViewById(R.id.voice_play_layout);
        delete_btn = (FrameLayout) convertView.findViewById(R.id.delete_btn);
        weixi_btn = (FrameLayout) convertView.findViewById(R.id.weixi_btn);
        play_content_btn_progressbar = (ProgressBar) convertView.findViewById(R.id.play_content_btn_progressbar);
    }

    public void render(final WordDetailListItem mBean) {
        AnimationDrawable animationDrawable = (AnimationDrawable) voice_play.getBackground();
        MyOnClickListener mMyOnClickListener = new MyOnClickListener(mBean, voice_play, play_content_btn_progressbar, true);
        MyOnClickListener mQuestionOnClickListener = new MyOnClickListener(mBean, voice_play, play_content_btn_progressbar, false);

        record_question.setText(mBean.getName());
        if (!TextUtils.isEmpty(mBean.getSymbol())) {
            record_answer.setText(mBean.getSymbol() + "\n" + mBean.getDesc());
        } else {
            record_answer.setText(mBean.getDesc());
        }
        record_question_cover.setOnClickListener(mQuestionOnClickListener);
        record_answer_cover.setOnClickListener(mMyOnClickListener);
        voice_play_layout.setOnClickListener(mQuestionOnClickListener);

        record_answer_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context, mBean.getName());
                return true;
            }
        });
        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context, mBean.getParaphrase());
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
                String text = mBean.getName() + "\n" + mBean.getDesc();
                Setings.share(context, text);
            }
        });
    }

    public void deleteEntity(int position) {
        try {
            WordDetailListItem mBean = beans.remove(position);
            mAdapter.notifyItemRemoved(position);
            BoxHelper.update(mBean,false);
            ToastUtil.diaplayMesShort(context, context.getResources().getString(R.string.dele_success));
            AVAnalytics.onEvent(context, "collect_tran_delete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyOnClickListener implements View.OnClickListener {

        boolean isNotify = false;
        private WordDetailListItem mBean;
        private ImageButton voice_play;
        private ProgressBar play_content_btn_progressbar;
        private boolean isPlayResult;

        private MyOnClickListener(WordDetailListItem bean, ImageButton voice_play,
                                  ProgressBar progressbar, boolean isPlayResult) {
            this.mBean = bean;
            this.voice_play = voice_play;
            this.play_content_btn_progressbar = progressbar;
            this.isPlayResult = isPlayResult;
        }

        @Override
        public void onClick(final View v) {
            String filepath = "";
            String speakContent = "";
            if (isPlayResult) {
                speakContent = mBean.getDesc();
            } else {
                speakContent = mBean.getName();
            }
            PlayUtil.play(filepath, speakContent,
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
                        play_content_btn_progressbar.setVisibility(View.GONE);
                        voice_play.setVisibility(View.VISIBLE);
                        PlayUtil.onStartPlay();
                    }

                    @Override
                    public void onCompleted(SpeechError arg0) {
                        LogUtil.DefalutLog("---onCompleted");
                        if (arg0 != null) {
                            ToastUtil.diaplayMesShort(context, arg0.getErrorDescription());
                        }
                        BoxHelper.insert(mBean);
                        PlayUtil.onFinishPlay();
                    }

                    @Override
                    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                        if(arg0 < 10){
                            play_content_btn_progressbar.setVisibility(View.VISIBLE);
                            voice_play.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                    }
                });
            if (v.getId() == R.id.record_question_cover) {
                AVAnalytics.onEvent(context, "collect_tran_play_question");
            } else if (v.getId() == R.id.record_answer_cover) {
                AVAnalytics.onEvent(context, "collect_tran_play_result");
            } else if (v.getId() == R.id.voice_play_layout) {
                AVAnalytics.onEvent(context, "collect_tran_play_voice");
            }
        }
    }

}
