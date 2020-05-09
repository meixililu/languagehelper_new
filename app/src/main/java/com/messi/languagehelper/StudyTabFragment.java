package com.messi.languagehelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.bean.ReadingCategory;
import com.messi.languagehelper.databinding.StudyTabFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.TablayoutOnSelectedListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.XimalayaUtil;

import java.util.List;

public class StudyTabFragment extends BaseFragment implements TablayoutOnSelectedListener {

    private StudyTabFragmentBinding binding;
    private List<ReadingCategory> categories;
    private int position;
    private SharedPreferences sp;
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
        initFragment();
        return binding.getRoot();
    }

    private void initTablayout() {
        sp = Setings.getSharedPreferences(getContext());
        position = sp.getInt(KeyUtil.StudyTabPosition,0);
        binding.searchBtn.setOnClickListener(view -> onViewClicked(view));
        categories = DataUtil.getStudyTab(getContext());
        for (ReadingCategory item : categories) {
            binding.tablayout.addTab(binding.tablayout.newTab().setText(item.getName()));
        }
        binding.tablayout.getTabAt(position).select();
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelectedListener(tab.getPosition(), categories.get(tab.getPosition()));
                Setings.saveSharedPreferences(sp, KeyUtil.StudyTabPosition, tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabReselectedListener(tab.getPosition(), categories.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initFragment(){
        studyFragment = new ReadingFragment.Builder().build();
        subjectFragment = SubjectFragment.getInstance("","","des","");
        boutiquesFragment = new BoutiquesFragment.Builder()
                .type("english")
                .build();
        mXmlyRecommendFragment = XmlyRecommendFragment.newInstance();
        mXmlyRecommendFragment.refreshByTags(XimalayaUtil.Category_english,"");
//        mReadingFragment = new Fragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout,studyFragment)
                .add(R.id.content_layout,subjectFragment)
                .add(R.id.content_layout, boutiquesFragment)
                .add(R.id.content_layout, mXmlyRecommendFragment)
//                .add(R.id.content_layout,mReadingFragment)
                .commit();
        hideAllFragment();
        showFragment(position);
    }

    private void showFragment(int position){
        hideAllFragment();
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = studyFragment;
                break;
            case 1:
                fragment = boutiquesFragment;
                break;
            case 2:
                fragment = mXmlyRecommendFragment;
                break;
            case 3:
                fragment = subjectFragment;
                break;
//            case 4:
//                fragment = mReadingFragment;
//                break;
        }
        getChildFragmentManager()
                .beginTransaction().show(fragment).commit();
    }

    private void hideAllFragment(){
        getChildFragmentManager()
                .beginTransaction()
                .hide(studyFragment)
                .hide(boutiquesFragment)
                .hide(subjectFragment)
                .hide(mXmlyRecommendFragment)
//                .hide(mReadingFragment)
                .commit();
    }

    @Override
    public void onTabSelectedListener(int position, ReadingCategory mReadingCategory) {
        showFragment(position);
    }

    @Override
    public void onTabReselectedListener(int position, ReadingCategory mReadingCategory) {

    }

    public void onViewClicked(View view) {
        toActivity(SearchActivity.class,null);
        AVAnalytics.onEvent(getContext(), "tab4_to_search");
    }

}
