package com.messi.languagehelper.wxapi;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.LeisureFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.StudyFragment;
import com.messi.languagehelper.TitleFragment;
import com.messi.languagehelper.XimalayaTagsFragment;
import com.messi.languagehelper.YYJHomeFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AppUpdateUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;

public class YYJMainActivity extends BaseActivity implements FragmentProgressbarListener {

	private long exitTime = 0;
	private SharedPreferences mSharedPreferences;
	private SpeechSynthesizer mSpeechSynthesizer;
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
	}

	private void initFragment() {
		navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		mWordHomeFragment = TitleFragment.newInstance(YYJHomeFragment.getInstance(),R.string.title_home_tab);
		practiceFragment = TitleFragment.newInstance(StudyFragment.getInstance(),R.string.title_study);
		dashboardFragment = TitleFragment.newInstance(XimalayaTagsFragment.newInstance(XimalayaUtil.Category_Eng,"",null),
				R.string.title_listen_fm);
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

	private void initPermissions(){
		if (Build.VERSION.SDK_INT >= 23) {
			checkAndRequestPermission();
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	private void checkAndRequestPermission() {
		Settings.verifyStoragePermissions(this, Settings.PERMISSIONS_STORAGE);
	}

	private void initSDKAndPermission(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initPermissions();
			}
		}, 1 * 1000);
	}

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PlayerService.MusicBinder binder = (PlayerService.MusicBinder) service;
			Settings.musicSrv = binder.getService();
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
		TranslateUtil.saveTranslateApiOrder(mSharedPreferences);
		Jzvd.releaseAllVideos();
		PlayUtil.onDestroy();
		if (playIntent != null) {
			stopService(playIntent);
		}
		unbindService(musicConnection);
		XmPlayerManager.getInstance(this).release();
		Settings.musicSrv = null;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 10010:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					AppUpdateUtil.checkUpdate(this);
				} else {
					Uri packageURI = Uri.parse("package:"+this.getPackageName());
					Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
					startActivityForResult(intent, 10086);
				}
				break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 10086) {
			AppUpdateUtil.checkUpdate(this);
		}
	}

}
