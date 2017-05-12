package com.messi.languagehelper.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.bean.DictionaryDataJuhe;
import com.messi.languagehelper.bean.DictionaryResultJuhe;
import com.messi.languagehelper.bean.DictionaryRootJuhe;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.bean.Root;
import com.messi.languagehelper.bean.Showapi_res_body;
import com.messi.languagehelper.bean.Web;
import com.messi.languagehelper.db.DataBaseUtil;

import android.text.TextUtils;


public class JsonParser {
	
	//showapi dictionary
	public static Dictionary changeShowapiResultToDicBean(Root mRoot, String query){
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_play = new StringBuilder();
		Dictionary mDictionary = new Dictionary();
		Showapi_res_body mShowapi_res_body = mRoot.getShowapi_res_body();
		mDictionary.setType(KeyUtil.ResultTypeShowapi);
		boolean isEnglish = StringUtils.isEnglish(query);
		sb_play.append(query);
		sb_play.append("\n");
		if(isEnglish){
			mDictionary.setFrom("en");
			mDictionary.setTo("zh");
		}else{
			mDictionary.setFrom("zh");
			mDictionary.setTo("en");
		}
		mDictionary.setWord_name(query);
		if(mShowapi_res_body.getBasic() != null){
			if(isEnglish){
				mDictionary.setPh_en(mShowapi_res_body.getBasic().getUk_phonetic());
				mDictionary.setPh_am(mShowapi_res_body.getBasic().getUs_phonetic());
				if(TextUtils.isEmpty(mDictionary.getPh_en())){
					mDictionary.setPh_en(mShowapi_res_body.getBasic().getPhonetic());
				}
				if(!TextUtils.isEmpty(mDictionary.getPh_am())){
					sb.append("美 /" + mDictionary.getPh_am() + "/      英 /" + mDictionary.getPh_en() + "/");
					sb.append("\n");
				}
			}else{
				mDictionary.setPh_zh(mShowapi_res_body.getBasic().getPhonetic());
				if(!TextUtils.isEmpty(mDictionary.getPh_zh())){
					sb.append("拼 /" + mDictionary.getPh_zh() + "/");
					sb.append("\n");
				}
			}
			if(mShowapi_res_body.getBasic().getExplains() != null){
				for(String item : mShowapi_res_body.getBasic().getExplains()){
					sb.append(item);
					sb.append("\n");
					sb_play.append(item);
					sb_play.append("\n");
				}
			}
		}
		if(mShowapi_res_body.getTranslations() != null){
			for(String item : mShowapi_res_body.getTranslations()){
				if(!sb.toString().contains(item)){
					sb.append(item);
					sb.append("\n");
					sb_play.append(item);
					sb_play.append("\n");
				}
			}
		}
		if(mShowapi_res_body.getWebs() != null){
			sb.append("网络例句：");
			sb.append("\n");
			for(Web mWeb : mShowapi_res_body.getWebs()){
				if(!TextUtils.isEmpty(mWeb.getKey())){
					sb.append(mWeb.getKey());
					sb.append("\n");
					sb_play.append(mWeb.getKey());
					sb_play.append("\n");
				}
				if(mWeb.getValues() != null){
					for(String value : mWeb.getValues()){
						sb.append(value);
						sb.append("\n");
						sb_play.append(value);
						sb_play.append("\n");
					}
				}
			}
		}
		if(sb_play.length() > 1){
			mDictionary.setResult(sb.substring(0, sb.lastIndexOf("\n")));
			String result_for_play = sb_play.substring(0, sb_play.lastIndexOf("\n"));
			result_for_play = result_for_play.replace("n.", "");
			result_for_play = result_for_play.replace("vt.", "");
			result_for_play = result_for_play.replace("vi.", "");
			result_for_play = result_for_play.replace("adj.", "");
			result_for_play = result_for_play.replace("adv.", "");
			result_for_play = result_for_play.replace("prep.", "");
			mDictionary.setBackup1(result_for_play);
			DataBaseUtil.getInstance().insert(mDictionary);
		}
		return mDictionary;
	}
	
	public static Dictionary changeJuheResultToDicBean(DictionaryRootJuhe mRoot, String query){
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_play = new StringBuilder();
		Dictionary mDictionary = new Dictionary();
		DictionaryResultJuhe mResult = mRoot.getResult();
		mDictionary.setType(KeyUtil.ResultTypeShowapi);
		boolean isEnglish = StringUtils.isEnglish(query);
		sb_play.append(query);
		sb_play.append("\n");
		if(isEnglish){
			mDictionary.setFrom("en");
			mDictionary.setTo("zh");
		}else{
			mDictionary.setFrom("zh");
			mDictionary.setTo("en");
		}
		mDictionary.setWord_name(query);
		DictionaryDataJuhe mdata = mResult.getData(); 
		if(mdata != null && mdata.getBasic() != null){
			if(isEnglish){
				mDictionary.setPh_en(mdata.getBasic().getUk_phonetic());
				mDictionary.setPh_am(mdata.getBasic().getUs_phonetic());
				if(TextUtils.isEmpty(mDictionary.getPh_en())){
					mDictionary.setPh_en(mdata.getBasic().getPhonetic());
				}
				if(!TextUtils.isEmpty(mDictionary.getPh_am())){
					sb.append("美 /" + mDictionary.getPh_am() + "/      英 /" + mDictionary.getPh_en() + "/");
					sb.append("\n");
				}
			}else{
				mDictionary.setPh_zh(mdata.getBasic().getPhonetic());
				if(!TextUtils.isEmpty(mDictionary.getPh_zh())){
					sb.append("拼 /" + mDictionary.getPh_zh() + "/");
					sb.append("\n");
				}
			}
			if(mdata.getBasic().getExplains() != null){
				for(String item : mdata.getBasic().getExplains()){
					sb.append(item);
					sb.append("\n");
					sb_play.append(item);
					sb_play.append("\n");
				}
			}
		}
		if(mdata.getTranslations() != null){
			for(String item : mdata.getTranslations()){
				if(!sb.toString().contains(item)){
					sb.append(item);
					sb.append("\n");
					sb_play.append(item);
					sb_play.append("\n");
				}
			}
		}
		if(mdata.getWebs() != null){
			sb.append("网络例句：");
			sb.append("\n");
			for(Web mWeb : mdata.getWebs()){
				if(!TextUtils.isEmpty(mWeb.getKey())){
					sb.append(mWeb.getKey());
					sb.append("\n");
					sb_play.append(mWeb.getKey());
					sb_play.append("\n");
				}
				if(mWeb.getValues() != null){
					for(String value : mWeb.getValues()){
						sb.append(value);
						sb.append("\n");
						sb_play.append(value);
						sb_play.append("\n");
					}
				}
			}
		}
		if(sb_play.length() > 1){
			mDictionary.setResult(sb.substring(0, sb.lastIndexOf("\n")));
			String result_for_play = sb_play.substring(0, sb_play.lastIndexOf("\n"));
			result_for_play = result_for_play.replace("n.", "");
			result_for_play = result_for_play.replace("vt.", "");
			result_for_play = result_for_play.replace("vi.", "");
			result_for_play = result_for_play.replace("adj.", "");
			result_for_play = result_for_play.replace("adv.", "");
			result_for_play = result_for_play.replace("prep.", "");
			mDictionary.setBackup1(result_for_play);
			DataBaseUtil.getInstance().insert(mDictionary);
		}
		return mDictionary;
	}

	//{"from":"en","to":"zh","trans_result":[{"src":"cold","dst":"\u51b7"}]}
	public static String getTranslateResult(String jsonString){
		try {
			JSONObject jObject = new JSONObject(jsonString);
			if(jObject.has("error_code")){
				return "Error:"+jObject.getString("error_msg");
			}else{
				if(jObject.has("trans_result")){
					JSONArray jArray = new JSONArray(jObject.getString("trans_result"));
					int len = jArray.length();
					if(len >= 1){
						JSONObject jaObject = jArray.getJSONObject(0);
						if(jaObject.has("dst")){
							 String tempString = jaObject.getString("dst");
							 return UnicodeToStr.decodeUnicode(tempString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 听写结果的Json格式解析
	 * @param json
	 * @return
	 */
	public static String parseIatResult(String json) {
		if(TextUtils.isEmpty(json))
			return "";
		
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 听写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret.toString();
	}
	
	/**
	 * 识别结果的Json格式解析
	 * @param json
	 * @return
	 */
	public static String parseGrammarResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				for(int j = 0; j < items.length(); j++)
				{
					JSONObject obj = items.getJSONObject(j);
					if(obj.getString("w").contains("nomatch"))
					{
						ret.append("没有匹配结果.");
						return ret.toString();
					}
					ret.append("【结果】" + obj.getString("w"));
					ret.append("【置信度】" + obj.getInt("sc"));
					ret.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("没有匹配结果.");
		} 
		return ret.toString();
	}
	
	/**
	 * 语义结果的Json格式解析
	 * @param json
	 * @return
	 */
	public static String parseUnderstandResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			ret.append("【应答码】" + joResult.getString("rc") + "\n");
			ret.append("【转写结果】" + joResult.getString("text") + "\n");
			ret.append("【服务名称】" + joResult.getString("service") + "\n");
			ret.append("【操作名称】" + joResult.getString("operation") + "\n");
			ret.append("【完整结果】" + json);
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("没有匹配结果.");
		} 
		return ret.toString();
	}
	
	public static EveryDaySentence parseEveryDaySentence(String result){
		LogUtil.DefalutLog("parseEveryDaySentence:"+result);
		EveryDaySentence bean = new EveryDaySentence();
		JSONArray tags = null;
		try {
			JSONObject jObject = new JSONObject(result);
			if(jObject.has("sid")){
				bean.setSid(jObject.getString("sid"));
			}
			if(jObject.has("tts")){
				bean.setTts(jObject.getString("tts"));
			}
			if(jObject.has("content")){
				bean.setContent(jObject.getString("content")); 
			}
			if(jObject.has("note")){
				bean.setNote(jObject.getString("note"));
			}
			if(jObject.has("love")){
				bean.setLove(jObject.getString("love"));
			}
			if(jObject.has("translation")){
				bean.setTranslation(jObject.getString("translation"));
			}
			if(jObject.has("picture")){
				bean.setPicture(jObject.getString("picture")); 
			}
			if(jObject.has("picture2")){
				bean.setPicture2(jObject.getString("picture2")); 
			}
			if(jObject.has("caption")){
				bean.setCaption(jObject.getString("caption")); 
			}
			if(jObject.has("dateline")){
				String dateline = jObject.getString("dateline");
				bean.setDateline(dateline); 
				String temp = dateline.replaceAll("-", "");
				long cid = NumberUtil.StringToLong(temp);
				bean.setCid(cid);
			}
			if(jObject.has("s_pv")){
				bean.setS_pv(jObject.getString("s_pv")); 
			}
			if(jObject.has("sp_pv")){
				bean.setSp_pv(jObject.getString("sp_pv")); 
			}
			if(jObject.has("fenxiang_img")){
				bean.setFenxiang_img(jObject.getString("fenxiang_img")); 
			}
//			if(jObject.has("tags")){
//				if(!TextUtils.isEmpty(jObject.getString("tags"))){
//					tags = jObject.getJSONArray("tags");
//				}
//			}
			if(bean != null){
				DataBaseUtil.getInstance().insert(bean);
//				long everyDaySentenceId = bean.getId();
//				if(everyDaySentenceId >= 0 && tags != null){
//					for(int i=0; i<tags.length(); i++){
//						Tag mTag = new Tag();
//						JSONObject tag = tags.getJSONObject(i);
//						mTag.setName(tag.getString("name"));
//						mTag.setEveryDaySentenceId(everyDaySentenceId);
//						DataBaseUtil.getInstance().insert(mTag);
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public static boolean isJson(String value) { 
		try {
			if (TextUtils.isEmpty(value)) {
				return false;
			}else {
				new JSONObject(value);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
