package com.messi.languagehelper.coursefragment

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.google.android.flexbox.FlexboxLayout
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.CourseData
import com.messi.languagehelper.databinding.CourseWordLlkFragmentBinding
import com.messi.languagehelper.util.*
import com.messi.languagehelper.viewmodels.MyCourseViewModel


class CourseWordLLKFragment : BaseFragment() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: CourseData
    lateinit var binding: CourseWordLlkFragmentBinding
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    val viewModel: MyCourseViewModel by activityViewModels()
    val map = HashMap<String,String>()
    val textList = ArrayList<String>()
    val tvList = ArrayList<TextView>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseWordLlkFragmentBinding.inflate(inflater)
        initializeSoundPool()
        init()
        return binding.root
    }

    private fun init() {
        binding.checkBtn.setOnClickListener { checkOrNext() }
        mAVObject = viewModel.currentCourse
        binding.checkBtn.isEnabled = false
        binding.checkBtn.text = "Check"
        wordToCharacter()
    }

    private fun wordToCharacter() {
        if (TextUtils.isEmpty(mAVObject.tips)){
            binding.tips.visibility = View.GONE
        }else{
            binding.tips.visibility = View.VISIBLE
            binding.tips.text = mAVObject.tips
        }
        binding.titleTv.text = mAVObject.title
        binding.questionTv.text = mAVObject.question
        binding.resultLayout.visibility = View.GONE
        binding.autoWrapOptions.removeAllViews()
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        if(NullUtil.isNotEmpty(mAVObject.words)){
            for (item in mAVObject.words?.shuffled()!!){
                map[item.name] = item.des
                map[item.des] = item.name
                textList.add(item.name)
                textList.add(item.des)
            }
            for(item in textList.shuffled()){
                getItemView(item)
            }
        }
    }

    fun getItemView(text: String){
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.textSize = 18f
        textView.tag = 0
        textView.setBackgroundResource(R.drawable.bg_btn_course_item)
        textView.setTextColor(requireActivity().resources.getColor(R.color.text_black))
        val paddingbg = ScreenUtil.dip2px(context, 12f)
        val paddingLRbg = ScreenUtil.dip2px(context, 12f)
        ViewCompat.setPaddingRelative(textView, paddingLRbg, paddingbg, paddingLRbg, paddingbg)
        val layoutParamsbg = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = ScreenUtil.dip2px(context, 20f)
        val marginTop = ScreenUtil.dip2px(context, 20f)
        layoutParamsbg.setMargins(0, 0, margin, marginTop)
        textView.layoutParams = layoutParamsbg
        binding.autoWrapOptions.addView(textView)

        textView.setOnClickListener {
            LogUtil.DefalutLog(text)
            textView.setBackgroundResource(R.drawable.bg_btn_course_item_s)
            tvList.add(textView)
            if(tvList.size == 2){
                checkList()
            }
        }
    }

    private fun checkList(){
        var isCorrect = false
        var text0 = tvList[0].text.toString()
        var text1 = tvList[1].text.toString()
        if (text1 == map[text0]){
            isCorrect = true
            if(StringUtils.isAllEnglish(text1)){
                MyPlayer.getInstance(context).start(text1)
            }else {
                MyPlayer.getInstance(context).start(text0)
            }
        }
        Handler().postDelayed({
            for (item in tvList){
                if(isCorrect){
                    item.setBackgroundResource(R.color.none)
                    item.setTextColor(requireActivity().resources.getColor(R.color.none))
                    item.isClickable = false
                    item.tag = 1
                    isFinish()
                }else{
                    item.setBackgroundResource(R.drawable.bg_btn_course_item)
                }
            }
            tvList.clear()
        },300)
    }

    fun isFinish(){
        var showNextBtn = true
        for (index in 0 until binding.autoWrapOptions.childCount){
            var tag = (binding.autoWrapOptions[index] as TextView).tag
            if (tag == 0){
                showNextBtn = false
                break
            }
        }
        if (showNextBtn){
            checkOrNext()
        }
    }

    private fun checkOrNext() {
        if (binding.checkBtn.text.toString() == "Check") {
            check()
        } else if (binding.checkBtn.text.toString() == "Next") {
            viewModel.next()
        }
    }

    private fun check() {
        viewModel.sendProgress()
        binding.checkBtn.text = "Next"
        binding.resultLayout.visibility = View.VISIBLE
        binding.checkBtn.isEnabled = true
        mAVObject.user_result = true
        playSoundPool(mAVObject.user_result)
        binding.checkSuccess.setAnimation("check_success.json")
        binding.checkSuccess.speed = 2F
        binding.checkSuccess.playAnimation()
        binding.resultTv.text = "真棒"
        binding.chineseTv.text = ""
        binding.chineseTv.visibility = View.GONE
        binding.resultLayout.setBackgroundResource(R.color.correct_bg)
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        binding.chineseTv.setTextColor(resources.getColor(R.color.correct_text))
        binding.resultTv.setTextColor(resources.getColor(R.color.correct_text))
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

    override fun onDestroyView() {
        super.onDestroyView()
        tvList.clear()
        textList.clear()
        map.clear()
    }


}