package com.messi.languagehelper.adapter;

import java.util.List;

import cn.leancloud.AVObject;
import com.messi.languagehelper.ReadingFragment;
import com.messi.languagehelper.util.AVOUtil;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ReadingJuheListAdapter extends FragmentPagerAdapter {

	private List<AVObject> avObjects;
	
    public ReadingJuheListAdapter(FragmentManager fm,Context mContext,List<AVObject> avObjects) {
        super(fm);
        this.avObjects = avObjects;
    }

    @Override
    public Fragment getItem(int position) {
    	String category_id = avObjects.get(position).getString(AVOUtil.Category.category_id);
    	return ReadingFragment.newInstance(category_id, null);
    }

    @Override
    public int getCount() {
        return avObjects.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return avObjects.get(position).getString(AVOUtil.Category.name);
    }
}