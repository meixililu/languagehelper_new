package com.messi.languagehelper.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class CourseMedias (
        var media_url: String = "",
        var start_time: String = "",
        var end_time: String = "",
        var img: String = "",
        var content: String = "",
        var transalte: String = "",
        var video_type: String = ""
) : Parcelable