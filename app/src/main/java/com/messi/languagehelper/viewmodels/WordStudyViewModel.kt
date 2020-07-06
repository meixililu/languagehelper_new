package com.messi.languagehelper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.util.LogUtil
import com.messi.languagehelper.util.NullUtil
import com.messi.languagehelper.util.NumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WordStudyViewModel : ViewModel(){

    var mResult = MutableLiveData<String>()
    var totalSum = 0
    var position = 0
    var index = 0
    lateinit var randomPlayIndex: List<Int>
    lateinit var itemList: ArrayList<WordDetailListItem>

    fun init(lists: ArrayList<WordDetailListItem>){
        itemList = lists
        totalSum = itemList.size
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0)
    }

    fun refreshData(action: String){
        LogUtil.DefalutLog("viewModel.index:$index")
        mResult.value = action
    }

    val currentItem: WordDetailListItem
        get(){
            return itemList[position]
        }

    val resultAction: LiveData<String>
        get() = mResult

    val hasLearnWordNum: Int
        get() {
            var count = 0
            if (NullUtil.isNotEmpty(itemList)) {
                for (item in itemList) {
                    if (item.isIs_know) {
                        count++
                    }
                }
            }
            return count
        }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO)  {
            getData()
        }
    }

    suspend fun getData() {

    }
}