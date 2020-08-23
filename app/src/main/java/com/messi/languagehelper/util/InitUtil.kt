package com.messi.languagehelper.util

import android.content.Context
import com.umeng.commonsdk.UMConfigure
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.youdao.sdk.app.YouDaoApplication

class InitUtil {

    companion object {

        fun init(context: Context){
            LogUtil.DefalutLog("InitUtil---init---start")
            SystemUtil.setPacketName(context)
            initYouDao(context)
            initXMLY(context)
            initUM(context)
            LogUtil.DefalutLog("InitUtil---init---end")
        }

        private fun initYouDao(context: Context) {
            try {
                YouDaoApplication.init(context.applicationContext, Setings.YoudaoApiKey)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun initXMLY(context: Context) {
            try {
                 val mXimalaya = CommonRequest.getInstanse()
                 if (context.packageName == Setings.application_id_yys) {
                     Setings.XMLYAppAppKey = "e52a1a0372ff5d7dd81a672ef18ce41a"
                     Setings.XMLYAppSecret = "4d6ccaf67441973ea5bac086fc193b2d"
                 }
                //            else if(getPackageName().equals(Setings.application_id_yyj)){
//                Setings.XMLYAppAppKey = "1c5edba74bbef3d22e010920c1e04aa5";
//                Setings.XMLYAppSecret = "55eb5065bf697e31bf5f39a161b0a5d7";
//            }
                mXimalaya.setAppkey(Setings.XMLYAppAppKey)
                mXimalaya.setPackid(context.packageName)
                mXimalaya.init(context.applicationContext, Setings.XMLYAppSecret)
                LogUtil.DefalutLog("initXimalayaSDK")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun initUM(context: Context) {
            try {
                Setings.appVersion = Setings.getVersion(context)
                Setings.appChannel = Setings.getMetaData(context, "UMENG_CHANNEL")
                setAPPData(context)
                UMConfigure.init(context.applicationContext, Setings.UmengAPPId, Setings.appChannel, UMConfigure.DEVICE_TYPE_PHONE, "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun setAPPData(context: Context) {
            when (context.packageName) {
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

}