package com.messi.languagehelper;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.ChineseDictionaryAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChineseDictionaryActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private ChineseDictionaryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zh_dic_activity);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_two));
        mAdapter = new ChineseDictionaryAdapter(this.getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewPager);
    }

}
