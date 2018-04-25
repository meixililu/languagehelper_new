package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbums;
import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbumsList;

import java.util.HashMap;
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
    @BindView(R.id.content_tv)
    LinearLayout contentTv;
    @BindView(R.id.xmly_layout)
    FrameLayout xmlyLayout;

    private LayoutInflater inflater;
    private int skip = 2;
    public static final String RandomNum = "6";

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
        initSwipeRefresh(view);
        this.inflater = inflater;
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
    public void loadDataOnStart() {
        super.loadDataOnStart();
        QueryTask();
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
        map.put(DTransferConstants.DISPLAY_COUNT, RandomNum);
        CommonRequest.getCategoryRecommendAlbums(map,
                new IDataCallBack<CategoryRecommendAlbumsList>() {
                    @Override
                    public void onSuccess(@Nullable CategoryRecommendAlbumsList categoryRecommendAlbumsList) {
                        onFinishLoadData();
                        contentTv.removeAllViews();
                        for (CategoryRecommendAlbums dra : categoryRecommendAlbumsList.getCategoryRecommendAlbumses()) {
                            setList(dra);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        onFinishLoadData();
                    }
                });
    }

    private void setList(final CategoryRecommendAlbums dra) {
        contentTv.addView(initView(dra));
    }

    private View initView(final CategoryRecommendAlbums dra) {
        View view = inflater.inflate(R.layout.xmly_recommend_root, null);
        FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParams.bottomMargin = ScreenUtil.dip2px(getContext(), 12);
        view.setLayoutParams(mParams);
        FrameLayout header = (FrameLayout) view.findViewById(R.id.xmly_recommend_header);
        TextView header_title = (TextView) view.findViewById(R.id.xmly_recommend_header_title);
        final LinearLayout content_tv = (LinearLayout) view.findViewById(R.id.content_tv);
        FrameLayout footer = (FrameLayout) view.findViewById(R.id.xmly_recommend_footer);
        header_title.setText(dra.getDisPlayTagName());
        content_tv.removeAllViews();
        for (Album album : dra.getAlbumList()) {
            content_tv.addView(initAlbum(album));
            content_tv.addView(ViewUtil.getLine(getContext()));
        }
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toXmlyCategoryActivity(dra);
            }
        });
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoryRecommendList(dra, content_tv);
            }
        });
        return view;
    }

    private View initAlbum(final Album mAVObject) {
        View view = inflater.inflate(R.layout.ximalaya_list_item, null);
        FrameLayout layout_cover = (FrameLayout) view.findViewById(R.id.layout_cover);
        SimpleDraweeView list_item_img = (SimpleDraweeView) view.findViewById(R.id.list_item_img);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView sub_title = (TextView) view.findViewById(R.id.sub_title);
        TextView source_name = (TextView) view.findViewById(R.id.source_name);
        TextView type_name = (TextView) view.findViewById(R.id.type_name);
        title.setText(mAVObject.getAlbumTitle());
        sub_title.setText(mAVObject.getAlbumIntro());
        source_name.setText(StringUtils.numToStrTimes(mAVObject.getPlayCount()));
        type_name.setText(" " + String.valueOf(mAVObject.getIncludeTrackCount()) + " 集");
        list_item_img.setImageURI(Uri.parse(mAVObject.getCoverUrlLarge()));
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        source_name.setCompoundDrawables(drawable, null, null, null);
        Drawable dra = this.getResources().getDrawable(R.drawable.ic_item_sounds_count);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        type_name.setCompoundDrawables(dra, null, null, null);
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAlbumActivity(mAVObject);
            }
        });
        return view;
    }

    private void getCategoryRecommendList(final CategoryRecommendAlbums dra, final LinearLayout content_tv) {
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, XimalayaUtil.Category_Eng);
        if (!TextUtils.isEmpty(dra.getTagName())) {
            map.put(DTransferConstants.TAG_NAME, dra.getTagName());
        }
        map.put(DTransferConstants.CALC_DIMENSION, "1");
        map.put(DTransferConstants.PAGE_SIZE, RandomNum);
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(@Nullable AlbumList albumList) {
                onFinishLoadData();
                if (albumList != null && albumList.getAlbums() != null) {
                    skip += 1;
                    if (skip > albumList.getTotalPage()) {
                        skip = 1;
                    }
                    content_tv.removeAllViews();
                    for (Album album : albumList.getAlbums()) {
                        content_tv.addView(initAlbum(album));
                        content_tv.addView(ViewUtil.getLine(getContext()));
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                onFinishLoadData();
                LogUtil.DefalutLog(s);
            }
        });
    }

    private void toAlbumActivity(final Album mAVObject) {
        Intent intent = new Intent(getActivity(), XimalayaTrackListActivity.class);
        intent.putExtra("album_id", mAVObject.getId() + "");
        intent.putExtra("play_times", mAVObject.getPlayCount());
        intent.putExtra("track_count", mAVObject.getIncludeTrackCount());
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getAlbumTitle());
        startActivity(intent);
        AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_album");
    }

    private void toXmlyCategoryActivity(CategoryRecommendAlbums dra) {
        Intent intent = new Intent(getContext(),XmlyAlbumActivity.class);
        intent.putExtra(KeyUtil.Category, XimalayaUtil.Category_Eng);
        intent.putExtra(KeyUtil.Xmly_Tag, dra.getTagName());
        if(!TextUtils.isEmpty(dra.getTagName())){
            intent.putExtra(KeyUtil.ActionbarTitle, dra.getTagName());
        }else {
            intent.putExtra(KeyUtil.ActionbarTitle, "精选");
        }
        startActivity(intent);
        AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_category");
    }

    private void onFinishLoadData() {
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        QueryTask();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcast();
    }

}
