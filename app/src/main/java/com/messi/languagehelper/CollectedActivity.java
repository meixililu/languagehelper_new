package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ProgressBar;

import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.CollectedActivityAdapter;
import com.messi.languagehelper.util.PlayUtil;

public class CollectedActivity extends BaseActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;

    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    //合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;

    private CollectedActivityAdapter mAdapter;

    private Bundle bundle;
    private int maxNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collected_main);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_favorite));
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mAdapter = new CollectedActivityAdapter(this.getSupportFragmentManager(), bundle, this);

        viewPager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayUtil.stopPlay();
    }
}
