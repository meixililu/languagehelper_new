package com.messi.languagehelper.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.UserSpeakBean;

import java.util.regex.Pattern;

public class ScoreUtil {

	public static UserSpeakBean score(Context mContext, String content, String result){
		LogUtil.DefalutLog("old---content:"+content+"---result:"+result);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		UserSpeakBean bean = new UserSpeakBean();
		String mResult = formatString(result);
		String mContent = formatString(content);
		LogUtil.DefalutLog("old---content:"+mContent+"---result:"+mResult);
		String[] mResults = mResult.split(" ");
		String[] mContents = mContent.split(" ");
		int count = 0;
		for(String item : mContents){
			boolean isRight = false;
			for(String str : mResults){
				if(item.toLowerCase().equals(str.toLowerCase())){
					isRight = true;
					count ++;
					break;
				}
			}
			if(isRight){
				sb.append( setColor(mContext,item,R.color.green) );
				sb.append(" ");
			}else{
				sb.append( setColor(mContext,item,R.color.text_red) );
				sb.append(" ");
			}
		}
		double length = 0;
		for(String str : mResults){
			if(!TextUtils.isEmpty(str.trim())){
				length++;
			}
		}
		int scoreInt = (int)(count / length * 100);
		if(scoreInt > 59 ){
			bean.setColor(R.color.green);
		}else{
			bean.setColor(R.color.text_red);
		}
		bean.setContent(sb);
		bean.setScoreInt(scoreInt);
		bean.setScore(String.valueOf(scoreInt));
		return bean;
	}
	
	public static String formatString(String content){
		content = content.replaceAll("\\p{P}", " ");
		content = content.replaceAll("\\s+"," ");
		return content.trim();
	}
	
	public static SpannableString setColor(Context mContext, String content, int color){
		SpannableString spa = new SpannableString(content);
		try {
			spa.setSpan(new ForegroundColorSpan(
							mContext.getResources().getColor(color)), 
							0, content.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return spa;
	}
	
}
