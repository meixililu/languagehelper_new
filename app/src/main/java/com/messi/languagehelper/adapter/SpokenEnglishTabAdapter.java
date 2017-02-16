package com.messi.languagehelper.adapter;

import com.messi.languagehelper.JichuxiulianFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.SpokenEnglishPractiseFragment;
import com.messi.languagehelper.util.AVOUtil;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SpokenEnglishTabAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	
    public SpokenEnglishTabAdapter(FragmentManager fm,Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.spoken_english_reading),
        		mContext.getResources().getString(R.string.title_practice_word)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return SpokenEnglishPractiseFragment.getInstance();
        }else if( position == 1 ){
        	return JichuxiulianFragment.getInstance();
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