package com.messi.languagehelper

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.databinding.CoursesActivityBinding
import com.messi.languagehelper.impl.FragmentProgressbarListener
import com.messi.languagehelper.util.AVOUtil
import com.messi.languagehelper.viewmodels.MyCourseViewModel

class CoursesActivity: FragmentProgressbarListener, BaseActivity() {

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
        setActionBarTitle("Lesson 1")
        viewModel = ViewModelProvider(this).get(MyCourseViewModel::class.java)
        binding.closeBtn.setOnClickListener { finish() }
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ReadingFragment.newInstance(AVOUtil.Category.composition, ""))
                .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showQuickDialog()
    }

    private fun showQuickDialog() {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
        builder.setTitle("温馨提示")
        builder.setMessage("正在学习中，确定要退出？")
        builder.setPositiveButton("确认") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            this@CoursesActivity.finish()
        }
        builder.setNegativeButton("取消") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun initFragment(){

    }

    private fun onEmailClick() {

    }

    private fun onImgClick() {

    }

//    override fun showProgressbar(){
//
//    }
}