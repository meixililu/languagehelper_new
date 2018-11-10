package com.messi.languagehelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.languagehelper.XimalayaRadioHomeFragment;
import com.messi.languagehelper.XimalayaTagsFragment;
import com.messi.languagehelper.XmlyCategoryFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.XimalayaUtil;

public class XmlyAllAdapter extends FragmentPagerAdapter {

    private FragmentProgressbarListener listener;

    public XmlyAllAdapter(FragmentManager fm,FragmentProgressbarListener listener) {
        super(fm);
        this.listener = listener;
    }

//    return XmlyCategoryRecommendFragment.newInstance("","",listener);
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return XmlyCategoryFragment.getInstance();
        }else if(position == 1) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_english,"",listener);
        }else if(position == 2) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_novel,"",listener);
        }else if(position == 3) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_yule,"",listener);
        }else if(position == 4) {
            return XimalayaRadioHomeFragment.newInstance(listener);
        }else if(position == 5) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_music,"",listener);
        }else if(position == 6) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_qinggan,"",listener);
        }else if(position == 7) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_xiangsheng,"",listener);
        }else if(position == 8) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_jiaoyu,"",listener);
        }else if(position == 9) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_history,"",listener);
        }else if(position == 10) {
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_gongkaike,"",listener);
        }else{
            return XimalayaTagsFragment.newInstance(XimalayaUtil.Category_english,"",listener);
        }
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "分类";
        }else if(position == 1) {
            return "英语";
        }else if(position == 2) {
            return "小说";
        }else if(position == 3) {
            return "娱乐";
        }else if(position == 4) {
            return "广播";
        }else if(position == 5) {
            return "音乐";
        }else if(position == 6) {
            return "情感";
        }else if(position == 7) {
            return "相声";
        }else if(position == 8) {
            return "教育";
        }else if(position == 9) {
            return "历史";
        }else if(position == 10) {
            return "公开课";
        }else {
            return "其他";
        }

    }
}