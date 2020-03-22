package com.messi.languagehelper.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.repositories.TwistaItemRepository;

public class TXAPIViewModel extends BaseViewModel {

    private MutableLiveData<RespoData<TwistaItem>> mTwistaItem = new MutableLiveData<>();
    private TwistaItemRepository mRepo;

    public void init(String apiType){
        if (mRepo == null) {
            mRepo = TwistaItemRepository.getInstance(apiType);
        }
        mRepo.setApiType(apiType);
        isLoading.setValue(false);
        loadData();
    }

    public void loadData(){
        mRepo.getTwistaItem(mTwistaItem, isLoading);
    }

    public LiveData<RespoData<TwistaItem>> getTwistaItem(){
        return mTwistaItem;
    }

}
