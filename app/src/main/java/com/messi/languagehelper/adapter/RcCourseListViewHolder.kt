package com.messi.languagehelper.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.messi.languagehelper.R
import com.messi.languagehelper.ReadDetailTouTiaoActivity
import com.messi.languagehelper.ViewModel.XXLModel
import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.CourseList
import com.messi.languagehelper.coursefragment.CoursesActivity
import com.messi.languagehelper.coursefragment.ListenCourseActivity
import com.messi.languagehelper.util.*

/**
 * Created by luli on 10/23/16.
 */
class RcCourseListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val layout_cover: FrameLayout
    private val course_name: TextView
    private val list_item_img: SimpleDraweeView
    private val course_progress: ProgressBar
    private val context: Context

    init {
        context = itemView.context
        layout_cover = itemView.findViewById<View>(R.id.course_item) as FrameLayout
        course_name = itemView.findViewById<View>(R.id.course_name) as TextView
        list_item_img = itemView.findViewById<View>(R.id.list_item_img) as SimpleDraweeView
        course_progress = itemView.findViewById<View>(R.id.course_progress) as ProgressBar
    }

    fun render(mAVObject: CourseList) {
        course_name.text = mAVObject.name
        list_item_img.setImageURI(mAVObject.img)
        course_progress.progress = mAVObject.current
        layout_cover.setOnClickListener { onItemClick(mAVObject) }
    }

    private fun onItemClick(mAVObject: CourseList) {
        val intent = Intent()
        if (TextUtils.isEmpty(mAVObject.to_activity)){
            val bundle = Bundle()
            bundle.putString(KeyUtil.CourseId,mAVObject.course_id)
            intent.setClass(context,CoursesActivity::class.java)
            intent.putExtra(KeyUtil.BundleKey,bundle)
        }else if (mAVObject.to_activity == "ListenCourseActivity"){
            intent.setClass(context,ListenCourseActivity::class.java)
        }
        context.startActivity(intent)
        BoxHelper.updateViews(mAVObject)
    }


}