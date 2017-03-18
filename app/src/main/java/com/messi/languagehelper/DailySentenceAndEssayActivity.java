package com.messi.languagehelper;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.DailySentenceAndEssayAdapter;
import com.messi.languagehelper.adapter.JokePageAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class DailySentenceAndEssayActivity extends BaseActivity implements FragmentProgressbarListener {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private DailySentenceAndEssayAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_sentence_and_essay_activity);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().setTitle("");
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        pageAdapter = new DailySentenceAndEssayAdapter(getSupportFragmentManager(), this);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewpager);
    }

}
