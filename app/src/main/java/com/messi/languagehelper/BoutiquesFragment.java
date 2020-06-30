package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.adapter.RcBoutiquesAdapter;
import com.messi.languagehelper.bean.AdData;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.databinding.BoutiquesFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.viewmodels.BoutiquesViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class BoutiquesFragment extends BaseFragment {

	private RcBoutiquesAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private String type;
	private String category;
	private String title;
    private BoutiquesViewModel viewModel;
    private BoutiquesFragmentBinding binding;

	public static BoutiquesFragment getInstance(Builder builder) {
		BoutiquesFragment fragment = new BoutiquesFragment();
		Bundle args = new Bundle();
		args.putString(KeyUtil.Category,builder.category);
		args.putString(KeyUtil.Type,builder.type);
		args.putString(KeyUtil.FragmentTitle,builder.title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Context activity) {
		super.onAttach(activity);
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			category = getArguments().getString(KeyUtil.Category);
			type = getArguments().getString(KeyUtil.Type);
			title = getArguments().getString(KeyUtil.FragmentTitle);
			viewModel = new ViewModelProvider(requireActivity()).get(BoutiquesViewModel.class);
			viewModel.init(getContext());
			viewModel.getRepo().setCategory(category);
			viewModel.getRepo().setType(type);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = BoutiquesFragmentBinding.inflate(inflater);
		initSwipeRefresh(binding.getRoot());
		init();
		return binding.getRoot();
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		LogUtil.DefalutLog("------SubjectFragment------");
		viewModel.loadData();
		viewModel.count();
	}
	
	private void init(){
		if(!TextUtils.isEmpty(title)){
			binding.myAwesomeToolbar.setVisibility(View.VISIBLE);
			binding.myAwesomeToolbar.setTitle(title);
		}
		mAdapter = new RcBoutiquesAdapter();
        mAdapter.setFooter(new Object());
		mAdapter.setItems(viewModel.getRepo().getList());
        hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		binding.listview.setLayoutManager(mLinearLayoutManager);
		binding.listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.padding_7)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		binding.listview.setAdapter(mAdapter);
		setListOnScrollListener();
		initLiveData();
	}

	private void initLiveData(){
		viewModel.getReadingList().observe(getViewLifecycleOwner(), data -> onDataChange(data));
		viewModel.isShowProgressBar().observe(getViewLifecycleOwner(), isShow -> isShowProgressBar(isShow));
		viewModel.getAD().observe(getViewLifecycleOwner(),data -> refreshAD(data));
	}

	private void refreshAD(RespoADData data){
		LogUtil.DefalutLog("ViewModel---refresh---ad");
		if (data != null) {
			if (data.getCode() == 1) {
				if(mAdapter != null){
					mAdapter.notifyItemInserted(data.getPos());
				}
			}
		}
	}

	private void onDataChange(RespoData data){
		LogUtil.DefalutLog("ViewModel---onDataChange---");
		if (data != null) {
			if (data.getCode() == 1) {
				if(mAdapter != null){
					mAdapter.notifyItemRangeInserted(data.getPositionStart(),data.getItemCount());
				}
			} else {
				ToastUtil.diaplayMesShort(getActivity(),data.getErrStr());
			}
			if (data.isHideFooter()) {
				hideFooterview();
			}else {
				showFooterview();
			}
		}else {
			ToastUtil.diaplayMesShort(getActivity(),"网络异常，请检查网络连接。");
		}
	}

	private void isShowProgressBar(Boolean isShow){
		if (isShow) {
			showProgressbar();
		} else {
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
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
		if(viewModel.getRepo().getList().size() > 3){
			for(int i=first;i< (first+vCount);i++){
				if(i < viewModel.getRepo().getList().size() && i > 0){
					BoutiquesBean mAVObject = viewModel.getRepo().getList().get(i);
					if(mAVObject != null && mAVObject.isAd() && mAVObject.getAdData() != null){
						AdData mAdData = mAVObject.getAdData();
						if(mAdData.getMNativeADDataRef() != null && !mAdData.isAdShow()){
							NativeDataRef mNativeADDataRef = mAdData.getMNativeADDataRef();
							boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
							mAdData.setAdShow(isExposure);
							LogUtil.DefalutLog("isExposure:"+isExposure);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        viewModel.refresh();
	}

	@Override
	public void showProgressbar() {
		super.showProgressbar();
		if(binding != null && binding.myAwesomeToolbar.isShown()){
			binding.progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideProgressbar() {
		super.hideProgressbar();
		if(binding != null && binding.myAwesomeToolbar.isShown()){
			binding.progressBarCircularIndetermininate.setVisibility(View.GONE);
		}
	}

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }


	public static final class Builder {
		private String type;
		private String category;
		private String title;

		public Builder() {
		}

		public Builder type(String val) {
			type = val;
			return this;
		}

		public Builder category(String val) {
			category = val;
			return this;
		}

		public Builder title(String val) {
			title = val;
			return this;
		}

		public BoutiquesFragment build() {
			return BoutiquesFragment.getInstance(this);
		}
	}
}