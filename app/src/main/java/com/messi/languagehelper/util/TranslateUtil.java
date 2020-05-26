package com.messi.languagehelper.util;

import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.messi.languagehelper.bean.AiYueYuBean;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.box.TranResultZhYue;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.OnTranZhYueFinishListener;
import com.messi.languagehelper.impl.OnTranslateFinishListener;
import com.youdao.localtransengine.EnLineTranslator;
import com.youdao.localtransengine.LanguageConvert;
import com.youdao.sdk.ydtranslate.EnWordTranslator;
import com.youdao.sdk.ydtranslate.Translate;

import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TranslateUtil {

	public static void Translate_init(Handler mHandler) {
		new DictHelper().Translate(mHandler);
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

	public static String getHtmlContext(String html) {
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("<span class=\"dd\">([^</span>]*)");//匹配<title>开头，</title>结尾的文档
		Matcher m = p.matcher(html);//开始编译
		int count = 0;
		while (m.find()) {
			if (count > 0) {
				sb.append("\n");
			}
			sb.append(m.group(1).trim());//获取被匹配的部分
			count++;
		}

		return sb.toString();
	}

	public static void offlineTranslate(ObservableEmitter<Translate> e){
		Translate translate = null;
		if(isOfflineTranslateWords()){
			LogUtil.DefalutLog("offline-word");
			translate = getWordTranslate(e);
		}else {
			LogUtil.DefalutLog("offline-sentence");
			translate = getSentenceTranslate();
		}
		e.onNext(translate);
		e.onComplete();
	}

	public static Translate getWordTranslate(ObservableEmitter<Translate> e){
		EnWordTranslator.initDictPath(SDCardUtil.OfflineDicPath);
		if(!EnWordTranslator.isInited()){
			EnWordTranslator.init();
		}
		return EnWordTranslator.lookupNative(Setings.q);
	}

	public static Translate getSentenceTranslate(){
		EnLineTranslator.initDictPath(SDCardUtil.OfflineDicPath);
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

	public static void Translate(final OnTranslateFinishListener listener) throws Exception{
		new TranslateHelper().Translate(listener);
	}

	public static void TranslateZhYue(OnTranZhYueFinishListener listener) throws Exception{
		Tran_BaiduYue(listener);
	}

	private static void Tran_BaiduYue(final OnTranZhYueFinishListener listener) {
		Observable.create(new ObservableOnSubscribe<TranResultZhYue>() {
			@Override
			public void subscribe(final ObservableEmitter<TranResultZhYue> e) throws Exception {
				LanguagehelperHttpClient.postBaidu(new BgCallback(){
					@Override
					public void onFailured() {
						Tran_Aiyueyu(listener);
					}
					@Override
					public void onResponsed(String responseString) {
						TranResultZhYue result = null;
						try {
							result = tran_bd_api_zh_yue(responseString);
						} catch (Exception ec) {
							ec.printStackTrace();
						}
						if(result != null){
							e.onNext( result );
						}else {
							Tran_Aiyueyu(listener);
						}
					}
				});
			}
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<TranResultZhYue>() {
					@Override
					public void onSubscribe(Disposable d) {
					}
					@Override
					public void onNext(TranResultZhYue mResult) {
						listener.OnFinishTranslate(mResult);
					}
					@Override
					public void onError(Throwable e) {
						Tran_Aiyueyu(listener);
					}
					@Override
					public void onComplete() {
					}
				});
	}

	private static void Tran_Aiyueyu(final OnTranZhYueFinishListener listener) {
		Observable.create(new ObservableOnSubscribe<TranResultZhYue>() {
			@Override
			public void subscribe(final ObservableEmitter<TranResultZhYue> e) throws Exception {
				LanguagehelperHttpClient.getAiyueyu(new BgCallback(){
					@Override
					public void onFailured() {
						e.onError(null);
					}
					@Override
					public void onResponsed(String responseString) {
						TranResultZhYue result = null;
						try {
							result = tran_aiyueyu(responseString);
						} catch (Exception ec) {
							ec.printStackTrace();
						}
						if(result != null){
							e.onNext( result );
						}else {
							e.onError(null);
						}
					}
				});
			}
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<TranResultZhYue>() {
					@Override
					public void onSubscribe(Disposable d) {
					}
					@Override
					public void onNext(TranResultZhYue mResult) {
						listener.OnFinishTranslate(mResult);
					}
					@Override
					public void onError(Throwable e) {
						listener.OnFinishTranslate(null);
					}
					@Override
					public void onComplete() {
					}
				});
	}

	private static TranResultZhYue tran_bd_api_zh_yue(String mResult) {
		TranResultZhYue currentDialogBean = null;
		try {
			if(!TextUtils.isEmpty(mResult)) {
				if (JsonParser.isJson(mResult)) {
					String dstString = JsonParser.getTranslateResult(mResult);
					if (!dstString.contains("error_msg:")) {
						currentDialogBean = new TranResultZhYue(dstString, Setings.q, Setings.to);
						LogUtil.DefalutLog("tran_bd_api_zh_yue http:"+dstString);
					}
				}
			}
		}catch (Exception e){
			LogUtil.DefalutLog("tran_bd_api_zh_yue error");
			e.printStackTrace();
		}
		return currentDialogBean;
	}

	private static TranResultZhYue tran_aiyueyu(String mResult) throws Exception{
		TranResultZhYue currentDialogBean = null;
		if(!TextUtils.isEmpty(mResult)) {
			String result = mResult.substring(mResult.indexOf("{"));
			LogUtil.DefalutLog("tran_aiyueyu:"+result);
			AiYueYuBean mAiYueYuBean = new Gson().fromJson(result, AiYueYuBean.class);
			if (mAiYueYuBean != null && mAiYueYuBean.getState() == 200 &&
					!TextUtils.isEmpty(mAiYueYuBean.getContent())) {
				currentDialogBean = new TranResultZhYue(mAiYueYuBean.getContent(), Setings.q, Setings.to);
				LogUtil.DefalutLog("tran_aiyueyu http:"+mAiYueYuBean.getContent());
			}
		}
		return currentDialogBean;
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
