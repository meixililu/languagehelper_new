package com.messi.languagehelper;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVOSCloud;
import com.baidu.mobads.AdView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.dao.DaoMaster;
import com.messi.languagehelper.dao.DaoSession;
import com.messi.languagehelper.db.LHContract;
import com.messi.languagehelper.db.SQLiteOpenHelper;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.umeng.commonsdk.UMConfigure;
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
//                try {
                    Fresco.initialize(BaseApplication.this);
                    AVOSCloud.initialize(mInstance,"3fg5ql3r45i3apx2is4j9on5q5rf6kapxce51t5bc0ffw2y4", "twhlgs6nvdt7z7sfaw76ujbmaw7l12gb8v6sdyjw1nzk9b1a");
                    YouDaoApplication.init(BaseApplication.this, Setings.YoudaoApiKey);
                    initChannel();
                    initXMLY();
                    CSJADUtil.init(BaseApplication.this);
                    BoxHelper.init(BaseApplication.this);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }).run();
    }

    private void initXMLY(){
        try {
            CommonRequest.getInstanse().init(BaseApplication.this, "c779eeb1873325a487e2956a2077f2bc");
            CommonRequest.getInstanse().setHttpConfig(null);
            CommonRequest.getInstanse().setUseHttps(true);
            LogUtil.DefalutLog("initXimalayaSDK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChannel(){
        UMConfigure.setLogEnabled(true);
        Setings.appVersion = Setings.getVersion(getApplicationContext());
        Setings.appChannel = Setings.getMetaData(getApplicationContext(),"UMENG_CHANNEL");
        if(getPackageName().equals(Setings.application_id_zyhy)){
            AdView.setAppSid(getApplicationContext(),"f60acdfd");
            UMConfigure.init(mInstance,"551e3853fd98c5403800122c",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_zyhy_google)){
            AdView.setAppSid(getApplicationContext(),"f60acdfd");
            UMConfigure.init(mInstance,"551e3853fd98c5403800122c",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_yys)){
            UMConfigure.init(mInstance,"5c1f3af4b465f53ecc00093b",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_yys_google)){
            UMConfigure.init(mInstance,"5c1f3af4b465f53ecc00093b",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_yyj)){
            UMConfigure.init(mInstance,"5c1f36d4f1f55655d1000f88",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_yyj_google)){
            UMConfigure.init(mInstance,"5c1f36d4f1f55655d1000f88",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_ywcd)){
            UMConfigure.init(mInstance,"5c1f3b7bb465f5598b000f57",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_xbky)){
            UMConfigure.init(mInstance,"5c1f3b97f1f556949e0007b3",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_xbtl)){
            UMConfigure.init(mInstance,"5c1f3bb9b465f54c9700043c",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_qmzj)){
            UMConfigure.init(mInstance,"5c1f3be0f1f5566129000fcf",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_zrhy)){
            UMConfigure.init(mInstance,"5c1f3c0bb465f5341a000778",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else if(getPackageName().equals(Setings.application_id_zhhy)){
            UMConfigure.init(mInstance,"5c1f3c2af1f5564f7a000035",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        }else{
            AdView.setAppSid(getApplicationContext(),"f60acdfd");
            UMConfigure.init(mInstance,"551e3853fd98c5403800122c",Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
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
