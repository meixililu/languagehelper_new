package com.messi.languagehelper.bean

import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.Expose
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class BoutiquesBean(
        var objectId: String = "",
        var title: String = "",
        var content: String = "",
        var code: String = "",
        var img_url: String = "",
        var tag: String = "",
        var source_name: String = "",
        var source_url: String = "",
        var type: String = "",
        var category: String = "",
        var duration: String = "",
        var views: Int = 0,
        var isAd: Boolean = false
) : Parcelable {
    @JSONField(serialize = false)
    @Transient
    var adData: AdData? = null
}