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
import com.messi.languagehelper.util.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbums;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbumsList;
import com.ximalaya.ting.android.opensdk.model.album.DiscoveryRecommendAlbums;
import com.ximalaya.ting.android.opensdk.model.album.DiscoveryRecommendAlbumsList;
import com.ximalaya.ting.android.opensdk.model.announcer.AnnouncerCategory;
import com.ximalaya.ting.android.opensdk.model.announcer.AnnouncerCategoryList;

import java.util.HashMap;
import java.util.Map;

import static com.messi.languagehelper.R.id.viewpager;

public class XimalayaLiveFragment extends BaseFragment implements OnClickListener{

	private ViewPager mViewPager;
	private ProgressBar progressBar;
	private TabLayout mTabLayout;

	public static Fragment newInstance(String category, String tag_name){
		XimalayaLiveFragment fragment = new XimalayaLiveFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",category);
		bundle.putString("tag_name",tag_name);
		fragment.setArguments(bundle);
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
		View view = inflater.inflate(R.layout.xmly_live_fragment, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
		mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
		mViewPager = (ViewPager) view.findViewById(viewpager);
		mViewPager.setOffscreenPageLimit(4);
		XmlyAllAdapter allAdapter = new XmlyAllAdapter(getChildFragmentManager());

		mViewPager.setAdapter(allAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
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

	private void test(){
		Map<String, String> map = new HashMap<String, String>();
		CommonRequest.getAnnouncerCategoryList(map, new IDataCallBack<AnnouncerCategoryList>(){
			@Override
			public void onSuccess(@Nullable AnnouncerCategoryList announcerCategoryList) {
				for (AnnouncerCategory category : announcerCategoryList.getList()){
					LogUtil.DefalutLog(category.getVcategoryName());
				}
			}

			@Override
			public void onError(int i, String s) {
			}
		});

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(DTransferConstants.DISPLAY_COUNT, "3");
		CommonRequest.getDiscoveryRecommendAlbums(map1,new IDataCallBack<DiscoveryRecommendAlbumsList>(){
			@Override
			public void onSuccess(@Nullable DiscoveryRecommendAlbumsList discoveryRecommendAlbumsList) {
				for (DiscoveryRecommendAlbums album : discoveryRecommendAlbumsList.getDiscoveryRecommendAlbumses()){
					LogUtil.DefalutLog(album.getCategoryName());
				}
			}

			@Override
			public void onError(int i, String s) {

			}
		});

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(DTransferConstants.CATEGORY_ID, "38");
		map2.put(DTransferConstants.DISPLAY_COUNT ,"5");
		CommonRequest.getCategoryRecommendAlbums(map2, new IDataCallBack<CategoryRecommendAlbumsList>() {
			@Override
			public void onSuccess(@Nullable CategoryRecommendAlbumsList categoryRecommendAlbumsList) {
				for (CategoryRecommendAlbums album : categoryRecommendAlbumsList.getCategoryRecommendAlbumses()){
					LogUtil.DefalutLog("---"+album.getTagName());
				}
			}

			@Override
			public void onError(int i, String s) {

			}
		});
	}



}
