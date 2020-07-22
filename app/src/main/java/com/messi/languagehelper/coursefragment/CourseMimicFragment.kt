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
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.iflytek.cloud.*
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.ListenCourseData
import com.messi.languagehelper.databinding.CourseMimicFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel

class CourseMimicFragment : BaseFragment() {

    lateinit var binding: CourseMimicFragmentBinding
    lateinit var viewModel: MyCourseViewModel
    private var isMimic = false
    lateinit var recognizer: SpeechRecognizer
    lateinit var mAVObject: ListenCourseData
    lateinit var player: SimpleExoPlayer
    private var startPosition = 0L
    private var endPosition = 0L
    private var userPcmPath = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath) + "wordmimic.pcm"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MyCourseViewModel::class.java)
        recognizer = SpeechRecognizer.createRecognizer(context, null)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(listener)
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
        if(TextUtils.isEmpty(mAVObject.img)){
            binding.playBtn.visibility = View.VISIBLE
            binding.imgLayout.visibility = View.GONE
        } else {
            binding.playBtn.visibility = View.GONE
            binding.imgLayout.visibility = View.VISIBLE
            binding.imgItem.setImageURI(mAVObject.img)
        }
        binding.wordName.text = mAVObject.content
        binding.imgLayout.setOnClickListener {
            justPlay()
        }
        binding.playBtn.setOnClickListener {
            justPlay()
        }
        binding.goOn.setOnClickListener { toNext() }
        binding.mimic.setOnClickListener {
            playAndListen()
        }
        playDelay()
    }

    private fun justPlay(){
        isMimic = false
        playSound()
    }

    private fun playAndListen(){
        if (!recognizer.isListening){
            showOrHidePrompt(0)
            binding.scoreTv.text = ""
            isMimic = true
            player.playWhenReady = false
            playSound()
        }else {
            finishRecord()
            recognizer.stopListening()
        }
    }

    private fun showOrHidePrompt(status: Int){
        when(status){
            0 -> {
                binding.recordAnimationLayout.visibility = View.VISIBLE
                binding.recordAnimationText.text = "Listen"
            }
            1 -> {
                binding.recordAnimationLayout.visibility = View.VISIBLE
                binding.recordAnimationText.text = "Your turn"
            }
            2 -> {
                binding.recordAnimationLayout.visibility = View.GONE
                binding.recordAnimationText.text = ""
            }
        }
    }

    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),1001)
        } else {
            animationMimic()
        }
    }

    private fun animationMimic(){
        showOrHidePrompt(1)
        Handler().postDelayed({
            showOrHidePrompt(2)
            mimic()
        },900)
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

    private fun playDelay() {
        Handler().postDelayed({ playSound() }, 10)
    }

    private fun playSound() {
        if(PCMAudioPlayer.getInstance().isPlaying){
            PCMAudioPlayer.getInstance().stopPlay()
        }
        binding.playBtn.playAnimation()
        binding.imgPlayBtn.playAnimation()
        var mp3Url = mAVObject.mp3_url
        var startTime = mAVObject.start_time
        var endTime = mAVObject.end_time
        if(TextUtils.isEmpty(mp3Url)){
            mp3Url = MyPlayer.playUrl + mAVObject.content
        }
        if(!TextUtils.isEmpty(startTime)){
            startPosition = KStringUtils.getTimeMills(startTime)
            if(!TextUtils.isEmpty(endTime)){
                endPosition = KStringUtils.getTimeMills(endTime)
            }
        }
        val videoSource = MyPlayer.getMediaSource(mp3Url)
        player.prepare(videoSource)
        player.playWhenReady = true
    }

    private fun toNext(){
        relase()
        viewModel.next()
    }

    private fun finishRecord() {
        if(context != null) {
            binding.recordLayout.visibility = View.GONE
            binding.mimic.text = getString(R.string.start_to_follow)
            binding.goOn.text = getString(R.string.practice_next_level)
        }
    }

    private fun relase(){
        endPosition = 0
        if (player != null){
            player.stop()
            player.release()
        }
        if (recognizer != null) {
            recognizer.stopListening()
        }
        PCMAudioPlayer.getInstance().stopPlay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        relase()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mimic()
            }
        }
    }

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

    private fun stopAtEndPosition() {
        if(endPosition > 0){
            Handler().postDelayed({
                if (player.currentPosition > endPosition) {
                    binding.playBtn.cancelAnimation()
                    binding.imgPlayBtn.cancelAnimation()
                    player.playWhenReady = false
                }else{
                    stopAtEndPosition()
                }
            }, 10)
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

    var listener = object : Player.EventListener{
        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            LogUtil.DefalutLog("---onTracksChanged---")
        }
        override fun onLoadingChanged(isLoading: Boolean) {
            LogUtil.DefalutLog("---onLoadingChanged---")
        }
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            LogUtil.DefalutLog("---onPlayerStateChanged---")
            when (playbackState) {
                Player.STATE_IDLE -> LogUtil.DefalutLog("STATE_IDLE")
                Player.STATE_BUFFERING -> LogUtil.DefalutLog("STATE_BUFFERING")
                Player.STATE_READY -> {
                    if(playWhenReady){
                        if (startPosition > 0) {
                            player.seekTo(startPosition)
                            player.playWhenReady = true
                            startPosition = 0
                            stopAtEndPosition()
                        }
                    }else {
                        if(isMimic){
                            checkPermission()
                        }
                    }
                }
                Player.STATE_ENDED -> {
                    binding.playBtn.cancelAnimation()
                    binding.imgPlayBtn.cancelAnimation()
                    if(isMimic){
                        checkPermission()
                    }
                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            LogUtil.DefalutLog("---onRepeatModeChanged---")
        }
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            LogUtil.DefalutLog("---onShuffleModeEnabledChanged---")
        }
        override fun onPlayerError(error: ExoPlaybackException) {

        }
        override fun onPositionDiscontinuity(reason: Int) {
            LogUtil.DefalutLog("---onPositionDiscontinuity---")
        }
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            LogUtil.DefalutLog("---onPlaybackParametersChanged---")
        }
        override fun onSeekProcessed() {
            LogUtil.DefalutLog("---onSeekProcessed---")
        }
    }
}