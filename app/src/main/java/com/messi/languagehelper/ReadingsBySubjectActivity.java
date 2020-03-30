package com.messi.languagehelper;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.box.ReadingSubject;
import com.messi.languagehelper.databinding.ReadingBySubjectActivityBinding;
import com.messi.languagehelper.event.SubjectSubscribeEvent;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.viewmodels.ReadingListViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

public class ReadingsBySubjectActivity extends BaseActivity implements View.OnClickListener {

	private RcReadingListAdapter mAdapter;
	private String subjectName;
	private String objectId;
	private ReadingSubject mReadingSubject;
	private String level;
	private LinearLayoutManager mLinearLayoutManager;
	private ReadingBySubjectActivityBinding binding;
	private ReadingListViewModel viewModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ReadingBySubjectActivityBinding.inflate(LayoutInflater.from(this));
		setContentView(binding.getRoot());
		registerBroadcast();
		initViews();
	}

	private void initViews(){
		viewModel = ViewModelProviders.of(this).get(ReadingListViewModel.class);
		subjectName = getIntent().getStringExtra(KeyUtil.SubjectName);
		mReadingSubject = getIntent().getParcelableExtra(KeyUtil.ObjectKey);
		objectId = mReadingSubject.getObjectId();
		level = getIntent().getStringExtra(KeyUtil.LevelKey);

		viewModel.init();
		viewModel.getRepo().setSubjectName(subjectName);
		viewModel.getRepo().setLevel(level);
		viewModel.getRepo().setOrderById(true);

		initSwipeRefresh();
		binding.collectBtn.setOnClickListener(this);
        binding.btnSort.setOnClickListener(this);
        initCollectedButton();
		mAdapter = new RcReadingListAdapter(viewModel.getRepo().list);
		mAdapter.setItems(viewModel.getRepo().list);
		mAdapter.setFooter(new Object());
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
		setListOnScrollListener();
		setData();
		initViewModel();
		viewModel.loadData();
		viewModel.count();
	}

	private void setData(){
		binding.itemImg.setImageResource(ColorUtil.getRadomColor());
		if (mReadingSubject != null) {
			binding.itemSign.setText(mReadingSubject.getSource_name());
			binding.trackInfo.setText(Setings.getCategoryName(mReadingSubject.getCategory()));
			binding.trackTitle.setText(mReadingSubject.getName());
		}
	}

	private void initCollectedButton(){
		ReadingSubject temp = BoxHelper.findReadingSubjectByObjectId(objectId);
	    if(temp != null){
			mReadingSubject = temp;
			binding.volumeImg.setImageResource(R.drawable.ic_collected_white);
			binding.volumeImg.setTag(1);
        }else {
			binding.volumeImg.setImageResource(R.drawable.ic_uncollected_white);
			binding.volumeImg.setTag(0);
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
				if ((visible + firstVisibleItem) >= total){
					viewModel.loadData();
				}
			}
		});
	}

	private void isADInList(RecyclerView view,int first, int vCount){
		if(viewModel.getRepo().list.size() > 3){
			for(int i=first;i< (first+vCount);i++){
				if(i < viewModel.getRepo().list.size() && i > 0){
					Reading mAVObject = viewModel.getRepo().list.get(i);
					if(mAVObject != null && mAVObject.isAd()){
						if(!mAVObject.isAdShow()){
							NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
							boolean isShow = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
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
		viewModel.refresh(0);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.collect_btn){
		    if((int)binding.volumeImg.getTag() == 1){
                collectedOrUncollected(0);
            }else {
                collectedOrUncollected(1);
            }
		}else if(v.getId() == R.id.btn_sort){
			viewModel.getRepo().setOrderById(!viewModel.getRepo().getOrderById());
			viewModel.refresh(0);
			if (viewModel.getRepo().getOrderById()) {
				binding.imgSort.setImageResource(R.drawable.main_album_sort_asc);
			} else {
				binding.imgSort.setImageResource(R.drawable.main_album_sort_desc);
			}
        }
	}

    private void collectedOrUncollected(int tag){
        if(mReadingSubject != null){
			SubjectSubscribeEvent event = new SubjectSubscribeEvent();
			event.setObjectID(mReadingSubject.getObjectId());
            if(tag == 1){
                BoxHelper.saveReadingSubject(mReadingSubject);
				binding.volumeImg.setImageResource(R.drawable.ic_collected_white);
				ToastUtil.diaplayMesShort(this,"已订阅");
				event.setType("subscribe");
            }else {
                BoxHelper.removeReadingSubject(mReadingSubject);
				binding.volumeImg.setImageResource(R.drawable.ic_uncollected_white);
				ToastUtil.diaplayMesShort(this,"取消订阅");
				event.setType("unsubscribe");
            }
			EventBus.getDefault().post(event);
			binding.volumeImg.setTag(tag);
        }
    }

    private void initViewModel(){
		viewModel.getReadingList().observe(this, data -> onDataChange(data));
		viewModel.isShowProgressBar().observe(this, isShow -> isShowProgressBar(isShow));
		viewModel.getAD().observe(this,data -> refreshAD(data));
		viewModel.getCount().observe(this,count -> showMaxCount(count));
	}

	private void refreshAD(RespoADData data){
		LogUtil.DefalutLog("ViewModel---refresh---ad");
		if (data != null) {
			if (data.getCode() == 1) {
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private void onDataChange(RespoData data){
		LogUtil.DefalutLog("ViewModel---onDataChange---");
		if (data != null) {
			if (data.getCode() == 1) {
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			} else {
				ToastUtil.diaplayMesShort(this,data.getErrStr());
			}
			if (data.isHideFooter()) {
				hideFooterview();
			}else {
				showFooterview();
			}
		}else {
			ToastUtil.diaplayMesShort(this,"网络异常，请检查网络连接。");
		}
	}

	private void showMaxCount(Integer count){
		LogUtil.DefalutLog("ViewModel---refresh---ad");
		binding.pageCount.setText(count+"集");
	}

	private void isShowProgressBar(Boolean isShow){
		if (isShow) {
			showProgressbar();
		} else {
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
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
	protected void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}

}
