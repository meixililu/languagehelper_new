package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.XVideoDetailActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXVideoListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final LinearLayout ad_layout;
    private final SimpleDraweeView img;
    private Context context;
    private List<AVObject> mAVObjects;

    private String category;
    private String keyword;

    public RcXVideoListItemViewHolder(View convertView,List<AVObject> mAVObjects,String category) {
        super(convertView);
        this.context = convertView.getContext();
        this.mAVObjects = mAVObjects;
        this.category = category;
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        img = (SimpleDraweeView) convertView.findViewById(R.id.img);
        ad_layout = (LinearLayout) convertView.findViewById(R.id.ad_layout);
    }

    public void render(final AVObject mAVObject) {
        ad_layout.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        final NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.get(KeyUtil.ADKey);
        if(mNativeADDataRef == null){
            NativeExpressADView mADView = (NativeExpressADView) mAVObject.get(KeyUtil.TXADView);
            if(mADView != null){
                name.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (mADView.getParent() != null) {
                    ((ViewGroup) mADView.getParent()).removeView(mADView);
                }
                ad_layout.addView(mADView);
                mADView.render();
            }else if(mAVObject.get(KeyUtil.VideoAD) != null){
                Object object = mAVObject.get(KeyUtil.VideoAD);
                if(object instanceof TTDrawFeedAd){
                    TTDrawFeedAd ad = (TTDrawFeedAd)object;
                    img.setImageURI(ad.getVideoCoverImage().getImageUrl());
                    name.setText(ad.getTitle());
                    cover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            onItemClick(mAVObject);
                        }
                    });
                }
            }else {
                img.setImageURI(mAVObject.getString(AVOUtil.XVideo.img_url));
                name.setText( mAVObject.getString(AVOUtil.XVideo.title));
                cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        onItemClick(mAVObject);
                    }
                });
            }
        }else{
            name.setText(mNativeADDataRef.getTitle()+"  广告");
            img.setAspectRatio((float) 0.56);
            img.setImageURI(Uri.parse(mNativeADDataRef.getImgUrl()));
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean onClicked = mNativeADDataRef.onClick(v);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
                }
            });
        }

    }

    private void onItemClick(AVObject mAVObject){
        Setings.dataMap.put(KeyUtil.DataMapKey,mAVObjects);
        Intent intent = new Intent(context, XVideoDetailActivity.class);
        intent.putExtra(KeyUtil.PositionKey,mAVObjects.indexOf(mAVObject));
        intent.putExtra(KeyUtil.Category, category);
        context.startActivity(intent);
    }

}
