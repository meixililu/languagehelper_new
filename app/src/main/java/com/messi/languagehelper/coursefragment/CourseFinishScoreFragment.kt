package com.messi.languagehelper.coursefragment

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
import com.messi.languagehelper.databinding.CourseFinishFragmentBinding
import com.messi.languagehelper.databinding.CourseFinishScoreFragmentBinding
import com.messi.languagehelper.databinding.CourseWordFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseFinishScoreFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var binding: CourseFinishScoreFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    val viewModel: MyCourseViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseFinishScoreFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        return binding.root
    }

    private fun initViews() {
        if (viewModel.show_check_in()){
            binding.checkBtn.text = "Next"
        }
        binding.checkBtn.setOnClickListener { checkOrNext() }
        setScore()
        Handler().postDelayed({
            playSoundPool()
            binding.checkBtn.isEnabled = true
        },600)
    }

    private fun setScore(){
        try {
            binding.scoreTv.text = "恭喜你"
            binding.levelTv.text = "获得 " + viewModel.courseList.size + " 点经验" + "\n" +
                    "经验值达到了 " + viewModel.userProfile!!.course_score + " ！"
        } catch (e: Exception) {
        }
    }

    private fun checkOrNext() {
        if (binding.checkBtn.text.toString() == "Finish") {
            requireActivity().finish()
        } else if (binding.checkBtn.text.toString() == "Next") {
            viewModel.toCheckIn()
        }
    }

    private fun initializeSoundPool() {
        ourSounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else {
            SoundPool(5, AudioManager.STREAM_MUSIC, 1)
        }
        answerRight = ourSounds.load(context, R.raw.sound_correct, 1)
    }

    private fun playSoundPool() {
        ourSounds.play(answerRight, 1f, 1f, 1, 0, 1f)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ourSounds.stop(answerRight)
    }
}