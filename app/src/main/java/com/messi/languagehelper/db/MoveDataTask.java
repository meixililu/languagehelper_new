package com.messi.languagehelper.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.box.Record;
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
        final SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        if(!sp.getBoolean(KeyUtil.HasMoveRecordData,false)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<record> dataList = DataBaseUtil.getInstance().getDataListRecord();
                    LogUtil.DefalutLog("HasMoveRecordData---start:"+dataList.size());
                    if(NullUtil.isNotEmpty(dataList)){
                        getRecordList(dataList);
                        DataBaseUtil.getInstance().clearRecordData();
                    }
                    Setings.saveSharedPreferences(sp,KeyUtil.HasMoveRecordData,true);
                    LogUtil.DefalutLog("HasMoveRecordData finish");
                }
            }).start();
        }
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
}
