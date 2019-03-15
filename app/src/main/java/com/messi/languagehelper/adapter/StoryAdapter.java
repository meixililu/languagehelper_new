package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.SubjectFragment;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

import java.util.ArrayList;

public class StoryAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList = new ArrayList<String>();

    public StoryAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        titleList.add(mContext.getResources().getString(R.string.title_grammar));
        titleList.add(mContext.getResources().getString(R.string.title_english_story));
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return SubjectFragment.getInstance(AVOUtil.Category.grammar,KeyUtil.RecentGrammar,"");
        }else if( position == 1 ){
            return SubjectFragment.getInstance(AVOUtil.Category.story,KeyUtil.RecentStory,"","desc");
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