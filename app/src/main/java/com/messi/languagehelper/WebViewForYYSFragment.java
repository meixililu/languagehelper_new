package com.messi.languagehelper;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class WebViewForYYSFragment extends BaseFragment{

	private WebView mWebView;
	private String adFilte;
	private String ad_url;

    public static WebViewForYYSFragment getInstance(FragmentProgressbarListener listener){
		WebViewForYYSFragment mMainFragment = new WebViewForYYSFragment();
		mMainFragment.setmProgressbarListener(listener);
		return mMainFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater,container,savedInstanceState);
		View view = inflater.inflate(R.layout.web_view_for_yys_fragment, null);
		initViews(view);
		getFilter();
		return view;
	}
	
	private void initViews(View view){
		mWebView = (WebView) view.findViewById(R.id.refreshable_webview);
		mWebView.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
		mWebView.getSettings().setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript。
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setBlockNetworkImage(false);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
			mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				LogUtil.DefalutLog("WebViewClient:onPageStarted");
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				LogUtil.DefalutLog("failingUrl:"+failingUrl);
				view.goBack();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				hideProgressbar();
				hideAd(view);
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
				LogUtil.DefalutLog("uploadLogAdSDK");
				LogUtil.DefalutLog(url);
				int action = getAdAction(url);
				if(action == 1){
					return new WebResourceResponse(null, null, null);
				}else if(action == 2){
					return new WebResourceResponse(null, null, null);
				}
				return super.shouldInterceptRequest(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.contains("openapp.jdmobile") || url.contains("taobao")){
					Uri uri = Uri.parse(url);
					view.loadUrl("");
					ADUtil.toAdActivity(getContext(),uri);
					getActivity().finish();
					return true;
				}else if(url.contains("bilibili:")){
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedSslError(WebView view,final SslErrorHandler handler, SslError error) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				String message = "SSL Certificate error.";
				switch (error.getPrimaryError()) {
					case SslError.SSL_UNTRUSTED:
						message = "The certificate authority is not trusted.";
						break;
					case SslError.SSL_EXPIRED:
						message = "The certificate has expired.";
						break;
					case SslError.SSL_IDMISMATCH:
						message = "The certificate Hostname mismatch.";
						break;
					case SslError.SSL_NOTYETVALID:
						message = "The certificate is not yet valid.";
						break;
				}
				message += " Do you want to continue anyway?";
				builder.setTitle("SSL Certificate Error");
				builder.setMessage(message);
				builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.proceed();
					}
				});
				builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.cancel();
					}
				});
				final AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}

	private void getFilter(){
		LogUtil.DefalutLog("----------------getFilter---------");
		try {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.AdFilter.AdFilter);
			query.whereEqualTo(AVOUtil.AdFilter.name, "粤语发音词典");
			query.findInBackground(new FindCallback<AVObject>() {
				@Override
				public void done(List<AVObject> list, AVException e) {
					if(list != null){
						AVObject mAVObject = list.get(0);
						if(mAVObject != null){
							adFilte = mAVObject.getString(AVOUtil.AdFilter.filter);
							ad_url = mAVObject.getString(AVOUtil.AdFilter.ad_url);
							LogUtil.DefalutLog("adFilte:"+adFilte+"-------"+"ad_url:"+ad_url);
							hideAd(mWebView);
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void submit() {
		try {
			String url = "http://m.yueyv.cn/?submit=&keyword=" + URLEncoder.encode(Setings.q,"gb2312");
			mWebView.loadUrl(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setmProgressbarListener(FragmentProgressbarListener listener){
		mProgressbarListener = listener;
	}

	public void showProgressbar(){
		if(mProgressbarListener != null){
			mProgressbarListener.showProgressbar();
		}
	}

	public void hideProgressbar(){
		if(mProgressbarListener != null){
			mProgressbarListener.hideProgressbar();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
	}

	private void hideAd(final WebView view){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!TextUtils.isEmpty(adFilte)){
					String[] filters = adFilte.split("#");
					if(filters != null && filters.length > 0){
						for(final String items : filters){
							if(!TextUtils.isEmpty(items)){
								if(items.startsWith("id:") || items.startsWith("class:") ){
									String[] item = items.split(":");
									if(item != null && item.length > 1){
										if(item[0].equals("id")){
											view.loadUrl(
													"javascript:(function() { " +
															"var element = document.getElementById('"+item[1]+"');"
															+ "element.parentNode.removeChild(element);" + "})()");
										}else if(item[0].equals("class")){
											view.loadUrl(
													"javascript:(function() { " +
															"var element = document.getElementsByClassName('"+item[1]+"')[0];"
															+ "element.parentNode.removeChild(element);" + "})()");
										}
									}
								}else {
									view.loadUrl(items);
								}

							}
						}
					}
				}
			}
		},60);
	}

	private int getAdAction(String url){
		int action = 0;
		if(!TextUtils.isEmpty(ad_url)){
			String[] filters = ad_url.split("#");
			if(filters != null && filters.length > 0){
				for(final String item : filters) {
					String[] fls = item.split(":");
					if(fls != null && fls.length == 2){
						if(url.contains(fls[0])){
							action = Integer.parseInt(fls[1]);
						}
					}
				}
			}
		}
		return action;
	}
}
