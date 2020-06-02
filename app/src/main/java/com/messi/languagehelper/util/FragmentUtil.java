package com.messi.languagehelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.messi.languagehelper.AiDialogueCourseForYYSFragment;
import com.messi.languagehelper.LeisureFragment;
import com.messi.languagehelper.MainFragment;
import com.messi.languagehelper.MainFragmentOld;
import com.messi.languagehelper.MainFragmentYWCD;
import com.messi.languagehelper.MainFragmentYYS;
import com.messi.languagehelper.ReadingFragmentYWCD;
import com.messi.languagehelper.StudyCategoryFragment;
import com.messi.languagehelper.StudyFragment;
import com.messi.languagehelper.StudyTabFragment;
import com.messi.languagehelper.XmlyMainForYWCDFragment;
import com.messi.languagehelper.XmlyMainForYYSFragment;
import com.messi.languagehelper.YYJHomeFragment;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtil {

    public static List<Fragment> getMainPageFragments(Context mContext,
                                                      FragmentProgressbarListener listener){
        SharedPreferences sp = Setings.getSharedPreferences(mContext);
        List<Fragment> fragments = new ArrayList<>();
        if(mContext.getPackageName().equals(Setings.application_id_yyj) ||
                mContext.getPackageName().equals(Setings.application_id_yyj_google)){
            fragments.add(StudyFragment.getInstance());
            fragments.add(StudyCategoryFragment.getInstance());
            fragments.add(YYJHomeFragment.getInstance());
            fragments.add(LeisureFragment.getInstance());
        } else if(mContext.getPackageName().equals(Setings.application_id_yys) ||
                mContext.getPackageName().equals(Setings.application_id_yys_google)){
            fragments.add(MainFragmentYYS.getInstance(listener));
            fragments.add(AiDialogueCourseForYYSFragment.getInstance());
            fragments.add(XmlyMainForYYSFragment.newInstance());
            fragments.add(LeisureFragment.getInstance());
        } else if(mContext.getPackageName().equals(Setings.application_id_ywcd)){
            fragments.add(MainFragmentYWCD.getInstance(listener));
            fragments.add(ReadingFragmentYWCD.newInstance(500));
            fragments.add(XmlyMainForYWCDFragment.newInstance());
            fragments.add(LeisureFragment.getInstance());
        } else {
            if(sp.getBoolean(KeyUtil.IsUseOldStyle,true)){
                fragments.add(MainFragmentOld.getInstance(listener));
            }else {
                fragments.add(MainFragment.getInstance(listener));
            }
            fragments.add(StudyCategoryFragment.getInstance());
            fragments.add(StudyTabFragment.getInstance());
            fragments.add(LeisureFragment.getInstance());
        }
        return fragments;
    }

    public static List<Fragment> getFragmentsFromInstanceState(Bundle inState,FragmentManager fm){
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            Fragment fragment = fm.getFragment(inState,"fragment"+i);
            if (fragment != null) {
                fragments.add(fragment);
            }
        }
        return fragments;
    }

    public static void saveFragmentsToInstanceState(Bundle outState,
                                                    FragmentManager fm,
                                                    List<Fragment> list){
        if(NullUtil.isNotEmpty(list)){
            for (int i = 0; i < list.size(); i++){
                fm.putFragment(outState,"fragment"+i,list.get(i));
            }
        }
    }
}
