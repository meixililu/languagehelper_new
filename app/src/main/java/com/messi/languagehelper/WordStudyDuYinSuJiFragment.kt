package com.messi.languagehelper

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.WordStudyDuyinsujiBinding
import com.messi.languagehelper.impl.MyPlayerListener
import com.messi.languagehelper.util.LogUtil
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordStudyDuYinSuJiFragment : BaseFragment() {

    private var playTimes = 0
    lateinit var binding: WordStudyDuyinsujiBinding
    private lateinit var viewModel: WordStudyViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        LogUtil.DefalutLog("WordRecognizeFragment---onCreateView")
        binding = WordStudyDuyinsujiBinding.inflate(inflater)
        setData()
        return binding.root
    }

    private fun setData() {
        playTimes = 0
        var item = viewModel.currentItem
        binding.wordName.text = item.name
        binding.wordSymbol.text = item.symbol;
        binding.layoutContent.setOnClickListener { toNext(item.name) }
        MyPlayer.getInstance(context).stop()
        MyPlayer.getInstance(context).setListener(object : MyPlayerListener {
            override fun onStart() {
            }
            override fun onFinish() {
                replay(item)
            }
        })
        playDelay(item)
    }

    private fun playDelay(item: WordDetailListItem) {
        Handler().postDelayed({ playSound(item) }, 100)
    }

    private fun playSound(item: WordDetailListItem) {
        playItem(item)
        MyPlayer.getInstance(context).start(item.name, item.sound)
    }

    private fun replay(item: WordDetailListItem) {
        playTimes++
        if (playTimes < 3) {
            playSound(item)
        } else {
            toNext(item.name)
        }
    }

    private fun playItem(item: WordDetailListItem) {
        if (playTimes == 1) {
            binding.wordDes.text = item.desc;
        }
    }

    private fun toNext(name:String){
        MyPlayer.getInstance(context).onDestroy()
        var letters = name.split("")
        var size = letters.size
        if (size > 1){
            viewModel.refreshData("duyinxuanci")
        }else{
            viewModel.refreshData("next")
        }
    }
}