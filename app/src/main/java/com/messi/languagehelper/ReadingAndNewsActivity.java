package com.messi.languagehelper;

import android.os.Bundle;
import android.os.PersistableBundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.messi.languagehelper.adapter.ReadingAndNewsAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

public class ReadingAndNewsActivity extends BaseActivity implements FragmentProgressbarListener{

	private TabLayout tablayout;
	private ViewPager viewpager;
	private ReadingAndNewsAdapter pageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_activity);
		initViews();
	}
	
	private void initViews(){
		getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_reading));
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		
		pageAdapter = new ReadingAndNewsAdapter(getSupportFragmentManager(),this);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOffscreenPageLimit(9);
		tablayout.setupWithViewPager(viewpager);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//		super.onSaveInstanceState(outState, outPersistentState);
	}
	
	
}
