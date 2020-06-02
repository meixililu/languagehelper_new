package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.CaricatureSourceActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcCaricatureSourceItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final SimpleDraweeView img;
    private Context context;

    public RcCaricatureSourceItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        img = (SimpleDraweeView) convertView.findViewById(R.id.img);
    }

    public void render(final AVObject mAVObject) {
        img.setImageURI(mAVObject.getString(AVOUtil.EnglishWebsite.ImgUrl));
        name.setText( mAVObject.getString(AVOUtil.EnglishWebsite.Title));
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context, CaricatureSourceActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.EnglishWebsite.Title));
        intent.putExtra(KeyUtil.SearchKey, mAVObject.getString(AVOUtil.EnglishWebsite.Title));
        intent.putExtra(KeyUtil.URL, mAVObject.getString(AVOUtil.EnglishWebsite.Url));
        intent.putExtra(KeyUtil.AdFilter, mAVObject.getString(AVOUtil.EnglishWebsite.ad_filte));
        context.startActivity(intent);
    }

}
