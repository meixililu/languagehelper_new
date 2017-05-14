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
                SymbolListDaoDao.class, WordDetailListItemDao.class);
    }

//    public static void createTable(Database db, boolean ifNotExists) {
//        String constraint = ifNotExists? "IF NOT EXISTS ": "";
//        db.execSQL("CREATE TABLE " + constraint + "\"DICTIONARY_T\" (" + //
//                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
//                "\"WORD_NAME\" TEXT," + // 1: word_name
//                "\"RESULT\" TEXT," + // 2: result
//                "\"TO_LAN\" TEXT," + // 3: to
//                "\"FROM_LAN\" TEXT," + // 4: from
//                "\"PH_AM\" TEXT," + // 5: ph_am
//                "\"PH_EN\" TEXT," + // 6: ph_en
//                "\"PH_ZH\" TEXT," + // 7: ph_zh
//                "\"TYPE\" TEXT," + // 8: type
//                "\"QUESTION_VOICE_ID\" TEXT," + // 9: questionVoiceId
//                "\"QUESTION_AUDIO_PATH\" TEXT," + // 10: questionAudioPath
//                "\"RESULT_VOICE_ID\" TEXT," + // 11: resultVoiceId
//                "\"RESULT_AUDIO_PATH\" TEXT," + // 12: resultAudioPath
//                "\"ISCOLLECTED\" TEXT," + // 13: iscollected
//                "\"VISIT_TIMES\" INTEGER," + // 14: visit_times
//                "\"SPEAK_SPEED\" INTEGER," + // 15: speak_speed
//                "\"BACKUP1\" TEXT," + // 16: backup1
//                "\"BACKUP2\" TEXT," + // 17: backup2
//                "\"BACKUP3\" TEXT," + // 18: backup3
//                "\"BACKUP4\" TEXT," + // 19: backup4
//                "\"BACKUP5\" TEXT);"); // 20: backup5
//        LogUtil.DefalutLog("createTable DICTIONARY_T");
//    }
//
//    private static void saveTemp(SQLiteDatabase db) {
//        Cursor cursor = db.rawQuery("select * from DICTIONARY", null);
////        List<Dictionary> backupList = new ArrayList<Dictionary>();
//        while (cursor.moveToNext()) {
//            String Word_name = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Word_name.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Result = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Result.columnName)); //获取第一列的值,第一列的索引从0开始
//            String To = cursor.getString(cursor.getColumnIndex("TO")); //获取第一列的值,第一列的索引从0开始
//            String From = cursor.getString(cursor.getColumnIndex("FROM")); //获取第一列的值,第一列的索引从0开始
//            String Ph_am = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Ph_am.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Ph_en = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Ph_en.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Ph_zh = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Ph_zh.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Type = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Type.columnName)); //获取第一列的值,第一列的索引从0开始
//            String QuestionVoiceId = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.QuestionVoiceId.columnName)); //获取第一列的值,第一列的索引从0开始
//            String QuestionAudioPath = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.QuestionAudioPath.columnName)); //获取第一列的值,第一列的索引从0开始
//            String ResultVoiceId = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.ResultVoiceId.columnName)); //获取第一列的值,第一列的索引从0开始
//            String ResultAudioPath = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.ResultAudioPath.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Iscollected = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Iscollected.columnName)); //获取第一列的值,第一列的索引从0开始
//            int Visit_times = cursor.getInt(cursor.getColumnIndex(DictionaryDao.Properties.Visit_times.columnName)); //获取第一列的值,第一列的索引从0开始
//            int Speak_speed = cursor.getInt(cursor.getColumnIndex(DictionaryDao.Properties.Speak_speed.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup1 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup1.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup2 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup2.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup3 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup3.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup4 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup4.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup5 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup5.columnName)); //获取第一列的值,第一列的索引从0开始
//
////            Dictionary dictionary = new Dictionary();
////            dictionary.setWord_name(Word_name);
////            dictionary.setResult(Result);
////            dictionary.setTo(To);
////            dictionary.setFrom(From);
////            dictionary.setPh_am(Ph_am);
////            dictionary.setPh_en(Ph_en);
////            dictionary.setPh_zh(Ph_zh);
////            dictionary.setType(Type);
////            dictionary.setQuestionVoiceId(QuestionVoiceId);
////            dictionary.setQuestionAudioPath(QuestionAudioPath);
////            dictionary.setResultVoiceId(ResultVoiceId);
////            dictionary.setResultAudioPath(ResultAudioPath);
////            dictionary.setIscollected(Iscollected);
////            dictionary.setVisit_times(Visit_times);
////            dictionary.setSpeak_speed(Speak_speed);
////            dictionary.setBackup1(Backup1);
////            dictionary.setBackup2(Backup2);
////            dictionary.setBackup3(Backup3);
////            dictionary.setBackup4(Backup4);
////            dictionary.setBackup5(Backup5);
////            backupList.add(dictionary);
//            ContentValues contentValues = new ContentValues();
//            if(!TextUtils.isEmpty(Word_name)){
//                LogUtil.DefalutLog("Word_name:"+Word_name);
//                contentValues.put(DictionaryDao.Properties.Word_name.columnName,Word_name);
//            }
//            if(!TextUtils.isEmpty(Result)){
//                LogUtil.DefalutLog("Result:"+Result);
//                contentValues.put(DictionaryDao.Properties.Result.columnName,Result);
//            }
//            if(!TextUtils.isEmpty(To)){
//                LogUtil.DefalutLog("To:"+To);
//                contentValues.put(DictionaryDao.Properties.To_lan.columnName,To);
//            }
//            if(!TextUtils.isEmpty(From)){
//                LogUtil.DefalutLog("From:"+From);
//                contentValues.put(DictionaryDao.Properties.From_lan.columnName,From);
//            }
//            if(!TextUtils.isEmpty(Ph_am)){
//                LogUtil.DefalutLog("Ph_am:"+Ph_am);
//                contentValues.put(DictionaryDao.Properties.Ph_am.columnName,Ph_am);
//            }
//            if(!TextUtils.isEmpty(Ph_en)){
//                LogUtil.DefalutLog("Ph_en:"+Ph_en);
//                contentValues.put(DictionaryDao.Properties.Ph_en.columnName,Ph_en);
//            }
//            if(!TextUtils.isEmpty(Ph_zh)){
//                contentValues.put(DictionaryDao.Properties.Ph_zh.columnName,Ph_zh);
//            }
//            if(!TextUtils.isEmpty(Type)){
//                contentValues.put(DictionaryDao.Properties.Type.columnName,Type);
//            }
//            if(!TextUtils.isEmpty(QuestionVoiceId)){
//                contentValues.put(DictionaryDao.Properties.QuestionVoiceId.columnName,QuestionVoiceId);
//            }
//            if(!TextUtils.isEmpty(QuestionAudioPath)){
//                contentValues.put(DictionaryDao.Properties.QuestionAudioPath.columnName,QuestionAudioPath);
//            }
//            if(!TextUtils.isEmpty(ResultVoiceId)){
//                contentValues.put(DictionaryDao.Properties.ResultVoiceId.columnName,ResultVoiceId);
//            }
//            if(!TextUtils.isEmpty(ResultAudioPath)){
//                contentValues.put(DictionaryDao.Properties.ResultAudioPath.columnName,ResultAudioPath);
//            }
//            if(!TextUtils.isEmpty(Iscollected)){
//            contentValues.put(DictionaryDao.Properties.Iscollected.columnName,Iscollected);
//            }
//            if(!TextUtils.isEmpty(Backup1)){
//            contentValues.put(DictionaryDao.Properties.Backup1.columnName,Backup1);
//            }
//            if(!TextUtils.isEmpty(Backup2)){
//            contentValues.put(DictionaryDao.Properties.Backup2.columnName,Backup2);
//            }
//            if(!TextUtils.isEmpty(Backup3)){
//            contentValues.put(DictionaryDao.Properties.Backup3.columnName,Backup3);
//            }
//            if(!TextUtils.isEmpty(Backup4)){
//            contentValues.put(DictionaryDao.Properties.Backup4.columnName,Backup4);
//            }
//            if(!TextUtils.isEmpty(Backup5)){
//            contentValues.put(DictionaryDao.Properties.Backup5.columnName,Backup5);
//            }
//            contentValues.put(DictionaryDao.Properties.Visit_times.columnName,Visit_times);
//            contentValues.put(DictionaryDao.Properties.Speak_speed.columnName,Speak_speed);
//            db.insert("DICTIONARY_T", null, contentValues);
//        }
////        SaveData.saveObject(BaseApplication.mInstance, "BackupDictionaryList", backupList);
//        cursor.close();
//        LogUtil.DefalutLog("saveTemp from DICTIONARY to DICTIONARY_T");
//    }
//
//    private static void backupFromTemp(SQLiteDatabase db) {
//        Cursor cursor = db.rawQuery("select * from DICTIONARY_T", null);
//        while (cursor.moveToNext()) {
//            String Word_name = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Word_name.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Result = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Result.columnName)); //获取第一列的值,第一列的索引从0开始
//            String To = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.To_lan.columnName)); //获取第一列的值,第一列的索引从0开始
//            String From = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.From_lan.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Ph_am = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Ph_am.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Ph_en = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Ph_en.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Ph_zh = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Ph_zh.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Type = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Type.columnName)); //获取第一列的值,第一列的索引从0开始
//            String QuestionVoiceId = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.QuestionVoiceId.columnName)); //获取第一列的值,第一列的索引从0开始
//            String QuestionAudioPath = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.QuestionAudioPath.columnName)); //获取第一列的值,第一列的索引从0开始
//            String ResultVoiceId = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.ResultVoiceId.columnName)); //获取第一列的值,第一列的索引从0开始
//            String ResultAudioPath = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.ResultAudioPath.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Iscollected = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Iscollected.columnName)); //获取第一列的值,第一列的索引从0开始
//            int Visit_times = cursor.getInt(cursor.getColumnIndex(DictionaryDao.Properties.Visit_times.columnName)); //获取第一列的值,第一列的索引从0开始
//            int Speak_speed = cursor.getInt(cursor.getColumnIndex(DictionaryDao.Properties.Speak_speed.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup1 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup1.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup2 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup2.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup3 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup3.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup4 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup4.columnName)); //获取第一列的值,第一列的索引从0开始
//            String Backup5 = cursor.getString(cursor.getColumnIndex(DictionaryDao.Properties.Backup5.columnName)); //获取第一列的值,第一列的索引从0开始
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(DictionaryDao.Properties.Word_name.columnName,Word_name);
//            contentValues.put(DictionaryDao.Properties.Result.columnName,Result);
//            contentValues.put(DictionaryDao.Properties.To_lan.columnName,To);
//            contentValues.put(DictionaryDao.Properties.From_lan.columnName,From);
//            contentValues.put(DictionaryDao.Properties.Ph_am.columnName,Ph_am);
//            contentValues.put(DictionaryDao.Properties.Ph_en.columnName,Ph_en);
//            contentValues.put(DictionaryDao.Properties.Ph_zh.columnName,Ph_zh);
//            contentValues.put(DictionaryDao.Properties.Type.columnName,Type);
//            contentValues.put(DictionaryDao.Properties.QuestionVoiceId.columnName,QuestionVoiceId);
//            contentValues.put(DictionaryDao.Properties.QuestionAudioPath.columnName,QuestionAudioPath);
//            contentValues.put(DictionaryDao.Properties.ResultVoiceId.columnName,ResultVoiceId);
//            contentValues.put(DictionaryDao.Properties.ResultAudioPath.columnName,ResultAudioPath);
//            contentValues.put(DictionaryDao.Properties.Iscollected.columnName,Iscollected);
//            contentValues.put(DictionaryDao.Properties.Visit_times.columnName,Visit_times);
//            contentValues.put(DictionaryDao.Properties.Speak_speed.columnName,Speak_speed);
//            contentValues.put(DictionaryDao.Properties.Backup1.columnName,Backup1);
//            contentValues.put(DictionaryDao.Properties.Backup2.columnName,Backup2);
//            contentValues.put(DictionaryDao.Properties.Backup3.columnName,Backup3);
//            contentValues.put(DictionaryDao.Properties.Backup4.columnName,Backup4);
//            contentValues.put(DictionaryDao.Properties.Backup5.columnName,Backup5);
//            db.insert("DICTIONARY", null, contentValues);
//        }
//        cursor.close();
//        LogUtil.DefalutLog("backupFromTemp to DICTIONARY");
//    }
//



}
