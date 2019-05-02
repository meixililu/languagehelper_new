package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.AVOUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcMomentsCommentListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView content;
    private Context context;

    public RcMomentsCommentListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        content = (TextView) convertView.findViewById(R.id.content);
    }

    public void render(final AVObject mAVObject) {
        content.setText(mAVObject.getString(AVOUtil.Moments.content));
    }


}
