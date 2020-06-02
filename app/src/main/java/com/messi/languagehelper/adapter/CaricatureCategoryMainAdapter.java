package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messi.languagehelper.CaricatureCategoryFragment;
import com.messi.languagehelper.CaricatureWebListFragment;
import com.messi.languagehelper.R;


public class CaricatureCategoryMainAdapter extends FragmentPagerAdapter {

    public static String[] CONTENT;
    private Context mContext;

    public CaricatureCategoryMainAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        CONTENT = new String[] {
                mContext.getResources().getString(R.string.title_category),
                mContext.getResources().getString(R.string.title_source)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return CaricatureCategoryFragment.newInstance();
        }else if( position == 1 ){
            return CaricatureWebListFragment.newInstance();
        }
        return null;
    }

    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position].toUpperCase();
    }

}