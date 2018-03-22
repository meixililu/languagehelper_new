package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.LeisureFragment;
import com.messi.languagehelper.MainFragment;
import com.messi.languagehelper.MainFragmentOld;
import com.messi.languagehelper.R;
import com.messi.languagehelper.StudyCategoryFragment;
import com.messi.languagehelper.StudyFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;

public class MainPageAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	private Bundle bundle;
	private Activity mContext;
	private SharedPreferences mSharedPreferences;
    private StudyFragment mfragment;
    private FragmentProgressbarListener listener;

    public MainPageAdapter(FragmentManager fm,Bundle bundle,Activity mContext,
                           SharedPreferences mSharedPreferences,
                           FragmentProgressbarListener listener) {
        super(fm);
        this.mContext = mContext;
        this.bundle = bundle;
        this.listener = listener;
        this.mSharedPreferences = mSharedPreferences;
        CONTENT = new String[] {
        		mContext.getResources().getString(R.string.title_home_tab),
        		mContext.getResources().getString(R.string.title_study_category),
        		mContext.getResources().getString(R.string.title_study),
        		mContext.getResources().getString(R.string.title_leisure)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            if(mSharedPreferences.getBoolean(KeyUtil.IsUseOldStyle,true)){
                return MainFragmentOld.getInstance(listener);
            }else {
        	    return MainFragment.getInstance(listener);
            }
        }else if( position == 1 ){
        	return StudyCategoryFragment.getInstance();
        }else if( position == 2 ){
            mfragment = StudyFragment.getInstance();
        	return mfragment;
        }else if( position == 3 ){
        	return LeisureFragment.getInstance();
        }
        return null;
    }
//    if(mSharedPreferences.getBoolean(KeyUtil.IsUseOldStyle,true)){
//        return DictionaryFragmentOld.getInstance(bundle,mContext);
//    }else {
//        return DictionaryFragment.getInstance(bundle,mContext);
//    }

    public void onTabReselected(int index){
        if(mfragment != null){
            mfragment.onTabReselected(index);
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