package com.messi.languagehelper;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BaseFragment extends Fragment {
	
	public FragmentProgressbarListener mProgressbarListener;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	public boolean isHasLoadData;
	public boolean misVisibleToUser;

	public BaseFragment(){}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(getUserVisibleHint() && !isHasLoadData){
			loadDataOnStart();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		misVisibleToUser = isVisibleToUser;
		if(getActivity() != null && !isHasLoadData){
			loadDataOnStart();
		}
	}

	public void loadDataOnStart(){
		LogUtil.DefalutLog("loadDataOnStart");
		isHasLoadData = true;
	}

	/**
	 * need init beford use
	 */
	protected void initSwipeRefresh(View view){
		if(mSwipeRefreshLayout == null){
			mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mswiperefreshlayout);
			mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, 
		            R.color.holo_green_light, 
		            R.color.holo_orange_light, 
		            R.color.holo_red_light);
			mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					onSwipeRefreshLayoutRefresh();
				}
			});
		}
	}
	
	public void onSwipeRefreshLayoutFinish(){
		if(mSwipeRefreshLayout != null){
			mSwipeRefreshLayout.setRefreshing(false);
		}
	}
	
	public void onSwipeRefreshLayoutRefresh(){
	}
	
	
	public void showProgressbar(){
		if(mProgressbarListener != null){
			mProgressbarListener.showProgressbar();
		}
	}
	
	public void hideProgressbar(){
		if(mProgressbarListener != null){
			mProgressbarListener.hideProgressbar();
		}
	}

}
