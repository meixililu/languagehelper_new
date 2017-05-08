package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyCategoryFragment extends BaseFragment {


    @BindView(R.id.symbol_study_cover)
    FrameLayout symbolStudyCover;
    @BindView(R.id.word_study_cover)
    FrameLayout wordStudyCover;
    @BindView(R.id.study_listening_layout)
    FrameLayout studyListeningLayout;
    @BindView(R.id.news_layout)
    FrameLayout newsLayout;
    @BindView(R.id.study_spoken_english)
    FrameLayout studySpokenEnglish;
    @BindView(R.id.study_test)
    FrameLayout studyTest;
    @BindView(R.id.en_examination_layout)
    FrameLayout enExaminationLayout;
    @BindView(R.id.study_composition)
    FrameLayout studyComposition;
    @BindView(R.id.juhe_layout)
    FrameLayout juheLayout;
    @BindView(R.id.jokes_layout)
    FrameLayout jokesLayout;
    @BindView(R.id.story_layout)
    FrameLayout storyLayout;
    @BindView(R.id.instagram_layout)
    FrameLayout instagramLayout;
    @BindView(R.id.collected_layout)
    FrameLayout collectedLayout;

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
        initViews();
        return view;
    }

    private void initViews() {

    }

    @Override
    public void updateUI(String music_action) {
        if (music_action.equals(PlayerService.action_loading)) {
            showProgressbar();
        } else if (music_action.equals(PlayerService.action_finish_loading)) {
            hideProgressbar();
        } else {
            //
        }
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();

    }

    @OnClick({R.id.symbol_study_cover, R.id.word_study_cover, R.id.study_listening_layout,
            R.id.news_layout, R.id.study_spoken_english, R.id.study_test,
            R.id.en_examination_layout, R.id.study_composition, R.id.juhe_layout,
            R.id.jokes_layout, R.id.story_layout, R.id.instagram_layout,
            R.id.collected_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.symbol_study_cover:
                toActivity(SymbolListActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
                break;
            case R.id.word_study_cover:
                toActivity(WordStudyActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy");
                break;
            case R.id.study_listening_layout:
                toReadingActivity(getContext().getResources().getString(R.string.title_listening),
                        AVOUtil.Category.listening);
                AVAnalytics.onEvent(getContext(), "tab3_to_listening");
                break;
            case R.id.news_layout:
                toReadingActivity(getContext().getResources().getString(R.string.reading),
                        AVOUtil.Category.shuangyu_reading);
                AVAnalytics.onEvent(getContext(), "tab3_to_reading");
                break;
            case R.id.study_spoken_english:
                toReadingActivity(getContext().getResources().getString(R.string.title_spoken_english_study), AVOUtil.Category.spoken_english);
                AVAnalytics.onEvent(getContext(), "tab3_study_spokenenglish_basic");
                break;
            case R.id.study_test:
                toActivity(JichuxiulianAndKouyulianxiActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_evaluation");
                break;
            case R.id.en_examination_layout:
                toExaminationActivity(getContext().getResources().getString(R.string.examination));
                AVAnalytics.onEvent(getContext(), "tab3_to_examination");
                break;
            case R.id.study_composition:
                toActivity(CompositionActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_composition");
                break;
            case R.id.juhe_layout:
                toReadingJuheActivity(getContext().getResources().getString(R.string.title_juhe));
                AVAnalytics.onEvent(getContext(), "tab3_to_juhenews");
                break;
            case R.id.jokes_layout:
                toReadingActivity(getContext().getResources().getString(R.string.title_english_video),
                        "", "video");
                AVAnalytics.onEvent(getContext(), "tab3_to_videos");
                break;
            case R.id.story_layout:
                toReadingActivity(getContext().getResources().getString(R.string.title_english_story),
                        AVOUtil.Category.story);
                AVAnalytics.onEvent(getContext(), "tab3_to_story");
                break;
            case R.id.instagram_layout:
                toActivity(EnglishWebsiteRecommendActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_websiterecommend");
                break;
            case R.id.collected_layout:
                toActivity(CollectedActivity.class, null);
                AVAnalytics.onEvent(getContext(), "index_pg_to_collectedpg");
                break;
        }
    }

    private void toReadingActivity(String title, String category) {
        Intent intent = new Intent(getContext(), ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        intent.putExtra(KeyUtil.Category, category);
        getContext().startActivity(intent);
    }

    private void toReadingActivity(String title, String category, String type) {
        Intent intent = new Intent(getContext(), ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        intent.putExtra(KeyUtil.Category, category);
        intent.putExtra(KeyUtil.NewsType, type);
        getContext().startActivity(intent);
    }

    private void toReadingJuheActivity(String title) {
        Intent intent = new Intent(getContext(), ReadingJuheActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getContext().startActivity(intent);
    }

    private void toExaminationActivity(String title) {
        Intent intent = new Intent(getContext(), ExaminationActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getContext().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcast();
    }
}
