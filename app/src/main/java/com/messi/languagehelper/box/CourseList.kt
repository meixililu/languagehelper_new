package com.messi.languagehelper.box

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
@Entity
class CourseList(
        @Id var id: Long = 0,
        @Index var objectId: String = "",
        @Index var course_id: String = "",
        var name: String = "",
        var course_num: Int = 0,
        var current: Int = 0,
        var order: Int = 0,
        var to_activity: String? = "",
        var img: String = "",
        var level: String = "",
        var lock: String? = "",
        var backkup: String? = "",
        var backkup1: String? = "",
        var backkup2: String? = "",
        var backkup3: String? = "",
        var backkup4: String? = "",
        var backkup5: String? = "",
        var level_num: Int = 0,
        var unit_num: Int = 0,
        var views: Int = 0
) : Parcelable