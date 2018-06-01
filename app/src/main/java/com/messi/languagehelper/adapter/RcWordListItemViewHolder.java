package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudySecondActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordListItemViewHolder extends RecyclerView.ViewHolder {

    private View cover;
    private TextView name;
    private Context context;
    private String sing_plan;

    public RcWordListItemViewHolder(View convertView,String sing_plan) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        this.sing_plan = sing_plan;
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.WordCategory.name) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,WordStudySecondActivity.class);
        if(!TextUtils.isEmpty(sing_plan)){
            intent.putExtra(KeyUtil.WordStudyPlan, sing_plan);
        }
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.WordCategory.name));
        intent.putExtra(KeyUtil.Category, mAVObject.getString(AVOUtil.WordCategory.category_id));
        context.startActivity(intent);
    }

}
