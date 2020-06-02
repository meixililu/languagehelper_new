package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.bean.ReadingCategory;
import com.messi.languagehelper.databinding.StudyTabFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.XimalayaUtil;

import java.util.List;

public class StudyTabFragment extends BaseFragment {

    private StudyTabFragmentBinding binding;
    private List<ReadingCategory> categories;
//    private int position;
//    private SharedPreferences sp;
    private Fragment studyFragment;
    private SubjectFragment subjectFragment;
    private BoutiquesFragment boutiquesFragment;
    private XmlyRecommendFragment mXmlyRecommendFragment;
//    private Fragment mReadingFragment;

    public static StudyTabFragment getInstance() {
        return new StudyTabFragment();
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
        binding = StudyTabFragmentBinding.inflate(inflater);
        initTablayout();
        return binding.getRoot();
    }

    private void initTablayout() {
//        sp = Setings.getSharedPreferences(getContext());
//        position = sp.getInt(KeyUtil.StudyTabPosition,0);
        binding.searchBtn.setOnClickListener(view -> onViewClicked(view));
        categories = DataUtil.getStudyTab(getContext());
        for (ReadingCategory item : categories) {
            binding.tablayout.addTab(binding.tablayout.newTab().setText(item.getName()));
        }
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragment(tab.getPosition());
//                Setings.saveSharedPreferences(sp, KeyUtil.StudyTabPosition, tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                showFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
        binding.tablayout.getTabAt(0).select();
    }

    private void showFragment(int position){
        hideAllFragment();
        Fragment fragment = null;
        switch (position){
            case 0:
                if (studyFragment == null) {
                    studyFragment = new ReadingFragment.Builder().build();
                    addFragment(studyFragment);
                }
                fragment = studyFragment;
                break;
            case 1:
                if (boutiquesFragment == null) {
                    boutiquesFragment = new BoutiquesFragment.Builder()
                            .type("english")
                            .build();
                    addFragment(boutiquesFragment);
                }
                fragment = boutiquesFragment;
                break;
            case 2:
                if (mXmlyRecommendFragment == null) {
                    mXmlyRecommendFragment = XmlyRecommendFragment.newInstance();
                    mXmlyRecommendFragment.refreshByTags(XimalayaUtil.Category_english,"");
                    addFragment(mXmlyRecommendFragment);
                }
                fragment = mXmlyRecommendFragment;
                break;
            case 3:
                if (subjectFragment == null) {
                    subjectFragment = SubjectFragment.getInstance("",500);
                    addFragment(subjectFragment);
                }
                fragment = subjectFragment;
                break;
        }
        getChildFragmentManager()
                .beginTransaction().show(fragment).commit();
    }

    private void addFragment(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout,fragment)
                .commit();
    }

    private void hideAllFragment(){
        if (studyFragment != null) {
            hideFragment(studyFragment);
        }
        if (boutiquesFragment != null) {
            hideFragment(boutiquesFragment);
        }
        if (subjectFragment != null) {
            hideFragment(subjectFragment);
        }
        if (mXmlyRecommendFragment != null) {
            hideFragment(mXmlyRecommendFragment);
        }
    }

    private void hideFragment(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .hide(fragment)
                .commit();
    }

    public void onViewClicked(View view) {
        toActivity(SearchActivity.class,null);
        AVAnalytics.onEvent(getContext(), "tab4_to_search");
    }

}
