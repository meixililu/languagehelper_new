package com.messi.languagehelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Setings;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

public class BaseActivity extends AppCompatActivity {

    public static final String UpdateMusicUIToStop = "com.messi.languagehelper.updateuito.stop";
    public static final String ActivityClose = "com.messi.languagehelper.activity.close";
    public Toolbar toolbar;
    public ProgressBar mProgressbar;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private View mScrollable;
    public boolean isRegisterBus;
    private View rootView;

    BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                if(!TextUtils.isEmpty(action)){
                    if(UpdateMusicUIToStop.equals(action)){
                        updateUI(intent.getStringExtra(KeyUtil.MusicAction));
                    }else if(ActivityClose.equals(action)){
                        finish();
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);
    }

    public void transparentStatusbar() {
        try {
            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                //透明状态栏 透明导航栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarTextColor(boolean isBlack) {
        try {
            if (VERSION.SDK_INT > VERSION_CODES.M) {
                if (isBlack) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
                }else {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setStatusbarColor(int color) {
        try {
            if (VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(this.getResources().getColor(color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateMusicUIToStop);
        registerReceiver(activityReceiver, intentFilter);
    }

    public void registerBroadcast(String action){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        registerReceiver(activityReceiver, intentFilter);
    }

    public void unregisterBroadcast(){
        unregisterReceiver(activityReceiver);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
        initProgressbar();
    }

    @Override
    public void setContentView(View view){
        super.setContentView(view);
        rootView = view;
        getActionBarToolbar();
        initProgressbar();
    }

    protected void getActionBarToolbar() {
        if (toolbar == null) {
            if (rootView != null) {
                toolbar = (Toolbar) rootView.findViewById(R.id.my_awesome_toolbar);
            } else {
                toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
            }
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (VERSION.SDK_INT >= VERSION_CODES.KITKAT && VERSION.SDK_INT <= VERSION_CODES.LOLLIPOP) {
                    toolbar.setPadding(0, ScreenUtil.dip2px(this, 10), 0, 0);
                }
            }
            String title = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
            if(!TextUtils.isEmpty(title)){
                setActionBarTitle(title);
            }else {
                Bundle bundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
                if(bundle != null){
                    title = bundle.getString(KeyUtil.ActionbarTitle);
                    setActionBarTitle(title);
                }
            }
        }
    }

    protected void hideTitle(){
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void updateUI(String music_action){}

    protected void setToolbarBackground(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(this.getResources().getColor(color));
        }
    }

    protected void setActionBarTitle(String title) {
        if (!TextUtils.isEmpty(title) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setCollTitle(String title) {
        BaseActivity.this.setTitle(title);
    }

    protected void toActivity(Class mClass, Bundle bundle) {
        Intent intent = new Intent(this, mClass);
        if (bundle != null) {
            intent.putExtra(KeyUtil.BundleKey, bundle);
        }
        startActivity(intent);
    }

    protected void initProgressbar() {
        if (mProgressbar == null) {
            if (rootView != null) {
                mProgressbar = (ProgressBar) rootView.findViewById(R.id.progressBarCircularIndetermininate);
                LogUtil.DefalutLog("init mProgressbar");
            } else {
                mProgressbar = (ProgressBar) findViewById(R.id.progressBarCircularIndetermininate);
            }
        }
    }

    /**
     * need init beford use
     */
    protected void initSwipeRefresh() {
        if (mSwipeRefreshLayout == null) {
            if (rootView != null) {
                mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mswiperefreshlayout);
            } else {
                mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mswiperefreshlayout);
            }
            mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
                    R.color.holo_green_light,
                    R.color.holo_orange_light,
                    R.color.holo_red_light);
            mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onSwipeRefreshLayoutRefresh();
                }
            });
        }
    }

    public void onSwipeRefreshLayoutFinish() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onSwipeRefreshLayoutRefresh() {
    }

    public void showProgressbar() {
        LogUtil.DefalutLog("showProgressbar");
        if (mProgressbar != null) {
            mProgressbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressbar() {
        LogUtil.DefalutLog("hideProgressbar");
        if (mProgressbar != null) {
            mProgressbar.setVisibility(View.GONE);
        }
    }


    protected void setScrollable(View s) {
        mScrollable = s;
    }

    /**
     * 点击翻译之后隐藏输入法
     */
    protected void hideIME(View view) {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 点击编辑之后显示输入法
     */
    protected void showIME() {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if(isRegisterBus){
                if(!EventBus.getDefault().isRegistered(this)){
                    EventBus.getDefault().register(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (isRegisterBus) {
                if(EventBus.getDefault().isRegistered(this)){
                    EventBus.getDefault().unregister(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                AudioTrackUtil.adjustStreamVolume(BaseActivity.this, keyCode);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setAppTheme(){
        SharedPreferences sp = Setings.getSharedPreferences(this);
        String apptheme = sp.getString(KeyUtil.APPTheme,"blue");
        if ("blue".equals(apptheme)) {
            setTheme(R.style.AppThemeBlue);
        } else if ("green".equals(apptheme)) {
            setTheme(R.style.AppThemeGreen);
        } else if ("orange".equals(apptheme)) {
            setTheme(R.style.AppThemeOrange);
        } else if ("indigo".equals(apptheme)) {
            setTheme(R.style.AppThemeIndigo);
        } else if ("red".equals(apptheme)) {
            setTheme(R.style.AppThemeRed);
        }  else if ("purple".equals(apptheme)) {
            setTheme(R.style.AppThemePurple);
        }  else if ("gray".equals(apptheme)) {
            setTheme(R.style.AppThemeGray);
        }  else if ("cyan".equals(apptheme)) {
            setTheme(R.style.AppThemeCyan);
        }  else if ("teal".equals(apptheme)) {
            setTheme(R.style.AppThemeTeal);
        }  else if ("pink".equals(apptheme)) {
            setTheme(R.style.AppThemePink);
        } else {
            setTheme(R.style.AppThemeBlue);
        }
        
    }

}
