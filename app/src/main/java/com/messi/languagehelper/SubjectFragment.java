package com.messi.languagehelper;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.adapter.RcSubjectListAdapter;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.databinding.SymbolListFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.viewmodels.SubjectViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class SubjectFragment extends BaseFragment {

    private RcSubjectListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String category;
    private String title;
    private String level;
    private String order;
    private SymbolListFragmentBinding binding;
    private SubjectViewModel viewModel;

    public static SubjectFragment getInstance(String category, String level) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category",category);
        bundle.putString("level",level);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SubjectFragment getInstance(String category, String level, String order, String title) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category",category);
        bundle.putString("level",level);
        bundle.putString("order",order);
        bundle.putString(KeyUtil.ActionbarTitle,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.title = mBundle.getString(KeyUtil.ActionbarTitle);
        this.category = mBundle.getString("category");
        this.level = mBundle.getString("level");
        this.order = mBundle.getString("order");
        viewModel = ViewModelProviders.of(getActivity()).get(SubjectViewModel.class);
        viewModel.init();
        viewModel.getRepo().setCategory(category);
        viewModel.getRepo().setLevel(level);
        viewModel.getRepo().setOrder(order);
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
    public void loadDataOnStart() {
        super.loadDataOnStart();
        viewModel.loadData();
        viewModel.count();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = SymbolListFragmentBinding.inflate(inflater);
        initSwipeRefresh(binding.getRoot());
        initViews();
        initAdapter();
        initViewModel();
        return binding.getRoot();
    }

    private void initViews() {
        if(!TextUtils.isEmpty(title)){
            binding.myAwesomeToolbar.setVisibility(View.VISIBLE);
            binding.myAwesomeToolbar.setTitle(title);
        }
    }

    private void initAdapter(){
        if(mAdapter == null && binding.studycategoryLv != null){
            mAdapter = new RcSubjectListAdapter();
            mAdapter.setItems(viewModel.getRepo().getList());
            mAdapter.setFooter(new Object());
            hideFooterview();
            mLinearLayoutManager = new LinearLayoutManager(getContext());
            binding.studycategoryLv.setLayoutManager(mLinearLayoutManager);
            binding.studycategoryLv.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(getContext())
                            .colorResId(R.color.text_tint)
                            .sizeResId(R.dimen.list_divider_size)
                            .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                            .build());
            binding.studycategoryLv.setAdapter(mAdapter);
            setListOnScrollListener();
        }
    }

    public void setListOnScrollListener(){
        binding.studycategoryLv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    AVObject mAVObject = viewModel.getRepo().getList().get(i);
                    if(mAVObject != null && mAVObject.get(KeyUtil.ADKey) != null){
                        if(!(Boolean) mAVObject.get(KeyUtil.ADIsShowKey)){
                            NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.get(KeyUtil.ADKey);
                            boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
                            LogUtil.DefalutLog("isExposure:"+isExposure);
                            if(isExposure){
                                mAVObject.put(KeyUtil.ADIsShowKey, isExposure);
                            }
                        }
                    }
                }
            }
        }
    }

    private void initViewModel(){
        viewModel.getReadingList().observe(this, data -> onDataChange(data));
        viewModel.isShowProgressBar().observe(this, isShow -> isShowProgressBar(isShow));
        viewModel.getAD().observe(this,data -> refreshAD(data));
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

    private void hideFooterview() {
        if (mAdapter != null) {
            mAdapter.hideFooter();
        }
    }

    private void showFooterview() {
        if (mAdapter != null) {
            mAdapter.showFooter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
