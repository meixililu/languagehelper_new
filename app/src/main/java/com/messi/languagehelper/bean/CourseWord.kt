package com.messi.languagehelper.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class CourseWord (
        var name: String = "",
        var des: String = "",
        var img: String = ""
) : Parcelable