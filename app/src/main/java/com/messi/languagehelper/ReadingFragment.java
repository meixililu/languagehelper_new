package com.messi.languagehelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.adapter.ReadingListAdapter;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ReadingFragment extends BaseFragment implements OnClickListener{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private int skip = 0;
	private String category;
	private String code;
	private int maxRandom;
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private Reading mADObject;
	private LinearLayoutManager mLinearLayoutManager;

	public static Fragment newInstance(String category, String code){
		ReadingFragment fragment = new ReadingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",category);
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
		this.category = mBundle.getString("category");
		this.code = mBundle.getString("code");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.DefalutLog("onAttach");
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		loadAD();
		new QueryTask().execute();
		getMaxPageNumberBackground();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LogUtil.DefalutLog("onCreateView:"+category);
		View view = inflater.inflate(R.layout.composition_fragment, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		listview = (RecyclerView) view.findViewById(R.id.listview);
		avObjects = new ArrayList<Reading>();
		avObjects.addAll(DataBaseUtil.getInstance().getReadingList(Settings.page_size,category,"",code));
		initSwipeRefresh(view);
		mAdapter = new RcReadingListAdapter(avObjects);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
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
		skip = (int) Math.round(Math.random()*maxRandom);
		LogUtil.DefalutLog("skip:"+skip);
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
							boolean isExposure = mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
							LogUtil.DefalutLog("isExposure:"+isExposure);
							mAVObject.setAdShow(isExposure);
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
		loadAD();
		hideFooterview();
		random();
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask().execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading = true;
			showProgressbar();
		}
		
		@Override
		protected List<AVObject> doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
			if(!TextUtils.isEmpty(category)){
				query.whereEqualTo(AVOUtil.Reading.category, category);
			}
			if(!TextUtils.isEmpty(code)){
				if(!code.equals("1000")){
					query.whereEqualTo(AVOUtil.Reading.type_id, code);
				}
			}
			query.addDescendingOrder(AVOUtil.Reading.publish_time);
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
			LogUtil.DefalutLog("onPostExecute---");
			loading = false;
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
			if(avObject != null){
				if(avObject.size() == 0){
					ToastUtil.diaplayMesShort(getContext(), "没有了！");
					hideFooterview();
				}else{
					if(avObjects != null && mAdapter != null){
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
				}
			}
			if(skip == maxRandom){
				hasMore = false;
			}
		}
	}
	
	private void loadAD(){
		nativeAd = new IFLYNativeAd(getContext(), ADUtil.randomAd(), new IFLYNativeListener() {
			@Override
			public void onConfirm() {
			}
			@Override
			public void onCancel() {
			}
			@Override
			public void onAdFailed(AdError arg0) {
				loadADBackup();
				LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> adList) {
				LogUtil.DefalutLog("onADLoaded---");
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

	private void loadADBackup(){
		nativeAd = new IFLYNativeAd(getContext(), ADUtil.XXLAD, new IFLYNativeListener() {
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
				LogUtil.DefalutLog("onADLoaded---");
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
			int index = avObjects.size() - Settings.page_size + NumberUtil.randomNumberRange(2, 4);
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
					if(!TextUtils.isEmpty(code)){
						if(!code.equals("1000")){
							query.whereEqualTo(AVOUtil.Reading.type_id, code);
						}
					}
					maxRandom =  query.count() / Settings.page_size; 
					LogUtil.DefalutLog("category:"+category+"---maxRandom:"+maxRandom);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}
}
