package com.messi.languagehelper.http;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.Cache;
import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.HttpUrl;
import com.avos.avoscloud.okhttp.Interceptor;
import com.avos.avoscloud.okhttp.MediaType;
import com.avos.avoscloud.okhttp.OkHttpClient;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.RequestBody;
import com.avos.avoscloud.okhttp.Response;
import com.messi.languagehelper.MainFragmentOld;
import com.messi.languagehelper.bean.BaiduAccessToken;
import com.messi.languagehelper.impl.ProgressListener;
import com.messi.languagehelper.util.CameraUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MD5;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.messi.languagehelper.util.Settings.from;
import static com.messi.languagehelper.util.Settings.to;

public class LanguagehelperHttpClient {
	
	public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
	private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
	public static final String Header = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36";
	public static OkHttpClient client = new OkHttpClient();
	
	public static OkHttpClient initClient(Context mContext){
		if(client ==  null){
			client = new OkHttpClient();
		}
		client.setConnectTimeout(15, TimeUnit.SECONDS);
		client.setWriteTimeout(15, TimeUnit.SECONDS);
		client.setReadTimeout(30, TimeUnit.SECONDS);
		File baseDir = mContext.getCacheDir();
		if(baseDir != null){
			File cacheDir = new File(baseDir,"HttpResponseCache");
			client.setCache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
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

	public static Response postBaidu(Callback mCallback) {
		long salt = System.currentTimeMillis();
		RequestBody formBody  = new FormEncodingBuilder()
			.add("appid", Settings.baidu_appid)
			.add("salt", String.valueOf(salt))
			.add("q", Settings.q)
			.add("from", from)
			.add("to", to)
			.add("sign", getBaiduTranslateSign(salt))
			.build();
		return post(Settings.baiduTranslateUrl,  formBody , mCallback);
	}

	public static Response postHjApi(Callback mCallback) {
		String from = "";
		String to = "";
		if (StringUtils.isEnglish(Settings.q)) {
			from = "en";
			to = "zh-CN";
		} else {
			from = "zh-CN";
			to = "en";
		}
		RequestBody formBody = new FormEncodingBuilder()
			.add("text", Settings.q)
			.add("from", from)
			.add("to", to)
			.build();
		Request request = new Request.Builder()
			.url(Settings.HjTranslateUrl)
			.header("User-Agent", Header)
			.post(formBody)
			.build();
		return executePost(request,mCallback);
	}

	public static Response postIcibaNew(Callback mCallback) {
		String from = "";
		String to = "";
		if (StringUtils.isEnglish(Settings.q)) {
			from = "en-US";
			to = "zh-CN";
		} else {
			from = "zh-CN";
			to = "en-US";
		}
		LogUtil.DefalutLog("from:"+from+"---to:"+to);
		RequestBody formBody = new FormEncodingBuilder()
			.add("w", Settings.q)
			.add("", "")
			.add("f", from)
			.add("t", to)
			.build();
		Request request = new Request.Builder()
			.url(Settings.IcibaTranslateNewUrl)
			.header("User-Agent", Header)
			.post(formBody)
			.build();
		return executePost(request,mCallback);
	}

	public static Response getAiyueyu(Callback mCallback) {
		LogUtil.DefalutLog("getAiyueyu");
		String type = "";
		if (Settings.to.equals(Settings.yue)) {
			type = "0";
		} else {
			type = "1";
		}
		RequestBody formBody = new FormEncodingBuilder()
				.add("type", type)
				.add("text", Settings.q)
				.build();
		Request request = new Request.Builder()
				.url(Settings.TranAiyueyuUrl)
				.header("User-Agent", Header)
				.post(formBody)
				.build();
		return executePost(request,mCallback);
	}

	public static Response getBaiduV2api(Callback mCallback) {
		String url = Settings.BaiduTranV2api + Settings.q;
		Request request = new Request.Builder()
				.url(url)
				.header("User-Agent", Header)
				.build();
		return executePost(request,mCallback);
	}

	public static void postBaiduOCR(Context context, String path, Callback mCallback) {
		try {
			String BaiduAccessToken = PlayUtil.getSP().getString(KeyUtil.BaiduAccessToken,"");
			long BaiduAccessTokenExpires = PlayUtil.getSP().getLong(KeyUtil.BaiduAccessTokenExpires,(long)0);
			long BaiduAccessTokenCreateAt = PlayUtil.getSP().getLong(KeyUtil.BaiduAccessTokenCreateAt,(long)0);
			boolean isExpired = System.currentTimeMillis() - BaiduAccessTokenCreateAt > BaiduAccessTokenExpires;
			if(!TextUtils.isEmpty(BaiduAccessToken) && !isExpired){
				File imageFile = new File(path);
				File tempImage = new File(context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
				CameraUtil.resize(imageFile.getAbsolutePath(), tempImage.getAbsolutePath(), 1280, 1280);

				MainFragmentOld.base64 = CameraUtil.encodeBase64File(tempImage);
				LogUtil.DefalutLog(MainFragmentOld.base64);
				RequestBody formBody = new FormEncodingBuilder()
						.add("image", MainFragmentOld.base64)
						.build();
				Request request = new Request.Builder()
						.url(Settings.BaiduOCRUrl+"?access_token="+PlayUtil.getSP().getString(KeyUtil.BaiduAccessToken,""))
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

	public static void getBaiduAccessToken(final Context context,final String path,final Callback mCallback){
		RequestBody formBody  = new FormEncodingBuilder()
				.add("grant_type", "client_credentials")
				.add("client_id", Settings.BaiduORCAK)
				.add("client_secret", Settings.BaiduORCSK)
				.build();
		post(Settings.BaiduAccessToken,  formBody ,new UICallback(WXEntryActivity.mInstance){
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
						Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessToken, mBaiduAccessToken.getAccess_token());
						Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessTokenExpires, mBaiduAccessToken.getExpires_in());
						Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessTokenCreateAt, System.currentTimeMillis());
					}
					postBaiduOCR(context,path,mCallback);
				}
			}
		});
	}
	
	public static OkHttpClient addProgressResponseListener(final ProgressListener progressListener){
        //克隆
		OkHttpClient clone = client.clone();
		// 增加拦截器
		clone.networkInterceptors().add(new Interceptor() {
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
		});
        return clone;
    }

	public static String getBaiduTranslateSign(long salt) {
		String str = Settings.baidu_appid + Settings.q + salt + Settings.baidu_secretkey;
		return MD5.encode(str);
	}

	public static void setTranslateLan(boolean isToYue){
		if(isToYue){
			Settings.from = Settings.zh;
			Settings.to = Settings.yue;
		}else{
			Settings.from = Settings.yue;
			Settings.to = Settings.zh;
		}
	}
}
