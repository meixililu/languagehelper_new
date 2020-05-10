package com.messi.languagehelper.util;

import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.ReadingCategory;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.box.ReadingSubject;
import com.messi.languagehelper.box.WebFilter;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataUtil {

    public static List<CNWBean> toCNWBeanList(List<AVObject> list){
        List<CNWBean> beans = new ArrayList<CNWBean>();
        for(AVObject mAVObject : list){
            beans.add(AVObjectToCNWBean(mAVObject));
        }
        return beans;
    }

    public static CNWBean AVObjectToCNWBean(AVObject mAVObject){
        CNWBean bean = null;
        bean = BoxHelper.findCNWBeanByItemId(mAVObject.getObjectId());
        if(bean == null){
            bean = new CNWBean();
            bean.setTable(AVOUtil.Caricature.Caricature);
        }
        bean.setItemId(mAVObject.getObjectId());
        bean.setRead_url(mAVObject.getString(AVOUtil.Caricature.read_url));
        bean.setTitle(mAVObject.getString(AVOUtil.Caricature.name));
        bean.setDes(mAVObject.getString(AVOUtil.Caricature.des));
        bean.setAuthor(mAVObject.getString(AVOUtil.Caricature.author));
        bean.setImg_url(mAVObject.getString(AVOUtil.Caricature.book_img_url));
        bean.setView(mAVObject.getNumber(AVOUtil.Caricature.views).longValue());
        bean.setTag(mAVObject.getString(AVOUtil.Caricature.tag));
        bean.setSource_url(mAVObject.getString(AVOUtil.Caricature.url));
        bean.setSource_name(mAVObject.getString(AVOUtil.Caricature.source_name));
        bean.setIs_free(mAVObject.getString(AVOUtil.Caricature.isFree));
        bean.setUpdate_des(mAVObject.getString(AVOUtil.Caricature.update));
        bean.setType(mAVObject.getString(AVOUtil.Caricature.type));
        bean.setCategory(mAVObject.getString(AVOUtil.Caricature.category));
        return bean;
    }

    public static List<WebFilter> toWebFilter(List<AVObject> list){
        List<WebFilter> beans = new ArrayList<WebFilter>();
        for(AVObject mAVObject : list){
            beans.add(AVObjectToWebFilter(mAVObject));
        }
        return beans;
    }

    public static WebFilter AVObjectToWebFilter(AVObject mAVObject){
        WebFilter bean = new WebFilter();
        bean.setName(mAVObject.getString(AVOUtil.AdFilter.name));
        bean.setAd_filter(mAVObject.getString(AVOUtil.AdFilter.filter));
        bean.setAd_url(mAVObject.getString(AVOUtil.AdFilter.ad_url));
        bean.setIs_show_ad(mAVObject.getString(AVOUtil.AdFilter.isShowAd));
        return bean;
    }

    public static void changeDataToReading(List<AVObject> avObjectlist, List<Reading> avObjects, boolean isAddToHead) {
        changeDataToReading(avObjectlist, avObjects, isAddToHead, "");
    }

    public static void changeDataToReading(List<AVObject> avObjectlist, List<Reading> avObjects, boolean isAddToHead,String subjectName) {
        for (AVObject item : avObjectlist) {
            Reading mReading = new Reading();
            mReading.setObject_id(item.getObjectId());
            if(item.has(AVOUtil.Reading.category)){
                mReading.setCategory(item.getString(AVOUtil.Reading.category));
            }
            if(item.has(AVOUtil.Reading.category_2)){
                mReading.setCategory_2(item.getString(AVOUtil.Reading.category_2));
            }
            if(item.has(AVOUtil.Reading.content)){
                mReading.setContent(item.getString(AVOUtil.Reading.content));
            }
            if(item.has(AVOUtil.Reading.type_id)){
                mReading.setType_id(item.getString(AVOUtil.Reading.type_id));
            }
            if(item.has(AVOUtil.Reading.type_name)){
                mReading.setType_name(item.getString(AVOUtil.Reading.type_name));
            }
            if(item.has(AVOUtil.Reading.title)){
                mReading.setTitle(item.getString(AVOUtil.Reading.title));
            }
            if(item.has(AVOUtil.Reading.vid)){
                mReading.setVid(item.getString(AVOUtil.Reading.vid));
            }
            if(item.has(AVOUtil.Reading.item_id)){
                mReading.setItem_id(String.valueOf(item.getNumber(AVOUtil.Reading.item_id)));
            }
            if(item.has(AVOUtil.Reading.img_url)){
                mReading.setImg_url(item.getString(AVOUtil.Reading.img_url));
            }
            if(item.has(AVOUtil.Reading.publish_time)){
                mReading.setPublish_time(String.valueOf(item.getDate(AVOUtil.Reading.publish_time).getTime()));
            }
            if(item.has(AVOUtil.Reading.img_type)){
                mReading.setImg_type(item.getString(AVOUtil.Reading.img_type));
            }
            if(item.has(AVOUtil.Reading.source_name)){
                mReading.setSource_name(item.getString(AVOUtil.Reading.source_name));
            }
            if(item.has(AVOUtil.Reading.source_url)){
                mReading.setSource_url(item.getString(AVOUtil.Reading.source_url));
            }
            if(item.has(AVOUtil.Reading.type)){
                mReading.setType(item.getString(AVOUtil.Reading.type));
            }
            if(item.has(AVOUtil.Reading.boutique_code)){
                mReading.setBoutique_code(item.getString(AVOUtil.Reading.boutique_code));
            }
            if(item.has(AVOUtil.Reading.media_url)){
                mReading.setMedia_url(item.getString(AVOUtil.Reading.media_url));
            }
            if(item.has(AVOUtil.Reading.content_type)){
                mReading.setContent_type(item.getString(AVOUtil.Reading.content_type));
            }
            if(item.has(AVOUtil.Reading.lrc_url)){
                mReading.setLrc_url(item.getString(AVOUtil.Reading.lrc_url));
            }
            if (!TextUtils.isEmpty(subjectName)) {
                mReading.setBackup2(subjectName);
            }
            BoxHelper.saveOrGetStatus(mReading);
            if (isAddToHead) {
                avObjects.add(0, mReading);
            } else {
                avObjects.add(mReading);
            }
        }
    }

    public static void changeBoutiquesListToReading(List<AVObject> avObjectlist, List<Reading> avObjects, boolean isAddToHead) {
        for (AVObject item : avObjectlist) {
            Reading mReading = new Reading();
            mReading.setObject_id(item.getObjectId());
            if(item.has(AVOUtil.BoutiquesList.bcdoe)){
                mReading.setBoutique_code(item.getString(AVOUtil.BoutiquesList.bcdoe));
            }
            if(item.has(AVOUtil.BoutiquesList.des)){
                mReading.setContent(item.getString(AVOUtil.BoutiquesList.des));
            }
            if(item.has(AVOUtil.BoutiquesList.type_name)){
                mReading.setType_name(item.getString(AVOUtil.BoutiquesList.type_name));
            }
            if(item.has(AVOUtil.BoutiquesList.title)){
                mReading.setTitle(item.getString(AVOUtil.BoutiquesList.title));
            }
            if(item.has(AVOUtil.BoutiquesList.content_type)){
                mReading.setContent_type(item.getString(AVOUtil.BoutiquesList.content_type));
            }
            if(item.has(AVOUtil.BoutiquesList.vid)){
                mReading.setVid(item.getString(AVOUtil.BoutiquesList.vid));
            }
            if(item.has(AVOUtil.BoutiquesList.img)){
                mReading.setImg_url(item.getString(AVOUtil.BoutiquesList.img));
            }
            if(item.has(AVOUtil.BoutiquesList.source_name)){
                mReading.setSource_name(item.getString(AVOUtil.BoutiquesList.source_name));
            }
            if(item.has(AVOUtil.BoutiquesList.source_url)){
                mReading.setSource_url(item.getString(AVOUtil.BoutiquesList.source_url));
            }
            if(item.has(AVOUtil.BoutiquesList.type)){
                mReading.setType(item.getString(AVOUtil.BoutiquesList.type));
            }
            if(item.has(AVOUtil.BoutiquesList.media_url)){
                mReading.setMedia_url(item.getString(AVOUtil.BoutiquesList.media_url));
            }
            BoxHelper.saveOrGetStatus(mReading);
            if (isAddToHead) {
                avObjects.add(0, mReading);
            } else {
                avObjects.add(mReading);
            }
        }
    }

    public static List<Track> getTrackList(Reading reading){
        Track track = new Track();
        track.setKind(PlayableModel.KIND_TRACK);
        track.setTrackTitle(reading.getTitle());
        track.setPlayUrl32(reading.getMedia_url());
        track.setDataId(-1 * UUID.randomUUID().hashCode());
        List<Track> tracks = new ArrayList<Track>();
        tracks.add(track);
        return tracks;
    }

    public static List<Track> getTrackList(List<Reading> readings){
        List<Track> tracks = new ArrayList<Track>();
        for(Reading reading : readings){
            if (!TextUtils.isEmpty(reading.getTitle())) {
                Track track = new Track();
                track.setKind(PlayableModel.KIND_TRACK);
                track.setTrackTitle(reading.getTitle());
                track.setPlayUrl32(reading.getMedia_url());
                track.setDataId(-1 * UUID.randomUUID().hashCode());
                tracks.add(track);
            }
        }
        return tracks;
    }

    public static ReadingSubject toReadingSubject(AVObject mAVObject){
        ReadingSubject item = new ReadingSubject();
        item.setObjectId(mAVObject.getObjectId());
        item.setCategory(mAVObject.getString(AVOUtil.SubjectList.category));
        item.setCode(mAVObject.getString(AVOUtil.SubjectList.code));
        item.setName(mAVObject.getString(AVOUtil.SubjectList.name));
        item.setLevel(mAVObject.getString(AVOUtil.SubjectList.level));
        item.setViews(mAVObject.getInt(AVOUtil.SubjectList.views));
        item.setSource_name(mAVObject.getString(AVOUtil.SubjectList.source_name));
        item.setSource_url(mAVObject.getString(AVOUtil.SubjectList.source_url));
        return item;
    }

    public static List<ReadingCategory> getTabItem(Context context) {
        List<ReadingCategory> readingCategories = new ArrayList<ReadingCategory>();
        readingCategories.add(new ReadingCategory(context.getString(R.string.recommend), ""));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_video), "video"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_listening), "listening"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.spoken_english_practice), "spoken_english"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.reading), "shuangyu_reading"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_word_study_vocabulary), "word"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_composition), "composition"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.examination), "examination"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_story), "story"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_jokes), "jokes"));
        return readingCategories;
    }

    public static List<ReadingCategory> getStudyTab(Context context) {
        List<ReadingCategory> readingCategories = new ArrayList<ReadingCategory>();
        readingCategories.add(new ReadingCategory(context.getString(R.string.recommend), "recommend"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_video), "video"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.selection), "jingxuan"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.xmly_album), "zhuanji"));
//        readingCategories.add(new ReadingCategory(context.getString(R.string.title_xvideo_yys), "xvideo"));
        return readingCategories;
    }
}
