package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewNoAdActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcWebsiteItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final ImageView img;
    private Context context;

    public RcWebsiteItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        img = (ImageView) convertView.findViewById(R.id.img);
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.EnglishWebsite.Title));
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context, WebViewNoAdActivity.class);
        intent.putExtra(KeyUtil.URL, mAVObject.getString(AVOUtil.EnglishWebsite.Url));
        intent.putExtra(KeyUtil.AdFilter, mAVObject.getString(AVOUtil.EnglishWebsite.ad_filte));
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        context.startActivity(intent);
        AVAnalytics.onEvent(context, "to_webview");
    }

}
