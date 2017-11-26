package com.messi.languagehelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.XimalayaFragment;
import com.messi.languagehelper.XmlyCategoryFragment;

public class XmlyAllAdapter extends FragmentPagerAdapter {


    public XmlyAllAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return XmlyCategoryFragment.getInstance();
        }else if(position == 1) {
            return XimalayaFragment.newInstance("1", "");
        }else if(position == 2) {
            return XimalayaFragment.newInstance("6", "");
        }else {
            return XimalayaFragment.newInstance("39", "");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "分类";
        }else if(position == 1) {
            return "推荐";
        }else if(position == 2) {
            return "直播";
        }else {
            return "广播";
        }

    }
}