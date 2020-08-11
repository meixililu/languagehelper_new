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
import com.messi.languagehelper.databinding.CourseFinishFragmentBinding
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseFinishFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var binding: CourseFinishFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var isShowCHeckIn = false
    val viewModel: MyCourseViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseFinishFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        return binding.root
    }

    private fun initViews() {
        if (viewModel.show_check_in()){
            isShowCHeckIn = true
        }
        LiveEventBus.get(KeyUtil.CourseListUpdate).post("")
        binding.checkBtn.setOnClickListener { checkOrNext() }
        setData()
        Handler().postDelayed({
            playSoundPool()
            binding.checkBtn.isEnabled = true
        },600)
    }

    fun setData(){
        if (viewModel.userCourseRecord != null && viewModel.userProfile != null && viewModel.userProfile?.show_level_up!!){
            if (viewModel.userCourseRecord.user_level_num == viewModel.userCourseRecord.level_num){
                binding.scoreTv.text = viewModel.userCourseRecord.name + " 技能达到了最高级" + "\n面对疾风吧！"
            }else{
                binding.scoreTv.text = viewModel.userCourseRecord.name + " 技能达到第" + viewModel.userCourseRecord.user_level_num + "级" + "\n面对疾风吧！"
            }
            viewModel.userProfile?.show_level_up = false
            BoxHelper.update(viewModel.userProfile)
        }else{
            binding.scoreTv.text = "本单元学习完成啦！"
        }
        binding.levelTv.text = "当前级别："+viewModel.userCourseRecord.user_level_num.toString() + " / " + viewModel.userCourseRecord.level_num
        binding.unitTv.text =  "下一单元："+viewModel.userCourseRecord.user_unit_num.toString() + " / " + viewModel.userCourseRecord.unit_num
    }

    private fun checkOrNext() {
        if (isShowCHeckIn){
            viewModel.toCheckIn()
        }else{
            viewModel.toScore()
        }
    }

    private fun initializeSoundPool() {
        ourSounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else {
            SoundPool(5, AudioManager.STREAM_MUSIC, 1)
        }
        answerRight = ourSounds.load(context, R.raw.lesson_complete, 1)
    }

    private fun playSoundPool() {
        ourSounds.play(answerRight, 1f, 1f, 1, 0, 1f)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ourSounds.stop(answerRight)
    }
}