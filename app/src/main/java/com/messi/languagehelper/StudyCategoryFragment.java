package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.adapter.XmlyRecommendPageAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbums;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbumsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyCategoryFragment extends BaseFragment {

    @BindView(R.id.symbol_study_cover)
    FrameLayout symbolStudyCover;
    @BindView(R.id.study_listening_layout)
    FrameLayout studyListeningLayout;
    @BindView(R.id.en_examination_layout)
    FrameLayout enExaminationLayout;
    @BindView(R.id.study_composition)
    FrameLayout studyComposition;
    @BindView(R.id.study_word_layout)
    FrameLayout studyWordLayout;

    @BindView(R.id.collected_layout)
    FrameLayout collectedLayout;
    @BindView(R.id.study_spoken_english)
    FrameLayout studySpokenEnglish;
    @BindView(R.id.en_grammar)
    FrameLayout enGrammar;
    @BindView(R.id.en_story_layout)
    FrameLayout enStoryLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.xmly_layout)
    FrameLayout xmlyLayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    private List<CategoryRecommendAlbums> mTabList;

    private XmlyRecommendPageAdapter mAdapter;

    public static StudyCategoryFragment getInstance() {
        return new StudyCategoryFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            registerBroadcast();
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.study_category_fragment, null);
        ButterKnife.bind(this, view);
        mAdapter = new XmlyRecommendPageAdapter(getChildFragmentManager());
        viewpager.setAdapter(mAdapter);
        QueryTask();
        return view;
    }

    @Override
    public void updateUI(String music_action) {
        if (music_action.equals(PlayerService.action_loading)) {
            showProgressbar();
        } else if (music_action.equals(PlayerService.action_finish_loading)) {
            hideProgressbar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.symbol_study_cover, R.id.study_listening_layout,
            R.id.en_examination_layout, R.id.study_composition, R.id.collected_layout,
            R.id.study_spoken_english, R.id.en_grammar, R.id.en_story_layout,
            R.id.xmly_layout, R.id.study_word_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.symbol_study_cover:
                toActivity(SymbolActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
                break;
            case R.id.study_listening_layout:
                toActivity(ListenActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_listening");
                break;
            case R.id.en_examination_layout:
                toExaminationActivity(getContext().getResources().getString(R.string.examination));
                AVAnalytics.onEvent(getContext(), "tab3_to_examination");
                break;
            case R.id.study_composition:
                toActivity(CompositionActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_composition");
                break;
            case R.id.collected_layout:
                toActivity(CollectedActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_collected");
                break;
            case R.id.study_spoken_english:
                toActivity(SpokenActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_spoken_english");
                break;
            case R.id.en_grammar:
                toActivity(GrammarActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_grammar");
                break;
            case R.id.en_story_layout:
                toActivity(StoryActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_story");
                break;
            case R.id.xmly_layout:
                toActivity(XmlyActivity.class,null);
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_home");
                break;
            case R.id.study_word_layout:
                toActivity(WordsActivity.class,null);
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_home");
                break;
        }
    }

    private void toExaminationActivity(String title) {
        Intent intent = new Intent(getContext(), ExaminationActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getContext().startActivity(intent);
    }

    private void QueryTask() {
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, XimalayaUtil.Category_Eng);
        map.put(DTransferConstants.DISPLAY_COUNT, "1");
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
        if(mTabList != null && mTabList.size() > 0){
            mAdapter.refreshByTags(XimalayaUtil.Category_Eng,mTabList.get(0).getTagName());
        }
        for (CategoryRecommendAlbums dra : mTabList) {
            if(!TextUtils.isEmpty(dra.getTagName())){
                tablayout.addTab(tablayout.newTab().setText(dra.getTagName()));
            }else {
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
        mAdapter.refreshByTags(XimalayaUtil.Category_Eng,dra.getTagName());
    }

    private void onFinishLoadData() {
        hideProgressbar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcast();
    }

}
