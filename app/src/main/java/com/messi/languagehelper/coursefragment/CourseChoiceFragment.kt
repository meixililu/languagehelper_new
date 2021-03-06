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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.databinding.CourseChoiceFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel
import java.lang.Exception


class CourseChoiceFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: CourseData
    lateinit var binding: CourseChoiceFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    private var startPosition = 0L
    private var endPosition = 0L
    lateinit var player: SimpleExoPlayer
    val viewModel: MyCourseViewModel by activityViewModels()
    var optionBtns = ArrayList<TextView>()
    var userAnswer = ""
    var isClickable = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseChoiceFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(listener)
        binding.playBtn.setOnClickListener { playItem() }
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun init() {
        if(viewModel.currentCourse != null){
            isClickable = true
            mAVObject = viewModel.currentCourse
            binding.checkBtn.isEnabled = false
            binding.checkBtn.text = "Check"
            wordToCharacter()
        }
    }

    private fun wordToCharacter() {
        if (TextUtils.isEmpty(mAVObject.tips)){
            binding.tips.visibility = View.GONE
        }else{
            binding.tips.visibility = View.VISIBLE
            binding.tips.text = mAVObject.tips
        }
        binding.titleTv.text = mAVObject.title

        TextHandlerUtil.handlerText(context, binding.cQuestion, mAVObject.question)
        if (StringUtils.isEnglish(mAVObject.question) && !mAVObject.question.contains("_")){
            if(viewModel != null && viewModel.getType().contains("comprehension")){
                binding.playBtn.visibility = View.GONE
            }else{
                binding.playBtn.visibility = View.VISIBLE
                playItem()
            }
        } else {
            binding.playBtn.visibility = View.GONE
        }
        if(TextUtils.isEmpty(mAVObject.img)){
            binding.imgItem.visibility = View.GONE
        } else {
            binding.imgItem.visibility = View.VISIBLE
            binding.imgItem.setImageURI(mAVObject.img)
        }
        binding.resultLayout.visibility = View.GONE
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        if (NullUtil.isNotEmpty(mAVObject.options)){
            for (item in mAVObject.options?.shuffled()!!) {
                var textView = KViewUtil.createOptionItem(requireContext(),item.trim())
                textView.setOnClickListener {
                    if(isClickable){
                        resetTV(textView)
                        binding.checkBtn.isEnabled = true
                        userAnswer = item
                    }
                }
                binding.optionsLayout.addView(textView)
                optionBtns.add(textView)
            }
        }
    }

    private fun resetTV(tv: TextView){
        for (item in optionBtns) {
            KViewUtil.setOptionItemStyle(context, item, R.drawable.border_shadow_gray_oval_selecter)
        }
        KViewUtil.setOptionItemStyle(context, tv, R.drawable.border_shadow_blue_oval_s)
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
        isClickable = false
        val content = StringUtils.replaceSome(result.toLowerCase())
        if (!TextUtils.isEmpty(userAnswer)) {
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (content == StringUtils.replaceSome(userAnswer.toLowerCase())) {
                mAVObject.user_result = true
                viewModel.sendProgress()
                playSoundPool(mAVObject.user_result)
                binding.checkSuccess.setAnimation("check_success.json")
                binding.checkSuccess.speed = 2F
                binding.checkSuccess.playAnimation()
                binding.resultTv.text = "正确"
                KViewUtil.setDataOrHide(binding.chineseTv, mAVObject.translate)
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
                KViewUtil.setDataOrHide(binding.chineseTv, result + "\n" + mAVObject.translate)
                binding.resultLayout.setBackgroundResource(R.color.wrong_bg)
                binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_red_selecter)
                binding.chineseTv.setTextColor(resources.getColor(R.color.wrong_text))
                binding.resultTv.setTextColor(resources.getColor(R.color.wrong_text))
            }
        }else{
            ToastUtil.diaplayMesShort(context,getString(R.string.input_translate_hint))
        }
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

    private val result: String
        private get() {
            return if(StringUtils.isEnglish(mAVObject.answer)){
                var sb = StringBuilder()
                var contents = mAVObject.answer.split(" ")
                for (item in contents) {
                    if (!TextUtils.isEmpty(item)) {
                        sb.append(item)
                        sb.append(" ")
                    }
                }
                sb.toString().trim()
            }else {
                mAVObject.answer.trim()
            }
        }

    fun playItem() {
        binding.playBtn.playAnimation()
        var mp3Url = mAVObject.media_url
        var startTime = mAVObject.start_time
        var endTime = mAVObject.end_time
        if(TextUtils.isEmpty(mp3Url)){
            mp3Url = MyPlayer.playUrl + mAVObject.question
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

    private fun stopAtEndPosition() {
        if(endPosition > 0){
            Handler().postDelayed({
                if (player.currentPosition > endPosition) {
                    binding.playBtn.cancelAnimation()
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
        optionBtns.clear()
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