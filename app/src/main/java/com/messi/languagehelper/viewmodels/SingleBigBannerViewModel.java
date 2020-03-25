package com.messi.languagehelper.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.messi.languagehelper.bean.ADBean;
import com.messi.languagehelper.repositories.ADRepository;

public class SingleBigBannerViewModel extends ViewModel {

    private MutableLiveData<ADBean> mADBean;
    private ADRepository mRepo;

    public void init(){
        mRepo = new ADRepository();
        mADBean = mRepo.mItem;
        loadData();
    }

    public void loadData(){
        mRepo.counter = 0;
        mRepo.getSBBAD();
    }

    public LiveData<ADBean> getADBean(){
        return mADBean;
    }


}
