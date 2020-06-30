package com.messi.languagehelper.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.messi.languagehelper.R
import com.messi.languagehelper.ReadDetailTouTiaoActivity
import com.messi.languagehelper.ViewModel.XXLModel
import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.util.*

/**
 * Created by luli on 10/23/16.
 */
class RcBiliSearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val layout_cover: FrameLayout
    private val name: TextView
    private val list_item_img: SimpleDraweeView
    private val ad_layout: FrameLayout
    private val content_layout: FrameLayout
    private val context: Context

    fun render(mAVObject: BoutiquesBean) {
        LogUtil.DefalutLog(mAVObject.img_url)
        ad_layout.visibility = View.GONE
        content_layout.visibility = View.GONE
        name.text = ""
        if (mAVObject.isAd && mAVObject.adData != null) {
            val mAdData = mAVObject.adData
            if (mAdData!!.mNativeADDataRef != null) {
                content_layout.visibility = View.VISIBLE
                val mNativeADDataRef = mAdData.mNativeADDataRef
                name.text = mNativeADDataRef!!.title + "  广告"
                list_item_img.aspectRatio = 1.5.toFloat()
                list_item_img.visibility = View.VISIBLE
                list_item_img.setImageURI(mNativeADDataRef.imgUrl)
                list_item_img.setOnClickListener { v: View? ->
                    val onClicked = mNativeADDataRef.onClick(v)
                    LogUtil.DefalutLog("onClicked:$onClicked")
                }
            } else if (mAdData.mTXADView != null) {
                ad_layout.removeAllViews()
                ad_layout.visibility = View.VISIBLE
                val mADView = mAdData.mTXADView
                if (mADView!!.parent != null) {
                    (mADView.parent as ViewGroup).removeView(mADView)
                }
                ad_layout.addView(mADView)
                mADView.render()
            } else if (mAdData.bdAdView != null) {
                ad_layout.removeAllViews()
                ad_layout.visibility = View.VISIBLE
                val adView = mAdData.bdAdView
                val height = mAdData.bdHeight
                if (adView!!.parent != null) {
                    (adView.parent as ViewGroup).removeView(adView)
                }
                val marginT = ScreenUtil.dip2px(context, 10f)
                val marginB = ScreenUtil.dip2px(context, 5f)
                val marginLR = ScreenUtil.dip2px(context, 10f)
                val rllp = LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH - marginLR * 2, height)
                rllp.setMargins(marginLR, marginT, marginLR, marginB)
                ad_layout.addView(adView, rllp)
            } else if (mAdData.csjTTFeedAd != null) {
                ad_layout.removeAllViews()
                ad_layout.visibility = View.VISIBLE
                val ad = mAdData.csjTTFeedAd
                XXLModel.setCSJDView(context, ad, ad_layout)
            }
        } else {
            content_layout.visibility = View.VISIBLE
            name.text = mAVObject.title
            if (TextUtils.isEmpty(mAVObject.img_url)) {
                list_item_img.visibility = View.GONE
            } else {
                list_item_img.visibility = View.VISIBLE
                list_item_img.setImageURI(mAVObject.img_url)
            }
            layout_cover.setOnClickListener { onItemClick(mAVObject) }
        }
    }

    private fun onItemClick(mAVObject: BoutiquesBean) {
        val intent = Intent(context, ReadDetailTouTiaoActivity::class.java)
        Setings.dataMap[KeyUtil.DataMapKey] = KDataUtil.fromBoutiquesBeanToReading(mAVObject)
        context.startActivity(intent)
    }

    init {
        context = itemView.context
        layout_cover = itemView.findViewById<View>(R.id.layout_cover) as FrameLayout
        name = itemView.findViewById<View>(R.id.name) as TextView
        list_item_img = itemView.findViewById<View>(R.id.list_item_img) as SimpleDraweeView
        ad_layout = itemView.findViewById<View>(R.id.ad_layout) as FrameLayout
        content_layout = itemView.findViewById<View>(R.id.content_layout) as FrameLayout
    }
}