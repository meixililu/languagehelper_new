package com.messi.languagehelper

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import cn.leancloud.AVException
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.callback.FindCallback
import cn.leancloud.convertor.ObserverBuilder
import com.messi.languagehelper.databinding.DailyEnglishActivityBinding
import com.messi.languagehelper.util.*
import java.lang.StringBuilder
import kotlin.math.roundToInt

class DailyEnglishActivity : BaseActivity() {

    private val maxRandom = 90000
    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: AVObject
    lateinit var binding: DailyEnglishActivityBinding
    private var selectedNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DailyEnglishActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
        queryData()
    }

    private fun initViews() {
        setActionBarTitle(this.resources.getString(R.string.title_daily_english))
        IPlayerUtil.pauseAudioPlayer(this)
        mSharedPreferences = Setings.getSharedPreferences(this)
        binding.playBtn.setOnClickListener { playItem() }
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun checkOrNext() {
        if (binding.checkBtn.text.toString() == "Check") {
            binding.checkBtn.text = "Next"
            check()
        } else if (binding.checkBtn.text.toString() == "Next") {
            queryData()
        }
    }

    private fun queryData() {
        showProgressbar()
        val query = AVQuery<AVObject>(AVOUtil.EvaluationDetail.EvaluationDetail)
        query.whereEqualTo(AVOUtil.EvaluationDetail.EDIsValid, "1")
        query.limit(1)
        query.skip((Math.random() * maxRandom).roundToInt())
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver<AVObject>(object : FindCallback<AVObject>() {
            override fun done(avObjects: List<AVObject>, avException: AVException?) {
                hideProgressbar()
                if (avObjects != null && avObjects.size > 0) {
                    mAVObject = avObjects[0]
                    init(true)
                }
            }
        }))
    }

    private fun init(isPlay: Boolean) {
        selectedNum = 0
        setWordName()
        wordToCharacter()
        if (isPlay) {
            playItem()
        }
    }

    private fun setWordName() {
        binding.checkBtn.text = "Check"
        binding.chineseTv.text = chineseContent
    }

    private fun check() {
        val content = englishContent
        if (binding.autoWrapResult.childCount > 0) {
            var selectedStr = getSelectedResult()
            binding.checkBtn.text = "Next"
            if (content == selectedStr) {
                ToastUtil.diaplayMesShort(this,"正确")
            }else{
                ToastUtil.diaplayMesShort(this,"错了")
            }
        }else{
            ToastUtil.diaplayMesShort(this,"请先做题，做完再Check！")
        }
    }

    private fun getSelectedResult(): String{
        var sb = StringBuilder()
        for (index in 0 until binding.autoWrapResult.childCount){
            var item = ((binding.autoWrapResult[index] as ViewGroup)[0] as TextView).text.toString()
            if (!TextUtils.isEmpty(item)){
                sb.append(item)
                if (index != binding.autoWrapResult.childCount-1){
                    sb.append(" ")
                }
            }
        }
        return sb.toString()
    }

    private fun wordToCharacter() {
        binding.autoWrapOptions.removeAllViews()
        binding.autoWrapResult.removeAllViews()
        val content = englishContent
        val contents = content.split(" ")
        for ((index,item) in contents.shuffled().withIndex()) {
            KViewUtil.createNewFlexItemTextView(this,
                    binding.autoWrapOptions,
                    binding.autoWrapResult,
                    item.trim(),
                    index)
        }
    }

    private val englishContent: String
        private get() {
            val temp = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent)
            val str = temp.split("#".toRegex()).toTypedArray()
            return if (str.size > 1) {
                temp.split("#".toRegex()).toTypedArray()[0]
            } else {
                temp
            }
        }

    private val chineseContent: String
        private get() {
            val temp = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent)
            val str = temp.split("#".toRegex()).toTypedArray()
            return if (str.size > 1) {
                temp.split("#".toRegex()).toTypedArray()[1]
            } else {
                ""
            }
        }

    fun playItem() {
        if (mAVObject != null) {
            MyPlayer.getInstance(this).start(englishContent, mAVObject.getString(AVOUtil.EvaluationDetail.mp3))
        }
    }

    val isPlaying: Boolean
        get() = MyPlayer.getInstance(this).isPlaying

    override fun onDestroy() {
        super.onDestroy()
        MyPlayer.getInstance(this).onDestroy()
    }

}