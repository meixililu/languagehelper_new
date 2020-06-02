package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messi.languagehelper.CaricatureSearchResultFragment;
import com.messi.languagehelper.MiaosouResultListFragment;
import com.messi.languagehelper.R;


public class CaricatureSearchResultAdapter extends FragmentPagerAdapter {

    public static String[] CONTENT;
    private Context mContext;
    private String search_text;
    private String url;

    public CaricatureSearchResultAdapter(FragmentManager fm, Context mContext,String search_text,
                                         String url) {
        super(fm);
        this.mContext = mContext;
        this.search_text = search_text;
        this.url = url;
        CONTENT = new String[] {
                mContext.getResources().getString(R.string.title_zhannei),
                mContext.getResources().getString(R.string.title_zhanwai)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return CaricatureSearchResultFragment.getInstance(search_text);
        }else if( position == 1 ){
            return MiaosouResultListFragment.newInstance(url);
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