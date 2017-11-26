package com.messi.languagehelper;

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

import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcXmlyTagsAdapter;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
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

public class XimalayaFragment extends BaseFragment implements OnClickListener{

	private RecyclerView listview;
	private RcXmlyTagsAdapter mAdapter;
	private List<Album> avObjects;
	private int skip = 1;
	private int max_page = 1;
	private int ad_try_times = 1;
	private int type = new Random().nextInt(3) + 1;
	private String category;
	private String tag_name;
	private IFLYNativeAd nativeAd;
	private boolean loading;
	private boolean hasMore = true;
	private AlbumForAd mADObject;
	private LinearLayoutManager mLinearLayoutManager;

	public static Fragment newInstance(String category, String tag_name){
		XimalayaFragment fragment = new XimalayaFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category",category);
		bundle.putString("tag_name",tag_name);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle mBundle = getArguments();
		this.category = mBundle.getString("category");
		this.tag_name = mBundle.getString("tag_name");
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		loadAD();
		QueryTask();
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
		LogUtil.DefalutLog("type:"+type);
		avObjects = new ArrayList<Album>();
		initSwipeRefresh(view);
		mAdapter = new RcXmlyTagsAdapter();
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
						if(!((AlbumForAd)mAVObject).isAdShow()){
							NativeADDataRef mNativeADDataRef = ((AlbumForAd)mAVObject).getmNativeADDataRef();
							boolean isExposure = mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
							LogUtil.DefalutLog("isExposure:"+isExposure);
							((AlbumForAd)mAVObject).setAdShow(isExposure);
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
		map.put(DTransferConstants.PAGE_SIZE ,String.valueOf(Settings.page_size));
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
						ToastUtil.diaplayMesShort(getContext(), "没有了！");
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
	
	private void loadAD(){
		nativeAd = new IFLYNativeAd(getContext(), ADUtil.XXLAD, new IFLYNativeListener() {
			@Override
			public void onConfirm() {
			}
			@Override
			public void onCancel() {
			}
			@Override
			public void onAdFailed(AdError arg0) {
				if(ad_try_times > 0){
					ad_try_times -= 1;
					loadAD();
				}
				LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> adList) {
				LogUtil.DefalutLog("onADLoaded---");
				if(adList != null && adList.size() > 0){
					NativeADDataRef nad = adList.get(0);
					mADObject = new AlbumForAd();
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
	public void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
