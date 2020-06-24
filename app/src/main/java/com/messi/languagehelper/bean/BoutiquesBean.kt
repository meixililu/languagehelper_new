package com.messi.languagehelper.bean

import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoutiquesBean(
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
        var views: Int = 0,
        var isAd: Boolean = false
) : Parcelable {
    @JSONField(serialize = false)
    var adData: AdData? = null
}