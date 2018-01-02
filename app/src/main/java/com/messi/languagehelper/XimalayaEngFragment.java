package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.messi.languagehelper.adapter.XmlyEngAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

public class XimalayaEngFragment extends BaseFragment implements OnClickListener,
		FragmentProgressbarListener{

	private ViewPager mViewPager;
	private ProgressBar progressBar;
	private TabLayout mTabLayout;
	private FrameLayout search_btn;

	public static Fragment newInstance(){
		XimalayaEngFragment fragment = new XimalayaEngFragment();
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
		View view = inflater.inflate(R.layout.xmly_eng_fragment, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
		mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		search_btn = (FrameLayout) view.findViewById(R.id.search_btn);
		mViewPager.setOffscreenPageLimit(2);
		XmlyEngAdapter allAdapter = new XmlyEngAdapter(getChildFragmentManager(),this);
		mViewPager.setAdapter(allAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
		search_btn.setOnClickListener(this);
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
