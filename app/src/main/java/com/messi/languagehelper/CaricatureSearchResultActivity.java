package com.messi.languagehelper;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.messi.languagehelper.adapter.CaricatureSearchResultAdapter;
import com.messi.languagehelper.util.KeyUtil;


public class CaricatureSearchResultActivity extends BaseActivity {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private String search_text;
    private String url;
    private CaricatureSearchResultAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayout_search_result_activity);
        initViews();
    }

    private void initViews(){
        search_text = getIntent().getStringExtra(KeyUtil.SearchKey);
        url = getIntent().getStringExtra(KeyUtil.SearchUrl);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        pageAdapter = new CaricatureSearchResultAdapter(getSupportFragmentManager(),this,search_text,url);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewpager);
    }

}
