package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.XFUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcEnDictItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView record_question;
    public FrameLayout record_question_cover;


    public RcEnDictItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_question = (TextView) convertView.findViewById(R.id.record_question);
    }

    public void render(final String item) {
        record_question.setText(item);
        record_question_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XFUtil.play(item,"");
            }
        });
        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context,item);
                return true;
            }
        });
    }


}
