package com.messi.languagehelper.util

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import cn.leancloud.json.JSON
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.*
import com.messi.languagehelper.box.Record
import com.messi.languagehelper.http.BgCallback
import com.messi.languagehelper.http.LanguagehelperHttpClient
import com.messi.languagehelper.httpservice.RetrofitBuilder
import com.messi.languagehelper.util.TranslateHelper.getParseBingyingWebHtml
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException
import java.util.ArrayList

object KTranslateHelper {

    //  jscb,youdaoweb,bingweb,hujiangweb,qqfyj,hujiangapi,youdaoapi,baidu#youdaoweb,bingweb,hujiangweb,jscb,qqfyj,youdaoapi,hujiangapi,baidu
    private var OrderTran = "jscb,youdaoweb,bingweb,hujiangweb,hujiangapi,qqfyj,youdaoapi,baidu"
    private val youdaoweb = "youdaoweb"
    private val youdaoapi = "youdaoapi"
    private val bingweb = "bingweb"
    private val jscb = "jscb"
    private val hujiangapi = "hujiangapi"
    private val hujiangweb = "hujiangweb"
    private val qqfyj = "qqfyj"
    private val baidu = "baidu"
    private var HJCookie = ""
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
                    LogUtil.DefalutLog("---initTranOrder---keys[0]:$"+keys[0])
                    LogUtil.DefalutLog("---initTranOrder---keys[1]:$"+keys[1])
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
    }

    suspend fun doTranslateTask():Record? {
        var mRecordResult: Record? = null
        for(method in tranOrder){
            LogUtil.DefalutLog("DoTranslateByMethod---$method")
            when (method){
//                youdaoweb -> {
//                    mRecordResult = Tran_Youdao_Web(e)
//                }
                jscb -> {
                    mRecordResult = tranFromICiBa()
                }
//                youdaoapi -> {
//                    mRecordResult = Tran_Youdao_Api(e)
//                }
//                bingweb -> {
//                    mRecordResult = Tran_Bing_Web(e)
//                }
//                hujiangapi -> {
//                    mRecordResult = Tran_Hj_Api(e)
//                }
//                hujiangweb -> {
//                    mRecordResult = Tran_Baidu(e)
//                }
//                qqfyj -> {
//                    mRecordResult = Tran_QQFYJApi(e)
//                }
//                baidu -> {
//                    mRecordResult = Tran_Hj_Web(e)
//                }
                else -> {
                    LogUtil.DefalutLog("translate error method:$method")
                }
            }
            if (mRecordResult != null) break
        }
        return mRecordResult
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


    private fun Tran_QQAILabApi(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_QQAILAb")
        LanguagehelperHttpClient.postTranQQAILabAPi(object : BgCallback() {
            override fun onFailured() {
            }

            override fun onResponsed(responseString: String) {
                LogUtil.DefalutLog("Tran_QQAILabApi:$responseString")
                var result: Record? = null
                try {
                    val root = JSON.parseObject(responseString, QQTranAILabRoot::class.java)
                    if (root != null && root.ret == 0 && root.data != null) {
                        result = Record(root.data.trans_text, Setings.q)
                    }
                } catch (ec: Exception) {
                    result = null
                    ec.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

    private fun Tran_QQFYJApi(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_QQFYJApi")
        LanguagehelperHttpClient.postTranQQFYJAPi(object : BgCallback() {
            override fun onFailured() {
            }

            override fun onResponsed(responseString: String) {
                var result: Record? = null
                try {
                    val root = JSON.parseObject(responseString, QQTranAILabRoot::class.java)
                    if (root != null && root.ret == 0 && root.data != null) {
                        result = Record(root.data.target_text, Setings.q)
                    }
                } catch (ec: Exception) {
                    result = null
                    ec.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

    private fun Tran_Youdao_Web(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_Youdao_Web")
        if (isNotWord) {
            return
        }
        LanguagehelperHttpClient.get(Setings.YoudaoWeb + Setings.q + Setings.YoudaoWebEnd, object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, ioe: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, mResponse: okhttp3.Response) {
                var result: Record? = null
                try {
                    if (mResponse.isSuccessful) {
                        val responseString = mResponse.body!!.string()
                        if (!TextUtils.isEmpty(responseString)) {
                            result = getParseYoudaoWebHtml(responseString)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

    private fun Tran_Bing_Web(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_Bing_Web")
        if (isNotWord) {
            return
        }
        LanguagehelperHttpClient.get(Setings.BingyingWeb + Setings.q, object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, ioe: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, mResponse: okhttp3.Response) {
                var result: Record? = null
                try {
                    if (mResponse.isSuccessful) {
                        val responseString = mResponse.body!!.string()
                        if (!TextUtils.isEmpty(responseString)) {
                            result = getParseBingyingWebHtml(responseString)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

    private fun Tran_Hj_Web(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_Hj_Web")
        if (isNotWord) {
            return
        }
        val request = Request.Builder()
                .url(Setings.HjiangWeb + Setings.q)
                .header("User-Agent", LanguagehelperHttpClient.Header)
                .header("Cookie", HJCookie)
                .build()
        LanguagehelperHttpClient.get(request, object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, ioe: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, mResponse: okhttp3.Response) {
                var result: Record? = null
                try {
                    if (mResponse.isSuccessful) {
                        val responseString = mResponse.body!!.string()
                        if (!TextUtils.isEmpty(responseString)) {
                            result = getParseHjiangWebHtml(responseString)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

    private fun Tran_Youdao_Api(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_Youdao_Api")
        val formBody = FormBody.Builder()
                .add("i", Setings.q)
                .build()
        LanguagehelperHttpClient.post(Setings.YoudaoApi, formBody, object : BgCallback() {
            override fun onFailured() {
            }

            override fun onResponsed(responseString: String) {
                var result: Record? = null
                try {
                    result = parseYoudaoApiResult(responseString)
                } catch (ec: Exception) {
                    ec.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

    private fun Tran_Hj_Api(e: ObservableEmitter<Record>) {
        LogUtil.DefalutLog("Result---Tran_Hj_Api")
        LanguagehelperHttpClient.postHjApi(object : BgCallback() {
            override fun onFailured() {
            }

            override fun onResponsed(responseString: String) {
                var result: Record? = null
                try {
                    result = tran_hj_api(responseString)
                } catch (ec: Exception) {
                    ec.printStackTrace()
                } finally {
                    if (result != null) {
                        e.onNext(result)
                    } else {
                    }
                }
            }
        })
    }

//    private fun Tran_Baidu(e: ObservableEmitter<Record>) {
//        LogUtil.DefalutLog("Result---zyhy server")
//        val timestamp = System.currentTimeMillis().toString()
//        val platform = SystemUtil.platform
//        val network = SystemUtil.network
//        val sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, Setings.q,
//                platform, network, Setings.from, Setings.to)
//        if (StringUtils.isEnglish(Setings.q)) {
//            Setings.from = "en"
//            Setings.to = "zh"
//        } else {
//            Setings.from = "zh"
//            Setings.to = "en"
//        }
//        val service = RetrofitApiService.getRetrofitApiService(Setings.TranApi,
//                RetrofitApiService::class.java)
//        val call = service.tranDict(Setings.q, Setings.from, Setings.to,
//                network, platform, sign, 0, timestamp)
//        call.enqueue(object : Callback<TranResultRoot<TranDictResult?>?> {
//            override fun onResponse(call: Call<TranResultRoot<TranDictResult?>?>, response: Response<TranResultRoot<TranDictResult?>?>) {
//                var result: Record? = null
//                if (response.isSuccessful) {
//                    val mResult = response.body()
//                    if (mResult != null) {
//                        val tdResult = mResult.result
//                        if (tdResult != null && !TextUtils.isEmpty(tdResult.result)) {
//                            var des = tdResult.result
//                            if (!TextUtils.isEmpty(tdResult.symbol)) {
//                                des = """
//                                    ${tdResult.symbol}
//                                    ${tdResult.result}
//                                    """.trimIndent()
//                            }
//                            result = Record(des, Setings.q)
//                            result.ph_am_mp3 = tdResult.mp3_am
//                            result.ph_en_mp3 = tdResult.mp3_en
//                            result.backup1 = tdResult.result
//                        }
//                    }
//                }
//                if (result != null) {
//                    e.onNext(result)
//                } else {
//                    onFaileTranslate(e)
//                }
//            }
//
//            override fun onFailure(call: Call<TranResultRoot<TranDictResult?>?>, t: Throwable) {
//                onFaileTranslate(e)
//            }
//        })
//    }

    private fun parseYoudaoApiResult(mResult: String): Record? {
        var currentDialogBean: Record? = null
        try {
            if (!TextUtils.isEmpty(mResult)) {
                if (JsonParser.isJson(mResult)) {
                    val bean = JSON.parseObject(mResult, YoudaoApiBean::class.java)
                    if (bean != null && bean.errorCode == 0 && bean.translateResult != null) {
                        val list = bean.translateResult
                        if (list.size > 0) {
                            val item = list[0]
                            if (item != null && item.size > 0) {
                                val result = item[0]
                                if (result != null && !TextUtils.isEmpty(result.tgt)) {
                                    currentDialogBean = Record(result.tgt, Setings.q)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.DefalutLog("parseYoudaoApiResult error")
            e.printStackTrace()
        }
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

    fun getParseYoudaoWebHtml(html: String?): Record? {
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
        return if (sb.length > 0) {
            var resutlStr: String? = ""
            resutlStr = if (sb.lastIndexOf("\n") > 0) {
                sb.substring(0, sb.lastIndexOf("\n"))
            } else {
                sb.toString()
            }
            mrecord = Record(resutlStr, Setings.q)
            mrecord.backup1 = sb_play.toString()
            mrecord
        } else {
            null
        }
    }

    fun getParseHjiangWebHtml(html: String?): Record? {
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
        return if (sb.length > 0) {
            var resutlStr: String? = ""
            resutlStr = if (sb.lastIndexOf("\n") > 0) {
                sb.substring(0, sb.lastIndexOf("\n"))
            } else {
                sb.toString()
            }
            mrecord = Record(resutlStr, Setings.q)
            mrecord.backup1 = sb_play.toString()
            mrecord
        } else {
            null
        }
    }

    private fun getWordsCount(content: String): Int {
        var count = 0
        val len = content.length
        for (i in 0 until len) {
            val tem = Setings.q[i]
            if (tem == ' ') count++
        }
        return count
    }

    private val isNotWord: Boolean
        private get() {
            if (getWordsCount(Setings.q.trim { it <= ' ' }) > 1) {
                return true
            } else if (StringUtils.isContainChinese(Setings.q.trim { it <= ' ' })) {
                return true
            }
            return false
        }


}