package com.messi.languagehelper.wxapi;


import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.MoreActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewFragment;
import com.messi.languagehelper.adapter.MainPageAdapter;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TranslateUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.HashMap;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class WXEntryActivity extends BaseActivity implements OnClickListener,FragmentProgressbarListener {

	public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
	private TabLayout tablayout;
	private ViewPager viewPager;
	private MainPageAdapter mAdapter;
	
	private long exitTime = 0;
	private Bundle bundle;
	private boolean isRespondWX;
	public static WXEntryActivity mInstance;
	private SharedPreferences mSharedPreferences;
	private SpeechSynthesizer mSpeechSynthesizer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.content_frame);
			mInstance = this;
			initViews();
			Settings.verifyStoragePermissions(this,Settings.PERMISSIONS_STORAGE);
//			checkUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initViews(){
		bundle = getIntent().getExtras();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" +getString(R.string.app_id));
		if (toolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setTitle("");
		}
		mSharedPreferences = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
		PlayUtil.initData(this,mSpeechSynthesizer,mSharedPreferences);

		viewPager = (ViewPager) findViewById(R.id.pager);
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		mAdapter = new MainPageAdapter(this.getSupportFragmentManager(),bundle,this,
				mSharedPreferences);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);
		tablayout.setupWithViewPager(viewPager);
		tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				if(mAdapter != null){
					mAdapter.onTabReselected(tab.getPosition());
				}
			}
		});
		
        setLastTimeSelectTab();
	}
	
	private void setLastTimeSelectTab(){
		int index = mSharedPreferences.getInt(KeyUtil.LastTimeSelectTab, 0);
		viewPager.setCurrentItem(index);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_more:
			toMoreActivity();
			break;
		}
       return true;
	}
	
	private void toMoreActivity(){
		Intent intent = new Intent(this, MoreActivity.class); 
		startActivity(intent); 
		AVAnalytics.onEvent(this, "index_pg_to_morepg");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			 toMoreActivity();
			 return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		if (JCVideoPlayer.backPress()) {
			return;
		}
    	if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void saveSelectTab(){
		int index = viewPager.getCurrentItem();
		LogUtil.DefalutLog("WXEntryActivity---onDestroy---saveSelectTab---index:"+index);
		Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.LastTimeSelectTab,index);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveSelectTab();
		WebViewFragment.mMainFragment = null;
		mInstance = null;
		TranslateUtil.saveTranslateApiOrder(mSharedPreferences);
		if(mSharedPreferences.getBoolean(KeyUtil.AutoClearDic, false)){
			DataBaseUtil.getInstance().clearExceptFavoriteDic();
		}
		if(mSharedPreferences.getBoolean(KeyUtil.AutoClearTran, false)){
			DataBaseUtil.getInstance().clearExceptFavoriteTran();
		}
		JCVideoPlayer.releaseAllVideos();
		PlayUtil.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JCVideoPlayer.releaseAllVideos();
	}

	//	private void checkUpdate(){
//		BDAutoUpdateSDK.cpUpdateCheck(this, new MyCPCheckUpdateCallback());
//	}
//
//	private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {
//
//		@Override
//		public void onCheckUpdateCallback(AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
//			if(info != null || infoForInstall != null){
//				showUpdateDialog(info, infoForInstall);
//			}
//		}
//	}
//
//	private void showUpdateDialog(final AppUpdateInfo info, final AppUpdateInfoForInstall infoForInstall){
//		String updateInfo = "有更丰富的内容，更快的速度，更好的体验，快快更新吧！";
//		if(info != null){
//			updateInfo = info.getAppChangeLog();
//		}else if(infoForInstall != null){
//			updateInfo = infoForInstall.getAppChangeLog();
//		}
//		if(updateInfo.contains("<br>")){
//			updateInfo = updateInfo.replace("<br>", "\n");
//		}
//		Dialog dialog = new Dialog(WXEntryActivity.this, "更新啦,更新啦!", updateInfo);
//		dialog.addAcceptButton("好的");
//		dialog.addCancelButton("稍后");
//		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
//					BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), infoForInstall.getInstallPath());
//				}else if(info != null) {
//					BDAutoUpdateSDK.cpUpdateDownload(WXEntryActivity.this, info, new UpdateDownloadCallback());
//				}
//			}
//		});
//		dialog.show();
//	}
//
//	private class UpdateDownloadCallback implements CPUpdateDownloadCallback {
//
//		@Override
//		public void onDownloadComplete(String apkPath) {
//			BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), apkPath);
//		}
//		@Override
//		public void onStart() {
//		}
//
//		@Override
//		public void onPercent(int percent, long rcvLen, long fileSize) {
//		}
//
//		@Override
//		public void onFail(Throwable error, String content) {
//		}
//
//		@Override
//		public void onStop() {
//		}
//
//	}


}
