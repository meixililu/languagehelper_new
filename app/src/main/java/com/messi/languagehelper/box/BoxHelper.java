package com.messi.languagehelper.box;

import android.text.TextUtils;

import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class BoxHelper {

    public static BoxStore boxStore;

    public static void init(BaseApplication app){
        try {
            boxStore = MyObjectBox.builder().androidContext(app).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BoxStore getBoxStore(){
        if(boxStore == null){
            init(BaseApplication.mInstance);
        }
        return boxStore;
    }

    /**CNWBean**/
    public static Box<CNWBean> getCNWBeanBox(){
        return getBoxStore().boxFor(CNWBean.class);
    }

    public static void updateCNWBean(CNWBean bean){
        if(bean != null && !TextUtils.isEmpty(bean.getItemId())){
            CNWBean oldData = findCNWBeanByItemId(bean.getItemId());
            if(oldData != null){
                bean.setId(oldData.getId());
            }
        }
        long id = getCNWBeanBox().put(bean);
        bean.setId(id);
        LogUtil.DefalutLog("updateCNWBean:"+bean.toString());
    }

    public static void updateCNWBean(List<CNWBean> list){
        for (CNWBean item : list){
            updateCNWBean(item);
        }
//        getCNWBeanBox().put(list);
    }

    public static CNWBean findCNWBeanByItemId(String  mItemId){
        if(!TextUtils.isEmpty(mItemId)){
            return getCNWBeanBox()
                    .query()
                    .equal(CNWBean_.itemId,mItemId)
                    .build()
                    .findFirst();
        }else {
            return null;
        }
    }

    public static CNWBean getNewestData(CNWBean bean){
        if(bean != null && !TextUtils.isEmpty(bean.getItemId())){
            CNWBean oldBean = findCNWBeanByItemId(bean.getItemId());
            if(oldBean != null){
                LogUtil.DefalutLog("getNewestData:"+oldBean.toString());
                return oldBean;
            }
        }
        return bean;
    }

    public static List<CNWBean> getCollectedList(String table, int offset, int psize){
        QueryBuilder<CNWBean> query = getCNWBeanBox().query();
        query.equal(CNWBean_.table,table);
        query.greater(CNWBean_.collected,100);
        query.order(CNWBean_.updateTime,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static void deleteAllData(String table){
        try {
            List<CNWBean> list = getCollectedList(table,0,0);
            if(list != null && list.size() > 0){
                getCNWBeanBox().remove(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCNWBean(CNWBean bean){
        try {
            getCNWBeanBox().remove(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**WebFilter**/
    public static Box<WebFilter> getWebFilterBox(){
        return getBoxStore().boxFor(WebFilter.class);
    }

    public static void updateWebFilter(WebFilter bean){
        getWebFilterBox().put(bean);
    }

    public static void updateWebFilter(List<WebFilter> list){
        getWebFilterBox().removeAll();
        getWebFilterBox().put(list);
    }

    public static WebFilter findWebFilterByName(String name){
        QueryBuilder<WebFilter> query = getWebFilterBox().query();
        query.equal(WebFilter_.name,name);
        return query.build().findFirst();

    }

    /**ReadingSubject**/
    public static Box<ReadingSubject> getReadingSubjectBox(){
        return getBoxStore().boxFor(ReadingSubject.class);
    }

    public static void saveReadingSubject(ReadingSubject item){
        getReadingSubjectBox().put(item);
    }

    public static void removeReadingSubject(ReadingSubject item){
        try {
            getReadingSubjectBox().remove(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ReadingSubject findReadingSubjectByObjectId(String oid){
        QueryBuilder<ReadingSubject> query = getReadingSubjectBox().query();
        query.equal(ReadingSubject_.objectId,oid);
        return query.build().findFirst();
    }

    public static List<ReadingSubject> getReadingSubjectList(int offset, int psize){
        QueryBuilder<ReadingSubject> query = getReadingSubjectBox().query();
        query.order(ReadingSubject_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    /** readings curd**/

    public static Box<Reading> getReadingBox(){
        return getBoxStore().boxFor(Reading.class);
    }
    public static long insert(Reading item){
        return getReadingBox().put(item);
    }


    public static void update(Reading item){
        try {
            List<Reading> datas = isDataExit(item.getObject_id());
            if (datas.size() > 0) {
                getReadingBox().put(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Reading> getReadingList(int offset,int limit, String category, String type, String code) {
        QueryBuilder<Reading> qb = getReadingBox().query();
        if(!TextUtils.isEmpty(category)){
            qb.equal(Reading_.category,category);
        }
        if(!TextUtils.isEmpty(type)){
            qb.equal(Reading_.type,type);
        }
        if(!TextUtils.isEmpty(code)){
            if(!code.equals("1000")){
                qb.equal(Reading_.type_id,code);
            }
        }
        qb.order(Reading_.publish_time,QueryBuilder.DESCENDING);
        return qb.build().find(offset,limit);
    }

    public static List<Reading> getReadingCollectedList(int page,int page_size) {
        QueryBuilder<Reading> qb = getReadingBox().query();
        qb.equal(Reading_.isCollected,"1");
        qb.order(Reading_.collected_time);
        return qb.build().find(page * page_size,page_size);
    }

    public static void saveOrGetStatus(Reading bean){
        List<Reading> datas = isDataExit(bean.getObject_id());
        if (datas.size() > 0) {
            Reading localData = datas.get(0);
            bean.setStatus(localData.getStatus());
            bean.setIsCollected(localData.getIsCollected());
            bean.setIsReadLater(localData.getIsReadLater());
            bean.setImg_color(localData.getImg_color());
            bean.setId(localData.getId());
        }else {
            bean.setImg_color(ColorUtil.getRadomColor());
            insert(bean);
        }
    }

    public static List<Reading> isDataExit(String oid){
        return getReadingBox()
                .query()
                .equal(Reading_.object_id,oid)
                .build()
                .find();
    }
    /** readings curd**/
    /** moments like**/
    public static Box<MomentLikes> getMomentLikesBox(){
        return getBoxStore().boxFor(MomentLikes.class);
    }

    public static long insertMomentLike(String mid){
        List<MomentLikes> temp = getMomentLikesList(mid);
        if(temp != null && temp.size() > 0){
            return 0;
        }else {
            MomentLikes item = new MomentLikes();
            item.setMoments_id(mid);
            return getMomentLikesBox().put(item);
        }
    }

    public static List<MomentLikes> getMomentLikesList(String mid){
        return getMomentLikesBox()
                .query()
                .equal(MomentLikes_.moments_id,mid)
                .build()
                .find();
    }

    public static String isLike(String mid){
        List<MomentLikes> temp = getMomentLikesList(mid);
        if(temp != null && temp.size() > 0){
            return KeyUtil.MomentLike;
        }else {
            return "";
        }
    }

    public static void deleteMomentLikes(String mid){
        List<MomentLikes> temp = getMomentLikesList(mid);
        if(temp != null && temp.size() > 0){
            getMomentLikesBox().remove(temp.get(0));
        }
    }
    /** moments like**/
}
