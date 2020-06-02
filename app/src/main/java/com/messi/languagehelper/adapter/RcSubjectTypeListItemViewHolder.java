package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.SubjectActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ScreenUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcSubjectTypeListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;
    private String recentKey;

    public RcSubjectTypeListItemViewHolder(View convertView, String recentKey) {
        super(convertView);
        this.recentKey = recentKey;
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        name.setPadding(ScreenUtil.dip2px(context,15),
                ScreenUtil.dip2px(context,5),
                0,
                ScreenUtil.dip2px(context,5));
        name.setGravity(Gravity.LEFT);
        name.setText( mAVObject.getString(AVOUtil.SubjectType.name) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,SubjectActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.SubjectType.name));
        intent.putExtra(KeyUtil.SubjectName, mAVObject.getString(AVOUtil.SubjectType.code));
        intent.putExtra(KeyUtil.RecentKey, recentKey);
        context.startActivity(intent);
    }

}
