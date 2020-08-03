package com.messi.languagehelper.repositories

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

class TranDictRepository(var context: Context) {

    var mRespoData = MutableLiveData<RespoData<Record>>()
    var mDictRespoData = MutableLiveData<RespoData<Dictionary>>()
    var isRefreshTran = MutableLiveData<Boolean>()
    var isRefreshDict = MutableLiveData<Boolean>()
    private val sp: SharedPreferences = Setings.getSharedPreferences(context)
    val trans: ArrayList<Record> = ArrayList()
    val dicts: ArrayList<Dictionary> = ArrayList()
    var tSkip = 0
    var tNoMoreData = false
    var dSkip = 0
    var dNoMoreData = false

    fun initSample() {
        val isHasShowMessage = sp.getBoolean(KeyUtil.IsHasShowBaiduMessage, false)
        if (!isHasShowMessage) {
            val sampleBean = Record("Click on the microphone to speak", "点击话筒说话")
            BoxHelper.insert(sampleBean)
            trans.add(0, sampleBean)
            Setings.saveSharedPreferences(sp, KeyUtil.IsHasShowBaiduMessage, true)
        }
    }

    fun loadTranData(isRefresh: Boolean) {
        if (isRefresh) {
            trans.clear()
            tSkip = 0
            tNoMoreData = false
        }
        if (!tNoMoreData) {
            val list = BoxHelper.getRecordList(tSkip, Setings.RecordOffset)
            if (NullUtil.isNotEmpty(list)) {
                trans.addAll(list)
                tSkip += list.size
                tNoMoreData = false
            } else {
                tNoMoreData = true
            }
            isRefreshTran.postValue(true)
        }
    }

    suspend fun tranDict() = withContext(Dispatchers.IO){
        if (NetworkUtil.isNetworkConnected(context)) {
            LogUtil.DefalutLog("tranDict-online")
            doTranslateTask()
        } else {
            LogUtil.DefalutLog("tranDict-offline")
            translateOffline()
        }
    }

    private suspend fun doTranslateTask() {
        var result: Record? = null
        result = KTranslateHelper.doTranslateTask()
        val mData: RespoData<Record> = RespoData(1, "")
        if (result != null) {
            mData.data = result
            trans.add(0, result)
        } else {
            mData.code = 0
            mData.setErrStr(context.resources.getString(R.string.network_error))
        }
        mRespoData.postValue(mData)
    }

    private fun translateOffline() {
        CoroutineScope(Dispatchers.IO).launch {
            val mTranslate = TranslateUtil.offlineTranslate()
            parseOfflineData(mTranslate)
        }
    }

    private fun parseOfflineData(translate: Translate?) {
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
                val mrecord = Record(sb.substring(0, sb.lastIndexOf("\n")), Setings.q)
                trans.add(0, mrecord)
            }
        } else {
            mData.code = 0
            mData.setErrStr("没找到离线词典，请到更多页面下载！")
        }
        mRespoData.postValue(mData)
    }

    //-----------------dict-----------------
    fun loadDictData(isRefresh: Boolean) {
        if (isRefresh) {
            dicts.clear()
            dSkip = 0
            dNoMoreData = false
        }
        if (!dNoMoreData) {
            val list = BoxHelper.getDictionaryList(dSkip, Setings.RecordOffset)
            if (NullUtil.isNotEmpty(list)) {
                dicts.addAll(list)
                dSkip += list.size
                dNoMoreData = false
            } else {
                dNoMoreData = true
            }
            isRefreshDict.postValue(true)
        }
    }

    fun getDict() {
        if (NetworkUtil.isNetworkConnected(context)) {
            LogUtil.DefalutLog("dict-online")
            doDictTask()
        } else {
            LogUtil.DefalutLog("dict-offline")
            translateOfflineForDict()
        }
    }

    private fun doDictTask() {
        TranslateUtil.Translate_init { mDict: Dictionary ->
            val mData: RespoData<Dictionary> = RespoData(1, "")
            if (mDict != null) {
                mData.data = mDict
                dicts.add(0, mDict)
                BoxHelper.insert(mDict)
            } else {
                mData.code = 0
                mData.errStr = context.resources.getString(R.string.network_error)
            }
            mDictRespoData.postValue(mData)
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
                dicts.add(0, mDictionaryBean)
                BoxHelper.insert(mDictionaryBean)
            }
        } else {
            mData.code = 0
            mData.setErrStr("没找到离线词典，请到更多页面下载！")
        }
        mDictRespoData.postValue(mData)
    }

}