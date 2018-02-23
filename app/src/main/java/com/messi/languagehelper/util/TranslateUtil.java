package com.messi.languagehelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.RequestBody;
import com.avos.avoscloud.okhttp.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.messi.languagehelper.bean.BaiduV2Bean;
import com.messi.languagehelper.bean.DictionaryRootJuhe;
import com.messi.languagehelper.bean.HjTranBean;
import com.messi.languagehelper.bean.IcibaNew;
import com.messi.languagehelper.bean.Root;
import com.messi.languagehelper.bean.StackTransalte;
import com.messi.languagehelper.bean.TranslateApiBean;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.OnTranslateFinishListener;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.youdao.localtransengine.EnLineTranslator;
import com.youdao.localtransengine.LanguageConvert;
import com.youdao.sdk.ydtranslate.EnWordTranslator;
import com.youdao.sdk.ydtranslate.Translate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
	
	public static final String baidu_api = "baidu_api";
	public static final String show_api = "show_api";
	public static final String juhe_api = "juhe_api";
	public static final String biying_web = "biying_web";
	public static final String youdao_web = "youdao_web";
	public static List<TranslateApiBean> apiOrder;
	public static StackTransalte mStackTransalte;
	public static String translateDefaultOrder = "[{\"name\":\"baidu_api\",\"status\":\"1\"},{\"name\":\"show_api\",\"status\":\"1\"},{\"name\":\"juhe_api\",\"status\":\"1\"},{\"name\":\"biying_web\",\"status\":\"1\"},{\"name\":\"youdao_web\",\"status\":\"1\"}]";

	public static void Translate_init(Context mActivity, Handler mHandler) {
		SharedPreferences sp = Settings.getSharedPreferences(mActivity);
		if(apiOrder == null){
			String apiOrders = sp.getString(KeyUtil.TranslateApiOrder, translateDefaultOrder);
			LogUtil.DefalutLog("apiOrders:"+apiOrders);
			if(TextUtils.isEmpty(apiOrders) || apiOrders.equals("null")){
				apiOrders = translateDefaultOrder;
			}
			Type listType = new TypeToken<ArrayList<TranslateApiBean>>(){}.getType();
			apiOrder = new Gson().fromJson(apiOrders, listType);
		}
		mStackTransalte = getStackTransalte();
		Translate(mActivity,mHandler);
	}
	
	public static void Translate(Context mActivity, Handler mHandler) {
		if(!mStackTransalte.isEmpty()){
			TranslateApiBean bean = (TranslateApiBean) mStackTransalte.pop();
			if(bean.getStatus() == 1){
				selectTranslateApi(mActivity, mHandler, bean.getName());
			}else if(bean.getStatus() > 1 && System.currentTimeMillis() - bean.getStatus() > 1000*60*10){
				selectTranslateApi(mActivity, mHandler, bean.getName());
			}else{
				Translate(mActivity,mHandler);
			}
		}else{
			sendMessage(mHandler,2);
		}
	}
	
	public static StackTransalte getStackTransalte(){
		StackTransalte mStackTransalte = new StackTransalte();
		for(TranslateApiBean bean : apiOrder){
			if(bean.getStatus() >= 1){
				mStackTransalte.push(bean);
			}
		}
		return mStackTransalte;
	}
	
	public static void selectTranslateApi(Context mActivity, Handler mHandler, String method){
		LogUtil.DefalutLog("selectTranslateApi:"+method);
		if(method.equals(show_api)){
			Translate_Showapi(mActivity, mHandler);
		}else if(method.equals(juhe_api)){
			Translate_Juhe(mActivity, mHandler);
		}else if(method.equals(youdao_web)){
			Translate_youdao_web(mActivity, mHandler);
		}else if(method.equals(biying_web)){
			Translate_Biying_web(mActivity, mHandler);
		}else{
			Translate_baidu(mActivity, mHandler);
		}
	}

	public static void Translate_Showapi(final Context mActivity, final Handler mHandler) {
		RequestBody formBody = new FormEncodingBuilder()
				.add("showapi_appid", Settings.showapi_appid)
				.add("showapi_sign", Settings.showapi_secret)
				.add("showapi_timestamp", String.valueOf(System.currentTimeMillis()))
				.add("showapi_res_gzip", "1")
				.add("q", Settings.q)
				.build();
		LanguagehelperHttpClient.post(Settings.ShowApiDictionaryUrl, formBody, new Callback() {
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Translate(mActivity,mHandler);
			}

			@Override
			public void onResponse(Response mResponse) throws IOException {
				try {
					if (mResponse.isSuccessful()) {
						String responseString = mResponse.body().string();
						LogUtil.DefalutLog("Result---showapi:" + responseString);
						if (!TextUtils.isEmpty(responseString)) {
							if (JsonParser.isJson(responseString)) {
								Root mRoot = new Gson().fromJson(responseString, Root.class);
								if (mRoot != null && mRoot.getShowapi_res_code() == 0
										&& mRoot.getShowapi_res_body() != null
										&& mRoot.getShowapi_res_body().getRet_code() != -1) {
									Dictionary mDictionaryBean = JsonParser.changeShowapiResultToDicBean(mRoot,Settings.q);
									WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mDictionaryBean);
									sendMessage(mHandler,1);
								}else{
									Translate(mActivity,mHandler);
								}
							}else{
								Translate(mActivity,mHandler);
							}
						}else{
							Translate(mActivity,mHandler);
						}
					}else{
						Translate(mActivity,mHandler);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Translate(mActivity,mHandler);
				}
			}
		});
	}

	public static void Translate_Juhe(final Context mActivity, final Handler mHandler) {
		LanguagehelperHttpClient.get(Settings.JuheYoudaoApiUrl + Settings.q, new Callback() {
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Translate(mActivity,mHandler);
			}

			@Override
			public void onResponse(Response mResponse) throws IOException {
				try {
					if (mResponse.isSuccessful()) {
						String responseString = mResponse.body().string();
						LogUtil.DefalutLog("Result---juhe:" + responseString);
						if (!TextUtils.isEmpty(responseString)) {
							if (JsonParser.isJson(responseString)) {
								if(responseString.contains("us-phonetic")){
									responseString = responseString.replace("us-phonetic", "us_phonetic");
								}
								if(responseString.contains("uk-phonetic")){
									responseString = responseString.replace("uk-phonetic", "uk_phonetic");
								}
								DictionaryRootJuhe mRoot = new Gson().fromJson(responseString, DictionaryRootJuhe.class);
								if (mRoot != null && mRoot.getError_code() == 0 && mRoot.getResult() != null) {
									Dictionary mDictionaryBean = JsonParser.changeJuheResultToDicBean(mRoot,Settings.q);
									WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mDictionaryBean);
									sendMessage(mHandler,1);
								}else{
									Translate(mActivity,mHandler);
								}
							}else{
								Translate(mActivity,mHandler);
							}
						}else{
							Translate(mActivity,mHandler);
						}
					}else{
						Translate(mActivity,mHandler);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Translate(mActivity,mHandler);
				}
			}
		});
	}
	
	public static void Translate_youdao_web(final Context mActivity, final Handler mHandler) {
		LanguagehelperHttpClient.get(Settings.YoudaoWeb + Settings.q + Settings.YoudaoWebEnd, new Callback() {
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Translate(mActivity,mHandler);
			}
			
			@Override
			public void onResponse(Response mResponse) throws IOException {
				try {
					if (mResponse.isSuccessful()) {
						String responseString = mResponse.body().string();
						LogUtil.DefalutLog("Result---youdaoweb");
						if (!TextUtils.isEmpty(responseString)) {
							Dictionary mDictionary = getParseYoudaoWebHtml(responseString);
							if (mDictionary != null) {
								WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mDictionary);
								sendMessage(mHandler,1);
							}else{
								Translate(mActivity,mHandler);
							}
						}else{
							Translate(mActivity,mHandler);
						}
					}else{
						Translate(mActivity,mHandler);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Translate(mActivity,mHandler);
				}
			}
		});
	}
	
	public static Dictionary getParseYoudaoWebHtml(String html){
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_play = new StringBuilder();
		Dictionary mDictionary = new Dictionary();
		boolean isEnglish = StringUtils.isEnglish(Settings.q);
		mDictionary.setType(KeyUtil.ResultTypeShowapi);
		if(isEnglish){
			mDictionary.setFrom("en");
			mDictionary.setTo("zh");
		}else{
			mDictionary.setFrom("zh");
			mDictionary.setTo("en");
		}
		mDictionary.setWord_name(Settings.q);
		sb_play.append(Settings.q);
		sb_play.append("\n");
		Document doc = Jsoup.parse(html);
		Element error = doc.select("div.error-wrapper").first();
		if(error != null){
			LogUtil.DefalutLog(error.text());
			return null;
		}
		
		Element feedback = doc.select("div.feedback").first();
		if(feedback != null){
			LogUtil.DefalutLog(feedback.text());
			saveTranslateApiStatus(youdao_web,System.currentTimeMillis());
			return null;
		}
		
		Element symblo = doc.select("h2.wordbook-js > div.baav").first();
		if(symblo != null){
			addContent(symblo,sb);
		}
		Element translate = doc.select("div#phrsListTab > div.trans-container").first();
		if(translate != null){
			Element lis = translate.getElementsByTag("ul").first();
			if(lis != null){
				for(Element li : lis.children()){
					addContent(li,sb);
				}
			}
			Element p = translate.select("p.additional").first();
			if(p != null){
				addContent(p,sb);
			}
		}
		Element title1 = doc.select("div#tWebTrans > div.wt-container > div.title > span").first();
		if(title1 != null){
			sb.append("\n");
			sb.append("网络释义:");
			sb.append("\n");
			addContentAll(title1,sb,sb_play);
		}
		
		Elements containers = doc.select("div#tWebTrans > div.wt-container.wt-collapse");
		if(containers != null){
			for(Element item : containers){
				Element title = item.select("div.title > span").first();
				if(title != null){
					addContentAll(title,sb,sb_play);
				}
			}
		}
		Element webPhrase = doc.select("div#webPhrase").first();
		if(webPhrase != null){
			sb.append("\n");
			Element title = webPhrase.select("div.title").first();
			if(title != null){
				addContentAll(title,sb,sb_play);
			}
			Elements pTags = webPhrase.select("p");
			if(pTags != null){
				for(Element ptag : pTags){
					addContentAll(ptag,sb,sb_play);
				}
			}
		}
		Element authDictTrans = doc.select("div#authTrans > div#authTransToggle > div#authDictTrans").first();
		if(authDictTrans != null){
			Element wordGroup = authDictTrans.select("h4.wordGroup").first();
			if(wordGroup != null){
				sb.append("\n");
				sb.append("21世纪大英汉词典:");
				sb.append("\n");
				addContentAll(wordGroup,sb,sb_play);
			}
			Elements lis = authDictTrans.select("div#authDictTrans > ul > li");
			if(lis != null && lis.size() > 0){
				for(Element li : lis){
					li_iteration(li,sb,sb_play);
				}
			}
		}
		
		Elements wordGroup = doc.select("div#eTransform > div#transformToggle > div#wordGroup > p");
		if(wordGroup != null && wordGroup.size() > 0){
			sb.append("\n");
			sb.append("词组短语:");
			sb.append("\n");
			for(Element item : wordGroup){
				addContentAll(item,sb,sb_play);
			}
		}
		
		Element discriminate = doc.select("div#eTransform > div#transformToggle > div#discriminate > div.wt-container").first();
		if(discriminate != null){
			Element title = discriminate.select("div.title").first();
			if(title != null){
				sb.append("\n");
				sb.append("词语辨析:");
				sb.append("\n");
				addContentAll(title,sb,sb_play);
			}
			Elements wordGroups = discriminate.select("div.collapse-content > div.wordGroup");
			if(wordGroups != null){
				for(Element item : wordGroups){
					addContentAll(item,sb,sb_play);
				}
			}
		}
		
		Elements examples = doc.select("div#examples > div#examplesToggle > div#bilingual > ul.ol > li");
		if(examples != null && examples.size() > 0){
			sb.append("\n");
			sb.append("双语例句:");
			sb.append("\n");
			for(Element item : examples){
				Elements pTags = item.getElementsByTag("p");
				if(pTags != null){
					if(pTags.size() > 1){
						for(int i=0; i<pTags.size()-1; i++){
							addContentAll(pTags.get(i),sb,sb_play);
						}
					}
				}
			}
		}
		
		Elements originalSounds = doc.select("div#examples > div#examplesToggle > div#originalSound > ul.ol > li");
		if(originalSounds != null && originalSounds.size() > 0){
			sb.append("\n");
			sb.append("原声例句:");
			sb.append("\n");
			for(Element item : originalSounds){
				Elements pTags = item.getElementsByTag("p");
				if(pTags != null){
					if(pTags.size() > 1){
						for(int i=0; i<pTags.size()-1; i++){
							addContentAll(pTags.get(i),sb,sb_play);
						}
					}
				}
			}
		}
		
		if(sb.length() > 0){
			mDictionary.setResult(sb.substring(0, sb.lastIndexOf("\n")));
			mDictionary.setBackup1(sb_play.substring(0, sb_play.lastIndexOf("\n")));
			DataBaseUtil.getInstance().insert(mDictionary);
			return mDictionary;
		}else{
			return null;
		}
	}
	
	public static void li_iteration(Element li,StringBuilder sb, StringBuilder sb_play){
		if(!TextUtils.isEmpty(li.ownText().trim())){
			addContentAll(li.ownText().trim(),sb,sb_play);
		}
		if(li.childNodeSize() > 0){
			for(Element lichild : li.children()){
				if(lichild.tagName().equals("ul") || lichild.tagName().equals("li")){
					li_iteration(lichild,sb,sb_play);
				}else{
					addContentAll(lichild,sb,sb_play);
				} 
			}
		}
	}
	
	public static void Translate_Biying_web(final Context mActivity, final Handler mHandler) {
		LanguagehelperHttpClient.get(Settings.BingyingWeb + Settings.q, new Callback() {
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Translate(mActivity,mHandler);
			}
			
			@Override
			public void onResponse(Response mResponse) throws IOException {
				try {
					if (mResponse.isSuccessful()) {
						String responseString = mResponse.body().string();
						LogUtil.DefalutLog("Result---bingyingweb");
//						LogUtil.DefalutLog("Result---bingyingweb:" + responseString);
						if (!TextUtils.isEmpty(responseString)) {
							Dictionary mDictionary = getParseBingyingWebHtml(responseString);
							if (mDictionary != null) {
								WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mDictionary);
								sendMessage(mHandler,1);
							}else{
								Translate(mActivity,mHandler);
							}
						}else{
							Translate(mActivity,mHandler);
						}
					}else{
						Translate(mActivity,mHandler);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Translate(mActivity,mHandler);
				}
			}
		});
	}
	
	public static Dictionary getParseBingyingWebHtml(String html){
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_play = new StringBuilder();
		Dictionary mDictionary = new Dictionary();
		boolean isEnglish = StringUtils.isEnglish(Settings.q);
		mDictionary.setType(KeyUtil.ResultTypeShowapi);
		if(isEnglish){
			mDictionary.setFrom("en");
			mDictionary.setTo("zh");
		}else{
			mDictionary.setFrom("zh");
			mDictionary.setTo("en");
		}
		mDictionary.setWord_name(Settings.q);
		sb_play.append(Settings.q);
		sb_play.append("\n");
		Document doc = Jsoup.parse(html);
		Element smt_hw = doc.select("div.smt_hw").first();
		if(smt_hw != null){
			Element p1_11 = doc.select("div.p1-11").first();
			if(p1_11 != null){
				addContentAll(p1_11,sb,sb_play);
			}
		}
		
		Element symblo = doc.select("div.hd_p1_1").first();
		if(symblo != null){
			addContent(symblo,sb);
		}
		Elements translates = doc.select("div.qdef > ul > li");
		if(translates != null && translates.size() > 0){
			for(Element li : translates){
				String content = li.text().trim();
				if(content.contains("网络")){
					content = content.replace("网络","网络：");
				}
				sb.append(content);
				sb.append("\n");
			}
		}
		Element fusu = doc.select("div.qdef > div.hd_div1 > div.hd_if").first();
		if(fusu != null){
			addContent(fusu,sb);
		}
		
		Elements dapeis = doc.select("div.wd_div > div#thesaurusesid > div#colid > div.df_div2");
		if(dapeis != null && dapeis.size() > 0){
			sb.append("\n");
			sb.append("搭配:");
			sb.append("\n");
			for(Element item : dapeis){
				addContentAll(item,sb,sb_play);
			}
		}
		
		Element authid = doc.select("div.df_div > div#defid > div#authid").first();
		if(authid != null){
			sb.append("\n");
			sb.append("权威英汉双解:");
			sb.append("\n");
			Element title = authid.select("div.hw_ti > div.hw_area2 > div.hd_div2 > span").first();
			if(title != null){
				addContentAll(title,sb,sb_play);
			}
			
			Elements each_seg = authid.select("div.li_sen > div.each_seg");
			for(Element item : each_seg){
				Element type = item.select("div.li_pos > div.pos_lin > div.pos").first();
				if(type != null){
					addContentAll(type,sb,sb_play);
				}
				Elements de_segs = item.select("div.li_pos > div.de_seg > div");
				if(de_segs != null && de_segs.size() > 0){
					for(Element se : de_segs){
						if(se.className().contains("se_lis")){
							addContentAll(se,sb,sb_play);
						}else if(se.className().contains("li_exs")){
							Elements li_exs = se.select("div.li_ex");
							if(li_exs != null && li_exs.size() > 0){
								sb.append("例句:");
								sb.append("\n");
								for(Element liex : li_exs){
									Elements exs = liex.select("div");
									if(exs != null && exs.size() > 1){
										addContentAll(exs.get(1),sb,sb_play);
										addContentAll(exs.get(2),sb,sb_play);
									}
								}
								sb.append("\n");
							}
						}
					}
				}
				Elements idm_s = item.select("div.li_id > div.idm_seg > div.idm_s");
				if(idm_s != null && idm_s.size() > 0){
					sb.append("IDM:");
					for(Element idm : idm_s){
						sb.append("\n");
						addContentAll(idm,sb,sb_play);
						Element li_ids_co = idm.nextElementSibling();
						if(li_ids_co.className().contains("li_ids_co")){
							Element idmdef_li = li_ids_co.select("div.li_sens > div.idmdef_li").first();
							if(idmdef_li != null){
								addContentAll(idmdef_li,sb,sb_play);
							}
							Element li_exs = li_ids_co.select("div.li_sens > div.li_exs").first();
							if(li_exs != null){
								Elements li_ex = li_exs.select("div.li_ex");
								sb.append("例句:");
								sb.append("\n");
								for(Element liex : li_ex){
									Elements exs = liex.select("div");
									if(exs != null && exs.size() > 1){
										addContentAll(exs.get(1),sb,sb_play);
										addContentAll(exs.get(2),sb,sb_play);
									}
								}
							}
						}
					}
				}
			}
		}
		
		Elements se_lis = doc.select("div#sentenceCon > div#sentenceSeg > div.se_li");
		if(se_lis != null && se_lis.size() > 0){
			sb.append("\n");
			sb.append("例句:");
			sb.append("\n");
			for(Element seli : se_lis){
				Element se_li1 = seli.select("div.se_li1").first();
				if(se_li1 != null){
					Elements selis = se_li1.select("div");
					if(selis != null && selis.size() > 1){
						addContentAll(selis.get(1),sb,sb_play);
						addContentAll(selis.get(2),sb,sb_play);
					}
				}
			}
		}
		
		if(sb.length() > 1){
			mDictionary.setResult(sb.substring(0, sb.lastIndexOf("\n")));
			mDictionary.setBackup1(sb_play.substring(0, sb_play.lastIndexOf("\n")));
			DataBaseUtil.getInstance().insert(mDictionary);
			return mDictionary;
		}else{
			return null;
		}
	}
	
	public static void addContent(Element title,StringBuilder sb){
		sb.append(title.text().trim());
		sb.append("\n");
	}
	
	public static void addContentAll(Element title,StringBuilder sb,StringBuilder sb_play){
		sb.append(title.text().trim());
		sb.append("\n");
		sb_play.append(title.text().trim());
		sb_play.append("\n");
	}
	
	public static void addContentAll(String title,StringBuilder sb,StringBuilder sb_play){
		sb.append(title);
		sb.append("\n");
		sb_play.append(title);
		sb_play.append("\n");
	}
	
	/**
	 * if dictionary api fail baidu translate api
	 */
	public static void Translate_baidu(final Context mActivity, final Handler mHandler) {
		LanguagehelperHttpClient.postBaidu(new Callback() {
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Translate(mActivity,mHandler);
			}
			@Override
			public void onResponse(Response mResponse) throws IOException {
				try {
					if (mResponse.isSuccessful()) {
						String responseString = mResponse.body().string();
						if (!TextUtils.isEmpty(responseString)) {
							LogUtil.DefalutLog("Result---baidu tran:" + responseString);
							String dstString = JsonParser.getTranslateResult(responseString);
							if (dstString.contains("error_msg:")) {
								LogUtil.DefalutLog("Result---baidu tran---error_msg:" + dstString);
								Translate(mActivity,mHandler);
							} else {
								Dictionary mDictionaryBean = new Dictionary();
								mDictionaryBean.setType(KeyUtil.ResultTypeTranslate);
								mDictionaryBean.setWord_name(Settings.q);
								mDictionaryBean.setResult(dstString);
								DataBaseUtil.getInstance().insert(mDictionaryBean);
								WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mDictionaryBean);
								sendMessage(mHandler,1);
							}
						} else {
							Translate(mActivity,mHandler);
						}
					}else{
						Translate(mActivity,mHandler);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Translate(mActivity,mHandler);
				}
			}
		});
	}
	
	public static void sendMessage(Handler mHandler,int result_code){
		if (mHandler != null) {
			Message msg = Message.obtain(mHandler, result_code);
			mHandler.sendMessage(msg);
		}
	}
	
	public static void saveTranslateApiStatus(String name, long status){
		if(apiOrder != null){
			for(TranslateApiBean bean : apiOrder){
				if(bean.getName().equals(name)){
					bean.setStatus(status);
				}
			}
		}
	}
	
	public static void saveTranslateApiOrder(SharedPreferences sp){
		if(apiOrder != null){
			String order = new Gson().toJson(apiOrder);
			LogUtil.DefalutLog("saveTranslateApiOrder:"+order);
			Settings.saveSharedPreferences(sp, KeyUtil.TranslateApiOrder, order);
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
		return EnWordTranslator.lookupNative(Settings.q);
	}

	public static Translate getSentenceTranslate(){
		EnLineTranslator.initDictPath(SDCardUtil.OfflineDicPath);
		return EnLineTranslator.lookup(Settings.q, LanguageConvert.AUTO);
	}

	public static boolean isOfflineTranslateWords(){
		if (StringUtils.isEnglish(Settings.q)) {
			if(Settings.q.split(" ").length > 2){
				return false;
			}else {
				return true;
			}
		}else {
			if(Settings.q.split("").length > 3){
				return false;
			}else {
				return true;
			}
		}
	}

	public static void addSymbol(Translate translate, StringBuilder sb){
		if(StringUtils.isEnglish(Settings.q)){
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
		Observable.create(new ObservableOnSubscribe<record>() {
			@Override
			public void subscribe(ObservableEmitter<record> e) throws Exception {
				e.onNext( doTranslateBackground() );
				e.onComplete();
			}
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<record>() {
					@Override
					public void onSubscribe(Disposable d) {
					}

					@Override
					public void onNext(record mResult) {
						listener.OnFinishTranslate(mResult);
					}

					@Override
					public void onError(Throwable e) {
					}

					@Override
					public void onComplete() {
					}
				});
	}

	private static record doTranslateBackground() {
		record result = null;
		Response mResponse = null;
		try {
			mResponse = LanguagehelperHttpClient.postIcibaNew(null);
			if(mResponse != null){
				result = tran_js_newapi(mResponse);
				if(result == null){
					mResponse = LanguagehelperHttpClient.postHjApi(null);
					result = tran_hj_api(mResponse);
					if(result == null){
						mResponse = LanguagehelperHttpClient.postBaidu(null);
						result = tran_bd_api(mResponse);
					}
				}
			}else {
				mResponse = LanguagehelperHttpClient.postBaidu(null);
				result = tran_bd_api(mResponse);
			}
		}catch (Exception e){
			LogUtil.DefalutLog("doTranslateBackground error");
			mResponse = LanguagehelperHttpClient.postBaidu(null);
			result = tran_bd_api(mResponse);
			e.printStackTrace();
		}
		return result;
	}

	private static record tran_bd_api(Response mResponse) {
		record currentDialogBean = null;
		try {
			if(mResponse != null && mResponse.isSuccessful()) {
				String mResult = mResponse.body().string();
				if (JsonParser.isJson(mResult)) {
					String dstString = JsonParser.getTranslateResult(mResult);
					if (!dstString.contains("error_msg:")) {
						currentDialogBean = new record(dstString, Settings.q);
						LogUtil.DefalutLog("tran_bd_api http:"+dstString);
					}
				}
			}
		}catch (Exception e){
			LogUtil.DefalutLog("tran_bd_api error");
			e.printStackTrace();
		}
		return currentDialogBean;
	}

	private static record tran_3g88_api(Response mResponse) throws Exception{
		record currentDialogBean = null;
		if(mResponse != null && mResponse.isSuccessful()) {
			String mResult = mResponse.body().string();
			if (!TextUtils.isEmpty(mResult)) {
				currentDialogBean = new record(mResult, Settings.q);
				LogUtil.DefalutLog("tran_3g88_api http:"+mResult);
			}
		}
		return currentDialogBean;
	}

	private static record tran_baiduv2_api(Response mResponse) throws Exception{
		record currentDialogBean = null;
		if(mResponse != null && mResponse.isSuccessful()) {
			String mResult = mResponse.body().string();
			LogUtil.DefalutLog("v2:"+mResult);
			BaiduV2Bean mBaiduV2Bean = JSON.parseObject(mResult, BaiduV2Bean.class);
			if (mBaiduV2Bean != null && mBaiduV2Bean.getTrans_result() != null &&
					mBaiduV2Bean.getTrans_result().getStatus() == 0) {
				currentDialogBean = new record(mBaiduV2Bean.getTrans_result().getData().get(0).getDst(), Settings.q);
				LogUtil.DefalutLog("tran_baiduv2_api http:"+mBaiduV2Bean.getTrans_result().getData().get(0).getDst());
			}
		}
		return currentDialogBean;
	}

	private static record tran_hj_api(Response mResponse) throws Exception{
		record currentDialogBean = null;
		if(mResponse != null && mResponse.isSuccessful()) {
			String mResult = mResponse.body().string();
			if (JsonParser.isJson(mResult)) {
				HjTranBean mHjTranBean = JSON.parseObject(mResult, HjTranBean.class);
				if (mHjTranBean != null && mHjTranBean.getStatus() == 0) {
					currentDialogBean = new record(mHjTranBean.getContent(), Settings.q);
					LogUtil.DefalutLog("tran_hj_api http:"+mHjTranBean.getContent());
				}
			}
		}
		return currentDialogBean;
	}

	private static record tran_js_newapi(Response mResponse) throws Exception{
		record currentDialogBean = null;
		if(mResponse != null && mResponse.isSuccessful()) {
			String mResult = mResponse.body().string();
			if (JsonParser.isJson(mResult)) {
				IcibaNew mIciba = JSON.parseObject(mResult, IcibaNew.class);
				if (mIciba != null && mIciba.getContent() != null) {
					if (mIciba.getStatus().equals("0")) {
						StringBuilder sb = new StringBuilder();
						StringBuilder sbplay = new StringBuilder();
						if (!TextUtils.isEmpty(mIciba.getContent().getPh_en())) {
							sb.append("英[");
							sb.append(mIciba.getContent().getPh_en());
							sb.append("]    ");
						}
						if (!TextUtils.isEmpty(mIciba.getContent().getPh_am())) {
							sb.append("美[");
							sb.append(mIciba.getContent().getPh_am());
							sb.append("]");
						}
						if (sb.length() > 0) {
							sb.append("\n");
						}
						if (mIciba.getContent().getWord_mean() != null) {
							for (String item : mIciba.getContent().getWord_mean()) {
								sb.append(item.trim());
								sb.append("\n");
								sbplay.append(item);
								sbplay.append(",");
							}
						}
						String resultStr = sbplay.toString();
						resultStr = resultStr.replace("n.", "");
						resultStr = resultStr.replace("adj.", "");
						resultStr = resultStr.replace("adv.", "");
						resultStr = resultStr.replace("vi.", "");
						resultStr = resultStr.replace("vt.", "");
						resultStr = resultStr.replace("v.", "");
						currentDialogBean = new record(sb.substring(0, sb.lastIndexOf("\n")), Settings.q);
						currentDialogBean.setBackup1(resultStr);
						if (!TextUtils.isEmpty(mIciba.getContent().getPh_tts_mp3())) {
							currentDialogBean.setBackup3(mIciba.getContent().getPh_tts_mp3());
						}
					} else if (mIciba.getStatus().equals("1")) {
						currentDialogBean = new record(mIciba.getContent().getOut().replaceAll("<br/>", "").trim(), Settings.q);
					}
				}
			}
		}
		LogUtil.DefalutLog("tran_js_newapi http:"+currentDialogBean.getEnglish());
		return currentDialogBean;
	}
	
}
