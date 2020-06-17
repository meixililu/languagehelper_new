package com.messi.languagehelper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.repositories.TranDictRepository;

public class TranDictViewModel extends AndroidViewModel {

    private MutableLiveData<RespoData<Record>> mTranRespoData;
    private MutableLiveData<RespoData<Dictionary>> mDictRespoData;
    private MutableLiveData<Boolean> isRefreshTran;
    private MutableLiveData<Boolean> isRefreshDict;
    private TranDictRepository mRepository;

    public TranDictViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TranDictRepository(getApplication());
        mTranRespoData = mRepository.mRespoData;
        isRefreshTran = mRepository.isRefreshTran;
        isRefreshDict = mRepository.isRefreshDict;
        mDictRespoData = mRepository.mDictRespoData;
    }

    public void initSample() {
        mRepository.initSample();
    }

    public void loadTranData(boolean isRefresh){
        mRepository.loadTranData(isRefresh);
    }

    public void tranDict(){
        mRepository.tranDict();
    }

    public void loadDictData(boolean isRefresh){
        mRepository.loadDictData(isRefresh);
    }

    public void getDict(){
        mRepository.getDict();
    }

    public LiveData<Boolean> isRefreshTran(){
        return isRefreshTran;
    }

    public LiveData<RespoData<Record>> getTranRespoData(){
        return mTranRespoData;
    }

    public LiveData<Boolean> isRefreshDict(){
        return isRefreshDict;
    }

    public LiveData<RespoData<Dictionary>> getDictRespoData(){
        return mDictRespoData;
    }

    public TranDictRepository getRepository(){
        return mRepository;
    }

}
