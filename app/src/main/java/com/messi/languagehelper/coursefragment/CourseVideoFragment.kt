package com.messi.languagehelper.coursefragment

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.ListenCourseData
import com.messi.languagehelper.bean.PVideoResult
import com.messi.languagehelper.databinding.CourseVideoFragmentBinding
import com.messi.languagehelper.util.LogUtil
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.util.Setings
import com.messi.languagehelper.util.ToastUtil
import com.messi.languagehelper.viewmodels.MyCourseViewModel

class CourseVideoFragment : BaseFragment(), Player.EventListener {

    private val STATE_RESUME_WINDOW = "resumeWindow"
    private val STATE_RESUME_POSITION = "resumePosition"
    private val STATE_PLAYER_FULLSCREEN = "playerFullscreen"

    private var player: SimpleExoPlayer? = null
    private var mFullScreenButton: FrameLayout? = null
    private var mFullScreenIcon: ImageView? = null
    lateinit var back_btn: LinearLayout
    private var mFullScreenDialog: Dialog? = null
    private var mExoPlayerFullscreen = false
    private var mResumeWindow = 0
    private var mResumePosition: Long = 0
    private var status = 0

    lateinit var mAVObject: ListenCourseData
    lateinit var binding: CourseVideoFragmentBinding
    lateinit var viewModel: MyCourseViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MyCourseViewModel::class.java)
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
        mAVObject = viewModel.currentCourse
        if (mAVObject.video_type == "api") {
            viewModel.loadVideo()
        }else {
            exoplaer(mAVObject.video_url,"")
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
        binding.checkBtn.setOnClickListener { viewModel.next() }
        viewModel.mRespoVideo.observe(viewLifecycleOwner, Observer {
            if (it.code == 0){
                onPVideoApiSuccess(it.data)
            }else{
                ToastUtil.diaplayMesShort(context,"error")
            }
        })
    }

    private fun onPVideoApiSuccess(mResult: PVideoResult) {
        exoplaer(mResult.getUrl(),mResult.getMp3Url())
    }

    private fun exoplaer(mediaUrl: String, voiceUrl: String) {
        LogUtil.DefalutLog("exoplaer---media_url:$mediaUrl")
        if (status == 0) {
            status = 1
            player = SimpleExoPlayer.Builder(requireContext()).build()
            binding.playerView.player = player;
            val haveResumePosition = mResumeWindow != C.INDEX_UNSET
            if (haveResumePosition) {
                binding.playerView.player?.seekTo(mResumeWindow, mResumePosition);
            }
            val mediaSource = MyPlayer.getMediaSource(mediaUrl, voiceUrl, mAVObject.video_url)
            player!!.addListener(this)
            player!!.prepare(mediaSource!!)
            player!!.playWhenReady = true
            LogUtil.DefalutLog("ACTION_setPlayWhenReady")
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
        status = 1
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player!!.playWhenReady = false
        }
    }

    private fun onBack_btn() {
        if (mExoPlayerFullscreen) {
            closeFullscreenDialog()
        }
    }

    fun releasePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.release()
            player = null
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog!!.dismiss()
        }
    }
}