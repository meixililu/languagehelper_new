package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudySummaryListActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordSummaryListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;

    public RcWordSummaryListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        name.setText(mAVObject.getString(AVOUtil.HJWordStudyCategory.name));
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject) {
        Intent intent = new Intent(context, WordStudySummaryListActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.HJWordStudyCategory.name));
        intent.putExtra(KeyUtil.Category, mAVObject.getString(AVOUtil.HJWordStudyCategory.category));
        intent.putExtra(KeyUtil.list_type, mAVObject.getString(AVOUtil.HJWordStudyCategory.list_type));
        context.startActivity(intent);
    }

}
