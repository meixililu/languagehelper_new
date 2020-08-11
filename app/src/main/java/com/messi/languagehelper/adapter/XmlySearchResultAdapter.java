package com.messi.languagehelper.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messi.languagehelper.BiliSearchResultFragmeng;
import com.messi.languagehelper.R;
import com.messi.languagehelper.SearchResultReadingFragment;
import com.messi.languagehelper.SearchResultSubjectFragment;
import com.messi.languagehelper.XmlySearchAlbumFragment;
import com.messi.languagehelper.XmlySearchTrackFragment;

public class XmlySearchResultAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
    private String search_text;

    public XmlySearchResultAdapter(FragmentManager fm, Context mContext, String search_text) {
        super(fm);
        this.search_text = search_text;
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.title_juhe),
        		mContext.getResources().getString(R.string.title_english_video),
        		mContext.getResources().getString(R.string.xmly_album),
        		mContext.getResources().getString(R.string.title_course),
        		mContext.getResources().getString(R.string.xmly_track)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return SearchResultReadingFragment.newInstance(search_text,"");
        }else if( position == 1 ){
            return BiliSearchResultFragmeng.Companion.getInstance(search_text);
        }else if( position == 2 ){
            return XmlySearchAlbumFragment.newInstance(search_text,"");
        }else if( position == 3 ){
            return SearchResultSubjectFragment.newInstance(search_text);
        }else if( position == 4 ){
            return XmlySearchTrackFragment.newInstance(search_text);
        }
        return new Fragment();
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