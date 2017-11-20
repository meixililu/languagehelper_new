package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.SpokenEnglishSubjectAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import cn.jzvd.JZVideoPlayer;

public class SpokenEnglishActivity extends BaseActivity implements FragmentProgressbarListener {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private SpokenEnglishSubjectAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_activity);
        initViews();
    }

    private void initViews(){
        getSupportActionBar().setTitle(getResources().getString(R.string.spoken_english_practice));
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        pageAdapter = new SpokenEnglishSubjectAdapter(getSupportFragmentManager(),this);
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
