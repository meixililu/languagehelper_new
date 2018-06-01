package com.messi.languagehelper;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.LogUtil;


public class WebViewFragment extends BaseFragment{

	private TextView tap_to_reload;
	private ProgressBar progressdeterminate;
	private WebView mWebView;
	private long lastClick;
    private String url;
    
    public static WebViewFragment getInstance(String url){
		WebViewFragment mMainFragment = new WebViewFragment();
		mMainFragment.url = url;
		return mMainFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater,container,savedInstanceState);
		View view = inflater.inflate(R.layout.web_view_fragment, null);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		initSwipeRefresh(view);
		mWebView = (WebView) view.findViewById(R.id.refreshable_webview);
		progressdeterminate = (ProgressBar) view.findViewById(R.id.progressdeterminate);
		tap_to_reload = (TextView) view.findViewById(R.id.tap_to_reload);
		mWebView.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
		mWebView.getSettings().setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript。
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setBlockNetworkImage(false);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
			mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}

		tap_to_reload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl(url);
				tap_to_reload.setVisibility(View.GONE);
				lastClick = System.currentTimeMillis();
			}
		});
		//当前页面加载
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
				if(failingUrl.contains("openapp.jdmobile") || failingUrl.contains("taobao")){
					Uri uri = Uri.parse(failingUrl);
					view.loadUrl("");
					ADUtil.toAdActivity(getContext(),uri);
					getActivity().finish();
				}else {
					view.loadUrl("");
					if(System.currentTimeMillis() - lastClick < 500){
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								tap_to_reload.setVisibility(View.VISIBLE);
							}
						}, 600);
					}else{
						tap_to_reload.setVisibility(View.VISIBLE);
					}
				}
				LogUtil.DefalutLog("WebViewClient:onReceivedError---"+errorCode);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mSwipeRefreshLayout.setRefreshing(false);
				LogUtil.DefalutLog("WebViewClient:onPageFinished");
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

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressdeterminate.setVisibility(View.GONE);
					mSwipeRefreshLayout.setRefreshing(false);
					LogUtil.DefalutLog("WebViewClient:newProgress == 100");
				} else {
					if (progressdeterminate.getVisibility() == View.GONE)
						progressdeterminate.setVisibility(View.VISIBLE);
					progressdeterminate.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}

	@Override
	public void onSwipeRefreshLayoutRefresh() {
		super.onSwipeRefreshLayoutRefresh();
		mWebView.reload();
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		mWebView.loadUrl(url);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mWebView != null){
			if(mWebView.canGoBack()){
				mWebView.goBack();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
	}
}
