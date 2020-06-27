package com.messi.languagehelper.repositories

import androidx.lifecycle.MutableLiveData
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.CollectedData
import com.messi.languagehelper.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CollectedListRepository {

    var isLoading = MutableLiveData<Boolean>()
    var mRespoData = MutableLiveData<RespoData<*>>()

    private val pageSize = 50
    var list: MutableList<CollectedData>
    var skip = 0
    var hasMore = true
    var loading = false

    init {
        isLoading.value = false
        list = ArrayList()
    }

    fun getDataList(isRefresh: Boolean) {
        if (isRefresh){
            skip = 0
            list.clear()
            hasMore = true
        }
        LogUtil.DefalutLog("hasMore:$hasMore")
        if(!loading && hasMore){
            GlobalScope.launch {
                loading = true
                var mResult = loadData()
                mRespoData.postValue(mResult)
                loading = false
            }
        }
    }

    suspend fun loadData(): RespoData<String> =
            withContext(Dispatchers.IO){
        LogUtil.DefalutLog("loadData")
        var result = RespoData<String>()
        result.code = 0
        var datas = BoxHelper.getCollectedList(skip,pageSize)
        if (datas.size > 0){
            result.code = 1
            result.positionStart = list.size
            result.itemCount = datas.size
            list.addAll(datas)
            skip += pageSize
        }
        hasMore = datas.size == pageSize
        result.isHideFooter = !hasMore
        result
    }
}