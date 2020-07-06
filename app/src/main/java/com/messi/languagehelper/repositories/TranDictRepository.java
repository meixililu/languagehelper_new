package com.messi.languagehelper.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.TranslateUtil;
import com.youdao.sdk.ydtranslate.Translate;

import java.util.ArrayList;
import java.util.List;

public class TranDictRepository {

    public MutableLiveData<RespoData<Record>> mRespoData = new MutableLiveData<>();
    public MutableLiveData<RespoData<Dictionary>> mDictRespoData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRefreshTran = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRefreshDict = new MutableLiveData<>();
    private SharedPreferences sp;
    public List<Record> trans;
    public List<Dictionary> dicts;
    public Context context;
    public int tSkip;
    public boolean tNoMoreData;
    public int dSkip;
    public boolean dNoMoreData;

    public TranDictRepository(Context context){
        this.context = context;
        this.trans = new ArrayList<>();
        this.dicts = new ArrayList<>();
        sp = Setings.getSharedPreferences(context);
    }

    public void initSample() {
        boolean IsHasShowBaiduMessage = sp.getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
        if (!IsHasShowBaiduMessage) {
            Record sampleBean = new Record("Click on the microphone to speak", "点击话筒说话");
            BoxHelper.insert(sampleBean);
            trans.add(0, sampleBean);
            Setings.saveSharedPreferences(sp, KeyUtil.IsHasShowBaiduMessage, true);
        }
    }

    public void loadTranData(boolean isRefresh){
        if (isRefresh) {
            trans.clear();
            tSkip = 0;
            tNoMoreData = false;
        }
        if (!tNoMoreData) {
            new Thread(() -> {
                List<Record> list = BoxHelper.getRecordList(tSkip, Setings.RecordOffset);
                if (NullUtil.isNotEmpty(list)) {
                    trans.addAll(list);
                    tSkip += list.size();
                }else {
                    tNoMoreData = true;
                }
                isRefreshTran.postValue(true);
            }).start();
        }
    }

    public void tranDict(){
        if(NetworkUtil.isNetworkConnected(context)){
            LogUtil.DefalutLog("tranDict-online");
            RequestJinShanNewAsyncTask();
        }else {
            LogUtil.DefalutLog("tranDict-offline");
            translateOffline();
        }
    }

    private void RequestJinShanNewAsyncTask(){
        TranslateUtil.Translate(mrecord -> {
            RespoData mData = new RespoData<Record>(1,"");
            if (mrecord != null) {
                mData.setData(mrecord);
                trans.add(0, mrecord);
            } else {
                mData.setCode(0);
                mData.setErrStr(context.getResources().getString(R.string.network_error));
            }
            mRespoData.postValue(mData);
        });
    }

    private void translateOffline(){
        new Thread(() -> {
            Translate mTranslate = TranslateUtil.offlineTranslate();
            parseOfflineData(mTranslate);
        }).start();
    }

    private void parseOfflineData(Translate translate){
        LogUtil.DefalutLog("parseOfflineData:"+translate);
        RespoData mData = new RespoData<Record>(1,"");
        if(translate != null){
            if(translate.getErrorCode() == 0){
                StringBuilder sb = new StringBuilder();
                TranslateUtil.addSymbol(translate,sb);
                for(String tran : translate.getTranslations()){
                    sb.append(tran);
                    sb.append("\n");
                }
                Record mrecord = new Record(sb.substring(0, sb.lastIndexOf("\n")), Setings.q);
                trans.add(0, mrecord);
            }
        }else{
            mData.setCode(0);
            mData.setErrStr("没找到离线词典，请到更多页面下载！");
        }
        mRespoData.postValue(mData);
    }


    //dict-----------------
    public void loadDictData(boolean isRefresh){
        if (isRefresh) {
            dicts.clear();
            dSkip = 0;
            dNoMoreData = false;
        }
        if (!dNoMoreData) {
            List<Dictionary> list = BoxHelper.getDictionaryList(dSkip, Setings.RecordOffset);
            if (NullUtil.isNotEmpty(list)) {
                dicts.addAll(list);
                dSkip += list.size();
            }else {
                dNoMoreData = true;
            }
            isRefreshDict.setValue(true);
        }
    }

    public void getDict(){
        if(NetworkUtil.isNetworkConnected(context)){
            LogUtil.DefalutLog("dict-online");
            RequestTranslateApiTask();
        }else {
            LogUtil.DefalutLog("dict-offline");
            translateOfflineForDict();
        }
    }

    private void RequestTranslateApiTask() {
        TranslateUtil.Translate_init(mDict -> {
            RespoData mData = new RespoData<Dictionary>(1,"");
            if (mDict != null) {
                mData.setData(mDict);
                dicts.add(0, mDict);
                BoxHelper.insert(mDict);
            } else {
                mData.setCode(0);
                mData.setErrStr(context.getResources().getString(R.string.network_error));
            }
            mDictRespoData.postValue(mData);
        });
    }

    private void translateOfflineForDict(){
        new Thread(() -> {
            Translate mTranslate = TranslateUtil.offlineTranslate();
            parseOfflineDictData(mTranslate);
        }).start();
    }

    private void parseOfflineDictData(Translate translate){
        RespoData mData = new RespoData<Dictionary>(1,"");
        if(translate != null){
            if(translate.getErrorCode() == 0){
                StringBuilder sb = new StringBuilder();
                TranslateUtil.addSymbol(translate,sb);
                for(String tran : translate.getTranslations()){
                    sb.append(tran);
                    sb.append("\n");
                }
                Dictionary mDictionaryBean = new Dictionary();
                boolean isEnglish = StringUtils.isEnglish(Setings.q);
                if(isEnglish){
                    mDictionaryBean.setFrom("en");
                    mDictionaryBean.setTo("zh");
                }else{
                    mDictionaryBean.setFrom("zh");
                    mDictionaryBean.setTo("en");
                }
                mDictionaryBean.setWord_name(Setings.q);
                mDictionaryBean.setResult(sb.substring(0, sb.lastIndexOf("\n")));
                dicts.add(0, mDictionaryBean);
                BoxHelper.insert(mDictionaryBean);
            }
        }else{
            mData.setCode(0);
            mData.setErrStr("没找到离线词典，请到更多页面下载！");
        }
        mDictRespoData.postValue(mData);
    }

}
