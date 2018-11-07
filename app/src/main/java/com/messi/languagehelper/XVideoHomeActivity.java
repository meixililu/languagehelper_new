package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.XVideoHomeAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;

public class XVideoHomeActivity extends BaseActivity implements FragmentProgressbarListener {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private XVideoHomeAdapter pageAdapter;
    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_activity);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_xvideo));
        index = getIntent().getIntExtra(KeyUtil.IndexKey,0);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pageAdapter = new XVideoHomeAdapter(getSupportFragmentManager(), this);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(8);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(index);
    }
}
