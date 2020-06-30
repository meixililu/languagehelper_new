package com.messi.languagehelper.util

import android.content.Context
import android.text.TextUtils
import cn.leancloud.AVObject
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.bean.ReadingCategory
import com.messi.languagehelper.box.*
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.track.Track
import java.util.*

object KDataUtil {

    fun fromBoutiquesBeanToReading(mBoutiques: BoutiquesBean): Reading {
        val item = Reading()
        item.title = mBoutiques.title
        item.content = mBoutiques.content
        item.img_url = mBoutiques.img_url
        item.source_name = mBoutiques.source_name
        item.source_url = mBoutiques.source_url
        item.content_type = "url_api"
        item.category = mBoutiques.category
        return item
    }

}