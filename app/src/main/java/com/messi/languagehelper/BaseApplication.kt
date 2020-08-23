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
        val start = System.currentTimeMillis()
        init()
        val end = System.currentTimeMillis()
        LogUtil.DefalutLog("BaseApplication:"+(end-start))
    }

    private fun init() {
        instance = this
        try {
            initAVOSCloud()
            ADUtil.initAd(this)
            initFresco()
            BoxHelper.init(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            Fresco.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}