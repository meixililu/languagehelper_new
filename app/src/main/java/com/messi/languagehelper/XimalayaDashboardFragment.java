package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.messi.languagehelper.adapter.XmlyAllAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import static com.messi.languagehelper.R.id.viewpager;

public class XimalayaDashboardFragment extends BaseFragment implements OnClickListener,
		FragmentProgressbarListener{

	private ViewPager mViewPager;
	private ProgressBar progressBar;
	private TabLayout mTabLayout;

	public static Fragment newInstance(){
		XimalayaDashboardFragment fragment = new XimalayaDashboardFragment();
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.xmly_dashboard_fragment, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
		mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
		mViewPager = (ViewPager) view.findViewById(viewpager);
		mViewPager.setOffscreenPageLimit(3);
		XmlyAllAdapter allAdapter = new XmlyAllAdapter(getChildFragmentManager(),this);
		mViewPager.setAdapter(allAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
		mViewPager.setCurrentItem(1);
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
