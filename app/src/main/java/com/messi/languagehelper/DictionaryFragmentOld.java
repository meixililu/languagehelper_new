package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.adapter.RcDictionaryListAdapter;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.event.ProgressEvent;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.viewmodels.TranDictViewModel;
import com.messi.languagehelper.views.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictionaryFragmentOld extends BaseFragment implements DictionaryTranslateListener {

    private Dictionary mDictionaryBean;

    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;
    @BindView(R.id.cidian_result_layout)
    NestedScrollView cidianResultLayout;
    @BindView(R.id.dic_result_layout)
    LinearLayout dicResultLayout;

    private RcDictionaryListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private TranDictViewModel mViewModel;
    private View view;

    public static DictionaryFragmentOld getInstance(FragmentProgressbarListener listener) {
        DictionaryFragmentOld mMainFragment = new DictionaryFragmentOld();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(TranDictViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (view != null) {
            ViewUtil.removeFromParentView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_dictionary_old, null);
        ButterKnife.bind(this, view);
        init();
        initLiveData();
        return view;
    }

    private void init() {
        liveEventBus();
        loadData();
        mAdapter = new RcDictionaryListAdapter(getContext(), mViewModel.getRepository().dicts, this);
        recent_used_lv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recent_used_lv.setLayoutManager(mLinearLayoutManager);
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(mViewModel.getRepository().dicts);
        mAdapter.setFooter(new Object());
        recent_used_lv.setAdapter(mAdapter);
        isShowRecentList(true);
        setListOnScrollListener();
    }

    public void setListOnScrollListener(){
        recent_used_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visible  = mLinearLayoutManager.getChildCount();
            int total = mLinearLayoutManager.getItemCount();
            int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if ((visible + firstVisibleItem) >= total){
                LogUtil.DefalutLog("should load more data");
                mViewModel.loadDictData(false);
            }
            }
        });
    }

    private void initLiveData(){
        mViewModel.isRefreshDict().observe(getViewLifecycleOwner(), isNeedRefresh -> {
            refresh();
        });
        mViewModel.getDictRespoData().observe(getViewLifecycleOwner(), mResult -> {
            showResult(mResult);
        });
    }

    public void loadData(){
        mViewModel.loadDictData(true);
    }

    private void isShowRecentList(boolean value) {
        if (value) {
            recent_used_lv.setVisibility(View.VISIBLE);
            cidianResultLayout.setVisibility(View.GONE);
        } else {
            cidianResultLayout.setVisibility(View.VISIBLE);
            recent_used_lv.setVisibility(View.GONE);
        }
    }

    private void translateController(){
        showProgressbar();
        mViewModel.getDict();
    }

    private void showResult(RespoData<Dictionary> mRespoData){
        hideProgressbar();
        if(mRespoData == null || mRespoData.getData() == null || mRespoData.getCode() == 0){
            showToast(mRespoData.getErrStr());
        }else {
            mDictionaryBean = mRespoData.getData();
            setBean();
            LiveEventBus.get(KeyUtil.onTranDictFinish).post("");
        }
    }

    private void setBean() {
        isShowRecentList(false);
        cidianResultLayout.scrollTo(0, 0);
        DictionaryHelper.addDicContent(getContext(), dicResultLayout, mDictionaryBean);
    }

    public void submit() {
        translateController();
    }

    @Override
    public void showItem(Dictionary word) {
        mDictionaryBean = word;
        setBean();
    }

    public void refresh() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void reloadData() {
        mViewModel.loadDictData(true);
    }

    public void liveEventBus(){
        LiveEventBus.get(KeyUtil.DictProgressEvent, ProgressEvent.class)
                .observe(getViewLifecycleOwner(), event -> {
                    LogUtil.DefalutLog("ProgressEvent");
                    if(event.getStatus() == 0){
                        showProgressbar();
                    }else {
                        hideProgressbar();
                    }
        });
        LiveEventBus.get(KeyUtil.TranAndDicRefreshEvent).observe(getViewLifecycleOwner(), result -> {
            reloadData();
        });
    }

    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(getContext(), toastString);
    }
}
