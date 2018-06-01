package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.ListenHomeAdapter;

public class ListenHomeFragment extends BaseFragment{

    private TabLayout tablayout;
    private ViewPager viewpager;
    private ListenHomeAdapter pageAdapter;

    public static ListenHomeFragment getInstance() {
        return new ListenHomeFragment();
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

        pageAdapter = new ListenHomeAdapter(getChildFragmentManager(),getContext());
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewpager);
    }

    public int getCurrentPosition(){
        return viewpager.getCurrentItem();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return pageAdapter.getWebViewFragment(viewpager.getCurrentItem()).onKeyDown(keyCode,event);
    }
}
