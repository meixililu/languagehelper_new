package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsBySubjectActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcSubjectListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final LinearLayout normal_layout;
    private final SimpleDraweeView list_item_img;
    private final FrameLayout ad_layout;
    private Context context;
    private String recentKey;

    public RcSubjectListItemViewHolder(View convertView,String recentKey) {
        super(convertView);
        this.recentKey = recentKey;
        this.context = convertView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
        normal_layout = (LinearLayout) itemView.findViewById(R.id.normal_layout);
        title = (TextView) itemView.findViewById(R.id.title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
    }

    public void render(final AVObject mAVObject) {
        normal_layout.setVisibility(View.GONE);
        title.setText( mAVObject.getString(AVOUtil.SubjectList.name) );
        normal_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,ReadingsBySubjectActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.SubjectList.name));
        intent.putExtra(KeyUtil.SubjectName, mAVObject.getString(AVOUtil.SubjectList.name));
        intent.putExtra(KeyUtil.LevelKey, mAVObject.getString(AVOUtil.SubjectList.level));
        intent.putExtra(KeyUtil.RecentKey, recentKey);
        context.startActivity(intent);
    }

}
