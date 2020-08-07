package com.messi.languagehelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.Dictionary
import com.messi.languagehelper.box.Record
import com.messi.languagehelper.repositories.TranDictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TranDictViewModel(application: Application) : AndroidViewModel(application) {

    private val mTranRespoData: MutableLiveData<RespoData<Record>>
    private val mDictRespoData: MutableLiveData<RespoData<Dictionary>>
    private val isRefreshTran: MutableLiveData<Boolean>
    private val isRefreshDict: MutableLiveData<Boolean>
    var repository: TranDictRepository = TranDictRepository(getApplication())

    init {
        mTranRespoData = repository.mRespoData
        isRefreshTran = repository.isRefreshTran
        isRefreshDict = repository.isRefreshDict
        mDictRespoData = repository.mDictRespoData
    }

    fun initSample() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initSample()
        }
    }

    fun loadTranData(isRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadTranData(isRefresh)
        }
    }

    fun tranDict() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.tranDict()
        }
    }

    fun loadDictData(isRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadDictData(isRefresh)
        }
    }

    fun getDict() {
        viewModelScope.launch() {
            repository.getDict()
        }
    }

    fun isRefreshTran(): LiveData<Boolean> {
        return isRefreshTran
    }

    val tranRespoData: LiveData<RespoData<Record>>
        get() = mTranRespoData

    fun isRefreshDict(): LiveData<Boolean> {
        return isRefreshDict
    }

    val dictRespoData: LiveData<RespoData<Dictionary>>
        get() = mDictRespoData
}