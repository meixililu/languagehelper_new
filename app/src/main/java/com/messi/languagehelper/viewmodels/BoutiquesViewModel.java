package com.messi.languagehelper.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.repositories.BoutiquesListRepository;
import com.messi.languagehelper.repositories.XXLAVObjectRepository;

public class BoutiquesViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<RespoData> mRespoData;
    private MutableLiveData<RespoADData> mRespoADData;
    private BoutiquesListRepository mRepo;
    private XXLAVObjectRepository mADRepo;

    public void init(){
        mRepo = new BoutiquesListRepository();
        mRespoData = mRepo.mRespoData;
        isLoading = mRepo.isLoading;
        isLoading.setValue(false);

        mADRepo = new XXLAVObjectRepository(mRepo.getList());
        mRespoADData = mADRepo.mRespoData;
        mRepo.setADXXLRepository(mADRepo);
    }

    public void loadData(){
        mRepo.getDataList();
    }

    public void refresh(){
        mRepo.refresh();
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

    public BoutiquesListRepository getRepo(){
        return mRepo;
    }
}
