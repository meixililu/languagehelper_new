package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.AVOUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcXVideoDetailListItemViewHolder extends RecyclerView.ViewHolder {

    public TextView title,btn_detail;
    public FrameLayout player_view_layout;
    public ImageView cover_img;
    public ProgressBar progress_bar;
    public Context context;
    public AVObject mAVObject;

    public RcXVideoDetailListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        title = (TextView) convertView.findViewById(R.id.title);
        btn_detail = (TextView) convertView.findViewById(R.id.btn_detail);
        player_view_layout = (FrameLayout) convertView.findViewById(R.id.player_view_layout);
        cover_img = (ImageView) convertView.findViewById(R.id.cover_img);
        progress_bar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
    }

    public void render(final AVObject mAVObject) {
        player_view_layout.removeAllViews();
        this.mAVObject = mAVObject;
        btn_detail.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        cover_img.setVisibility(View.VISIBLE);
        title.setText(mAVObject.getString(AVOUtil.XVideo.title));
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.XVideo.img_url))){
            Glide.with(context)
                    .load(mAVObject.getString(AVOUtil.XVideo.img_url))
                    .into(cover_img);
        }
    }

}
