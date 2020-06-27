package com.messi.languagehelper;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.messi.languagehelper.adapter.XmlySearchResultAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;

public class XmlySearchResultActivity extends BaseActivity implements FragmentProgressbarListener {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private XmlySearchResultAdapter pageAdapter;
    private String search_text;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_activity);
        initViews();
    }

    private void initViews() {
        position = getIntent().getIntExtra(KeyUtil.PositionKey,0);
        search_text = getIntent().getStringExtra(KeyUtil.SearchKey);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        pageAdapter = new XmlySearchResultAdapter(getSupportFragmentManager(), this, search_text);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(5);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

}
