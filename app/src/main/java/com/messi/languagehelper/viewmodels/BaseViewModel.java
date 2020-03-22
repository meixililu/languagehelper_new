package com.messi.languagehelper.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> isShowProgressBar(){
        return isLoading;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        isLoading = null;
    }
}
