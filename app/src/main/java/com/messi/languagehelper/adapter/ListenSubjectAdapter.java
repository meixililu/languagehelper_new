package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsBySubjectFragment;
import com.messi.languagehelper.SubjectFragment;
import com.messi.languagehelper.SymbolListFragment;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Settings;

import java.util.ArrayList;

public class ListenSubjectAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList = new ArrayList<String>();
    private Context mContext;
    private String category_2;

    public ListenSubjectAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        category_2 = Settings.getSharedPreferences(mContext).getString(KeyUtil.RecentListen,"");
        if(TextUtils.isEmpty(category_2)){
            addTitle();
        }else {
            titleList.add(mContext.getResources().getString(R.string.recent));
            addTitle();
        }
    }

    private void addTitle(){
        titleList.add(mContext.getResources().getString(R.string.beginner));
        titleList.add(mContext.getResources().getString(R.string.intermediate));
        titleList.add(mContext.getResources().getString(R.string.advanced));
    }

    @Override
    public Fragment getItem(int position) {
        if(TextUtils.isEmpty(category_2)){
            if( position == 0 ){
                return SubjectFragment.getInstance("listening",KeyUtil.RecentListen,"1");
            }else if( position == 1 ){
                return SubjectFragment.getInstance("listening",KeyUtil.RecentListen,"2");
            }else if( position == 2 ){
                return SubjectFragment.getInstance("listening",KeyUtil.RecentListen,"3");
            }
        }else {
            if( position == 0 ){
                return ReadingsBySubjectFragment.newInstance(category_2,KeyUtil.RecentListen,"");
            }else if( position == 1 ){
                return SubjectFragment.getInstance("listening",KeyUtil.RecentListen,"1");
            }else if( position == 2 ){
                return SubjectFragment.getInstance("listening",KeyUtil.RecentListen,"2");
            }else if( position == 3 ){
                return SubjectFragment.getInstance("listening",KeyUtil.RecentListen,"3");
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