package com.messi.languagehelper.views

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.messi.languagehelper.dialog.TranslateResultDialog

class TouchableSpan(private val context: Context, private val word: String) : ClickableSpan() {

    override fun onClick(tv: View) {
        showDialog()
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
    }

    private fun showDialog() {
        try {
            val dialog = TranslateResultDialog(context, word)
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}