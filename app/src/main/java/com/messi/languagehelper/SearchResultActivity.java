package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResultActivity extends BaseActivity{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private String quest;
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private Reading mADObject;
	private LinearLayoutManager mLinearLayoutManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_activity);
		registerBroadcast();
		initViews();
		loadAD();
		new QueryTask().execute();
	}

	private void initViews(){
		quest = getIntent().getStringExtra(KeyUtil.SearchKey);
		avObjects = new ArrayList<Reading>();
		initSwipeRefresh();
		listview = (RecyclerView) findViewById(R.id.listview);
		mAdapter = new RcReadingListAdapter(avObjects);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(this);
		listview.setLayoutManager(mLinearLayoutManager);
		listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(this)
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
						.build());
		listview.setAdapter(mAdapter);
		setListOnScrollListener();
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
				if(!loading && hasMore){
					if ((visible + firstVisibleItem) >= total){
						loadAD();
						new QueryTask().execute();
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
							NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
							boolean isShow = mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
							LogUtil.DefalutLog("onExposured:"+isShow);
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
		loadAD();
		hideFooterview();
		skip = 0;
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask().execute();
	}

	private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
			loading = true;
		}

		@Override
		protected List<AVObject> doInBackground(Void... params) {
			AVQuery<AVObject> query = null;
			if(quest.toLowerCase().equals(quest.toUpperCase())){
				query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
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
			try {
				return query.find();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<AVObject> avObject) {
			loading = false;
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
			if(avObject != null){
				if(avObject.size() == 0){
					ToastUtil.diaplayMesShort(SearchResultActivity.this, "没有了！");
					hasMore = false;
					hideFooterview();
				}else{
					if(skip == 0){
						avObjects.clear();
					}
					StudyFragment.changeData(avObject,avObjects);
					if(addAD()){
						mAdapter.notifyDataSetChanged();
					}
					if(avObject.size() == Setings.page_size){
						skip += Setings.page_size;
						showFooterview();
						hasMore = true;
					}else {
						hasMore = false;
						hideFooterview();
					}
				}
			}else{
				ToastUtil.diaplayMesShort(SearchResultActivity.this, "加载失败，下拉可刷新");
			}
		}
	}

	private void loadAD(){
		nativeAd = new IFLYNativeAd(this, ADUtil.XXLAD, new IFLYNativeListener() {
			@Override
			public void onConfirm() {
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onAdFailed(AdError arg0) {
				LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
				if(ADUtil.isHasLocalAd()){
					onADLoaded(ADUtil.getRandomAdList());
				}
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> adList) {
				if(adList != null && adList.size() > 0){
					NativeADDataRef nad = adList.get(0);
					mADObject = new Reading();
					mADObject.setmNativeADDataRef(nad);
					mADObject.setAd(true);
					if(!loading){
						addAD();
					}
				}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd(1);
	}

	private boolean addAD(){
		if(mADObject != null && avObjects != null && avObjects.size() > 0){
			int index = avObjects.size() - Setings.page_size + NumberUtil.randomNumberRange(1, 2);
			if(index < 0){
				index = 1;
			}
			avObjects.add(index,mADObject);
			mAdapter.notifyDataSetChanged();
			mADObject = null;
			return false;
		}else{
			return true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}

}
