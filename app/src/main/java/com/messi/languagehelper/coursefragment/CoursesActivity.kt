package com.messi.languagehelper.coursefragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.BaseActivity
import com.messi.languagehelper.R
import com.messi.languagehelper.databinding.CoursesActivityBinding
import com.messi.languagehelper.impl.FragmentProgressbarListener
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.util.ToastUtil
import com.messi.languagehelper.viewmodels.MyCourseViewModel

class CoursesActivity: FragmentProgressbarListener, BaseActivity() {

    var course_id = ""
    lateinit var binding: CoursesActivityBinding
    lateinit var viewModel: MyCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTextColor(true)
        setStatusbarColor(R.color.white)
        binding = CoursesActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val bundle = intent.getBundleExtra(KeyUtil.BundleKey)
        course_id = bundle.getString(KeyUtil.CourseId,"")
        if (!TextUtils.isEmpty(course_id)){
            viewModel = ViewModelProvider(this).get(MyCourseViewModel::class.java)
            viewModel.course_id = course_id
            viewModel.loadData()
            binding.closeBtn.setOnClickListener { finish() }
            viewModel.result.observe(this, Observer {
                when (it) {
                    "finish" -> {
                        ToastUtil.diaplayMesLong(this,"finish")
                    }
                    "error" -> {
                        ToastUtil.diaplayMesLong(this,"error")
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
                }
            })
        }
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
        MyPlayer.getInstance(this).onDestroy()
    }

}