package com.messi.languagehelper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
