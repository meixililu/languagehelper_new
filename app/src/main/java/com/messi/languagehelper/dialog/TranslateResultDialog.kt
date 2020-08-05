package com.messi.languagehelper.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.messi.languagehelper.R
import com.messi.languagehelper.box.BoxHelper
import com.messi.languagehelper.box.Record
import com.messi.languagehelper.databinding.DialogTranslateResultBinding
import com.messi.languagehelper.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TranslateResultDialog(val mContext: Context, private val word: String) : Dialog(mContext, R.style.DialogNone) {

    private lateinit var binding: DialogTranslateResultBinding
    private var mBean: Record? = null
    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTranslateResultBinding.inflate(LayoutInflater.from(mContext))
        setContentView(binding.root)
        binding.dicTitle.text = word
        binding.desTv.text = "Loading..."
        binding.desTv.setOnClickListener { this.dismiss() }
        binding.collectedCb.setOnClickListener { updateCollectedStatus() }
        binding.titleLayout.setOnClickListener { play() }
        translateController()
    }

    override fun show() {
        setPopViewPosition()
        super.show()
    }

    private fun play() {
        if (mBean != null && !TextUtils.isEmpty(mBean!!.ph_en_mp3)) {
            MyPlayer.getInstance(mContext).start(word, mBean!!.ph_en_mp3)
        } else {
            MyPlayer.getInstance(mContext).start(word)
        }
    }

    private fun updateCollectedStatus() {
        if (mBean != null) {
            if (mBean!!.iscollected == "0") {
                mBean!!.iscollected = "1"
                binding!!.collectedCb.isChecked = true
                ToastUtil.diaplayMesShort(mContext, mContext.resources.getString(R.string.favorite_success))
                TranslateUtil.addToNewword(mBean)
            } else {
                mBean!!.iscollected = "0"
                binding!!.collectedCb.isChecked = false
                ToastUtil.diaplayMesShort(mContext, mContext.resources.getString(R.string.favorite_cancle))
            }
            BoxHelper.update(mBean)
        }
    }

    private fun translateController() {
        Setings.q = word
        job = CoroutineScope(Dispatchers.Main).launch{
            var result = TranDictHelper.tranDict(mContext)
            onResult(result.data)
        }
    }

    private fun onResult(mRecord: Record?) {
        mBean = mRecord
        if (mRecord == null) {
            ToastUtil.diaplayMesShort(mContext, mContext.resources.getString(R.string.network_error))
        } else {
            binding!!.desTv.text = mRecord.english
            BoxHelper.insert(mRecord)
            LiveEventBus.get(KeyUtil.TranAndDicRefreshEvent).post("reload")
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtil.DefalutLog("TranslateResultDialog---onStop:"+job?.isCompleted)
        MyPlayer.getInstance(mContext).onDestroy()
        if (job != null && !job?.isCompleted!!){
            job?.cancel()
        }
    }

    private fun setPopViewPosition() {
        if (x > 0 && y > 0) {
            val win = this.window
            val params = WindowManager.LayoutParams()
            params.gravity = Gravity.TOP
            params.x = x - ScreenUtil.dip2px(mContext, 60f)
            params.y = y - ScreenUtil.dip2px(mContext, 18f)
            win!!.attributes = params
            setCanceledOnTouchOutside(true)
        }
    }

    companion object {
        @JvmField
		var x = 0
        @JvmField
		var y = 0
    }

}