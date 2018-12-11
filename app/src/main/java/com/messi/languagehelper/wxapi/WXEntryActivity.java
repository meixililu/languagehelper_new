package com.messi.languagehelper.wxapi;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.MoreActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.adapter.MainPageAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AppUpdateUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.Locale;

import cn.jzvd.Jzvd;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class WXEntryActivity extends BaseActivity implements FragmentProgressbarListener {

	private TabLayout tablayout;
	private ViewPager viewPager;

	private long exitTime = 0;
	private SharedPreferences mSharedPreferences;
	private SpeechSynthesizer mSpeechSynthesizer;

	private Intent playIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.content_frame);
			initData();
			initViews();
			initSDKAndPermission();
			AppUpdateUtil.runCheckUpdateTask(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initData(){
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
		mSharedPreferences = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
		PlayUtil.initData(this, mSpeechSynthesizer, mSharedPreferences);
		SystemUtil.lan = Locale.getDefault().getLanguage();
		if (toolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setTitle("");
		}
	}

	private void initViews() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		final MainPageAdapter mAdapter = new MainPageAdapter(this.getSupportFragmentManager(),this,
				mSharedPreferences,this);
		if(SystemUtil.lan.equals("en")){
			tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		}
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
				if (mAdapter != null) {
					mAdapter.onTabReselected(tab.getPosition());
				}
			}
		});
		setLastTimeSelectTab();
	}

	private void initSDKAndPermission(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				WXEntryActivityPermissionsDispatcher.showPermissionWithPermissionCheck(WXEntryActivity.this);
			}
		}, 1 * 1000);
	}

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PlayerService.MusicBinder binder = (PlayerService.MusicBinder) service;
			Setings.musicSrv = binder.getService();
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

	private void startMusicPlayerService() {
		if (playIntent == null) {
			playIntent = new Intent(this, PlayerService.class);
			bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(playIntent);
		}
	}

	private void setLastTimeSelectTab() {
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

	private void toMoreActivity() {
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
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	private void saveSelectTab() {
		int index = viewPager.getCurrentItem();
		LogUtil.DefalutLog("WXEntryActivity---onDestroy---saveSelectTab---index:" + index);
		Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.LastTimeSelectTab, index);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveSelectTab();
		TranslateUtil.saveTranslateApiOrder(mSharedPreferences);
		Jzvd.releaseAllVideos();
		PlayUtil.onDestroy();
		if (playIntent != null) {
			stopService(playIntent);
		}
		unbindService(musicConnection);
		XmPlayerManager.getInstance(this).release();
		Setings.musicSrv = null;
	}

	@NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION})
	void showPermission() {
		LogUtil.DefalutLog("showPermission");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		WXEntryActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}

	@OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION})
	void onShowRationale(final PermissionRequest request) {
		new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
				.setTitle("温馨提示")
				.setMessage("需要授权才能使用。")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						request.proceed();
					}
				}).show();
	}

	@OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION})
	void onPerDenied() {
		ToastUtil.diaplayMesShort(this,"没有授权，部分功能将无法使用！");
	}
}
