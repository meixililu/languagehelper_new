package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLForXMLYAlbumModel;
import com.messi.languagehelper.adapter.RcXmlySearchAlbumAdapter;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XmlySearchAlbumFragment extends BaseFragment {

    private RecyclerView listview;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private View view;
    private RcXmlySearchAlbumAdapter mAdapter;
    private List<Album> avObjects;
    private int skip = 1;
    private int max_page = 1;
    private int type = 1;
    private String search_text;
    private String title;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLForXMLYAlbumModel mXXLModel;

    public static Fragment newInstance(String search_text,String title) {
        XmlySearchAlbumFragment fragment = new XmlySearchAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.SearchKey, search_text);
        bundle.putString(KeyUtil.ActionbarTitle, title);
        fragment.setArguments(bundle);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.search_text = mBundle.getString(KeyUtil.SearchKey);
        this.title = mBundle.getString(KeyUtil.ActionbarTitle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.xmly_search_reasult_fragment, container, false);
        initSwipeRefresh(view);
        initViews();
        return view;
    }

    @Override
    public void loadDataOnStart() {
        QueryTask();
    }

    private void initViews() {
        listview = (RecyclerView)view.findViewById(R.id.listview);
        mToolbar = (Toolbar)view.findViewById(R.id.my_awesome_toolbar);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBarCircularIndetermininate);
        if(!TextUtils.isEmpty(title)){
            mToolbar.setVisibility(View.VISIBLE);
            mToolbar.setTitle(title);
        }
        avObjects = new ArrayList<Album>();
        mXXLModel = new XXLForXMLYAlbumModel(getActivity());
        mAdapter = new RcXmlySearchAlbumAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects,mAdapter);
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
                if (!mXXLModel.loading && mXXLModel.hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        QueryTask();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        if (avObjects.size() > 3) {
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
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        random();
        hideFooterview();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
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
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, search_text);
        map.put(DTransferConstants.CATEGORY_ID, "0");
        map.put(DTransferConstants.CALC_DIMENSION, String.valueOf(type));
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Setings.page_size));
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList albumList) {
                onFinishLoadData();
                if (albumList != null && albumList.getAlbums() != null) {
                    avObjects.addAll(albumList.getAlbums());
                    skip += 1;
                    mAdapter.notifyDataSetChanged();
                    loadAD();
                    if (skip > albumList.getTotalPage()) {
//                        ToastUtil.diaplayMesShort(getContext().getApplicationContext(), "没有了！");
                        hideFooterview();
                        mXXLModel.hasMore = false;
                    } else {
                        mXXLModel.hasMore = true;
                        showFooterview();
                    }
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

    @Override
    public void showProgressbar() {
        super.showProgressbar();
        if(mToolbar != null && mToolbar.isShown()){
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressbar() {
        super.hideProgressbar();
        if(mToolbar != null && mToolbar.isShown()){
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void onFinishLoadData() {
        mXXLModel.loading = false;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
