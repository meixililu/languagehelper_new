package com.messi.languagehelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.messi.languagehelper.dao.DaoMaster.OpenHelper;
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
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.dao.WordDetailListItemDao;
import com.messi.languagehelper.dao.record;
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
        MigrationHelper.migrate(db, EveryDaySentenceDao.class, MeansDao.class,
                DictionaryDao.class, PartsDao.class, ReadingDao.class, recordDao.class,
                SymbolListDaoDao.class, WordDetailListItemDao.class);
    }
}
