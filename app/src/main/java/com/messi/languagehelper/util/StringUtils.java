package com.messi.languagehelper.util;

import android.text.Html;
import android.text.Spanned;

import java.util.Random;

public class StringUtils {

	@SuppressWarnings("deprecation")
	public static Spanned fromHtml(String html){
		html = html.replace("&lt;br&gt;","\n");
		Spanned result;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
		} else {
			result = Html.fromHtml(html);
		}
		return result;
	}

	public static void setSpeakerByLan(String lan) {
		if(lan.equals("en")){
			Setings.role = XFUtil.SpeakerEn;
		}else{
			Setings.role = XFUtil.SpeakerZh;
		}
	}
	
	public static void setSpeaker(String content) {
		LogUtil.DefalutLog(content+"---"+isEnglish(content));
		Setings.role = XFUtil.SpeakerZh;
		if (isEnglish(content)) {
			Setings.role = XFUtil.SpeakerEn;
		}
	}

	public static boolean isEnglish(String charaString) {
		char[] arr = charaString.toCharArray();
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			if ((arr[i] >= 65 && arr[i] <= 90) || (arr[i] >= 97 && arr[i] <= 125) || (arr[i] == 39)
					|| (arr[i] == 8217)) {
				count++;
			}
		}
		return (double)count / arr.length  > 0.5;
	}

	public static boolean isAllEnglish(String charaString) {
		char[] arr = charaString.toCharArray();
		boolean count = true;
		int word = 0;
		for (int i = 0; i < arr.length; i++) {
			if ((arr[i] > 0 && arr[i] < 127) || (arr[i] == 8217)) {
				if((arr[i] >= 65 && arr[i] <= 90) || (arr[i] >= 97 && arr[i] <= 122)){
					word++;
				}
			}else {
				count = false;
			}
		}
		if(word < (arr.length/2)){
			count = false;
		}
		return count;
	}
	
	public static boolean isAllChinese(String str) {
		boolean result = true;
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public static boolean isContainChinese(String str) {
		boolean result = false;
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public static String numToStrTimes(long times){
		if(times < 10000){
			return " " + String.valueOf(times);
		}else if(times < 100000000){
			return " " + new java.text.DecimalFormat("#.0").format( (double)times / 10000 ) + "万";
		}else {
			return " " + new java.text.DecimalFormat("#.0").format( (double)times / 100000000 ) + "亿";
		}
	}

	//length表示生成字符串的长度
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String replaceSome(String content){
		return content.replaceAll("[,.?#!，。？！]", "");
	}

	public static String replaceAll(String content){
		return content.replaceAll("[\\p{Punct}]+", "");
	}



}
