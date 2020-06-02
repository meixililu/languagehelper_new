package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleFragment extends BaseFragment {


    @BindView(R.id.progressBarCircularIndetermininate)
    ProgressBar progressBarCircularIndetermininate;
    @BindView(R.id.my_awesome_toolbar)
    Toolbar myAwesomeToolbar;
    @BindView(R.id.contont_layout)
    LinearLayout contontLayout;

    Fragment contentFragment;
    int title;

    public static TitleFragment newInstance(Fragment mFragment,int title){
        TitleFragment fragment = new TitleFragment();
        LogUtil.DefalutLog("newInstance:"+title);
        fragment.contentFragment = mFragment;
        fragment.title = title;
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.title_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init(){
        if(title > 0 && myAwesomeToolbar != null){
            myAwesomeToolbar.setTitle(title);
        }
        if(contentFragment != null){
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.contont_layout, contentFragment)
                    .commit();
        }
    }


}
