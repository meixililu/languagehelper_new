package com.messi.languagehelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.messi.languagehelper.dao.DaoMaster;
import com.messi.languagehelper.dao.DaoMaster.OpenHelper;

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
        DaoMaster.dropAllTables(new StandardDatabase(db),true);
        DaoMaster.createAllTables(new StandardDatabase(db),true);
    }

}
