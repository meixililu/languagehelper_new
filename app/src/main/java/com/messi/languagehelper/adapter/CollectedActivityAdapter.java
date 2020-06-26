package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
        		mContext.getResources().getString(R.string.title_course),
        		mContext.getResources().getString(R.string.title_article_collected),
        		mContext.getResources().getString(R.string.title_my_newword),
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return SubjectSubscribeFragment.getInstance();
        }else if( position == 1 ){
            return new ReadingCollectedListFragment();
        }else if( position == 2 ){
        	return new CollectedTranslateFragment();
        }else if( position == 3 ){
        	return new CollectedDictionaryFragment();
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