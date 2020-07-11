package com.messi.languagehelper

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.CourseListFragmentBinding
import com.messi.languagehelper.databinding.WordStudyDancixuanyiBinding
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.util.NumberUtil
import com.messi.languagehelper.util.Setings
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class CourseListsFragment : BaseFragment() {

    lateinit var binding: CourseListFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseListFragmentBinding.inflate(inflater)
        init()
        return binding.root
    }

    private fun  init(){
        binding.jichu.setOnClickListener {
            toActivity(DailyEnglishActivity::class.java, null)
        }
    }

}