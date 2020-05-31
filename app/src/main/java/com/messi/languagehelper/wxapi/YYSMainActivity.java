package com.messi.languagehelper.wxapi;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.adapter.MainPageAdapter;
import com.messi.languagehelper.aidl.IXBPlayer;
import com.messi.languagehelper.databinding.ContentFrameBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AppUpdateUtil;
import com.messi.languagehelper.util.IPlayerUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateHelper;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import cn.jzvd.Jzvd;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class YYSMainActivity extends BaseActivity implements FragmentProgressbarListener {

	private long pressTime = 0;
	private SharedPreferences sp;
	private Intent playIntent;
	private ContentFrameBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			binding = ContentFrameBinding.inflate(LayoutInflater.from(this));
			setContentView(binding.getRoot());
			initData();
			initViews();
			setupTabIcons();
			initSDKAndPermission();
			AppUpdateUtil.isNeedUpdate(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initData(){
		registerBroadcast();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
		sp = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
		PlayUtil.initData(this, sp);
		TranslateHelper.init(sp);
	}

	private void setupTabIcons() {
		binding.tablayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_home_white_selector));
		binding.tablayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_school_selector));
		binding.tablayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_wb_cloudy_selector));
		binding.tablayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.ic_view_list_selector));
	}

	private void initViews() {
		MainPageAdapter mAdapter = new MainPageAdapter(this.getSupportFragmentManager(),this, this);
		binding.playbtnLayout.setOnClickListener(view -> onPlayBtnClick(view));
		binding.pager.setAdapter(mAdapter);
		binding.pager.setOffscreenPageLimit(5);
		binding.tablayout.setupWithViewPager(binding.pager);
		binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				initPlayerBtn(tab.getPosition());
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

	private void initPlayerBtn(int index){
		try {
			if (index == 1 || index == 2) {
				binding.playbtnLayout.setVisibility(View.VISIBLE);
				if (IPlayerUtil.MPlayerIsPlaying() || XmPlayerManager.getInstance(this).isPlaying()) {
					binding.btnPlay.setSelected(true);
				} else {
					binding.btnPlay.setSelected(false);
				}
			}else {
				binding.playbtnLayout.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPlayBtnClick(View view){
		if (IPlayerUtil.MPlayerIsPlaying() || XmPlayerManager.getInstance(this).isPlaying()) {
			binding.btnPlay.setSelected(false);
			IPlayerUtil.pauseAudioPlayer(this);
		}else {
			binding.btnPlay.setSelected(true);
			IPlayerUtil.restartAudioPlayer(this);
		}
	}

	@Override
	public void updateUI(String music_action) {
		LogUtil.DefalutLog("updateUI---music_action");
		if(PlayerService.action_restart.equals(music_action)){
			binding.btnPlay.setSelected(false);
		}else if (PlayerService.action_pause.equals(music_action)) {
			binding.btnPlay.setSelected(true);
		}
	}

	private void initSDKAndPermission(){
		new Handler().postDelayed(
				() -> YYSMainActivityPermissionsDispatcher.showPermissionWithPermissionCheck(YYSMainActivity.this)
		, 1 * 1000);
	}

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogUtil.DefalutLog("WXEntryActivity---musicConnection---");
			IPlayerUtil.musicSrv = IXBPlayer.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.DefalutLog("WXEntryActivity---onStart");
		startMusicPlayerService();
	}

	private void startMusicPlayerService() {
		if (playIntent == null) {
			playIntent = new Intent(this, PlayerService.class);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(playIntent);
			}else {
				startService(playIntent);
			}
			bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
		}
	}

	private void setLastTimeSelectTab() {
		int index = sp.getInt(KeyUtil.LastTimeSelectTab, 0);
		binding.pager.setCurrentItem(index);
		initPlayerBtn(index);
	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - pressTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), Toast.LENGTH_SHORT).show();
			pressTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	private void saveSelectTab() {
		int index = binding.pager.getCurrentItem();
		LogUtil.DefalutLog("WXEntryActivity---onDestroy---saveSelectTab---index:" + index);
		Setings.saveSharedPreferences(sp, KeyUtil.LastTimeSelectTab, index);
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
		LogUtil.DefalutLog("WXEntryActivity---onSaveInstanceState");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogUtil.DefalutLog("WXEntryActivity---onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterBroadcast();
			saveSelectTab();
			Jzvd.releaseAllVideos();
			release();
			PlayUtil.onDestroy();
			UnbindService();
			isBackgroundPlay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void release(){
		MyPlayer.release();
	}

	private void UnbindService(){
		IPlayerUtil.setAppExit(true);
		if (musicConnection != null && IPlayerUtil.musicSrv != null) {
			unbindService(musicConnection);
			musicConnection = null;
		}
	}

	private void isBackgroundPlay(){
		if(XmPlayerManager.getInstance(this).isPlaying() || IPlayerUtil.MPlayerIsPlaying()){
			LogUtil.DefalutLog("xmly or myplayer is playing.");
		}else {
			((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Setings.NOTIFY_ID);
			if (playIntent != null) {
				stopService(playIntent);
				playIntent = null;
			}
			XmPlayerManager.getInstance(this).release();
			IPlayerUtil.musicSrv = null;
		}
	}

	@NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION})
	void showPermission() {
		LogUtil.DefalutLog("showPermission");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		YYSMainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
