package com.messi.languagehelper;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.HomeListenPageAdapter;
import com.messi.languagehelper.adapter.JokePageAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class HomeListenActivity extends BaseActivity implements FragmentProgressbarListener {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private HomeListenPageAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_listen_activity);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_listening));
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        pageAdapter = new HomeListenPageAdapter(getSupportFragmentManager(), this);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewpager);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(pageAdapter != null){
                    pageAdapter.onTabReselected(tab.getPosition());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
