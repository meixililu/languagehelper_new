package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SaveData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyCategoryFragment extends BaseFragment {


    @BindView(R.id.symbol_study_cover)
    FrameLayout symbolStudyCover;
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
    @BindView(R.id.word_study_change_plan)
    FrameLayout wordStudyChangePlan;
    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;
    @BindView(R.id.word_study_plan)
    RelativeLayout wordStudyPlan;
    @BindView(R.id.word_study_view_all)
    FrameLayout wordStudyViewAll;
    @BindView(R.id.word_study_daily)
    FrameLayout wordStudyDaily;
    @BindView(R.id.word_study_new_word)
    FrameLayout wordStudyNewWord;
    @BindView(R.id.word_study_book_name)
    TextView wordStudyBookName;

    private WordListItem wordListItem;

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
        setBookName();
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

    private void setBookName(){
        wordListItem =  SaveData.getDataFonJson(getContext(), KeyUtil.WordStudyUnit, WordListItem.class);
        if(wordListItem != null && wordStudyBookName != null){
            wordStudyBookName.setText(wordListItem.getTitle());
            arcProgress.setMax(wordListItem.getCourse_num());
            arcProgress.setProgress(wordListItem.getCourse_id());
            LogUtil.DefalutLog("wordListItem:"+wordListItem.getCourse_id());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.DefalutLog("onResume");
        setBookName();
    }

    @OnClick({R.id.word_study_view_all, R.id.word_study_daily, R.id.symbol_study_cover,
            R.id.study_listening_layout, R.id.word_study_change_plan,
            R.id.word_study_plan,
            R.id.news_layout, R.id.study_spoken_english, R.id.study_test,
            R.id.en_examination_layout, R.id.study_composition, R.id.juhe_layout,
            R.id.jokes_layout, R.id.story_layout, R.id.instagram_layout,
            R.id.collected_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.word_study_view_all:
                toActivity(WordStudyActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_view_all");
                break;
            case R.id.word_study_daily:
                toReadingActivity(getContext().getResources().getString(R.string.title_word_vocabulary),
                        AVOUtil.Category.word);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_daily");
                break;
            case R.id.word_study_change_plan:
                toActivity(WordStudyPlanActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_daily");
                break;
            case R.id.word_study_plan:
                toWordStudyDetailActivity();
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_daily");
                break;
            case R.id.symbol_study_cover:
                toActivity(SymbolListActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
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

    private void toWordStudyDetailActivity(){
        if(wordListItem != null){
            Intent intent = new Intent(getContext(), WordStudyPlanDetailActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle, wordListItem.getTitle());
            getActivity().startActivity(intent);
        }else {
            toActivity(WordStudyPlanActivity.class, null);
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
