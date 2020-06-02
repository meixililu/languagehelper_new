package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.XimalayaRadioDetailActivity;
import com.messi.languagehelper.bean.RadioForAd;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyRadioHomeItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final LinearLayout source_layout;
    private final TextView title, sub_title;
    private final TextView type_name;
    private final TextView source_name;
    private final TextView source_tag;
    private final ImageView music_play_img;
    private final SimpleDraweeView list_item_img;
    private final FrameLayout ad_layout;
    private List<Radio> radios;
    private Context context;

    public RcXmlyRadioHomeItemViewHolder(View view,List<Radio> radios) {
        super(view);
        this.context = view.getContext();
        this.radios = radios;
        layout_cover = (FrameLayout) view.findViewById(R.id.layout_cover);
        source_layout = (LinearLayout) itemView.findViewById(R.id.source_layout);
        list_item_img = (SimpleDraweeView) view.findViewById(R.id.list_item_img);
        title = (TextView) view.findViewById(R.id.title);
        sub_title = (TextView) view.findViewById(R.id.sub_title);
        source_name = (TextView) view.findViewById(R.id.source_name);
        type_name = (TextView) view.findViewById(R.id.type_name);
        source_tag = (TextView) view.findViewById(R.id.source_tag);
        music_play_img = (ImageView) view.findViewById(R.id.music_play_img);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
    }

    public void render(final Radio mAVObject) {
        ad_layout.setVisibility(View.GONE);
        source_layout.setVisibility(View.GONE);
        list_item_img.setVisibility(View.GONE);
        sub_title.setText("");
        type_name.setText("");
        if (mAVObject instanceof RadioForAd) {
            source_tag.setVisibility(View.GONE);
            if(((RadioForAd) mAVObject).getmTXADView() != null){
                NativeExpressADView adView = ((RadioForAd) mAVObject).getmTXADView();
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                ad_layout.addView(adView);
                adView.render();
            }else if(((RadioForAd)mAVObject).getBdAdView() != null){
                AdView bdAdview = ((RadioForAd)mAVObject).getBdAdView();
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (bdAdview.getParent() != null) {
                    ((ViewGroup) bdAdview.getParent()).removeView(bdAdview);
                }
                int marginT = ScreenUtil.dip2px(context,10);
                int marginB = ScreenUtil.dip2px(context,3);
                int marginLR = ScreenUtil.dip2px(context,10);
                LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-marginLR*2, ((RadioForAd)mAVObject).getBdHeight());
                rllp.setMargins(marginLR,marginT,marginLR,marginB);
                ad_layout.addView(bdAdview,rllp);
            }else if(((RadioForAd)mAVObject).getCsjTTFeedAd() != null){
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                XXLModel.setCSJDView(context,((RadioForAd)mAVObject).getCsjTTFeedAd(), ad_layout);
            }else {
                source_layout.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                final NativeDataRef mNativeADDataRef = ((RadioForAd) mAVObject).getmNativeADDataRef();
                if (mNativeADDataRef != null) {
                    music_play_img.setVisibility(View.GONE);
                    title.setText(mNativeADDataRef.getTitle());
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
            source_tag.setVisibility(View.VISIBLE);
            music_play_img.setVisibility(View.VISIBLE);
            title.setText(mAVObject.getRadioName());
            sub_title.setText("正在直播：" + mAVObject.getProgramName());
            source_name.setText(StringUtils.numToStrTimes(mAVObject.getRadioPlayCount()));
            type_name.setText("");
            if(mAVObject.isActivityLive()){
                music_play_img.setImageResource(R.drawable.jz_pause_normal);
            }else {
                music_play_img.setImageResource(R.drawable.jz_play_normal);
            }
            list_item_img.setImageURI(Uri.parse(mAVObject.getCoverUrlLarge()));
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            source_name.setCompoundDrawables(drawable, null, null, null);
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playRadio(mAVObject);
                }
            });
        }
    }

    private void playRadio(Radio mAVObject) {
        reset();
        mAVObject.setActivityLive(true);
        LogUtil.DefalutLog("radio DataId:" + mAVObject.getDataId() + "---pid:" + mAVObject.getProgramId());
        Intent intent = new Intent(context, XimalayaRadioDetailActivity.class);
        intent.putExtra(KeyUtil.XmlyRadio, mAVObject);
        context.startActivity(intent);
    }

    private void reset(){
        for (Radio mRadio : radios){
            mRadio.setActivityLive(false);
        }
    }

}
