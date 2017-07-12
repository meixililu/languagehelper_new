package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.JichuxiulianFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.SpokenEnglishPractiseFragment;
import com.messi.languagehelper.SymbolListFragment;
import com.messi.languagehelper.SymbolMoreFragment;

public class SymbolAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public SymbolAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.recommend),
        		mContext.getResources().getString(R.string.title_more)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return SymbolListFragment.getInstance();
        }else if( position == 1 ){
        	return SymbolMoreFragment.getInstance();
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