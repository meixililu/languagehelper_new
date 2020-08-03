package com.messi.languagehelper.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.messi.languagehelper.R
import com.messi.languagehelper.box.BoxHelper
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppUpdateUtil {
    @JvmStatic
    fun runCheckUpdateTask(mActivity: Activity) {
        checkUpdate(mActivity.applicationContext)
        initXMLY(mActivity)
        ADUtil.initTXADID(mActivity.applicationContext)
    }

    private fun initXMLY(mActivity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            XmPlayerManager.getInstance(mActivity.applicationContext).init()
            XmPlayerManager.getInstance(mActivity.applicationContext).setCommonBusinessHandle(XmDownloadManager.getInstance())
            val dm = DisplayMetrics()
            mActivity.windowManager.defaultDisplay.getMetrics(dm)
            SystemUtil.SCREEN_WIDTH = dm.widthPixels
            SystemUtil.SCREEN_HEIGHT = dm.heightPixels
            SystemUtil.screen = SystemUtil.SCREEN_WIDTH.toString() + "x" + SystemUtil.SCREEN_HEIGHT
        }
    }

    @JvmStatic
    val webFilter: Unit
        get() {
            CoroutineScope(Dispatchers.IO).launch {
                val query = AVQuery<AVObject>(AVOUtil.AdFilter.AdFilter)
                query.whereContains(AVOUtil.AdFilter.category, "ca_novel")
                val list = query.find()
                if (list != null) {
                    val beans = DataUtil.toWebFilter(list)
                    BoxHelper.updateWebFilter(beans)
                }
            }
        }

    @JvmStatic
    fun checkUpdate(mContext: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val query = AVQuery<AVObject>(AVOUtil.UpdateInfo.UpdateInfo)
            when (mContext.packageName) {
                Setings.application_id_zyhy -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zyhy")
                }
                Setings.application_id_zyhy_google -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zyhy_google")
                }
                Setings.application_id_yys -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yys")
                }
                Setings.application_id_yys_google -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yys_google")
                }
                Setings.application_id_yyj -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yyj")
                }
                Setings.application_id_yyj_google -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yyj_google")
                }
                Setings.application_id_ywcd -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "ywcd")
                }
                Setings.application_id_xbky -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "xbky")
                }
                Setings.application_id_xbtl -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "xbtl")
                }
                Setings.application_id_qmzj -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "qmzj")
                }
                Setings.application_id_zrhy -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zrhy")
                }
                Setings.application_id_zhhy -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zhhy")
                }
                else -> {
                    query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "noupdate")
                }
            }
            var avObjects = query.find()
            if (NullUtil.isNotEmpty(avObjects)) {
                val mAVObject = avObjects[0]
                saveSetting(mContext.applicationContext, mAVObject)
            }
        }
    }

    fun saveSetting(context: Context, mAVObject: AVObject) {
        val sp = Setings.getSharedPreferences(context)
        LogUtil.DefalutLog(mAVObject.getString(AVOUtil.UpdateInfo.AppName))
        val app_advertiser = mAVObject.getString(AVOUtil.UpdateInfo.ad_type)
        val wyyx_url = mAVObject.getString(AVOUtil.UpdateInfo.wyyx_url)
        val uctt_url = mAVObject.getString(AVOUtil.UpdateInfo.uctt_url)
        val yuey_url = mAVObject.getString(AVOUtil.UpdateInfo.yuey_url)
        val ucsearch_url = mAVObject.getString(AVOUtil.UpdateInfo.ucsearch_url)
        val tran_order = mAVObject.getString(AVOUtil.UpdateInfo.tran_order)
        val ad_ids = mAVObject.getString(AVOUtil.UpdateInfo.ad_ids)
        val ad_csj = mAVObject.getString(AVOUtil.UpdateInfo.ad_csj)
        val ad_bd = mAVObject.getString(AVOUtil.UpdateInfo.ad_bd)
        val no_ad_channel = mAVObject.getString(AVOUtil.UpdateInfo.no_ad_channel)
        val interceptUrls = mAVObject.getString(AVOUtil.UpdateInfo.interceptUrls)
        val adConf = mAVObject.getString(AVOUtil.UpdateInfo.adConf)
        val Caricature_channel = mAVObject.getString(AVOUtil.UpdateInfo.Caricature_channel)
        val domain = mAVObject.getString(AVOUtil.UpdateInfo.domain)
        val Caricature_version = mAVObject.getInt(AVOUtil.UpdateInfo.Caricature_version)
        val HjCookie = mAVObject.getString(AVOUtil.UpdateInfo.HjCookie)
        val UseNewPVApi = mAVObject.getString(AVOUtil.UpdateInfo.UseNewPVApi)
        TranslateHelper.setHjCookie(HjCookie)
        ADUtil.setAdConfig(adConf)
        TXADUtil.setADData(ad_ids)
        CSJADUtil.setADData(ad_csj)
        BDADUtil.setADData(ad_bd)
        KTranslateHelper.initTranOrder(tran_order)
        Setings.saveSharedPreferences(sp, KeyUtil.TranOrder, tran_order)
        Setings.saveSharedPreferences(sp, KeyUtil.InterceptUrls, interceptUrls)
        Setings.saveSharedPreferences(sp, KeyUtil.APP_Advertiser, app_advertiser)
        Setings.saveSharedPreferences(sp, KeyUtil.Lei_DVideo, uctt_url)
        Setings.saveSharedPreferences(sp, KeyUtil.Lei_Novel, wyyx_url)
        Setings.saveSharedPreferences(sp, KeyUtil.YueYUrl, yuey_url)
        Setings.saveSharedPreferences(sp, KeyUtil.Lei_UCSearch, ucsearch_url)
        Setings.saveSharedPreferences(sp, KeyUtil.Ad_Ids, ad_ids)
        Setings.saveSharedPreferences(sp, KeyUtil.Ad_Csj, ad_csj)
        Setings.saveSharedPreferences(sp, KeyUtil.Ad_Bd, ad_bd)
        Setings.saveSharedPreferences(sp, KeyUtil.LeanCloudIPAddress, domain)
        Setings.saveSharedPreferences(sp, KeyUtil.No_Ad_Channel, no_ad_channel)
        Setings.saveSharedPreferences(sp, KeyUtil.AdConfig, adConf)
        Setings.saveSharedPreferences(sp, KeyUtil.VersionCode,
                mAVObject.getInt(AVOUtil.UpdateInfo.VersionCode))
        Setings.saveSharedPreferences(sp, KeyUtil.Caricature_version, Caricature_version)
        Setings.saveSharedPreferences(sp, KeyUtil.Caricature_channel, Caricature_channel)
        Setings.saveSharedPreferences(sp, KeyUtil.UseNewPVApi, UseNewPVApi)
        Setings.saveSharedPreferences(sp, KeyUtil.UpdateBean, mAVObject.toString())
    }

    @JvmStatic
    fun isNeedUpdate(mActivity: Activity) {
        Handler().postDelayed({ showUpdateDialog(mActivity) }, 3500)
    }

    private fun showUpdateDialog(mActivity: Activity) {
        try {
            if (mActivity.isFinishing) return
            val sp = Setings.getSharedPreferences(mActivity)
            val mAVObject = AVObject.parseAVObject(sp.getString(KeyUtil.UpdateBean, ""))
            val isValid = mAVObject.getString(AVOUtil.UpdateInfo.IsValid)
            if (!TextUtils.isEmpty(isValid) && isValid == "3") {
                val newVersionCode = mAVObject.getInt(AVOUtil.UpdateInfo.VersionCode)
                val oldVersionCode = Setings.getVersion(mActivity)
                if (newVersionCode > oldVersionCode) {
                    val updateInfo = mAVObject.getString(AVOUtil.UpdateInfo.AppUpdateInfo)
                    val apkUrl = mAVObject.getString(AVOUtil.UpdateInfo.APPUrl)
                    //                    LogUtil.DefalutLog("apkUrl:" + apkUrl);
                    val view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_update_info, null)
                    val updage_info = view.findViewById<View>(R.id.updage_info) as TextView
                    val cancel_btn = view.findViewById<View>(R.id.cancel_btn) as ImageView
                    val update_btn = view.findViewById<View>(R.id.update_btn) as TextView
                    val dialog = AlertDialog.Builder(mActivity).create()
                    dialog.setView(view)
                    dialog.setCancelable(false)
                    updage_info.text = updateInfo
                    cancel_btn.setOnClickListener { dialog.dismiss() }
                    update_btn.setOnClickListener {
                        dialog.dismiss()
                        AppDownloadUtil(mActivity,
                                apkUrl,
                                mAVObject.getString(AVOUtil.UpdateInfo.AppName),
                                mAVObject.objectId,
                                SDCardUtil.apkUpdatePath
                        ).DownloadFile()
                    }
                    dialog.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}