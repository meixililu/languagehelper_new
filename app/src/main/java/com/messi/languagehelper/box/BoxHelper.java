package com.messi.languagehelper.box;

import android.content.Context;
import android.text.TextUtils;

import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class BoxHelper {

    public static BoxStore boxStore;

    public static void init(Context context){
        try {
            boxStore = MyObjectBox.builder().androidContext(context.getApplicationContext()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BoxStore getBoxStore(){
        if(boxStore == null){
            init(BaseApplication.instance.getApplicationContext());
        }
        return boxStore;
    }

    /**Record**/
    public static Box<Record> getRecordBox(){
        return getBoxStore().boxFor(Record.class);
    }

    public static long insert(Record bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(50);
//        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
//        bean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
        return getRecordBox().put(bean);
    }

    public static List<Record> getRecordList(int offset, int psize) {
        QueryBuilder<Record> query = getRecordBox().query();
        query.order(Record_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static List<Record> getCollectedRecordList(int offset, int psize, String type) {
        QueryBuilder<Record> query = getRecordBox().query();
        query.equal(Record_.iscollected,type);
        query.order(Record_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static void update(Record bean) {
        getRecordBox().put(bean);
    }

    public static void remove(Record bean) {
        getRecordBox().remove(bean);
    }

    public static void removeNoCollectedRecordData(){
        List<Record> list = getCollectedRecordList(0,0,"0");
        getRecordBox().remove(list);
    }

    public static void removeAllRecord() {
        getRecordBox().removeAll();
    }

    /**Dictionary**/
    public static Box<Dictionary> getDictionaryBox(){
        return getBoxStore().boxFor(Dictionary.class);
    }

    public static long insert(Dictionary bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(50);
//        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
        return getDictionaryBox().put(bean);
    }

    public static void update(Dictionary bean) {
        getDictionaryBox().put(bean);
    }

    public static List<Dictionary> getDictionaryList(int offset, int psize) {
        QueryBuilder<Dictionary> query = getDictionaryBox().query();
        query.order(Dictionary_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static List<Dictionary> getCollectedDictionaryList(int offset, int psize, String type) {
        QueryBuilder<Dictionary> query = getDictionaryBox().query();
        query.equal(Dictionary_.iscollected,type);
        query.order(Dictionary_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static void remove(Dictionary bean) {
        getDictionaryBox().remove(bean);
    }

    public static void removeNoCollectedDictionaryBoxData(){
        List<Dictionary> list = getCollectedDictionaryList(0,0,"0");
        getDictionaryBox().remove(list);
    }

    public static void removeAllDictionary() {
        getDictionaryBox().removeAll();
    }

    /**TranResultZhYue**/
    public static Box<TranResultZhYue> getTranResultZhYueBox(){
        return getBoxStore().boxFor(TranResultZhYue.class);
    }

    public static long insert(TranResultZhYue bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(50);
//        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
//        bean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
        return getTranResultZhYueBox().put(bean);
    }

    public static void update(TranResultZhYue bean) {
        getTranResultZhYueBox().put(bean);
    }

    public static List<TranResultZhYue> getTranResultZhYueList(int offset, int psize) {
        QueryBuilder<TranResultZhYue> query = getTranResultZhYueBox().query();
        query.order(TranResultZhYue_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static List<TranResultZhYue> getCollectedTranResultZhYueList(int offset, int psize, String type) {
        QueryBuilder<TranResultZhYue> query = getTranResultZhYueBox().query();
        query.equal(TranResultZhYue_.iscollected,type);
        query.order(TranResultZhYue_.id,QueryBuilder.DESCENDING);
        if(psize > 0){
            return query.build().find(offset,psize);
        }else {
            return query.build().find();
        }
    }

    public static void remove(TranResultZhYue bean) {
        getTranResultZhYueBox().remove(bean);
    }

    public static void removeNoCollectedTranResultZhYueData(){
        List<TranResultZhYue> list = getCollectedTranResultZhYueList(0,0,"0");
        getTranResultZhYueBox().remove(list);
    }

    public static void removeAllTranResultZhYue() {
        getTranResultZhYueBox().removeAll();
    }

    /**clear record dictionary tranzhyue**/
    public static void clearExceptFavorite() {
        try {
            removeNoCollectedRecordData();
            removeNoCollectedDictionaryBoxData();
            removeNoCollectedTranResultZhYueData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearAllData(){
        try {
            removeAllRecord();
            removeAllDictionary();
            removeAllTranResultZhYue();
        }catch (Exception e){
            e.printStackTrace();
        }
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
            List<Reading> datas = isOidExit(item.getObject_id());
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
        if (page_size > 0) {
            return qb.build().find(page,page_size);
        } else {
            return qb.build().find();
        }
    }

    public static void saveOrGetStatus(Reading bean){
        List<Reading> datas = isOidExit(bean.getObject_id());
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

    public static List<Reading> isOidExit(String oid){
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

    public static Box<EveryDaySentence> getEveryDaySentenceBox(){
        return getBoxStore().boxFor(EveryDaySentence.class);
    }

    public static long insert(EveryDaySentence item){
        return getEveryDaySentenceBox().put(item);
    }

    public static boolean isEveryDaySentenceExist(long mid){
         int size = getEveryDaySentenceBox()
                 .query()
                 .equal(EveryDaySentence_.cid,mid)
                 .build()
                 .find()
                 .size();
        LogUtil.DefalutLog("isEveryDaySentenceExist---size:" + size);
         return size > 0;
    }

    public static List<EveryDaySentence> getEveryDaySentenceList(int limit){
        return getEveryDaySentenceBox()
                .query()
                .build()
                .find(0,limit);
    }

    /** AiEntity **/
    public static Box<AiEntity> getAiEntityBox(){
        return getBoxStore().boxFor(AiEntity.class);
    }

    public static long insertOrUpdate(AiEntity entity){
        return getAiEntityBox().put(entity);
    }

    public static List<AiEntity> getAiEntityList(String type){
        List<AiEntity> history = getAiEntityBox()
                .query()
                .equal(AiEntity_.ai_type,type)
                .orderDesc(AiEntity_.id)
                .build()
                .find(0,30);
        Collections.reverse(history);
        return history;
    }

    public static List<AiEntity> getAiEntityList(long id,String type){
        List<AiEntity> history = getAiEntityBox()
                .query()
                .equal(AiEntity_.ai_type,type)
                .less(AiEntity_.id,id)
                .orderDesc(AiEntity_.id)
                .build()
                .find(0,30);
        Collections.reverse(history);
        return history;
    }

    public static void deleteAiEntity(String type){
        List<AiEntity> history = getAiEntityBox()
                .query()
                .equal(AiEntity_.ai_type,type)
                .build()
                .find();
        getAiEntityBox().remove(history);
    }
    /** AiEntity end **/

    /** word study **/
    public static Box<WordDetailListItem> getWordDetailListItemBox(){
        return getBoxStore().boxFor(WordDetailListItem.class);
    }

    public static void saveAndGetStatusList(List<WordDetailListItem> beans){
        if (NullUtil.isNotEmpty(beans)) {
            for(WordDetailListItem bean : beans){
                saveAndGetStatus(bean);
            }
        }
    }

    public static void saveAndGetStatus(WordDetailListItem bean){
        List<WordDetailListItem> items = isExit(bean);
        if(items.size() == 0){
            long id = insert(bean);
            bean.setId(id);
        }else {
            WordDetailListItem oldItem = items.get(0);
            bean.setId(oldItem.getId());
            bean.setIs_know(oldItem.isIs_know());
        }
    }

    public static void updateList(List<WordDetailListItem> beans, boolean isNewWord){
        if (NullUtil.isNotEmpty(beans)) {
            for(WordDetailListItem bean : beans){
                update(bean,isNewWord);
            }
        }
    }

    public static void update(WordDetailListItem bean, boolean isNewWord){
        if (bean.getId() == 0) {
            List<WordDetailListItem> items = isOidExit(bean);
            if(items.size() > 0){
                bean.setId(items.get(0).getId());
            }
        }
        if(isNewWord){
            bean.setNew_words("1");
        }else {
            bean.setNew_words("0");
        }
        long id = insert(bean);
        if (bean.getId() == 0) {
            bean.setId(id);
        }
    }

    public static List<WordDetailListItem> isOidExit(WordDetailListItem bean){
        return getWordDetailListItemBox()
                .query()
                .equal(WordDetailListItem_.class_id,bean.getClass_id())
                .equal(WordDetailListItem_.item_id,bean.getItem_id())
                .build()
                .find();
    }

    public static List<WordDetailListItem> isExit(WordDetailListItem bean){
        return getWordDetailListItemBox()
                .query()
                .equal(WordDetailListItem_.name,bean.getName())
                .equal(WordDetailListItem_.class_id,bean.getClass_id())
                .equal(WordDetailListItem_.course,bean.getCourse())
                .build()
                .find();
    }

    public static List<WordDetailListItem> isExitByType(WordDetailListItem bean){
        return getWordDetailListItemBox()
                .query()
                .equal(WordDetailListItem_.name,bean.getName())
                .equal(WordDetailListItem_.type,bean.getType())
                .build()
                .find();
    }

    public static void saveSearchResultToNewWord(WordDetailListItem bean){
        List<WordDetailListItem> items = isExitByType(bean);
        if(items.size() > 0){
            bean.setId(items.get(0).getId());
        }
        insert(bean);
    }

    public static long insert(WordDetailListItem bean){
        return getWordDetailListItemBox().put(bean);
    }

    public static List<WordDetailListItem> getNewWordList(int skip, int page_size){
        QueryBuilder<WordDetailListItem> query = getWordDetailListItemBox().query();
        query.equal(WordDetailListItem_.new_words,"1");
        query.order(WordDetailListItem_.id,QueryBuilder.DESCENDING);
        if(page_size > 0){
            return query.build().find(skip, page_size);
        }else {
            return query.build().find();
        }
    }

    public static List<WordDetailListItem> getList(String class_id,int course_id){
        return getWordDetailListItemBox()
                .query()
                .equal(WordDetailListItem_.class_id,class_id)
                .equal(WordDetailListItem_.course,course_id)
                .build()
                .find();
    }

    public static WordDetailListItem getBench(){
        long count = countWordDetailListItem();
        int randomPage = new Random().nextInt((int)count);
        return getWordDetailListItemBox()
                .query()
                .build()
                .find(randomPage,1)
                .get(0);
    }

    public static int countNewWordNumber(){
        return (int)getWordDetailListItemBox()
                .query()
                .equal(WordDetailListItem_.new_words,"1")
                .build()
                .count();
    }

    public static int countWordDetailListItem(){
        return (int)getWordDetailListItemBox()
                .query()
                .build()
                .count();
    }

    public static void removeList(List<WordDetailListItem> beans){
        getWordDetailListItemBox().remove(beans);
    }

    public static void removeAllWordDetailListItem(){
        getWordDetailListItemBox().removeAll();
    }

    public static void remove(WordDetailListItem bean){
        getWordDetailListItemBox().remove(bean);
    }

    //CollectedData
    public static Box<CollectedData> getCollectedDataBox(){
        return getBoxStore().boxFor(CollectedData.class);
    }

    public static void insert(CollectedData bean){
        List<CollectedData> list = isCollectedDataExist(bean.getObjectId());
        if (!NullUtil.isNotEmpty(list)) {
            getCollectedDataBox().put(bean);
        }
    }

    public static void remove(CollectedData bean){
        List<CollectedData> list = isCollectedDataExist(bean.getObjectId());
        if (NullUtil.isNotEmpty(list)) {
            CollectedData data = list.get(0);
            getCollectedDataBox().remove(data);
        }
    }

    public static List<CollectedData> isCollectedDataExist(String oid){
        return getCollectedDataBox()
                .query()
                .equal(CollectedData_.objectId,oid)
                .build()
                .find();
    }

    public static boolean isCollected(String oid){
        int size = 0;
        if (!TextUtils.isEmpty(oid)) {
            size = getCollectedDataBox()
                    .query()
                    .equal(CollectedData_.objectId,oid)
                    .build()
                    .find()
                    .size();
        }
        return size > 0;
    }

    public static List<CollectedData> getCollectedList(int offset,int page_size) {
        QueryBuilder<CollectedData> qb = getCollectedDataBox().query();
        qb.orderDesc(CollectedData_.id);
        return qb.build().find(offset,page_size);
    }
}
