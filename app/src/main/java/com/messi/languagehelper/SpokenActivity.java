package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.views.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpokenActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment mWordHomeFragment;
    private Fragment practiceFragment;
    private Fragment dashboardFragment;
    private Fragment radioHomeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(mWordHomeFragment).commit();;
                    AVAnalytics.onEvent(SpokenActivity.this, "spoken_home");
                    return true;
                case R.id.navigation_practice:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(practiceFragment).commit();;
                    AVAnalytics.onEvent(SpokenActivity.this, "spoken_practice");
                    return true;
                case R.id.navigation_word_study:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(dashboardFragment).commit();;
                    AVAnalytics.onEvent(SpokenActivity.this, "spoken_course");
                    return true;
                case R.id.navigation_vovabulary:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(radioHomeFragment).commit();;
                    AVAnalytics.onEvent(SpokenActivity.this, "spoken_bussiness");
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoken);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment(){
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mWordHomeFragment = SpokenHomeFragment.getInstance();
        practiceFragment = AiDialogueCourseFragment.getInstance();
        dashboardFragment = SpokenCourseFragment.getInstance();
        radioHomeFragment = BusinessFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mWordHomeFragment)
                .add(R.id.content, practiceFragment)
                .add(R.id.content, dashboardFragment)
                .add(R.id.content, radioHomeFragment)
                .commit();
        hideAllFragment();
        getSupportFragmentManager()
                .beginTransaction().show(mWordHomeFragment).commit();
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .hide(dashboardFragment)
                .hide(practiceFragment)
                .hide(radioHomeFragment)
                .hide(mWordHomeFragment)
                .commit();
    }



}
