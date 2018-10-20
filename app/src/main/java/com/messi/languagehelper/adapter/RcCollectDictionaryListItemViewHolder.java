package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcCollectDictionaryListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView record_answer;
    private FrameLayout record_question_cover;
    private FrameLayout record_answer_cover;
    private TextView record_question;
    private FrameLayout copy_btn;
    private FrameLayout collected_btn;
    private FrameLayout delete_btn;
    private CheckBox collected_cb;
    private ImageButton voice_play;
    private FrameLayout voice_play_layout;
    private ProgressBar play_content_btn_progressbar;
    private Activity context;
    private List<Dictionary> mBeans;
    RcCollectDictionaryListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;

    public RcCollectDictionaryListItemViewHolder(View convertView,
                                                 Activity context,
                                                 List<Dictionary> mBeans,
                                                 RcCollectDictionaryListAdapter mAdapter,
                                                 SharedPreferences mSharedPreferences) {
        super(convertView);
        this.context = context;
        this.mBeans = mBeans;
        this.mAdapter = mAdapter;
        this.mSharedPreferences = mSharedPreferences;
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
        collected_cb = (CheckBox) convertView.findViewById(R.id.collected_cb);
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        copy_btn = (FrameLayout) convertView.findViewById(R.id.copy_btn);
        collected_btn = (FrameLayout) convertView.findViewById(R.id.collected_btn);
        delete_btn = (FrameLayout) convertView.findViewById(R.id.delete_btn);
        voice_play = (ImageButton) convertView.findViewById(R.id.voice_play);
        voice_play_layout = (FrameLayout) convertView.findViewById(R.id.voice_play_layout);
        play_content_btn_progressbar = (ProgressBar) convertView.findViewById(R.id.play_content_btn_progressbar);
    }

    public void render(final Dictionary mBean) {
        AnimationDrawable animationDrawable = (AnimationDrawable) voice_play.getBackground();
        MyOnClickListener mQuestionOnClickListener = new MyOnClickListener(mBean,animationDrawable,voice_play,play_content_btn_progressbar,false);
        record_question.setText(mBean.getWord_name());
        String[] temps = mBean.getResult().split("\n\n");
        String result = "";
        if(temps.length > 0){
            result = temps[0].trim();
        }else {
            result = mBean.getResult();
        }
        record_answer.setText(result);
        record_question_cover.setOnClickListener(mQuestionOnClickListener);
        voice_play_layout.setOnClickListener(mQuestionOnClickListener);
        if (mBean.getIscollected().equals("0")) {
            collected_cb.setChecked(false);
        } else {
            collected_cb.setChecked(true);
        }
        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context,mBean.getWord_name());
                return true;
            }
        });
        final String finalResult = result;
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setings.copy(context,mBean.getWord_name() + "\n" + finalResult);
            }
        });
        collected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCollectedStatus(getAdapterPosition());
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntity(getAdapterPosition());
            }
        });
    }

    public void deleteEntity(int position) {
        try {
            Dictionary mBean = mBeans.remove(position);
            mAdapter.notifyItemRemoved(position);
            DataBaseUtil.getInstance().dele(mBean);
            Setings.isDictionaryFragmentNeedRefresh = true;
            ToastUtil.diaplayMesShort(context, context.getResources().getString(R.string.dele_success));
            AVAnalytics.onEvent(context, "tab2_delete_btn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCollectedStatus(int position) {
        Dictionary mBean = mBeans.remove(position);
        mAdapter.notifyItemRemoved(position);
        mBean.setIscollected("0");
        DataBaseUtil.getInstance().update(mBean);
        Setings.isDictionaryFragmentNeedRefresh = true;
        ToastUtil.diaplayMesShort(context, context.getResources().getString(R.string.favorite_cancle));
    }

    public class MyOnClickListener implements View.OnClickListener {

        private Dictionary mBean;
        private ImageButton voice_play;
        private AnimationDrawable animationDrawable;
        private ProgressBar play_content_btn_progressbar;
        private boolean isPlayResult;
        boolean isNotify = false;

        private MyOnClickListener(Dictionary bean,AnimationDrawable mAnimationDrawable,ImageButton voice_play,
                                  ProgressBar progressbar, boolean isPlayResult){
            this.mBean = bean;
            this.voice_play = voice_play;
            this.animationDrawable = mAnimationDrawable;
            this.play_content_btn_progressbar = progressbar;
            this.isPlayResult = isPlayResult;
        }
        @Override
        public void onClick(final View v) {
            String filepath = "";
            String speakContent = "";
            String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
            if (TextUtils.isEmpty(mBean.getResultVoiceId()) || TextUtils.isEmpty(mBean.getQuestionVoiceId())) {
                mBean.setQuestionVoiceId(System.currentTimeMillis() + "");
                mBean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
            }
            if (isPlayResult) {
                filepath = path + mBean.getResultVoiceId() + ".pcm";
                mBean.setResultAudioPath(filepath);
                if (!TextUtils.isEmpty(mBean.getBackup1())) {
                    speakContent = mBean.getBackup1();
                } else {
                    speakContent = mBean.getResult();
                }
            } else {
                filepath = path + mBean.getQuestionVoiceId() + ".pcm";
                mBean.setQuestionAudioPath(filepath);
                speakContent = mBean.getWord_name();
            }
            if (mBean.getSpeak_speed() != mSharedPreferences.getInt(context.getString(R.string.preference_key_tts_speed), 50)) {
                String filep1 = path + mBean.getResultVoiceId() + ".pcm";
                String filep2 = path + mBean.getQuestionVoiceId() + ".pcm";
                AudioTrackUtil.deleteFile(filep1);
                AudioTrackUtil.deleteFile(filep2);
                mBean.setSpeak_speed(mSharedPreferences.getInt(context.getString(R.string.preference_key_tts_speed), 50));
            }
            PlayUtil.play(filepath, speakContent, animationDrawable,
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
                        DataBaseUtil.getInstance().update(mBean);
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
                AVAnalytics.onEvent(context, "tab2_dic_play_question");
            } else if (v.getId() == R.id.record_answer_cover) {
                AVAnalytics.onEvent(context, "tab2_dic_play_result");
            } else if (v.getId() == R.id.voice_play_layout) {
                AVAnalytics.onEvent(context, "tab2_dic_play_voice");
            }
        }

    }

}
