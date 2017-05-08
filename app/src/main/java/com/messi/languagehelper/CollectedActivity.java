package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.messi.languagehelper.adapter.CollectedActivityAdapter;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Settings;

public class CollectedActivity extends BaseActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private SharedPreferences mSharedPreferences;
    private CollectedActivityAdapter mAdapter;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collected_main);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_favorite));
        mSharedPreferences = Settings.getSharedPreferences(this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        mAdapter = new CollectedActivityAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewPager);
        setLastTimeSelectTab();
    }

    private void setLastTimeSelectTab(){
        int index = Settings.getSharedPreferences(this).getInt(KeyUtil.LastTimeCollectedActivitySelectTab, 0);
        viewPager.setCurrentItem(index);
    }

    private void saveSelectTab(){
        int index = viewPager.getCurrentItem();
        Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.LastTimeCollectedActivitySelectTab,index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSelectTab();
        PlayUtil.stopPlay();
    }
}