package com.messi.languagehelper.adapter;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messi.languagehelper.NewsFragment;
import com.messi.languagehelper.ToutiaoNewsFragment;
import com.messi.languagehelper.WechatJXFragment;

public class ReadingAndNewsAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
    private Activity mContext;

    public ReadingAndNewsAdapter(FragmentManager fm, Activity mContext) {
        super(fm);
        this.mContext = mContext;
        CONTENT = new String[] { 
        		"精选",
        		"社会",
        		"体育",
        		"科技",
        		"国内",
        		"娱乐",
        		"头条",
        		"国际",
        		"生活"
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return WechatJXFragment.getInstance(mContext);
        }else if( position == 1 ){
        	return NewsFragment.getInstance(NewsFragment.social, CONTENT[position], mContext);
        }else if( position == 2 ){
            return NewsFragment.getInstance(NewsFragment.tiyu, CONTENT[position], mContext);
        }else if( position == 3 ){
            return NewsFragment.getInstance(NewsFragment.keji, CONTENT[position], mContext);
        }else if( position == 4 ){
            return NewsFragment.getInstance(NewsFragment.guonei, CONTENT[position], mContext);
        }else if( position == 5 ){
            return NewsFragment.getInstance(NewsFragment.yule, CONTENT[position], mContext);
        }else if( position == 6 ){
        	return ToutiaoNewsFragment.getInstance("top",mContext);
        }else if( position == 7 ){
            return NewsFragment.getInstance(NewsFragment.guoji, CONTENT[position], mContext);
        }else if( position == 8 ){
            return NewsFragment.getInstance(NewsFragment.health, CONTENT[position], mContext);
        }else if( position == 9 ){
            return NewsFragment.getInstance(NewsFragment.social, CONTENT[position], mContext);
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