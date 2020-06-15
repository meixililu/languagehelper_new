package com.messi.languagehelper.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TranslateUtil;
import com.youdao.sdk.ydtranslate.Translate;

import java.util.ArrayList;
import java.util.List;

public class TranDictRepository {

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<RespoData<Record>> mRespoData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRefreshTran = new MutableLiveData<>();
    private SharedPreferences sp;
    public List<Record> beans;
    public Context context;
    public int skip;
    public boolean noMoreData;

    public TranDictRepository(Context context){
        this.context = context;
        this.beans = new ArrayList<Record>();
        sp = Setings.getSharedPreferences(context);
    }

    public void initSample() {
        boolean IsHasShowBaiduMessage = sp.getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
        if (!IsHasShowBaiduMessage) {
            Record sampleBean = new Record("Click the mic to speak", "点击话筒说话");
            BoxHelper.insert(sampleBean);
            beans.add(0, sampleBean);
            Setings.saveSharedPreferences(sp, KeyUtil.IsHasShowBaiduMessage, true);
        }
    }

    public void loadTranData(boolean isRefresh){
        if (isRefresh) {
            beans.clear();
            skip = 0;
            noMoreData = false;
        }
        if (!noMoreData) {
            List<Record> list = BoxHelper.getRecordList(skip, Setings.RecordOffset);
            if (NullUtil.isNotEmpty(list)) {
                beans.addAll(list);
                skip += list.size();
            }else {
                noMoreData = true;
            }
            isRefreshTran.setValue(true);
        }
    }

    public void tranDict(){
        if(NetworkUtil.isNetworkConnected(context)){
            LogUtil.DefalutLog("online");
            try {
                RequestJinShanNewAsyncTask();
            } catch (Exception e) {
                LogUtil.DefalutLog("online exception");
                e.printStackTrace();
            }
        }else {
            LogUtil.DefalutLog("offline");
            translateOffline();
        }
    }

    private void RequestJinShanNewAsyncTask() throws Exception{
        TranslateUtil.Translate(mrecord -> {
            RespoData mData = new RespoData<Record>(1,"");
            if (mrecord != null) {
                mData.setData(mrecord);
                beans.add(0, mrecord);
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
                beans.add(0, mrecord);
            }
        }else{
            mData.setCode(0);
            mData.setErrStr("没找到离线词典，请到更多页面下载！");
        }
        mRespoData.postValue(mData);
    }

    public void dict(){

    }

}
