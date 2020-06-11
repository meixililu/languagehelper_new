package com.messi.languagehelper.http;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.BaiduAccessToken;
import com.messi.languagehelper.impl.ProgressListener;
import com.messi.languagehelper.util.CameraUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MD5;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.TranslateHelper;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LanguagehelperHttpClient {
	
	public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
	private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
	public static final String Header = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36";
	public static OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(15, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(15, TimeUnit.SECONDS)
			.build();
	
	public static OkHttpClient initClient(Context mContext){
		File baseDir = mContext.getCacheDir();
		File cacheDir = new File(baseDir,"HttpResponseCache");
		if(cacheDir != null){
			client = new OkHttpClient.Builder()
					.connectTimeout(15, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS)
					.writeTimeout(15, TimeUnit.SECONDS)
					.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE))
					.build();
		}
		return client;
	}

	public static Response get(String url) {
		Response mResponse = null;
		try {
			Request request = new Request.Builder()
					.url(url)
					.build();
			mResponse = client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResponse;
	}
	
	public static void get(String url, Callback mCallback) {
		Request request = new Request.Builder()
			.url(url)
			.header("User-Agent", Header)
			.build();
		client.newCall(request).enqueue(mCallback);
	}

	public static void get(Request request, Callback mCallback) {
		client.newCall(request).enqueue(mCallback);
	}

	public static Response get(String url,ProgressListener progressListener) {
		Response mResponse = null;
		try {
			Request request = new Request.Builder()
					.url(url)
					.build();
			OkHttpClient clone = LanguagehelperHttpClient.addProgressResponseListener(progressListener);
			mResponse = clone.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResponse;
	}

	public static void get(HttpUrl mHttpUrl, String apikey, Callback mCallback) {
		if (TextUtils.isEmpty(apikey)) {
			Request request = new Request.Builder()
					.url(mHttpUrl)
					.header("User-Agent", Header)
					.build();
			client.newCall(request).enqueue(mCallback);
		}else {
			Request request = new Request.Builder()
					.url(mHttpUrl)
					.addHeader("apikey",apikey)
					.header("User-Agent", Header)
					.build();
			client.newCall(request).enqueue(mCallback);
		}
	}

	public static Response post(String url, RequestBody params, Callback mCallback) {
		Request request = new Request.Builder()
				.url(url)
				.post(params)
				.build();
		return executePost(request,mCallback);
	}

	public static Response postWithHeader(String url, RequestBody params, Callback mCallback) {
		Request request = new Request.Builder()
				.header("User-Agent", Header)
				.url(url)
				.post(params)
				.build();
		return executePost(request,mCallback);
	}

	public static Response executePost(Request request,Callback mCallback){
		Response mResponse = null;
		if(mCallback != null){
			client.newCall(request).enqueue(mCallback);
		}else {
			try {
				mResponse = client.newCall(request).execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mResponse;
	}

	public static Response postTranQQAILabAPi(Callback mCallback) {
		String time_stamp = String.valueOf(System.currentTimeMillis()/1000);
		String nonce_str = StringUtils.getRandomString(16);
		String type = "0";//9中文转粤语 10粤语转中文

		Map<String, String> map = null;
		try {
			map = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					return str1.compareTo(str2);
				}
			});
			map.put("app_id", URLEncoder.encode(Setings.QQAPPID,"UTF-8"));
			map.put("nonce_str",URLEncoder.encode(nonce_str,"UTF-8"));
			map.put("text",URLEncoder.encode(Setings.q,"UTF-8"));
			map.put("time_stamp",URLEncoder.encode(String.valueOf(time_stamp),"UTF-8"));
			map.put("type",URLEncoder.encode(type,"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sign = getSortData(map);
		FormBody formBody = new FormBody.Builder()
				.add("app_id", Setings.QQAPPID)
				.add("time_stamp", time_stamp)
				.add("nonce_str", nonce_str)
				.add("sign", sign)
				.add("type", type)
				.add("text", Setings.q)
				.build();
		return post(Setings.QQTranAILabApi,  formBody , mCallback);
	}

	public static Response postTranQQFYJAPi(Callback mCallback) {
		String time_stamp = String.valueOf(System.currentTimeMillis()/1000);
		String nonce_str = StringUtils.getRandomString(16);
		String source = "";
		String target = "";
		if (StringUtils.isEnglish(Setings.q)) {
			source = "en";
			target = "zh";
		} else {
			source = "zh";
			target = "en";
		}
		Map<String, String> map = null;
		try {
			map = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					return str1.compareTo(str2);
				}
			});
			map.put("app_id", URLEncoder.encode(Setings.QQAPPID,"UTF-8"));
			map.put("nonce_str",URLEncoder.encode(nonce_str,"UTF-8"));
			map.put("text",URLEncoder.encode(Setings.q,"UTF-8"));
			map.put("time_stamp",URLEncoder.encode(String.valueOf(time_stamp),"UTF-8"));
			map.put("source",URLEncoder.encode(source,"UTF-8"));
			map.put("target",URLEncoder.encode(target,"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sign = getSortData(map);
		FormBody formBody = new FormBody.Builder()
				.add("app_id", Setings.QQAPPID)
				.add("time_stamp", time_stamp)
				.add("nonce_str", nonce_str)
				.add("sign", sign)
				.add("source", source)
				.add("target", target)
				.add("text", Setings.q)
				.build();
		return post(Setings.QQTranFYJApi,  formBody , mCallback);
	}

	public static Response postHjApi(Callback mCallback) {
		//cn en jp(Japanese) kr(Korean) fr(French) de(German) th(Thai)
		//es(Spaish) ru(Russian) pt(Portuguese) it(Italian)
		String from = "";
		String to = "";
		if (StringUtils.isEnglish(Setings.q)) {
			from = "/en";
			to = "/cn";
		} else {
			from = "/cn";
			to = "/en";
		}
		String url = Setings.HjTranslateUrl + from + to;
		LogUtil.DefalutLog("HjTranslateUrl:"+url);
		FormBody formBody = new FormBody.Builder()
			.add("content", Setings.q)
			.build();
		Request request = new Request.Builder()
				.url(url)
				.header("User-Agent", Header)
				.header("Cookie", TranslateHelper.HJCookie)
				.post(formBody)
				.build();
		return executePost(request,mCallback);
	}

	public static Response postIcibaNew(Callback mCallback) {
		//zh  en  ja  ko  fr  de  es
		String from = "";
		String to = "";
		if (StringUtils.isEnglish(Setings.q)) {
			from = "en";
			to = "zh";
		} else {
			from = "zh";
			to = "en";
		}
		LogUtil.DefalutLog("from:"+from+"---to:"+to);
		FormBody formBody = new FormBody.Builder()
			.add("w", Setings.q)
			.add("", "")
			.add("f", from)
			.add("t", to)
			.build();
		Request request = new Request.Builder()
			.url(Setings.IcibaTranslateNewUrl)
			.header("User-Agent", Header)
			.post(formBody)
			.build();
		return executePost(request,mCallback);
	}

	public static Response getAiyueyu(Callback mCallback) {
		LogUtil.DefalutLog("getAiyueyu");
		String type = "";
		if (Setings.to.equals(Setings.yue)) {
			type = "0";
		} else {
			type = "1";
		}
		LogUtil.DefalutLog("type:"+type);
		FormBody formBody = new FormBody.Builder()
				.add("type", type)
				.add("text", Setings.q)
				.build();
		Request request = new Request.Builder()
				.url(Setings.TranAiyueyuUrl)
				.header("User-Agent", Header)
				.post(formBody)
				.build();
		return executePost(request,mCallback);
	}

	public static void postBaiduOCR(Activity context, String path, Callback mCallback) {
		try {
			String BaiduAccessToken = PlayUtil.getSP().getString(KeyUtil.BaiduAccessToken,"");
			long BaiduAccessTokenExpires = PlayUtil.getSP().getLong(KeyUtil.BaiduAccessTokenExpires,(long)0);
			long BaiduAccessTokenCreateAt = PlayUtil.getSP().getLong(KeyUtil.BaiduAccessTokenCreateAt,(long)0);
			boolean isExpired = System.currentTimeMillis() - BaiduAccessTokenCreateAt > BaiduAccessTokenExpires;
			if(!TextUtils.isEmpty(BaiduAccessToken) && !isExpired){
				String base64 = CameraUtil.getImageBase64(path,1280,1280,4000);
				FormBody formBody = new FormBody.Builder()
						.add("image", base64)
						.build();
				Request request = new Request.Builder()
						.url(Setings.BaiduOCRUrl+"?access_token="+PlayUtil.getSP().getString(KeyUtil.BaiduAccessToken,""))
						.header("Content-Type", "application/x-www-form-urlencoded")
						.post(formBody)
						.build();
				client.newCall(request).enqueue(mCallback);
			}else {
				getBaiduAccessToken(context,path,mCallback);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getBaiduAccessToken(final Activity context,final String path,final Callback mCallback){
		FormBody formBody = new FormBody.Builder()
				.add("grant_type", "client_credentials")
				.add("client_id", Setings.BaiduORCAK)
				.add("client_secret", Setings.BaiduORCSK)
				.build();
		post(Setings.BaiduAccessToken,  formBody ,new UICallback(context){
			@Override
			public void onFailured() {
			}
			@Override
			public void onFinished() {
			}
			@Override
			public void onResponsed(String responseString) {
				if(JsonParser.isJson(responseString)){
					BaiduAccessToken mBaiduAccessToken = JSON.parseObject(responseString, BaiduAccessToken.class);
					if(mBaiduAccessToken != null){
						Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessToken, mBaiduAccessToken.getAccess_token());
						Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessTokenExpires, mBaiduAccessToken.getExpires_in());
						Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessTokenCreateAt, System.currentTimeMillis());
					}
					postBaiduOCR(context,path,mCallback);
				}
			}
		});
	}
	
	public static OkHttpClient addProgressResponseListener(final ProgressListener progressListener){
		OkHttpClient clone = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				// 拦截
				Response originalResponse = chain.proceed(chain.request());
				// 包装响应体并返回
				return originalResponse
						.newBuilder()
						.body(new ProgressResponseBody(originalResponse.body(), progressListener))
						.build();
			}
		}).build();
		return clone;
    }

	public static String getSortData(Map<String, String> map){
		String result = "";
		if(map != null){
			for(Map.Entry<String,String> entry : map.entrySet()){
				result += entry.getKey() + "=" + entry.getValue() + "&";
			}
			result += "app_key="+ Setings.QQAPPKEY;
			LogUtil.DefalutLog("result:"+result);
			result = MD5.encode(result).toUpperCase();
			LogUtil.DefalutLog("result:"+result);
		}
		return result;
	}

	public static void setTranslateLan(boolean isToYue){
		if(isToYue){
			Setings.from = Setings.zh;
			Setings.to = Setings.yue;
		}else{
			Setings.from = Setings.yue;
			Setings.to = Setings.zh;
		}
	}

	private static String getPara(TreeMap<String,String> map){
		StringBuilder sb = new StringBuilder();
		for (Map.Entry entry : map.entrySet()) {
			sb = sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		String para = sb.substring(0,sb.lastIndexOf("&"));
		LogUtil.DefalutLog("pinjie para:"+para);
		return para;
	}
}
