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
import com.messi.languagehelper.databinding.CourseUnknowTypeFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseUnknowTypeFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: CourseData
    lateinit var binding: CourseUnknowTypeFragmentBinding
    val viewModel: MyCourseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseUnknowTypeFragmentBinding.inflate(inflater)
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun init() {
        mAVObject = viewModel.currentCourse
        binding.checkBtn.isEnabled = false
        wordToCharacter()
    }

    private fun wordToCharacter() {
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        Handler().postDelayed({
            binding.checkBtn.isEnabled = true
        },600)
    }

    private fun checkOrNext() {
        mAVObject.user_result = true
        viewModel.sendProgress()
        viewModel.next()
    }

}