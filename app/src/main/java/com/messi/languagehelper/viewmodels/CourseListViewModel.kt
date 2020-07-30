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

    fun loadData(type: String) {
        if (isRequestInProgress) return
        serverVersion = sp.getInt(KeyUtil.Caricature_version,1)
//        localVersion = sp.getInt(KeyUtil.CourseVersion,0)

        isRequestInProgress = true
        viewModelScope.launch(Dispatchers.IO) {
            if (localVersion == serverVersion){
                getDataFromDataBase(type)
            }else {
                getData(type)
            }
            isRequestInProgress = false
        }
    }

    fun getData(type: String) {
        val queryResult = RespoData<String>()
        queryResult.code = 1
        var query = AVQuery<AVObject>(AVOUtil.CourseList.CourseList)
        query.whereNotEqualTo(AVOUtil.CourseList.valid,"0")
        if(!TextUtils.isEmpty(type)){
            query.whereEqualTo(AVOUtil.CourseList.type,type)
        }
        query.orderByAscending(AVOUtil.CourseList.order)
        query.limit = 200
        var results = query.find()
        if (NullUtil.isNotEmpty(results)){
            queryResult.code = 0
            datas.clear()
            for (item in results){
                datas.add(fromAVObjectToCourseList(item))
            }
            datas.sortByDescending { it.views }
//            if (serverVersion > 1){
//                Setings.saveSharedPreferences(sp,KeyUtil.CourseVersion,serverVersion)
//            }
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
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.lock))){
            mCourseList.lock = mAVObject.getString(AVOUtil.CourseList.lock)
        }
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.img))){
            mCourseList.img = mAVObject.getString(AVOUtil.CourseList.img)
        }
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.to_activity))){
            mCourseList.to_activity = mAVObject.getString(AVOUtil.CourseList.to_activity)
        }
        if (!TextUtils.isEmpty(mAVObject.getString(AVOUtil.CourseList.type))){
            mCourseList.type = mAVObject.getString(AVOUtil.CourseList.type)
        }
        BoxHelper.saveAndUpdate(mCourseList)
        return mCourseList
    }

    private fun getDataFromDataBase(type: String){
        var list = BoxHelper.getCourseList(type)
        if (list.size > 0){
            datas.clear()
            datas.addAll(list)
            val queryResult = RespoData<String>()
            queryResult.code = 0
            result.postValue(queryResult)
        }else{
            getData(type)
        }
    }

}