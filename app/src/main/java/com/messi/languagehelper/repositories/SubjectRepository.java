package com.messi.languagehelper.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.util.ArrayList;
import java.util.List;

public class SubjectRepository {

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<RespoData> mRespoData = new MutableLiveData<>();
    public List<AVObject> list = new ArrayList<>();
    private String category;
    private String level;
    private String order;
    private int skip;
    private int maxRandom;
    private boolean isNeedClear;
    private boolean hasMore = true;
    private boolean loading = false;
    private XXLAVObjectRepository mADXXLRepository;

    public SubjectRepository(){
    }

    public void getDataList(){
        try {
            if (!isLoading.getValue()) {
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAD(boolean isShowAd){
        LogUtil.DefalutLog("loadAD");
        if (mADXXLRepository != null) {
            mADXXLRepository.showAd(isShowAd);
        }
    }

    public void loadData() throws Exception{
        LogUtil.DefalutLog("loadData");
        if(loading || !hasMore){
            return;
        }
        loading = true;
        isLoading.setValue(true);
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectList.SubjectList);
        if (!TextUtils.isEmpty(category)) {
            query.whereEqualTo(AVOUtil.SubjectList.category, category);
        }
        if (!TextUtils.isEmpty(level)) {
            query.whereEqualTo(AVOUtil.SubjectList.level, level);
        }
        if(!TextUtils.isEmpty(order)){
            query.orderByDescending(AVOUtil.SubjectList.order);
        }else {
            query.orderByAscending(AVOUtil.SubjectList.order);
        }
        query.orderByDescending(AVOUtil.SubjectList.views);
        query.skip(skip);
        query.limit(Setings.page_size);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObject, AVException avException) {
                LogUtil.DefalutLog("loadData:avObject"+"---AVException:"+avException);
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
                        addBgColor(avObject);
                        list.addAll(avObject);
//                        if (mADXXLRepository.mADObject != null) {
//                            list.add(mADXXLRepository.getIndex(), mADXXLRepository.mADObject);
//                            mADXXLRepository.mADObject = null;
//                            mData.setItemCount(avObject.size()+1);
//                        }else {
                            loadAD(true);
//                        }
                        if(avObject.size() == Setings.page_size){
                            skip += Setings.page_size;
                            mData.setHideFooter(false);
                            hasMore = true;
                        }else {
                            hasMore = false;
                            mData.setHideFooter(true);
                        }
                    }
                }else{
                    mData.setCode(0);
                    mData.setHideFooter(false);
                    mData.setErrStr("加载失败，下拉可刷新");
                }
                mRespoData.setValue(mData);
            }
        });
    }

    private void addBgColor(List<AVObject> avObject){
        for (AVObject item : avObject){
            item.put(KeyUtil.ColorKey, ColorUtil.getRadomColor());
        }
    }

    public void count(){
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectList.SubjectList);
        if (!TextUtils.isEmpty(category)) {
            query.whereEqualTo(AVOUtil.SubjectList.category, category);
        }
        if (!TextUtils.isEmpty(level)) {
            query.whereEqualTo(AVOUtil.SubjectList.level, level);
        }
        if(!TextUtils.isEmpty(order)){
            query.orderByDescending(AVOUtil.SubjectList.order);
        }else {
            query.orderByAscending(AVOUtil.SubjectList.order);
        }
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                maxRandom = count/10;
            }
        });
    }

    public ADXXLRepository getmADXXLRepository() {
        return mADXXLRepository;
    }

    public void setADXXLRepository(XXLAVObjectRepository mADXXLRepository) {
        this.mADXXLRepository = mADXXLRepository;
    }

    public int getMaxRandom() {
        return maxRandom;
    }

    public void setMaxRandom(int maxRandom) {
        this.maxRandom = maxRandom;
    }

    public void setNeedClear(boolean needClear) {
        isNeedClear = needClear;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean isLoading() {
        return loading;
    }

    public List<AVObject> getList() {
        return list;
    }

    public void setList(List<AVObject> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

}
