package com.messi.languagehelper;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.adapter.ReadingListAdapter;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.views.DividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ReadingsActivity extends BaseActivity implements OnClickListener{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<AVObject> avObjects;
	private int skip = 0;
	private int maxRandom;
	private String category;
	private String type;
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private LinearLayoutManager mLinearLayoutManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_activity);
		initViews();
		new QueryTask().execute();
		getMaxPageNumberBackground();
	}

	private void initViews(){
		category = getIntent().getStringExtra(KeyUtil.Category);
		type = getIntent().getStringExtra(KeyUtil.NewsType);
		avObjects = new ArrayList<AVObject>();
		initSwipeRefresh();
		listview = (RecyclerView) findViewById(R.id.listview);
		mAdapter = new RcReadingListAdapter(avObjects);
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
						new QueryTask().execute();
					}
				}
			}
		});
	}

	private void isADInList(RecyclerView view,int first, int vCount){
		if(avObjects.size() > 3){
			for(int i=first;i< (first+vCount);i++){
				if(i < avObjects.size()){
					AVObject mAVObject = avObjects.get(i);
					if(mAVObject != null && mAVObject.get(KeyUtil.ADKey) != null){
						if(!(Boolean) mAVObject.get(KeyUtil.ADIsShowKey)){
							NativeADDataRef mNativeADDataRef = (NativeADDataRef) mAVObject.get(KeyUtil.ADKey);
							mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
							mAVObject.put(KeyUtil.ADIsShowKey, true);
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
		mAdapter.setItems(avObjects);
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
				List<AVObject> avObject  = query.find();
				return avObject;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<AVObject> avObject) {
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
			if(avObject != null){
				if(avObject.size() == 0){
					ToastUtil.diaplayMesShort(ReadingsActivity.this, "没有了！");
					hideFooterview();
				}else{
					loadAD();
					avObjects.addAll(avObject);
					mAdapter.setItems(avObjects);
					mAdapter.notifyDataSetChanged();
					skip += Settings.page_size;
					showFooterview();
				}
			}else{
				ToastUtil.diaplayMesShort(ReadingsActivity.this, "加载失败，下拉可刷新");
			}
			if(skip == maxRandom){
				hasMore = false;
			}
			loading = false;
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
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> adList) {
				if(adList != null && adList.size() > 0){
					NativeADDataRef nad = adList.get(0);
					AVObject mAVObject = new AVObject();
					mAVObject.put(KeyUtil.ADKey, nad);
					mAVObject.put(KeyUtil.ADIsShowKey, false);
					int index = avObjects.size() - Settings.page_size + NumberUtil.randomNumberRange(2, 4);
					if(index < 0){
						index = 0;
					}
					avObjects.add(index,mAVObject);
					mAdapter.notifyItemInserted(index);
				}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd(1);
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


}
