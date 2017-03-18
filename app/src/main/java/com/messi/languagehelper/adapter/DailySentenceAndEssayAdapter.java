package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.DailySentenceFragment;
import com.messi.languagehelper.EssayFragment;
import com.messi.languagehelper.R;

public class DailySentenceAndEssayAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public DailySentenceAndEssayAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.dailysentence),
        		mContext.getResources().getString(R.string.english_essay)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return new DailySentenceFragment();
        }else if( position == 1 ){
        	return new EssayFragment();
        }
        return null;
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