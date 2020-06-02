package com.messi.languagehelper;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.SpokenEnglishSubjectAdapter;

public class SpokenCourseFragment extends BaseFragment {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private SpokenEnglishSubjectAdapter pageAdapter;

    public static SpokenCourseFragment getInstance() {
        return new SpokenCourseFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tablayout_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        pageAdapter = new SpokenEnglishSubjectAdapter(getChildFragmentManager(),getContext());
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(5);
        tablayout.setupWithViewPager(viewpager);
    }


}
