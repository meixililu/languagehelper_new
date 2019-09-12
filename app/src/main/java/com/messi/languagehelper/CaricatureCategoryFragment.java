package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLCNWBeanModel;
import com.messi.languagehelper.adapter.RcCaricatureCategoryAdapter;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.impl.AdapterStringListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaricatureCategoryFragment extends BaseFragment implements AdapterStringListener {

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
    private RcCaricatureCategoryAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<CNWBean> mList;
    private List<AVObject> calist;
    private int skip = 0;
    private int max_count = 300;
    private boolean isNeedClear = true;
    private String search_text = "全部";
    private XXLCNWBeanModel mXXLModel;

    public static CaricatureCategoryFragment newInstance(){
        CaricatureCategoryFragment fragment = new CaricatureCategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.caricature_category_fragment, null);
        initViews(view);
        initSwipeRefresh(view);
        randomPage();
        Request();
        return view;
    }

    private void Request(){
        RequestCategoryTask();
        RequestAsyncTask();
    }

    private void initViews(View view) {
        mList = new ArrayList<CNWBean>();
        calist = new ArrayList<AVObject>();
        mXXLModel = new XXLCNWBeanModel(getActivity());
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcCaricatureCategoryAdapter(calist,this);
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        mAdapter.setFooter(new Object());
        mAdapter.setHeader(new Object());
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
        randomPage();
        mXXLModel.hasMore = true;
        Request();
    }

    private void randomPage(){
        isNeedClear = true;
        if(max_count > 0){
            skip = NumberUtil.getRandomNumber(max_count);
            LogUtil.DefalutLog("spip:"+skip);
        }else {
            skip = 0;
        }
    }

    private void RequestCategoryTask() {
        showProgressbar();
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.CaricatureSearchHot.CaricatureSearchHot);
        query.whereGreaterThan(AVOUtil.CaricatureSearchHot.click_time,98000080);
        query.orderByDescending(AVOUtil.CaricatureSearchHot.click_time);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if(list != null && list.size() > 0){
                    for(AVObject item : list){
                        item.put("selected","0");
                    }
                    calist.clear();
                    calist.addAll(list);
                    addTag();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addTag(){
        AVObject object = new AVObject();
        object.put(AVOUtil.CaricatureSearchHot.name,"全部");
        object.put("selected","1");
        calist.add(0,object);
    }

    private void RequestAsyncTask() {
        showProgressbar();
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        AVQuery<AVObject> query = null;
        if(!TextUtils.isEmpty(search_text) && !"全部".equals(search_text)){
            AVQuery<AVObject> priorityQuery = new AVQuery<>(AVOUtil.Caricature.Caricature);
            priorityQuery.whereContains(AVOUtil.Caricature.name, search_text);
            AVQuery<AVObject> statusQuery = new AVQuery<>(AVOUtil.Caricature.Caricature);
            statusQuery.whereContains(AVOUtil.Caricature.tag, search_text);
            query = AVQuery.or(Arrays.asList(priorityQuery,statusQuery));
        }else {
            query = new AVQuery<AVObject>(AVOUtil.Caricature.Caricature);
        }
        query.orderByDescending(AVOUtil.Caricature.views);
        query.skip(skip);
        query.limit(Setings.ca_psize);
        query.findInBackground(new FindCallback<AVObject>() {
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
                        if(isNeedClear){
                            isNeedClear = false;
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
        });
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

    @Override
    public void OnItemClick(String item) {
        search_text = item;
        skip = 0;
        mList.clear();
        mAdapter.notifyDataSetChanged();
        RequestAsyncTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
