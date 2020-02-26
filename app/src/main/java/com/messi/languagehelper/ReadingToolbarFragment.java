package com.messi.languagehelper;

import android.content.Context;
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
import android.widget.TextView;

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
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ReadingToolbarFragment extends BaseFragment implements OnClickListener{

	private RecyclerView listview;
	private RcReadingListAdapter mAdapter;
	private List<Reading> avObjects;
	private TextView toolbar_title;
	private int skip = 0;
	private String category;
	private String code;
	private int maxRandom;
	private LinearLayoutManager mLinearLayoutManager;
	private XXLModel mXXLModel;
	public String title;

	public static Fragment newInstance(String title, String category, String code){
		ReadingToolbarFragment fragment = new ReadingToolbarFragment();
		fragment.title = title;
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
	public void onAttach(Context activity) {
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
		new QueryTask(this).execute();
		getMaxPageNumberBackground();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LogUtil.DefalutLog("onCreateView:"+category);
		View view = inflater.inflate(R.layout.listen_home_fragment, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		listview = (RecyclerView) view.findViewById(R.id.listview);
		toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
		toolbar_title.setText(title);
		avObjects = new ArrayList<Reading>();
		mXXLModel = new XXLModel(getActivity());
		avObjects.addAll(BoxHelper.getReadingList(0,Setings.page_size,category,"",code));
		initSwipeRefresh(view);
		mAdapter = new RcReadingListAdapter(avObjects);
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
				if(!mXXLModel.loading && mXXLModel.hasMore){
					if ((visible + firstVisibleItem) >= total){
						new QueryTask(ReadingToolbarFragment.this).execute();
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

		private WeakReference<ReadingToolbarFragment> mainActivity;

		public QueryTask(ReadingToolbarFragment mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mXXLModel != null){
				mXXLModel.loading = true;
			}
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
					}else{
						if(avObjects != null && mAdapter != null){
							if(skip == 0){
								avObjects.clear();
							}
							DataUtil.changeDataToReading(avObject,avObjects,false);
							mAdapter.notifyDataSetChanged();
							loadAD();
							skip += Setings.page_size;
							showFooterview();
						}
					}
				}
				if(skip == maxRandom){
					mXXLModel.hasMore = false;
				}else {
					mXXLModel.hasMore = true;
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
					if(!TextUtils.isEmpty(code)){
						if(!code.equals("1000")){
							query.whereEqualTo(AVOUtil.Reading.type_id, code);
						}
					}
					maxRandom =  query.count() / Setings.page_size;
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
		if(mXXLModel != null){
			mXXLModel.onDestroy();
		}
	}
}
