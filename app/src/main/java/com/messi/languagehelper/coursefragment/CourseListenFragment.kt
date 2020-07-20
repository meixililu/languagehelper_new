package com.messi.languagehelper.coursefragment

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.ListenCourseData
import com.messi.languagehelper.databinding.ListenCourseFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseListenFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: ListenCourseData
    lateinit var binding: ListenCourseFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    private var startPosition = 0L
    private var endPosition = 0L
    lateinit var player: SimpleExoPlayer
    lateinit var viewModel: MyCourseViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MyCourseViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = ListenCourseFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        IPlayerUtil.pauseAudioPlayer(context)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(listener)
        binding.playBtn.setOnClickListener { playItem() }
        binding.imgPlayBtn.setOnClickListener { playItem() }
        binding.imgLayout.setOnClickListener { playItem() }
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun init() {
        mAVObject = viewModel.currentCourse
        if(mAVObject != null){
            binding.checkBtn.isEnabled = false
            binding.checkBtn.text = "Check"
            wordToCharacter()
            playItem()
        }
    }

    private fun wordToCharacter() {
        if (TextUtils.isEmpty(mAVObject.tips)){
            binding.tips.visibility = View.GONE
        }else{
            binding.tips.visibility = View.VISIBLE
            binding.tips.text = mAVObject.tips
        }
        if(TextUtils.isEmpty(mAVObject.img)){
            binding.noimgLayout.visibility = View.VISIBLE
            binding.imgLayout.visibility = View.GONE
        } else {
            binding.noimgLayout.visibility = View.GONE
            binding.imgLayout.visibility = View.VISIBLE
            binding.imgItem.setImageURI(mAVObject.img)
        }
        if (!TextUtils.isEmpty(mAVObject.title)){
            binding.titleTv.text = mAVObject.title
        }
        binding.resultLayout.visibility = View.GONE
        binding.autoWrapOptions.removeAllViews()
        binding.autoWrapResult.removeAllViews()
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        val contents = mAVObject.content.split(" ")
        var index = 0
        for (item in contents.shuffled()) {
            if (TextUtils.isEmpty(item) || item == " "){
                continue
            }else{
                KViewUtil.createNewFlexItemTextView(requireContext(),
                        binding.autoWrapOptions,
                        binding.autoWrapResult,
                        binding.checkBtn,
                        item.trim(),
                        index)
                index++
            }
        }
    }

    private fun checkOrNext() {
        if (binding.checkBtn.text.toString() == "Check") {
            binding.checkBtn.text = "Next"
            check()
        } else if (binding.checkBtn.text.toString() == "Next") {
            viewModel.next()
        }
    }

    private fun check() {
        val content = englishContent
        if (binding.autoWrapResult.childCount > 0) {
            var selectedStr = getSelectedResult()
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (content == selectedStr) {
                mAVObject.user_result = true
                playSoundPool(mAVObject.user_result)
                binding.checkSuccess.setAnimation("check_success.json")
                binding.checkSuccess.progress
                binding.checkSuccess.playAnimation()
                binding.resultTv.text = "正确"
                binding.chineseTv.text = mAVObject.transalte
                binding.resultLayout.setBackgroundResource(R.color.correct_bg)
                binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
                binding.chineseTv.setTextColor(resources.getColor(R.color.correct_text))
                binding.resultTv.setTextColor(resources.getColor(R.color.correct_text))
            }else{
                mAVObject.user_result = false
                playSoundPool(mAVObject.user_result)
                binding.checkSuccess.setAnimation("cross.json")
                binding.checkSuccess.playAnimation()
                binding.resultTv.text = "正确答案"
                binding.chineseTv.text = englishContent + "\n" + mAVObject.transalte
                binding.resultLayout.setBackgroundResource(R.color.wrong_bg)
                binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_red_selecter)
                binding.chineseTv.setTextColor(resources.getColor(R.color.wrong_text))
                binding.resultTv.setTextColor(resources.getColor(R.color.wrong_text))
            }
        }else{
            ToastUtil.diaplayMesShort(context,"请先做题，做完再Check！")
        }
    }

    private fun getSelectedResult(): String{
        var sb = StringBuilder()
        for (index in 0 until binding.autoWrapResult.childCount){
            var item = ((binding.autoWrapResult[index] as ViewGroup)[0] as TextView).text.toString()
            if (!TextUtils.isEmpty(item)){
                sb.append(item)
                if (index != binding.autoWrapResult.childCount-1){
                    sb.append(" ")
                }
            }
        }
        return sb.toString()
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
        answerRight = ourSounds.load(context, R.raw.right_answer, 1)
        answerWrong = ourSounds.load(context, R.raw.wrong_answer, 1)
    }

    private fun playSoundPool(isRight: Boolean) {
        if (isRight) {
            ourSounds.play(answerRight, 1f, 1f, 1, 0, 1f)
        } else {
            ourSounds.play(answerWrong, 1f, 1f, 1, 0, 1f)
        }
    }

    private val englishContent: String
        private get() {
            var sb = StringBuilder()
            var contents = mAVObject.answer.split(" ")
            for (item in contents) {
                if (!TextUtils.isEmpty(item)) {
                    sb.append(item)
                    sb.append(" ")
                }
            }
            return sb.toString().trim()
        }

    fun playItem() {
        if (mAVObject != null) {
            binding.playBtn.playAnimation()
            binding.imgPlayBtn.playAnimation()
            var mp3Url = mAVObject.mp3_url
            var startTime = mAVObject.start_time
            var endTime = mAVObject.end_time
            if(TextUtils.isEmpty(mp3Url)){
                mp3Url = MyPlayer.playUrl + mAVObject.answer
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

    val isPlaying: Boolean
        get() = player.playbackState == Player.STATE_READY && player.playWhenReady

    override fun onDestroy() {
        super.onDestroy()
        if(player != null){
            player.stop()
            player.release()
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
                    }
                }
                Player.STATE_ENDED -> {
                    binding.playBtn.cancelAnimation()
                    binding.imgPlayBtn.cancelAnimation()
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