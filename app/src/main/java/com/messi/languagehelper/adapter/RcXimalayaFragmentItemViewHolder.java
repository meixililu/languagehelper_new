package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.XimalayaTrackListActivity;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.StringUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created by luli on 10/23/16.
 */

public class RcXimalayaFragmentItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title, sub_title;
    private final TextView type_name;
    private final TextView source_name;
    private final SimpleDraweeView list_item_img;

    private Context context;

    public RcXimalayaFragmentItemViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        title = (TextView) itemView.findViewById(R.id.title);
        sub_title = (TextView) itemView.findViewById(R.id.sub_title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);

    }

    public void render(final Album mAVObject) {
        if (mAVObject instanceof AlbumForAd) {
            final NativeADDataRef mNativeADDataRef = ((AlbumForAd) mAVObject).getmNativeADDataRef();
            if (mNativeADDataRef != null) {
                title.setText(mNativeADDataRef.getTitle());
                sub_title.setText("");
                type_name.setText("");
                type_name.setCompoundDrawables(null, null, null, null);
                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                source_name.setCompoundDrawables(drawable, null, null, null);
                source_name.setText(" VoiceAds广告");
                list_item_img.setImageURI(mNativeADDataRef.getImage());
                layout_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean adClick = mNativeADDataRef.onClicked(v);
                        LogUtil.DefalutLog("adClick:" + adClick);
                    }
                });
            }
        } else {
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
        LogUtil.DefalutLog(mAVObject.toString());
        Intent intent = new Intent(context, XimalayaTrackListActivity.class);
        intent.putExtra("album_id", mAVObject.getId()+"");
        intent.putExtra("play_times", mAVObject.getPlayCount());
        intent.putExtra("track_count", mAVObject.getIncludeTrackCount());
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getAlbumTitle());
        context.startActivity(intent);
//        if(TextUtils.isEmpty(item.getStatus())){
//            item.setStatus("1");
//            DataBaseUtil.getInstance().update(item);
//        }
    }

}
