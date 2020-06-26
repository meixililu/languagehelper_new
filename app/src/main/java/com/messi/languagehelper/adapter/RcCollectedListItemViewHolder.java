package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadDetailTouTiaoActivity;
import com.messi.languagehelper.ReadingDetailActivity;
import com.messi.languagehelper.ReadingDetailLrcActivity;
import com.messi.languagehelper.ReadingMp3DetailActivity;
import com.messi.languagehelper.ReadingsActivity;
import com.messi.languagehelper.ReadingsBySubjectActivity;
import com.messi.languagehelper.XimalayaTrackListActivity;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.box.CollectedData;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.box.ReadingSubject;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;

/**
 * Created by luli on 10/23/16.
 */

public class RcCollectedListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final TextView xvideo_more_tv;
    private final ImageView music_play_img;
    private final LinearLayout imgs_layout;
    private final LinearLayout normal_layout;
    private final LinearLayout xvideo_layout;
    private final LinearLayout xvideo_content;
    private final FrameLayout list_item_img_parent;
    private final FrameLayout ad_layout;
    private final LinearLayout item_layout;
    private final SimpleDraweeView list_item_img,imgs_1,imgs_2,imgs_3;

    private final FrameLayout videoplayer_cover;
    private final SimpleDraweeView videoplayer_img;
    private Context context;

    public RcCollectedListItemViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        layout_cover =  itemView.findViewById(R.id.layout_cover);
        list_item_img_parent =  itemView.findViewById(R.id.list_item_img_parent);
        ad_layout =  itemView.findViewById(R.id.ad_layout);
        item_layout =  itemView.findViewById(R.id.item_layout);
        imgs_layout =  itemView.findViewById(R.id.imgs_layout);
        normal_layout =  itemView.findViewById(R.id.normal_layout);
        xvideo_layout =  itemView.findViewById(R.id.xvideo_layout);
        xvideo_content =  itemView.findViewById(R.id.xvideo_content);
        title =  itemView.findViewById(R.id.title);
        type_name =  itemView.findViewById(R.id.type_name);
        source_name =  itemView.findViewById(R.id.source_name);
        xvideo_more_tv =  itemView.findViewById(R.id.xvideo_more_tv);
        music_play_img =  itemView.findViewById(R.id.music_play_img);
        list_item_img =  itemView.findViewById(R.id.list_item_img);
        imgs_1 =  itemView.findViewById(R.id.imgs_1);
        imgs_2 =  itemView.findViewById(R.id.imgs_2);
        imgs_3 =  itemView.findViewById(R.id.imgs_3);
        videoplayer_img =  itemView.findViewById(R.id.videoplayer_img);
        videoplayer_cover =  itemView.findViewById(R.id.videoplayer_cover);
    }

    public void render(final CollectedData mCollectedData){
        list_item_img_parent.setClickable(false);
        ad_layout.setVisibility(View.GONE);
        imgs_layout.setVisibility(View.GONE);
        item_layout.setVisibility(View.GONE);
        videoplayer_cover.setVisibility(View.GONE);
        xvideo_layout.setVisibility(View.GONE);
        title.setText("");
        list_item_img_parent.setVisibility(View.GONE);

        String json = mCollectedData.getJson();
        String type = mCollectedData.getType();
        if(AVOUtil.Reading.Reading.equals(type)){
            Reading mAVObject = JSON.parseObject(json,Reading.class);
            item_layout.setVisibility(View.VISIBLE);
            title.setText( mAVObject.getTitle() );
            type_name.setText( mAVObject.getType_name() );
            source_name.setText( mAVObject.getSource_name() );
            if(mAVObject.getType() != null && mAVObject.getType().equals("video") && !TextUtils.isEmpty(mAVObject.getMedia_url())){
                videoplayer_cover.setVisibility(View.GONE);
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                music_play_img.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                    list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                }else{
                    if(mAVObject.getImg_color() > 0){
                        list_item_img.setImageResource(mAVObject.getImg_color());
                    }else {
                        list_item_img.setImageResource(R.color.style6_color2);
                    }
                }
                if(IPlayerUtil.MPlayerIsSameMp3(mAVObject)){
                    if(IPlayerUtil.getPlayStatus() == 1){
                        music_play_img.setImageResource(R.drawable.jz_click_pause_selector);
                    }else {
                        music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                    }
                }else {
                    music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                }
            }else if(mAVObject.getType() != null && mAVObject.getType().equals("mp3")){
                videoplayer_cover.setVisibility(View.GONE);
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                music_play_img.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                    list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                }else{
                    if(mAVObject.getImg_color() > 0){
                        list_item_img.setImageResource(mAVObject.getImg_color());
                    }else {
                        list_item_img.setImageResource(R.color.style6_color2);
                    }
                }
                if (!TextUtils.isEmpty(mAVObject.getMedia_url())) {
                    music_play_img.setVisibility(View.VISIBLE);
                    if(IPlayerUtil.MPlayerIsSameMp3(mAVObject)){
                        if(IPlayerUtil.getPlayStatus() == 1){
                            music_play_img.setImageResource(R.drawable.jz_click_pause_selector);
                        }else {
                            music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                        }
                    }else {
                        music_play_img.setImageResource(R.drawable.jz_click_play_selector);
                    }
                } else {
                    music_play_img.setVisibility(View.GONE);
                }
                list_item_img_parent.setClickable(true);
                list_item_img_parent.setOnClickListener(view ->
                        IPlayerUtil.initAndPlay(mAVObject, true,0)
                );
            }else {
                videoplayer_cover.setVisibility(View.GONE);
                music_play_img.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(mAVObject.getImg_url())){
                    list_item_img_parent.setVisibility(View.VISIBLE);
                    list_item_img.setVisibility(View.VISIBLE);
                    list_item_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
                }else {
                    list_item_img_parent.setVisibility(View.GONE);
                    list_item_img.setVisibility(View.GONE);
                }
            }
            layout_cover.setOnClickListener(view -> toDetailActivity(type, mAVObject));
        } else if (AVOUtil.Boutiques.Boutiques.equals(type)) {
            BoutiquesBean mBoutiquesBean = JSON.parseObject(json,BoutiquesBean.class);
            item_layout.setVisibility(View.GONE);
            title.setText( mBoutiquesBean.getTitle() );
            type_name.setText(mBoutiquesBean.getTag());
            source_name.setText(mBoutiquesBean.getSource_name());
            videoplayer_cover.setVisibility(View.VISIBLE);
            list_item_img_parent.setVisibility(View.GONE);
            list_item_img.setVisibility(View.VISIBLE);
            music_play_img.setVisibility(View.VISIBLE);
            videoplayer_img.setImageURI(mBoutiquesBean.getImg_url());
            layout_cover.setOnClickListener(view -> toDetailActivity(type, mBoutiquesBean));
        } else if (AVOUtil.SubjectList.SubjectList.equals(type)){
            ReadingSubject mReadingSubject = JSON.parseObject(json,ReadingSubject.class);
            item_layout.setVisibility(View.VISIBLE);
            title.setText( mReadingSubject.getName() );
            type_name.setText(mReadingSubject.getSource_name());
            source_name.setText(Setings.getCategoryName(mReadingSubject.getCategory()) );
            videoplayer_cover.setVisibility(View.GONE);
            list_item_img_parent.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            music_play_img.setVisibility(View.GONE);
            list_item_img.setImageResource(mReadingSubject.getImgId());
            layout_cover.setOnClickListener(view -> toDetailActivity(type, mReadingSubject));
        } else if (KeyUtil.XimalayaTrack.equals(type)){
            Album mAlbum = JSON.parseObject(json,Album.class);
            item_layout.setVisibility(View.VISIBLE);
            title.setText( mAlbum.getAlbumTitle() );
            type_name.setText( " " + mAlbum.getIncludeTrackCount() + " 集" );
            source_name.setText(context.getString(R.string.xmly_fm));
            videoplayer_cover.setVisibility(View.GONE);
            list_item_img_parent.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            music_play_img.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(mAlbum.getCoverUrlLarge())){
                list_item_img.setImageURI(Uri.parse(mAlbum.getCoverUrlLarge()));
            }else{
                list_item_img.setImageResource(R.color.style6_color2);
            }
            layout_cover.setOnClickListener(view -> toDetailActivity(type, mAlbum));
        }
    }

    private void toDetailActivity(String type, Object object){
        Intent intent = new Intent();
        Class toDetail = null;
        if(AVOUtil.Reading.Reading.equals(type)){
            Reading item = (Reading)object;
            ArrayList<Reading> datas = new ArrayList<>();
            datas.add(item);
            if (!TextUtils.isEmpty(item.getType()) && "video".equals(item.getType())) {
                Setings.dataMap.put(KeyUtil.DataMapKey, item);
                toDetail = ReadDetailTouTiaoActivity.class;
            } else if (!TextUtils.isEmpty(item.getType()) && "mp3".equals(item.getType())) {
                Setings.dataMap.put(KeyUtil.DataMapKey, datas);
                intent.putExtra(KeyUtil.IndexKey, 0);
                if (TextUtils.isEmpty(item.getLrc_url())) {
                    toDetail = ReadingMp3DetailActivity.class;
                } else {
                    toDetail = ReadingDetailLrcActivity.class;
                }
            } else {
                Setings.dataMap.put(KeyUtil.DataMapKey, datas);
                intent.putExtra(KeyUtil.IndexKey, 0);
                toDetail = ReadingDetailActivity.class;
            }
        } else if (AVOUtil.Boutiques.Boutiques.equals(type)) {
            BoutiquesBean mBoutiquesBean = (BoutiquesBean)object;
            toDetail = ReadingsActivity.class;
            intent.putExtra(KeyUtil.ActionbarTitle, mBoutiquesBean.getTitle());
            intent.putExtra(KeyUtil.BoutiqueCode, mBoutiquesBean.getCode());
            intent.putExtra(KeyUtil.ObjectKey,mBoutiquesBean);
        } else if (AVOUtil.SubjectList.SubjectList.equals(type)){
            ReadingSubject mReadingSubject = (ReadingSubject)object;
            toDetail = ReadingsBySubjectActivity.class;
            intent.putExtra(KeyUtil.ActionbarTitle, mReadingSubject.getName());
            intent.putExtra(KeyUtil.SubjectName, mReadingSubject.getName());
            intent.putExtra(KeyUtil.ObjectKey, mReadingSubject);
        } else if (KeyUtil.XimalayaTrack.equals(type)){
            Album mAVObject = (Album)object;
            toDetail = XimalayaTrackListActivity.class;
            intent.putExtra("album_id", mAVObject.getId()+"");
            intent.putExtra("play_times", mAVObject.getPlayCount());
            intent.putExtra("track_count", mAVObject.getIncludeTrackCount());
            intent.putExtra(KeyUtil.JSONData, new Gson().toJson(mAVObject));
            intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getAlbumTitle());
        }
        if (toDetail != null) {
            intent.setClass(context, toDetail);
            context.startActivity(intent);
        }
    }

}
