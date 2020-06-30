package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLAVObjectModel;
import com.messi.languagehelper.adapter.RcSubjectListAdapter;
import com.messi.languagehelper.databinding.SymbolListFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class SubjectFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 1;
    private RcSubjectListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private GridLayoutManager layoutManager;
    private String category;
    private String title;
    private String level;
    private String order;
    private int maxRandom;
    private XXLAVObjectModel mXXLModel;
    private SymbolListFragmentBinding binding;

    public static SubjectFragment getInstance(String category, int maxRandom) {
        SubjectFragment fragment = new SubjectFragment();
        fragment.category = category;
        fragment.maxRandom = maxRandom;
        return fragment;
    }

    public static SubjectFragment getInstance(String category, String level, String order, String title) {
        SubjectFragment fragment = new SubjectFragment();
        fragment.category = category;
        fragment.level = level;
        fragment.order = order;
        fragment.title = title;
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
    public void loadDataOnStart() {
        super.loadDataOnStart();
        random();
        getDataTask();
        getMaxPageNumberBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = SymbolListFragmentBinding.inflate(inflater);
        initSwipeRefresh(binding.getRoot());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        if(!TextUtils.isEmpty(title)){
            binding.myAwesomeToolbar.setVisibility(View.VISIBLE);
            binding.myAwesomeToolbar.setTitle(title);
        }
        avObjects = new ArrayList<>();
        mXXLModel = new XXLAVObjectModel(getActivity());
        mAdapter = new RcSubjectListAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects,mAdapter);
        hideFooterview();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        binding.studycategoryLv.setLayoutManager(layoutManager);
        binding.studycategoryLv.addItemDecoration(new DividerGridItemDecoration(1));
        binding.studycategoryLv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        binding.studycategoryLv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (!mXXLModel.loading && mXXLModel.hasMore && isHasLoadData) {
                    if ((visible + firstVisibleItem) >= total) {
                        getDataTask();
                    }
                }
            }
        });
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        random();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        getDataTask();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        toActivity(SearchActivity.class, null);
    }

    private void random(){
        if(maxRandom > 0){
            skip = (int) Math.round(Math.random()*maxRandom);
        }else {
            skip = 0;
        }
    }

    private void getDataTask(){
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        showProgressbar();
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectList.SubjectList);
        if (!TextUtils.isEmpty(category)) {
            query.whereEqualTo(AVOUtil.SubjectList.category, category);
        }
        if(!TextUtils.isEmpty(order)){
            query.orderByDescending(AVOUtil.SubjectList.order);
        }else {
            query.orderByAscending(AVOUtil.SubjectList.order);
        }
        query.orderByDescending(AVOUtil.SubjectList.views);
        query.orderByAscending(AVOUtil.SubjectList.level);
        query.skip(skip);
        query.limit(Setings.page_size);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> objects, AVException avException) {
                if (mXXLModel != null) {
                    mXXLModel.loading = false;
                }
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if (objects != null) {
                    if (objects.size() == 0) {
                        mXXLModel.hasMore = false;
                        hideFooterview();
                    } else {
                        if (mAdapter != null) {
                            if (skip == 0) {
                                avObjects.clear();
                            }
                            addBgColor(objects);
                            int StratPosition = avObjects.size();
                            avObjects.addAll(objects);
                            mAdapter.notifyItemRangeInserted(StratPosition,objects.size());
                            loadAD();
                            if(objects.size() == Setings.page_size){
                                skip += Setings.page_size;
                                showFooterview();
                                mXXLModel.hasMore = true;
                            }else {
                                mXXLModel.hasMore = false;
                                hideFooterview();
                            }
                        }
                    }
                }
            }
        }));
    }

    private void addBgColor(List<AVObject> avObject){
        for (AVObject item : avObject){
            item.put(KeyUtil.ColorKey, ColorUtil.getRadomColor());
        }
    }

    @Override
    public void showProgressbar() {
        super.showProgressbar();
        if(binding != null && binding.myAwesomeToolbar.isShown()){
            binding.progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        }
    }

    private void getMaxPageNumberBackground(){
        new Thread(() -> {
            try {
                AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectList.SubjectList);
                if (!TextUtils.isEmpty(category)) {
                    query.whereEqualTo(AVOUtil.SubjectList.category, category);
                }
                if(!TextUtils.isEmpty(order)){
                    query.orderByDescending(AVOUtil.SubjectList.order);
                }else {
                    query.orderByAscending(AVOUtil.SubjectList.order);
                }
                maxRandom =  query.count()-100;
                LogUtil.DefalutLog("category:"+category+"---maxRandom:"+maxRandom);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void hideProgressbar() {
        super.hideProgressbar();
        if(binding != null && binding.myAwesomeToolbar.isShown()){
            binding.progressBarCircularIndetermininate.setVisibility(View.GONE);
        }
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
