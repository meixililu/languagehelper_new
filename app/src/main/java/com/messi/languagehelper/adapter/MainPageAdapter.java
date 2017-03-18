package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.DictionaryFragment;
import com.messi.languagehelper.DictionaryFragmentOld;
import com.messi.languagehelper.LeisureFragment;
import com.messi.languagehelper.MainFragment;
import com.messi.languagehelper.MainFragmentOld;
import com.messi.languagehelper.R;
import com.messi.languagehelper.StudyFragment;
import com.messi.languagehelper.util.KeyUtil;

public class MainPageAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	private Bundle bundle;
	private Activity mContext;
	private SharedPreferences mSharedPreferences;
    private StudyFragment mfragment;

    public MainPageAdapter(FragmentManager fm,Bundle bundle,Activity mContext,SharedPreferences mSharedPreferences) {
        super(fm);
        this.mContext = mContext;
        this.bundle = bundle;
        this.mSharedPreferences = mSharedPreferences;
        CONTENT = new String[] {
        		mContext.getResources().getString(R.string.title_translate),
        		mContext.getResources().getString(R.string.title_dictionary),
        		mContext.getResources().getString(R.string.title_study),
        		mContext.getResources().getString(R.string.title_leisure)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            if(mSharedPreferences.getBoolean(KeyUtil.IsUseOldStyle,true)){
                return MainFragmentOld.getInstance(bundle,mContext);
            }else {
        	    return MainFragment.getInstance(bundle,mContext);
            }
        }else if( position == 1 ){
            if(mSharedPreferences.getBoolean(KeyUtil.IsUseOldStyle,true)){
                return DictionaryFragmentOld.getInstance(bundle,mContext);
            }else {
                return DictionaryFragment.getInstance(bundle,mContext);
            }
        }else if( position == 2 ){
            mfragment = StudyFragment.getInstance();
        	return mfragment;
        }else if( position == 3 ){
        	return LeisureFragment.getInstance();
        }
        return null;
    }

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