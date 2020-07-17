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
    var courseList: ArrayList<ListenCourseData> = ArrayList<ListenCourseData>()
    lateinit var currentCourse: ListenCourseData
    lateinit var course_id: String

    var position = 0
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
            position = 0
            getDataTask()
            showCourse()
            loading = false
        }
    }

    fun next(){
        position ++
        showCourse()
    }

    fun showCourse(){
        if (courseList.size > position){
            currentCourse = courseList[position]
            mRespoData.postValue(currentCourse.type)
        } else {
            var isFinish = true
            for (item in courseList){
                if (!item.user_result){
                    currentCourse = item
                    mRespoData.postValue(currentCourse.type)
                    isFinish = false
                    break
                }
            }
            if(isFinish){
                mRespoData.postValue("finish")
            }
        }
    }

    suspend fun getDataTask() = withContext(Dispatchers.IO){
        var item9 = ListenCourseData()
        item9.type = "choice"
        item9.answer = "school"
        item9.c_title = "选择正确的翻译"
        item9.c_question = "学校"
        item9.c_options.add("school")
        item9.c_options.add("name")
        item9.c_options.add("Results in easily testable code.")
        item9.c_options.add("Model — deals with application data e.g. Network interface (API), SQLite database access. Responsible for storing and fetching of data")
        item9.c_options.add("Clean separation of concerns (decoupled codebase)")
        item9.c_options.add("desk")
        item9.c_options.add("The benefits of using this pattern include")
        item9.c_options.add("desk")
        item9.img = "http://res.mzxbkj.com/img/thefunnycat3.png"
        courseList.add(item9)
        var item8 = ListenCourseData()
        item8.type = "word_enter"
        item8.answer = "school"
        item8.content = ""
        item8.transalte = "学校"
        item8.img = "http://res.mzxbkj.com/img/thefunnycat3.png"
        courseList.add(item8)
        var mListenCourse = ListenCourseData()
        mListenCourse.type = "translate"
        mListenCourse.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        mListenCourse.answer = "some bridges are made from stone"
        mListenCourse.content = "some bridges is bike make wood are made from stone"
        mListenCourse.transalte = "一些桥是由石头建造的"
        mListenCourse.img = "http://res.mzxbkj.com/img/thefunnycat3.png"
        courseList.add(mListenCourse)
        var item1 = ListenCourseData()
        item1.type = "translate"
        item1.answer = "some bridges are made from stone"
        item1.content = "some bridges is bike make wood are made from stone"
        item1.transalte = "一些桥是由石头建造的"
        item1.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item1)
        var item2 = ListenCourseData()
        item2.type = "translate"
        item2.answer = "一些桥是由石头建造的"
        item2.content = "一些 桥 是 由 石头 建造 的 一个 河 一条"
        item2.transalte = "some bridges are made from stone"
        item2.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item2)
        var item6 = ListenCourseData()
        item6.type = "translate_enter"
        item6.answer = "some bridges are made from stone"
        item6.content = "some bridges is bike make wood are made from stone"
        item6.transalte = "一些桥是由石头建造的"
        item6.img = "http://res.mzxbkj.com/img/thefunnycat3.png"
        item6.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item6)
        var item3 = ListenCourseData()
        item3.type = "listen"
        item3.answer = "some bridges are made from stone"
        item3.content = "some bridges is bike make wood are made from stone"
        item3.transalte = "一些桥是由石头建造的"
        item3.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item3)
        var item4 = ListenCourseData()
        item4.type = "listen"
        item4.answer = "some bridges are made from stone"
        item4.content = "some bridges is bike make wood are made from stone"
        item4.transalte = "一些桥是由石头建造的"
        item4.img = "http://res.mzxbkj.com/img/thefunnycat3.png"
        item4.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item4)
        var item5 = ListenCourseData()
        item5.type = "listen_enter"
        item5.answer = "some bridges are made from stone"
        item5.content = "some bridges is bike make wood are made from stone"
        item5.transalte = "一些桥是由石头建造的"
        item5.img = "http://res.mzxbkj.com/img/thefunnycat3.png"
        item5.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item5)
        var item7 = ListenCourseData()
        item7.type = "listen_enter"
        item7.answer = "some bridges are made from stone"
        item7.content = "some bridges is bike make wood are made from stone"
        item7.transalte = "一些桥是由石头建造的"
        item7.tips = "举例：\n man - 男人复数：men\nwoman-女人复数：women"
        courseList.add(item7)
    }
}