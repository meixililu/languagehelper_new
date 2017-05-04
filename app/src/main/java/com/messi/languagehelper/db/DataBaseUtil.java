package com.messi.languagehelper.db;

import android.content.Context;
import android.text.TextUtils;

import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.DaoSession;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.DictionaryDao;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.dao.EveryDaySentenceDao;
import com.messi.languagehelper.dao.Means;
import com.messi.languagehelper.dao.MeansDao;
import com.messi.languagehelper.dao.Parts;
import com.messi.languagehelper.dao.PartsDao;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.dao.ReadingDao;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.dao.SymbolListDaoDao;
import com.messi.languagehelper.dao.Tag;
import com.messi.languagehelper.dao.TagDao;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.dao.recordDao;
import com.messi.languagehelper.dao.recordDao.Properties;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DataBaseUtil {

    private static DataBaseUtil instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private recordDao mrecordDao;
    private EveryDaySentenceDao mEveryDaySentenceDao;
    private DictionaryDao mDictionaryDao;
    private MeansDao MmeansDao;
    private PartsDao mPartsDao;
    private TagDao mTagDao;
    private ReadingDao mReadingDao;
    private SymbolListDaoDao mSymbolListDaoDao;

    public DataBaseUtil() {
    }

    public static DataBaseUtil getInstance() {
        if (instance == null) {
            instance = new DataBaseUtil();
            if (appContext == null) {
                appContext = BaseApplication.mInstance;
            }
            instance.mDaoSession = BaseApplication.getDaoSession(appContext);
            instance.mrecordDao = instance.mDaoSession.getRecordDao();
            instance.mDictionaryDao = instance.mDaoSession.getDictionaryDao();
            instance.mEveryDaySentenceDao = instance.mDaoSession.getEveryDaySentenceDao();
            instance.mPartsDao = instance.mDaoSession.getPartsDao();
            instance.MmeansDao = instance.mDaoSession.getMeansDao();
            instance.mTagDao = instance.mDaoSession.getTagDao();
            instance.mSymbolListDaoDao = instance.mDaoSession.getSymbolListDaoDao();
            instance.mReadingDao = instance.mDaoSession.getReadingDao();
        }
        return instance;
    }

    public long insert(Dictionary bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(Settings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
        return mDictionaryDao.insert(bean);
    }

    public long insert(Parts bean) {
        return mPartsDao.insert(bean);
    }

    public long insert(Tag bean) {
        return mTagDao.insert(bean);
    }

    public long insert(Means bean) {
        return MmeansDao.insert(bean);
    }

    public void insert(List<SymbolListDao> beans) {
        for (SymbolListDao bean : beans) {
            mSymbolListDaoDao.insert(bean);
        }
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

    public long insert(record bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(Settings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
        bean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
        return mrecordDao.insert(bean);
    }


    public void update(record bean) {
        mrecordDao.update(bean);
    }

    public void update(Dictionary bean) {
        mDictionaryDao.update(bean);
    }

    public List<record> getDataListRecord(int offset, int maxResult) {
        QueryBuilder<record> qb = mrecordDao.queryBuilder();
        qb.orderDesc(Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public List<Dictionary> getDataListDictionary(int offset, int maxResult) {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        qb.orderDesc(DictionaryDao.Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public List<record> getDataListCollected(int offset, int maxResult) {
        QueryBuilder<record> qb = mrecordDao.queryBuilder();
        qb.where(Properties.Iscollected.eq("1"));
        qb.orderDesc(Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public List<Dictionary> getDataListDictionaryCollected(int offset, int maxResult) {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        qb.where(DictionaryDao.Properties.Iscollected.eq("1"));
        qb.orderDesc(DictionaryDao.Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public void dele(record bean) {
        mrecordDao.delete(bean);
    }

    public void dele(Dictionary bean) {
        mDictionaryDao.delete(bean);
    }

    public void clearExceptFavoriteTran() {
        clearTranslateExceptFavorite();
    }

    public void clearExceptFavoriteDic() {
        clearDictionaryExceptFavorite();
    }

    public void clearSymbolList() {
        mSymbolListDaoDao.deleteAll();
    }

    public void clearTranslateExceptFavorite() {
        QueryBuilder<record> qb = mrecordDao.queryBuilder();
        DeleteQuery<record> bd = qb.where(Properties.Iscollected.eq("0")).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    public void clearDictionaryExceptFavorite() {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        DeleteQuery<Dictionary> bd = qb.where(DictionaryDao.Properties.Iscollected.eq("0")).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    public void clearAllTran() {
        clearAllTranslate();
    }

    public void clearAllDic() {
        clearAllDictionary();
    }

    public void clearAllTranslate() {
        mrecordDao.deleteAll();
    }

    public void clearAllDictionary() {
        mDictionaryDao.deleteAll();
    }

    public long getRecordCount() {
        return mrecordDao.count();
    }

    public long getDictionaryCount() {
        return mDictionaryDao.count();
    }

    /**
     * Daily Sentence CURD
     **/
    public long insert(EveryDaySentence bean) {
        return mEveryDaySentenceDao.insert(bean);
    }

    public boolean isExist(long cid) {
        QueryBuilder<EveryDaySentence> qb = mEveryDaySentenceDao.queryBuilder();
        qb.where(EveryDaySentenceDao.Properties.Cid.eq(cid));
        int size = qb.list().size();
        LogUtil.DefalutLog("isExist---size:" + size);
        return size > 0;
    }

    public List<EveryDaySentence> getDailySentenceList(int limit) {
        QueryBuilder<EveryDaySentence> qb = mEveryDaySentenceDao.queryBuilder();
        qb.orderDesc(EveryDaySentenceDao.Properties.Dateline);
        qb.limit(limit);
        return qb.list();
    }

    public void saveEveryDaySentenceList(List<EveryDaySentence> beans) {
        for (EveryDaySentence item : beans) {
            if (!isExist(item.getCid())) {
                insert(item);
            }
        }
    }
    /**Daily Sentence CURD**/

    /** readings curd**/
    public long insert(Reading item){
        return mReadingDao.insert(item);
    }

    public void update(Reading item){
        List<Reading> datas = isDataExit(item);
        if (datas.size() > 0) {
            mReadingDao.update(item);
        }
    }

    public List<Reading> getReadingList(int limit, String category, String type, String code) {
        QueryBuilder<Reading> qb = mReadingDao.queryBuilder();
        if(!TextUtils.isEmpty(category)){
            qb.where(ReadingDao.Properties.Category.eq(category));
        }
        if(!TextUtils.isEmpty(type)){
            qb.where(ReadingDao.Properties.Type.eq(type));
        }
        if(!TextUtils.isEmpty(code)){
            if(!code.equals("1000")){
                qb.where(ReadingDao.Properties.Type_id.eq(code));
            }
        }
        qb.orderDesc(ReadingDao.Properties.Publish_time);
        qb.limit(limit);
        return qb.list();
    }

    public void saveOrGetStatus(Reading bean){
        List<Reading> datas = isDataExit(bean);
        if (datas.size() > 0) {
            Reading localData = datas.get(0);
            bean.setStatus(localData.getStatus());
            bean.setIsCollected(localData.getIsCollected());
            bean.setIsReadLater(localData.getIsReadLater());
            bean.setId(localData.getId());
        }else {
            insert(bean);
        }
    }

    public List<Reading> isDataExit(Reading bean){
        return mReadingDao
                .queryBuilder()
                .where(ReadingDao.Properties.Object_id.eq(bean.getObject_id()))
                .list();
    }

    /** readings curd**/
}
