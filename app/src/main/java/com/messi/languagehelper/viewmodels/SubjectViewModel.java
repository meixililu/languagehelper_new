package com.messi.languagehelper.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.repositories.SubjectRepository;
import com.messi.languagehelper.repositories.XXLAVObjectRepository;
import com.messi.languagehelper.util.LogUtil;

public class SubjectViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<RespoData> mRespoData;
    private MutableLiveData<RespoADData> mRespoADData;
    private SubjectRepository mRepo;
    private XXLAVObjectRepository mADRepo;

    public void init(){
        mRepo = new SubjectRepository();
        mRespoData = mRepo.mRespoData;
        isLoading = mRepo.isLoading;
        isLoading.setValue(false);

        mADRepo = new XXLAVObjectRepository(mRepo.list);
        mRespoADData = mADRepo.mRespoData;
        mRepo.setADXXLRepository(mADRepo);
    }

    public void refresh(){
        int skip = (int) Math.round(Math.random() * mRepo.getMaxRandom());
        LogUtil.DefalutLog("skip:"+skip);
        mRepo.setNeedClear(true);
        mRepo.setHasMore(true);
        mRepo.setSkip(skip);
        mRepo.getDataList();
    }

    public void loadAdPre(){
        if (mADRepo != null && mRepo != null && mRepo.isHasMore()) {
            if (mADRepo.mADObject == null && !mADRepo.isLoading) {
                LogUtil.DefalutLog("SubjectViewModel---loadAdPre");
                mADRepo.showAd(false);
            }
        }
    }

    public void loadData(){
        mRepo.getDataList();
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

    public SubjectRepository getRepo(){
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
