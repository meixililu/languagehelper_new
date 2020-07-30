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
        var course_score: Int = 0,
        var backkup: String? = "",
        var backkup1: String? = "",
        var backkup2: String? = "",
        var backkup3: String? = "",
        var backkup4: String? = "",
        var backkup5: String? = ""
) : Parcelable