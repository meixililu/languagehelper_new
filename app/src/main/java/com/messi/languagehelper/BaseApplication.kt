package com.messi.languagehelper

import android.app.Application
import androidx.multidex.MultiDexApplication
import cn.leancloud.AVOSCloud
import com.facebook.drawee.backends.pipeline.Fresco
import com.messi.languagehelper.BaseApplication
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.util.*
import com.umeng.commonsdk.UMConfigure
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.youdao.sdk.app.YouDaoApplication
import java.util.*

class BaseApplication : MultiDexApplication() {

    companion object {
        var dataMap = HashMap<String, Any>()
        @JvmField
        var instance: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        try {
            instance = this
            Thread(Runnable { initPartOne() }).run()
            Thread(Runnable { initPartTwo() }).run()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initPartOne() {
        BoxHelper.init(this)
        initAVOSCloud()
        ADUtil.initAd(this)
        initXMLY()
    }

    private fun initPartTwo() {
        initFresco()
        initYouDao()
        initUM()
    }

    private fun initAVOSCloud() {
        try {
            val sp = Setings.getSharedPreferences(this)
            val ipAddress = sp.getString(KeyUtil.LeanCloudIPAddress, "http://leancloud.mzxbkj.com")
            AVOSCloud.initialize(this, "3fg5ql3r45i3apx2is4j9on5q5rf6kapxce51t5bc0ffw2y4",
                    "twhlgs6nvdt7z7sfaw76ujbmaw7l12gb8v6sdyjw1nzk9b1a", ipAddress)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initFresco() {
        try {
            SystemUtil.setPacketName(this)
            Fresco.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initYouDao() {
        try {
            YouDaoApplication.init(this, Setings.YoudaoApiKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initXMLY() {
        try {
            val mXimalaya = CommonRequest.getInstanse()
            if (packageName == Setings.application_id_yys) {
                Setings.XMLYAppAppKey = "e52a1a0372ff5d7dd81a672ef18ce41a"
                Setings.XMLYAppSecret = "4d6ccaf67441973ea5bac086fc193b2d"
            }
            //            else if(getPackageName().equals(Setings.application_id_yyj)){
//                Setings.XMLYAppAppKey = "1c5edba74bbef3d22e010920c1e04aa5";
//                Setings.XMLYAppSecret = "55eb5065bf697e31bf5f39a161b0a5d7";
//            }
            mXimalaya.setAppkey(Setings.XMLYAppAppKey)
            mXimalaya.setPackid(packageName)
            mXimalaya.init(this, Setings.XMLYAppSecret)
            LogUtil.DefalutLog("initXimalayaSDK")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initUM() {
        try {
//            UMConfigure.setLogEnabled(true);
            Setings.appVersion = Setings.getVersion(applicationContext)
            Setings.appChannel = Setings.getMetaData(applicationContext, "UMENG_CHANNEL")
            setAPPData()
            UMConfigure.init(this, Setings.UmengAPPId, Setings.appChannel, UMConfigure.DEVICE_TYPE_PHONE, "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAPPData() {
        when (packageName) {
            Setings.application_id_zyhy -> {
                Setings.UmengAPPId = "551e3853fd98c5403800122c"
            }
            Setings.application_id_zyhy_google -> {
                Setings.UmengAPPId = "551e3853fd98c5403800122c"
            }
            Setings.application_id_yys -> {
                Setings.UmengAPPId = "5c1f3af4b465f53ecc00093b"
            }
            Setings.application_id_yys_google -> {
                Setings.UmengAPPId = "5c1f3af4b465f53ecc00093b"
            }
            Setings.application_id_yyj -> {
                Setings.UmengAPPId = "5c1f36d4f1f55655d1000f88"
            }
            Setings.application_id_yyj_google -> {
                Setings.UmengAPPId = "5c1f36d4f1f55655d1000f88"
            }
            Setings.application_id_ywcd -> {
                Setings.UmengAPPId = "5c1f3b7bb465f5598b000f57"
            }
            Setings.application_id_xbky -> {
                Setings.UmengAPPId = "5c1f3b97f1f556949e0007b3"
            }
            Setings.application_id_xbtl -> {
                Setings.UmengAPPId = "5c1f3bb9b465f54c9700043c"
            }
            Setings.application_id_qmzj -> {
                Setings.UmengAPPId = "5c1f3be0f1f5566129000fcf"
            }
            Setings.application_id_zrhy -> {
                Setings.UmengAPPId = "5c1f3c0bb465f5341a000778"
            }
            Setings.application_id_zhhy -> {
                Setings.UmengAPPId = "5c1f3c2af1f5564f7a000035"
            }
            else -> {
                Setings.UmengAPPId = "551e3853fd98c5403800122c"
            }
        }
    }


}