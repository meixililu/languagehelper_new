package com.messi.languagehelper.bean

import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
class BoutiquesBean(
        @Expose() var objectId: String = "",
        @Expose() var title: String = "",
        @Expose() var content: String = "",
        @Expose() var code: String = "",
        @Expose() var img_url: String = "",
        @Expose() var tag: String = "",
        @Expose() var source_name: String = "",
        @Expose() var source_url: String = "",
        @Expose() var type: String = "",
        @Expose() var category: String = "",
        @Expose() var duration: String = "",
        @Expose() var views: Int = 0,
        @Expose() var isAd: Boolean = false
) : Parcelable {
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    var adData: AdData? = null
}