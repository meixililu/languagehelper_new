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

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.MomentsComentActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

/**
 * Created by luli on 10/23/16.
 */

public class RcMomentsListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView content;
    private LinearLayout layout_cover;
    private FrameLayout ad_layout;
    private SimpleDraweeView list_item_img;
    private ImageView comment;
    private ImageView like;
    private TextView like_num;
    private Context context;

    public RcMomentsListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        ad_layout = (FrameLayout) convertView.findViewById(R.id.ad_layout);
        layout_cover = (LinearLayout) convertView.findViewById(R.id.layout_cover);
        content = (TextView) convertView.findViewById(R.id.content);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
        comment = (ImageView) convertView.findViewById(R.id.comment);
        like = (ImageView) convertView.findViewById(R.id.like);
        like_num = (TextView) convertView.findViewById(R.id.like_num);
    }

    public void render(final AVObject mAVObject) {
        layout_cover.setVisibility(View.GONE);
        ad_layout.setVisibility(View.GONE);
        list_item_img.setVisibility(View.GONE);
        content.setText("");
        if(mAVObject.get(KeyUtil.ADKey) != null) {
            layout_cover.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            final NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.get(KeyUtil.ADKey);
            content.setText(mNativeADDataRef.getTitle()+"  广告");
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
            XXLModel.getCSJDView(context,ad, ad_layout);
        }else {
            layout_cover.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(mAVObject.getString(KeyUtil.MomentLike))){
                like.setImageResource(R.drawable.ic_favorite_border_black);
            }else {
                like.setImageResource(R.drawable.ic_favorite_red);
            }
            like_num.setText(String.valueOf(mAVObject.getNumber(AVOUtil.Moments.likes)));
            content.setText(mAVObject.getString(AVOUtil.Moments.content));
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mAVObject);
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mAVObject);
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(mAVObject.getString(KeyUtil.MomentLike))){
                        BoxHelper.insertMomentLike(mAVObject.getObjectId());
                        mAVObject.put(KeyUtil.MomentLike,KeyUtil.MomentLike);
                        like.setImageResource(R.drawable.ic_favorite_red);
                        mAVObject.increment(AVOUtil.Moments.likes);
                        like_num.setText(String.valueOf(mAVObject.getNumber(AVOUtil.Moments.likes)));
                        mAVObject.saveInBackground();
                    }else {
                        BoxHelper.deleteMomentLikes(mAVObject.getObjectId());
                        mAVObject.put(KeyUtil.MomentLike,"");
                        like.setImageResource(R.drawable.ic_favorite_border_black);
                        mAVObject.put(AVOUtil.Moments.likes,mAVObject.getNumber(AVOUtil.Moments.likes).intValue()-1);
                        like_num.setText(String.valueOf(mAVObject.getNumber(AVOUtil.Moments.likes)));
                    }
                }
            });
        }
    }

    private void onItemClick(AVObject mAVObject) {
        Intent intent = new Intent(context, MomentsComentActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, " ");
        intent.putExtra(KeyUtil.ContextKey, mAVObject.getString(AVOUtil.Moments.content));
        intent.putExtra(KeyUtil.Id, mAVObject.getObjectId());
        context.startActivity(intent);
    }

}
