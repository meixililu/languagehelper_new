package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.XimalayaFragment;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

import java.util.List;

public class XimalayaEnglishAdapter extends FragmentPagerAdapter {

    private List<Tag> tagList;
    private Context mContext;

    public XimalayaEnglishAdapter(FragmentManager fm, Context mContext, List<Tag> tagList) {
        super(fm);
        this.mContext = mContext;
        this.tagList = tagList;

    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return XimalayaFragment.newInstance(XimalayaUtil.Category_Eng, "");
        }else {
            return XimalayaFragment.newInstance(XimalayaUtil.Category_Eng,
                    tagList.get(position-1).getTagName());
        }
    }

    @Override
    public int getCount() {
        return tagList.size() + 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "热门";
        }else {
            return tagList.get(position-1).getTagName().trim();
        }
    }
}