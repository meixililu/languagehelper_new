package com.messi.languagehelper.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messi.languagehelper.XimalayaTagsFragment;
import com.messi.languagehelper.XmlyTagRecommendFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.XimalayaUtil;

public class XmlyEngAdapter extends FragmentPagerAdapter {

    private FragmentProgressbarListener listener;

    public XmlyEngAdapter(FragmentManager fm, FragmentProgressbarListener listener) {
        super(fm);
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_english,"",listener);
        }else {
            return XmlyTagRecommendFragment.newInstance(XimalayaUtil.Category_english,listener);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "分类";
        }else {
            return "推荐";
        }
    }
}