package com.messi.languagehelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.messi.languagehelper.dao.AiEntityDao;
import com.messi.languagehelper.dao.AvobjectDao;
import com.messi.languagehelper.dao.DaoMaster;
import com.messi.languagehelper.dao.DaoMaster.OpenHelper;
import com.messi.languagehelper.dao.DictionaryDao;
import com.messi.languagehelper.dao.EveryDaySentenceDao;
import com.messi.languagehelper.dao.ReadingDao;
import com.messi.languagehelper.dao.SymbolListDaoDao;
import com.messi.languagehelper.dao.TranRecordDao;
import com.messi.languagehelper.dao.TranRecord_jpDao;
import com.messi.languagehelper.dao.TranRecord_korDao;
import com.messi.languagehelper.dao.TranResultZhYueDao;
import com.messi.languagehelper.dao.WordDetailListItemDao;
import com.messi.languagehelper.dao.recordDao;

import org.greenrobot.greendao.database.StandardDatabase;

/**
 * Created by luli on 12/05/2017.
 */

public class SQLiteOpenHelper extends OpenHelper {

    public SQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            MigrationHelper.migrate(db, EveryDaySentenceDao.class,
                    DictionaryDao.class, ReadingDao.class, recordDao.class,
                    SymbolListDaoDao.class, WordDetailListItemDao.class, AiEntityDao.class,
                    TranResultZhYueDao.class, TranRecordDao.class, TranRecord_jpDao.class,
                    TranRecord_korDao.class, AvobjectDao.class);
        } catch (Exception e) {
            DaoMaster.dropAllTables(new StandardDatabase(db),true);
            DaoMaster.createAllTables(new StandardDatabase(db),true);
            e.printStackTrace();
        }
    }

}
