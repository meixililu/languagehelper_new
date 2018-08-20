package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.messi.languagehelper.AiChatActivity;
import com.messi.languagehelper.AiSpokenBasicActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.XFYSAD;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenAiHeaderViewHolder extends RecyclerView.ViewHolder {

    private XFYSAD mXFYSAD;
    public View headerView;
    private Context mContext;

    public RcSpokenAiHeaderViewHolder(View itemView, XFYSAD mXFYSAD,
                                      Context mContext) {
        super(itemView);
        LogUtil.DefalutLog("RcAdHeaderViewHolder---init");
        this.headerView = itemView;
        this.mXFYSAD = mXFYSAD;
        this.mContext = mContext;
        mXFYSAD.setParentView(headerView);
        if(mXFYSAD != null){
            mXFYSAD.showAD();
        }
        FrameLayout ai_layout = (FrameLayout)itemView.findViewById(R.id.ai_layout);
        FrameLayout base_layout = (FrameLayout)itemView.findViewById(R.id.base_layout);
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
    }

    private void toActivity(Class mClass){
        Intent intent = new Intent(mContext,mClass);
        mContext.startActivity(intent);
    }

}