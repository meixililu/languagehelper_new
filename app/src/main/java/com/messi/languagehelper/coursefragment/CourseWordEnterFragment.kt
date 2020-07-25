package com.messi.languagehelper.coursefragment

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.messi.languagehelper.databinding.CourseTranslateEnterFragmentBinding
import com.messi.languagehelper.databinding.CourseTranslateFragmentBinding
import com.messi.languagehelper.databinding.CourseWordEnterFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseWordEnterFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: ListenCourseData
    lateinit var binding: CourseWordEnterFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    lateinit var viewModel: MyCourseViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MyCourseViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseWordEnterFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        binding.checkBtn.setOnClickListener { checkOrNext() }
        binding.editText.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text != null) {
                    binding.checkBtn.isEnabled = text.isNotEmpty()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun init() {
        if(viewModel.currentCourse != null){
            mAVObject = viewModel.currentCourse
            binding.checkBtn.isEnabled = false
            binding.checkBtn.text = "Check"
            wordToCharacter()
        }
    }

    private fun wordToCharacter() {
        if(TextUtils.isEmpty(mAVObject.img)){
            binding.imgItem.visibility = View.GONE
        } else {
            binding.imgItem.visibility = View.VISIBLE
            binding.imgItem.setImageURI(mAVObject.img)
        }
        if (!TextUtils.isEmpty(mAVObject.title)){
            binding.titleTv.text = mAVObject.title
        }
        binding.questionTv.text = mAVObject.question
        binding.resultLayout.visibility = View.GONE
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
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
        val content = mAVObject.answer.trim()
        val userInput = binding.editText.text.toString().toLowerCase().trim()
        if (!TextUtils.isEmpty(userInput)) {
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (content == userInput) {
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

}