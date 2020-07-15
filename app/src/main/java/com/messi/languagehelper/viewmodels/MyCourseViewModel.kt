package com.messi.languagehelper.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.messi.languagehelper.BaseApplication
import com.messi.languagehelper.bean.ListenCourseData
import com.messi.languagehelper.bean.RespoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCourseViewModel(application: Application) : AndroidViewModel(application){

    var mRespoData = MutableLiveData<String>()
    lateinit var mListenCourse: ListenCourseData
    lateinit var course_id: String

    var page: Int = 1
    var loading = false
    var keyword = ""

    val result: LiveData<String>
        get() = mRespoData

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO)  {
            getData()

        }
    }

    suspend fun getData() {
        if(!loading){
            loading = true
            getDataTask()
            mRespoData.postValue("finish")
            loading = false
        }
    }

    suspend fun getDataTask() = withContext(Dispatchers.IO){
        mListenCourse = ListenCourseData()
        mListenCourse.answer = "some bridges are made from stone"
        mListenCourse.content = "some bridges is bike make wood are made from stone"
        mListenCourse.transalte = "一些桥是由石头建造的"
    }

    fun next(){
        mRespoData.postValue("next")
    }
}