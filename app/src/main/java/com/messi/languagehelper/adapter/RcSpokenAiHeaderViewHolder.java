package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.messi.languagehelper.AiSpokenBasicActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenAiHeaderViewHolder extends RecyclerView.ViewHolder {

    public View headerView;
    private Context mContext;

    public RcSpokenAiHeaderViewHolder(View itemView, Context mContext) {
        super(itemView);
        LogUtil.DefalutLog("RcAdHeaderViewHolder---init");
        this.headerView = itemView;
        this.mContext = mContext;
        FrameLayout base_layout = (FrameLayout)itemView.findViewById(R.id.base_layout);
        FrameLayout video_layout = (FrameLayout)itemView.findViewById(R.id.video_layout);
        base_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(AiSpokenBasicActivity.class);
            }
        });
        video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCoachActivity();
            }
        });
    }

    private void toActivity(Class mClass){
        Intent intent = new Intent(mContext,mClass);
        mContext.startActivity(intent);
    }

    private void toCoachActivity(){
        Intent intent = new Intent(mContext, ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, "口语跟读");
        intent.putExtra(KeyUtil.Category, AVOUtil.Category.spoken_english);
        intent.putExtra(KeyUtil.BoutiqueCode,"1000003");
        mContext.startActivity(intent);
    }

}
