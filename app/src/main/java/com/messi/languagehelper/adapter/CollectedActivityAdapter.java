package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.CollectedDictionaryFragment;
import com.messi.languagehelper.CollectedTranslateFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingCollectedListFragment;
import com.messi.languagehelper.SubjectSubscribeFragment;

public class CollectedActivityAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public CollectedActivityAdapter(FragmentManager fm,Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.recent),
        		mContext.getResources().getString(R.string.title_translate),
        		mContext.getResources().getString(R.string.title_dictionary),
        		mContext.getResources().getString(R.string.title_article_collected),
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return SubjectSubscribeFragment.getInstance();
        }else if( position == 1 ){
        	return new CollectedTranslateFragment();
        }else if( position == 2 ){
        	return new CollectedDictionaryFragment();
        }else if( position == 3 ){
            return ReadingCollectedListFragment.newInstance();
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