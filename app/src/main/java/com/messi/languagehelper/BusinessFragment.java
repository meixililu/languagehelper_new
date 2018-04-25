package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.BusinessAdapter;

public class BusinessFragment extends BaseFragment {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private BusinessAdapter pageAdapter;

    public static BusinessFragment getInstance() {
        return new BusinessFragment();
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

        pageAdapter = new BusinessAdapter(getChildFragmentManager(),getContext());
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewpager);
    }

}
