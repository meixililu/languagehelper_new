package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsActivity;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.bean.AdData;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

/**
 * Created by luli on 10/23/16.
 */

public class RcBoutiquesViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView name;
    private final SimpleDraweeView list_item_img;
    private final FrameLayout ad_layout,content_layout;
    private Context context;

    public RcBoutiquesViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        name = (TextView) itemView.findViewById(R.id.name);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
        content_layout = (FrameLayout) itemView.findViewById(R.id.content_layout);
    }

    public void render(final BoutiquesBean mAVObject) {
        ad_layout.setVisibility(View.GONE);
        content_layout.setVisibility(View.GONE);
        name.setText("");
        if (mAVObject.isAd() && mAVObject.getAdData() != null) {
            AdData mAdData = mAVObject.getAdData();
            if(mAdData.getMNativeADDataRef() != null) {
                content_layout.setVisibility(View.VISIBLE);
                final NativeDataRef mNativeADDataRef = mAdData.getMNativeADDataRef();
                name.setText(mNativeADDataRef.getTitle()+"  广告");
                list_item_img.setAspectRatio((float) 1.5);
                list_item_img.setVisibility(View.VISIBLE);
                list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImgUrl()));
                list_item_img.setOnClickListener(v -> {
                    boolean onClicked = mNativeADDataRef.onClick(v);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
                });
            }else if(mAdData.getMTXADView() != null) {
                ad_layout.removeAllViews();
                ad_layout.setVisibility(View.VISIBLE);
                NativeExpressADView mADView = mAdData.getMTXADView();
                if (mADView.getParent() != null) {
                    ((ViewGroup) mADView.getParent()).removeView(mADView);
                }
                ad_layout.addView(mADView);
                mADView.render();
            } else if(mAdData.getBdAdView() != null) {
                ad_layout.removeAllViews();
                ad_layout.setVisibility(View.VISIBLE);
                AdView adView = mAdData.getBdAdView();
                int height = mAdData.getBdHeight();
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                int marginT = ScreenUtil.dip2px(context,10);
                int marginB = ScreenUtil.dip2px(context,5);
                int marginLR = ScreenUtil.dip2px(context,10);
                LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH-marginLR*2, height);
                rllp.setMargins(marginLR,marginT,marginLR,marginB);
                ad_layout.addView(adView,rllp);
            } else if(mAdData.getCsjTTFeedAd() != null){
                ad_layout.removeAllViews();
                ad_layout.setVisibility(View.VISIBLE);
                TTFeedAd ad = mAdData.getCsjTTFeedAd();
                XXLModel.setCSJDView(context,ad, ad_layout);
            }
        } else {
            content_layout.setVisibility(View.VISIBLE);
            name.setText( mAVObject.getTitle() );
            if(TextUtils.isEmpty(mAVObject.getImg_url())){
                list_item_img.setVisibility(View.GONE);;
            }else{
                list_item_img.setVisibility(View.VISIBLE);
                list_item_img.setImageURI(mAVObject.getImg_url());
            }
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(mAVObject);
                }
            });
        }
    }

    private void onItemClick(BoutiquesBean mAVObject){
        Intent intent = new Intent(context, ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
        intent.putExtra(KeyUtil.BoutiqueCode, mAVObject.getCode());
        intent.putExtra(KeyUtil.ObjectKey,mAVObject);
        context.startActivity(intent);
    }

}
