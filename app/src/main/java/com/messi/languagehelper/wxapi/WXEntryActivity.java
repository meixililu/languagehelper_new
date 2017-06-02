package com.messi.languagehelper.wxapi;


import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.okhttp.internal.framed.FrameReader;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.MoreActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewFragment;
import com.messi.languagehelper.adapter.MainPageAdapter;
import com.messi.languagehelper.adapter.NewsAdapter;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.AppDownloadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TranslateUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static PlayerService musicSrv;
	private Intent playIntent;
	private DialogPlus dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.content_frame);
			mInstance = this;
			initViews();
			Settings.verifyStoragePermissions(this,Settings.PERMISSIONS_STORAGE);
			runCheckUpdateTask();
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
		viewPager.setOffscreenPageLimit(5);
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

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PlayerService.MusicBinder binder = (PlayerService.MusicBinder)service;
			musicSrv = binder.getService();
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		startMusicPlayerService();
	}

	private void startMusicPlayerService(){
		if(playIntent == null){
			playIntent = new Intent(this, PlayerService.class);
			bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(playIntent);
		}
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
	protected void onPause() {
		super.onPause();
		JCVideoPlayer.releaseAllVideos();
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
		if(playIntent != null){
			stopService(playIntent);
		}
		unbindService(musicConnection);
		musicSrv = null;
	}

	private void runCheckUpdateTask(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				checkUpdate();
			}
		},5*1000);
	}

	private void checkUpdate(){
		AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.UpdateInfo.UpdateInfo);
		query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zyhy");
		query.whereEqualTo(AVOUtil.UpdateInfo.IsValid, "1");
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (avObjects != null && avObjects.size() > 0) {
					AVObject mAVObject = avObjects.get(0);
					showUpdateDialog(mAVObject);
				}
			}
		});
	}

	private void showUpdateDialog(final AVObject mAVObject){
		int newVersionCode = mAVObject.getInt(AVOUtil.UpdateInfo.VersionCode);
		int oldVersionCode = Settings.getVersion(WXEntryActivity.this);
		if(newVersionCode > oldVersionCode){
			String updateInfo = mAVObject.getString(AVOUtil.UpdateInfo.AppUpdateInfo);
			String downloadType = mAVObject.getString(AVOUtil.UpdateInfo.DownloadType);
			String apkUrl = "";
			if(downloadType.equals("apk")){
				AVFile avFile = mAVObject.getAVFile(AVOUtil.UpdateInfo.Apk);
				apkUrl = avFile.getUrl();
			}else{
				apkUrl = mAVObject.getString(AVOUtil.UpdateInfo.APPUrl);
			}
			final String downloadUrl = apkUrl;
			LogUtil.DefalutLog("apkUrl:"+apkUrl);

			dialog = DialogPlus.newDialog(this)
					.setContentHolder(new ViewHolder(R.layout.dialog_update_info))
					.setCancelable(false)
					.setGravity(Gravity.BOTTOM)
					.setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
					.setOverlayBackgroundResource(R.color.none_alpha)
					.create();
			View view = dialog.getHolderView();
			TextView updage_info = (TextView) view.findViewById(R.id.updage_info);
			TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
			TextView update_btn = (TextView) view.findViewById(R.id.update_btn);

			updage_info.setText(updateInfo);
			cancel_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			update_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					new AppDownloadUtil(WXEntryActivity.this,
							downloadUrl,
							mAVObject.getString(AVOUtil.UpdateInfo.AppName),
							mAVObject.getObjectId(),
							SDCardUtil.apkUpdatePath
					).DownloadFile();
				}
			});
			dialog.show();
		}
	}

}
