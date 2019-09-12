package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import cn.jzvd.JzvdStd;

/**
 * Created by luli on 10/23/16.
 */

public class RcJokeListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView des;
    private SimpleDraweeView list_item_img;
    private LinearLayout layout_cover;
    private FrameLayout ad_layout;
    private JzvdStd videoplayer;
    private Context context;

    public RcJokeListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        des = (TextView) convertView.findViewById(R.id.des);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
        ad_layout = (FrameLayout) convertView.findViewById(R.id.ad_layout);
        videoplayer = (JzvdStd) convertView.findViewById(R.id.videoplayer);
    }

    //		type 1 img; 2 gif; 3 gif; 4 video; 5 text; 6 url；
    public void render(final AVObject mAVObject) {
        list_item_img.setVisibility(View.GONE);
        videoplayer.setVisibility(View.GONE);
        ad_layout.setVisibility(View.GONE);
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
            XXLModel.getCSJDView(context,ad, ad_layout);
        }else {
            des.setText(StringUtils.fromHtml(mAVObject.getString(AVOUtil.Joke.text)));
            if (mAVObject.getString(AVOUtil.Joke.type).equals("1")) {
                list_item_img.setAspectRatio((float)mAVObject.getDouble(AVOUtil.Joke.ratio));
                list_item_img.setVisibility(View.VISIBLE);
                list_item_img.setImageURI(mAVObject.getString(AVOUtil.Joke.img));
            } else if(mAVObject.getString(AVOUtil.Joke.type).equals("3")){
                list_item_img.setAspectRatio((float)mAVObject.getDouble(AVOUtil.Joke.ratio));
                list_item_img.setVisibility(View.VISIBLE);
                DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true)
                        .setUri(Uri.parse(mAVObject.getString(AVOUtil.Joke.img)))
                        .build();
                list_item_img.setController(mDraweeController);
            }else if(mAVObject.getString(AVOUtil.Joke.type).equals("4")){
                videoplayer.setVisibility(View.VISIBLE);
                videoplayer.setUp(mAVObject.getString(AVOUtil.Joke.video_url),
                        "",JzvdStd.SCREEN_WINDOW_LIST);
                if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.Joke.img))){
                    Glide.with(context)
                            .load(mAVObject.getString(AVOUtil.Joke.img))
                            .into(videoplayer.thumbImageView);
                }
            }
            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mAVObject);
                }
            });
        }
    }

    private void onItemClick(AVObject mAVObject) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, " ");
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.Joke.img))){
            intent.putExtra(KeyUtil.URL, mAVObject.getString(AVOUtil.Joke.img));
        }else {
            intent.putExtra(KeyUtil.URL, mAVObject.getString(AVOUtil.Joke.source_url));
        }
        context.startActivity(intent);
    }

}
