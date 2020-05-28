package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.MyPlayer;
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
    public FrameLayout voice_play_layout;
    private Context context;

    private List<WordDetailListItem> beans;
    private RcCollectTranslateListAdapter mAdapter;

    public RcCollectTranslateLiatItemViewHolder(View convertView,
                                                List<WordDetailListItem> mBeans,
                                                SharedPreferences mSharedPreferences,
                                                RcCollectTranslateListAdapter mAdapter) {
        super(convertView);
        this.context = convertView.getContext();
        this.beans = mBeans;
        this.mAdapter = mAdapter;
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
        voice_play_layout = (FrameLayout) convertView.findViewById(R.id.voice_play_layout);
        delete_btn = (FrameLayout) convertView.findViewById(R.id.delete_btn);
        weixi_btn = (FrameLayout) convertView.findViewById(R.id.weixi_btn);
    }

    public void render(final WordDetailListItem mBean) {
        record_question.setText(mBean.getName());
        if (!TextUtils.isEmpty(mBean.getSymbol())) {
            record_answer.setText(mBean.getSymbol() + "\n" + mBean.getDesc());
        } else {
            record_answer.setText(mBean.getDesc());
        }
        record_answer_cover.setOnClickListener(view ->
                MyPlayer.getInstance(context).start(mBean.getDesc()) );
        record_question_cover.setOnClickListener(view ->
                MyPlayer.getInstance(context).start(mBean.getName(),mBean.getSound()) );
        voice_play_layout.setOnClickListener(view ->
                MyPlayer.getInstance(context).start(mBean.getName(),mBean.getSound()) );

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
}
