package com.messi.languagehelper;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLForXMLYAlbumModel;
import com.messi.languagehelper.adapter.RcXmlyTagsAdapter;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XimalayaTagsFragment extends BaseFragment implements OnClickListener {

    private RecyclerView listview;
    TabLayout tablayout;
    private View view;
    private RcXmlyTagsAdapter mAdapter;
    private List<Album> avObjects;
    private int skip = 1;
    private int max_page = 1;
    private int type = 1;
    private String category;
    private String tag_name;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLForXMLYAlbumModel mXXLModel;

    public static Fragment newInstance(String category, String tag_name, FragmentProgressbarListener listener) {
        XimalayaTagsFragment fragment = new XimalayaTagsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("tag_name", tag_name);
        fragment.setArguments(bundle);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.category = mBundle.getString("category");
        this.tag_name = mBundle.getString("tag_name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.DefalutLog("XimalayaTagsFragment---onCreateView");
        View view = inflater.inflate(R.layout.xmly_tags_fragment, container, false);
        initViews(view);
        initSwipeRefresh(view);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        getTagsData();
        QueryTask();
    }


    private void initViews(View view) {
        mXXLModel = new XXLForXMLYAlbumModel(getActivity());
        avObjects = new ArrayList<Album>();
        LogUtil.DefalutLog("type:" + type);
        listview = (RecyclerView)view.findViewById(R.id.listview);
        tablayout = (TabLayout)view.findViewById(R.id.tablayout);
        mAdapter = new RcXmlyTagsAdapter();
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
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
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

    private void getTagsData() {
        showProgressbar();
        RequestTagsData();
    }

    private void QueryTask() {
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
                if (albumList != null && albumList.getAlbums() != null) {
                    avObjects.addAll(albumList.getAlbums());
                    skip += 1;
                    mAdapter.notifyDataSetChanged();
                    loadAD();
                    if (skip > albumList.getTotalPage()) {
//                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
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

    private void onFinishLoadData() {
        mXXLModel.loading = false;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    private void RequestTagsData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, category);
        map.put(DTransferConstants.TYPE, "0");
        showProgressbar();
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(@Nullable TagList tagList) {
                if (tagList != null) {
                    List<Tag> tags = tagList.getTagList();
                    Tag tag = new Tag();
                    tag.setTagName("热门");
                    tag.setKind("1");
                    tags.add(0, tag);
                    initTab(tags);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("onError:" + i + "---mes:" + s);
            }
        });
    }

    private void initTab(List<Tag> mTabList) {
        for (Tag dra : mTabList) {
            if(!TextUtils.isEmpty(dra.getTagName())){
                tablayout.addTab(tablayout.newTab().setText(dra.getTagName()));
            }else {
                tablayout.addTab(tablayout.newTab().setText("精选"));
            }
        }
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                OnItemClick(mTabList.get(tab.getPosition()).getTagName());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                OnItemClick(mTabList.get(tab.getPosition()).getTagName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }


    public void OnItemClick(String item) {
        type = 1;
        skip = 1;
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        tag_name = item;
        QueryTask();
    }

    public void setListener(FragmentProgressbarListener listener){
        mProgressbarListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
