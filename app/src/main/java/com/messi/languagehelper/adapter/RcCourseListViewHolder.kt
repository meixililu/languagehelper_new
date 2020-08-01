package com.messi.languagehelper.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVObject
import com.facebook.drawee.view.SimpleDraweeView
import com.messi.languagehelper.R
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.CourseList
import com.messi.languagehelper.coursefragment.CoursesActivity
import com.messi.languagehelper.coursefragment.ListenCourseActivity
import com.messi.languagehelper.util.AVOUtil
import com.messi.languagehelper.util.KeyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by luli on 10/23/16.
 */
class RcCourseListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val layout_cover: RelativeLayout
    private val course_name: TextView
    private val level_tv: TextView
    private val level_img: ImageView
    private val list_item_img: SimpleDraweeView
    private val course_progress: ProgressBar
    private val context: Context

    init {
        context = itemView.context
        layout_cover = itemView.findViewById<View>(R.id.course_item) as RelativeLayout
        course_name = itemView.findViewById<View>(R.id.course_name) as TextView
        level_tv = itemView.findViewById<View>(R.id.level_tv) as TextView
        level_img = itemView.findViewById<View>(R.id.level_img) as ImageView
        list_item_img = itemView.findViewById<View>(R.id.list_item_img) as SimpleDraweeView
        course_progress = itemView.findViewById<View>(R.id.course_progress) as ProgressBar
    }

    fun render(mAVObject: CourseList) {
        course_name.text = mAVObject.name
        list_item_img.setImageURI(mAVObject.img)
        course_progress.max = mAVObject.unit_num
        course_progress.progress = 0
        if (mAVObject.user_unit_num >= 1){
            course_progress.progress = mAVObject.user_unit_num - 1
        }
        if(mAVObject.user_level_num > 1){
            level_img.visibility = View.VISIBLE
            level_img.setImageResource(R.drawable.crowns_drawer)
            if (mAVObject.finish){
                level_tv.text = mAVObject.user_level_num.toString()
                course_progress.progress = mAVObject.unit_num
            }else{
                level_tv.text = (mAVObject.user_level_num - 1).toString()
            }
        }else{
            level_img.visibility = View.GONE
            level_img.setImageResource(R.drawable.crown_gray)
            level_tv.text = ""
        }
        layout_cover.setOnClickListener { onItemClick(mAVObject) }
    }

    private fun onItemClick(mAVObject: CourseList) {
        val intent = Intent()
        if (TextUtils.isEmpty(mAVObject.to_activity)){
            val bundle = Bundle()
            bundle.putString(KeyUtil.CourseId,mAVObject.course_id)
            bundle.putParcelable(KeyUtil.ObjectKey,mAVObject)
            intent.setClass(context,CoursesActivity::class.java)
            intent.putExtra(KeyUtil.BundleKey,bundle)
        }else if (mAVObject.to_activity == "ListenCourseActivity"){
            intent.setClass(context,ListenCourseActivity::class.java)
            intent.putExtra(KeyUtil.ObjectKey,mAVObject)
        }
        context.startActivity(intent)
        update(mAVObject)
    }

    private fun update(mAVObject: CourseList){
        CoroutineScope(Dispatchers.IO).launch {
            BoxHelper.updateViews(mAVObject)
            val item = AVObject.createWithoutData(AVOUtil.CourseList.CourseList, mAVObject.objectId)
            item.increment(AVOUtil.CourseList.views)
            item.save()
        }
    }


}