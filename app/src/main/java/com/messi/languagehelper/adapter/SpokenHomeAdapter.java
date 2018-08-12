package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.SpokenAIFragment;
import com.messi.languagehelper.util.AVOUtil;

public class SpokenHomeAdapter extends FragmentPagerAdapter {

    public static String[] CONTENT;
    private Context mContext;

    public SpokenHomeAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        CONTENT = new String[] {
                mContext.getResources().getString(R.string.spoken_english_practice),
                mContext.getResources().getString(R.string.spoken_english_coach)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return SpokenAIFragment.newInstance(AVOUtil.Category.spoken_english,"");
        }else if( position == 1 ){
            return ReadingFragment.newInstanceBySource(AVOUtil.Category.spoken_english,"口语天堂");
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