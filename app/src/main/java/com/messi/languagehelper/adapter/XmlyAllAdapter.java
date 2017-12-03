package com.messi.languagehelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.XimalayaRadioHomeFragment;
import com.messi.languagehelper.XmlyCategoryFragment;
import com.messi.languagehelper.XmlyCategoryRecommendFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

public class XmlyAllAdapter extends FragmentPagerAdapter {

    private FragmentProgressbarListener listener;

    public XmlyAllAdapter(FragmentManager fm,FragmentProgressbarListener listener) {
        super(fm);
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return XmlyCategoryFragment.getInstance();
        }else if(position == 1) {
            return XmlyCategoryRecommendFragment.newInstance("","",listener);
        }else{
            return XimalayaRadioHomeFragment.newInstance(listener);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "分类";
        }else if(position == 1) {
            return "推荐";
        }else {
            return "广播";
        }

    }
}