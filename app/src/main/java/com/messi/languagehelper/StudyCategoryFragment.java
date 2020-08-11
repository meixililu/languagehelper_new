package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.coursefragment.CourseListFragment;
import com.messi.languagehelper.coursefragment.CourseTypeActivity;
import com.messi.languagehelper.databinding.StudyCategoryFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;

public class StudyCategoryFragment extends BaseFragment {

    private StudyCategoryFragmentBinding binding;
    public static StudyCategoryFragment getInstance() {
        return new StudyCategoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = StudyCategoryFragmentBinding.inflate(inflater);
        initViews();
        initFragment();
        return binding.getRoot();
    }

    private void initFragment(){
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, CourseListFragment.Companion.getInstance(3,"", "base"))
                .commit();
    }

    public void initViews() {
        binding.symbolStudyCover.setOnClickListener(view -> {
            toActivity(SymbolActivity.class, null);
            AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
        });
        binding.studyListeningLayout.setOnClickListener(view -> {
            toActivity(ListenActivity.class, null);
            AVAnalytics.onEvent(getContext(), "tab3_to_listening");
        });
        binding.enExaminationLayout.setOnClickListener(view -> {
            toActivity(AiChatActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.studyComposition.setOnClickListener(view -> {
            toActivity(ComExamActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.collectedLayout.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(KeyUtil.ActionbarTitle, getContext().getString(R.string.title_reading_comprehension));
            bundle.putString(KeyUtil.Type, "comprehension");
            bundle.putInt(KeyUtil.Column, 3);
            toActivity(CourseTypeActivity.class,bundle);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.studySpokenEnglish.setOnClickListener(view -> {
            toActivity(SpokenActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.enGrammar.setOnClickListener(view -> {
            toActivity(MomentsActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.enStoryLayout.setOnClickListener(view -> {
            toActivity(StoryActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.xmlyLayout.setOnClickListener(view -> {
            toActivity(XmlyActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
        binding.studyWordLayout.setOnClickListener(view -> {
            toActivity(WordsActivity.class,null);
            AVAnalytics.onEvent(getContext(), "tab3_to_examination");
        });
    }

}
