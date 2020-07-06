package com.messi.languagehelper

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.WordStudyDancixuanyiBinding
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.util.NumberUtil
import com.messi.languagehelper.util.Setings
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordDCXYFragment : BaseFragment() {

    lateinit var binding: WordStudyDancixuanyiBinding
    private lateinit var viewModel: WordStudyViewModel
    lateinit var ourSounds: SoundPool
    private var answer_right = 0
    private var answer_wrong = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = WordStudyDancixuanyiBinding.inflate(inflater)
        initializeSoundPool()
        setData()
        return binding.root
    }

    private fun initializeSoundPool() {
        ourSounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else {
            SoundPool(5, AudioManager.STREAM_MUSIC, 1)
        }
        answer_right = ourSounds.load(context, R.raw.answer_right, 1)
        answer_wrong = ourSounds.load(context, R.raw.answer_wrong, 1)
    }

    fun setData() {
        var item = viewModel.currentItem
        binding.wordTv.text = item.name
        binding.wordLayout.setOnClickListener { playMp3(item.name, item.sound) }
        val tvList = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
                if (viewModel.totalSum < 4) 10 else viewModel.totalSum,
                0, 3, viewModel.position)
        if (tvList.size == 4) {
            if (viewModel.totalSum > tvList[0]) {
                binding.selection1.text = viewModel.itemList[tvList[0]].desc
            } else {
                binding.selection1.text = BoxHelper.getBench().desc
            }
            if (viewModel.totalSum > tvList[1]) {
                binding.selection2.text = viewModel.itemList[tvList[1]].desc
            } else {
                binding.selection2.text = BoxHelper.getBench().desc
            }
            if (viewModel.totalSum > tvList[2]) {
                binding.selection3.text = viewModel.itemList[tvList[2]].desc
            } else {
                binding.selection3.text = BoxHelper.getBench().desc
            }
            if (viewModel.totalSum > tvList[3]) {
                binding.selection4.text = viewModel.itemList[tvList[3]].desc
            } else {
                binding.selection4.text = BoxHelper.getBench().desc
            }
            binding.selection1Layout.setOnClickListener { checkResultThenGoNext(binding.selection1, item) }
            binding.selection2Layout.setOnClickListener { checkResultThenGoNext(binding.selection2, item) }
            binding.selection3Layout.setOnClickListener { checkResultThenGoNext(binding.selection3, item) }
            binding.selection4Layout.setOnClickListener { checkResultThenGoNext(binding.selection4, item) }
        }
    }

    private fun checkResultThenGoNext(tv: TextView, item: WordDetailListItem) {
        if (Setings.isFastClick(context)) {
            return
        }
        val text = tv.text.toString()
        if (item.desc != text) {
            playSoundPool(false)
            tv.setTextColor(resources.getColor(R.color.material_color_red))
        } else {
            playSoundPool(true)
            tv.setTextColor(resources.getColor(R.color.material_color_green))
            item.isIs_know = true
        }
        Handler().postDelayed({
            viewModel.refreshData("next")
        }, 500)
        if (item.isIs_know) {
            BoxHelper.insert(item)
        }
    }

    private fun playMp3(content: String, url: String) {
        MyPlayer.getInstance(context).start(content, url)
    }

    private fun playSoundPool(isRight: Boolean) {
        if (isRight) {
            ourSounds.play(answer_right, 1f, 1f, 1, 0, 1f)
        } else {
            ourSounds.play(answer_wrong, 1f, 1f, 1, 0, 1f)
        }
    }

}