package com.messi.languagehelper.viewmodels

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.*
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.messi.languagehelper.bean.ListenCourseData
import com.messi.languagehelper.bean.PVideoResult
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.CourseList
import com.messi.languagehelper.httpservice.RetrofitBuilder
import com.messi.languagehelper.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCourseViewModel(application: Application) : AndroidViewModel(application){

    var mRespoData = MutableLiveData<String>()
    var mRespoVideo = MutableLiveData<RespoData<PVideoResult>>()
    var courseList: ArrayList<ListenCourseData> = ArrayList()
    lateinit var currentCourse: ListenCourseData
    val sp = Setings.getSharedPreferences(getApplication())
    var course_id = ""

    var skip = 0
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

    fun getData() {
        if(!loading){
            loading = true
            position = 0
            getDataTask()
            showCourse()
            loading = false
        }
    }

    fun next(){
        skip ++
        position ++
        showCourse()
    }

    private fun showCourse(){
        if(courseList.size > 0){
            if (courseList.size > position){
                currentCourse = courseList[position]
                Setings.saveSharedPreferences(sp,course_id,skip)
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
    }

    fun getDataTask() {
        skip = sp.getInt(course_id,0)
        skip = 0
        var query = AVQuery<AVObject>(AVOUtil.CourseDetail.CourseDetail)
        query.whereEqualTo(AVOUtil.CourseDetail.course_id,course_id)
        query.orderByAscending(AVOUtil.CourseDetail.order)
        query.limit = 20
        query.skip = skip
        var results = query.find()
        if (NullUtil.isNotEmpty(results)){
            for (item in results){
                fromAVObjectToListenCourseData(item)
            }
        }else{
            mRespoData.postValue("finish")
        }
    }

    fun loadVideo() {
        viewModelScope.launch(Dispatchers.IO)  {
            loadVideoTask()
        }
    }

    private suspend fun loadVideoTask() = withContext(Dispatchers.IO){
        var result = RespoData<PVideoResult>()
        result.code = 1
        val timestamp = System.currentTimeMillis().toString()
        val platform = SystemUtil.platform
        val network = SystemUtil.network
        val sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, currentCourse.video_url, platform, network)
        val call = RetrofitBuilder.vService.getPVideoApi(currentCourse.video_url, network, platform, sign, timestamp, 0, "")
        if (call.isSuccessful){
            var mPVideoResult = call.body()
            if (mPVideoResult != null && !TextUtils.isEmpty(mPVideoResult.getUrl())){
                result.data = mPVideoResult
                result.code = 0
            }
        }
        mRespoVideo.postValue(result)
    }

    private fun fromAVObjectToListenCourseData(mAVObject: AVObject){
        val mItem = ListenCourseData()
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.title))){
            mItem.title = mAVObject.getString(AVOUtil.CourseDetail.title)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.tips))){
            mItem.tips = mAVObject.getString(AVOUtil.CourseDetail.tips)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.transalte))){
            mItem.transalte = mAVObject.getString(AVOUtil.CourseDetail.transalte)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.type))){
            mItem.type = mAVObject.getString(AVOUtil.CourseDetail.type)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.answer))){
            mItem.answer = mAVObject.getString(AVOUtil.CourseDetail.answer)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.content))){
            mItem.content = mAVObject.getString(AVOUtil.CourseDetail.content)
        }
        mItem.order = mAVObject.getInt(AVOUtil.CourseDetail.order)
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.course_id))){
            mItem.course_id = mAVObject.getString(AVOUtil.CourseDetail.course_id)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.end_time))){
            mItem.end_time = mAVObject.getString(AVOUtil.CourseDetail.end_time)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.start_time))){
            mItem.start_time = mAVObject.getString(AVOUtil.CourseDetail.start_time)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.img))){
            mItem.img = mAVObject.getString(AVOUtil.CourseDetail.img)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.mp3_url))){
            mItem.mp3_url = mAVObject.getString(AVOUtil.CourseDetail.mp3_url)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.play_content))){
            mItem.play_content = mAVObject.getString(AVOUtil.CourseDetail.play_content)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.question))){
            mItem.question = mAVObject.getString(AVOUtil.CourseDetail.question)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.video_type))){
            mItem.video_type = mAVObject.getString(AVOUtil.CourseDetail.video_type)
        }
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.video_url))){
            mItem.video_url = mAVObject.getString(AVOUtil.CourseDetail.video_url)
        }
        if(mAVObject.getList(AVOUtil.CourseDetail.options) != null){
            mItem.options = mAVObject.getList(AVOUtil.CourseDetail.options) as ArrayList<String>
        }
        courseList.add(mItem)
    }

}