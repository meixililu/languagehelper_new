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
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.messi.languagehelper.adapter.ToutiaoNewsAdapter;
import com.messi.languagehelper.dao.ToutiaoNews;
import com.messi.languagehelper.dao.ToutiaoNewsItem;
import com.messi.languagehelper.dao.ToutiaoNewsResult;
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

public class ToutiaoNewsFragment extends BaseFragment implements OnClickListener{

	private ListView listview;
	private ToutiaoNewsAdapter mAdapter;
	private List<ToutiaoNewsItem> mWechatJXItem;
	private String type;
	private boolean misVisibleToUser;
	private boolean isHasLoadOnce;
//	private int currentPage = 1;
//	private View footerview;
	//top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)

	private Activity mContext;
	public void setType(String type) {
		this.type = type;
	}

	public static ToutiaoNewsFragment getInstance(String type,Activity mContext){
		ToutiaoNewsFragment fragment = new ToutiaoNewsFragment();
		fragment.setmContext(mContext);
		fragment.setType(type);
		return fragment;
	}

	public void setmContext(Activity mContext) {
		this.mContext = mContext;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.joke_budejie_fragment, container, false);
		initViews(view);
//		initFooterview(inflater);
		return view;
	}
	
	private void initViews(View view){
		mWechatJXItem = new ArrayList<ToutiaoNewsItem>();
		listview = (ListView) view.findViewById(R.id.listview);
		initSwipeRefresh(view);
		mAdapter = new ToutiaoNewsAdapter(getContext(), mWechatJXItem);
		listview.setAdapter(mAdapter);
//		setListOnScrollListener();
	}
	
//	public void setListOnScrollListener(){
//		listview.setOnScrollListener(new OnScrollListener() {
//            private int lastItemIndex;//当前ListView中最后一个Item的索引
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mAdapter.getCount() - 1) {
//                	requestData();
//                }
//            }
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                lastItemIndex = firstVisibleItem + visibleItemCount - 2;
//            }
//        });
//	}
	
//	private void initFooterview(LayoutInflater inflater){
//		footerview = inflater.inflate(R.layout.footerview, null, false);
//		listview.addFooterView(footerview);
//		hideFooterview();
//	}
//
//	private void showFooterview(){
//		footerview.setVisibility(View.VISIBLE);
//		footerview.setPadding(0, ScreenUtil.dip2px(getContext(), 15), 0, ScreenUtil.dip2px(getContext(), 15));
//	}
//
//	private void hideFooterview(){
//		footerview.setVisibility(View.GONE);
//		footerview.setPadding(0, -footerview.getHeight(), 0, 0);
//	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
//		currentPage = 1;
		requestData();
	}
	
	private void requestData(){
		showProgressbar();
		RequestBody formBody = new FormEncodingBuilder()
				.add("key", "06bc3d4c6f31f4a2c8794ef1ad45517f")
				.add("type", type)
				.build();
		LanguagehelperHttpClient.post(Settings.ToutiaoNewsUrl, formBody, new UICallback(mContext){
			@Override
			public void onResponsed(String responseString){
				try {
					if(JsonParser.isJson(responseString)){
						ToutiaoNews mRoot = JSON.parseObject(responseString, ToutiaoNews.class);
						if(mRoot.getError_code() == 0){
							ToutiaoNewsResult mToutiaoNewsResult = mRoot.getResult();
							mWechatJXItem.clear();
							mWechatJXItem.addAll(mToutiaoNewsResult.getData());
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
