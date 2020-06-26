package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.adapter.RcCollectedListAdapter;
import com.messi.languagehelper.databinding.CompositionFragmentBinding;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.viewmodels.CollectedListViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class ReadingCollectedListFragment extends BaseFragment {

	private RcCollectedListAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private CompositionFragmentBinding binding;
	private CollectedListViewModel viewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerBroadcast();
		viewModel = new ViewModelProvider(requireActivity()).get(CollectedListViewModel.class);
		viewModel.init();
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		viewModel.loadData(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = CompositionFragmentBinding.inflate(inflater);
		initViews();
		initSwipeRefresh(binding.getRoot());
		initLiveData();
		return binding.getRoot();
	}
	
	private void initViews(){
		mAdapter = new RcCollectedListAdapter();
		mAdapter.setItems(viewModel.mRepo.getList());
		mAdapter.setFooter(new Object());
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		binding.listview.setLayoutManager(mLinearLayoutManager);
		binding.listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		binding.listview.setAdapter(mAdapter);
		setListOnScrollListener();
	}

	private void initLiveData(){
		viewModel.getDataList().observe(getViewLifecycleOwner(), mRespoData -> {
			onSwipeRefreshLayoutFinish();
			if (mRespoData.getCode() == 1) {
				mAdapter.notifyItemRangeInserted(mRespoData.getPositionStart(),mRespoData.getItemCount());
			}
			if (mRespoData.isHideFooter()) {
				hideFooterview();
			} else {
				showFooterview();
			}
		});
		LiveEventBus.get(KeyUtil.UpdateCollectedData).observe(getViewLifecycleOwner(), result -> {
			viewModel.loadData(true);
		});
	}
	
	public void setListOnScrollListener(){
		binding.listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int visible  = mLinearLayoutManager.getChildCount();
				int total = mLinearLayoutManager.getItemCount();
				int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
				if ((visible + firstVisibleItem) >= total){
					viewModel.loadData(false);
				}
			}
		});
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		viewModel.loadData(true);
	}

	private void hideFooterview(){
		mAdapter.hideFooter();
	}

	private void showFooterview(){
		mAdapter.showFooter();
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

//	@Override
//	public void onResume() {
//		super.onResume();
//		if () {
//
//		}
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}
}
