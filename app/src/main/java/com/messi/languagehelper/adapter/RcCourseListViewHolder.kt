package com.messi.languagehelper.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVObject
import com.facebook.drawee.view.SimpleDraweeView
import com.messi.languagehelper.R
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.CourseList
import com.messi.languagehelper.coursefragment.CoursesActivity
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
        when (mAVObject.user_level_num) {
            0 -> {
                course_progress.progressDrawable = context.getDrawable(R.drawable.progress_bar_round_primary)
            }
            1 -> {
                course_progress.progressDrawable = context.getDrawable(R.drawable.progress_bar_round_yellow)
            }
            2 -> {
                course_progress.progressDrawable = context.getDrawable(R.drawable.progress_bar_round_yellow_dark)
            }
            else -> {
                course_progress.progressDrawable = context.getDrawable(R.drawable.progress_bar_round_orange)
            }
        }
        course_progress.max = mAVObject.unit_num
        course_progress.progress = mAVObject.user_unit_num
        if(mAVObject.user_level_num > 0){
            level_img.visibility = View.VISIBLE
            level_tv.text = mAVObject.user_level_num.toString()
            if (mAVObject.finish){
                course_progress.progress = mAVObject.unit_num
            }
        }else{
            level_img.visibility = View.GONE
            level_tv.text = ""
        }
        layout_cover.setOnClickListener { onItemClick(mAVObject) }
    }

    private fun onItemClick(mAVObject: CourseList) {
        val intent = Intent(context,CoursesActivity::class.java)
        val bundle = Bundle()
        bundle.putString(KeyUtil.CourseId,mAVObject.course_id)
        intent.putExtra(KeyUtil.BundleKey,bundle)
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