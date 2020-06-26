package com.messi.languagehelper.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CollectedData;
import com.messi.languagehelper.box.ReadingSubject;
import com.messi.languagehelper.repositories.ReadingListRepository;
import com.messi.languagehelper.repositories.XXLReadingRepository;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

public class ReadingListViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<RespoData> mRespoData;
    private MutableLiveData<RespoADData> mRespoADData;
    private MutableLiveData<Integer> mMutaCount;
    private ReadingListRepository mRepo;
    private XXLReadingRepository mADRepo;

    public void init(Context context){
        mRepo = new ReadingListRepository();
        mRespoData = mRepo.mRespoData;
        isLoading = mRepo.isLoading;
        mMutaCount = mRepo.mMutaCount;
        isLoading.setValue(false);

        mADRepo = new XXLReadingRepository(context,mRepo.list);
        mRespoADData = mADRepo.mRespoData;
        mRepo.setADXXLRepository(mADRepo);
    }

    public void refresh(int skip){
        mRepo.setNeedClear(true);
        mRepo.setHasMore(true);
        mRepo.setSkip(skip);
        mRepo.getReadingList();
    }

    public void loadData(){
        mRepo.getReadingList();
    }

    public void count(){
        mRepo.count();
    }

    public LiveData<Boolean> isShowProgressBar(){
        return isLoading;
    }

    public LiveData<RespoData> getReadingList(){
        return mRespoData;
    }

    public LiveData<RespoADData> getAD(){
        return mRespoADData;
    }

    public LiveData<Integer> getCount(){
        return mMutaCount;
    }

    public ReadingListRepository getRepo(){
        return mRepo;
    }

    public void collectData(boolean tag, ReadingSubject mReadingSubject){
        new Thread(() -> {
            CollectedData cdata = new CollectedData();
            if(tag){
                cdata.setObjectId(mReadingSubject.getObjectId());
                cdata.setName(mReadingSubject.getName());
                cdata.setType(AVOUtil.SubjectList.SubjectList);
                cdata.setJson(JSON.toJSONString(mReadingSubject));
                BoxHelper.insert(cdata);
            }else {
                cdata.setObjectId(mReadingSubject.getObjectId());
                BoxHelper.remove(cdata);
            }
            LiveEventBus.get(KeyUtil.UpdateCollectedData).post("");
        }).start();
    }

}
