package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.CaricatureBHAdapter;

public class CaricatureBHFragment extends BaseFragment{

    private TabLayout tablayout;
    private ViewPager viewpager;
    private CaricatureBHAdapter pageAdapter;

    public static CaricatureBHFragment getInstance() {
        return new CaricatureBHFragment();
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

        pageAdapter = new CaricatureBHAdapter(getChildFragmentManager(),getContext());
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewpager);
    }

}
