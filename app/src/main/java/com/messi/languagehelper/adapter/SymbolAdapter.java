package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.ReadingsBySubjectFragment;
import com.messi.languagehelper.SubjectFragment;
import com.messi.languagehelper.SymbolListFragment;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;

import java.util.ArrayList;

public class SymbolAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList = new ArrayList<String>();
    private Context mContext;
    private String category_2;

    public SymbolAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        category_2 = Setings.getSharedPreferences(mContext).getString(KeyUtil.RecentSymbol,"");
        titleList.add(mContext.getResources().getString(R.string.recommend));
        titleList.add(mContext.getResources().getString(R.string.title_study_category));
        titleList.add(mContext.getResources().getString(R.string.title_course));
        titleList.add(mContext.getResources().getString(R.string.recent));
    }

    private void addTitle(){

    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return SymbolListFragment.getInstance();
        }else if( position == 1 ){
            return ReadingFragment.newInstanceBySearchTitle("","音标");
        }else if( position == 2 ){
            return SubjectFragment.getInstance(AVOUtil.Category.symbol,KeyUtil.RecentSymbol,"");
        }else if( position == 3 ){
            return ReadingsBySubjectFragment.newInstance(category_2,KeyUtil.RecentSymbol,"");
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