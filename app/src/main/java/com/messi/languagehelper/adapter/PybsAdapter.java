package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.ChPybsFragment;
import com.messi.languagehelper.R;

public class PybsAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public PybsAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.ChDicPinyinList),
        		mContext.getResources().getString(R.string.ChDicBushouList)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return ChPybsFragment.getInstance(ChPybsFragment.pinyin);
        }else {
            return ChPybsFragment.getInstance(ChPybsFragment.bushou);
        }
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