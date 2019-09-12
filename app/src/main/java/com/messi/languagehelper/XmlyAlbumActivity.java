package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.adapter.RcXmlyTagsAdapter;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TXADUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XmlyAlbumActivity extends BaseActivity implements OnClickListener{

	private RecyclerView listview;
	private RcXmlyTagsAdapter mAdapter;
	private List<Album> avObjects;
	private int skip = 1;
	private int max_page = 1;
	private int type = 1;
	private String category;
	private String tag_name;
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private AlbumForAd mADObject;
	private LinearLayoutManager mLinearLayoutManager;
	private List<NativeExpressADView> mTXADList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmly_album);
		initSwipeRefresh();
		initData();
		initViews();
		loadAD();
		QueryTask();
	}

	private void initData(){
		this.category = getIntent().getStringExtra(KeyUtil.Category);
		this.tag_name = getIntent().getStringExtra(KeyUtil.Xmly_Tag);
		LogUtil.DefalutLog("category:"+category);
		LogUtil.DefalutLog("tag_name:"+tag_name);
	}
	
	private void initViews(){
		listview = (RecyclerView) findViewById(R.id.listview);
		avObjects = new ArrayList<Album>();
		mTXADList = new ArrayList<NativeExpressADView>();
		mAdapter = new RcXmlyTagsAdapter();
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
						QueryTask();
					}
				}
			}
		});
	}
	
	private void isADInList(RecyclerView view,int first, int vCount){
		if(avObjects.size() > 3){
			for(int i=first;i< (first+vCount);i++){
				if(i < avObjects.size() && i > 0){
					Album mAVObject = avObjects.get(i);
					if (mAVObject instanceof AlbumForAd) {
						if(((AlbumForAd) mAVObject).getmNativeADDataRef() != null){
							if(!((AlbumForAd)mAVObject).isAdShow()){
								NativeDataRef mNativeADDataRef = ((AlbumForAd)mAVObject).getmNativeADDataRef();
								boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
								LogUtil.DefalutLog("isExposure:"+isExposure);
								if(isExposure){
									((AlbumForAd)mAVObject).setAdShow(isExposure);
								}
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
		random();
		hideFooterview();
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		QueryTask();
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

	private void random(){
		if(max_page > 1){
		    skip = new Random().nextInt(max_page) + 1;
		}else {
			skip = 1;
		}
		LogUtil.DefalutLog("random:"+skip);
	}

	private void QueryTask(){
		loading = true;
		showProgressbar();
		Map<String ,String> map = new HashMap<String, String>();
		map.put(DTransferConstants.CATEGORY_ID ,category);
		if(!TextUtils.isEmpty(tag_name)){
			map.put(DTransferConstants.TAG_NAME ,tag_name);
		}
		map.put(DTransferConstants.CALC_DIMENSION ,String.valueOf(type));
		map.put(DTransferConstants.PAGE_SIZE ,String.valueOf(Setings.page_size));
		map.put(DTransferConstants.PAGE ,String.valueOf(skip));
		CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>(){
			@Override
			public void onSuccess(@Nullable AlbumList albumList) {
				onFinishLoadData();
				if(albumList != null && albumList.getAlbums() != null){
					LogUtil.DefalutLog(albumList.toString());
					avObjects.addAll( albumList.getAlbums() );
					skip += 1;
					if(addAD()){
						mAdapter.notifyDataSetChanged();
					}
					if(skip > albumList.getTotalPage()){
						ToastUtil.diaplayMesShort(XmlyAlbumActivity.this, "没有了！");
						hideFooterview();
						hasMore = false;
					}else {
						hasMore = true;
						showFooterview();
					}
					max_page = albumList.getTotalPage();
				}
			}
			@Override
			public void onError(int i, String s) {
				onFinishLoadData();
				LogUtil.DefalutLog(s);
			}
		});
	}

	private void onFinishLoadData(){
		loading = false;
		hideProgressbar();
		onSwipeRefreshLayoutFinish();
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
			public void onAdLoaded(NativeDataRef nativeDataRef) {
				if(nativeDataRef != null){
					addXFAD(nativeDataRef);
				}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd();
	}

	private void addXFAD(NativeDataRef nad){
		mADObject = new AlbumForAd();
		mADObject.setmNativeADDataRef(nad);
		mADObject.setAd(true);
		if (!loading) {
			addAD();
		}
	}

	private void onADFaile(){
		if(ADUtil.isHasLocalAd()){
			NativeDataRef nad = ADUtil.getRandomAd(this);
			addXFAD(nad);
		}
	}

	private void loadTXAD(){
		TXADUtil.showXXL_ZWYT(this, new NativeExpressAD.NativeExpressADListener() {
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
					mADObject = new AlbumForAd();
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
	public void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_search:
				toMoreActivity();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toMoreActivity() {
		toActivity(XmlySearchActivity.class, null);
		AVAnalytics.onEvent(this, "index_pg_to_morepg");
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mTXADList != null){
			for(NativeExpressADView adView : mTXADList){
				adView.destroy();
			}
		}
	}
}
