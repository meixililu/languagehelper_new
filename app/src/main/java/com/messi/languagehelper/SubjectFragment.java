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
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLAVObjectModel;
import com.messi.languagehelper.adapter.RcSubjectListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class SubjectFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 1;

    @BindView(R.id.studycategory_lv)
    RecyclerView category_lv;
    @BindView(R.id.my_awesome_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progressBarCircularIndetermininate)
    ProgressBar progressBar;
    Unbinder unbinder;
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
        LogUtil.DefalutLog("------SubjectFragment------");
        random();
        getDataTask();
        getMaxPageNumberBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.symbol_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initSwipeRefresh(view);
        initViews();
        return view;
    }

    private void initViews() {
        if(!TextUtils.isEmpty(title)){
            mToolbar.setVisibility(View.VISIBLE);
            mToolbar.setTitle(title);
        }
        avObjects = new ArrayList<AVObject>();
        mXXLModel = new XXLAVObjectModel(getActivity());
        mAdapter = new RcSubjectListAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects,mAdapter);
        hideFooterview();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        AVAnalytics.onEvent(getContext(), "subject_to_search");
    }

    private void random(){
        if(maxRandom > 0){
            skip = (int) Math.round(Math.random()*maxRandom);
        }else {
            skip = 0;
        }
        LogUtil.DefalutLog("skip:"+skip);
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
        if(mToolbar != null && mToolbar.isShown()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void getMaxPageNumberBackground(){
        new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        }).start();
    }

    @Override
    public void hideProgressbar() {
        super.hideProgressbar();
        if(mToolbar != null && mToolbar.isShown()){
            progressBar.setVisibility(View.GONE);
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
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
