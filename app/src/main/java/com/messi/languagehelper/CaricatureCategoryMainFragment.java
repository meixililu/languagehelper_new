package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.messi.languagehelper.adapter.CaricatureCategoryMainAdapter;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;

public class CaricatureCategoryMainFragment extends BaseFragment implements View.OnClickListener{

    private TabLayout tablayout;
    private ViewPager viewpager;
    private FrameLayout search_btn;
    private CaricatureCategoryMainAdapter pageAdapter;

    public static CaricatureCategoryMainFragment getInstance() {
        return new CaricatureCategoryMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tablayout_with_search_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        search_btn = (FrameLayout) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);

        pageAdapter = new CaricatureCategoryMainAdapter(getChildFragmentManager(),getContext());
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.search_btn){
            toSearchActivity();
        }
    }

    private void toSearchActivity(){
        if(Setings.IsShowNovel){
            toKSearch();
        }else {
            toActivity(CaricatureSearchActivity.class,null);
        }
    }

    private void toKSearch(){
        Intent intent = new Intent(getContext(),CNSearchActivity.class);
        intent.putExtra(KeyUtil.PositionKey,0);
        startActivity(intent);
    }
}
