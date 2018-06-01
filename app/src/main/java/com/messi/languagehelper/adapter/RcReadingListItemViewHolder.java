package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingDetailActivity;
import com.messi.languagehelper.ReadingVideoDetailActivity;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcReadingListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final ImageView music_play_img;
    private final LinearLayout imgs_layout;
    private final FrameLayout list_item_img_parent;
    private final FrameLayout ad_layout;
    private final SimpleDraweeView list_item_img,imgs_1,imgs_2,imgs_3;

    private final FrameLayout videoplayer_cover;
    private final SimpleDraweeView videoplayer_img;
    private Context context;
    private List<Reading> avObjects;

    public RcReadingListItemViewHolder(View itemView,List<Reading> avObjects) {
        super(itemView);
        this.context = itemView.getContext();
        this.avObjects = avObjects;
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        list_item_img_parent = (FrameLayout) itemView.findViewById(R.id.list_item_img_parent);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
        imgs_layout = (LinearLayout) itemView.findViewById(R.id.imgs_layout);
        title = (TextView) itemView.findViewById(R.id.title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        music_play_img = (ImageView) itemView.findViewById(R.id.music_play_img);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
        imgs_1 = (SimpleDraweeView) itemView.findViewById(R.id.imgs_1);
        imgs_2 = (SimpleDraweeView) itemView.findViewById(R.id.imgs_2);
        imgs_3 = (SimpleDraweeView) itemView.findViewById(R.id.imgs_3);
        videoplayer_img = (SimpleDraweeView) itemView.findViewById(R.id.videoplayer_img);
        videoplayer_cover = (FrameLayout) itemView.findViewById(R.id.videoplayer_cover);
    }

    public void render(final Reading mAVObject) {
        list_item_img_parent.setClickable(false);
        ad_layout.setVisibility(View.GONE);
        if(!mAVObject.isAd()){
            if(mAVObject.getmTXADView() != null){
                NativeExpressADView adView = mAVObject.getmTXADView();
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                ad_layout.addView(adView);
                adView.render();
            }else {
                imgs_layout.setVisibility(View.GONE);
                if(TextUtils.isEmpty(mAVObject.getStatus())){
                    title.setTextColor(context.getResources().getColor(R.color.text_dark));
                }else {
                    title.setTextColor(context.getResources().getColor(R.color.text_grey2));
                }
                title.setText( mAVObject.getTitle() );
                type_name.setText( mAVObject.getType_name() );
                source_name.setText( mAVObject.getSource_name() );
                if(mAVObject.getType() != null && mAVObject.getType().equals("video") && !TextUtils.isEmpty(mAVObject.getMedia_url())){
                    videoplayer_cover.setVisibility(View.VISIBLE);
                    list_item_img_parent.setVisibility(View.GONE);
                    list_item_img.setVisibility(View.GONE);
                    if(mAVObject.getImg_color() > 0){
                        videoplayer_img.setImageResource(mAVObject.getImg_color());
                    }else {
                        videoplayer_img.setImageResource(R.color.black);
                    }
                    if (!TextUtils.isEmpty(mAVObject.getImg_url())) {
                        Glide.with(context)
                                .load(mAVObject.getImg_url())
                                .into(videoplayer_img);
                    }
                }else if(mAVObject.getType() != null && mAVObject.getType().equals("mp3")){
                    videoplayer_cover.setVisibility(View.GONE);
                    list_item_img_parent.setVisibility(View.VISIBLE);
                    list_item_img.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                        list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                    }else{
                        if(mAVObject.getImg_color() > 0){
                            LogUtil.DefalutLog("Img_color:"+mAVObject.getImg_color());
                            list_item_img.setImageResource(mAVObject.getImg_color());
                        }else {
                            list_item_img.setImageResource(R.color.style6_color2);
                        }
                    }
                    if (!TextUtils.isEmpty(mAVObject.getMedia_url())) {
                        music_play_img.setVisibility(View.VISIBLE);
                        if(WXEntryActivity.musicSrv == null){
                            music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                        }else if(mAVObject.getObject_id().equals(WXEntryActivity.musicSrv.lastSongId)){
                            if(WXEntryActivity.musicSrv.PlayerStatus == 1){
                                music_play_img.setImageResource(R.drawable.jz_click_pause_selector);
                            }else {
                                music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                            }
                        }else {
                            music_play_img.setImageResource(R.drawable.jz_click_play_selector);
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
                    videoplayer_cover.setVisibility(View.GONE);
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
            }
        }else{
            final NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
            if(mNativeADDataRef != null){
                title.setTextColor(context.getResources().getColor(R.color.text_dark));
                videoplayer_cover.setVisibility(View.GONE);
                music_play_img.setVisibility(View.GONE);
                title.setText( mNativeADDataRef.getTitle() );
                type_name.setText(mNativeADDataRef.getSubTitle());
                source_name.setText("广告");

                if(mNativeADDataRef.getImgUrls() != null && mNativeADDataRef.getImgUrls().size() > 2){
                    imgs_layout.setVisibility(View.VISIBLE);
                    list_item_img_parent.setVisibility(View.GONE);
                    list_item_img.setVisibility(View.GONE);
                    imgs_1.setImageURI(mNativeADDataRef.getImgUrls().get(0));
                    imgs_2.setImageURI(mNativeADDataRef.getImgUrls().get(1));
                    imgs_3.setImageURI(mNativeADDataRef.getImgUrls().get(2));
                }else {
                    imgs_layout.setVisibility(View.GONE);
                    list_item_img_parent.setVisibility(View.VISIBLE);
                    list_item_img.setVisibility(View.VISIBLE);
                    list_item_img.setImageURI(mNativeADDataRef.getImage());
                }
                layout_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean adClick = mNativeADDataRef.onClicked(v);
                        LogUtil.DefalutLog("adClick:"+adClick);
                    }
                });
            }
        }
    }

    private void toDetailActivity(int position){
        Reading item = avObjects.get(position);
        if(!TextUtils.isEmpty(item.getContent_type())){
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(KeyUtil.URL, item.getSource_url());
            intent.putExtra(KeyUtil.ActionbarTitle, " ");
            context.startActivity(intent);
        }else {
            WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, avObjects);
            Class toDetail = null;
            if(item.getType() != null && item.getType().equals("video") && !TextUtils.isEmpty(item.getMedia_url())){
                toDetail = ReadingVideoDetailActivity.class;
            }else {
                toDetail = ReadingDetailActivity.class;
            }
            Intent intent = new Intent(context,toDetail);
            intent.putExtra(KeyUtil.IndexKey, position);
            context.startActivity(intent);
        }
        if(TextUtils.isEmpty(item.getStatus())){
            item.setStatus("1");
            DataBaseUtil.getInstance().update(item);
        }
    }

}
