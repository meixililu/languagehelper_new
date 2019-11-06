package com.messi.languagehelper;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ShareUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.views.VideoEnabledWebChromeClient;
import com.messi.languagehelper.views.VideoEnabledWebView;

import java.util.List;


public class WebViewFullscreenActivity extends BaseActivity{

	private final String STATE_RESUME_WINDOW = "resumeWindow";
	private final String STATE_RESUME_POSITION = "resumePosition";
	private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

	private ProgressBar progressdeterminate;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private VideoEnabledWebView mWebView;
	private VideoEnabledWebChromeClient webChromeClient;
	private TextView tap_to_reload;
	private RelativeLayout nonVideoLayout;
	private RelativeLayout videoLayout;
    private String Url;
    private String title;
    private String ShareUrlMsg;
    private int ToolbarBackgroundColor;
    private boolean isReedPullDownRefresh;
    private boolean isHideToolbar;
    private long lastClick;
	private String adFilte;
	private boolean is_need_get_filter;
	private String filter_source_name;

	private boolean mExoPlayerFullscreen = false;
	private int mResumeWindow;
	private long mResumePosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_fullscreen_activity);
		setStatusbarColor(R.color.white);
		changeStatusBarTextColor(true);
		if (savedInstanceState != null) {
			mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
			mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
			mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
		}
		initData();
		initViews();
	}
	
	private void initData(){
		Url = getIntent().getStringExtra(KeyUtil.URL);
		title = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
		ShareUrlMsg = getIntent().getStringExtra(KeyUtil.ShareUrlMsg);
		adFilte = getIntent().getStringExtra(KeyUtil.AdFilter);
		filter_source_name = getIntent().getStringExtra(KeyUtil.FilterName);
		is_need_get_filter = getIntent().getBooleanExtra(KeyUtil.IsNeedGetFilter,false);
		ToolbarBackgroundColor = getIntent().getIntExtra(KeyUtil.ToolbarBackgroundColorKey,0);
		isReedPullDownRefresh = getIntent().getBooleanExtra(KeyUtil.IsReedPullDownRefresh, true);
		isHideToolbar = getIntent().getBooleanExtra(KeyUtil.IsHideToolbar, false);
		LogUtil.DefalutLog("ToolbarBackgroundColor:"+ToolbarBackgroundColor);
		if(ToolbarBackgroundColor != 0){
			setToolbarBackground(ToolbarBackgroundColor);
		}
		if(!TextUtils.isEmpty(title)){
			getSupportActionBar().setTitle(title);
		}
		if (isHideToolbar) {
			getSupportActionBar().hide();
		}
		if(is_need_get_filter){
			getFilter();
		}
	}

	private void getFilter(){
		if(!TextUtils.isEmpty(filter_source_name)){
			try {
				AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.AdFilter.AdFilter);
				query.whereEqualTo(AVOUtil.AdFilter.name, filter_source_name);
				query.findInBackground(new FindCallback<AVObject>() {
					@Override
					public void done(List<AVObject> list, AVException e) {
						if(list != null){
							AVObject mAVObject = list.get(0);
							if(mAVObject != null){
								adFilte = mAVObject.getString(AVOUtil.AdFilter.filter);
								hideAd(mWebView);
							}
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initViews(){
		nonVideoLayout = (RelativeLayout) findViewById(R.id.nonVideoLayout);
		videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		progressdeterminate = (ProgressBar) findViewById(R.id.progressdeterminate);
		mWebView = (VideoEnabledWebView) findViewById(R.id.refreshable_webview);
		tap_to_reload = (TextView) findViewById(R.id.tap_to_reload);
		View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null);
		setScrollable(mSwipeRefreshLayout);
		mWebView.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
		mWebView.getSettings().setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript。
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setBlockNetworkImage(false);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
			mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}

		tap_to_reload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl(Url);
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
					ADUtil.toAdActivity(WebViewFullscreenActivity.this,uri);
					WebViewFullscreenActivity.this.finish();
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
				hideAd(view);
				LogUtil.DefalutLog("WebViewClient:onPageFinished");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				LogUtil.DefalutLog(url);
				if(url.contains("openapp.jdmobile") || url.contains("taobao")){
					Uri uri = Uri.parse(url);
					view.loadUrl("");
					ADUtil.toAdActivity(WebViewFullscreenActivity.this,uri);
					WebViewFullscreenActivity.this.finish();
					return true;
				}else if(url.contains("bilibili:")){
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedSslError(WebView view,final SslErrorHandler handler, SslError error) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewFullscreenActivity.this);
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
		webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, mWebView) // See all available constructors...
		{
			// Subscribe to standard events, such as onProgressChanged()...
			@Override
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
					progressdeterminate.setVisibility(View.GONE);
					mSwipeRefreshLayout.setRefreshing(false);
					LogUtil.DefalutLog("WebViewClient:newProgress == 100");
				} else {
					if (progressdeterminate.getVisibility() == View.GONE)
						progressdeterminate.setVisibility(View.VISIBLE);
					progressdeterminate.setProgress(progress);
				}
				super.onProgressChanged(view, progress);
			}
		};
		webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
		   @Override
		   public void toggledFullscreen(boolean fullscreen) {
			   // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
			   if (fullscreen) {
				   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				   WindowManager.LayoutParams attrs = getWindow().getAttributes();
				   attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
				   attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				   getWindow().setAttributes(attrs);
				   if (android.os.Build.VERSION.SDK_INT >= 14) {
					   //noinspection all
					   getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
				   }
			   } else {
				   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				   WindowManager.LayoutParams attrs = getWindow().getAttributes();
				   attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
				   attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				   getWindow().setAttributes(attrs);
				   if (android.os.Build.VERSION.SDK_INT >= 14) {
					   //noinspection all
					   getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
				   }
			   }

		   }});
		mWebView.setWebChromeClient(webChromeClient);
		
		mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, 
	            R.color.holo_green_light, 
	            R.color.holo_orange_light, 
	            R.color.holo_red_light);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mWebView.reload();
			}
		});
		if(!isReedPullDownRefresh){
			mSwipeRefreshLayout.setEnabled(false);
		}
		mWebView.loadUrl(Url);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
		outState.putLong(STATE_RESUME_POSITION, mResumePosition);
		outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			menu.add(0,0,0,this.getResources().getString(R.string.menu_share)).setIcon(R.drawable.ic_share_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			if(!TextUtils.isEmpty(ShareUrlMsg)){
				ShareUtil.shareText(WebViewFullscreenActivity.this, ShareUrlMsg);
			}else {
				ShareUtil.shareText(WebViewFullscreenActivity.this,mWebView.getTitle() + " (share from:"+getString(R.string.app_name)+") " + Url);
			}
			AVAnalytics.onEvent(this, "webview_share_link");
			break;
		}
       return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ViewUtil.destroyWebView(mWebView);
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
	
}
