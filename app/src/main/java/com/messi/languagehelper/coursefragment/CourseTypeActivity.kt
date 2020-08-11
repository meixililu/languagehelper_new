package com.messi.languagehelper.coursefragment

import android.os.Bundle
import android.view.LayoutInflater
import com.messi.languagehelper.BaseActivity
import com.messi.languagehelper.R
import com.messi.languagehelper.coursefragment.CourseListFragment.Companion.getInstance
import com.messi.languagehelper.databinding.CourseTypeActivityBinding
import com.messi.languagehelper.util.KeyUtil

class CourseTypeActivity : BaseActivity() {

    lateinit var binding: CourseTypeActivityBinding
    var title = ""
    var type = ""
    var column = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CourseTypeActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
        initFragment()
    }

    private fun initViews() {
        var bundle = intent.getBundleExtra(KeyUtil.BundleKey)
        title = bundle.getString(KeyUtil.ActionbarTitle,"")
        type = bundle.getString(KeyUtil.Type,"")
        column = bundle.getInt(KeyUtil.Column,3)
        binding.myAwesomeToolbar.title = title
    }

    private fun initFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, getInstance(column, "", type))
                .commit()
    }

}