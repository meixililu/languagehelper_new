package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingDetailActivity;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by luli on 10/23/16.
 */

public class RcReadingCollectedListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final ImageView music_play_img;
    private final FrameLayout list_item_img_parent;
    private final SimpleDraweeView list_item_img;
    private final JCVideoPlayerStandard videoplayer;
    private Fragment context;
    private List<Reading> avObjects;

    public RcReadingCollectedListItemViewHolder(Fragment context, View itemView, List<Reading> avObjects) {
        super(itemView);
        this.avObjects = avObjects;
        this.context = context;
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        list_item_img_parent = (FrameLayout) itemView.findViewById(R.id.list_item_img_parent);
        title = (TextView) itemView.findViewById(R.id.title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        music_play_img = (ImageView) itemView.findViewById(R.id.music_play_img);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
        videoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
    }

    public void render(final Reading mAVObject) {
        list_item_img_parent.setClickable(false);
        if(!mAVObject.isAd()){
            title.setText( mAVObject.getTitle() );
            type_name.setText( mAVObject.getType_name() );
            source_name.setText( mAVObject.getSource_name() );
            if(mAVObject.getType().equals("video") && !TextUtils.isEmpty(mAVObject.getMedia_url())){
                videoplayer.setVisibility(View.VISIBLE);
                list_item_img_parent.setVisibility(View.GONE);
                list_item_img.setVisibility(View.GONE);
                videoplayer.setUp(mAVObject.getMedia_url(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
                if (!TextUtils.isEmpty(mAVObject.getImg_url())) {
                    Glide.with(context)
                            .load(mAVObject.getImg_url())
                            .into(videoplayer.thumbImageView);
                }
            }else if(mAVObject.getType().equals("mp3")){
                videoplayer.setVisibility(View.GONE);
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                    list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                }else{
                    list_item_img.setImageResource(R.color.style3_color1);
                }
                if (!TextUtils.isEmpty(mAVObject.getMedia_url())) {
                    music_play_img.setVisibility(View.VISIBLE);
                    if(WXEntryActivity.musicSrv == null){
                        music_play_img.setImageResource(R.drawable.jc_click_play_selector);
                    }else if(mAVObject.getObject_id().equals(WXEntryActivity.musicSrv.lastSongId)){
                        if(WXEntryActivity.musicSrv.PlayerStatus == 1){
                            music_play_img.setImageResource(R.drawable.jc_click_pause_selector);
                        }else {
                            music_play_img.setImageResource(R.drawable.jc_click_play_selector);
                        }
                    }else {
                        music_play_img.setImageResource(R.drawable.jc_click_play_selector);
                    }

                } else {
                    music_play_img.setVisibility(View.GONE);
                }
                list_item_img_parent.setClickable(true);
                list_item_img_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WXEntryActivity.musicSrv.initAndPlay(mAVObject);
                    }
                });
            }else {
                videoplayer.setVisibility(View.GONE);
                music_play_img.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                    list_item_img_parent.setVisibility(View.VISIBLE);
                    list_item_img.setVisibility(View.VISIBLE);
                    list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                }else {
                    list_item_img_parent.setVisibility(View.GONE);
                    list_item_img.setVisibility(View.GONE);
                }
            }
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(avObjects.indexOf(mAVObject));
                }
            });
        }else{
            final NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
            if(mNativeADDataRef != null){
                videoplayer.setVisibility(View.GONE);
                music_play_img.setVisibility(View.GONE);
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                title.setText( mNativeADDataRef.getSubTitle() );
                type_name.setText(mNativeADDataRef.getTitle());
                source_name.setText("VoiceAds广告");
                Glide.with(context)
                        .load(mNativeADDataRef.getImage())
                        .into(list_item_img);
                layout_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNativeADDataRef.onClicked(v);
                    }
                });
            }
        }
    }

    private void toDetailActivity(int position){
        Reading item = avObjects.get(position);
        if(!TextUtils.isEmpty(item.getContent_type())){
            Intent intent = new Intent(context.getContext(), WebViewActivity.class);
            intent.putExtra(KeyUtil.URL, item.getSource_url());
            intent.putExtra(KeyUtil.ActionbarTitle, " ");
            context.startActivity(intent);
        }else {
            WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, avObjects);
            Intent intent = new Intent(context.getContext(),ReadingDetailActivity.class);
            intent.putExtra(KeyUtil.IndexKey, position);
            context.startActivityForResult(intent,position);
        }
    }

}