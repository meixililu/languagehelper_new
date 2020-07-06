package com.messi.languagehelper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messi.languagehelper.bean.RespoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCourseViewModel : ViewModel(){

    var mRespoData = MutableLiveData<RespoData<String>>()
    var page: Int = 1
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
        page += 1
        result.isHideFooter = !hasMore
        result
    }
}