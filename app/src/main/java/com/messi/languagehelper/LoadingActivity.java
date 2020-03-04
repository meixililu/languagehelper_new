package com.messi.languagehelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.ViewModel.KaipingModel;
import com.messi.languagehelper.event.KaipingPageEvent;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AppUpdateUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity {

    @BindView(R.id.ad_source)
    TextView ad_source;
    @BindView(R.id.skip_view)
    TextView skip_view;
    @BindView(R.id.ad_img)
    SimpleDraweeView ad_img;
    @BindView(R.id.splash_container)
    FrameLayout splash_container;
    private SharedPreferences mSharedPreferences;
    private KaipingModel mKaipingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            setTheme(R.style.AppTheme);
            super.onCreate(savedInstanceState);
            TransparentStatusbar();
            setContentView(R.layout.loading_activity);
            ButterKnife.bind(this);

            EventBus.getDefault().register(this);
            AppUpdateUtil.runCheckUpdateTask(this);
            init();
        } catch (Exception e) {
            onException();
            e.printStackTrace();
        }
    }

    private void TransparentStatusbar() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void init() {
        mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        ADUtil.initAdConfig(mSharedPreferences);
        initPermissions();
        if(!mSharedPreferences.getBoolean(KeyUtil.PrivacyKey,false)){
            Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.PrivacyKey,true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startPrivacyActivity();
                }
            },1000);
        }else {
            mKaipingModel = new KaipingModel(this);
            mKaipingModel.setViews(ad_source,skip_view,ad_img,splash_container);
            mKaipingModel.showAd();
        }
    }

    private void startPrivacyActivity(){
        Intent intent = new Intent(this,LoadingPreActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Subscribe
    public void EventBusEvent(KaipingPageEvent event){
        if(event.getMsg().equals("finish")){
            finish();
        }
    }

    //防止用户返回键退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mKaipingModel != null){
            mKaipingModel.notJump = false;
            if(mKaipingModel.isAdClicked) {
                mKaipingModel.toNextPage();
            }
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mKaipingModel != null){
            mKaipingModel.notJump = true;
        }
        MobclickAgent.onPause(this);
    }

    private void initPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (lackedPermission.size() == 0) {
            Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.IsTXADPermissionReady,true);
        } else {
            Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.IsTXADPermissionReady,false);
        }
    }

    private void onException(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mKaipingModel != null){
                    mKaipingModel.toNextPage();
                }
            }
        },3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
