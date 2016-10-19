package com.messi.languagehelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.adapter.WechatJXAdapter;
import com.messi.languagehelper.dao.WechatJX;
import com.messi.languagehelper.dao.WechatJXResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class WechatJXFragment extends BaseFragment implements OnClickListener{

	private ListView listview;
	private WechatJXAdapter mAdapter;
	private List<WechatJXResult> mWechatJXItem;
	private int currentPage = 1;
	private View footerview;
	private boolean misVisibleToUser;
	private boolean isHasLoadOnce;
	private Activity mContext;

	public static WechatJXFragment getInstance(Activity mContext){
		WechatJXFragment fragment = new WechatJXFragment();
		fragment.setmContext(mContext);
		return fragment;
	}

	public void setmContext(Activity mContext) {
		this.mContext = mContext;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.DefalutLog("setUserVisibleHint:"+isVisibleToUser);
		misVisibleToUser = isVisibleToUser;
		if(!isHasLoadOnce && isVisibleToUser){
			isHasLoadOnce = true;
			requestData();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.joke_budejie_fragment, container, false);
		initViews(view);
		initFooterview(inflater);
		return view;
	}
	
	private void initViews(View view){
		mWechatJXItem = new ArrayList<WechatJXResult>();
		listview = (ListView) view.findViewById(R.id.listview);
		initSwipeRefresh(view);
		mAdapter = new WechatJXAdapter(getContext(), mWechatJXItem);
		setListOnScrollListener();
	}
	
	public void setListOnScrollListener(){
		listview.setOnScrollListener(new OnScrollListener() {  
            private int lastItemIndex;//当前ListView中最后一个Item的索引  
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) { 
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mAdapter.getCount() - 1) {  
//                	LogUtil.DefalutLog(logContent);
                	requestData();
                }  
            }  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  
                lastItemIndex = firstVisibleItem + visibleItemCount - 2;  
            }  
        });
	}
	
	private void initFooterview(LayoutInflater inflater){
		footerview = inflater.inflate(R.layout.footerview, null, false);
		listview.addFooterView(footerview);
		hideFooterview();
		listview.setAdapter(mAdapter);
	}
	
	private void showFooterview(){
		footerview.setVisibility(View.VISIBLE);  
		footerview.setPadding(0, ScreenUtil.dip2px(getContext(), 15), 0, ScreenUtil.dip2px(getContext(), 15));  
	}
	
	private void hideFooterview(){
		footerview.setVisibility(View.GONE);  
		footerview.setPadding(0, -footerview.getHeight(), 0, 0);  
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		currentPage = 1;
		requestData();
	}
	
	private void requestData(){
		showProgressbar();
		LanguagehelperHttpClient.get(Settings.WechatJXUrl+currentPage, new UICallback(mContext){
			@Override
			public void onResponsed(String responseString){
				try {
					if(JsonParser.isJson(responseString)){
						WechatJX mRoot = JSON.parseObject(responseString, WechatJX.class);
						if(mRoot.getCode() == 200){
							if(currentPage == 1){
								mWechatJXItem.clear();
							}
							mWechatJXItem.addAll(mRoot.getNewslist());
							currentPage++;
							showFooterview();
							mAdapter.notifyDataSetChanged();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailured() {
				ToastUtil.diaplayMesShort(getContext(),getActivity().getResources().getString(R.string.network_error));
			}
			@Override
			public void onFinished() {
				onSwipeRefreshLayoutFinish();
				hideProgressbar();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	
	
}
