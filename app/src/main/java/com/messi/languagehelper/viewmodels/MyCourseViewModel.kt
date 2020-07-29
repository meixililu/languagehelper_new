package com.messi.languagehelper.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.google.gson.Gson
import com.messi.languagehelper.bean.*
import com.messi.languagehelper.httpservice.RetrofitBuilder
import com.messi.languagehelper.util.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type


class MyCourseViewModel(application: Application) : AndroidViewModel(application){

    var mRespoData = MutableLiveData<String>()
    var mRespoVideo = MutableLiveData<RespoData<PVideoResult>>()
    var courseList: ArrayList<CourseData> = ArrayList()
    lateinit var currentCourse: CourseData
    val sp: SharedPreferences = Setings.getSharedPreferences(getApplication())
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
        query.orderByDescending(AVOUtil.CourseDetail.order)
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

    fun loadVideo(mCourseMimic: CourseMedias) {
        viewModelScope.launch(Dispatchers.IO)  {
            loadVideoTask(mCourseMimic)
        }
    }

    private suspend fun loadVideoTask(mCourseMimic: CourseMedias) = withContext(Dispatchers.IO){
        var result = RespoData<PVideoResult>()
        result.code = 1
        val timestamp = System.currentTimeMillis().toString()
        val platform = SystemUtil.platform
        val network = SystemUtil.network
        val sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, mCourseMimic.media_url, platform, network)
        val call = RetrofitBuilder.vService.getPVideoApi(mCourseMimic.media_url, network, platform, sign, timestamp, 0, "")
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
        var mItem = CourseData()
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.json))){
            var json = mAVObject.getString(AVOUtil.CourseDetail.json)
            json = json.replace("，",",")
            json = json.replace("：",":")
            json = json.replace("\"","\"")
            mItem = Gson().fromJson(json,CourseData::class.java)
        }
        mItem.order = mAVObject.getInt(AVOUtil.CourseDetail.order)
        mItem.unit = mAVObject.getInt(AVOUtil.CourseDetail.unit)
        mItem.level = mAVObject.getInt(AVOUtil.CourseDetail.level)
        if(!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseDetail.course_id))){
            mItem.course_id = mAVObject.getString(AVOUtil.CourseDetail.course_id)
        }
        courseList.add(mItem)
    }

}