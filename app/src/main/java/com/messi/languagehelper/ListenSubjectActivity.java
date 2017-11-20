package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.ListenSubjectAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import cn.jzvd.JZVideoPlayer;

public class ListenSubjectActivity extends BaseActivity implements FragmentProgressbarListener {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private ListenSubjectAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_activity);
        initViews();
    }

    private void initViews(){
        getSupportActionBar().setTitle(getResources().getString(R.string.title_listening));
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        pageAdapter = new ListenSubjectAdapter(getSupportFragmentManager(),this);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(5);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
