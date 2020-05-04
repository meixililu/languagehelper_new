package com.messi.languagehelper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.FullScreenVideoADModel;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcXVideoDetailListItemViewHolder extends RecyclerView.ViewHolder {

    public SimpleExoPlayer player;
    public TextView title,btn_detail;
    public FrameLayout player_view_layout;
    public SimpleDraweeView cover_img;
    public ProgressBar progress_bar;
    public Context context;
    public AVObject mAVObject;
    public PlayerView player_view;
    public WebView mWebView;

    public String media_url;
    public String type;
    public String Url = "";

    public RcXVideoDetailListItemViewHolder(View convertView, SimpleExoPlayer player, PlayerView player_view, WebView mWebView) {
        super(convertView);
        this.context = convertView.getContext();
        this.player_view = player_view;
        this.mWebView = mWebView;
        this.player = player;
        title = (TextView) convertView.findViewById(R.id.title);
        btn_detail = (TextView) convertView.findViewById(R.id.btn_detail);
        player_view_layout = (FrameLayout) convertView.findViewById(R.id.player_view_layout);
        cover_img = (SimpleDraweeView) convertView.findViewById(R.id.cover_img);
        progress_bar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
    }

    public void render(final AVObject mAVObject) {
        this.mAVObject = mAVObject;
        btn_detail.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        cover_img.setVisibility(View.VISIBLE);
        player_view_layout.removeAllViews();
        if(player != null){
            player.setPlayWhenReady(false);
        }
        type = mAVObject.getString(AVOUtil.XVideo.type);
        media_url = mAVObject.getString(AVOUtil.XVideo.media_url);
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.XVideo.group_id))){
            Url = "https://www.ixigua.com/i"+mAVObject.getString(AVOUtil.XVideo.group_id);
        }else {
            Url = mAVObject.getString(AVOUtil.XVideo.source_url);
        }
        if(mAVObject.get(KeyUtil.VideoAD) != null){
            Object object = mAVObject.get(KeyUtil.VideoAD);
            if(object instanceof TTDrawFeedAd){
                TTDrawFeedAd ad = (TTDrawFeedAd)object;
                FullScreenVideoADModel.initAdViewAndAction(ad,player_view_layout,title,btn_detail);
                if(!TextUtils.isEmpty(ad.getVideoCoverImage().getImageUrl())){
                    cover_img.setImageURI(Uri.parse(ad.getVideoCoverImage().getImageUrl()));
                }
            }
        }else {
            title.setText(mAVObject.getString(AVOUtil.XVideo.title));
            if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.XVideo.img_url))){
                cover_img.setImageURI(Uri.parse(mAVObject.getString(AVOUtil.XVideo.img_url)));
            }
        }
    }

}
