package com.messi.languagehelper.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.*
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.CourseList
import com.messi.languagehelper.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseListViewModel(application: Application) : AndroidViewModel(application){

    private var isRequestInProgress = false
    val result = MutableLiveData<RespoData<String>>()
    val datas = ArrayList<CourseList>()
    private var localVersion = 0
    private var serverVersion = 1
    var sp: SharedPreferences = Setings.getSharedPreferences(application.applicationContext)

    fun loadData() {
        if (isRequestInProgress) return
        serverVersion = sp.getInt(KeyUtil.Caricature_version,1)
//        serverVersion = 3
        localVersion = sp.getInt(KeyUtil.CourseVersion,0)

        isRequestInProgress = true
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            if (localVersion == serverVersion){
                LogUtil.DefalutLog("localVersion == serverVersion")
                getDataFromDataBase()
            }else {
                LogUtil.DefalutLog("localVersion != serverVersion")
                getData()
            }
            isRequestInProgress = false
        }
    }

    fun getData() {
        val queryResult = RespoData<String>()
        queryResult.code = 1
        var query = AVQuery<AVObject>(AVOUtil.CourseList.CourseList)
        query.whereNotEqualTo(AVOUtil.CourseList.valid,"0")
        query.orderByAscending(AVOUtil.CourseList.order)
        query.limit = 200
        var results = query.find()
        if (NullUtil.isNotEmpty(results)){
            queryResult.code = 0
            datas.clear()
            for (item in results){
                datas.add(fromAVObjectToCourseList(item))
            }
            if (serverVersion > 1){
                Setings.saveSharedPreferences(sp,KeyUtil.CourseVersion,serverVersion)
            }
        }
        result.postValue(queryResult)
    }

    private fun fromAVObjectToCourseList(mAVObject: AVObject): CourseList {
        val mCourseList = CourseList()
        mCourseList.objectId = mAVObject.objectId
        mCourseList.name = mAVObject.getString(AVOUtil.CourseList.name)
        mCourseList.course_id = mAVObject.getString(AVOUtil.CourseList.course_id)
        mCourseList.course_num = mAVObject.getInt(AVOUtil.CourseList.course_num)
        mCourseList.order = mAVObject.getInt(AVOUtil.CourseList.order)
        mCourseList.level_num = mAVObject.getInt(AVOUtil.CourseList.level_num)
        mCourseList.unit_num = mAVObject.getInt(AVOUtil.CourseList.unit_num)
        mCourseList.order = mAVObject.getInt(AVOUtil.CourseList.order)
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.lock))){
            mCourseList.lock = mAVObject.getString(AVOUtil.CourseList.lock)
        }
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.img))){
            mCourseList.img = mAVObject.getString(AVOUtil.CourseList.img)
        }
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.to_activity))){
            mCourseList.to_activity = mAVObject.getString(AVOUtil.CourseList.to_activity)
        }
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.level))){
            mCourseList.level = mAVObject.getString(AVOUtil.CourseList.level)
        }
        BoxHelper.saveAndUpdate(mCourseList)
        return mCourseList
    }

    private fun getDataFromDataBase(){
        var list = BoxHelper.getCourseList()
        if (list.size > 0){
            datas.clear()
            datas.addAll(list)
            val queryResult = RespoData<String>()
            queryResult.code = 0
            result.postValue(queryResult)
        }else{
            getData()
        }
    }

}