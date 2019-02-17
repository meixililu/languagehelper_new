package com.messi.languagehelper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;

import com.avos.avoscloud.AVObject;

/**
 * Created by luli on 10/23/16.
 */

public class RcAdEnglishItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView source_name;
    private final SimpleDraweeView list_item_img;
    private Context context;

    public RcAdEnglishItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        title = (TextView) itemView.findViewById(R.id.title);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
    }

    public void render(final AVObject mAVObject) {
        title.setText( mAVObject.getString(AVOUtil.AdList.title) );
        source_name.setText( mAVObject.getString(AVOUtil.AdList.des) );
        list_item_img.setImageURI( Uri.parse(mAVObject.getString(AVOUtil.AdList.img)) );
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        ADUtil.toAdView(context,
                mAVObject.getString(AVOUtil.AdList.type)
                ,mAVObject.getString(AVOUtil.AdList.url));
        updateDownloadTime(mAVObject.getObjectId());
    }

    private void updateDownloadTime(String objectid){
        AVObject post = AVObject.createWithoutData(AVOUtil.AdList.AdList, objectid);
        post.increment(AVOUtil.AdList.click_time);
        post.saveInBackground();
    }

}
