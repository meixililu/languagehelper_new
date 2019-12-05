package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.SubjectFragment;
import com.messi.languagehelper.util.AVOUtil;

import java.util.ArrayList;

public class VocabularyStudyAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList = new ArrayList<String>();

    public VocabularyStudyAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        titleList.add(mContext.getResources().getString(R.string.title_daily_word_study));
        titleList.add(mContext.getResources().getString(R.string.title_course));
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return ReadingFragment.newInstance(AVOUtil.Category.word,"");
        }else if( position == 1 ){
            return SubjectFragment.getInstance(AVOUtil.Category.word,"");
        }
        return null;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position).toUpperCase();
    }
}