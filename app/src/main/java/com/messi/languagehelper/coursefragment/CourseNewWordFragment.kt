package com.messi.languagehelper.coursefragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.databinding.CourseNewWordFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseNewWordFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: CourseData
    lateinit var binding: CourseNewWordFragmentBinding
    private var startPosition = 0L
    private var endPosition = 0L
    lateinit var player: SimpleExoPlayer
    val viewModel: MyCourseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseNewWordFragmentBinding.inflate(inflater)
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(listener)
        binding.imgPlayBtn.setOnClickListener { playItem() }
        binding.imgLayout.setOnClickListener { playItem() }
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun init() {
        mAVObject = viewModel.currentCourse
        binding.checkBtn.isEnabled = false
        wordToCharacter()
        playItem()
    }

    private fun wordToCharacter() {
        if (TextUtils.isEmpty(mAVObject.tips)){
            binding.tips.visibility = View.GONE
        }else{
            binding.tips.visibility = View.VISIBLE
            binding.tips.text = mAVObject.tips
        }
        if (!TextUtils.isEmpty(mAVObject.title)){
            binding.titleTv.text = mAVObject.title
        }
        if (!TextUtils.isEmpty(mAVObject.question)){
            binding.questionTv.text = mAVObject.question
        }
        binding.imgItem.setImageURI(mAVObject.img)
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        Handler().postDelayed({
            binding.checkBtn.isEnabled = true
        },600)
    }

    private fun checkOrNext() {
        mAVObject.user_result = true
        viewModel.sendProgress()
        if(::player.isInitialized) {
            player.stop()
        }
        viewModel.next()
    }

    fun playItem() {
        if (::mAVObject.isInitialized && ::player.isInitialized) {
            binding.imgPlayBtn.playAnimation()
            var mp3Url = mAVObject.media_url
            var startTime = mAVObject.start_time
            var endTime = mAVObject.end_time
            if(TextUtils.isEmpty(mp3Url)){
                mp3Url = MyPlayer.playUrl + mAVObject.title
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
                    binding.imgPlayBtn.cancelAnimation()
                    player.playWhenReady = false
                }else{
                    stopAtEndPosition()
                }
            }, 10)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::player.isInitialized){
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