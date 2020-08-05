package com.messi.languagehelper.util

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.Dictionary
import com.messi.languagehelper.box.Record
import com.messi.languagehelper.util.*
import com.youdao.sdk.ydtranslate.Translate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

object TranDictHelper {

    suspend fun tranDict(context: Context): RespoData<Record> = withContext(Dispatchers.IO){
        return@withContext if (NetworkUtil.isNetworkConnected(context)) {
            LogUtil.DefalutLog("tranDict-online")
            doTranslateTask(context)
        } else {
            LogUtil.DefalutLog("tranDict-offline")
            translateOffline()
        }
    }

    private suspend fun doTranslateTask(context: Context): RespoData<Record> {
        var result: Record? = null
        result = KTranslateHelper.doTranslateTask()
        val mData: RespoData<Record> = RespoData(1, "")
        if (result != null) {
            mData.data = result
        } else {
            mData.code = 0
            mData.setErrStr(context.resources.getString(R.string.network_error))
        }
        return mData
    }

    private fun translateOffline(): RespoData<Record> {
        val mTranslate = TranslateUtil.offlineTranslate()
        return parseOfflineData(mTranslate)
    }

    private fun parseOfflineData(translate: Translate?): RespoData<Record> {
        LogUtil.DefalutLog("parseOfflineData:$translate")
        val mData: RespoData<Record> = RespoData(1, "")
        if (translate != null) {
            if (translate.errorCode == 0) {
                val sb = StringBuilder()
                TranslateUtil.addSymbol(translate, sb)
                for (tran in translate.translations) {
                    sb.append(tran)
                    sb.append("\n")
                }
                mData.data = Record(sb.substring(0, sb.lastIndexOf("\n")), Setings.q)
            }
        } else {
            mData.code = 0
            mData.setErrStr("没找到离线词典，请到更多页面下载！")
        }
        return mData
    }

    //-----------------dict-----------------

    fun getDict(context: Context) {
        if (NetworkUtil.isNetworkConnected(context)) {
            LogUtil.DefalutLog("dict-online")
            doDictTask(context)
        } else {
            LogUtil.DefalutLog("dict-offline")
            translateOfflineForDict()
        }
    }

    private fun doDictTask(context: Context) {
        TranslateUtil.Translate_init { mDict: Dictionary ->
            val mData: RespoData<Dictionary> = RespoData(1, "")
            if (mDict != null) {
                mData.data = mDict
                BoxHelper.insert(mDict)
            } else {
                mData.code = 0
                mData.errStr = context.resources.getString(R.string.network_error)
            }
//            mDictRespoData.postValue(mData)
        }
    }

    private fun translateOfflineForDict() {
        CoroutineScope(Dispatchers.IO).launch {
            val mTranslate = TranslateUtil.offlineTranslate()
            parseOfflineDictData(mTranslate)
        }
    }

    private fun parseOfflineDictData(translate: Translate) {
        val mData: RespoData<Dictionary> = RespoData(1, "")
        if (translate != null) {
            if (translate.errorCode == 0) {
                val sb = StringBuilder()
                TranslateUtil.addSymbol(translate, sb)
                for (tran in translate.translations) {
                    sb.append(tran)
                    sb.append("\n")
                }
                val mDictionaryBean = Dictionary()
                val isEnglish = StringUtils.isEnglish(Setings.q)
                if (isEnglish) {
                    mDictionaryBean.from = "en"
                    mDictionaryBean.setTo("zh")
                } else {
                    mDictionaryBean.from = "zh"
                    mDictionaryBean.setTo("en")
                }
                mDictionaryBean.word_name = Setings.q
                mDictionaryBean.result = sb.substring(0, sb.lastIndexOf("\n"))
                BoxHelper.insert(mDictionaryBean)
            }
        } else {
            mData.code = 0
            mData.setErrStr("没找到离线词典，请到更多页面下载！")
        }
//        mDictRespoData.postValue(mData)
    }

}