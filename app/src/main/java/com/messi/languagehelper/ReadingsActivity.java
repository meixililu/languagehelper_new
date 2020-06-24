package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
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

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class ReadingsActivity extends BaseActivity implements OnClickListener{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private int maxRandom;
	private String category;
	private String type;
	private String source;
	private String boutique_code;
	private LinearLayoutManager mLinearLayoutManager;
	private XXLModel mXXLModel;
	private BoutiquesBean mBoutiquesBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_activity);
		registerBroadcast();
		initViews();
		new QueryTask(this).execute();
		getMaxPageNumberBackground();
	}

	private void initViews(){
		category = getIntent().getStringExtra(KeyUtil.Category);
		type = getIntent().getStringExtra(KeyUtil.NewsType);
		source = getIntent().getStringExtra(KeyUtil.NewsSource);
		boutique_code = getIntent().getStringExtra(KeyUtil.BoutiqueCode);
		mBoutiquesBean = getIntent().getParcelableExtra(KeyUtil.ObjectKey);
		LogUtil.DefalutLog(JSON.toJSONString(mBoutiquesBean));

		avObjects = new ArrayList<Reading>();
		mXXLModel = new XXLModel(this);
		avObjects.addAll(BoxHelper.getReadingList(0,Setings.page_size,category,type,""));
		initSwipeRefresh();
		listview = (RecyclerView) findViewById(R.id.listview);
		mAdapter = new RcReadingListAdapter(avObjects);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
		mXXLModel.setAdapter(avObjects,mAdapter);
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(this);
		listview.setLayoutManager(mLinearLayoutManager);
		listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(this)
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
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
						new QueryTask(ReadingsActivity.this).execute();
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

		private WeakReference<ReadingsActivity> mainActivity;

		public QueryTask(ReadingsActivity mActivity){
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
			if(!TextUtils.isEmpty(type)){
				query.whereEqualTo(AVOUtil.Reading.type, type);
			}
			if(!TextUtils.isEmpty(source)){
				query.whereEqualTo(AVOUtil.Reading.source_name, source);
			}
			if(!TextUtils.isEmpty(boutique_code)){
				query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
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
				mXXLModel.loading = false;
				hideProgressbar();
				onSwipeRefreshLayoutFinish();
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
						skip += Setings.page_size;
						if(avObject.size() < Setings.page_size){
							mXXLModel.hasMore = false;
							hideFooterview();
						}else {
							mXXLModel.hasMore = true;
							showFooterview();
						}
					}
				}else{
					ToastUtil.diaplayMesShort(ReadingsActivity.this, "加载失败，下拉可刷新");
				}
			}
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
					if(!TextUtils.isEmpty(source)){
						query.whereEqualTo(AVOUtil.Reading.source_name, source);
					}
					if(!TextUtils.isEmpty(boutique_code)){
						query.whereEqualTo(AVOUtil.Reading.boutique_code, boutique_code);
					}
					maxRandom = query.count();
					maxRandom /= Setings.page_size;
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
		if(mXXLModel != null){
			mXXLModel.onDestroy();
		}
	}

}
