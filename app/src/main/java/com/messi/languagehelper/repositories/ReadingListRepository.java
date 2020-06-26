package com.messi.languagehelper.repositories;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.Reading;
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

public class ReadingListRepository {

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<RespoData> mRespoData = new MutableLiveData<>();
    public MutableLiveData<Integer> mMutaCount = new MutableLiveData<>();
    public List<Reading> list = new ArrayList<>();
    private String category;
    private String subjectName;
    private String type;
    private String source;
    private String boutique_code;
    private String quest;
    private String code;
    private String level;
    private boolean withOutVideo;
    private boolean orderById;
    private boolean isNeedClear = false;
    private int maxRandom;
    private int skip;
    private int total;
    private boolean isDesc;
    private boolean hasMore = true;
    private boolean loading = false;
    private XXLReadingRepository mADXXLRepository;

    public ReadingListRepository(){
    }

    public void getReadingList(){
        try {
            if (!isLoading.getValue()) {
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        LogUtil.DefalutLog("subjectName:"+subjectName);
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
        if(!TextUtils.isEmpty(category)){
            query.whereEqualTo(AVOUtil.Reading.category, category);
        }
        if (!TextUtils.isEmpty(subjectName)) {
            query.whereEqualTo(AVOUtil.Reading.category_2, subjectName);
        }
        if(!TextUtils.isEmpty(level)){
            query.whereEqualTo(AVOUtil.Reading.level, level);
        }
        if(!TextUtils.isEmpty(type)){
            query.whereEqualTo(AVOUtil.Reading.type, type);
        }
        if(!TextUtils.isEmpty(source)){
            query.whereEqualTo(AVOUtil.Reading.source_name, source);
        }
        if(!TextUtils.isEmpty(boutique_code)){
            query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
        }
        if(!TextUtils.isEmpty(quest)){
            query.whereContains(AVOUtil.Reading.title, quest);
        }
        if(!TextUtils.isEmpty(boutique_code)){
            query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
        }
        if(!TextUtils.isEmpty(code)){
            if(!code.equals("1000")){
                query.whereEqualTo(AVOUtil.Reading.type_id, code);
            }
        }
        if (withOutVideo) {
            query.whereNotEqualTo(AVOUtil.Reading.type, "video");
        }
        if (orderById) {
            if (isDesc) {
                query.addDescendingOrder(AVOUtil.Reading.item_id);
            } else {
                query.addAscendingOrder(AVOUtil.Reading.item_id);
            }
        }else {
            query.addDescendingOrder(AVOUtil.Reading.publish_time);
        }
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
                        DataUtil.changeDataToReading(avObject,list,false,subjectName);
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
                    mData.setHideFooter(true);
                    mData.setErrStr("加载失败，下拉可刷新");
                }
                mRespoData.setValue(mData);
            }
        }));
    }

    public void count(){
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
        if(!TextUtils.isEmpty(category)){
            query.whereEqualTo(AVOUtil.Reading.category, category);
        }
        if (!TextUtils.isEmpty(subjectName)) {
            query.whereEqualTo(AVOUtil.Reading.category_2, subjectName);
        }
        if(!TextUtils.isEmpty(level)){
            query.whereEqualTo(AVOUtil.Reading.level, level);
        }
        if(!TextUtils.isEmpty(type)){
            query.whereEqualTo(AVOUtil.Reading.type, type);
        }
        if(!TextUtils.isEmpty(source)){
            query.whereEqualTo(AVOUtil.Reading.source_name, source);
        }
        if(!TextUtils.isEmpty(boutique_code)){
            query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
        }
        if(!TextUtils.isEmpty(quest)){
            query.whereContains(AVOUtil.Reading.title, quest);
        }
        if(!TextUtils.isEmpty(boutique_code)){
            query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
        }
        if(!TextUtils.isEmpty(code)){
            if(!code.equals("1000")){
                query.whereEqualTo(AVOUtil.Reading.type_id, code);
            }
        }
        query.countInBackground().subscribe(ObserverBuilder.buildSingleObserver(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                total = count;
                maxRandom = count/10;
                mMutaCount.setValue(count);
            }
        }));
    }

    public ADXXLRepository getmADXXLRepository() {
        return mADXXLRepository;
    }

    public void setADXXLRepository(XXLReadingRepository mADXXLRepository) {
        this.mADXXLRepository = mADXXLRepository;
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

    public List<Reading> getList() {
        return list;
    }

    public void setList(List<Reading> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBoutique_code() {
        return boutique_code;
    }

    public void setBoutique_code(String boutique_code) {
        this.boutique_code = boutique_code;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public boolean isWithOutVideo() {
        return withOutVideo;
    }

    public void setWithOutVideo(boolean withOutVideo) {
        this.withOutVideo = withOutVideo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean getOrderById() {
        return orderById;
    }

    public void setOrderById(boolean orderById) {
        this.orderById = orderById;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setDesc(boolean desc) {
        isDesc = desc;
    }

    public void setNeedClear(boolean needClear) {
        isNeedClear = needClear;
    }

    public void setMaxRandom(int maxRandom) {
        this.maxRandom = maxRandom;
    }
}
