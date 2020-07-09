package com.messi.languagehelper.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.UserSpeakBean;

public class ScoreUtil {

	public static UserSpeakBean score(String content, String result){
		LogUtil.DefalutLog("old---content:"+content+"---result:"+result);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		UserSpeakBean bean = new UserSpeakBean();
		String mResult = result.replaceAll("\\s+"," ");
		String mContent = content.replaceAll("\\s+"," ");
		LogUtil.DefalutLog("old---content:"+mContent+"---result:"+mResult);
		String[] mResults = mResult.split(" ");
		String[] mContents = mContent.split(" ");
		int count = 0;
		for(String item : mContents){
			String itemTemp = item.replaceAll("\\p{P}", "");
			boolean isRight = false;
			for(String str : mResults){
				String strTemp = str.replaceAll("\\p{P}", "");
				if(itemTemp.toLowerCase().equals(strTemp.toLowerCase())){
					isRight = true;
					count ++;
					break;
				}
			}
			if(isRight){
				sb.append( setColor(item,R.color.green) );
			}else{
				sb.append( setColor(item,R.color.text_red) );
			}
			sb.append(" ");
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
			if(scoreInt < 10){
				scoreInt = NumberUtil.getRandomNumber(10,50);
			}
			bean.setColor(R.color.text_red);
		}
		bean.setContent(sb);
		bean.setScoreInt(scoreInt);
		bean.setScore(String.valueOf(scoreInt));
		return bean;
	}

	public static UserSpeakBean score(String content, String result, int type){
		LogUtil.DefalutLog("old---content:"+content+"---result:"+result);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		UserSpeakBean bean = new UserSpeakBean();
		String mResult = result.replaceAll("\\s+"," ");
		String mContent = content.replaceAll("\\s+"," ");
		LogUtil.DefalutLog("old---content:"+mContent+"---result:"+mResult);
		String[] mResults = mResult.split(" ");
		String[] mContents = mContent.split(" ");
		int count = 0;
		for(String item : mResults){
			String itemTemp = item.replaceAll("\\p{P}", "");
			boolean isRight = false;
			for(String str : mContents){
				String strTemp = str.replaceAll("\\p{P}", "");
				if(itemTemp.toLowerCase().equals(strTemp.toLowerCase())){
					isRight = true;
					count ++;
					break;
				}
			}
			if(isRight){
				sb.append( setColor(item,R.color.green) );
			}else{
				sb.append( setColor(item,R.color.text_red) );
			}
			sb.append(" ");
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

	public static UserSpeakBean scoreChinese(Context mContext, String content, String result){
		LogUtil.DefalutLog("old---content:"+content+"---result:"+result);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		UserSpeakBean bean = new UserSpeakBean();
		String mResult = result.replaceAll("\\s+"," ").replaceAll("\\p{P}", "");
		String mContent = content.replaceAll("\\s+"," ").replaceAll("\\p{P}", "");
		LogUtil.DefalutLog("old---content:"+mContent+"---result:"+mResult);
		String[] mResults = mResult.split("");
		String[] mContents = mContent.split("");
		int count = 0;
		for(String item : mContents){
			boolean isRight = false;
			if(!TextUtils.isEmpty(item)){
				for(String str : mResults){
					if(!TextUtils.isEmpty(str)){
						if(item.toLowerCase().equals(str.toLowerCase())){
							isRight = true;
							count ++;
							break;
						}
					}
				}
			}
			if(isRight){
				sb.append( setColor(item,R.color.green) );
			}else{
				sb.append( setColor(item,R.color.text_red) );
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

	public static String getTestResult(int score) {
		String word = "Nice";
		if (score > 90) {
			word = "Perfect";
		} else if (score > 70) {
			word = "Great";
		} else if (score > 59) {
			word = "Not bad";
		} else {
			word = "Not bad";
		}
		return word;
	}
	
	public static String formatString(String content){
		content = content.replaceAll("\\p{P}", " ");
		content = content.replaceAll("\\s+"," ");
		return content.trim();
	}
	
	public static SpannableString setColor(String content, int color){
		SpannableString spa = new SpannableString(content);
		try {
			spa.setSpan(new ForegroundColorSpan(BaseApplication.instance.getResources().getColor(color)),
					0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return spa;
	}
	
}
