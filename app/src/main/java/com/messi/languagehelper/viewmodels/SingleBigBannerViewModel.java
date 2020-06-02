package com.messi.languagehelper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;

import com.messi.languagehelper.bean.ADBean;
import com.messi.languagehelper.repositories.ADRepository;

public class SingleBigBannerViewModel extends ViewModel {

    private MutableLiveData<ADBean> mADBean;
    private ADRepository mRepo;
    private Context context;

    public void init(Context context){
        mRepo = new ADRepository(context);
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
