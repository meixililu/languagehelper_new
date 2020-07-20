package com.messi.languagehelper


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.WordDetailActivityBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.WordStudyViewModel
import java.util.*

class WordDetailActivity : BaseActivity() {

    lateinit var binding: WordDetailActivityBinding
    lateinit var viewModel: WordStudyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTextColor(true)
        setStatusbarColor(R.color.white)
        binding = WordDetailActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
        setData()
    }

    private fun init() {
        var wordTestType = intent.getStringExtra(KeyUtil.WordTestType)
        val itemList: ArrayList<WordDetailListItem> = intent.getParcelableArrayListExtra(KeyUtil.List)
        if (itemList == null) {
            ToastUtil.diaplayMesShort(this, "没有单词，请选择一本单词书！")
            finish()
        }
        viewModel = ViewModelProvider(this).get(WordStudyViewModel::class.java)
        viewModel.init(itemList)
        viewModel.wordTestType = wordTestType
        viewModel.resultAction.observe(this, Observer<String> { result ->
            LogUtil.DefalutLog(result)
            when (result) {
                "next" -> {
                    setData()
                }
                "test" -> {
                    showFragment(WordDCXYFragment())
                }
                "study" -> {
                    showFragment(WordStudyDuYinSuJiFragment())
                }
                "duyinxuanci" -> {
                    showFragment(WordStudyDuYinXuanCiFragment())
                }
            }
        })
    }

    private fun setData() {
        binding.classProgress.max = viewModel.totalSum
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.classProgress.setProgress(viewModel.hasLearnWordNum, true)
        }else{
            binding.classProgress.progress = viewModel.hasLearnWordNum
        }
        binding.closeBtn.setOnClickListener { finish() }
        if (viewModel.index < viewModel.randomPlayIndex.size) {
            viewModel.position = viewModel.randomPlayIndex[viewModel.index]
            val item: WordDetailListItem = viewModel.itemList[viewModel.position]
            viewModel.index++
            if (item != null && !item.isIs_know) {
                showFragment(WordRecognizeFragment())
            } else {
                setData()
            }
        } else {
            if (viewModel.hasLearnWordNum == viewModel.totalSum) {
                showFragment(WordToTestFragment())
            } else {
                viewModel.index = 0
                setData()
            }
        }
    }

    private fun showFragment(fragment: BaseFragment){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null && binding.fragmentContainer != null) {
            binding.fragmentContainer.removeAllViews()
        }
        MyPlayer.getInstance(this).onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}