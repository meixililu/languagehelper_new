package com.messi.languagehelper.coursefragment

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
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
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.bean.CourseMedias
import com.messi.languagehelper.bean.PVideoResult
import com.messi.languagehelper.databinding.CourseVideoFragmentBinding
import com.messi.languagehelper.util.KStringUtils
import com.messi.languagehelper.util.LogUtil
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.util.ToastUtil
import com.messi.languagehelper.viewmodels.MyCourseViewModel

class CourseVideoFragment : BaseFragment(), Player.EventListener {

    private val STATE_RESUME_WINDOW = "resumeWindow"
    private val STATE_RESUME_POSITION = "resumePosition"
    private val STATE_PLAYER_FULLSCREEN = "playerFullscreen"

    lateinit var player: SimpleExoPlayer
    private var mFullScreenButton: FrameLayout? = null
    private var mFullScreenIcon: ImageView? = null
    lateinit var back_btn: LinearLayout
    private var mFullScreenDialog: Dialog? = null
    private var mExoPlayerFullscreen = false
    private var mResumeWindow = 0
    private var mResumePosition: Long = 0
    private var status = 0

    lateinit var mAVObject: CourseData
    lateinit var binding: CourseVideoFragmentBinding
    val viewModel: MyCourseViewModel by activityViewModels()
    lateinit var mimics: List<CourseMedias>
    private var position = 0
    private var startPosition = 0L
    private var endPosition = 0L


    override fun onAttach(context: Context) {
        super.onAttach(context)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseVideoFragmentBinding.inflate(inflater)
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
            if (mimics[position].video_type == "api") {
                viewModel.loadVideo(mimics[position])
            }else {
                exoplaer(mimics[position].media_url,"")
            }
            if (TextUtils.isEmpty(mAVObject.tips)){
                binding.tips.visibility = View.GONE
            }else{
                binding.tips.visibility = View.VISIBLE
                binding.tips.text = mAVObject.tips
            }
            if (!TextUtils.isEmpty(mAVObject.title)){
                binding.titleTv.text = mAVObject.title
            }
            binding.checkBtn.setOnClickListener { toNext() }
            viewModel.mRespoVideo.observe(viewLifecycleOwner, Observer {
                if (it.code == 0){
                    onPVideoApiSuccess(it.data)
                }else{
                    ToastUtil.diaplayMesShort(context,"error")
                }
            })
        }
    }

    private fun toNext(){
        release()
        viewModel.next()
    }

    private fun onPVideoApiSuccess(mResult: PVideoResult) {
        exoplaer(mResult.getUrl(),mResult.getMp3Url())
    }

    private fun exoplaer(mediaUrl: String, voiceUrl: String) {
        if (mimics.size <= position) {
            toNext()
            return
        }
        if (status == 0) {
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
            player.prepare(mediaSource!!)
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

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
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
            Player.STATE_ENDED -> {}
        }
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
        back_btn = binding.playerView.findViewById<LinearLayout>(R.id.back_btn)
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

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player.playWhenReady = false
        }
    }

    private fun onBack_btn() {
        if (mExoPlayerFullscreen) {
            closeFullscreenDialog()
        }
    }

    private fun release() {
        status = 1
        if (player != null) {
            player.playWhenReady = false
            player.release()
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog!!.dismiss()
        }
    }
}