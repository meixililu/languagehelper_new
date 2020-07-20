package com.messi.languagehelper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iflytek.cloud.*
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.WordStudyDuyinsujiBinding
import com.messi.languagehelper.impl.MyPlayerListener
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordStudyDuYinSuJiFragment : BaseFragment() {

    lateinit var binding: WordStudyDuyinsujiBinding
    private lateinit var viewModel: WordStudyViewModel
    private var isMimic = false
    lateinit var recognizer: SpeechRecognizer
    var word = ""
    private var userPcmPath = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath) + "wordmimic.pcm"

    var recognizerListener: RecognizerListener = object : RecognizerListener {

        override fun onVolumeChanged(volume: Int, p1: ByteArray?) {
            KViewUtil.showRecordImgAnimation(volume, binding.recordAnimImg)
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
            Handler().postDelayed({
                playUserPcm()
            },30)
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
                    checkPermission()
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

    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),1001)
        } else {
            mimic()
        }
    }

    private fun mimic() {
        isMimic = false
        if (!recognizer.isListening) {
            binding.recordLayout.visibility = View.VISIBLE
            binding.mimic.text = getString(R.string.finish)
            recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, userPcmPath)
            XFUtil.showSpeechRecognizer(context, PlayUtil.getSP(), recognizer, recognizerListener, XFUtil.VoiceEngineEN)
        } else {
            finishRecord()
            recognizer.stopListening()
        }
    }

    private fun playUserPcm() {
        PCMAudioPlayer.getInstance().startPlay(userPcmPath)
    }

    private fun playDelay(item: WordDetailListItem) {
        Handler().postDelayed({ playSound(item) }, 100)
    }

    private fun playSound(item: WordDetailListItem) {
        MyPlayer.getInstance(context).start(item.name, item.sound)
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
        MyPlayer.getInstance(context).onDestroy()
        if (recognizer != null) {
            recognizer.stopListening()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mimic()
            }
        }
    }

}