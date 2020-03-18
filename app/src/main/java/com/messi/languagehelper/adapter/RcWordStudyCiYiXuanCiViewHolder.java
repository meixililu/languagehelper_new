package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.box.WordDetailListItem;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudyCiYiXuanCiViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView user_incorrect;
    private final TextView correct;
    private Context context;

    public RcWordStudyCiYiXuanCiViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        title = (TextView) convertView.findViewById(R.id.title);
        user_incorrect = (TextView) convertView.findViewById(R.id.user_incorrect);
        correct = (TextView) convertView.findViewById(R.id.correct);
    }

    public void render(final WordDetailListItem mAVObject) {
        title.setText( mAVObject.getName() );
        correct.setText( mAVObject.getDesc() );
        if(mAVObject.getSelect_time() > 0){
            user_incorrect.setText("错"+mAVObject.getSelect_time()+"次");
        }else {
            user_incorrect.setText(" ");
        }


    }
}
