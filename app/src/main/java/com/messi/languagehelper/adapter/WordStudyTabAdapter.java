package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudyFirstFragment;
import com.messi.languagehelper.WordStudySummaryFragment;

public class WordStudyTabAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	
    public WordStudyTabAdapter(FragmentManager fm,Context context) {
        super(fm);
        CONTENT = new String[] {
                context.getResources().getString(R.string.title_word_study),
                context.getResources().getString(R.string.title_word_study_vocabulary)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return new WordStudyFirstFragment();
        }else if( position == 1 ){
        	return new WordStudySummaryFragment();
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