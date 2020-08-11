package com.messi.languagehelper.coursefragment

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jeremyliao.liveeventbus.LiveEventBus
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.databinding.CourseCheckInFragmentBinding
import com.messi.languagehelper.databinding.CourseFinishFragmentBinding
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseCheckInFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var binding: CourseCheckInFragmentBinding
    val viewModel: MyCourseViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseCheckInFragmentBinding.inflate(inflater)
        initViews()
        return binding.root
    }

    private fun initViews() {
        LiveEventBus.get(KeyUtil.CourseListUpdate).post("")
        binding.checkBtn.setOnClickListener { checkOrNext() }
        setData()
        Handler().postDelayed({
            binding.checkBtn.isEnabled = true
        },600)
    }

    fun setData(){
        binding.scoreTv.text = "厉害！"
//        if (viewModel.userProfile != null && viewModel.userProfile?.check_in_sum!! > 1){
            binding.scoreTv.text = "厉害，连续打卡 " + viewModel.userProfile?.check_in_sum!! + " 天!"
            viewModel.userProfile!!.course_score += viewModel.userProfile?.check_in_sum!!
            binding.levelTv.text = "经验值加 " + viewModel.userProfile?.check_in_sum + " 点"
//        }else{
//            binding.levelTv.text = "每天打卡学习，学好英语并不难！"
//        }
    }

    private fun checkOrNext() {
        viewModel.toScore()
    }

}