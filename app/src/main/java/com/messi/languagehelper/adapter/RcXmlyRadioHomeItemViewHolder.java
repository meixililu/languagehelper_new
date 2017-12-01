package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.XimalayaRadioDetailActivity;
import com.messi.languagehelper.bean.RadioForAd;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.StringUtils;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyRadioHomeItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title, sub_title;
    private final TextView type_name;
    private final TextView source_name;
    private final ImageView music_play_img;
    private final SimpleDraweeView list_item_img;
    private List<Radio> radios;
    private Context context;

    public RcXmlyRadioHomeItemViewHolder(View view,List<Radio> radios) {
        super(view);
        this.context = view.getContext();
        this.radios = radios;
        layout_cover = (FrameLayout) view.findViewById(R.id.layout_cover);
        list_item_img = (SimpleDraweeView) view.findViewById(R.id.list_item_img);
        title = (TextView) view.findViewById(R.id.title);
        sub_title = (TextView) view.findViewById(R.id.sub_title);
        source_name = (TextView) view.findViewById(R.id.source_name);
        type_name = (TextView) view.findViewById(R.id.type_name);
        music_play_img = (ImageView) view.findViewById(R.id.music_play_img);
    }

    public void render(final Radio mAVObject) {
        if (mAVObject instanceof RadioForAd) {
            final NativeADDataRef mNativeADDataRef = ((RadioForAd) mAVObject).getmNativeADDataRef();
            if (mNativeADDataRef != null) {
                music_play_img.setVisibility(View.GONE);
                title.setText(mNativeADDataRef.getTitle());
                sub_title.setText("");
                type_name.setText("");
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
            title.setText(mAVObject.getRadioName());
            sub_title.setText("正在直播：" + mAVObject.getProgramName());
            source_name.setText(StringUtils.numToStrTimes(mAVObject.getRadioPlayCount()));
            type_name.setText("");
            music_play_img.setVisibility(View.VISIBLE);
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
