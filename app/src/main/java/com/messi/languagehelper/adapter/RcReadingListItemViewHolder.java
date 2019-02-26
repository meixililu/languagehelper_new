package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadDetailTouTiaoActivity;
import com.messi.languagehelper.ReadingDetailActivity;
import com.messi.languagehelper.ReadingDetailLrcActivity;
import com.messi.languagehelper.ReadingVideoDetailActivity;
import com.messi.languagehelper.WebViewForAdActivity;
import com.messi.languagehelper.XVideoDetailActivity;
import com.messi.languagehelper.XVideoHomeActivity;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
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
    private final TextView xvideo_more_tv;
    private final ImageView music_play_img;
    private final LinearLayout imgs_layout;
    private final LinearLayout normal_layout;
    private final LinearLayout xvideo_layout;
    private final LinearLayout xvideo_content;
    private final FrameLayout list_item_img_parent;
    private final FrameLayout ad_layout;
    private final LinearLayout item_layout;
    private final SimpleDraweeView list_item_img,imgs_1,imgs_2,imgs_3;

    private final FrameLayout videoplayer_cover;
    private final SimpleDraweeView videoplayer_img;
    private Context context;
    private List<Reading> avObjects;
    private boolean isPlayList;

    public RcReadingListItemViewHolder(View itemView,List<Reading> avObjects,boolean isPlayList) {
        super(itemView);
        this.context = itemView.getContext();
        this.avObjects = avObjects;
        this.isPlayList = isPlayList;
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        list_item_img_parent = (FrameLayout) itemView.findViewById(R.id.list_item_img_parent);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
        item_layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
        imgs_layout = (LinearLayout) itemView.findViewById(R.id.imgs_layout);
        normal_layout = (LinearLayout) itemView.findViewById(R.id.normal_layout);
        xvideo_layout = (LinearLayout) itemView.findViewById(R.id.xvideo_layout);
        xvideo_content = (LinearLayout) itemView.findViewById(R.id.xvideo_content);
        title = (TextView) itemView.findViewById(R.id.title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        xvideo_more_tv = (TextView) itemView.findViewById(R.id.xvideo_more_tv);
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
        imgs_layout.setVisibility(View.GONE);
        item_layout.setVisibility(View.GONE);
        videoplayer_cover.setVisibility(View.GONE);
        xvideo_layout.setVisibility(View.GONE);
        title.setText("");
        list_item_img_parent.setVisibility(View.GONE);
        if(!mAVObject.isAd()){
            if(mAVObject.getmTXADView() != null){
                NativeExpressADView adView = mAVObject.getmTXADView();
                ad_layout.setVisibility(View.VISIBLE);
                item_layout.setVisibility(View.GONE);
                ad_layout.removeAllViews();
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                ad_layout.addView(adView);
                adView.render();
            }else if(mAVObject.getBdAdView() != null){
                AdView bdAdview = mAVObject.getBdAdView();
                ad_layout.setVisibility(View.VISIBLE);
                item_layout.setVisibility(View.GONE);
                ad_layout.removeAllViews();
                if (bdAdview.getParent() != null) {
                    ((ViewGroup) bdAdview.getParent()).removeView(bdAdview);
                }
                int marginT = ScreenUtil.dip2px(context,8);
                int marginB = ScreenUtil.dip2px(context,2);
                int marginLR = ScreenUtil.dip2px(context,8);
                LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-marginLR*2, mAVObject.getBdHeight());
                rllp.setMargins(marginLR,marginT,marginLR,marginB);
                ad_layout.addView(bdAdview,rllp);
            }else if(mAVObject.getCsjTTFeedAd() != null){
                ad_layout.setVisibility(View.VISIBLE);
                item_layout.setVisibility(View.GONE);
                ad_layout.removeAllViews();
                getCSJDView(mAVObject.getCsjTTFeedAd());
            }else if(mAVObject.getXvideoList() != null){
                xvideo_layout.setVisibility(View.VISIBLE);
                xvideo_more_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toXVideoActivity();
                    }
                });
                addXvideo(mAVObject.getXvideoList());
            }else {
                item_layout.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(mAVObject.getStatus())){
                    title.setTextColor(context.getResources().getColor(R.color.text_dark));
                }else {
                    title.setTextColor(context.getResources().getColor(R.color.text_grey2));
                }
                title.setText( mAVObject.getTitle() );
                type_name.setText( mAVObject.getType_name() );
                source_name.setText( mAVObject.getSource_name() );
                if(mAVObject.getType() != null && mAVObject.getType().equals("video") && !TextUtils.isEmpty(mAVObject.getMedia_url())){
                    videoplayer_cover.setVisibility(View.GONE);
                    list_item_img_parent.setVisibility(View.VISIBLE);
                    list_item_img.setVisibility(View.VISIBLE);
                    music_play_img.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                        list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                    }else{
                        if(mAVObject.getImg_color() > 0){
                            list_item_img.setImageResource(mAVObject.getImg_color());
                        }else {
                            list_item_img.setImageResource(R.color.style6_color2);
                        }
                    }
                    music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                }else if(mAVObject.getType() != null && mAVObject.getType().equals("mp3")){
                    videoplayer_cover.setVisibility(View.GONE);
                    list_item_img_parent.setVisibility(View.VISIBLE);
                    list_item_img.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                        list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                    }else{
                        if(mAVObject.getImg_color() > 0){
                            list_item_img.setImageResource(mAVObject.getImg_color());
                        }else {
                            list_item_img.setImageResource(R.color.style6_color2);
                        }
                    }
                    if (!TextUtils.isEmpty(mAVObject.getMedia_url())) {
                        music_play_img.setVisibility(View.VISIBLE);
                        if(Setings.musicSrv == null){
                            music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                        }else if(mAVObject.getObject_id().equals(Setings.musicSrv.lastSongId)){
                            if(Setings.musicSrv.PlayerStatus == 1){
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
                            if(isPlayList){
                                Setings.musicSrv.initPlayList(avObjects,avObjects.indexOf(mAVObject));
                            }else {
                                Setings.musicSrv.initAndPlay(mAVObject);
                            }
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
            item_layout.setVisibility(View.VISIBLE);
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
            if("今日头条".equals(item.getSource_name()) && item.getType().equals("video")){
                Setings.dataMap.put(KeyUtil.DataMapKey, item);
                Intent intent = new Intent(context, ReadDetailTouTiaoActivity.class);
                context.startActivity(intent);
            }else {
                Intent intent = new Intent(context, WebViewForAdActivity.class);
                intent.putExtra(KeyUtil.URL, item.getSource_url());
                intent.putExtra(KeyUtil.Type, item.getType());
                intent.putExtra(KeyUtil.IsNeedGetFilter, true);
                intent.putExtra(KeyUtil.FilterName, item.getSource_name());
                intent.putExtra(KeyUtil.ActionbarTitle, " ");
                intent.putExtra(KeyUtil.IsHideToolbar, true);
                context.startActivity(intent);
            }
        }else {
            Setings.dataMap.put(KeyUtil.DataMapKey, avObjects);
            Class toDetail = null;
            if(item.getType() != null && item.getType().equals("video") && !TextUtils.isEmpty(item.getMedia_url())){
                toDetail = ReadingVideoDetailActivity.class;
            }else {
                if(TextUtils.isEmpty(item.getLrc_url())){
                    toDetail = ReadingDetailActivity.class;
                }else {
                    toDetail = ReadingDetailLrcActivity.class;
                }
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

    private void toXVideoActivity(){
        Intent intent = new Intent(context, XVideoHomeActivity.class);
        intent.putExtra(KeyUtil.IndexKey,1);
        context.startActivity(intent);
    }

    private void addXvideo(List<AVObject> list){
        xvideo_content.removeAllViews();
        for(AVObject item : list){
            xvideo_content.addView(getItemView(item));
        }
        xvideo_content.addView(getItemMoreView());
    }

    private View getItemView(final AVObject item){
        View view = LayoutInflater.from(context).inflate(R.layout.xvideo_item,null);
        View cover = (View) view.findViewById(R.id.layout_cover);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(context, 170),ScreenUtil.dip2px(context, 250));
        mParams.rightMargin = ScreenUtil.dip2px(context, 10);
        cover.setLayoutParams(mParams);
        TextView name = (TextView) view.findViewById(R.id.name);
        SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.img);
        img.setImageURI(item.getString(AVOUtil.XVideo.img_url));
        name.setText( item.getString(AVOUtil.XVideo.title));
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(item);
            }
        });
        return view;
    }

    private View getItemMoreView(){
        View view = LayoutInflater.from(context).inflate(R.layout.xvideo_item_more,null);
        View cover = (View) view.findViewById(R.id.layout_cover);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(context, 170),ScreenUtil.dip2px(context, 250));
        mParams.rightMargin = ScreenUtil.dip2px(context, 10);
        cover.setLayoutParams(mParams);
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                toXVideoActivity();
            }
        });
        return view;
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context, XVideoDetailActivity.class);
        intent.putExtra(KeyUtil.AVObjectKey, mAVObject.toString());
        context.startActivity(intent);
    }

    private void getCSJDView(TTFeedAd ad){
        View view = null;
        if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_LARGE_IMG) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_ad_large_pic,null);
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_GROUP_IMG) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_ad_group_pic,null);
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_ad_large_video,null);

        }
        if(view != null){
//            int marginT = ScreenUtil.dip2px(context,8);
//            int marginB = ScreenUtil.dip2px(context,2);
//            int marginLR = ScreenUtil.dip2px(context,8);
//            LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-marginLR*2, mAVObject.getBdHeight());
//            rllp.setMargins(marginLR,marginT,marginLR,marginB);
//            ad_layout.addView(view,rllp);
            ad_layout.addView(view);
        }
    }
}
