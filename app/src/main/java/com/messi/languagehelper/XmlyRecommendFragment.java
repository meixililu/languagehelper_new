package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLForXMLYAlbumModel;
import com.messi.languagehelper.adapter.RcXmlyRecommendAdapter;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XmlyRecommendFragment extends BaseFragment {

    private RecyclerView listview;
    private View view;
    private RcXmlyRecommendAdapter mAdapter;
    private List<Album> avObjects;
    private int skip = 1;
    private int max_page = 1;
    private int type = 1;
    private String category;
    private String tag_name;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLForXMLYAlbumModel mXXLModel;
    private boolean isNeedClear;

    public static XmlyRecommendFragment newInstance() {
        return new XmlyRecommendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.DefalutLog("XmlyRecommendFragment---onCreateView");
        view = inflater.inflate(R.layout.xmly_recommend_fragment, container, false);
        initViews();
        initSwipeRefresh(view);
        return view;
    }

    private void initViews() {
        avObjects = new ArrayList<Album>();
        mXXLModel = new XXLForXMLYAlbumModel(getActivity());
        listview = (RecyclerView)view.findViewById(R.id.listview);
        mAdapter = new RcXmlyRecommendAdapter();
        mXXLModel.setAdapter(avObjects,mAdapter);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                isADInList(recyclerView, firstVisibleItem, visible);
                LogUtil.DefalutLog("visible:"+visible+"-first:"+firstVisibleItem+"-total:"+total);
                if (!mXXLModel.loading && mXXLModel.hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        LogUtil.DefalutLog("QueryTask-setListOnScrollListener");
                        QueryTask();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        try {
            if (avObjects != null && avObjects.size() > 3) {
                for (int i = first; i < (first + vCount); i++) {
                    if (i < avObjects.size() && i > 0) {
                        Album mAVObject = avObjects.get(i);
                        if (mAVObject instanceof AlbumForAd) {
                            if(((AlbumForAd) mAVObject).getmNativeADDataRef() != null){
                                if (!((AlbumForAd) mAVObject).isAdShow()) {
                                    NativeDataRef mNativeADDataRef = ((AlbumForAd) mAVObject).getmNativeADDataRef();
                                    boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i % vCount));
                                    LogUtil.DefalutLog("isExposure:" + isExposure);
                                    if(isExposure){
                                        ((AlbumForAd) mAVObject).setAdShow(isExposure);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideFooterview() {
        if(mAdapter != null){
            mAdapter.hideFooter();
        }
    }

    private void showFooterview() {
        if(mAdapter != null){
            mAdapter.showFooter();
        }
    }

    public void refreshByTags(String category, String tag_name) {
        LogUtil.DefalutLog("refreshByTags");
        this.category = category;
        this.tag_name = tag_name;
        skip = 1;
        hideFooterview();
        isNeedClear = true;
        LogUtil.DefalutLog("QueryTask-refreshByTags");
        QueryTask();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        random();
        hideFooterview();
        isNeedClear = true;
        LogUtil.DefalutLog("QueryTask-onSwipeRefreshLayoutRefresh");
        QueryTask();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private void random() {
        if (max_page > 1) {
            skip = new Random().nextInt(max_page) + 1;
        } else {
            skip = 1;
        }
        LogUtil.DefalutLog("random:" + skip);
    }

    private void QueryTask() {
        LogUtil.DefalutLog("QueryTask");
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, category);
        if (!TextUtils.isEmpty(tag_name) && !tag_name.equals("热门")) {
            map.put(DTransferConstants.TAG_NAME, tag_name);
        }
        map.put(DTransferConstants.CALC_DIMENSION, String.valueOf(type));
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Setings.page_size));
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(@Nullable AlbumList albumList) {
                onFinishLoadData();
                if (albumList != null && albumList.getAlbums() != null && avObjects != null) {
                    loadAD();
                    if(isNeedClear){
                        isNeedClear = false;
                        avObjects.clear();
                    }
                    avObjects.addAll(albumList.getAlbums());
                    skip += 1;
                    if (skip > albumList.getTotalPage()) {
//                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        hideFooterview();
                        if(mXXLModel != null){
                            mXXLModel.hasMore = false;
                        }
                    } else {
                        if(mXXLModel != null){
                            mXXLModel.hasMore = true;
                        }
                        showFooterview();
                    }
                    mAdapter.notifyDataSetChanged();
                    max_page = albumList.getTotalPage();
                }
            }

            @Override
            public void onError(int i, String s) {
                onFinishLoadData();
                LogUtil.DefalutLog(s);
            }
        });
    }

    private void onFinishLoadData() {
        try {
            if(mXXLModel != null){
                mXXLModel.loading = false;
            }
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }

    public void setListener(FragmentProgressbarListener listener){
        mProgressbarListener = listener;
    }

}
