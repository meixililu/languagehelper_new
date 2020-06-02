package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLCNWBeanModel;
import com.messi.languagehelper.adapter.RcCaricatureHomeListAdapter;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class CaricatureSearchResultFragment extends BaseFragment{

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
    private RcCaricatureHomeListAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<CNWBean> mList;
    private int skip = 0;
    public String search_text;
    private XXLCNWBeanModel mXXLModel;

    public static CaricatureSearchResultFragment getInstance(String text){
        CaricatureSearchResultFragment fragment = new CaricatureSearchResultFragment();
        fragment.search_text = text;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_caricature_search_result, null);
        initViews(view);
        initSwipeRefresh(view);
        RequestAsyncTask();
        return view;
    }


    private void initViews(View view) {
        mList = new ArrayList<CNWBean>();
        mXXLModel = new XXLCNWBeanModel(getActivity());
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcCaricatureHomeListAdapter();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        mAdapter.setFooter(new Object());
        mAdapter.setItems(mList);
        mXXLModel.setAdapter(mList,mAdapter);
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
                isADInList(recyclerView, firstVisibleItem, visible);
                if (!mXXLModel.loading && mXXLModel.hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        RequestAsyncTask();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view,int first, int vCount){
        if(mList.size() > 3){
            for(int i=first;i< (first+vCount);i++){
                if(i < mList.size() && i > 0){
                    CNWBean mAVObject = mList.get(i);
                    if(mAVObject != null && mAVObject.getmNativeADDataRef() != null){
                        if(!mAVObject.isAdShow()){
                            NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                            boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
                            mAVObject.setAdShow(isExposure);
                            LogUtil.DefalutLog("isExposure:"+isExposure);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        skip = 0;
        RequestAsyncTask();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private void RequestAsyncTask() {
        showProgressbar();
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        AVQuery<AVObject> priorityQuery = new AVQuery<>(AVOUtil.Caricature.Caricature);
        priorityQuery.whereContains(AVOUtil.Caricature.name, search_text);

        AVQuery<AVObject> statusQuery = new AVQuery<>(AVOUtil.Caricature.Caricature);
        statusQuery.whereContains(AVOUtil.Caricature.tag, search_text);

        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(priorityQuery,statusQuery));
        query.orderByDescending(AVOUtil.Caricature.views);
        query.skip(skip);
        query.limit(Setings.ca_psize);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                hideProgressbar();
                mXXLModel.loading = false;
                onSwipeRefreshLayoutFinish();
                if(list != null){
                    if(list.size() == 0){
                        mXXLModel.hasMore = false;
                        hideFooterview();
                    }else {
                        if(skip == 0){
                            mList.clear();
                        }
                        mList.addAll(DataUtil.toCNWBeanList(list));
                        mAdapter.notifyDataSetChanged();
                        loadAD();
                        if(list.size() < Setings.ca_psize){
                            mXXLModel.hasMore = false;
                            hideFooterview();
                        }else {
                            skip += Setings.ca_psize;
                            mXXLModel.hasMore = true;
                            showFooterview();
                        }
                    }
                }else{
                    ToastUtil.diaplayMesShort(getContext(), "加载失败，下拉可刷新");
                }
            }
        }));
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
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
