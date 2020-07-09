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
import com.messi.languagehelper.databinding.WordStudyDuyinxuanciBinding
import com.messi.languagehelper.util.KStringUtils
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.util.NumberUtil
import com.messi.languagehelper.util.Setings
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordStudyDuYinXuanCiFragment : BaseFragment() {

    lateinit var binding: WordStudyDuyinxuanciBinding
    private lateinit var viewModel: WordStudyViewModel
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = WordStudyDuyinxuanciBinding.inflate(inflater)
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
        answerRight = ourSounds.load(context, R.raw.answer_right, 1)
        answerWrong = ourSounds.load(context, R.raw.answer_wrong, 1)
    }

    fun setData() {
        var item = viewModel.currentItem
        binding.wordLayout.setOnClickListener { playMp3(item.name, item.sound) }
        binding.wordDes.text = item.desc
        val samples = KStringUtils.getWordSamples(item.name)
        if (samples.size == 4) {
            binding.selection1.text = samples[0]
            binding.selection2.text = samples[1]
            binding.selection3.text = samples[2]
            binding.selection4.text = samples[3]
            binding.selection1.setOnClickListener { checkResultThenGoNext(binding.selection1, item) }
            binding.selection2.setOnClickListener { checkResultThenGoNext(binding.selection2, item) }
            binding.selection3.setOnClickListener { checkResultThenGoNext(binding.selection3, item) }
            binding.selection4.setOnClickListener { checkResultThenGoNext(binding.selection4, item) }
        }
    }

    private fun checkResultThenGoNext(tv: TextView, item: WordDetailListItem) {
        if (Setings.isFastClick(context)) {
            return
        }
        val text = tv.text.toString()
        if (item.name != text) {
            playSoundPool(false)
            tv.setTextColor(resources.getColor(R.color.material_color_red))
            tv.setBackgroundResource(R.drawable.border_shadow_wrong_oval)
        } else {
            playSoundPool(true)
            tv.setTextColor(resources.getColor(R.color.material_color_green))
            tv.setBackgroundResource(R.drawable.border_shadow_right_oval)
            Handler().postDelayed({
                viewModel.refreshData("next")
            }, 500)
        }
    }

    private fun playMp3(content: String, url: String) {
        MyPlayer.getInstance(context).start(content, url)
    }

    private fun playSoundPool(isRight: Boolean) {
        if (isRight) {
            ourSounds.play(answerRight, 1f, 1f, 1, 0, 1f)
        } else {
            ourSounds.play(answerWrong, 1f, 1f, 1, 0, 1f)
        }
    }
}