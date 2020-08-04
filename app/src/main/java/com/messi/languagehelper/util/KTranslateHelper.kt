package com.messi.languagehelper.util

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import cn.leancloud.json.JSON
import com.messi.languagehelper.bean.HjTranBean
import com.messi.languagehelper.bean.IcibaNew
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.Record
import com.messi.languagehelper.http.BgCallback
import com.messi.languagehelper.http.LanguagehelperHttpClient
import com.messi.languagehelper.httpservice.RetrofitBuilder
import io.reactivex.ObservableEmitter
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLEncoder

object KTranslateHelper {

    //  jscb,youdaoweb,bingweb,hujiangweb,qqfyj,hujiangapi,youdaoapi,baidu#youdaoweb,bingweb,hujiangweb,jscb,qqfyj,youdaoapi,hujiangapi,baidu
    private var OrderTran = "jscb,youdaoweb,bingweb,hujiangweb,hujiangapi,qqfyj,youdaoapi,baidu"
    private val youdaoweb = "youdaoweb"
    private val youdaoapi = "youdaoapi"
    private val bingweb = "bingweb"
    private val jscb = "jscb"
    private val xbkj = "xbkj"
    private val hujiangapi = "hujiangapi"
    private val hujiangweb = "hujiangweb"
    private val qqfyj = "qqfyj"
    private val baidu = "baidu"
    private lateinit var tranOrder: MutableList<String>
    var mRespoData: MutableLiveData<RespoData<Record>>? = null

    fun initTranOrder(orderStr: String) {
        LogUtil.DefalutLog("---initTranOrder---")
        try {
            if (!TextUtils.isEmpty(orderStr) && orderStr!!.contains("#")) {
                val keys = orderStr.split("#".toRegex()).toTypedArray()
                if (keys != null && keys.size > 1) {
                    if (!TextUtils.isEmpty(keys[0])) {
                        OrderTran = keys[0]
                    }
                    if (!TextUtils.isEmpty(keys[1])) {
                        DictHelper.OrderDic = keys[1]
                    }
                }
            } else {
                OrderTran = "jscb,youdaoweb,bingweb,hujiangweb,qqfyj,hujiangapi,baidu,youdaoapi"
            }
            tranOrder = OrderTran.split(",").toMutableList()
        } catch (e: Exception) {
            tranOrder = OrderTran.split(",").toMutableList()
            e.printStackTrace()
        }
        tranOrder.add(0,xbkj)
        tranOrder.add(qqfyj)
    }

    suspend fun doTranslateTask():Record? {
        var mRecordResult: Record? = null
        for(method in tranOrder){
            LogUtil.DefalutLog("DoTranslateByMethod---$method")
            when (method){
                youdaoweb -> {
                    mRecordResult = tranFromYDWeb()
                }
                jscb -> {
                    mRecordResult = tranFromICiBa()
                }
                xbkj -> {
                    mRecordResult = tranFromXBKJ()
                }
                youdaoapi -> {
                    mRecordResult = tranFromYDApi()
                }
                bingweb -> {
                    mRecordResult = tranFromBingWeb()
                }
                hujiangapi -> {
                    mRecordResult = tranFromHJApi()
                }
                hujiangweb -> {
                    mRecordResult = tranFromHjWeb()
                }
                qqfyj -> {
                    mRecordResult = tranFromQQFYJ()
                }
                baidu -> {
                    mRecordResult = tranFromXBKJ()
                }
            }
            if (mRecordResult != null) break
        }
        return mRecordResult
    }

    private suspend fun tranFromXBKJ(): Record? {
        LogUtil.DefalutLog("Result---zyhy server")
        var mRecord: Record? = null
        val timestamp = System.currentTimeMillis().toString()
        val platform = SystemUtil.platform
        val network = SystemUtil.network
        if (StringUtils.isEnglish(Setings.q)) {
            Setings.from = "en"
            Setings.to = "zh"
        } else {
            Setings.from = "zh"
            Setings.to = "en"
        }
        val sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, Setings.q,
                platform, network, Setings.from, Setings.to)
        var responseResult = RetrofitBuilder.tService.tranDict(Setings.q, Setings.from, Setings.to, network, platform, sign, 0, timestamp)
        val mResult = responseResult.body()
        if (mResult != null) {
            val tdResult = mResult.result
            if (tdResult != null && !TextUtils.isEmpty(tdResult.result)) {
                var des = tdResult.result
                if (!TextUtils.isEmpty(tdResult.symbol)) {
                    des = tdResult.symbol + "\n" + tdResult.result
                }
                mRecord = Record(des, Setings.q)
                mRecord.ph_am_mp3 = tdResult.mp3_am
                mRecord.ph_en_mp3 = tdResult.mp3_en
                mRecord.backup1 = tdResult.result
            }
        }
        return mRecord
    }

    private suspend fun tranFromICiBa(): Record? {
        LogUtil.DefalutLog("Result---Tran_Iciba")
        //zh  en  ja  ko  fr  de  es
        var result: Record? = null
        try {
            var from = "zh"
            var to = "en"
            if (StringUtils.isEnglish(Setings.q)) {
                from = "en"
                to = "zh"
            }
            var service = RetrofitBuilder.getKTranRetrofitService(Setings.IcibaTranslateBaseUrl)
            var mIcibaNew = service.tranByJSCB(Setings.q,from,to)
            result = mIcibaNew.body()?.let { fromIcibaNewToRecord(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private suspend fun tranFromQQFYJ(): Record? {
        LogUtil.DefalutLog("Result---TranQQFYJApi")
        var result: Record? = null
        try {
            val time_stamp = (System.currentTimeMillis() / 1000).toString()
            val nonce_str = StringUtils.getRandomString(16)
            var source = "zh"
            var target = "en"
            if (StringUtils.isEnglish(Setings.q)) {
                source = "en"
                target = "zh"
            }
            var map = sortedMapOf("app_id" to URLEncoder.encode(Setings.QQAPPID, "UTF-8"),
                    "nonce_str" to URLEncoder.encode(nonce_str, "UTF-8"),
                    "text" to URLEncoder.encode(Setings.q, "UTF-8"),
                    "time_stamp" to URLEncoder.encode(time_stamp, "UTF-8"),
                    "source" to URLEncoder.encode(source, "UTF-8"),
                    "target" to URLEncoder.encode(target, "UTF-8")
            )
            val sign = getSortData(map)
            var service = RetrofitBuilder.getKTranRetrofitService(Setings.QQTranFYJBase)
            var response = service.tranByQQFYJ(Setings.QQAPPID,time_stamp,sign,source,target,Setings.q,nonce_str)
            val data = response.body()
            if (data != null && data.ret == 0 && data.data != null) {
                result = Record(data.data.target_text, Setings.q)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private suspend fun tranFromYDWeb(): Record? {
        LogUtil.DefalutLog("Result---TranYoudaoWeb")
        var result: Record? = null
        try {
            var service = RetrofitBuilder.getKTranRetrofitService(Setings.YoudaoWeb)
            var response = service.tranByYDWeb(Setings.q)
            var data = response.body()
            if (data != null) {
                val responseString = data.string()
                if (!TextUtils.isEmpty(responseString)) {
                    result = getParseYoudaoWebHtml(responseString)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private suspend fun tranFromBingWeb(): Record? {
        LogUtil.DefalutLog("Result---TranBingWeb:")
        var result: Record? = null
        try {
            var service = RetrofitBuilder.getKTranRetrofitService(Setings.BingyingWebBase)
            var response = service.tranByBingWeb(Setings.q)
            var body = response.body()
            if (body != null) {
                val data = body.string()
                if (!TextUtils.isEmpty(data)) {
                    result = getParseBingyingWebHtml(data)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private suspend fun tranFromHjWeb(): Record?{
        LogUtil.DefalutLog("Result---Tran_Hj_Web")
        var result: Record? = null
        try {
            val request = Request.Builder()
                    .url(Setings.HjiangWeb + StringUtils.replaceAll(Setings.q))
                    .header("User-Agent", LanguagehelperHttpClient.Header)
                    .header("Cookie", TranslateHelper.HJCookie)
                    .build()
            var response = LanguagehelperHttpClient.get(request,null)
            LogUtil.DefalutLog(request.url.toUrl().toString())
            if (response.isSuccessful) {
                val responseString = response.body?.string()
                if (!TextUtils.isEmpty(responseString)) {
                    result = responseString?.let { getParseHjiangWebHtml(it) }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private suspend fun tranFromYDApi(): Record? {
        LogUtil.DefalutLog("Result---TranYoudaoApi")
        var mRecord: Record? = null
        try {
            var service = RetrofitBuilder.getKTranRetrofitService(Setings.YoudaoApiBase)
            var response = service.tranByYDApi(Setings.q)
            var bean = response.body()
            if (bean != null) {
                if (bean != null && bean.errorCode == 0 && bean.translateResult != null) {
                    val list = bean.translateResult
                    if (list.size > 0) {
                        val item = list[0]
                        if (item != null && item.size > 0) {
                            val result = item[0]
                            if (result != null && !TextUtils.isEmpty(result.tgt)) {
                                mRecord = Record(result.tgt, Setings.q)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mRecord
    }

    private suspend fun tranFromHJApi(): Record? {
        LogUtil.DefalutLog("Result---TranHjApi")
        var mRecord: Record? = null
        try {
            var response = LanguagehelperHttpClient.postHjApi(null)
            if (response.isSuccessful){
                var dataString = response.body?.string()
                mRecord = dataString?.let { tran_hj_api(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mRecord
    }

    @Throws(Exception::class)
    private fun tran_hj_api(mResult: String): Record? {
        var currentDialogBean: Record? = null
        if (!TextUtils.isEmpty(mResult)) {
            if (JsonParser.isJson(mResult)) {
                val mHjTranBean = JSON.parseObject(mResult, HjTranBean::class.java)
                if (mHjTranBean != null && mHjTranBean.status == 0 && mHjTranBean.data != null && !TextUtils.isEmpty(mHjTranBean.data.content)) {
                    currentDialogBean = Record(mHjTranBean.data.content, Setings.q)
                    LogUtil.DefalutLog("tran_hj_api http:" + mHjTranBean.data.content)
                }
            }
        }
        LogUtil.DefalutLog("tran_hj_api")
        return currentDialogBean
    }

    @Throws(Exception::class)
    private fun fromIcibaNewToRecord(mIciba: IcibaNew): Record? {
        var result: Record? = null
        if (mIciba != null && mIciba.content != null) {
            if (mIciba.status == "0") {
                val sb = StringBuilder()
                val sbplay = StringBuilder()
                if (!TextUtils.isEmpty(mIciba.content.ph_en)) {
                    sb.append("英[")
                    sb.append(mIciba.content.ph_en)
                    sb.append("]    ")
                }
                if (!TextUtils.isEmpty(mIciba.content.ph_am)) {
                    sb.append("美[")
                    sb.append(mIciba.content.ph_am)
                    sb.append("]")
                }
                if (sb.length > 0) {
                    sb.append("\n")
                }
                if (mIciba.content.word_mean != null) {
                    for (item in mIciba.content.word_mean) {
                        sb.append(item.trim { it <= ' ' })
                        sb.append("\n")
                        sbplay.append(item.trim { it <= ' ' })
                        sbplay.append(",")
                    }
                }
                var resutlStr: String? = ""
                resutlStr = if (sb.lastIndexOf("\n") > 0) {
                    sb.substring(0, sb.lastIndexOf("\n"))
                } else {
                    sb.toString()
                }
                result = Record(resutlStr, Setings.q)
                result.ph_am_mp3 = mIciba.content.ph_am_mp3
                result.ph_en_mp3 = mIciba.content.ph_en_mp3
                result.ph_tts_mp3 = mIciba.content.ph_tts_mp3
                result.backup1 = sbplay.toString()
                if (!TextUtils.isEmpty(mIciba.content.ph_tts_mp3)) {
                    result.backup3 = mIciba.content.ph_tts_mp3
                }
            } else if (mIciba.status == "1") {
                result = Record(mIciba.content.out.replace("<br/>".toRegex(), "").trim { it <= ' ' }, Setings.q)
            }
        }
        LogUtil.DefalutLog("tran_jscb_api")
        return result
    }

    fun getParseYoudaoWebHtml(html: String): Record? {
        val sb = StringBuilder()
        val sb_play = StringBuilder()
        var mrecord: Record? = null
        val doc = Jsoup.parse(html)
        val error = doc.select("div.error-wrapper").first()
        if (error != null) {
//            LogUtil.DefalutLog(error.text());
            return null
        }
        val feedback = doc.select("div.feedback").first()
        if (feedback != null) {
//            LogUtil.DefalutLog(feedback.text());
            return null
        }
        val symblo = doc.select("h2.wordbook-js > div.baav").first()
        if (symblo != null) {
            TranslateUtil.addContent(symblo, sb)
        }
        val translate = doc.select("div#phrsListTab > div.trans-container").first()
        if (translate != null) {
            val lis = translate.getElementsByTag("ul").first()
            if (lis != null) {
                for (li in lis.children()) {
                    TranslateUtil.addContentAll(li, sb, sb_play)
                }
            }
            val p = translate.select("p.additional").first()
            if (p != null) {
                TranslateUtil.addContentAll(p, sb, sb_play)
            }
        }
        val fanyiToggle = doc.select("div#fanyiToggle").first()
        if (fanyiToggle != null) {
            val lis = fanyiToggle.getElementsByTag("p")
            if (lis != null && lis.size > 1) {
                TranslateUtil.addContentAll(lis[1], sb, sb_play)
            }
        }
        var resutlStr = sb.toString().trim()
        mrecord = Record(resutlStr, Setings.q)
        mrecord.backup1 = sb_play.toString()
        return mrecord
    }

    private fun getParseBingyingWebHtml(html: String): Record? {
        LogUtil.DefalutLog("---getParseBingyingWebHtml---")
        var mrecord: Record? = null
        val sb = java.lang.StringBuilder()
        val sb_play = java.lang.StringBuilder()
        val doc = Jsoup.parse(html)
        val smt_hw = doc.select("div.smt_hw").first()
        if (smt_hw != null) {
            val p1_11 = doc.select("div.p1-11").first()
            if (p1_11 != null) {
                TranslateUtil.addContentAll(p1_11, sb, sb_play)
            }
        }
        val symblo = doc.select("div.hd_p1_1").first()
        if (symblo != null) {
            TranslateUtil.addContent(symblo, sb)
        }
        val translates = doc.select("div.qdef > ul > li")
        if (translates != null && translates.size > 0) {
            for (li in translates) {
                var content = li.text().trim { it <= ' ' }
                if (content.contains("网络")) {
                    content = content.replace("网络", "网络：")
                }
                sb.append(content)
                sb.append("\n")
                sb_play.append(content)
                sb_play.append(",")
            }
        }
        val fusu = doc.select("div.qdef > div.hd_div1 > div.hd_if").first()
        if (fusu != null) {
            TranslateUtil.addContentAll(fusu, sb, sb_play)
        }
        var resutlStr = sb.toString().trim()
        mrecord = Record(resutlStr, Setings.q)
        mrecord.backup1 = sb_play.toString()
        return mrecord
    }

    fun getParseHjiangWebHtml(html: String): Record? {
        LogUtil.DefalutLog(html)
        val sb = StringBuilder()
        val sb_play = StringBuilder()
        var mrecord: Record? = null
        val doc = Jsoup.parse(html)
        val error = doc.select("div.word-notfound").first()
        if (error != null) {
            LogUtil.DefalutLog(error.text())
            return null
        }
        val symblo = doc.select("div.word-info > div.pronounces").first()
        if (symblo != null) {
            TranslateUtil.addContent(symblo, sb)
        }
        val translate = doc.select("div.simple").first()
        if (translate != null) {
            for (li in translate.children()) {
                TranslateUtil.addContentAll(li, sb, sb_play)
            }
        }
        var resutlStr = sb.toString().trim()
        mrecord = Record(resutlStr, Setings.q)
        mrecord.backup1 = sb_play.toString()
        return mrecord
    }

    fun getSortData(map: Map<String, String>): String {
        var result = ""
        if (map != null) {
            for ((key, value) in map) {
                result += "$key=$value&"
            }
            result += "app_key=" + Setings.QQAPPKEY
            result = MD5.encode(result).toUpperCase()
        }
        return result
    }

}