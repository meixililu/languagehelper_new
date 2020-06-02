package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.bean.TXNewsItem;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcNewsListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final FrameLayout list_item_img_parent;
    private final  SimpleDraweeView list_item_img;
    private Context context;

    public RcNewsListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        layout_cover = (FrameLayout) convertView.findViewById(R.id.layout_cover);
        list_item_img_parent = (FrameLayout) convertView.findViewById(R.id.list_item_img_parent);
        title = (TextView) convertView.findViewById(R.id.title);
        type_name = (TextView) convertView.findViewById(R.id.type_name);
        source_name = (TextView) convertView.findViewById(R.id.source_name);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
    }

    public void render(final TXNewsItem mAVObject) {
        final NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.getmNativeADDataRef();
        if(mNativeADDataRef == null){
            title.setText( mAVObject.getTitle() );
            source_name.setText( mAVObject.getDescription() );
            if(!TextUtils.isEmpty(mAVObject.getPicUrl())){
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                list_item_img.setImageURI(mAVObject.getPicUrl());
            }else{
                list_item_img_parent.setVisibility(View.GONE);
                list_item_img.setVisibility(View.GONE);
            }
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(mAVObject);
                }
            });
        }else{
            title.setText( mNativeADDataRef.getTitle() );
            type_name.setText(mNativeADDataRef.getDesc());
            source_name.setText("广告");
            list_item_img_parent.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageURI(mNativeADDataRef.getImgUrl());
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean onClicked = mNativeADDataRef.onClick(v);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
                }
            });
        }
    }

    private void toDetailActivity(TXNewsItem mAVObject){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, " ");
        intent.putExtra(KeyUtil.ToolbarBackgroundColorKey, R.color.news_title_bg);
        intent.putExtra(KeyUtil.URL, mAVObject.getUrl());
        context.startActivity(intent);
    }

}
