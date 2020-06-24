package com.messi.languagehelper;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.umeng.commonsdk.UMConfigure;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.youdao.sdk.app.YouDaoApplication;

import java.util.HashMap;

import cn.leancloud.AVOSCloud;


public class BaseApplication extends MultiDexApplication {

	public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
    public static Application instance;

    @Override  
    public void onCreate() {  
        super.onCreate();
        init();
    }

    private void init(){
        try {
            instance = this;
            new Thread(() -> initPartOne()).run();
            new Thread(() -> initPartTwo()).run();
            BoxHelper.init(BaseApplication.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPartOne(){
        initAVOSCloud();
        ADUtil.initAd(this);
        initXMLY();
    }
     private void initPartTwo(){
         initFresco();
         initYouDao();
         initUM();
    }

    private void initAVOSCloud(){
        try {
            SharedPreferences sp = Setings.getSharedPreferences(BaseApplication.this);
            String ipAddress = sp.getString(KeyUtil.LeanCloudIPAddress,"http://leancloud.mzxbkj.com");
            AVOSCloud.initialize(BaseApplication.this,"3fg5ql3r45i3apx2is4j9on5q5rf6kapxce51t5bc0ffw2y4",
                    "twhlgs6nvdt7z7sfaw76ujbmaw7l12gb8v6sdyjw1nzk9b1a", ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFresco(){
        try {
            SystemUtil.setPacketName(BaseApplication.this);
            Fresco.initialize(BaseApplication.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initYouDao(){
        try {
            YouDaoApplication.init(BaseApplication.this, Setings.YoudaoApiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initXMLY(){
        try {
            CommonRequest mXimalaya = CommonRequest.getInstanse();
            if(getPackageName().equals(Setings.application_id_yys)){
                Setings.XMLYAppAppKey = "e52a1a0372ff5d7dd81a672ef18ce41a";
                Setings.XMLYAppSecret = "4d6ccaf67441973ea5bac086fc193b2d";
            }
//            else if(getPackageName().equals(Setings.application_id_yyj)){
//                Setings.XMLYAppAppKey = "1c5edba74bbef3d22e010920c1e04aa5";
//                Setings.XMLYAppSecret = "55eb5065bf697e31bf5f39a161b0a5d7";
//            }
            mXimalaya.setAppkey(Setings.XMLYAppAppKey);
            mXimalaya.setPackid(getPackageName());
            mXimalaya.init(BaseApplication.this, Setings.XMLYAppSecret);
            LogUtil.DefalutLog("initXimalayaSDK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUM(){
        try {
//            UMConfigure.setLogEnabled(true);
            Setings.appVersion = Setings.getVersion(getApplicationContext());
            Setings.appChannel = Setings.getMetaData(getApplicationContext(),"UMENG_CHANNEL");
            setAPPData();
            UMConfigure.init(BaseApplication.this,Setings.UmengAPPId,Setings.appChannel,UMConfigure.DEVICE_TYPE_PHONE,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
