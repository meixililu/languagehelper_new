package com.messi.languagehelper;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.viewmodels.ReadingListViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class ReadingFragment extends BaseFragment{

	private RecyclerView listview;
	private Toolbar mToolbar;
	private ProgressBar progressBar;
	private RcReadingListAdapter mAdapter;
	private int skip = 0;
	private int maxRandom;
	private String title;
	private String category;
	private String code;
	private String source;
	private String quest;
	private String type;
	private String boutique_code;
	private boolean withOutVideo;
	private boolean isNeedClear = false;
	private LinearLayoutManager mLinearLayoutManager;
	private ReadingListViewModel viewModel;

	public static ReadingFragment newInstance(Builder builder){
		ReadingFragment fragment = new ReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",builder.category);
		bundle.putString(KeyUtil.ActionbarTitle,builder.title);
		bundle.putString(KeyUtil.BoutiqueCode,builder.boutique_code);
		bundle.putString("code",builder.code);
		bundle.putString("source",builder.source);
		bundle.putBoolean("isPlayList",builder.isPlayList);
		bundle.putString("type",builder.type);
		bundle.putString("quest",builder.quest);
		bundle.putInt("maxRandom",builder.maxRandom);
		bundle.putBoolean("isNeedClear",builder.isNeedClear);
		bundle.putBoolean("withOutVideo",builder.withOutVideo);
		fragment.setArguments(bundle);
		return fragment;
	}

	public static ReadingFragment newInstance(String category, String code){
		ReadingFragment fragment = new ReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",category);
		bundle.putString("code",code);
		fragment.setArguments(bundle);
		return fragment;
	}

	public static ReadingFragment newInstance(String category, String code, boolean isPlayList){
		ReadingFragment fragment = new ReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",category);
		bundle.putBoolean("isPlayList",isPlayList);
		if(!TextUtils.isEmpty(code)){
			bundle.putString("code",code);
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerBroadcast();
		Bundle mBundle = getArguments();
		this.title = mBundle.getString(KeyUtil.ActionbarTitle);
		this.category = mBundle.getString("category");
		this.code = mBundle.getString("code");
		this.source = mBundle.getString("source");
		this.quest = mBundle.getString("quest");
		this.type = mBundle.getString("type");
		this.boutique_code = mBundle.getString(KeyUtil.BoutiqueCode);
		this.isNeedClear = mBundle.getBoolean("isNeedClear",false);
		this.withOutVideo = mBundle.getBoolean("withOutVideo",false);
		this.maxRandom = mBundle.getInt("maxRandom");
		viewModel = ViewModelProviders.of(getActivity()).get(ReadingListViewModel.class);
		viewModel.init();
		viewModel.getRepo().setCategory(category);
		viewModel.getRepo().setCode(code);
		viewModel.getRepo().setSource(source);
		viewModel.getRepo().setQuest(quest);
		viewModel.getRepo().setType(type);
		viewModel.getRepo().setBoutique_code(boutique_code);
		viewModel.getRepo().setNeedClear(isNeedClear);
		viewModel.getRepo().setWithOutVideo(withOutVideo);
	}

	@Override
	public void onAttach(Context activity) {
		super.onAttach(activity);
		LogUtil.DefalutLog("onAttach");
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		if(maxRandom > 0){
			random();
		}
		viewModel.refresh(skip);
		viewModel.count();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LogUtil.DefalutLog("onCreateView:"+category);
		View view = inflater.inflate(R.layout.reading_fragment, container, false);
		initViews(view);
		initViewModel();
		return view;
	}
	
	private void initViews(View view){
		listview = (RecyclerView) view.findViewById(R.id.listview);
		mToolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
		if(!TextUtils.isEmpty(title)){
			mToolbar.setVisibility(View.VISIBLE);
			mToolbar.setTitle(title);
		}
		viewModel.getRepo().getList().addAll(BoxHelper.getReadingList(0,Setings.page_size,category,"",code));
		initSwipeRefresh(view);
		mAdapter = new RcReadingListAdapter(viewModel.getRepo().getList());
		mAdapter.setItems(viewModel.getRepo().getList());
		mAdapter.setFooter(new Object());
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		listview.setLayoutManager(mLinearLayoutManager);
		listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		listview.setAdapter(mAdapter);
		setListOnScrollListener();
	}

	private void initViewModel(){
		viewModel.getReadingList().observe(this, data -> onDataChange(data));
		viewModel.isShowProgressBar().observe(this, isShow -> isShowProgressBar(isShow));
		viewModel.getAD().observe(this,data -> refreshAD(data));
		viewModel.getCount().observe(this,count -> getTotalCount(count));
	}

	private void getTotalCount(int count){
		LogUtil.DefalutLog("ViewModel---getTotalCount---count:"+count);
		maxRandom = count;
	}

	private void refreshAD(RespoADData data){
		LogUtil.DefalutLog("ViewModel---refresh---ad");
		if (data != null) {
			if (data.getCode() == 1) {
				if(mAdapter != null){
					mAdapter.notifyItemInserted(data.getPos());
				}
			}
		}
	}

	private void onDataChange(RespoData data){
		LogUtil.DefalutLog("ViewModel---onDataChange---");
		if (data != null) {
			if (data.getCode() == 1) {
				if(mAdapter != null){
					mAdapter.notifyItemRangeInserted(data.getPositionStart(),data.getItemCount());
				}
			} else {
				ToastUtil.diaplayMesShort(getActivity(),data.getErrStr());
			}
			if (data.isHideFooter()) {
				hideFooterview();
			}else {
				showFooterview();
			}
		}else {
			ToastUtil.diaplayMesShort(getActivity(),"网络异常，请检查网络连接。");
		}
	}

	private void isShowProgressBar(Boolean isShow){
		if (isShow) {
			showProgressbar();
		} else {
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
		}
	}

	private void random(){
		if(!TextUtils.isEmpty(boutique_code)){
			skip = 0;
		}else {
			skip = (int) Math.round(Math.random()*maxRandom);
		}
		LogUtil.DefalutLog("random:"+skip+"--boutique_code:"+boutique_code);
	}
	
	public void setListOnScrollListener(){
		listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int visible  = mLinearLayoutManager.getChildCount();
				int total = mLinearLayoutManager.getItemCount();
				int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
				isADInList(recyclerView,firstVisibleItem,visible);
				if ((visible + firstVisibleItem) >= total){
					viewModel.loadData();
				}
			}
		});
	}
	
	private void isADInList(RecyclerView view,int first, int vCount){
		if(viewModel.getRepo().getList().size() > 3){
			for(int i=first;i< (first+vCount);i++){
				if(i < viewModel.getRepo().getList().size() && i > 0){
					Reading mAVObject = viewModel.getRepo().getList().get(i);
					if(mAVObject != null && mAVObject.isAd()){
						if(!mAVObject.isAdShow()){
							NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
							boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
							LogUtil.DefalutLog("isExposure:"+isExposure);
							if(isExposure){
								mAVObject.setAdShow(isExposure);
							}
						}
					}
				}
			}
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
		random();
		viewModel.refresh(skip);
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

	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void showProgressbar(){
		super.showProgressbar();
		if(progressBar != null){
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideProgressbar() {
		super.hideProgressbar();
		if(progressBar != null){
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}

	public static class Builder {
		private int maxRandom;
		private String title;
		private String category;
		private String code;
		private String source;
		private String quest;
		private String type;
		private String boutique_code;
		private boolean isPlayList;
		private boolean withOutVideo;
		private boolean isNeedClear = false;

		public Builder maxRandom(int maxRandom) {
			this.maxRandom = maxRandom;
			return this;
		}
		public Builder title(String title) {
			this.title = title;
			return this;
		}
		public Builder category(String category) {
			this.category = category;
			return this;
		}
		public Builder code(String code) {
			this.code = code;
			return this;
		}
		public Builder source(String source) {
			this.source = source;
			return this;
		}
		public Builder quest(String quest) {
			this.quest = quest;
			return this;
		}
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		public Builder boutique_code(String boutique_code) {
			this.boutique_code = boutique_code;
			return this;
		}
		public Builder isPlayList(boolean isPlayList) {
			this.isPlayList = isPlayList;
			return this;
		}

        public Builder isWithOutVideo(boolean withOutVideo) {
            this.withOutVideo = withOutVideo;
            return this;
        }

        public Builder isNeedClear(boolean isNeedClear) {
			this.isNeedClear = isNeedClear;
			return this;
		}
		public ReadingFragment build(){
			return ReadingFragment.newInstance(this);
		}
	}
}
