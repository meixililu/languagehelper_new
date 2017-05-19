package com.messi.languagehelper;

import com.messi.languagehelper.adapter.WordStudyTabAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class WordStudyActivity extends BaseActivity implements FragmentProgressbarListener{

	private TabLayout tablayout;
	private ViewPager viewpager;
	private WordStudyTabAdapter pageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_activity);
		initViews();
	}
	
	private void initViews(){
		getSupportActionBar().setTitle(getResources().getString(R.string.title_word_study_summary));
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		
		pageAdapter = new WordStudyTabAdapter(getSupportFragmentManager(),this);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOffscreenPageLimit(2);
		tablayout.setTabsFromPagerAdapter(pageAdapter);
		tablayout.setupWithViewPager(viewpager);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
	
}
