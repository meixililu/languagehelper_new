package com.messi.languagehelper

import android.content.Context
import android.content.Intent
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.WordDetailListItem
import com.messi.languagehelper.databinding.WordDetailActivityBinding
import com.messi.languagehelper.databinding.WordStudyRecognizeFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.WordStudyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class WordRecognizeFragment : BaseFragment() {

    lateinit var binding: WordStudyRecognizeFragmentBinding
    private lateinit var viewModel: WordStudyViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = WordStudyRecognizeFragmentBinding.inflate(inflater)
        setData()
        return binding.root
    }

    fun setData() {
        var item = viewModel.currentItem
        binding.wordName.text = item.name
        binding.wordDes.text = ""
        binding.wordSymbol.text = item.symbol
        if (TextUtils.isEmpty(item.symbol)) {
            binding.wordSymbol.visibility = View.GONE
        } else {
            binding.wordSymbol.visibility = View.VISIBLE
        }
        binding.recognizeKnow.isEnabled = true
        binding.recognizeUnknow.text = getString(R.string.recognize_unknow)
        binding.recognizeKnow.setOnClickListener {
            viewModel.refreshData("test")
        }
        binding.recognizeUnknow.setOnClickListener {
            viewModel.refreshData("study")
        }
        binding.rootView.setOnClickListener {
            playMp3(item.name, item.sound)
        }
        lifecycleScope.launch(){
            delay(300)
            playMp3(item.name, item.sound)
        }
    }

    fun playMp3(content: String, url: String) {
        MyPlayer.getInstance(context).start(content, url)
    }

}