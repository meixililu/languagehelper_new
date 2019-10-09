package com.messi.languagehelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.XmlyRecommendFragment;
import com.messi.languagehelper.util.LogUtil;

public class XmlyRecommendPageAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
    public XmlyRecommendFragment mFragment;

    public XmlyRecommendPageAdapter(FragmentManager fm) {
        super(fm);
        CONTENT = new String[] { 
        		"精选"
        };
        mFragment = XmlyRecommendFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment;
    }

    public void refreshByTags(String category, String tag_name) {
        LogUtil.DefalutLog("XmlyRecommendPageAdapter-refreshByTags()");
        mFragment.refreshByTags(category,tag_name);
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position].toUpperCase();
    }
}