package com.messi.languagehelper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudySummaryListActivity;
import com.messi.languagehelper.util.AVOUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudySummaryMenuListItemViewHolder extends RecyclerView.ViewHolder {

    private WordStudySummaryListActivity mWordStudySummaryListActivity;
    private final View cover;
    private final TextView name;
    private Context context;

    public RcWordStudySummaryMenuListItemViewHolder(View convertView,WordStudySummaryListActivity mWordStudySummaryListActivity) {
        super(convertView);
        this.mWordStudySummaryListActivity = mWordStudySummaryListActivity;
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    @SuppressLint("ResourceAsColor")
    public void render(final AVObject mAVObject) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[] {
                R.attr.colorPrimary
        });
        name.setTextColor(array.getColor(0,R.color.load_blue));
        name.setText( mAVObject.getString(AVOUtil.HJWordStudyCategory.name) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        if(mWordStudySummaryListActivity != null){
            mWordStudySummaryListActivity.getDataByType(mAVObject.getString(AVOUtil.HJWordStudyCategory.type_code));
        }
    }

}
