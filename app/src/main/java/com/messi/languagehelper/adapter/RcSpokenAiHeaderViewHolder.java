package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.messi.languagehelper.AiChatActivity;
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
        FrameLayout ai_layout = (FrameLayout)itemView.findViewById(R.id.ai_layout);
        FrameLayout base_layout = (FrameLayout)itemView.findViewById(R.id.base_layout);
        FrameLayout video_layout = (FrameLayout)itemView.findViewById(R.id.video_layout);
        ai_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(AiChatActivity.class);
            }
        });
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
        intent.putExtra(KeyUtil.ActionbarTitle, "口语天堂");
        intent.putExtra(KeyUtil.Category, AVOUtil.Category.spoken_english);
        intent.putExtra(KeyUtil.NewsSource,"口语天堂");
        mContext.startActivity(intent);
    }

}
