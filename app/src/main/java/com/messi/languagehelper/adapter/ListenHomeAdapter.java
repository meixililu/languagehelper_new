package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.WebViewFragment;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.Settings;

public class ListenHomeAdapter extends FragmentPagerAdapter {

    public static String[] CONTENT;
    private Context mContext;
    private WebViewFragment mWebViewFragment1;
    private WebViewFragment mWebViewFragment2;

    public ListenHomeAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        CONTENT = new String[] {
                mContext.getResources().getString(R.string.title_listening),
                mContext.getResources().getString(R.string.title_web_npr),
                mContext.getResources().getString(R.string.title_web_AmericanLife)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return ReadingFragment.newInstance(AVOUtil.Category.listening,"");
        }else if( position == 1 ){
            mWebViewFragment1 = WebViewFragment.getInstance(Settings.NPR_Url);
            return mWebViewFragment1;
        }else if( position == 2 ){
            mWebViewFragment2 = WebViewFragment.getInstance(Settings.AmericanLife_Url);
            return mWebViewFragment2;
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

    public WebViewFragment getWebViewFragment(int position){
        if(position == 1){
            return mWebViewFragment1;
        }else {
            return mWebViewFragment2;
        }
    }

}