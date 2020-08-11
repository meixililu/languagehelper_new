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
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.bean.CourseMedias
import com.messi.languagehelper.bean.PVideoResult
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.CourseList
import com.messi.languagehelper.box.UserProfile
import com.messi.languagehelper.httpservice.RetrofitBuilder
import com.messi.languagehelper.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyCourseViewModel(application: Application) : AndroidViewModel(application){

    var userProfile: UserProfile? = null
    var mRespoData = MutableLiveData<String>()
    var mRespoVideo = MutableLiveData<RespoData<PVideoResult>>()
    var courseList: ArrayList<CourseData> = ArrayList()
    var progress = MutableLiveData<Int> ()
    lateinit var currentCourse: CourseData
    lateinit var userCourseRecord: CourseList
    val sp: SharedPreferences = Setings.getSharedPreferences(getApplication())
    var course_id = ""
    var position = 0
    var page: Int = 1
    var loading = false
    var keyword = ""

    init {
        viewModelScope.launch(Dispatchers.IO)  {
            userProfile = BoxHelper.getUserProfile()
        }
    }

    val result: LiveData<String>
        get() = mRespoData

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO)  {
            userCourseRecord = BoxHelper.getCourseListById(course_id)
            isFinish()
            getData()
        }
    }

    private fun isFinish(){
        if (!::userCourseRecord.isInitialized) return
        if (userCourseRecord.finish && userCourseRecord.user_level_num < userCourseRecord.level_num){
            LogUtil.DefalutLog("isFinish---has add more level")
            userCourseRecord.finish = false
            userCourseRecord.user_level_num ++
            userCourseRecord.user_unit_num = 0
            BoxHelper.update(userCourseRecord)
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

    fun sendProgress(){
        if(::currentCourse.isInitialized && courseList.size > 0 &&
                currentCourse.user_result){
            progress.value = courseList.size+1
        }
    }

    fun next(){
        position ++
        showCourse()
    }

    private fun showCourse(){
        if(courseList.size > 0){
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
                    checkIsFinish()
                    mRespoData.postValue("finish")
                }
            }
        }
    }

    private fun checkIsFinish(){
        try {
            if (::userCourseRecord.isInitialized && userProfile != null){
                if (userCourseRecord.user_unit_num < userCourseRecord.unit_num-1){
                    userCourseRecord.user_unit_num++
                    userProfile!!.course_unit_sum++
                }else{
                    if (userCourseRecord.user_level_num >= userCourseRecord.level_num){
                        userCourseRecord.finish = true
                    }else{
                        userCourseRecord.user_unit_num = 0
                        userCourseRecord.user_level_num++
                        userProfile!!.show_level_up = true
                        userProfile!!.course_level_sum++
                    }
                }
                userProfile!!.course_score += courseList.size
                BoxHelper.update(userCourseRecord)
                BoxHelper.update(userProfile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDataTask() {
        var query = AVQuery<AVObject>(AVOUtil.CourseDetail.CourseDetail)
        query.whereEqualTo(AVOUtil.CourseDetail.course_id,course_id)
        if (!userCourseRecord.finish){
            query.whereEqualTo(AVOUtil.CourseDetail.level, userCourseRecord.user_level_num)
            query.whereEqualTo(AVOUtil.CourseDetail.unit, userCourseRecord.user_unit_num)
            query.skip = 0
        }else{
            var skip = 0
            if (userCourseRecord.course_num - 15 > 10){
                skip = NumberUtil.getRandomNumber(userCourseRecord.course_num - 15)
            }
            query.skip = skip
            query.limit = 15
        }
        query.orderByAscending(AVOUtil.CourseDetail.order)
//        query.orderByDescending(AVOUtil.CourseDetail.order)
        var results:List<AVObject>? = null
        try {
             results = query.find()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (NullUtil.isNotEmpty(results)){
            for (item in results!!){
                fromAVObjectToListenCourseData(item)
            }
        }else{
            if (userCourseRecord.user_level_num > userCourseRecord.level_num){
                mRespoData.postValue("finish")
            }else{
                if (userCourseRecord.user_unit_num < userCourseRecord.unit_num-1){
                    userCourseRecord.user_unit_num++
                }else{
                    if (userCourseRecord.user_level_num >= userCourseRecord.level_num){
                        userCourseRecord.finish = true
                    }else{
                        userCourseRecord.user_unit_num = 0
                        userCourseRecord.user_level_num++
                        userProfile!!.show_level_up = true
                        userProfile!!.course_level_sum++
                    }
                }
                getDataTask()
            }
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
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show_check_in(): Boolean{
        var result = false
        try {
            if (userProfile != null){
                val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
                val date = Date(System.currentTimeMillis())
                var today = simpleDateFormat.format(date)

                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, -1) //向前走一天
                var yesterday = simpleDateFormat.format(calendar.time)
                if (userProfile!!.last_check_in != today){
                    if (userProfile!!.last_check_in == yesterday){
                        userProfile!!.check_in_sum ++
                    }else{
                        userProfile!!.check_in_sum = 1
                    }
                    result = true
                    userProfile!!.show_check_in = true
                    userProfile!!.last_check_in = today
                    BoxHelper.update(userProfile)
                }
            }
        } catch (e: Exception) {
        }
        return result
    }

    fun toScore(){
        mRespoData.postValue("score")
    }

    fun toCheckIn(){
        mRespoData.postValue("check_in")
    }

    fun getType():String {
        var type = ""
        if (::userCourseRecord.isInitialized && !TextUtils.isEmpty(userCourseRecord.type)){
            type = userCourseRecord.type!!
        }
        return type
    }

}