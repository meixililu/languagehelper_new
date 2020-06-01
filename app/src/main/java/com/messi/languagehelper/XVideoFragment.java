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
import com.messi.languagehelper.ViewModel.XXLAVObjectZXModel;
import com.messi.languagehelper.adapter.RcXVideoAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XVideoFragment extends BaseFragment{

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView category_lv;
    private RcXVideoAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<AVObject> mList;
    private int skip = 0;
    private int max_count = 500;
    private boolean isNeedClear = true;
    private String category;
    private String keyword;
    private XXLAVObjectZXModel mXXLModel;

    public static XVideoFragment newInstance(String category,String keyword){
        XVideoFragment fragment = new XVideoFragment();
        fragment.setCategory(category);
        fragment.setKeyword(keyword);
        return fragment;
    }

    public static XVideoFragment newInstance(String keyword){
        XVideoFragment fragment = new XVideoFragment();
        if(keyword.equals("英语")){
            fragment.setCategory("english");
        }else if(keyword.equals("粤语")){
            fragment.setCategory("cantonese");
        }else if(keyword.equals("推荐")){
            fragment.setKeyword("");
            fragment.setCategory("");
        }else {
            fragment.setKeyword(keyword);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.xvideo_fragment, null);
        initViews(view);
        initSwipeRefresh(view);
        randomPage();
        RequestAsyncTask();
        return view;
    }

    private void initViews(View view) {
        mList = new ArrayList<AVObject>();
        mXXLModel = new XXLAVObjectZXModel(getContext(),1);
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcXVideoAdapter(mList, category);
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
                    AVObject mAVObject = mList.get(i);
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
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XVideo.XVideo);
        if(!TextUtils.isEmpty(category)){
            query.whereEqualTo(AVOUtil.XVideo.category,category);
        }
        if(!TextUtils.isEmpty(keyword)){
            final AVQuery<AVObject> priorityQuery = new AVQuery<>(AVOUtil.XVideo.XVideo);
            priorityQuery.whereContains(AVOUtil.XVideo.title, keyword);

            final AVQuery<AVObject> statusQuery = new AVQuery<>(AVOUtil.XVideo.XVideo);
            statusQuery.whereContains(AVOUtil.XVideo.tag, keyword);

            query = AVQuery.or(Arrays.asList(priorityQuery, statusQuery));
        }
        query.orderByDescending(AVOUtil.XVideo.publish_time);
//        query.orderByDescending("createdAt");
        query.skip(skip);
        query.limit(Setings.page_size);
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
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        loadAD();
                        if(list.size() < Setings.page_size){
                            mXXLModel.hasMore = false;
                            hideFooterview();
                        }else {
                            skip += Setings.page_size;
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

    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
