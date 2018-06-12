package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.XimalayaRadioHomeFragment;
import com.messi.languagehelper.XmlyCategoryFragment;
import com.messi.languagehelper.XmlyCategoryRecommendFragment;
import com.messi.languagehelper.XmlySearchAlbumForYYSFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

public class XmlyMainForYYSAdapter extends FragmentPagerAdapter {

    private FragmentProgressbarListener listener;
    private Context mContext;

    public XmlyMainForYYSAdapter(FragmentManager fm,Context mContext,FragmentProgressbarListener listener) {
        super(fm);
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return XmlyCategoryFragment.getInstance();
        }else if(position == 1) {
            return XmlySearchAlbumForYYSFragment.newInstance("粤语");
        }else if(position == 2) {
            return XmlyCategoryRecommendFragment.newInstance("","",listener);
        }else {
            return XimalayaRadioHomeFragment.newInstance(listener);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return mContext.getResources().getString(R.string.title_category);
        }else if(position == 1) {
            return mContext.getResources().getString(R.string.title_cantonese);
        }else if(position == 2) {
            return mContext.getResources().getString(R.string.recommend);
        }else {
            return mContext.getResources().getString(R.string.title_broadcast);
        }
    }
}