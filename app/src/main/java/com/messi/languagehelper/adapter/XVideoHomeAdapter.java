package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.XVideoFragment;

public class XVideoHomeAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public XVideoHomeAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] {"推荐","英语","搞笑","游戏","舞蹈","美女","明星","帅哥"};
    }

    @Override
    public Fragment getItem(int position) {
        return XVideoFragment.newInstance(CONTENT[position]);
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