package com.messi.languagehelper.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.repositories.ADXXLRepository;
import com.messi.languagehelper.repositories.ReadingListRepository;
import com.messi.languagehelper.repositories.XXLReadingRepository;

public class ReadingListViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<RespoData> mRespoData;
    private MutableLiveData<RespoADData> mRespoADData;
    public MutableLiveData<Integer> mMutaCount;
    private ReadingListRepository mRepo;
    private ADXXLRepository mADRepo;

    public void init(){
        mRepo = new ReadingListRepository();
        mRespoData = mRepo.mRespoData;
        isLoading = mRepo.isLoading;
        mMutaCount = mRepo.mMutaCount;
        isLoading.setValue(false);

        mADRepo = new XXLReadingRepository(mRepo.list);
        mRespoADData = mADRepo.mRespoData;
        mRepo.setADXXLRepository(mADRepo);
    }

    public void refresh(int skip){
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mADRepo != null) {
            mADRepo.onDestroy();
        }
    }
}
