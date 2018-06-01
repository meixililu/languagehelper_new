package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.UserSpeakBean;
import com.messi.languagehelper.impl.SpokenEnglishPlayListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcAiDialoguePracticeItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final RelativeLayout item;
    private final TextView name;
    private final TextView des;
    private Context context;
    private List<AVObject> avObjects;
    private SpokenEnglishPlayListener mListener;

    public RcAiDialoguePracticeItemViewHolder(View convertView, List<AVObject> avObjects,
                                              SpokenEnglishPlayListener mListener) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        item = (RelativeLayout) convertView.findViewById(R.id.item);
        name = (TextView) convertView.findViewById(R.id.name);
        des = (TextView) convertView.findViewById(R.id.des);
        this.avObjects = avObjects;
        this.mListener = mListener;
    }

    public void render(final AVObject mAVObject) {
        String temp = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent);
        UserSpeakBean mBean = (UserSpeakBean)mAVObject.get(KeyUtil.UserSpeakBean);
        if(temp.contains("#")){
            String[] strs = temp.split("#");
            des.setVisibility(View.VISIBLE);
            des.setText( strs[1] );
            if(mBean != null && !TextUtils.isEmpty(mBean.getContent())){
                name.setText(mBean.getContent());
            }else {
                name.setText( strs[0] );
            }
        }else{
            if(mBean != null && !TextUtils.isEmpty(mBean.getContent())){
                name.setText(mBean.getContent());
            }else {
                name.setText( temp );
            }
            des.setVisibility(View.GONE);
        }
        if(!mAVObject.get(KeyUtil.PracticeItemIndex).equals("1")){
            name.setTextColor(context.getResources().getColor(R.color.text_black));
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }else {
            name.setTextColor(context.getResources().getColor(R.color.material_color_light_blue));
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        }
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(getAdapterPosition());
            }
        });
    }

    public void onItemClick(int position) {
        if(mListener != null){
            mListener.playOrStop(position);
        }
    }

}
