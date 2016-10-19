package com.messi.languagehelper;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.messi.languagehelper.adapter.JokeListAdapter;
import com.messi.languagehelper.dao.BDJContent;
import com.messi.languagehelper.dao.BDJPagebean;
import com.messi.languagehelper.dao.BDJRoot;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class JokeBuDeJieFragment extends BaseFragment implements OnClickListener{

	private ListView listview;
	private JokeListAdapter mAdapter;
	private List<BDJContent> mBDJContent;
	private BDJPagebean pagebean;
	private int currentPage = 1;
	private View footerview;
	private boolean misVisibleToUser;
	private boolean isHasLoadOnce;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.DefalutLog("setUserVisibleHint:"+isVisibleToUser);
		misVisibleToUser = isVisibleToUser;
		if(!isHasLoadOnce && isVisibleToUser){
			isHasLoadOnce = true;
			requestData();
		}
		JCVideoPlayer.releaseAllVideos();
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
		mBDJContent = new ArrayList<BDJContent>();
		listview = (ListView) view.findViewById(R.id.listview);
		initSwipeRefresh(view);
		mAdapter = new JokeListAdapter(getContext(), mBDJContent);
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
		RequestBody formBody = new FormEncodingBuilder()
				.add("showapi_appid", Settings.showapi_appid)
				.add("showapi_sign", Settings.showapi_secret)
				.add("showapi_timestamp", String.valueOf(System.currentTimeMillis()))
				.add("showapi_res_gzip", "1")
				.add("page", String.valueOf(currentPage))
				.build();
		LanguagehelperHttpClient.post(Settings.BudejieUrl, formBody, new UICallback(getActivity()){
			@Override
			public void onResponsed(String responseString){
				try {
					if(JsonParser.isJson(responseString)){
						BDJRoot mRoot = JSON.parseObject(responseString, BDJRoot.class);
						if(mRoot != null && mRoot.getShowapi_res_code() == 0 && mRoot.getShowapi_res_body().getPagebean() != null){
							pagebean = mRoot.getShowapi_res_body().getPagebean();
							if(currentPage == 1){
								mBDJContent.clear();
							}
							mBDJContent.addAll(pagebean.getContentlist());
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

	@Override
	public void onPause() {
		super.onPause();
		JCVideoPlayer.releaseAllVideos();
	}


}
