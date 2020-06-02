package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLCNWBeanModel;
import com.messi.languagehelper.adapter.RcCaricatureHomeListAdapter;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class CaricatureHomeFragment extends BaseFragment implements View.OnClickListener{

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
    private FrameLayout search_btn;
    private Toolbar my_awesome_toolbar;
    private RcCaricatureHomeListAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<CNWBean> mList;
    private int skip = 0;
    private int max_count = 8000;
    private boolean isNeedClear = true;
    private XXLCNWBeanModel mXXLModel;

    public static CaricatureHomeFragment newInstance(){
        CaricatureHomeFragment fragment = new CaricatureHomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.caricature_home_fragment, null);
        initViews(view);
        initSwipeRefresh(view);
        randomPage();
        RequestAsyncTask();
        return view;
    }

    private void initViews(View view) {
        mList = new ArrayList<CNWBean>();
        mXXLModel = new XXLCNWBeanModel(getActivity());
        my_awesome_toolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
        search_btn = (FrameLayout) view.findViewById(R.id.search_btn);
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        my_awesome_toolbar.setTitle(R.string.recommend);
        search_btn.setOnClickListener(this);
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
        randomPage();
        RequestAsyncTask();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
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

    private void RequestAsyncTask() {
        showProgressbar();
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Caricature.Caricature);
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
        }));
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.search_btn){
            toSearchActivity();
        }
    }

    private void toSearchActivity(){
        if(Setings.IsShowNovel){
            toKSearch();
        }else {
            toActivity(CaricatureSearchActivity.class,null);
        }
    }

    private void toKSearch(){
        Intent intent = new Intent(getContext(),CNSearchActivity.class);
        intent.putExtra(KeyUtil.PositionKey,0);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
