package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.util.AVOUtil;

public class ListenHomeAdapter extends FragmentPagerAdapter {

    public static String[] CONTENT;
    private Context mContext;

    public ListenHomeAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        CONTENT = new String[] {
                mContext.getResources().getString(R.string.title_listening),
                mContext.getResources().getString(R.string.title_intensive_listening)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return ReadingFragment.newInstance(AVOUtil.Category.listening,"",true);
        }else if( position == 1 ){
            return ReadingFragment.newInstanceBySource(AVOUtil.Category.listening,"VOA慢速英语精听网",true);
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