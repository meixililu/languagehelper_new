package com.messi.languagehelper.db;

import android.content.Context;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.dao.Avobject;
import com.messi.languagehelper.dao.AvobjectDao;
import com.messi.languagehelper.dao.DaoSession;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.DictionaryDao;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.dao.SymbolListDaoDao;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.dao.WordDetailListItemDao;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.dao.recordDao;
import com.messi.languagehelper.dao.recordDao.Properties;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ContextUtil;
import com.messi.languagehelper.util.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DataBaseUtil {

    private static DataBaseUtil instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private recordDao mrecordDao;
    private DictionaryDao mDictionaryDao;
    private SymbolListDaoDao mSymbolListDaoDao;
    private WordDetailListItemDao mWordDetailListItemDao;
    private AvobjectDao mAvobjectDao;
//    private TranResultZhYueDao mTranResultZhYueDao;
//    private TranRecordDao mTranRecordDao;
//    private TranRecord_jpDao mTranRecord_jpDao;
//    private TranRecord_korDao mTranRecord_korDao;

    public DataBaseUtil() {
    }

    public static DataBaseUtil getInstance() {
        if (instance == null) {
            instance = new DataBaseUtil();
            if (appContext == null) {
                appContext = ContextUtil.get().getContext();
            }
            instance.mDaoSession = BaseApplication.getDaoSession(appContext);
            instance.mrecordDao = instance.mDaoSession.getRecordDao();
            instance.mDictionaryDao = instance.mDaoSession.getDictionaryDao();
            instance.mSymbolListDaoDao = instance.mDaoSession.getSymbolListDaoDao();
            instance.mAvobjectDao = instance.mDaoSession.getAvobjectDao();
            instance.mWordDetailListItemDao = instance.mDaoSession.getWordDetailListItemDao();
//            instance.mTranResultZhYueDao = instance.mDaoSession.getTranResultZhYueDao();
//            instance.mTranRecordDao = instance.mDaoSession.getTranRecordDao();
//            instance.mTranRecord_jpDao = instance.mDaoSession.getTranRecord_jpDao();
//            instance.mTranRecord_korDao = instance.mDaoSession.getTranRecord_korDao();
        }
        return instance;
    }

//    public long insert(Dictionary bean) {
//        bean.setIscollected("0");
//        bean.setVisit_times(0);
//        bean.setSpeak_speed(Setings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
//        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
//        return mDictionaryDao.insert(bean);
//    }

//    public long insert(record bean) {
//        bean.setIscollected("0");
//        bean.setVisit_times(0);
//        bean.setSpeak_speed(Setings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
//        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
//        bean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
//        return mrecordDao.insert(bean);
//    }

//    public long insert(TranResultZhYue bean) {
//        bean.setIscollected("0");
//        bean.setVisit_times(0);
//        bean.setSpeak_speed(Setings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
//        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
//        bean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
//        return mTranResultZhYueDao.insert(bean);
//    }

//    public void update(record bean) {
//        mrecordDao.update(bean);
//    }

//    public void update(TranResultZhYue bean) {
//        mTranResultZhYueDao.update(bean);
//    }
//
//    public void update(Dictionary bean) {
//        mDictionaryDao.update(bean);
//    }

//    public List<TranResultZhYue> getDataListZhYue(int offset, int maxResult) {
//        QueryBuilder<TranResultZhYue> qb = mTranResultZhYueDao.queryBuilder();
//        qb.orderDesc(TranResultZhYueDao.Properties.Id);
//        qb.limit(maxResult);
//        return qb.list();
//    }
//
//    public List<Dictionary> getDataListDictionary(int offset, int maxResult) {
//        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
//        qb.orderDesc(DictionaryDao.Properties.Id);
//        qb.limit(maxResult);
//        return qb.list();
//    }

//    public List<Dictionary> getAllCollectedDictionaryData() {
//        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
//        qb.where(DictionaryDao.Properties.Iscollected.eq("1"));
//        qb.orderDesc(DictionaryDao.Properties.Id);
//        return qb.list();
//    }

//    public List<record> getAllCollectedData() {
//        QueryBuilder<record> qb = mrecordDao.queryBuilder();
//        qb.where(Properties.Iscollected.eq("1"));
//        qb.orderDesc(Properties.Id);
//        return qb.list();
//    }

//    public List<record> getTranCollectedListByPage(int page, int page_size) {
//        QueryBuilder<record> qb = mrecordDao.queryBuilder();
//        qb.where(Properties.Iscollected.eq("1"));
//        qb.orderDesc(Properties.Id);
//        qb.offset(page * page_size);
//        qb.limit(page_size);
//        return qb.list();
//    }

//    public List<TranResultZhYue> getTranZhYueCollectedListByPage(int page, int page_size) {
//        QueryBuilder<TranResultZhYue> qb = mTranResultZhYueDao.queryBuilder();
//        qb.where(TranResultZhYueDao.Properties.Iscollected.eq("1"));
//        qb.orderDesc(TranResultZhYueDao.Properties.Id);
//        qb.offset(page * page_size);
//        qb.limit(page_size);
//        return qb.list();
//    }
//
//    public List<Dictionary> getDataListDictionaryCollected(int offset, int maxResult) {
//        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
//        qb.where(DictionaryDao.Properties.Iscollected.eq("1"));
//        qb.orderDesc(DictionaryDao.Properties.Id);
//        qb.limit(maxResult);
//        return qb.list();
//    }
//    public List<Dictionary> getDicCollectedListByPage(int page, int page_size) {
//        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
//        qb.where(DictionaryDao.Properties.Iscollected.eq("1"));
//        qb.orderDesc(DictionaryDao.Properties.Id);
//        qb.offset(page * page_size);
//        qb.limit(page_size);
//        return qb.list();
//    }

//    public void dele(record bean) {
//        mrecordDao.delete(bean);
//    }

//    public void dele(TranResultZhYue bean) {
//        mTranResultZhYueDao.delete(bean);
//    }

//    public void dele(Dictionary bean) {
//        mDictionaryDao.delete(bean);
//    }

//    public void clearExceptFavorite() {
//        try {
//            clearTranslateExceptFavorite();
//            clearDictionaryExceptFavorite();
//            clearTranZhYueExceptFavorite();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public List<record> getDataListRecord() {
        QueryBuilder<record> qb = mrecordDao.queryBuilder();
        qb.orderAsc(Properties.Id);
        return qb.list();
    }

    public List<Dictionary> getDataListDictionary() {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        qb.orderAsc(DictionaryDao.Properties.Id);
        return qb.list();
    }


    public void insert(List<SymbolListDao> beans) {
        for (SymbolListDao bean : beans) {
            mSymbolListDaoDao.insert(bean);
        }
    }

    public List<WordDetailListItem> getAllList(){
        return mWordDetailListItemDao
                .queryBuilder()
                .list();
    }

    public long getSymbolListSize() {
        return mSymbolListDaoDao.count();
    }

    public List<SymbolListDao> getSymbolList() {
        return mSymbolListDaoDao.loadAll();
    }

    public void update(SymbolListDao bean) {
        mSymbolListDaoDao.update(bean);
    }

    public void clearSymbolList() {
        mSymbolListDaoDao.deleteAll();
    }

    public void clearAllDictionary() {
        mDictionaryDao.deleteAll();
    }

    public void clearAllWordDetailListItem() {
        mWordDetailListItemDao.deleteAll();
    }

//    public void clearTranslateExceptFavorite() {
//        QueryBuilder<record> qb = mrecordDao.queryBuilder();
//        DeleteQuery<record> bd = qb.where(Properties.Iscollected.eq("0")).buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//
//    public void clearTranZhYueExceptFavorite() {
//        QueryBuilder<TranResultZhYue> qb = mTranResultZhYueDao.queryBuilder();
//        DeleteQuery<TranResultZhYue> bd = qb.where(TranResultZhYueDao.Properties.Iscollected.eq("0")).buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//
//    public void clearDictionaryExceptFavorite() {
//        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
//        DeleteQuery<Dictionary> bd = qb.where(DictionaryDao.Properties.Iscollected.eq("0")).buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }

//    public void clearAllTran() {
//        clearAllTranslate();
//    }
//
//    public void clearAllDic() {
//        clearAllDictionary();
//    }

//    public void clearAllTranslate() {
//        mrecordDao.deleteAll();
//    }
//
//    public void clearAllTranZhYue() {
//        mTranResultZhYueDao.deleteAll();
//    }
//
//    public void clearAllData(){
//        try {
//            clearAllTranslate();
//            clearAllDictionary();
//            clearAllTranZhYue();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

//    public long getRecordCount() {
//        return mrecordDao.count();
//    }
//
//    public long getDictionaryCount() {
//        return mDictionaryDao.count();
//    }


//    /** word study **/
//    public void saveList(List<WordDetailListItem> beans, boolean isNewWord){
//        for(WordDetailListItem bean : beans){
//            saveOrUpdate(bean,isNewWord);
//        }
//    }
//
//    public void saveOrUpdate(WordDetailListItem bean, boolean isNewWord){
//        List<WordDetailListItem> items = isDataExit(bean);
//        if(items.size() > 0){
//            if(isNewWord){
//                WordDetailListItem item = items.get(0);
//                item.setNew_words("1");
//                mWordDetailListItemDao.update(item);
//            }
//        }else {
//            if(isNewWord){
//                bean.setNew_words("1");
//            }
//            insertData(bean);
//        }
//    }
//
//    public List<WordDetailListItem> isDataExit(WordDetailListItem bean){
//        return mWordDetailListItemDao
//                .queryBuilder()
//                .where(WordDetailListItemDao.Properties.Class_id.eq(bean.getClass_id()))
//                .where(WordDetailListItemDao.Properties.Name.eq(bean.getName()))
//                .list();
//    }
//
//    public long insertData(WordDetailListItem bean){
//        return mWordDetailListItemDao.insert(bean);
//    }
//
//    public List<WordDetailListItem> getNewWordList(int page, int page_size){
//        return mWordDetailListItemDao
//                .queryBuilder()
//                .where(WordDetailListItemDao.Properties.New_words.eq("1"))
//                .offset(page * page_size)
//                .limit(page_size)
//                .list();
//    }
//
//    public WordDetailListItem getBench(){
//        int count = countWordDetailListItem();
//        int randomPage = new Random().nextInt(count);
//        return mWordDetailListItemDao
//                .queryBuilder()
//                .offset(randomPage)
//                .limit(1)
//                .list()
//                .get(0);
//    }
//
//    public int countNewWordNumber(){
//        return mWordDetailListItemDao
//                .queryBuilder()
//                .where(WordDetailListItemDao.Properties.New_words.eq("1"))
//                .list()
//                .size();
//    }
//
//    public int countWordDetailListItem(){
//        return mWordDetailListItemDao
//                .queryBuilder()
//                .list()
//                .size();
//    }
//
//    public void deleteList(List<WordDetailListItem> beans){
//        for(WordDetailListItem bean : beans){
//            delete(bean);
//        }
//    }
//
//    public void delete(WordDetailListItem bean){
//        mWordDetailListItemDao.delete(bean);
//    }
//
//    /** word study **/

//    public long insert(AiEntity entity){
//        return mAiEntityDao.insert(entity);
//    }
//
//    public void update(AiEntity entity){
//        mAiEntityDao.update(entity);
//    }
//
//    public List<AiEntity> getAiEntityList(String type){
//        List<AiEntity> history = mAiEntityDao
//                .queryBuilder()
//                .where(AiEntityDao.Properties.Ai_type.eq(type))
//                .limit(30)
//                .orderDesc(AiEntityDao.Properties.Id)
//                .list();
//        Collections.reverse(history);
//        return history;
//    }
//
//    public List<AiEntity> getAiEntityList(long id,String type){
//        List<AiEntity> history = mAiEntityDao
//                .queryBuilder()
//                .where(AiEntityDao.Properties.Ai_type.eq(type))
//                .where(AiEntityDao.Properties.Id.lt(id))
//                .limit(30)
//                .orderDesc(AiEntityDao.Properties.Id)
//                .list();
//        Collections.reverse(history);
//        return history;
//    }
//
//    public void deleteAiEntity(String type){
//        List<AiEntity> history = mAiEntityDao
//                .queryBuilder()
//                .where(AiEntityDao.Properties.Ai_type.eq(type))
//                .list();
//        mAiEntityDao.deleteInTx(history);
//    }

//    public void deleteAiEntity(AiEntity entity){
//        mAiEntityDao.delete(entity);
//    }
//
//    public void deleteAllAiEntity(){
//        mAiEntityDao.deleteAll();
//    }

//    public long insert(String tableName,AVObject mItem,String key,long collected,long history){
//        Avobject entity = new Avobject();
//        entity.setTableName(tableName);
//        entity.setItemId(mItem.getObjectId());
//        entity.setSerializedString(mItem.toString());
//        entity.setKey(key);
//        entity.setUpdateTime(System.currentTimeMillis());
//        entity.setHistory(history);
//        entity.setCollected(collected);
//        entity.setCeateTime(System.currentTimeMillis());
//        return mAvobjectDao.insert(entity);
//    }
//    public void update(Avobject entity){
//        mAvobjectDao.update(entity);
//    }

//    public void updateOrInsertAVObject(String tableName,AVObject object,String key){
//        Avobject mAvobject = findById(tableName,object.getObjectId());
//        if(mAvobject != null){
//            mAvobject.setSerializedString(object.toString());
//            mAvobject.setUpdateTime(System.currentTimeMillis());
//            update(mAvobject);
//        }else {
//            insert(tableName,object,key,0,System.currentTimeMillis());
//        }
//    }

//    public void updateOrInsertAVObject(String tableName,AVObject object,String key,long collected){
//        Avobject mAvobject = findById(tableName,object.getObjectId());
//        if(mAvobject != null){
//            mAvobject.setKey(key);
//            mAvobject.setCollected(collected);
//            mAvobject.setSerializedString(object.toString());
//            mAvobject.setUpdateTime(System.currentTimeMillis());
//            update(mAvobject);
//        }else {
//            insert(tableName,object,key,collected,0);
//        }
//    }

//    public void updateAVObjectCollected(String tableName,AVObject object,long collected){
//        Avobject mAvobject = findById(tableName,object.getObjectId());
//        if(mAvobject != null){
//            mAvobject.setCollected(collected);
//            mAvobject.setSerializedString(object.toString());
//            update(mAvobject);
//        }
//    }
//
//    public void updateAVObjectHistory(String tableName,AVObject object,long history){
//        Avobject mAvobject = findById(tableName,object.getObjectId());
//        if(mAvobject != null){
//            mAvobject.setHistory(history);
//            mAvobject.setSerializedString(object.toString());
//            update(mAvobject);
//        }
//    }

//    public Avobject findById(String tableName, String objectId){
//        List<Avobject> history = mAvobjectDao
//                .queryBuilder()
//                .where(AvobjectDao.Properties.TableName.eq(tableName))
//                .where(AvobjectDao.Properties.ItemId.eq(objectId))
//                .orderDesc(AvobjectDao.Properties.Id)
//                .list();
//        if(history != null && history.size() > 0){
//            return history.get(0);
//        }else {
//            return null;
//        }
//    }

//    public Avobject findByKey(String tableName, String key){
//        List<Avobject> history = mAvobjectDao
//                .queryBuilder()
//                .where(AvobjectDao.Properties.TableName.eq(tableName))
//                .where(AvobjectDao.Properties.Key.eq(key))
//                .orderDesc(AvobjectDao.Properties.Id)
//                .list();
//        if(history != null && history.size() > 0){
//            return history.get(0);
//        }else {
//            return null;
//        }
//    }

//    public AVObject findByItemId(String tableName, String objectId){
//        try {
//            Avobject mAvobject = findById(tableName,objectId);
//            if (mAvobject != null) {
//                return AVObject.parseAVObject(mAvobject.getSerializedString());
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public AVObject findByItemKey(String tableName, String key){
//        try {
//            Avobject mAvobject = findByKey(tableName,key);
//            if (mAvobject != null) {
//                return AVObject.parseAVObject(mAvobject.getSerializedString());
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<Avobject> getCaricatureList(String tableName,int page,int page_size,
//                                            boolean isHistory, boolean isCollected) {
//        QueryBuilder<Avobject> qb = mAvobjectDao.queryBuilder();
//        qb.where(AvobjectDao.Properties.TableName.eq(tableName));
//        if(isCollected){
//            qb.where(AvobjectDao.Properties.Collected.ge(100));
//        }
//        if(isHistory){
//            qb.where(AvobjectDao.Properties.History.ge(100));
//        }
//        qb.offset(page);
//        qb.limit(page_size);
//        qb.orderDesc(AvobjectDao.Properties.UpdateTime);
//        return qb.list();
//    }

//    public List<AVObject> getCaricaturesList(String tableName,int page,int page_size,
//                                             boolean isHistory, boolean isCollected){
//        List<AVObject> dataList = new ArrayList<AVObject>();
//        try {
//            List<Avobject> list = getCaricatureList(tableName,page,page_size,isHistory,isCollected);
//            if(list != null && list.size() > 0){
//                for(Avobject item : list){
//                    AVObject object = AVObject.parseAVObject(item.getSerializedString());
//                    if(object != null){
//                        dataList.add(object);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dataList;
//    }

//    public void clearAvobjectCollected(String tableName) {
//        QueryBuilder<Avobject> qb = mAvobjectDao.queryBuilder();
//        DeleteQuery<Avobject> bd = qb
//                .where(AvobjectDao.Properties.TableName.eq(tableName))
//                .where(AvobjectDao.Properties.Collected.ge(100))
//                .buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//
//    public void clearAvobjectHistory(String tableName) {
//        QueryBuilder<Avobject> qb = mAvobjectDao.queryBuilder();
//        DeleteQuery<Avobject> bd = qb
//                .where(AvobjectDao.Properties.TableName.eq(tableName))
//                .where(AvobjectDao.Properties.Collected.le(100))
//                .where(AvobjectDao.Properties.History.ge(100))
//                .buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }


    public void clearAvobject() {
        mAvobjectDao.deleteAll();
    }

    public void clearRecordData() {
        mrecordDao.deleteAll();
    }

    public List<CNWBean> getAllAVObjectData(){
        List<CNWBean> dataList = new ArrayList<CNWBean>();
        List<Avobject> list = mAvobjectDao.loadAll();
        if(list != null && list.size() > 0){
            for(Avobject item : list){
                AVObject object = null;
                try{
                    object = AVObject.parseAVObject(item.getSerializedString());
                }catch (Exception e){
                    LogUtil.DefalutLog("---------------Exception----------------");
                    e.printStackTrace();
                }
                if(object != null){
                    CNWBean bean = new CNWBean();
                    bean.setTable(item.getTableName());
                    bean.setTitle(object.getString(AVOUtil.Caricature.name));
                    bean.setCategory(object.getString(AVOUtil.Caricature.category));
                    bean.setTag(object.getString(AVOUtil.Caricature.tag));
                    bean.setUpdate_des(object.getString(AVOUtil.Caricature.update));
                    bean.setAuthor(object.getString(AVOUtil.Caricature.author));
                    bean.setImg_url(object.getString(AVOUtil.Caricature.book_img_url));
                    bean.setRead_url(object.getString(AVOUtil.Caricature.read_url));
                    bean.setSource_url(object.getString(AVOUtil.Caricature.url));
                    bean.setSource_name(object.getString(AVOUtil.Caricature.source_name));
                    bean.setDes(object.getString(AVOUtil.Caricature.des));
                    bean.setType(object.getString(AVOUtil.Caricature.type));
                    bean.setCollected(item.getCollected());
                    bean.setHistory(item.getHistory());
                    bean.setItemId(item.getItemId());
                    bean.setView(item.getView());
                    bean.setUpdateTime(item.getUpdateTime());
                    dataList.add(bean);
                }
            }
        }
        return dataList;
    }
}
