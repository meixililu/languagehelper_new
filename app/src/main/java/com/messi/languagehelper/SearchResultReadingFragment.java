package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.databinding.XmlySearchReasultFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchResultReadingFragment extends BaseFragment{

	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private String quest;
	private XXLModel mXXLModel;
	private LinearLayoutManager mLinearLayoutManager;
	private XmlySearchReasultFragmentBinding binding;

	public static Fragment newInstance(String search_text, String title) {
		SearchResultReadingFragment fragment = new SearchResultReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString(KeyUtil.SearchKey, search_text);
		bundle.putString(KeyUtil.ActionbarTitle, title);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onAttach(Context activity) {
		super.onAttach(activity);
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle mBundle = getArguments();
		this.quest = mBundle.getString(KeyUtil.SearchKey);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = XmlySearchReasultFragmentBinding.inflate(inflater);
		registerBroadcast();
		initSwipeRefresh(binding.getRoot());
		initViews();
		queryTask();
		return binding.getRoot();
	}

	private void initViews(){
		mXXLModel = new XXLModel(getContext());
		avObjects = new ArrayList<Reading>();
		mAdapter = new RcReadingListAdapter(avObjects);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
		mXXLModel.setAdapter(avObjects,mAdapter);
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		binding.listview.setLayoutManager(mLinearLayoutManager);
		binding.listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		binding.listview.setAdapter(mAdapter);
		setListOnScrollListener();
	}

	public void setListOnScrollListener(){
		binding.listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int visible  = mLinearLayoutManager.getChildCount();
				int total = mLinearLayoutManager.getItemCount();
				int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
				isADInList(recyclerView,firstVisibleItem,visible);
				if(!mXXLModel.loading && mXXLModel.hasMore){
					if ((visible + firstVisibleItem) >= total){
						queryTask();
					}
				}
			}
		});
	}

	private void isADInList(RecyclerView view,int first, int vCount){
		if(avObjects.size() > 3){
			for(int i=first;i< (first+vCount);i++){
				if(i < avObjects.size() && i > 0){
					Reading mAVObject = avObjects.get(i);
					if(mAVObject != null && mAVObject.isAd()){
						if(!mAVObject.isAdShow()){
							NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
							boolean isShow = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
							LogUtil.DefalutLog("onExposure:"+isShow);
							if(isShow){
								mAVObject.setAdShow(isShow);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateUI(String music_action) {
		if(music_action.equals(PlayerService.action_loading)){
			showProgressbar();
		}else if(music_action.equals(PlayerService.action_finish_loading)){
			hideProgressbar();
		}else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void hideFooterview(){
		mAdapter.hideFooter();
	}

	private void showFooterview(){
		mAdapter.showFooter();
	}

	@Override
	public void onSwipeRefreshLayoutRefresh() {
		hideFooterview();
		skip = 0;
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		queryTask();
	}

	private void loadAD(){
		if (mXXLModel != null) {
			mXXLModel.showAd();
		}
	}

	private void queryTask(){
		showProgressbar();
		if(mXXLModel != null){
			mXXLModel.loading = true;
		}
		AVQuery<AVObject> query = null;
		if(quest.toLowerCase().equals(quest.toUpperCase())){
			query = new AVQuery<>(AVOUtil.Reading.Reading);
			query.whereContains(AVOUtil.Reading.title, quest);
			query.addDescendingOrder(AVOUtil.Reading.publish_time);
		}else {
			AVQuery<AVObject> priorityQuery = new AVQuery<>(AVOUtil.Reading.Reading);
			priorityQuery.whereContains(AVOUtil.Reading.title, quest.toLowerCase());
			priorityQuery.addDescendingOrder(AVOUtil.Reading.publish_time);

			AVQuery<AVObject> statusQuery = new AVQuery<>(AVOUtil.Reading.Reading);
			statusQuery.whereContains(AVOUtil.Reading.title, quest.toUpperCase());
			statusQuery.addDescendingOrder(AVOUtil.Reading.publish_time);
			query = AVQuery.or(Arrays.asList(priorityQuery, statusQuery));
		}
		query.skip(skip);
		query.limit(Setings.page_size);
		query.findInBackground().subscribe(new Observer<List<AVObject>>() {
			@Override
			public void onSubscribe(Disposable d) {
			}
			@Override
			public void onNext(List<AVObject> avObject) {
				if(avObject != null){
					if(avObject.size() == 0){
						mXXLModel.hasMore = false;
						hideFooterview();
					}else{
						if(skip == 0){
							avObjects.clear();
						}
						DataUtil.changeDataToReading(avObject,avObjects,false);
						mAdapter.notifyDataSetChanged();
						loadAD();
						if(avObject.size() == Setings.page_size){
							skip += Setings.page_size;
							showFooterview();
							mXXLModel.hasMore = true;
						}else {
							mXXLModel.hasMore = false;
							hideFooterview();
						}
					}
				}else{
					ToastUtil.diaplayMesShort(getContext(), "加载失败，下拉可刷新");
				}
			}
			@Override
			public void onError(Throwable e) {
			}
			@Override
			public void onComplete() {
				mXXLModel.loading = false;
				hideProgressbar();
				onSwipeRefreshLayoutFinish();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
		if(mXXLModel != null){
			mXXLModel.onDestroy();
		}
	}

}
