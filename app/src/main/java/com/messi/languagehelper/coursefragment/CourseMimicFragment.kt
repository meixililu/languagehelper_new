package com.messi.languagehelper.coursefragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iflytek.cloud.*
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.ListenCourseData
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.CourseMimicFragmentBinding
import com.messi.languagehelper.databinding.WordStudyDuyinsujiBinding
import com.messi.languagehelper.impl.MyPlayerListener
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class CourseMimicFragment : BaseFragment() {

    lateinit var binding: CourseMimicFragmentBinding
    lateinit var viewModel: MyCourseViewModel
    private var isMimic = false
    lateinit var recognizer: SpeechRecognizer
    lateinit var mAVObject: ListenCourseData
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
            val bean = ScoreUtil.score(mAVObject.content.toLowerCase(), text.toLowerCase())
            val resultText = ScoreUtil.getTestResult(bean.scoreInt)
            binding.scoreTv.text = resultText
            Handler().postDelayed({
                playUserPcm()
            },20)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MyCourseViewModel::class.java)
        recognizer = SpeechRecognizer.createRecognizer(context, null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseMimicFragmentBinding.inflate(inflater)
        setData()
        return binding.root
    }

    private fun setData() {
        mAVObject = viewModel.currentCourse
        if (TextUtils.isEmpty(mAVObject.tips)){
            binding.tips.visibility = View.GONE
        }else{
            binding.tips.visibility = View.VISIBLE
            binding.tips.text = mAVObject.tips
        }
        if (!TextUtils.isEmpty(mAVObject.title)){
            binding.titleTv.text = mAVObject.title
        }
        if (!TextUtils.isEmpty(mAVObject.transalte)){
            binding.wordDes.text = mAVObject.transalte
        }
        binding.wordName.text = mAVObject.content
        binding.playBtn.setOnClickListener {
            isMimic = false
            MyPlayer.getInstance(context).stop()
            playSound()
        }
        binding.goOn.setOnClickListener { toNext() }
        binding.mimic.setOnClickListener {
            playAndListen()
        }
        MyPlayer.getInstance(context).stop()
        MyPlayer.getInstance(context).setListener(object : MyPlayerListener {
            override fun onStart() {
            }
            override fun onFinish() {
                binding.playBtn.cancelAnimation()
                if(isMimic){
                    checkPermission()
                }
            }
        })
        playDelay()
    }

    private fun playAndListen(){
        if (!recognizer.isListening){
            binding.scoreTv.text = ""
            isMimic = true
            MyPlayer.getInstance(context).stop()
            playSound()
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

    fun mimic() {
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

    private fun playDelay() {
        Handler().postDelayed({ playSound() }, 100)
    }

    private fun playSound() {
        binding.playBtn.playAnimation()
        MyPlayer.getInstance(context).start(mAVObject.content)
    }

    private fun toNext(){
        if (recognizer.isListening) {
            recognizer.stopListening()
        }
        MyPlayer.getInstance(context).onDestroy()
        viewModel.next()
    }

    private fun finishRecord() {
        if(context != null) {
            binding.recordLayout.visibility = View.GONE
            binding.mimic.text = getString(R.string.start_to_follow)
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