package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.SubjectFragment;
import com.messi.languagehelper.SymbolListFragment;

public class ListenSubjectAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public ListenSubjectAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.beginner),
        		mContext.getResources().getString(R.string.intermediate),
        		mContext.getResources().getString(R.string.advanced)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return SubjectFragment.getInstance("listening","","1");
        }else if( position == 1 ){
        	return SubjectFragment.getInstance("listening","","2");
        }else if( position == 2 ){
            return SubjectFragment.getInstance("listening","","3");
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