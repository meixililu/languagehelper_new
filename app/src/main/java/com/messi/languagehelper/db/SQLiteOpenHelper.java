package com.messi.languagehelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.thirdparty.L;
import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.dao.AiEntity;
import com.messi.languagehelper.dao.AiEntityDao;
import com.messi.languagehelper.dao.DaoMaster.OpenHelper;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.DictionaryDao;
import com.messi.languagehelper.dao.EveryDaySentenceDao;
import com.messi.languagehelper.dao.ReadingDao;
import com.messi.languagehelper.dao.SymbolListDaoDao;
import com.messi.languagehelper.dao.WordDetailListItemDao;
import com.messi.languagehelper.dao.recordDao;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Settings;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        MigrationHelper.migrate(db, EveryDaySentenceDao.class,
                DictionaryDao.class, ReadingDao.class, recordDao.class,
                SymbolListDaoDao.class, WordDetailListItemDao.class, AiEntityDao.class);
    }

}
