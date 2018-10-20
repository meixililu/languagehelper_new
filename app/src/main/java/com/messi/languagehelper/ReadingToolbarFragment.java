package com.messi.languagehelper;

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
import android.widget.TextView;

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
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TXADUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private Reading mADObject;
	private LinearLayoutManager mLinearLayoutManager;
	private List<NativeExpressADView> mTXADList;
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
		View view = inflater.inflate(R.layout.listen_home_fragment, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view){
		listview = (RecyclerView) view.findViewById(R.id.listview);
		toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
		toolbar_title.setText(title);
		avObjects = new ArrayList<Reading>();
		mTXADList = new ArrayList<NativeExpressADView>();
		avObjects.addAll(DataBaseUtil.getInstance().getReadingList(Setings.page_size,category,"",code));
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
		loadAD();
		hideFooterview();
		random();
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask().execute();
	}

	private void loadAD(){
		if(ADUtil.IsShowAD){
			if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
				loadXFAD();
			}else {
				loadTXAD();
			}
		}
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
						skip += Setings.page_size;
						showFooterview();
					}
				}
			}
			if(skip == maxRandom){
				hasMore = false;
			}else {
				hasMore = true;
			}
		}
	}
	
	private void loadXFAD(){
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
				if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
					loadTXAD();
				}else {
					onADFaile();
				}
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> adList) {
				LogUtil.DefalutLog("onADLoaded---");
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
		TXADUtil.showXXL(getActivity(), new NativeExpressAD.NativeExpressADListener() {
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
			int index = avObjects.size() - Setings.page_size + NumberUtil.randomNumberRange(1, 2);
			if(index < 1){
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
		if(mTXADList != null){
			for(NativeExpressADView adView : mTXADList){
				adView.destroy();
			}
		}
	}
}
