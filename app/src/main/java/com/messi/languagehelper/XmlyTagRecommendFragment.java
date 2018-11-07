package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.XmlyRecommendPageAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbums;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbumsList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class XmlyTagRecommendFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    private String category;
    private LayoutInflater inflater;
    public static final String RandomNum = "1";
    private XmlyRecommendPageAdapter mAdapter;
    private List<CategoryRecommendAlbums> mTabList;

    public static Fragment newInstance(String category, FragmentProgressbarListener listener) {
        XmlyTagRecommendFragment fragment = new XmlyTagRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        fragment.setArguments(bundle);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.category = mBundle.getString("category");
        LogUtil.DefalutLog("onCreate:" + category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.xmly_tag_recommend_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.inflater = inflater;
        mAdapter = new XmlyRecommendPageAdapter(getChildFragmentManager());
        viewpager.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        QueryTask();
    }

    private void onFinishLoadData() {
        hideProgressbar();
    }

    private void QueryTask() {
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, category);
        map.put(DTransferConstants.DISPLAY_COUNT, RandomNum);
        CommonRequest.getCategoryRecommendAlbums(map,
                new IDataCallBack<CategoryRecommendAlbumsList>() {
                    @Override
                    public void onSuccess(@Nullable CategoryRecommendAlbumsList categoryRecommendAlbumsList) {
                        onFinishLoadData();
                        initTab(categoryRecommendAlbumsList);
                    }

                    @Override
                    public void onError(int i, String s) {
                        onFinishLoadData();
                    }
                });
    }


    private void initTab(CategoryRecommendAlbumsList categoryRecommendAlbumsList) {
        mTabList = categoryRecommendAlbumsList.getCategoryRecommendAlbumses();
        Collections.reverse(mTabList);
        if (mTabList != null && mTabList.size() > 0) {
            mAdapter.refreshByTags(XimalayaUtil.Category_english, mTabList.get(0).getTagName());
        }
        for (CategoryRecommendAlbums dra : mTabList) {
            if (!TextUtils.isEmpty(dra.getTagName())) {
                tablayout.addTab(tablayout.newTab().setText(dra.getTagName()));
            } else {
                tablayout.addTab(tablayout.newTab().setText("精选"));
            }
        }
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabChange(mTabList.get(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabChange(mTabList.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
    }

    private void onTabChange(CategoryRecommendAlbums dra){
        mAdapter.refreshByTags(XimalayaUtil.Category_english,dra.getTagName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setListener(FragmentProgressbarListener listener) {
        mProgressbarListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
