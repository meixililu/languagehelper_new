package com.messi.languagehelper.util

import android.animation.Animator
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.get
import com.google.android.flexbox.FlexboxLayout
import com.messi.languagehelper.R

object KViewUtil {

    fun removeFromParentView(view: View) {
        val parent = view.parent as ViewGroup
        parent?.removeView(view)
    }

    fun getViewsOffset(cView: View, nView: View): FloatArray {
        var cps = getViewPosition(cView)
        var nps = getViewPosition(nView)
        val x = nps[0]-cps[0]
        val y = nps[1]-cps[1]
        val offset = floatArrayOf(x.toFloat(), y.toFloat())
        LogUtil.DefalutLog("ViewsOffset:$x---$y")
        return offset
    }

    fun getViewPosition(paramView: View): IntArray {
        val arrayOfInt = IntArray(2)
        paramView.getLocationOnScreen(arrayOfInt)
        LogUtil.DefalutLog("ViewPosition:" + arrayOfInt[0] + "---" + arrayOfInt[1])
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

    private fun addNewFlexItemTextView(context: Context, word: String): ViewGroup {
        val frameLayout = FrameLayout(context)
        val frameParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        ViewCompat.setPaddingRelative(frameLayout, 0, 0, 0, 0)
        val margin = ScreenUtil.dip2px(context, 4f)
        val marginTop = ScreenUtil.dip2px(context, 12f)
        frameParams.setMargins(margin, marginTop, margin, 0)
        frameLayout.layoutParams = frameParams
        return frameLayout
    }

    fun removeItem(targetParent: ViewGroup, resultParent: ViewGroup,sumitBtn:TextView, tv: TextView){
        var position = tv.getTag(R.id.tag_key) as Int
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
        if (TextUtils.isEmpty(word)){
            LogUtil.DefalutLog("word:$position is empty")
            return
        }
        val frameLayout = FrameLayout(context)
        val frameParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        ViewCompat.setPaddingRelative(frameLayout, 0, 0, 0, 0)
        val margin = ScreenUtil.dip2px(context, 4f)
        val marginTop = ScreenUtil.dip2px(context, 12f)
        frameParams.setMargins(margin, marginTop, margin, 0)
        frameLayout.layoutParams = frameParams
        frameLayout.setBackgroundResource(R.color.none)

        val textViewbg = TextView(context)
        textViewbg.gravity = Gravity.CENTER
        textViewbg.text = word
        textViewbg.textSize = 18f
        textViewbg.setBackgroundResource(R.drawable.bg_btn_course_item_backcup)
        textViewbg.setTextColor(context.resources.getColor(R.color.none))
        val paddingbg = ScreenUtil.dip2px(context, 12f)
        val paddingLRbg = ScreenUtil.dip2px(context, 10f)
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
        val padding = ScreenUtil.dip2px(context, 12f)
        val paddingLR = ScreenUtil.dip2px(context, 10f)
        ViewCompat.setPaddingRelative(textView, paddingLR, padding, paddingLR, padding)
        val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams
        frameLayout.addView(textView)
        autoWrapOptions.addView(frameLayout)
    }
}