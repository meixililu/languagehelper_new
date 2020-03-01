package com.messi.languagehelper.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.dao.Dictionary;
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
                    List<CNWBean> dataList = DataBaseUtil.getInstance().getAllAVObjectData();
                    LogUtil.DefalutLog("moveCaricatureData---dataList:"+dataList.size());
                    if(dataList.size() > 0){
                        BoxHelper.updateCNWBean(dataList);
                        DataBaseUtil.getInstance().clearAvobject();
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
                    doRecordDataChange();
                    doDictionaryDataChange();
                    Setings.saveSharedPreferences(sp,KeyUtil.HasMoveRDDatas,true);
                }
            }).start();
        }
    }

    public static void doRecordDataChange(){
        List<record> dataList = DataBaseUtil.getInstance().getDataListRecord();
        LogUtil.DefalutLog("HasMoveRecordData---start:"+dataList.size());
        if(NullUtil.isNotEmpty(dataList)){
            getRecordList(dataList);
            DataBaseUtil.getInstance().clearRecordData();
        }
        LogUtil.DefalutLog("HasMoveRecordData finish");
    }

    public static void doDictionaryDataChange(){
        List<Dictionary> dataList = DataBaseUtil.getInstance().getDataListDictionary();
        LogUtil.DefalutLog("HasMoveDictionaryData---start:"+dataList.size());
        if(NullUtil.isNotEmpty(dataList)){
            getDictionaryList(dataList);
            DataBaseUtil.getInstance().clearAllDictionary();
        }
        LogUtil.DefalutLog("HasMoveDictionaryData finish");
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
}
