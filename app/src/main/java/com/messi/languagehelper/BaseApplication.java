package com.messi.languagehelper;

import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.messi.languagehelper.dao.DaoMaster;
import com.messi.languagehelper.dao.DaoSession;
import com.messi.languagehelper.db.LHContract;
import com.messi.languagehelper.db.SQLiteOpenHelper;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.youdao.sdk.app.YouDaoApplication;

import java.util.HashMap;

public class BaseApplication extends MultiDexApplication {

	public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
	private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static BaseApplication mInstance;

    @Override  
    public void onCreate() {  
        super.onCreate();
        if(mInstance == null)  mInstance = this;
        initAVOS();

    }

    private void initAVOS(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                Fresco.initialize(BaseApplication.this);
                AVOSCloud.initialize(BaseApplication.this, "3fg5ql3r45i3apx2is4j9on5q5rf6kapxce51t5bc0ffw2y4", "twhlgs6nvdt7z7sfaw76ujbmaw7l12gb8v6sdyjw1nzk9b1a");
                YouDaoApplication.init(BaseApplication.this, Setings.YoudaoApiKey);
                AVAnalytics.enableCrashReport(BaseApplication.this, true);
                initLearnCloudChannel();
            }
        }).run();
        CommonRequest.getInstanse().init(BaseApplication.this, "c779eeb1873325a487e2956a2077f2bc");
        CommonRequest.getInstanse().setHttpConfig(null);
        CommonRequest.getInstanse().setUseHttps(true);
        LogUtil.DefalutLog("initXimalayaSDK");
    }

    private void initLearnCloudChannel(){
        if(getPackageName().equals(Setings.application_id_zyhy)){
            AVAnalytics.setAppChannel("LeanCloud");
        }else if(getPackageName().equals(Setings.application_id_zyhy_google)){
            AVAnalytics.setAppChannel("zyhy_google");
        }else if(getPackageName().equals(Setings.application_id_yys)){
            AVAnalytics.setAppChannel("cantonese_study");
        }else if(getPackageName().equals(Setings.application_id_yys_google)){
            AVAnalytics.setAppChannel("yys_google");
        }else if(getPackageName().equals(Setings.application_id_yyj)){
            AVAnalytics.setAppChannel("learnEnglish");
        }else if(getPackageName().equals(Setings.application_id_yyj_google)){
            AVAnalytics.setAppChannel("yyj_google");
        }else if(getPackageName().equals(Setings.application_id_yycd)){
            AVAnalytics.setAppChannel("ywcd");
        }else if(getPackageName().equals(Setings.application_id_xbky)){
            AVAnalytics.setAppChannel("xbky");
        }else{
            AVAnalytics.setAppChannel("other");
        }
    }
	/**
     * 取得DaoMaster
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            SQLiteOpenHelper helper = new SQLiteOpenHelper(context,LHContract.DATABASE_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
