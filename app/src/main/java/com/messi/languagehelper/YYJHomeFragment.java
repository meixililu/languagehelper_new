package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YYJHomeFragment extends BaseFragment {

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
    @BindView(R.id.xmly_layout)
    FrameLayout xmlyLayout;
    @BindView(R.id.translate_layout)
    FrameLayout translateLayout;
    @BindView(R.id.study_setting)
    FrameLayout studySetting;

    public static YYJHomeFragment getInstance() {
        return new YYJHomeFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yyj_home_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.symbol_study_cover, R.id.study_listening_layout,
            R.id.en_examination_layout, R.id.study_composition, R.id.collected_layout,
            R.id.study_spoken_english, R.id.en_grammar, R.id.en_story_layout,
            R.id.xmly_layout, R.id.study_word_layout,R.id.translate_layout, R.id.study_setting})
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
                toActivity(AiChatActivity.class,null);
                AVAnalytics.onEvent(getContext(), "tab3_to_examination");
                break;
            case R.id.study_composition:
                toActivity(ComExamActivity.class, null);
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
                toActivity(MomentsActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_grammar");
                break;
            case R.id.en_story_layout:
                toActivity(StoryActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_story");
                break;
            case R.id.xmly_layout:
                toActivity(XmlyActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_home");
                break;
            case R.id.study_word_layout:
                toActivity(WordsActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_home");
                break;
            case R.id.translate_layout:
                toTranslate();
                break;
            case R.id.study_setting:
                toActivity(MoreActivity.class, null);
                break;
        }
    }

    private void toTranslate() {
        Intent intent = new Intent(getContext(), TitleActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, getResources().getString(R.string.translate));
        intent.putExtra(KeyUtil.Type, R.string.translate);
        startActivity(intent);
    }

    private void toExaminationActivity(String title) {
        Intent intent = new Intent(getContext(), ExaminationActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getContext().startActivity(intent);
    }

}
