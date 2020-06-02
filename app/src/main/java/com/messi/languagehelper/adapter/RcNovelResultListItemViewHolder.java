package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewWithCollectedActivity;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

/**
 * Created by luli on 10/23/16.
 */

public class RcNovelResultListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final TextView des;
    private final SimpleDraweeView img;
    private final LinearLayout ad_layout;
    private final LinearLayout item_layout;
    private final LinearLayout content;
    private Context context;

    public RcNovelResultListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        des = (TextView) convertView.findViewById(R.id.des);
        img = (SimpleDraweeView) convertView.findViewById(R.id.img);
        ad_layout = (LinearLayout) convertView.findViewById(R.id.ad_layout);
        item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
        content = (LinearLayout) convertView.findViewById(R.id.content);
    }

    public void render(final CNWBean mAVObject) {
        ad_layout.setVisibility(View.GONE);
        item_layout.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        final NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
        if(mNativeADDataRef == null){
            NativeExpressADView mADView = mAVObject.getmTXADView();
            if(mADView != null){
                ad_layout.setVisibility(View.VISIBLE);
                ad_layout.removeAllViews();
                if (mADView.getParent() != null) {
                    ((ViewGroup) mADView.getParent()).removeView(mADView);
                }
                ad_layout.addView(mADView);
                mADView.render();
            }else {
                item_layout.setVisibility(View.VISIBLE);
                content.setVisibility(View.VISIBLE);
                name.setText( mAVObject.getTitle());
                des.setText(mAVObject.getSub_title());
                cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        onItemClick(mAVObject);
                    }
                });
            }
        }else{
            item_layout.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
            name.setText(mNativeADDataRef.getTitle());
            des.setText(mNativeADDataRef.getDesc()+"  广告");
            img.setAspectRatio((float) 0.75);
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

    private void onItemClick(CNWBean mAVObject){
        Intent intent = new Intent(context, WebViewWithCollectedActivity.class);
        intent.putExtra(KeyUtil.URL, mAVObject.getRead_url());
        intent.putExtra(KeyUtil.ObjectKey, mAVObject);
        intent.putExtra(KeyUtil.FilterName, mAVObject.getSource_name());
        intent.putExtra(KeyUtil.IsNeedGetFilter, true);
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        intent.putExtra(KeyUtil.isHideMic,true);
        intent.putExtra(KeyUtil.IsReedPullDownRefresh, false);
        intent.putExtra(KeyUtil.IsShowCollectedBtn, true);
        context.startActivity(intent);
    }

}
