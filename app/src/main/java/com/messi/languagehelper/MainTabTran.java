package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.adapter.RcTranslateListAdapter;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.databinding.MainTabTranBinding;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.event.TranAndDicRefreshEvent;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.viewmodels.TranDictViewModel;
import com.messi.languagehelper.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainTabTran extends BaseFragment {

    private Record currentDialogBean;
    private RcTranslateListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String lastSearch;
    private View view;
    private MainTabTranBinding binding;
    private TranDictViewModel mViewModel;
    private SharedPreferences sp;

    public static MainTabTran getInstance(FragmentProgressbarListener listener) {
        MainTabTran mMainFragment = new MainTabTran();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = Setings.getSharedPreferences(getContext());
        mViewModel = new ViewModelProvider(this).get(TranDictViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (view != null) {
            ViewUtil.removeFromParentView(view);
            return view;
        }
        binding = MainTabTranBinding.inflate(inflater);
        view = binding.getRoot();
        initSwipeRefresh(binding.getRoot());
        init();
        initLiveData();
        return view;
    }

    private void init() {
        isRegisterBus = true;
        mViewModel.initSample();
        loadData();
        mAdapter = new RcTranslateListAdapter(mViewModel.getRepository().beans);
        binding.recentUsedLv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        binding.recentUsedLv.setLayoutManager(mLinearLayoutManager);
        binding.recentUsedLv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(mViewModel.getRepository().beans);
        mAdapter.setFooter(new Object());
        binding.recentUsedLv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener(){
        binding.recentUsedLv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if ((visible + firstVisibleItem) >= total){
                    mViewModel.loadTranData(false);
                }
            }
        });
    }

    private void initLiveData(){
        mViewModel.isRefreshTran().observe(getViewLifecycleOwner(), isNeedRefresh -> {
            mAdapter.notifyDataSetChanged();
            onSwipeRefreshLayoutFinish();
        });
        mViewModel.getRespoData().observe(getViewLifecycleOwner(), mResult -> {
            showResult(mResult);
        });
    }

    public void loadData(){
        mViewModel.loadTranData(true);
    }

    private void translateController(){
        showProgressbar();
        lastSearch = Setings.q;
        mViewModel.tranDict();
    }

    private void showResult(RespoData<Record> mRespoData){
        hideProgressbar();
        if(mRespoData == null || mRespoData.getData() == null || mRespoData.getCode() == 0){
            showToast(mRespoData.getErrStr());
        }else {
            currentDialogBean = mRespoData.getData();
            insertData();
            autoClearAndautoPlay();
        }
    }

    public void insertData() {
        mAdapter.notifyItemInserted(0);;
        binding.recentUsedLv.scrollToPosition(0);
        BoxHelper.insert(currentDialogBean);
    }

    public void autoClearAndautoPlay() {
        EventBus.getDefault().post(new FinishEvent());
        if (sp.getBoolean(KeyUtil.AutoPlayResult, false)) {
            delayAutoPlay();
        }
    }

    public void refresh() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void autoPlay() {
        try {
            View mView = binding.recentUsedLv.getChildAt(0);
            final FrameLayout record_answer_cover = (FrameLayout) mView.findViewById(R.id.record_answer_cover);
            final FrameLayout record_question_cover = (FrameLayout) mView.findViewById(R.id.record_question_cover);
            Record item = mViewModel.getRepository().beans.get(0);
            if (!TextUtils.isEmpty(item.getPh_en_mp3())) {
                if(record_question_cover != null){
                    record_question_cover.post(new Runnable() {
                        @Override
                        public void run() {
                            record_question_cover.performClick();
                        }
                    });
                }
            } else {
                if(record_answer_cover != null){
                    record_answer_cover.post(new Runnable() {
                        @Override
                        public void run() {
                            record_answer_cover.performClick();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(getContext(), toastString);
    }

    public void submit() {
        translateController();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TranAndDicRefreshEvent event){
        LogUtil.DefalutLog("---TranAndDicRefreshEvent---onEvent");
        mViewModel.loadTranData(true);
    }

    private void delayAutoPlay(){
        new Handler().postDelayed(() -> autoPlay(),100);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        mViewModel.loadTranData(true);
    }
}

