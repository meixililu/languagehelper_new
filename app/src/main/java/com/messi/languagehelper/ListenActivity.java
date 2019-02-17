package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListenActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private ListenHomeFragment mWordHomeFragment;
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
                    AVAnalytics.onEvent(ListenActivity.this, "wordstudy_daily");
                    return true;
                case R.id.navigation_word_study:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(dashboardFragment).commit();;
                    AVAnalytics.onEvent(ListenActivity.this, "wordstudy_course");
                    return true;
                case R.id.navigation_vovabulary:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(radioHomeFragment).commit();;
                    AVAnalytics.onEvent(ListenActivity.this, "wordstudy_vocabulary");
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mWordHomeFragment = ListenHomeFragment.getInstance();
        dashboardFragment = ListenCourseFragment.getInstance();
        radioHomeFragment = BroadcastFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mWordHomeFragment)
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
                .hide(radioHomeFragment)
                .hide(mWordHomeFragment)
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(navigation.getSelectedItemId() == R.id.navigation_home){
                if(mWordHomeFragment.getCurrentPosition() > 0){
                    boolean result = mWordHomeFragment.onKeyDown(keyCode,event);
                    if(!result){
                        return super.onKeyDown(keyCode, event);
                    }else {
                        return result;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
