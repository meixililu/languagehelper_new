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
class UserProfile(
        @Id var id: Long = 0,
        var user_name: String = "",
        var user_img: String = "",
        var credits: Int = 0,
        var continuous: Int = 0,
        var show_check_in: Boolean = false,
        var check_in_sum: Int = 0,
        var last_check_in: String = "",
        var course_score: Int = 0,
        var show_level_up: Boolean = false,
        var course_unit_sum: Int = 0,
        var course_level_sum: Int = 0,
        var backkup: String? = "",
        var backkup1: String? = "",
        var backkup2: String? = "",
        var backkup3: String? = "",
        var backkup4: String? = "",
        var backkup5: String? = ""
) : Parcelable