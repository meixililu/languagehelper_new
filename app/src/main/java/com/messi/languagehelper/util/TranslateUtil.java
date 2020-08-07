package com.messi.languagehelper.util;

import android.text.TextUtils;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.impl.OnDictFinishListener;
import com.messi.languagehelper.impl.OnTranslateFinishListener;
import com.youdao.localtransengine.EnLineTranslator;
import com.youdao.localtransengine.LanguageConvert;
import com.youdao.sdk.ydtranslate.EnWordTranslator;
import com.youdao.sdk.ydtranslate.Translate;

import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

public class TranslateUtil {

	public static void Translate_init(OnDictFinishListener mListener) {
		new DictHelper().Translate(mListener);
	}

	public static void addContent(Element title,StringBuilder sb){
		if(title != null){
			String text = title.text().trim();
			if(!TextUtils.isEmpty(text)){
				sb.append(text);
				sb.append("\n");
			}
		}

	}

	public static void addContentAll(Element title,StringBuilder sb,StringBuilder sb_play){
		if(title != null){
			String text = title.text().trim();
			if(!TextUtils.isEmpty(text)){
				sb.append(text);
				sb.append("\n");
				sb_play.append(text);
				sb_play.append(",");
			}
		}
	}

	public static void addContentAll(Element title,StringBuilder sb,StringBuilder sb_play,String space){
		if(title != null){
			String text = title.text().trim();
			if(!TextUtils.isEmpty(text)){
				sb.append(text);
				sb.append(space);
				sb_play.append(text);
				sb_play.append(",");
			}
		}
	}

	public static void addContentAll(String title,StringBuilder sb,StringBuilder sb_play){
		if(!TextUtils.isEmpty(title)){
			sb.append(title);
			sb.append("\n");
			sb_play.append(title);
			sb_play.append(",");
		}
	}

	public static Translate offlineTranslate(){
		Translate translate = null;
		if(isOfflineTranslateWords()){
			translate = getWordTranslate();
			LogUtil.DefalutLog("offline-word:"+translate);
		}else {
			translate = getSentenceTranslate();
			LogUtil.DefalutLog("offline-sentence:"+translate);
		}
		return translate;
	}

	public static Translate getWordTranslate(){
		EnWordTranslator.initDictPath(SDCardUtil.OfflineDicPathRoot);
		if(!EnWordTranslator.isInited()){
  			EnWordTranslator.init();
		}
		if (EnWordTranslator.isInited()) {
			return EnWordTranslator.lookupNative(Setings.q);
		}
		return null;
	}

	public static Translate getSentenceTranslate(){
		EnLineTranslator.initDictPath(SDCardUtil.OfflineDicPathRoot);
		return EnLineTranslator.lookup(Setings.q, LanguageConvert.AUTO);
	}

	public static boolean isOfflineTranslateWords(){
		if (StringUtils.isEnglish(Setings.q)) {
			if(Setings.q.split(" ").length > 2){
				return false;
			}else {
				return true;
			}
		}else {
			if(Setings.q.split("").length > 3){
				return false;
			}else {
				return true;
			}
		}
	}

	public static void addSymbol(Translate translate, StringBuilder sb){
		if(StringUtils.isEnglish(Setings.q)){
			boolean isHasSymbol = false;
			if (!TextUtils.isEmpty(translate.getUkPhonetic())) {
				sb.append("英[");
				sb.append(translate.getUkPhonetic());
				sb.append("]    ");
				isHasSymbol = true;
			}
			if (!TextUtils.isEmpty(translate.getUsPhonetic())) {
				sb.append("美[");
				sb.append(translate.getUsPhonetic());
				sb.append("]");
				isHasSymbol = true;
			}
			if(isHasSymbol){
				sb.append("\n");
			}
		}
	}

	public static void Translate(final OnTranslateFinishListener listener){
		new TranslateHelper().Translate(listener);
	}

	public static void addToNewword(Record mBean){
		WordDetailListItem myNewWord = new WordDetailListItem();
		myNewWord.setName(mBean.getChinese());
		myNewWord.setDesc(mBean.getEnglish());
		String pats1 = "英.*\\[";
		Pattern pattern1 = Pattern.compile(pats1);
		String pats2 = "美.*\\[";
		Pattern pattern2 = Pattern.compile(pats2);
		if(pattern1.matcher(mBean.getEnglish()).find() || pattern2.matcher(mBean.getEnglish()).find()){
			String[] texts = mBean.getEnglish().split("\n");
			if (texts != null && texts.length > 0) {
				String symbol = texts[0];
				if(symbol.contains("英") || symbol.contains("美")){
					String des = mBean.getEnglish().replace(symbol,"").trim();
					myNewWord.setSymbol(symbol);
					myNewWord.setDesc(des);
				}
			}
		}
		myNewWord.setSound(mBean.getPh_en_mp3());
		myNewWord.setNew_words("1");
		myNewWord.setType("search");
		BoxHelper.saveSearchResultToNewWord(myNewWord);
	}


}
