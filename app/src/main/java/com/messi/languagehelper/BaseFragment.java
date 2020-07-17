package com.messi.languagehelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;

public class BaseFragment extends Fragment {
	
	public FragmentProgressbarListener mProgressbarListener;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	public boolean isHasLoadData;
	public boolean isFragmentInit;
	public boolean misVisibleToUser;

	BroadcastReceiver activityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null){
				String action = intent.getAction();
				if(!TextUtils.isEmpty(action)){
					if(BaseActivity.UpdateMusicUIToStop.equals(action)){
						updateUI(intent.getStringExtra(KeyUtil.MusicAction));
					}
				}
			}
		}
	};

	public BaseFragment(){}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		isFragmentInit = true;
		if(getUserVisibleHint() && !isHasLoadData){
			isHasLoadData = true;
			loadDataOnStart();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		misVisibleToUser = isVisibleToUser;
		if(isFragmentInit){
			if(isVisibleToUser && getActivity() != null && !isHasLoadData){
				isHasLoadData = true;
				loadDataOnStart();
			}
		}
	}

	public void loadDataOnStart(){
		LogUtil.DefalutLog("loadDataOnStart");
	}

	public void registerBroadcast(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BaseActivity.UpdateMusicUIToStop);
		getActivity().registerReceiver(activityReceiver, intentFilter);
	}

	public void unregisterBroadcast(){
		getActivity().unregisterReceiver(activityReceiver);
	}

	public void updateUI(String music_action){}

	@Override
	public void onDestroyView() {
		if(mProgressbarListener != null){
			mProgressbarListener = null;
		}
		super.onDestroyView();
	}

	/**
	 * need init beford use
	 */
	protected void initSwipeRefresh(View view){
		if(mSwipeRefreshLayout == null){
			mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mswiperefreshlayout);
		}
		setSwipeOnRefresh();
	}

	protected void setSwipeOnRefresh(){
		if(mSwipeRefreshLayout != null){
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

	public void onSwipeRefreshLayoutStart(){
		if(mSwipeRefreshLayout != null){
			mSwipeRefreshLayout.setRefreshing(true);
		}
	}
	
	public void onSwipeRefreshLayoutRefresh(){
	}

	protected void hideKeyBoard() {
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = getActivity().getCurrentFocus();
		if (view == null) {
			view = new View(getActivity());
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

	public void setmProgressbarListener(FragmentProgressbarListener listener){
		mProgressbarListener = listener;
	}

	protected void toActivity(Class mClass, Bundle bundle) {
		Intent intent = new Intent(getContext(), mClass);
		if (bundle != null) {
			intent.putExtra(KeyUtil.BundleKey, bundle);
		}
		startActivity(intent);
	}

}
