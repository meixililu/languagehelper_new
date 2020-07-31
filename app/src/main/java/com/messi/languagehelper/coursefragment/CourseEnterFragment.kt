package com.messi.languagehelper.coursefragment

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.databinding.CourseEnterFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseEnterFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: CourseData
    lateinit var binding: CourseEnterFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    val viewModel: MyCourseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseEnterFragmentBinding.inflate(inflater)
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
        if (TextUtils.isEmpty(mAVObject.tips)){
            binding.tips.visibility = View.GONE
        }else{
            binding.tips.visibility = View.VISIBLE
            binding.tips.text = mAVObject.tips
        }
        if(TextUtils.isEmpty(mAVObject.img)){
            binding.imgItem.visibility = View.GONE
        } else {
            binding.imgItem.visibility = View.VISIBLE
            binding.imgItem.setImageURI(mAVObject.img)
        }
        binding.titleTv.text = mAVObject.title
        binding.cQuestion.text = mAVObject.question
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
        var content = StringUtils.replaceSome(mAVObject.answer.toLowerCase().trim())
        var userInput = StringUtils.replaceSome(binding.editText.text.toString().toLowerCase().trim())
        if (!TextUtils.isEmpty(userInput)) {
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (content == userInput) {
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
                KViewUtil.setDataOrHide(binding.chineseTv, mAVObject.answer)
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