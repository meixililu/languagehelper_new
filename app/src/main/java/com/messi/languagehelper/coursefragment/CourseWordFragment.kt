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
import android.widget.RelativeLayout
import android.widget.TextView
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
import com.messi.languagehelper.databinding.CourseChoiceFragmentBinding
import com.messi.languagehelper.databinding.CourseWordFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseWordFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: ListenCourseData
    lateinit var binding: CourseWordFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    private var startPosition = 0L
    private var endPosition = 0L
    lateinit var player: SimpleExoPlayer
    lateinit var viewModel: MyCourseViewModel
    var optionBtns = ArrayList<TextView>()
    var userAnswer = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MyCourseViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseWordFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        IPlayerUtil.pauseAudioPlayer(context)
        player = SimpleExoPlayer.Builder(context!!).build()
        player.addListener(listener)
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun init() {
        mAVObject = viewModel.currentCourse
        if(mAVObject != null){
            binding.checkBtn.isEnabled = false
            binding.checkBtn.text = "Check"
            wordToCharacter()
        }
    }

    private fun wordToCharacter() {
        binding.resultLayout.visibility = View.GONE
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        binding.titleTv.text = mAVObject.title
        for ((index,item) in mAVObject.options.shuffled().withIndex()){
            when (index) {
                0 -> {
                    binding.itemTv.text = item
                    binding.itemImg.setImageURI(getImgUrl(item))
                    binding.item.setOnClickListener {
                        userAnswer = item
                        resetItem(binding.item)
                    }
                }
                1 -> {
                    binding.itemTv1.text = item
                    binding.itemImg1.setImageURI(getImgUrl(item))
                    binding.item1.setOnClickListener {
                        userAnswer = item
                        resetItem(binding.item1)
                    }
                }
                2 -> {
                    binding.itemTv2.text = item
                    binding.itemImg2.setImageURI(getImgUrl(item))
                    binding.item2.setOnClickListener {
                        userAnswer = item
                        resetItem(binding.item2)
                    }
                }
                3 -> {
                    binding.itemTv3.text = item
                    binding.itemImg3.setImageURI(getImgUrl(item))
                    binding.item3.setOnClickListener {
                        userAnswer = item
                        resetItem(binding.item3)
                    }
                }
            }
        }
    }

    fun getImgUrl(name:String): String{
        return Setings.IMGRoot + "father" + ".png"
    }

    private fun resetItem(view: RelativeLayout){
        binding.checkBtn.isEnabled = true
        binding.item.setBackgroundResource(R.drawable.border_shadow_gray_selecter)
        binding.item1.setBackgroundResource(R.drawable.border_shadow_gray_selecter)
        binding.item2.setBackgroundResource(R.drawable.border_shadow_gray_selecter)
        binding.item3.setBackgroundResource(R.drawable.border_shadow_gray_selecter)
        view.setBackgroundResource(R.drawable.border_shadow_blue_item)
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
        hideKeyBoard()
        if (!TextUtils.isEmpty(userAnswer)) {
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (mAVObject.answer.trim() == userAnswer) {
                mAVObject.user_result = true
                playSoundPool(mAVObject.user_result)
                binding.checkSuccess.setAnimation("check_success.json")
                binding.checkSuccess.speed = 2F
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
                binding.chineseTv.text = mAVObject.answer
                binding.resultLayout.setBackgroundResource(R.color.wrong_bg)
                binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_red_selecter)
                binding.chineseTv.setTextColor(resources.getColor(R.color.wrong_text))
                binding.resultTv.setTextColor(resources.getColor(R.color.wrong_text))
            }
        }else{
            ToastUtil.diaplayMesShort(context,getString(R.string.choice_hint))
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

    fun playItem() {
        if (!TextUtils.isEmpty(mAVObject.play_content)) {
            var mp3Url = mAVObject.mp3_url
            var startTime = mAVObject.start_time
            var endTime = mAVObject.end_time
            if(TextUtils.isEmpty(mp3Url)){
                mp3Url = MyPlayer.playUrl + mAVObject.play_content
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