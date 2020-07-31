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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.iflytek.cloud.*
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.bean.CourseMedias
import com.messi.languagehelper.databinding.CourseMimicFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel
import java.lang.StringBuilder

class CourseMimicFragment : BaseFragment() {

    lateinit var binding: CourseMimicFragmentBinding
    val viewModel: MyCourseViewModel by activityViewModels()
    private var isMimic = false
    lateinit var recognizer: SpeechRecognizer
    lateinit var mAVObject: CourseData
    lateinit var mimics: List<CourseMedias>
    lateinit var player: SimpleExoPlayer
    private var lastUrl = "lastUrl"
    private var position = 0
    private var startPosition = 0L
    private var endPosition = 0L
    private var sb = StringBuilder()
    private var videoSource: MediaSource? = null
    private var userPcmPath = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath) + "wordmimic.pcm"

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        if(viewModel.currentCourse.medias != null){
            position = 0
            mAVObject = viewModel.currentCourse
            mimics = viewModel.currentCourse.medias!!
            mAVObject.user_result = true
            if (TextUtils.isEmpty(mAVObject.tips)){
                binding.tips.visibility = View.GONE
            }else{
                binding.tips.visibility = View.VISIBLE
                binding.tips.text = mAVObject.tips
            }
            if (!TextUtils.isEmpty(mAVObject.title)){
                binding.titleTv.text = mAVObject.title
            }
            if (mimics[position].video_type == "api") {
                viewModel.loadVideo(mimics[position])
            }else {
                exoplaer(mimics[position].media_url)
            }
            setListData()
            binding.imgLayout.setOnClickListener {
                isMimic = true
                playSound()
            }
            binding.playBtn.setOnClickListener {
                isMimic = true
                playSound()
            }
            binding.goOn.setOnClickListener {
                position++
                setListData()
            }
            binding.mimic.setOnClickListener {
                playAndListen()
            }
            viewModel.mRespoVideo.observe(viewLifecycleOwner, Observer {
                if (it.code == 0 && it.data != null){
                    var url = it.data.getUrl()
                    if(!TextUtils.isEmpty(it.data.getMp3Url())){
                        url = it.data.getMp3Url()
                    }
                    exoplaer(url)
                }else{
                    ToastUtil.diaplayMesShort(context,"error")
                }
            })
        }
    }

    private fun setListData(){
        if (recognizer.isListening){
            recognizer.stopListening()
        }
        PCMAudioPlayer.getInstance().stopPlay()
        if(mimics.size > position){
            if(TextUtils.isEmpty(mimics[position].img)){
                binding.playBtn.visibility = View.VISIBLE
                binding.imgLayout.visibility = View.GONE
            } else {
                binding.playBtn.visibility = View.GONE
                binding.imgLayout.visibility = View.VISIBLE
                binding.imgItem.setImageURI(mimics[position].img)
            }
            if (!TextUtils.isEmpty(mimics[position].transalte)){
                binding.wordDes.text = mimics[position].transalte
            }
            binding.wordName.text = mimics[position].content
            isMimic = true
            playSound()
        }else{
            toNext()
        }
    }

    private fun playAndListen(){
        if (!recognizer.isListening){
            showOrHidePrompt(0)
            binding.scoreTv.text = ""
            isMimic = true
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
        },700)
    }

    private fun mimic() {
        if(context != null){
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
    }

    private fun playUserPcm() {
        PCMAudioPlayer.getInstance().startPlay(userPcmPath)
    }

    private fun exoplaer(url: String) {
        if (mimics.size <= position) {
            toNext()
            return
        }
        if(PCMAudioPlayer.getInstance().isPlaying){
            PCMAudioPlayer.getInstance().stopPlay()
        }
        var mp3Url = url
        binding.playBtn.playAnimation()
        binding.imgPlayBtn.playAnimation()
        var startTime = mimics[position].start_time
        var endTime = mimics[position].end_time
        if(!TextUtils.isEmpty(startTime)){
            startPosition = KStringUtils.getTimeMills(startTime)
            if(!TextUtils.isEmpty(endTime)){
                endPosition = KStringUtils.getTimeMills(endTime)
            }
        }
        if(videoSource == null){
            if(TextUtils.isEmpty(mp3Url)){
                mp3Url = MyPlayer.playUrl + mimics[position].content
            }
            lastUrl = mp3Url
            videoSource = MyPlayer.getMediaSource(mp3Url)
        }
        player.prepare(videoSource!!)
        player.playWhenReady = true
    }

    private fun playSound(){
        if (mimics.size <= position) {
            toNext()
            return
        }
        if (videoSource != null){
            if(PCMAudioPlayer.getInstance().isPlaying){
                PCMAudioPlayer.getInstance().stopPlay()
            }
            binding.playBtn.playAnimation()
            binding.imgPlayBtn.playAnimation()
            var startTime = mimics[position].start_time
            var endTime = mimics[position].end_time
            if(!TextUtils.isEmpty(startTime)){
                startPosition = KStringUtils.getTimeMills(startTime)
                if(!TextUtils.isEmpty(endTime)){
                    endPosition = KStringUtils.getTimeMills(endTime)
                }
            }
            var currentUrl = mimics[position].media_url
            if (lastUrl != currentUrl && !currentUrl.contains("bili")){
                if(TextUtils.isEmpty(currentUrl)){
                    currentUrl = MyPlayer.playUrl + mimics[position].content
                }
                lastUrl = currentUrl
                videoSource = MyPlayer.getMediaSource(currentUrl)
                player.prepare(videoSource!!)
            }
            player.seekTo(startPosition)
            player.playWhenReady = true
        }
    }

    private fun toNext(){
        relase()
        viewModel.sendProgress()
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
        isMimic = false
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
            val text = JsonParser.parseIatResult(results?.resultString)
            sb.append(text)
            if (isLast) {
                finishRecord()
                showResult(sb.toString())
                sb.clear()
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
            if(!TextUtils.isEmpty(text)){
                val bean = ScoreUtil.score(binding.wordName.text.toString(), text.toLowerCase())
                val resultText = ScoreUtil.getTestResult(bean.scoreInt)
                binding.scoreTv.text = resultText
                playUserPcm()
            }else{
                ToastUtil.diaplayMesShort(context,"请跟读！")
            }
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