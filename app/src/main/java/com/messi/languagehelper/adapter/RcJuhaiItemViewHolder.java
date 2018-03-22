package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.JuhaiBean;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.XFUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcJuhaiItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView record_question;
    public TextView record_answer;
    public FrameLayout record_answer_cover;
    public FrameLayout record_question_cover;


    public RcJuhaiItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
    }

    public void render(final JuhaiBean mBean) {
        record_question.setText(mBean.getSentence());
        record_answer.setText(mBean.getMeanning());

        record_question_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XFUtil.play(mBean.getSentence(),"");
            }
        });
        record_answer_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XFUtil.play(mBean.getMeanning(),"");
            }
        });

        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Settings.copy(context,mBean.getSentence());
                return true;
            }
        });
        record_answer_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Settings.copy(context,mBean.getMeanning());
                return true;
            }
        });
    }


}
