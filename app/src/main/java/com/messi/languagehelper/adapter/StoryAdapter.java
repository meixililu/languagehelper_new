package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsBySubjectFragment;
import com.messi.languagehelper.SubjectFragment;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Settings;

import java.util.ArrayList;

public class StoryAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList = new ArrayList<String>();
    private Context mContext;
    private String category_2;

    public StoryAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        category_2 = Settings.getSharedPreferences(mContext).getString(KeyUtil.RecentStory,"");
        if(TextUtils.isEmpty(category_2)){
            addTitle();
            titleList.add(mContext.getResources().getString(R.string.recent));
        }else {
            titleList.add(mContext.getResources().getString(R.string.recent));
            addTitle();
        }
    }

    private void addTitle(){
        titleList.add(mContext.getResources().getString(R.string.title_english_story));
    }

    @Override
    public Fragment getItem(int position) {
        if(TextUtils.isEmpty(category_2)){
            if( position == 0 ){
                return SubjectFragment.getInstance(AVOUtil.Category.story,KeyUtil.RecentStory,"","desc");
            }else if( position == 1 ){
                return ReadingsBySubjectFragment.newInstance(category_2,KeyUtil.RecentStory,"");
            }
        }else {
            if( position == 0 ){
                return ReadingsBySubjectFragment.newInstance(category_2,KeyUtil.RecentStory,"");
            }else if( position == 1 ){
                return SubjectFragment.getInstance(AVOUtil.Category.story,KeyUtil.RecentStory,"","desc");
            }
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