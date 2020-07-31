package com.messi.languagehelper.coursefragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.messi.languagehelper.BaseActivity
import com.messi.languagehelper.R
import com.messi.languagehelper.databinding.CoursesActivityBinding
import com.messi.languagehelper.impl.FragmentProgressbarListener
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel

class CoursesActivity: FragmentProgressbarListener, BaseActivity() {

    var course_id = ""
    lateinit var binding: CoursesActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTextColor(true)
        setStatusbarColor(R.color.white)
        binding = CoursesActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    private fun setTempData(viewModel: MyCourseViewModel){
        binding.tempData.text = "level:"+viewModel.userCourseRecord.user_level_num+
                "unit:"+viewModel.userCourseRecord.user_unit_num+
                "order:"+viewModel.currentCourse.order
    }

    private fun init() {
        IPlayerUtil.MPlayerPause()
        val viewModel: MyCourseViewModel by viewModels()
        binding.loadingAv.visibility = View.VISIBLE
        binding.loadingAv.playAnimation()
        val bundle = intent.getBundleExtra(KeyUtil.BundleKey)
        course_id = bundle.getString(KeyUtil.CourseId,"")
        viewModel.course_id = course_id
        viewModel.loadData()
        viewModel.progress.observe(this, Observer {
            LogUtil.DefalutLog("viewModel.progress")
            binding.classProgress.max = it
            var cprogress = binding.classProgress.progress + 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.classProgress.setProgress(cprogress, true)
            }else{
                binding.classProgress.progress = cprogress
            }
        })
        viewModel.result.observe(this, Observer {
            binding.loadingAv.visibility = View.GONE
            binding.loadingAv.cancelAnimation()
            setTempData(viewModel)
            when (it) {
                "finish" -> {
                    initFragment(CourseFinishFragment())
                }
                "error" -> {
                    ToastUtil.diaplayMesShort(this,"error")
                }
                "translate" -> {
                    initFragment(CourseTranslateFragment())
                }
                "translate_enter" -> {
                    initFragment(CourseTranslateEnterFragment())
                }
                "listen" -> {
                    initFragment(CourseListenFragment())
                }
                "listen_enter" -> {
                    initFragment(CourseListenEnterFragment())
                }
                "word_enter" -> {
                    initFragment(CourseWordEnterFragment())
                }
                "choice" -> {
                    initFragment(CourseChoiceFragment())
                }
                "enter" -> {
                    initFragment(CourseEnterFragment())
                }
                "word" -> {
                    initFragment(CourseWordFragment())
                }
                "mimic" -> {
                    initFragment(CourseMimicFragment())
                }
                "video" -> {
                    initFragment(CourseVideoFragment())
                }
                "mimic_video" -> {
                    initFragment(CourseMimicVideoFragment())
                }
                "word_spell" -> {
                    initFragment(CourseWordSpellFragment())
                }
                "word_llk" -> {
                    initFragment(CourseWordLLKFragment())
                }
                else -> {
                    ToastUtil.diaplayMesShort(this,"未知Type")
                    viewModel.next()
                }
            }
        })
        binding.closeBtn.setOnClickListener { finish() }
    }

    private fun initFragment(fragment: Fragment){
        hideKeyBoard()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyBoard()
        MyPlayer.getInstance(this).onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}