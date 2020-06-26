package com.messi.languagehelper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.repositories.CollectedListRepository

class CollectedListViewModel : ViewModel() {

    lateinit var mRepo: CollectedListRepository

    fun init() {
        mRepo = CollectedListRepository()
    }

    fun loadData(isRefresh: Boolean) {
        mRepo.getDataList(isRefresh)
    }

    val isShowProgressBar: LiveData<Boolean>?
        get() = mRepo.isLoading

    val dataList: LiveData<RespoData<*>>?
        get() = mRepo.mRespoData
}