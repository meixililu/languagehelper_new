package com.messi.languagehelper.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.views.TouchableSpan;

public class TextHandlerUtil {

    public static void handlerText(Context context, ProgressBar mProgressbar,
                                   TextView contentTv, String text) {
        if (TextUtils.isEmpty(text)) {
            contentTv.setText(text);
            return;
        }
        SpannableString ss = new SpannableString(text);
        boolean isIn = false;
        boolean isFinish = false;
        int star = -1;
        int end = -1;
        char[] arr = text.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] >= 65 && arr[i] <= 90) || (arr[i] >= 97 && arr[i] <= 125) || (arr[i] == 39)
                    || (arr[i] == 8217)) {
                if (!isIn) {
                    isIn = true;
                    star = i;
                }
            } else {
                if (isIn && !isFinish) {
                    isFinish = true;
                    end = i;
                }
            }
            if (isIn && isFinish) {
                isIn = false;
                isFinish = false;
                final String word = text.substring(star, end);
                TouchableSpan clickableSpan = new TouchableSpan(context, mProgressbar, word);
                ss.setSpan(clickableSpan, star, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                star = -1;
                end = -1;
            }
        }
        contentTv.setText(ss);
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        contentTv.setHighlightColor(Color.TRANSPARENT);
    }

    public static void handlerText(Context context, FragmentProgressbarListener mProgressbarListener,
                                   TextView contentTv, String text) {
        if (TextUtils.isEmpty(text)) {
            contentTv.setText(text);
            return;
        }
        SpannableString ss = new SpannableString(text);
        boolean isIn = false;
        boolean isFinish = false;
        int star = -1;
        int end = -1;
        char[] arr = text.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] >= 65 && arr[i] <= 90) || (arr[i] >= 97 && arr[i] <= 125) || (arr[i] == 39)
                    || (arr[i] == 8217)) {
                if (!isIn) {
                    isIn = true;
                    star = i;
                }
            } else {
                if (isIn && !isFinish) {
                    isFinish = true;
                    end = i;
                }
            }
            if (isIn && isFinish) {
                isIn = false;
                isFinish = false;
                final String word = text.substring(star, end);
                TouchableSpan clickableSpan = new TouchableSpan(context, mProgressbarListener, word);
                ss.setSpan(clickableSpan, star, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                star = -1;
                end = -1;
            }
        }
        contentTv.setText(ss);
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        contentTv.setHighlightColor(Color.TRANSPARENT);
    }

}
