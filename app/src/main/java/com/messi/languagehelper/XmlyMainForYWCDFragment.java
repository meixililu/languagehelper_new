package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.SystemUtil;

public class XmlyMainForYWCDFragment extends BaseFragment implements OnClickListener,
		FragmentProgressbarListener{

	private ProgressBar progressBar;
	private TabLayout mTabLayout;
	private FrameLayout search_btn;
	public int currentTabIndex;

	private Fragment mFragment1;
	private Fragment mFragment2;
	private Fragment mFragment3;
	private Fragment mFragment4;

	public static Fragment newInstance(){
		return new XmlyMainForYWCDFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.xmly_main_yys_fragment, container, false);
		initViews(view);
		initFragment();
		initTablayout();
		return view;
	}
	
	private void initViews(View view){
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
		mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
		search_btn = (FrameLayout) view.findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
	}

	private void initTablayout(){
		if(SystemUtil.lan.equals("en")){
			mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		}
		currentTabIndex = PlayUtil.getSP().getInt(KeyUtil.XmlyMainForYWCD,0);
		mTabLayout.addTab(mTabLayout.newTab().setText(getText(R.string.title_category)));
		mTabLayout.addTab(mTabLayout.newTab().setText(getText(R.string.title_yuwen)));
		mTabLayout.addTab(mTabLayout.newTab().setText(getText(R.string.recommend)));
		mTabLayout.addTab(mTabLayout.newTab().setText(getText(R.string.title_broadcast)));
		mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				currentTabIndex = tab.getPosition();
				showSelectFragment();
				Settings.saveSharedPreferences(PlayUtil.getSP(),
						KeyUtil.XmlyMainForYWCD,
						currentTabIndex);
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
		showSelectFragment();
	}

	private void initFragment(){
		mFragment1 = XmlyCategoryFragment.getInstance();
		mFragment2 = XmlySearchAlbumForYYSFragment.newInstance("语文");
		mFragment3 = XmlyCategoryRecommendFragment.newInstance("","",this);
		mFragment4 = XimalayaRadioHomeFragment.newInstance(this);
		getChildFragmentManager()
				.beginTransaction()
				.add(R.id.content_layout,mFragment1)
				.add(R.id.content_layout,mFragment2)
				.add(R.id.content_layout,mFragment3)
				.add(R.id.content_layout, mFragment4)
				.commit();
	}

	private void hideAllFragment(){
		getChildFragmentManager()
				.beginTransaction()
				.hide(mFragment1)
				.hide(mFragment2)
				.hide(mFragment3)
				.hide(mFragment4)
				.commit();
	}

	private void showSelectFragment(){
		hideAllFragment();
		mTabLayout.getTabAt(currentTabIndex).select();
		switch (currentTabIndex){
			case 0:
				getChildFragmentManager()
						.beginTransaction().show(mFragment1).commit();
				break;
			case 1:
				getChildFragmentManager()
						.beginTransaction().show(mFragment2).commit();
				break;
			case 2:
				getChildFragmentManager()
						.beginTransaction().show(mFragment3).commit();
				break;
			case 3:
				getChildFragmentManager()
						.beginTransaction().show(mFragment4).commit();
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	public void showProgressbar(){
		if(progressBar != null){
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	public void hideProgressbar(){
		if(progressBar != null){
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.search_btn){
			toSearchActivity();
		}
	}

	private void toSearchActivity() {
		Intent intent = new Intent(getContext(), XmlySearchActivity.class);
		startActivity(intent);
	}

}
