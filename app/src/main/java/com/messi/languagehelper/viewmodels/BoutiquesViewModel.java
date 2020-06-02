package com.messi.languagehelper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;

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
    private Context context;

    public void init(Context context){
        mRepo = new BoutiquesListRepository();
        mRespoData = mRepo.mRespoData;
        isLoading = mRepo.isLoading;
        isLoading.setValue(false);

        mADRepo = new XXLAVObjectRepository(context,mRepo.getList());
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
