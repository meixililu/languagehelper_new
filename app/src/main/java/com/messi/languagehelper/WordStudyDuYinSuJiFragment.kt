package com.messi.languagehelper

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.iflytek.cloud.RecognizerListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechRecognizer
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.WordStudyDuyinsujiBinding
import com.messi.languagehelper.impl.MyPlayerListener
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordStudyDuYinSuJiFragment : BaseFragment() {

    private var playTimes = 0
    lateinit var binding: WordStudyDuyinsujiBinding
    private lateinit var viewModel: WordStudyViewModel
    private var isMimic = false
    lateinit var recognizer: SpeechRecognizer
    var word = ""

    var recognizerListener: RecognizerListener = object : RecognizerListener {

        override fun onVolumeChanged(volume: Int, p1: ByteArray?) {
            when {
                volume < 4 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_1)
                }
                volume < 8 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_2)
                }
                volume < 12 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_3)
                }
                volume < 16 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_4)
                }
                volume < 20 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_5)
                }
                volume < 24 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_6)
                }
                volume < 31 -> {
                    binding.recordAnimImg.setBackgroundResource(R.drawable.speak_voice_7)
                }
            }
        }

        override fun onResult(results: RecognizerResult?, isLast: Boolean) {
            LogUtil.DefalutLog("onResult")
            val text = JsonParser.parseIatResult(results?.resultString)
            LogUtil.DefalutLog(text)
            if (isLast) {
                finishRecord()
                showResult(text)
            }
        }

        override fun onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech")
        }

        override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
        }

        override fun onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech")
            finishRecord()
        }

        override fun onError(err: SpeechError?) {
            LogUtil.DefalutLog("onError:" + err?.errorDescription)
            finishRecord()
            ToastUtil.diaplayMesShort(context, err?.errorDescription)
        }
    }

    private fun showResult(text:String){
        if(context != null){
            val bean = ScoreUtil.score(word.toLowerCase(), text.toLowerCase())
            val resultText = ScoreUtil.getTestResult(bean.scoreInt)
            binding.scoreTv.text = resultText
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
        recognizer = SpeechRecognizer.createRecognizer(context, null)
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
        word = item.name
        binding.wordName.text = item.name
        binding.wordSymbol.text = item.symbol
        binding.wordDes.text = item.desc
        binding.layoutContent.setOnClickListener {
            isMimic = false
            MyPlayer.getInstance(context).stop()
            playSound(item)
        }
        binding.goOn.setOnClickListener { toNext() }
        binding.mimic.setOnClickListener {
            playAndListen(item)
        }
        MyPlayer.getInstance(context).stop()
        MyPlayer.getInstance(context).setListener(object : MyPlayerListener {
            override fun onStart() {
            }
            override fun onFinish() {
                if(isMimic){
                    mimic()
                }else{
                    replay(item)
                }

            }
        })
        playDelay(item)
    }

    private fun playAndListen(item: WordDetailListItem){
        if (!recognizer.isListening){
            binding.scoreTv.text = ""
            isMimic = true
            MyPlayer.getInstance(context).stop()
            playSound(item)
        }else {
            finishRecord()
            recognizer.stopListening()
        }
    }

    fun mimic() {
        isMimic = false
        if (!recognizer.isListening) {
            binding.recordLayout.visibility = View.VISIBLE
            binding.mimic.text = getString(R.string.finish)
            XFUtil.showSpeechRecognizer(context, PlayUtil.getSP(), recognizer, recognizerListener, XFUtil.VoiceEngineEN)
        } else {
            finishRecord()
            recognizer.stopListening()
        }
    }

    private fun playDelay(item: WordDetailListItem) {
        Handler().postDelayed({ playSound(item) }, 100)
    }

    private fun playSound(item: WordDetailListItem) {
        MyPlayer.getInstance(context).start(item.name, item.sound)
    }

    private fun replay(item: WordDetailListItem) {
        playTimes++
        if (playTimes < 2) {
            playSound(item)
        }
    }

    private fun toNext(){
        if (recognizer.isListening) {
            recognizer.stopListening()
        }
        MyPlayer.getInstance(context).onDestroy()
        viewModel.refreshData("duyinxuanci")
    }

    private fun finishRecord() {
        if(context != null) {
            binding.recordLayout.visibility = View.GONE
            binding.mimic.text = getString(R.string.go_on_follow)
            binding.goOn.text = getString(R.string.practice_next_level)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (recognizer != null) {
            recognizer.stopListening()
        }
    }
}