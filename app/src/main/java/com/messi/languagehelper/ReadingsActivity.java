package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TXADUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class ReadingsActivity extends BaseActivity implements OnClickListener{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private int maxRandom;
	private String category;
	private String type;
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private Reading mADObject;
	private LinearLayoutManager mLinearLayoutManager;
	private List<NativeExpressADView> mTXADList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_activity);
		registerBroadcast();
		initViews();
		loadAD();
		new QueryTask().execute();
		getMaxPageNumberBackground();
	}

	private void initViews(){
		category = getIntent().getStringExtra(KeyUtil.Category);
		type = getIntent().getStringExtra(KeyUtil.NewsType);
		avObjects = new ArrayList<Reading>();
		mTXADList = new ArrayList<NativeExpressADView>();
		avObjects.addAll(DataBaseUtil.getInstance().getReadingList(Settings.page_size,category,type,""));
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

	private void random(){
		skip = (int) Math.round(Math.random()*maxRandom);
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
		random();
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask().execute();
	}

	private void loadAD(){
		if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
			loadXFAD();
		}else {
			loadTXAD();
		}
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
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
			if(!TextUtils.isEmpty(category)){
				query.whereEqualTo(AVOUtil.Reading.category, category);
			}
			if(!TextUtils.isEmpty(type)){
				query.whereEqualTo(AVOUtil.Reading.type, type);
			}
			query.addDescendingOrder(AVOUtil.Reading.publish_time);
			query.addDescendingOrder(AVOUtil.Reading.item_id);
			query.skip(skip);
			query.limit(Settings.page_size);
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
					ToastUtil.diaplayMesShort(ReadingsActivity.this, "没有了！");
					hideFooterview();
				}else{
					if(skip == 0){
						avObjects.clear();
					}
					StudyFragment.changeData(avObject,avObjects);
					if(addAD()){
						mAdapter.notifyDataSetChanged();
					}
					skip += Settings.page_size;
					showFooterview();
				}
			}else{
				ToastUtil.diaplayMesShort(ReadingsActivity.this, "加载失败，下拉可刷新");
			}
			if(skip == maxRandom){
				hasMore = false;
			}else {
				hasMore = true;
			}

		}
	}

	private void loadXFAD(){
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
				if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
					loadTXAD();
				}else {
					onADFaile();
				}
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> adList) {
				if(adList != null && adList.size() > 0){
					NativeADDataRef nad = adList.get(0);
					addXFAD(nad);
				}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd(1);
	}

	private void addXFAD(NativeADDataRef nad){
		mADObject = new Reading();
		mADObject.setmNativeADDataRef(nad);
		mADObject.setAd(true);
		if (!loading) {
			addAD();
		}
	}

	private void onADFaile(){
		if(ADUtil.isHasLocalAd()){
			NativeADDataRef nad = ADUtil.getRandomAd();
			addXFAD(nad);
		}
	}

	private void loadTXAD(){
		TXADUtil.showXXL(this, new NativeExpressAD.NativeExpressADListener() {
			@Override
			public void onNoAD(com.qq.e.comm.util.AdError adError) {
				LogUtil.DefalutLog(adError.getErrorMsg());
				if(ADUtil.Advertiser.equals(ADUtil.Advertiser_TX)){
					loadXFAD();
				}else {
					onADFaile();
				}
			}
			@Override
			public void onADLoaded(List<NativeExpressADView> list) {
				LogUtil.DefalutLog("onADLoaded");
				if(list != null && list.size() > 0){
					mTXADList.add(list.get(0));
					mADObject = new Reading();
					mADObject.setmTXADView(list.get(0));
					if (!loading) {
						addAD();
					}
				}
			}
			@Override
			public void onRenderFail(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onRenderFail");
			}
			@Override
			public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onRenderSuccess");
			}
			@Override
			public void onADExposure(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADExposure");
			}
			@Override
			public void onADClicked(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADClicked");
			}
			@Override
			public void onADClosed(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADClosed");
			}
			@Override
			public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADLeftApplication");
			}
			@Override
			public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADOpenOverlay");
			}
			@Override
			public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADCloseOverlay");
			}
		});
	}

	private boolean addAD(){
		if(mADObject != null && avObjects != null && avObjects.size() > 0){
			int index = avObjects.size() - Settings.page_size + NumberUtil.randomNumberRange(1, 2);
			if(index < 0){
				index = 0;
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
	public void onBackPressed() {
		if (JZVideoPlayer.backPress()) {
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		JZVideoPlayer.releaseAllVideos();
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
					if(!TextUtils.isEmpty(type)){
						query.whereEqualTo(AVOUtil.Reading.type, type);
					}
					maxRandom = query.count();
					maxRandom /= Settings.page_size;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
		if(mTXADList != null){
			for(NativeExpressADView adView : mTXADList){
				adView.destroy();
			}
		}
	}

}
