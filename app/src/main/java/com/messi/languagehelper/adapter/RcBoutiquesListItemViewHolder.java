package com.messi.languagehelper.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.baidu.mobads.AdView;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import cn.jzvd.JzvdStd;

/**
 * Created by luli on 10/23/16.
 */

public class RcBoutiquesListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView des;
    private SimpleDraweeView list_item_img;
    private LinearLayout layout_cover;
    private FrameLayout ad_layout;
    private JzvdStd videoplayer;
    private Context context;

    public RcBoutiquesListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        des = (TextView) convertView.findViewById(R.id.des);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
        ad_layout = (FrameLayout) convertView.findViewById(R.id.ad_layout);
        videoplayer = (JzvdStd) convertView.findViewById(R.id.videoplayer);
    }

    public void render(final AVObject mAVObject) {
        list_item_img.setVisibility(View.GONE);
        videoplayer.setVisibility(View.GONE);
        ad_layout.setVisibility(View.GONE);
        des.setVisibility(View.VISIBLE);
        des.setText("");
        if(mAVObject.get(KeyUtil.ADKey) != null) {
            final NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.get(KeyUtil.ADKey);
            des.setText(mNativeADDataRef.getTitle()+"  广告");
            list_item_img.setAspectRatio((float) 1.5);
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImgUrl()));
            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean onClicked = mNativeADDataRef.onClick(v);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
                }
            });
        }else if(mAVObject.get(KeyUtil.TXADView) != null) {
            NativeExpressADView mADView = (NativeExpressADView) mAVObject.get(KeyUtil.TXADView);
            ad_layout.setVisibility(View.VISIBLE);
            ad_layout.removeAllViews();
            if (mADView.getParent() != null) {
                ((ViewGroup) mADView.getParent()).removeView(mADView);
            }
            ad_layout.addView(mADView);
            mADView.render();
        } else if(mAVObject.get(KeyUtil.BDADView) != null) {
            AdView adView = (AdView) mAVObject.get(KeyUtil.BDADView);
            int height = (int) mAVObject.get(KeyUtil.BDADViewHeigh);
            ad_layout.setVisibility(View.VISIBLE);
            ad_layout.removeAllViews();
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }
            int marginT = ScreenUtil.dip2px(context,10);
            int marginB = ScreenUtil.dip2px(context,5);
            int marginLR = ScreenUtil.dip2px(context,10);
            LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-marginLR*2, height);
            rllp.setMargins(marginLR,marginT,marginLR,marginB);
            ad_layout.addView(adView,rllp);
        } else if(mAVObject.get(KeyUtil.CSJADView) != null){
            TTFeedAd ad = (TTFeedAd) mAVObject.get(KeyUtil.CSJADView);
            ad_layout.setVisibility(View.VISIBLE);
            ad_layout.removeAllViews();
            XXLModel.setCSJDView(context,ad, ad_layout);
        }else {
            des.setVisibility(View.GONE);
            videoplayer.setVisibility(View.VISIBLE);
            videoplayer.setUp(mAVObject.getString(AVOUtil.BoutiquesList.media_url),
                    mAVObject.getString(AVOUtil.BoutiquesList.title));
            if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.BoutiquesList.img))){
                Glide.with(context)
                        .load(mAVObject.getString(AVOUtil.BoutiquesList.img))
                        .into(videoplayer.posterImageView);
            }
        }
    }


}
