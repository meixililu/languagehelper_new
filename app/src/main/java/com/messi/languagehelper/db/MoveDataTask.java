package com.messi.languagehelper.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;

import java.util.List;

public class MoveDataTask {

    public static void moveCaricatureData(final Context mContext){
        final SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        if(!sp.getBoolean(KeyUtil.HasMoveCaricatureData,false)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<CNWBean> dataList = DataBaseUtil.getInstance(mContext).getAllAVObjectData();
                    LogUtil.DefalutLog("moveCaricatureData---dataList:"+dataList.size());
                    if(dataList.size() > 0){
                        BoxHelper.updateCNWBean(dataList);
                        DataBaseUtil.getInstance(mContext).clearAvobject();
                    }
                    Setings.saveSharedPreferences(sp,KeyUtil.HasMoveCaricatureData,true);
                    LogUtil.DefalutLog("moveCaricatureData finish");
                }
            }).start();
        }
    }

//    public static void moveAlldata(final Context mContext){
//        moveRecordData(mContext);
//    }

    public static void moveRecordData(final Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        if(!sp.getBoolean(KeyUtil.HasMoveRDDatas,false)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doRecordDataChange(mContext);
                    doDictionaryDataChange(mContext);
                    doWordDetailListItemDataChange(mContext);
                    Setings.saveSharedPreferences(sp,KeyUtil.HasMoveRDDatas,true);
                }
            }).start();
        }
    }

    public static void doRecordDataChange(Context mContext){
        List<record> dataList = DataBaseUtil.getInstance(mContext).getDataListRecord();
        LogUtil.DefalutLog("HasMoveRecordData---start:"+dataList.size());
        if(NullUtil.isNotEmpty(dataList)){
            getRecordList(dataList);
            DataBaseUtil.getInstance(mContext).clearRecordData();
        }
        LogUtil.DefalutLog("HasMoveRecordData finish");
    }

    public static void doDictionaryDataChange(Context mContext){
        List<Dictionary> dataList = DataBaseUtil.getInstance(mContext).getDataListDictionary();
        LogUtil.DefalutLog("HasMoveDictionaryData---start:"+dataList.size());
        if(NullUtil.isNotEmpty(dataList)){
            getDictionaryList(dataList);
            DataBaseUtil.getInstance(mContext).clearAllDictionary();
        }
        LogUtil.DefalutLog("HasMoveDictionaryData finish");
    }

    public static void doWordDetailListItemDataChange(Context mContext){
        List<WordDetailListItem> dataList = DataBaseUtil.getInstance(mContext).getAllList();
        LogUtil.DefalutLog("HasMoveWordDetailListItemData---start:"+dataList.size());
        if(NullUtil.isNotEmpty(dataList)){
            getWordDetailListItemList(dataList);
            DataBaseUtil.getInstance(mContext).clearAllWordDetailListItem();
        }
        LogUtil.DefalutLog("HasMoveWordDetailListItemData finish");
    }

    public static void getRecordList(List<record> oList){
        for (record oItem : oList){
            Record mRecord = new Record();
            mRecord.setBackup1(oItem.getBackup1());
            mRecord.setBackup2(oItem.getBackup2());
            mRecord.setBackup3(oItem.getBackup3());
            mRecord.setChinese(oItem.getChinese());
            mRecord.setEnglish(oItem.getEnglish());
            mRecord.setIscollected(oItem.getIscollected());
            mRecord.setQuestionAudioPath(oItem.getQuestionAudioPath());
            mRecord.setQuestionVoiceId(oItem.getQuestionVoiceId());
            mRecord.setResultAudioPath(oItem.getResultAudioPath());
            mRecord.setResultVoiceId(oItem.getResultVoiceId());
            mRecord.setSpeak_speed(oItem.getSpeak_speed());
            mRecord.setVisit_times(oItem.getVisit_times());
            mRecord.setSpeak_speed(oItem.getSpeak_speed());
            BoxHelper.update(mRecord);
        }
    }

    public static void getDictionaryList(List<Dictionary> oList){
        for (Dictionary oItem : oList){
            com.messi.languagehelper.box.Dictionary mRecord = new com.messi.languagehelper.box.Dictionary();
            mRecord.setBackup1(oItem.getBackup1());
            mRecord.setBackup2(oItem.getBackup2());
            mRecord.setBackup3(oItem.getBackup3());
            mRecord.setBackup4(oItem.getBackup4());
            mRecord.setBackup5(oItem.getBackup5());
            mRecord.setWord_name(oItem.getWord_name());
            mRecord.setResult(oItem.getResult());
            mRecord.setTo(oItem.getTo_lan());
            mRecord.setFrom(oItem.getFrom());
            mRecord.setPh_am(oItem.getPh_am());
            mRecord.setPh_en(oItem.getPh_en());
            mRecord.setPh_zh(oItem.getPh_zh());
            mRecord.setType(oItem.getType());
            mRecord.setQuestionAudioPath(oItem.getQuestionAudioPath());
            mRecord.setQuestionVoiceId(oItem.getQuestionVoiceId());
            mRecord.setResultAudioPath(oItem.getResultAudioPath());
            mRecord.setResultVoiceId(oItem.getResultVoiceId());
            mRecord.setIscollected(oItem.getIscollected());
            mRecord.setVisit_times(oItem.getVisit_times());
            mRecord.setSpeak_speed(oItem.getSpeak_speed());
            BoxHelper.update(mRecord);
        }
    }

    public static void getWordDetailListItemList(List<WordDetailListItem> oList){
        for (WordDetailListItem oItem : oList){
            com.messi.languagehelper.box.WordDetailListItem item = new com.messi.languagehelper.box.WordDetailListItem();
            item.setBackup1(oItem.getBackup1());
            item.setBackup2(oItem.getBackup2());
            item.setBackup3(oItem.getBackup3());
            item.setClass_id(oItem.getClass_id());
            item.setClass_title(oItem.getClass_title());
            item.setCourse(oItem.getCourse());
            item.setDesc(oItem.getDesc());
            item.setExamples(oItem.getExamples());
            item.setImg_url(oItem.getImg_url());
            item.setIs_study(oItem.getIs_study());
            item.setItem_id(oItem.getItem_id());
            item.setMp3_sdpath(oItem.getMp3_sdpath());
            item.setName(oItem.getName());
            item.setNew_words(oItem.getNew_words());
            item.setSymbol(oItem.getSymbol());
            item.setSound(oItem.getSound());
            BoxHelper.insertData(item);
        }
    }
}
