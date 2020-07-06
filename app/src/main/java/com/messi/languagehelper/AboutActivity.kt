package com.messi.languagehelper

import android.os.Bundle
import android.view.LayoutInflater
import com.messi.languagehelper.databinding.AboutBinding
import com.messi.languagehelper.util.AVAnalytics
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.util.Setings
import com.messi.languagehelper.util.ToastUtil

class AboutActivity : BaseActivity() {

    private var clickTime = 0
    lateinit var binding: AboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setActionBarTitle(resources.getString(R.string.title_about))
        binding.appVersion.text = Setings.getVersionName(this)
        binding.emailLayout.setOnClickListener { onEmailClick() }
        binding.imgLogo.setOnClickListener { onImgClick() }
    }

    private fun onEmailClick() {
        Setings.contantUs(this@AboutActivity)
        AVAnalytics.onEvent(this, "about_pg_send_email")
    }

    private fun onImgClick() {
        clickTime++
        if (clickTime > 2) {
            ToastUtil.diaplayMesShort(this, "ok")
            Setings.saveSharedPreferences(Setings.getSharedPreferences(this), KeyUtil.ShowCNK, 1)
        }
    }
}