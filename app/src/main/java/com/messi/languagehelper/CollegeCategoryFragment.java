package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcCollegeCategoryAdapter;
import com.messi.languagehelper.databinding.CollegeCategoryFragmentBinding;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;


public class CollegeCategoryFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 3;
    private List<AVObject> avObjects;;
    private RcCollegeCategoryAdapter mAdapter;
    private SharedPreferences sharedPreferences;
    private CollegeCategoryFragmentBinding binding;

    public static CollegeCategoryFragment getInstance() {
        return new CollegeCategoryFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = CollegeCategoryFragmentBinding.inflate(inflater);
        initSwipeRefresh(binding.getRoot());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        binding.myAwesomeToolbar.setTitle(getString(R.string.title_college_class));
        avObjects = new ArrayList<AVObject>();
        sharedPreferences = Setings.getSharedPreferences(getContext());
        binding.listview.setHasFixedSize(true);
        mAdapter = new RcCollegeCategoryAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        binding.listview.setLayoutManager(layoutManager);
        binding.listview.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setItems(avObjects);
        binding.listview.setAdapter(mAdapter);
    }

    @Override
    public void loadDataOnStart() {
        QueryTask();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        QueryTask();
    }


    private void QueryTask() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.BoutiquesClass.BoutiquesClass);
        query.orderByAscending(AVOUtil.BoutiquesClass.order);
        query.orderByDescending(AVOUtil.BoutiquesClass.views);
        query.limit(30);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                onSwipeRefreshLayoutFinish();
                setData(avObjects);
            }
        }));
    }

    private void setData(List<AVObject> items){
        if(NullUtil.isNotEmpty(items)) {
            avObjects.clear();
            avObjects.addAll(items);
            mAdapter.notifyDataSetChanged();
        }
    }




}
