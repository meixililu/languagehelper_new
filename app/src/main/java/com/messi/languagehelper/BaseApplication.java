package com.messi.languagehelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.iflytek.voiceads.dex.DexLoader;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.dao.DaoMaster;
import com.messi.languagehelper.dao.DaoSession;
import com.messi.languagehelper.db.LHContract;
import com.messi.languagehelper.db.SQLiteOpenHelper;
import com.messi.languagehelper.util.BDADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.TXADUtil;
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
        init();
    }

    private void init(){
        if(mInstance == null)  mInstance = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemUtil.setPacketName(BaseApplication.this);
                    Fresco.initialize(BaseApplication.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    SharedPreferences sp = Setings.getSharedPreferences(BaseApplication.this);
                    String ipAddress = sp.getString(KeyUtil.LeanCloudIPAddress,"http://leancloud.mzxbkj.com");
                    AVOSCloud.setServer(AVOSCloud.SERVER_TYPE.API, ipAddress);
                    AVOSCloud.initialize(BaseApplication.this,"3fg5ql3r45i3apx2is4j9on5q5rf6kapxce51t5bc0ffw2y4", "twhlgs6nvdt7z7sfaw76ujbmaw7l12gb8v6sdyjw1nzk9b1a");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    YouDaoApplication.init(BaseApplication.this, Setings.YoudaoApiKey);
                    DexLoader.initIFLYADModule(BaseApplication.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initChannel();
                initXMLY();
                TXADUtil.init(BaseApplication.this);
                CSJADUtil.init(BaseApplication.this);
                BDADUtil.init(BaseApplication.this);
                BoxHelper.init(BaseApplication.this);
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
        try {
//            UMConfigure.setLogEnabled(true);
            Setings.appVersion = Setings.getVersion(getApplicationContext());
            Setings.appChannel = Setings.getMetaData(getApplicationContext(),"UMENG_CHANNEL");
            setAPPData();
            UMConfigure.init(mInstance,Setings.UmengAPPId,Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            SQLiteOpenHelper helper = new SQLiteOpenHelper(context,LHContract.DATABASE_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public void setAPPData(){
        if(getPackageName().equals(Setings.application_id_zyhy)){
            Setings.UmengAPPId = "551e3853fd98c5403800122c";
        }else if(getPackageName().equals(Setings.application_id_zyhy_google)){
            Setings.UmengAPPId = "551e3853fd98c5403800122c";
        }else if(getPackageName().equals(Setings.application_id_yys)){
            Setings.UmengAPPId = "5c1f3af4b465f53ecc00093b";
        }else if(getPackageName().equals(Setings.application_id_yys_google)){
            Setings.UmengAPPId = "5c1f3af4b465f53ecc00093b";
        }else if(getPackageName().equals(Setings.application_id_yyj)){
            Setings.UmengAPPId = "5c1f36d4f1f55655d1000f88";
        }else if(getPackageName().equals(Setings.application_id_yyj_google)){
            Setings.UmengAPPId = "5c1f36d4f1f55655d1000f88";
        }else if(getPackageName().equals(Setings.application_id_ywcd)){
            Setings.UmengAPPId = "5c1f3b7bb465f5598b000f57";
        }else if(getPackageName().equals(Setings.application_id_xbky)){
            Setings.UmengAPPId = "5c1f3b97f1f556949e0007b3";
        }else if(getPackageName().equals(Setings.application_id_xbtl)){
            Setings.UmengAPPId = "5c1f3bb9b465f54c9700043c";
        }else if(getPackageName().equals(Setings.application_id_qmzj)){
            Setings.UmengAPPId = "5c1f3be0f1f5566129000fcf";
        }else if(getPackageName().equals(Setings.application_id_zrhy)){
            Setings.UmengAPPId = "5c1f3c0bb465f5341a000778";
        }else if(getPackageName().equals(Setings.application_id_zhhy)){
            Setings.UmengAPPId = "5c1f3c2af1f5564f7a000035";
        }else{
            Setings.UmengAPPId = "551e3853fd98c5403800122c";
        }
    }

}
