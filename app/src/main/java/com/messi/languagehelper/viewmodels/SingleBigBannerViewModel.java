package com.messi.languagehelper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.text.TextUtils;

import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.bean.ADBean;
import com.messi.languagehelper.repositories.ADRepository;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.LogUtil;

public class SingleBigBannerViewModel extends ViewModel {

    private MutableLiveData<ADBean> mADBean;
    private ADRepository mRepo;

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
