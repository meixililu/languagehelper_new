package com.messi.languagehelper.coursefragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.iflytek.cloud.*
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.bean.CourseMedias
import com.messi.languagehelper.bean.PVideoResult
import com.messi.languagehelper.databinding.CourseMimicVideoFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel
import java.lang.StringBuilder

class CourseMimicVideoFragment : BaseFragment(), Player.EventListener {

    private val STATE_RESUME_WINDOW = "resumeWindow"
    private val STATE_RESUME_POSITION = "resumePosition"
    private val STATE_PLAYER_FULLSCREEN = "playerFullscreen"

    private var isMimic = false
    lateinit var player: SimpleExoPlayer
    private var mFullScreenButton: FrameLayout? = null
    private var mFullScreenIcon: ImageView? = null
    lateinit var back_btn: LinearLayout
    private var mFullScreenDialog: Dialog? = null
    private var mExoPlayerFullscreen = false
    private var hasInitVideo = false
    private var mResumeWindow = 0
    private var mResumePosition: Long = 0
    private var status = 0
    private var sb = StringBuilder()
    lateinit var recognizer: SpeechRecognizer
    lateinit var mAVObject: CourseData
    lateinit var mimics: List<CourseMedias>
    lateinit var binding: CourseMimicVideoFragmentBinding
    val viewModel: MyCourseViewModel by activityViewModels()
    private var position = 0
    private var startPosition = 0L
    private var endPosition = 0L
    private var userPcmPath = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath) + "wordmimic.pcm"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        recognizer = SpeechRecognizer.createRecognizer(context, null)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(listener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseMimicVideoFragmentBinding.inflate(inflater)
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
        }
        initDatas()
        initFullscreenDialog()
        initFullscreenButton()
        return binding.root
    }

    private fun initDatas() {
        if(viewModel.currentCourse.medias != null
                && viewModel.currentCourse.medias!!.size > position) {
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
                exoplaer(mimics[position].media_url,"")
            }
            setListData()
            binding.goOn.setOnClickListener {
                position++
                setListData()
            }
            binding.mimic.setOnClickListener {
                playAndListen()
            }
            viewModel.mRespoVideo.observe(viewLifecycleOwner, Observer {
                if (it.code == 0){
                    onPVideoApiSuccess(it.data)
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
            if (!TextUtils.isEmpty(mimics[position].transalte)){
                binding.translateTv.text = mimics[position].transalte
            }
            binding.contentTv.text = mimics[position].content
            isMimic = true
            playSound()
        }else{
            next()
        }

    }

    private fun playAndListen(){
        PCMAudioPlayer.getInstance().stopPlay()
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

    private fun finishRecord() {
        if(context != null) {
            binding.recordLayout.visibility = View.GONE
            binding.mimic.text = getString(R.string.start_to_follow)
            binding.goOn.text = getString(R.string.practice_next_level)
        }
    }

    private fun next(){
        release()
        viewModel.sendProgress()
        viewModel.next()
    }

    private fun onPVideoApiSuccess(mResult: PVideoResult) {
        exoplaer(mResult.getUrl(),mResult.getMp3Url())
    }

    private fun exoplaer(mediaUrl: String, voiceUrl: String) {
        if (mimics.size <= position) {
            next()
            return
        }
        if (status == 0) {
            hasInitVideo = true
            status = 1
            var startTime = mimics[position].start_time
            var endTime = mimics[position].end_time
            if(!TextUtils.isEmpty(startTime)){
                startPosition = KStringUtils.getTimeMills(startTime)
                if(!TextUtils.isEmpty(endTime)){
                    endPosition = KStringUtils.getTimeMills(endTime)
                }
            }
            binding.playerView.player = player
            val haveResumePosition = mResumeWindow != C.INDEX_UNSET
            if (haveResumePosition) {
                binding.playerView.player?.seekTo(mResumeWindow, mResumePosition);
            }
            val mediaSource = MyPlayer.getMediaSource(mediaUrl, voiceUrl, mimics[position].media_url)
            player.prepare(mediaSource)
            player.playWhenReady = true
        }
    }

    private fun playSound(){
        if (mimics.size <= position) {
            next()
            return
        }
        if (hasInitVideo){
            var startTime = mimics[position].start_time
            var endTime = mimics[position].end_time
            if(!TextUtils.isEmpty(startTime)){
                startPosition = KStringUtils.getTimeMills(startTime)
                if(!TextUtils.isEmpty(endTime)){
                    endPosition = KStringUtils.getTimeMills(endTime)
                }
            }
            player.seekTo(startPosition)
            player.playWhenReady = true
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        LogUtil.DefalutLog("onPlayerStateChanged:$playbackState-:playWhenReady:$playWhenReady")
    }

    override fun onPlayerError(error: ExoPlaybackException) {}

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow)
        outState.putLong(STATE_RESUME_POSITION, mResumePosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen)
        super.onSaveInstanceState(outState)
    }

    private fun initFullscreenDialog() {
        mFullScreenDialog = object : Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                if (mExoPlayerFullscreen) closeFullscreenDialog()
                super.onBackPressed()
            }
        }
    }

    private fun initFullscreenButton() {
        back_btn = binding.playerView.findViewById(R.id.back_btn)
        back_btn.visibility = View.GONE
        back_btn.setOnClickListener { onBack_btn() }
        mFullScreenIcon = binding.playerView.findViewById(R.id.exo_fullscreen_icon)
        mFullScreenButton = binding.playerView.findViewById(R.id.exo_fullscreen_button)
        mFullScreenButton!!.setOnClickListener {
            if (!mExoPlayerFullscreen) {
                openFullscreenDialog()
            } else {
                closeFullscreenDialog()
            }
        }
    }

    private fun openFullscreenDialog() {
        back_btn.visibility = View.VISIBLE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        (binding.playerView.parent as ViewGroup).removeView(binding.playerView)
        mFullScreenDialog!!.addContentView(binding.playerView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        mFullScreenIcon!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fullscreen_exit_white))
        mExoPlayerFullscreen = true
        mFullScreenDialog!!.show()
    }

    private fun closeFullscreenDialog() {
        back_btn.visibility = View.GONE
        requireActivity().requestedOrientation =ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (binding.playerView.parent as ViewGroup).removeView(binding.playerView)
        binding.videoLy.addView(binding.playerView)
        mExoPlayerFullscreen = false
        mFullScreenDialog!!.dismiss()
        mFullScreenIcon!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fullscreen_white))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        release()
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
    }

    private fun onBack_btn() {
        if (mExoPlayerFullscreen) {
            closeFullscreenDialog()
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

    var listener = object : Player.EventListener{
        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            LogUtil.DefalutLog("---onTracksChanged---")
        }
        override fun onLoadingChanged(isLoading: Boolean) {
            LogUtil.DefalutLog("---onLoadingChanged---")
        }
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            LogUtil.DefalutLog("---onPlayerStateChanged:$playbackState")
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

    var recognizerListener: RecognizerListener = object : RecognizerListener {

        override fun onVolumeChanged(volume: Int, p1: ByteArray?) {
            KViewUtil.showRecordImgAnimation(volume, binding.recordAnimImg)
        }

        override fun onResult(results: RecognizerResult?, isLast: Boolean) {
            LogUtil.DefalutLog("onResult")
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
                val bean = ScoreUtil.score(binding.contentTv.text.toString(), text.toLowerCase())
                val resultText = ScoreUtil.getTestResult(bean.scoreInt)
                binding.scoreTv.text = resultText
                playUserPcm()
            }else{
                ToastUtil.diaplayMesShort(context,"请跟读！")
            }
        }
    }

    private fun playUserPcm() {
        PCMAudioPlayer.getInstance().startPlay(userPcmPath)
    }

    private fun release() {
        PCMAudioPlayer.getInstance().stopPlay()
        status = 1
        endPosition = 0
        isMimic = false
        if (recognizer != null) {
            recognizer.stopListening()
        }
        if (player != null) {
            player.stop()
            player.release()
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog!!.dismiss()
        }
    }
}