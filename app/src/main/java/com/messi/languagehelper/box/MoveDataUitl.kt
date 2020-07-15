package com.messi.languagehelper.box

import android.content.Context
import android.content.SharedPreferences
import com.messi.languagehelper.util.AVOUtil
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.util.Setings
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MoveDataUitl {

    companion object {
        fun moveCollectedData(context: Context){
            var sp: SharedPreferences = Setings.getSharedPreferences(context)
            if (!sp.getBoolean(KeyUtil.HasMoveCollectedData, false)){
                GlobalScope.launch(Dispatchers.IO) {
                    async {
                        moveReadingData()
                    }
                    async {
                        moveReadingSubjectData()
                    }
                }
                Setings.saveSharedPreferences(sp, KeyUtil.HasMoveCollectedData,true)
            }
        }

        private fun moveReadingData(){
            var lists = BoxHelper.getReadingCollectedList(0,0)
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Reading> = moshi.adapter(Reading::class.java)
            for(item in lists){
                val cdata = CollectedData()
                cdata.objectId = item.object_id
                cdata.name = item.title
                cdata.type = AVOUtil.Reading.Reading
                cdata.json = jsonAdapter.toJson(item)
                BoxHelper.insert(cdata)
            }
        }

        private fun moveReadingSubjectData(){
            var lists = BoxHelper.getReadingSubjectList(0,0)
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<ReadingSubject> = moshi.adapter(ReadingSubject::class.java)
            for(item in lists){
                val cdata = CollectedData()
                cdata.objectId = item.objectId
                cdata.name = item.name
                cdata.type = AVOUtil.SubjectList.SubjectList
                cdata.json = jsonAdapter.toJson(item)
                BoxHelper.insert(cdata)
            }
        }

    }



}