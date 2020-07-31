package com.messi.languagehelper.util

import android.animation.Animator
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.get
import com.google.android.flexbox.FlexboxLayout
import com.messi.languagehelper.R

object KViewUtil {

    fun showRecordImgAnimation(volume: Int, recordAnimImg: ImageView) {
        when {
            volume < 4 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_1)
            }
            volume < 8 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_2)
            }
            volume < 12 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_3)
            }
            volume < 16 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_4)
            }
            volume < 20 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_5)
            }
            volume < 24 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_6)
            }
            volume < 31 -> {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_7)
            }
        }
    }

    fun removeFromParentView(view: View) {
        val parent = view.parent as ViewGroup
        parent?.removeView(view)
    }

    fun getViewsOffset(cView: View, nView: View): FloatArray {
        var cps = getViewPosition(cView)
        var nps = getViewPosition(nView)
        val x = nps[0]-cps[0]
        val y = nps[1]-cps[1]
        return floatArrayOf(x.toFloat(), y.toFloat())
    }

    fun getViewPosition(paramView: View): IntArray {
        val arrayOfInt = IntArray(2)
        paramView.getLocationOnScreen(arrayOfInt)
        return arrayOfInt
    }

    fun addViewAnimation(context: Context, targetParent: ViewGroup,sumitBtn:TextView,tv: TextView){
        var content = tv.text.toString()
        if(StringUtils.isEnglish(content)){
            MyPlayer.getInstance(context).start(content)
        }
        var nView = addNewFlexItemTextView(context,content)
        targetParent.addView(nView)
        nView.postDelayed({
            var offset = getViewsOffset(tv,nView)
            tv.animate()
                    .translationX(offset[0])
                    .translationY(offset[1])
                    .setDuration(200)
                    .setListener(object : Animator.AnimatorListener{
                        override fun onAnimationRepeat(p0: Animator?) {
                        }
                        override fun onAnimationEnd(p0: Animator?) {
                            ViewUtil.removeFromParentView(tv)
                            tv.x = 0f
                            tv.y = 0f
                            nView.addView(tv)
                            sumitBtn.isEnabled = true
                        }
                        override fun onAnimationCancel(p0: Animator?) {
                        }
                        override fun onAnimationStart(p0: Animator?) {
                        }
                    })
                    .setInterpolator(AccelerateInterpolator())
                    .start()
        },60)
    }

    fun removeItem(targetParent: ViewGroup, resultParent: ViewGroup,sumitBtn:TextView, tv: TextView){
        var position = tv.getTag(R.id.tag_key) as Int
        if(position > targetParent.childCount){
            return
        }
        var oview = targetParent[position] as FrameLayout
        var offset = getViewsOffset(tv,oview)
        tv.animate()
                .translationX(offset[0])
                .translationY(offset[1])
                .setDuration(200)
                .setListener(object :Animator.AnimatorListener{
                    override fun onAnimationRepeat(p0: Animator?) {
                    }
                    override fun onAnimationEnd(p0: Animator?) {
                        ViewUtil.removeFromParentView(tv.parent as FrameLayout)
                        ViewUtil.removeFromParentView(tv)
                        tv.x = 0f
                        tv.y = 0f
                        oview.addView(tv)
                        sumitBtn.isEnabled = resultParent.childCount != 0
                    }
                    override fun onAnimationCancel(p0: Animator?) {
                    }
                    override fun onAnimationStart(p0: Animator?) {
                    }
                })
                .setInterpolator(AccelerateInterpolator())
                .start()
    }
    fun createNewFlexItemTextView(context: Context,
                                  autoWrapOptions:ViewGroup,
                                  autoWrapResult:ViewGroup,
                                  sumitBtn:TextView,
                                  word: String,
                                  position: Int) {
        createNewFlexItemTextView(context,autoWrapOptions,autoWrapResult,sumitBtn,word,position,10f,10f)
    }

    fun createNewFlexItemTextView(context: Context,
                                  autoWrapOptions:ViewGroup,
                                  autoWrapResult:ViewGroup,
                                  sumitBtn:TextView,
                                  word: String,
                                  position: Int,
                                  paddingLR: Float,
                                  paddingTB: Float) {
        if (TextUtils.isEmpty(word)){
            LogUtil.DefalutLog("word:$position is empty")
            return
        }
        val frameLayout = FrameLayout(context)
        val frameParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        ViewCompat.setPaddingRelative(frameLayout, 0, 0, 0, 0)
        val margin = ScreenUtil.dip2px(context, 8f)
        val marginTop = ScreenUtil.dip2px(context, 7f)
        frameParams.setMargins(0, 0, margin, marginTop)
        frameLayout.layoutParams = frameParams
        frameLayout.setBackgroundResource(R.color.none)

        val textViewbg = TextView(context)
        textViewbg.gravity = Gravity.CENTER
        textViewbg.text = word
        textViewbg.textSize = 18f
        textViewbg.setBackgroundResource(R.drawable.bg_btn_course_item_backcup)
        textViewbg.setTextColor(context.resources.getColor(R.color.none))
        val paddingbg = ScreenUtil.dip2px(context, paddingTB)
        val paddingLRbg = ScreenUtil.dip2px(context, paddingLR)
        ViewCompat.setPaddingRelative(textViewbg, paddingLRbg, paddingbg, paddingLRbg, paddingbg)
        val layoutParamsbg = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        textViewbg.layoutParams = layoutParamsbg
        frameLayout.addView(textViewbg)

        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = word
        textView.textSize = 18f
        textView.setTag(R.id.tag_key,position)
        textView.setTag(R.id.tag_status,0)
        textView.setBackgroundResource(R.drawable.bg_btn_course_item)
        textView.setTextColor(context.resources.getColor(R.color.text_black))
        textView.setOnClickListener {
            var status = it.getTag(R.id.tag_status) as Int
            if (status == 0) {
                textView.setTag(R.id.tag_status,1)
                addViewAnimation(context,autoWrapResult,sumitBtn,textView)
            }else{
                textView.setTag(R.id.tag_status,0)
                removeItem(autoWrapOptions,autoWrapResult,sumitBtn,textView)
            }
        }
        val pTB = ScreenUtil.dip2px(context, paddingTB)
        val pLR = ScreenUtil.dip2px(context, paddingLR)
        ViewCompat.setPaddingRelative(textView, pLR, pTB, pLR, pTB)
        val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams
        frameLayout.addView(textView)
        autoWrapOptions.addView(frameLayout)
    }

    private fun addNewFlexItemTextView(context: Context, word: String): ViewGroup {
        val frameLayout = FrameLayout(context)
        val frameParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        ViewCompat.setPaddingRelative(frameLayout, 0, 0, 0, 0)
        val margin = ScreenUtil.dip2px(context, 8f)
        val marginTop = ScreenUtil.dip2px(context, 7f)
        frameParams.setMargins(0, 0, margin, marginTop)
        frameLayout.layoutParams = frameParams
        return frameLayout
    }

    fun createOptionItem(context: Context, item: String): TextView{
        val textViewbg = TextView(context)
        textViewbg.gravity = Gravity.CENTER
        textViewbg.text = item
        textViewbg.textSize = 16f
        textViewbg.elevation = 2f
        textViewbg.setTextColor(context.resources.getColor(R.color.text_black))
        setOptionItemStyle(context, textViewbg, R.drawable.border_shadow_gray_oval_selecter)
        return textViewbg
    }

    fun setOptionItemStyle(context: Context?, textViewbg: TextView, resid: Int){
        if(context != null){
            textViewbg.setBackgroundResource(resid)
            val paddingbg = ScreenUtil.dip2px(context, 16f)
            val paddingLRbg = ScreenUtil.dip2px(context, 14f)
            ViewCompat.setPaddingRelative(textViewbg, paddingLRbg, paddingbg, paddingLRbg, paddingbg)
            val layoutParamsbg = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            val marginBottom = ScreenUtil.dip2px(context, 10f)
            layoutParamsbg.setMargins(0, 0, 0, marginBottom)
            textViewbg.layoutParams = layoutParamsbg
        }
    }

    fun setDataOrHide(tv: TextView, text: String){
        if (TextUtils.isEmpty(text)){
            tv.visibility = View.GONE
        }else{
            tv.visibility = View.VISIBLE
            tv.text = text
        }
    }
}