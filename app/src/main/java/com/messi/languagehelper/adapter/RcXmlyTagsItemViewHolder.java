package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mobads.AdView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.XimalayaTrackListActivity;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyTagsItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final LinearLayout source_layout;
    private final TextView title, sub_title;
    private final TextView type_name;
    private final TextView source_name;
    private final TextView source_sign;
    private final SimpleDraweeView list_item_img;
    private final FrameLayout ad_layout;
    private Context context;

    public RcXmlyTagsItemViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        source_layout = (LinearLayout) itemView.findViewById(R.id.source_layout);
        title = (TextView) itemView.findViewById(R.id.title);
        sub_title = (TextView) itemView.findViewById(R.id.sub_title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        source_sign = (TextView) itemView.findViewById(R.id.source_sign);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
    }

    public void render(final Album mAVObject) {
        ad_layout.setVisibility(View.GONE);
        source_layout.setVisibility(View.GONE);
        list_item_img.setVisibility(View.GONE);
        title.setText("");
        sub_title.setText("");
        if (mAVObject instanceof AlbumForAd) {
            source_sign.setVisibility(View.GONE);
            if(((AlbumForAd) mAVObject).getmTXADView() != null){
                NativeExpressADView adView = ((AlbumForAd) mAVObject).getmTXADView();
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                ad_layout.addView(adView);
                adView.render();
            }else if(((AlbumForAd)mAVObject).getBdAdView() != null){
                AdView bdAdview = ((AlbumForAd)mAVObject).getBdAdView();
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (bdAdview.getParent() != null) {
                    ((ViewGroup) bdAdview.getParent()).removeView(bdAdview);
                }
                int marginT = ScreenUtil.dip2px(context,10);
                int marginB = ScreenUtil.dip2px(context,3);
                int marginLR = ScreenUtil.dip2px(context,10);
                LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-marginLR*2, ((AlbumForAd)mAVObject).getBdHeight());
                rllp.setMargins(marginLR,marginT,marginLR,marginB);
                ad_layout.addView(bdAdview,rllp);
            }else if(((AlbumForAd)mAVObject).getCsjTTFeedAd() != null){
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                XXLModel.setCSJDView(context,((AlbumForAd)mAVObject).getCsjTTFeedAd(), ad_layout);
            }else {
                list_item_img.setVisibility(View.VISIBLE);
                source_layout.setVisibility(View.VISIBLE);
                final NativeDataRef mNativeADDataRef = ((AlbumForAd) mAVObject).getmNativeADDataRef();
                if (mNativeADDataRef != null) {
                    title.setText(mNativeADDataRef.getTitle());
                    sub_title.setText("");
                    type_name.setText("");
                    type_name.setCompoundDrawables(null, null, null, null);
                    source_name.setCompoundDrawables(null, null, null, null);
                    source_name.setText("广告");
                    list_item_img.setImageURI(mNativeADDataRef.getImgUrl());
                    mNativeADDataRef.onExposure(layout_cover);
                    layout_cover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean adClick = mNativeADDataRef.onClick(v);
                            LogUtil.DefalutLog("adClick:" + adClick);
                        }
                    });
                }
            }
        } else {
            list_item_img.setVisibility(View.VISIBLE);
            source_layout.setVisibility(View.VISIBLE);
            source_sign.setVisibility(View.VISIBLE);
            title.setText(mAVObject.getAlbumTitle());
            sub_title.setText(mAVObject.getAlbumIntro());
            source_name.setText(StringUtils.numToStrTimes(mAVObject.getPlayCount()));
            type_name.setText(" " + String.valueOf(mAVObject.getIncludeTrackCount()) + " 集");
            list_item_img.setImageURI(Uri.parse(mAVObject.getCoverUrlLarge()));
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            source_name.setCompoundDrawables(drawable, null, null, null);
            Drawable dra = context.getResources().getDrawable(R.drawable.ic_item_sounds_count);
            dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
            type_name.setCompoundDrawables(dra, null, null, null);
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(mAVObject);
                }
            });
        }
    }

    private void toDetailActivity(final Album mAVObject) {
        Intent intent = new Intent(context, XimalayaTrackListActivity.class);
        intent.putExtra("album_id", mAVObject.getId()+"");
        intent.putExtra("play_times", mAVObject.getPlayCount());
        intent.putExtra("track_count", mAVObject.getIncludeTrackCount());
        intent.putExtra(KeyUtil.JSONData, new Gson().toJson(mAVObject));
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getAlbumTitle());
        context.startActivity(intent);
    }

}
