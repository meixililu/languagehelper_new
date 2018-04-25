package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SaveData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordHomeFragment extends BaseFragment {

    @BindView(R.id.word_study_change_plan)
    FrameLayout wordStudyChangePlan;
    @BindView(R.id.word_study_book_name)
    TextView wordStudyBookName;
    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;

    private WordListItem wordListItem;

    public static WordHomeFragment getInstance() {
        return new WordHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.word_home_fragment, null);
        ButterKnife.bind(this, view);
        setBookName();
        return view;
    }

    private void setBookName() {
        wordListItem = SaveData.getDataFonJson(getContext(), KeyUtil.WordStudyUnit, WordListItem.class);
        if (wordListItem != null && wordStudyBookName != null) {
            wordStudyBookName.setText(wordListItem.getTitle());
            arcProgress.setMax(wordListItem.getCourse_num());
            arcProgress.setProgress(wordListItem.getCourse_id());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setBookName();
    }

    @OnClick({R.id.word_study_new_word,R.id.word_study_change_plan,
            R.id.word_study_plan,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.word_study_change_plan:
                toActivity(WordStudyPlanActivity.class, null);
                AVAnalytics.onEvent(getContext(), "wordstudy_change_plan");
                break;
            case R.id.word_study_new_word:
                toActivity(WordStudyNewWordActivity.class, null);
                AVAnalytics.onEvent(getContext(), "wordstudy_new_word");
                break;
            case R.id.word_study_plan:
                toWordStudyDetailActivity();
                AVAnalytics.onEvent(getContext(), "wordstudy_detail");
                break;
        }
    }

    private void toWordStudyDetailActivity() {
        if (wordListItem != null) {
            Intent intent = new Intent(getContext(), WordStudyPlanDetailActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle, wordListItem.getTitle());
            getActivity().startActivity(intent);
        } else {
            toActivity(WordStudyPlanActivity.class, null);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
