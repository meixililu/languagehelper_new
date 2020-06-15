package com.messi.languagehelper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.repositories.TranDictRepository;

public class TranDictViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<RespoData<Record>> mRespoData;
    private MutableLiveData<Boolean> isRefreshTran;
    private TranDictRepository mRepository;

    public TranDictViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TranDictRepository(getApplication());
        isLoading = mRepository.isLoading;
        mRespoData = mRepository.mRespoData;
        isRefreshTran = mRepository.isRefreshTran;
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

    public void dict(){
        mRepository.dict();
    }

    public LiveData<Boolean> isShowProgressBar(){
        return isLoading;
    }

    public LiveData<Boolean> isRefreshTran(){
        return isRefreshTran;
    }

    public LiveData<RespoData<Record>> getRespoData(){
        return mRespoData;
    }

    public TranDictRepository getRepository(){
        return mRepository;
    }

}
