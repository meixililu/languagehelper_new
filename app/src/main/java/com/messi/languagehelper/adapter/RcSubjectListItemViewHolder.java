package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsBySubjectActivity;
import com.messi.languagehelper.SubjectActivity;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

/**
 * Created by luli on 10/23/16.
 */

public class RcSubjectListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final LinearLayout normal_layout;
    private final SimpleDraweeView list_item_img;
    private final FrameLayout ad_layout;
    private Context context;

    public RcSubjectListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
        ad_layout = (FrameLayout) itemView.findViewById(R.id.ad_layout);
        normal_layout = (LinearLayout) itemView.findViewById(R.id.normal_layout);
        title = (TextView) itemView.findViewById(R.id.title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
    }

    public void render(final AVObject mAVObject) {
        normal_layout.setVisibility(View.GONE);
        list_item_img.setVisibility(View.GONE);
        ad_layout.setVisibility(View.GONE);
        title.setText("");
        type_name.setText("");
        source_name.setText("");
        if(mAVObject.get(KeyUtil.ADKey) != null) {
            normal_layout.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            final NativeADDataRef mNativeADDataRef = (NativeADDataRef) mAVObject.get(KeyUtil.ADKey);
            title.setText(mNativeADDataRef.getTitle());
            source_name.setText("广告");
            list_item_img.setAspectRatio((float) 1.5);
            list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImage()));
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean onClicked = mNativeADDataRef.onClicked(v);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
                }
            });
        }else if(mAVObject.get(KeyUtil.TXADView) != null) {
            ad_layout.setVisibility(View.VISIBLE);
            NativeExpressADView mADView = (NativeExpressADView) mAVObject.get(KeyUtil.TXADView);
            ad_layout.removeAllViews();
            if (mADView.getParent() != null) {
                ((ViewGroup) mADView.getParent()).removeView(mADView);
            }
            ad_layout.addView(mADView);
            mADView.render();
        } else if(mAVObject.get(KeyUtil.BDADView) != null) {
            ad_layout.setVisibility(View.VISIBLE);
            AdView adView = (AdView) mAVObject.get(KeyUtil.BDADView);
            int height = (int) mAVObject.get(KeyUtil.BDADViewHeigh);
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
            ad_layout.setVisibility(View.VISIBLE);
            TTFeedAd ad = (TTFeedAd) mAVObject.get(KeyUtil.CSJADView);
            ad_layout.removeAllViews();
            XXLModel.getCSJDView(context,ad, ad_layout);
        }else {
            normal_layout.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageResource(mAVObject.getInt(KeyUtil.ColorKey));
            title.setText( mAVObject.getString(AVOUtil.SubjectList.name) );
            source_name.setText( getCategoryName(mAVObject.getString(AVOUtil.SubjectList.category)) );
            type_name.setText( "听力课堂" );
            normal_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onItemClick(mAVObject);
                }
            });
        }
    }
    
    private String getCategoryName(String category){
        if ("listening".equals(category)) {
            return "英语听力";
        } else if ("spoken_english".equals(category)) {
            return "英语口语";
        } else if ("symbol".equals(category)) {
            return "英语音标";
        } else if ("story".equals(category)) {
            return "英语故事";
        } else if ("grammar".equals(category)) {
            return "英语语法";
        } else if ("word".equals(category)) {
            return "英语单词";
        } else {
            return "英语学习";
        }
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,ReadingsBySubjectActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.SubjectList.name));
        intent.putExtra(KeyUtil.SubjectName, mAVObject.getString(AVOUtil.SubjectList.name));
        intent.putExtra(KeyUtil.ObjectKey, SubjectActivity.toReadingSubject(mAVObject));
        intent.putExtra(KeyUtil.LevelKey, mAVObject.getString(AVOUtil.SubjectList.level));
        context.startActivity(intent);
    }



}
