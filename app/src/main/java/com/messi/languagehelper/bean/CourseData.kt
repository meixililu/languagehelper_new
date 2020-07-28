package com.messi.languagehelper.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class CourseData (
        var type: String = "",
        var tips: String = "",
        var title: String = "",
        var question: String = "",
        var img: String = "",
        var content: String = "",
        var answer: String = "",
        var transalte: String = "",
        var media_url: String = "",
        var start_time: String = "",
        var end_time: String = "",
        var medias: List<CourseMedias>? = null,
        var options: List<String>? = null,
        var words: List<CourseWord>? = null,
        var course_id: String = "",
        var order: Int = 0,
        var unit: Int = 0,
        var level: Int = 0,
        var user_result: Boolean = false
) : Parcelable