package com.messi.languagehelper.coursefragment

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.databinding.CourseComprehensionFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseComprehensionFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: CourseData
    lateinit var binding: CourseComprehensionFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    val viewModel: MyCourseViewModel by activityViewModels()
    var optionBtns = ArrayList<TextView>()
    var userAnswer = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseComprehensionFragmentBinding.inflate(inflater)
        initializeSoundPool()
        initViews()
        init()
        return binding.root
    }

    private fun initViews() {
        binding.checkBtn.setOnClickListener { checkOrNext() }
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
        binding.titleTv.text = mAVObject.title
        TextHandlerUtil.handlerText(context, binding.cQuestion, mAVObject.question)
        if(TextUtils.isEmpty(mAVObject.img)){
            binding.imgItem.visibility = View.GONE
        } else {
            binding.imgItem.visibility = View.VISIBLE
            binding.imgItem.setImageURI(mAVObject.img)
        }
        binding.resultLayout.visibility = View.GONE
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        if (NullUtil.isNotEmpty(mAVObject.options)){
            for (item in mAVObject.options?.shuffled()!!) {
                var textView = KViewUtil.createOptionItem(requireContext(),item.trim())
                textView.setOnClickListener {
                    resetTV(textView)
                    binding.checkBtn.isEnabled = true
                    userAnswer = item
                }
                binding.optionsLayout.addView(textView)
                optionBtns.add(textView)
            }
        }
    }

    private fun resetTV(tv: TextView){
        for (item in optionBtns) {
            KViewUtil.setOptionItemStyle(context, item, R.drawable.border_shadow_gray_oval_selecter)
        }
        KViewUtil.setOptionItemStyle(context, tv, R.drawable.border_shadow_blue_oval_s)
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
        val content = StringUtils.replaceSome(result.toLowerCase())
        if (!TextUtils.isEmpty(userAnswer)) {
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (content == StringUtils.replaceSome(userAnswer.toLowerCase())) {
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
                KViewUtil.setDataOrHide(binding.chineseTv, result + "\n" + mAVObject.translate)
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

    private val result: String
        private get() {
            return if(StringUtils.isEnglish(mAVObject.answer)){
                var sb = StringBuilder()
                var contents = mAVObject.answer.split(" ")
                for (item in contents) {
                    if (!TextUtils.isEmpty(item)) {
                        sb.append(item)
                        sb.append(" ")
                    }
                }
                sb.toString().trim()
            }else {
                mAVObject.answer.trim()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        optionBtns.clear()
    }

}