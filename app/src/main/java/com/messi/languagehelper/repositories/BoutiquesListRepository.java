package com.messi.languagehelper.repositories;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.CountCallback;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class BoutiquesListRepository {

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<RespoData> mRespoData = new MutableLiveData<>();
    private List<BoutiquesBean> list = new ArrayList<>();
    private String type;
    private String category;
    private int maxRandom;
    private int skip;
    private boolean isNeedClear = false;
    private boolean hasMore = true;
    private boolean loading = false;
    private XXLBoutiquesRepository mADXXLRepository;

    public BoutiquesListRepository(){}

    public void getDataList(){
        try {
            if (!loading) {
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh(){
        try {
            if (!loading) {
                random();
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void random(){
        isNeedClear = true;
        hasMore = true;
        skip = (int) Math.round(Math.random() * maxRandom);
        LogUtil.DefalutLog("random:"+skip);
    }

    private void loadAD(boolean isShowAd){
        if (mADXXLRepository != null) {
            mADXXLRepository.showAd(isShowAd);
        }
    }

    public void loadData() throws Exception{
        LogUtil.DefalutLog("loadData---start");
        if(loading || !hasMore){
            return;
        }
        loading = true;
        isLoading.setValue(true);
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Boutiques.Boutiques);
        if(!TextUtils.isEmpty(category)){
            query.whereEqualTo(AVOUtil.Boutiques.category,category);
        }
        if(!TextUtils.isEmpty(type)){
            query.whereEqualTo(AVOUtil.Boutiques.type,type);
        }
        query.orderByAscending(AVOUtil.Boutiques.order);
        query.orderByDescending(AVOUtil.Boutiques.views);
        query.skip(skip);
        query.limit(Setings.page_size);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObject, AVException avException) {
                LogUtil.DefalutLog("loadData:"+avObject+"---AVException:"+avException);
                isLoading.setValue(false);
                loading = false;
                RespoData mData = new RespoData();
                if(avObject != null){
                    mData.setCode(1);
                    if(avObject.size() == 0){
                        mData.setHideFooter(true);
                        hasMore = false;
                    }else{
                        if(skip == 0){
                            list.clear();
                        }
                        if(isNeedClear){
                            isNeedClear = false;
                            list.clear();
                        }
                        mData.setPositionStart(list.size());
                        mData.setItemCount(avObject.size());
                        DataUtil.changeToBoutiquesBean(avObject,list);
                        loadAD(true);
                        if(avObject.size() == Setings.page_size){
                            skip += Setings.page_size;
                            hasMore = true;
                        }else {
                            hasMore = false;
                        }
                        mData.setHideFooter(!hasMore);
                    }
                }else{
                    mData.setCode(0);
                    mData.setHideFooter(false);
                    mData.setErrStr("加载失败，下拉可刷新");
                }
                mRespoData.setValue(mData);
            }
        }));
    }

    public void count(){
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Boutiques.Boutiques);
        if(!TextUtils.isEmpty(category)){
            query.whereEqualTo(AVOUtil.Boutiques.category,category);
        }
        if(!TextUtils.isEmpty(type)){
            query.whereEqualTo(AVOUtil.Boutiques.type,type);
        }
        query.countInBackground().subscribe(ObserverBuilder.buildSingleObserver(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                maxRandom = count-30;
            }
        }));
    }

    public List<BoutiquesBean> getList() {
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isNeedClear() {
        return isNeedClear;
    }

    public void setNeedClear(boolean needClear) {
        isNeedClear = needClear;
    }

    public int getMaxRandom() {
        return maxRandom;
    }

    public void setMaxRandom(int maxRandom) {
        this.maxRandom = maxRandom;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setADXXLRepository(XXLBoutiquesRepository mADXXLRepository) {
        this.mADXXLRepository = mADXXLRepository;
    }
}
