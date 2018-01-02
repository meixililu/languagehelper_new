package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.XmlySearchAlbumFragment;
import com.messi.languagehelper.XmlySearchRadioFragment;
import com.messi.languagehelper.XmlySearchTrackFragment;

public class XmlySearchResultAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
    private String search_text;

    public XmlySearchResultAdapter(FragmentManager fm, Context mContext, String search_text) {
        super(fm);
        this.search_text = search_text;
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.xmly_album),
        		mContext.getResources().getString(R.string.xmly_track),
        		mContext.getResources().getString(R.string.xmly_radio)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return XmlySearchAlbumFragment.newInstance(search_text);
        }else if( position == 1 ){
            return XmlySearchTrackFragment.newInstance(search_text);
        }else if( position == 2 ){
            return XmlySearchRadioFragment.newInstance(search_text);
        }
        return null;
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