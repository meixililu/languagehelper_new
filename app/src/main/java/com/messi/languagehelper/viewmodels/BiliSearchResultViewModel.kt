package com.messi.languagehelper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.httpservice.RetrofitBuilder
import com.messi.languagehelper.util.Setings
import com.messi.languagehelper.util.SignUtil
import com.messi.languagehelper.util.SystemUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BiliSearchResultViewModel : ViewModel(){

    var mRespoData = MutableLiveData<RespoData<String>>()
    var page: Int = 1
    var list: MutableList<BoutiquesBean?> = ArrayList()
    var hasMore = true
    var loading = false
    var keyword = ""

    val dataList: LiveData<RespoData<String>>?
        get() = mRespoData

    fun loadData(isRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO)  {
            getData(isRefresh)
        }
    }

    suspend fun getData(isRefresh: Boolean) {
        if (isRefresh){
            page = 1
            list.clear()
            hasMore = true
        }
        if(!loading && hasMore){
            loading = true
            var mResult = loadData()
            mRespoData.postValue(mResult)
            loading = false
        }
    }

    suspend fun loadData(): RespoData<String> = withContext(Dispatchers.IO){
        var result = RespoData<String>()
        result.code = 0
        val timestamp = System.currentTimeMillis().toString()
        val platform = SystemUtil.platform
        val network = SystemUtil.network
        val sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, keyword, page.toString(), platform, network)
        var respoData = RetrofitBuilder.vService.searchVideoApi(keyword, network, platform, sign, timestamp, page)
        var datas = respoData.body()
        if (datas != null) {
            if (datas.size > 0){
                result.code = 1
                result.positionStart = list.size
                result.itemCount = datas.size
                list.addAll(datas)
                page += 1
            }
            hasMore = datas.size == 20
        }
        result.isHideFooter = !hasMore
        result
    }
}