package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ReadingFragment extends BaseFragment implements OnClickListener{

	private RecyclerView listview;
	private Toolbar mToolbar;
	private ProgressBar progressBar;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private int maxRandom;
	private String title;
	private String category;
	private String code;
	private String source;
	private String quest;
	private String type;
	private String boutique_code;
	private boolean isPlayList;
	private boolean isNeedClear = false;
	private LinearLayoutManager mLinearLayoutManager;
	private XXLModel mXXLModel;

	public static Fragment newInstance(Builder builder){
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
		fragment.setArguments(bundle);
		return fragment;
	}

	public static Fragment newInstance(String category, String code){
		ReadingFragment fragment = new ReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",category);
		bundle.putString("code",code);
		fragment.setArguments(bundle);
		return fragment;
	}

	public static Fragment newInstance(String category, String code, boolean isPlayList){
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

	public static Fragment newInstanceByType(String type,int maxRandom,boolean isNeedClear){
		ReadingFragment fragment = new ReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type",type);
		bundle.putInt("maxRandom",maxRandom);
		bundle.putBoolean("isNeedClear",isNeedClear);
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
		this.maxRandom = mBundle.getInt("maxRandom");
		this.isNeedClear = mBundle.getBoolean("isNeedClear",false);
		this.isPlayList = mBundle.getBoolean("isPlayList",false);
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
		new QueryTask(this).execute();
		getMaxPageNumberBackground();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LogUtil.DefalutLog("onCreateView:"+category);
		View view = inflater.inflate(R.layout.reading_fragment, container, false);
		initViews(view);
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
		avObjects = new ArrayList<Reading>();
		mXXLModel = new XXLModel(getActivity());
		avObjects.addAll(BoxHelper.getReadingList(0,Setings.page_size,category,"",code));
		initSwipeRefresh(view);
		mAdapter = new RcReadingListAdapter(avObjects,isPlayList);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
		mXXLModel.setAdapter(avObjects,mAdapter);
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		listview.setLayoutManager(mLinearLayoutManager);
		listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
						.build());
		listview.setAdapter(mAdapter);
		setListOnScrollListener();
	}

	private void random(){
		if(!TextUtils.isEmpty(boutique_code)){
			skip = 0;
		}else {
			skip = (int) Math.round(Math.random()*maxRandom);
		}
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
				if(!mXXLModel.loading && mXXLModel.hasMore){
					if ((visible + firstVisibleItem) >= total){
						new QueryTask(ReadingFragment.this).execute();
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
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask(this).execute();
	}

	private void loadAD(){
		if (mXXLModel != null) {
			mXXLModel.showAd();
		}
	}
	
	private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

		private WeakReference<ReadingFragment> mainActivity;

		public QueryTask(ReadingFragment mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
			if(mXXLModel != null){
				mXXLModel.loading = true;
			}
		}
		
		@Override
		protected List<AVObject> doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
			if(!TextUtils.isEmpty(category)){
				query.whereEqualTo(AVOUtil.Reading.category, category);
			}
			if(!TextUtils.isEmpty(source)){
				query.whereEqualTo(AVOUtil.Reading.source_name, source);
			}
			if(!TextUtils.isEmpty(quest)){
				query.whereContains(AVOUtil.Reading.title, quest);
			}
			if(!TextUtils.isEmpty(type)){
				query.whereEqualTo(AVOUtil.Reading.type, type);
			}
			if(!TextUtils.isEmpty(boutique_code)){
				query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
			}
			if(!TextUtils.isEmpty(code)){
				if(!code.equals("1000")){
					query.whereEqualTo(AVOUtil.Reading.type_id, code);
				}
			}
			query.addDescendingOrder(AVOUtil.Reading.publish_time);
			query.skip(skip);
			query.limit(Setings.page_size);
			try {
				return query.find();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<AVObject> avObject) {
			if(mainActivity.get() != null){
				LogUtil.DefalutLog("onPostExecute---");
				mXXLModel.loading = false;
				hideProgressbar();
				onSwipeRefreshLayoutFinish();
				if(avObject != null){
					if(avObject.size() == 0){
						ToastUtil.diaplayMesShort(getContext(), "没有了！");
						hideFooterview();
						mXXLModel.hasMore = false;
					}else{
						if(avObjects != null && mAdapter != null){
							if(skip == 0){
								avObjects.clear();
							}
							if(isNeedClear){
								isNeedClear = false;
								avObjects.clear();
							}
							DataUtil.changeDataToReading(avObject,avObjects,false);
							mAdapter.notifyDataSetChanged();
							loadAD();
							if(avObject.size() < Setings.page_size){
								LogUtil.DefalutLog("avObject.size() < Settings.page_size");
								hideFooterview();
								mXXLModel.hasMore = false;
							}else {
								showFooterview();
								mXXLModel.hasMore = true;
							}
						}
					}
					skip += Setings.page_size;
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

	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void getMaxPageNumberBackground(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
					if(!TextUtils.isEmpty(category)){
						query.whereEqualTo(AVOUtil.Reading.category, category);
					}
					if(!TextUtils.isEmpty(source)){
						query.whereEqualTo(AVOUtil.Reading.source_name, source);
					}
					if(!TextUtils.isEmpty(quest)){
						query.whereContains(AVOUtil.Reading.title, quest);
					}
					if(!TextUtils.isEmpty(type)){
						query.whereEqualTo(AVOUtil.Reading.type, type);
					}
					if(!TextUtils.isEmpty(code)){
						if(!code.equals("1000")){
							query.whereEqualTo(AVOUtil.Reading.type_id, code);
						}
					}
					maxRandom =  query.count()-100;
					LogUtil.DefalutLog("category:"+category+"---maxRandom:"+maxRandom);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
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
		if(mXXLModel != null){
			mXXLModel.onDestroy();
		}
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
		public Builder isNeedClear(boolean isNeedClear) {
			this.isNeedClear = isNeedClear;
			return this;
		}
		public Fragment build(){
			return ReadingFragment.newInstance(this);
		}
	}
}
