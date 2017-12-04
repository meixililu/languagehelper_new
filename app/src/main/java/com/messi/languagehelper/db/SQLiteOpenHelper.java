package com.messi.languagehelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.messi.languagehelper.dao.AiEntityDao;
import com.messi.languagehelper.dao.DaoMaster.OpenHelper;
import com.messi.languagehelper.dao.DictionaryDao;
import com.messi.languagehelper.dao.EveryDaySentenceDao;
import com.messi.languagehelper.dao.ReadingDao;
import com.messi.languagehelper.dao.SymbolListDaoDao;
import com.messi.languagehelper.dao.WordDetailListItemDao;
import com.messi.languagehelper.dao.recordDao;

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
                    SymbolListDaoDao.class, WordDetailListItemDao.class, AiEntityDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
