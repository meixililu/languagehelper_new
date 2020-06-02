package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messi.languagehelper.R;
import com.messi.languagehelper.StudyFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.FragmentUtil;
import com.messi.languagehelper.util.Setings;

import java.util.List;

public class MainPageAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	private Context mContext;
    private FragmentProgressbarListener listener;
    private List<Fragment> fragments;

    public MainPageAdapter(FragmentManager fm, Context mContext,FragmentProgressbarListener listener) {
        super(fm);
        this.mContext = mContext.getApplicationContext();
        this.fragments = FragmentUtil.getMainPageFragments(mContext,listener);
        this.listener = listener;
        if(mContext.getPackageName().equals(Setings.application_id_yyj) ||
                mContext.getPackageName().equals(Setings.application_id_yyj_google)){
            CONTENT = new String[] {
                    mContext.getResources().getString(R.string.title_home_tab),
                    mContext.getResources().getString(R.string.title_study_category),
                    mContext.getResources().getString(R.string.title_translate),
                    mContext.getResources().getString(R.string.title_leisure)
            };
        }else if(mContext.getPackageName().equals(Setings.application_id_yys) ||
                mContext.getPackageName().equals(Setings.application_id_yys_google)){
            CONTENT = new String[] {
                    mContext.getResources().getString(R.string.title_home_tab),
                    mContext.getResources().getString(R.string.title_TranslatePractice),
                    mContext.getResources().getString(R.string.title_listen_fm),
                    mContext.getResources().getString(R.string.title_leisure)
            };
        }else if(mContext.getPackageName().equals(Setings.application_id_ywcd)){
            CONTENT = new String[] {
                    mContext.getResources().getString(R.string.title_home_tab),
                    mContext.getResources().getString(R.string.title_study_category),
                    mContext.getResources().getString(R.string.title_listen_fm),
                    mContext.getResources().getString(R.string.title_leisure)
            };
        }else {
            CONTENT = new String[] {
                    mContext.getResources().getString(R.string.title_home_tab),
                    mContext.getResources().getString(R.string.title_study_category),
                    mContext.getResources().getString(R.string.title_study),
                    mContext.getResources().getString(R.string.title_leisure)
            };
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null) {
            fragments = FragmentUtil.getMainPageFragments(mContext,listener);
        }
        return fragments.get(position);
    }

    public void onTabReselected(int index){
        Fragment fragment = fragments.get(index);
        if (fragment instanceof StudyFragment) {
            ((StudyFragment)fragment).onTabReselected(index);
        }
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}