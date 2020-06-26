package com.messi.languagehelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CollectedData;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.databinding.ReadingActivityBinding;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ReadingsActivity extends BaseActivity {

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
	private ReadingActivityBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ReadingActivityBinding.inflate(LayoutInflater.from(this));
		setContentView(binding.getRoot());
		registerBroadcast();
		initViews();
		queryTask();
		initCollectedButton();
		getMaxPageNumberBackground();
	}

	private void initViews(){
		category = getIntent().getStringExtra(KeyUtil.Category);
		type = getIntent().getStringExtra(KeyUtil.NewsType);
		source = getIntent().getStringExtra(KeyUtil.NewsSource);
		boutique_code = getIntent().getStringExtra(KeyUtil.BoutiqueCode);
		mBoutiquesBean = getIntent().getParcelableExtra(KeyUtil.ObjectKey);
		avObjects = new ArrayList<Reading>();
		mXXLModel = new XXLModel(this);
		avObjects.addAll(BoxHelper.getReadingList(0,Setings.page_size,category,type,""));
		initSwipeRefresh();
		mAdapter = new RcReadingListAdapter(avObjects);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
		mXXLModel.setAdapter(avObjects,mAdapter);
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(this);
		binding.listview.setLayoutManager(mLinearLayoutManager);
		binding.listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(this)
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		binding.listview.setAdapter(mAdapter);
		binding.collectBtn.setOnClickListener(view -> collectedOrUncollected());
		setListOnScrollListener();
		updateDataBackground(mBoutiquesBean);
	}

	private void updateDataBackground(BoutiquesBean mBoutiquesBean){
		if (mBoutiquesBean != null && !TextUtils.isEmpty(mBoutiquesBean.getObjectId())) {
			new Thread(() -> {
				AVObject mBoutiques = AVObject.createWithoutData(AVOUtil.Boutiques.Boutiques,
						mBoutiquesBean.getObjectId());
				mBoutiques.increment(AVOUtil.Boutiques.views);
				mBoutiques.save();
			}).start();
		}
	}

	private void random(){
		if(!TextUtils.isEmpty(boutique_code)){
			skip = 0;
		}else {
			skip = (int) Math.round(Math.random()*maxRandom);
		}
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

	private void initCollectedButton(){
		if (mBoutiquesBean != null && !TextUtils.isEmpty(mBoutiquesBean.getCode())) {
			binding.volumeImg.setVisibility(View.VISIBLE);
			if(BoxHelper.isCollected(mBoutiquesBean.getCode())){
				binding.volumeImg.setImageResource(R.drawable.ic_collected_white);
				binding.volumeImg.setTag(true);
			}else {
				binding.volumeImg.setImageResource(R.drawable.ic_uncollected_white);
				binding.volumeImg.setTag(false);
			}
		} else {
			binding.volumeImg.setVisibility(View.GONE);
		}
	}

	private void collectedOrUncollected(){
		boolean tag = !(boolean)binding.volumeImg.getTag();
		binding.volumeImg.setTag(tag);
		if(mBoutiquesBean != null){
			if(tag){
				binding.volumeImg.setImageResource(R.drawable.ic_collected_white);
				ToastUtil.diaplayMesShort(this,"已收藏");
			}else {
				binding.volumeImg.setImageResource(R.drawable.ic_uncollected_white);
				ToastUtil.diaplayMesShort(this,"取消收藏");
			}
			CollectedData cdata = new CollectedData();
			cdata.setObjectId(mBoutiquesBean.getCode());
			if(tag){
				cdata.setName(mBoutiquesBean.getTitle());
				cdata.setType(AVOUtil.Boutiques.Boutiques);
				cdata.setJson(JSON.toJSONString(mBoutiquesBean));
				BoxHelper.insert(cdata);
			}else {
				BoxHelper.remove(cdata);
			}
			LiveEventBus.get(KeyUtil.UpdateCollectedData).post("");
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
		query.findInBackground().subscribe(new Observer<List<AVObject>>() {
			@Override
			public void onSubscribe(Disposable d) {
			}
			@Override
			public void onNext(List<AVObject> avObjects) {
				onPostExecute(avObjects);
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

	protected void onPostExecute(List<AVObject> avObject) {
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

	@Override
	protected void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	private void getMaxPageNumberBackground(){
		new Thread(() -> {
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
