package com.messi.languagehelper.wxapi;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
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
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.LeisureFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.StudyFragment;
import com.messi.languagehelper.TitleFragment;
import com.messi.languagehelper.XimalayaDashboardFragment;
import com.messi.languagehelper.YYJHomeFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AppUpdateUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateHelper;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class YYJMainActivity extends BaseActivity implements FragmentProgressbarListener {

	private long exitTime = 0;
	private SharedPreferences mSharedPreferences;
	private Intent playIntent;

	@BindView(R.id.content)
	FrameLayout content;
	@BindView(R.id.navigation)
	BottomNavigationView navigation;
	private Fragment mWordHomeFragment;
	private Fragment practiceFragment;
	private Fragment dashboardFragment;
	private Fragment radioHomeFragment;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_home:
					hideAllFragment();
					getSupportFragmentManager().beginTransaction().show(mWordHomeFragment).commit();;
					AVAnalytics.onEvent(YYJMainActivity.this, "spoken_home");
					return true;
				case R.id.navigation_practice:
					hideAllFragment();
					getSupportFragmentManager().beginTransaction().show(practiceFragment).commit();;
					AVAnalytics.onEvent(YYJMainActivity.this, "spoken_practice");
					return true;
				case R.id.navigation_word_study:
					hideAllFragment();
					getSupportFragmentManager().beginTransaction().show(dashboardFragment).commit();;
					AVAnalytics.onEvent(YYJMainActivity.this, "spoken_course");
					return true;
				case R.id.navigation_vovabulary:
					hideAllFragment();
					getSupportFragmentManager().beginTransaction().show(radioHomeFragment).commit();;
					AVAnalytics.onEvent(YYJMainActivity.this, "spoken_bussiness");
					return true;
			}
			return false;
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_yyj_main);
			ButterKnife.bind(this);
			initData();
			initFragment();
			initSDKAndPermission();
			AppUpdateUtil.isNeedUpdate(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initData(){
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
		mSharedPreferences = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
		PlayUtil.initData(this, mSharedPreferences);
		TranslateHelper.init(mSharedPreferences);
	}

	private void initFragment() {
		navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		mWordHomeFragment = TitleFragment.newInstance(YYJHomeFragment.getInstance(),R.string.title_home_tab);
		practiceFragment = TitleFragment.newInstance(StudyFragment.getInstance(),R.string.title_study);
		dashboardFragment = XimalayaDashboardFragment.newInstance();;
		radioHomeFragment = TitleFragment.newInstance(LeisureFragment.getInstance(),R.string.title_leisure);
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content, mWordHomeFragment)
				.add(R.id.content, practiceFragment)
				.add(R.id.content, dashboardFragment)
				.add(R.id.content, radioHomeFragment)
				.commit();
		hideAllFragment();
		getSupportFragmentManager()
				.beginTransaction().show(mWordHomeFragment).commit();
	}

	private void hideAllFragment(){
		getSupportFragmentManager()
				.beginTransaction()
				.hide(dashboardFragment)
				.hide(practiceFragment)
				.hide(radioHomeFragment)
				.hide(mWordHomeFragment)
				.commit();
	}

	private void initSDKAndPermission(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				YYJMainActivityPermissionsDispatcher.showPermissionWithPermissionCheck(YYJMainActivity.this);
			}
		}, 1 * 1000);
	}

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PlayerService.MusicBinder binder = (PlayerService.MusicBinder) service;
			PlayerService.musicSrv = binder.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	@Override
	public void onStart() {
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

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			Jzvd.releaseAllVideos();
			PlayUtil.onDestroy();
			unbindService(musicConnection);
			isBackgroundPlay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void isBackgroundPlay(){
		if(XmPlayerManager.getInstance(this).isPlaying() || Setings.MPlayerIsPlaying()){
			LogUtil.DefalutLog("xmly or myplayer is playing.");
		}else {
			((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Setings.NOTIFY_ID);
			if (playIntent != null) {
				stopService(playIntent);
			}
			XmPlayerManager.getInstance(this).release();
			PlayerService.musicSrv = null;
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
		YYJMainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
