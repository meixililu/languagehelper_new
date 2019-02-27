package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class ReadingsBySubjectActivity extends BaseActivity{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private String subjectName;
	private String level;
	private String recentKey;
	private LinearLayoutManager mLinearLayoutManager;
	private XXLModel mXXLModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_activity);
		registerBroadcast();
		initViews();
		new QueryTask().execute();
	}

	private void initViews(){
		subjectName = getIntent().getStringExtra(KeyUtil.SubjectName);
		level = getIntent().getStringExtra(KeyUtil.LevelKey);
		recentKey = getIntent().getStringExtra(KeyUtil.RecentKey);
		avObjects = new ArrayList<Reading>();
		mXXLModel = new XXLModel(this);
		initSwipeRefresh();
		listview = (RecyclerView) findViewById(R.id.listview);
		mAdapter = new RcReadingListAdapter(avObjects,true);
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
				if(!mXXLModel.loading && mXXLModel.hasMore){
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
		hideFooterview();
		skip = 0;
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask().execute();
	}

	private void loadAD(){
		if(ADUtil.IsShowAD){
			if (mXXLModel != null) {
				mXXLModel.showAd();
			}
		}
	}

	private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

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
			query.whereEqualTo(AVOUtil.Reading.category_2, subjectName);
			if(!TextUtils.isEmpty(level)){
				query.whereEqualTo(AVOUtil.Reading.level, level);
			}
			query.addAscendingOrder(AVOUtil.Reading.item_id);
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
			mXXLModel.loading = false;
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
			if(avObject != null){
				if(avObject.size() == 0){
					ToastUtil.diaplayMesShort(ReadingsBySubjectActivity.this, "没有了！");
					mXXLModel.hasMore = false;
					hideFooterview();
				}else{
					if(skip == 0){
						avObjects.clear();
					}
					StudyFragment.changeData(avObject,avObjects);
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
				ToastUtil.diaplayMesShort(ReadingsBySubjectActivity.this, "加载失败，下拉可刷新");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_done, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add:
				saveRecentKey();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveRecentKey(){
		Setings.saveSharedPreferences(Setings.getSharedPreferences(this),
				recentKey,
				subjectName);
		if (!Setings.getSharedPreferences(this).getBoolean(KeyUtil.isAddRecentGuideShow, false)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
			builder.setTitle("");
			builder.setMessage("已订阅该课程");
			builder.setPositiveButton("确认", null);
			AlertDialog dialog = builder.create();
			dialog.show();
			Setings.saveSharedPreferences(Setings.getSharedPreferences(this), KeyUtil.isAddRecentGuideShow, true);
		}else {
			ToastUtil.diaplayMesShort(this,"已订阅该课程");
		}
	}

	@Override
	public void onBackPressed() {
		if (Jzvd.backPress()) {
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
		Jzvd.releaseAllVideos();
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
