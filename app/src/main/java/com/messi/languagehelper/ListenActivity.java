package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListenActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment mWordHomeFragment;
    private Fragment dashboardFragment;
    private Fragment jtFragment;
    private Fragment boutiquesFragment;

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
                case R.id.navigation_jingting:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(jtFragment).commit();;
                    AVAnalytics.onEvent(ListenActivity.this, "wordstudy_daily");
                    return true;
                case R.id.navigation_video:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(boutiquesFragment).commit();;
                    AVAnalytics.onEvent(ListenActivity.this, "wordstudy_daily");
                    return true;
                case R.id.navigation_word_study:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(dashboardFragment).commit();;
                    AVAnalytics.onEvent(ListenActivity.this, "wordstudy_course");
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tabs);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment(){
        navigation.inflateMenu(R.menu.listen_tabs);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mWordHomeFragment = new ReadingFragment.Builder()
                .title(getString(R.string.title_listening))
                .category(AVOUtil.Category.listening)
                .isPlayList(true)
                .build();
        jtFragment = XmlySearchAlbumFragment.newInstance("听力",getString(R.string.title_study_category));
        boutiquesFragment = BoutiquesFragment.getInstance("listening",getString(R.string.title_course));
        dashboardFragment = ListenCourseFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mWordHomeFragment)
                .add(R.id.content, dashboardFragment)
                .add(R.id.content, jtFragment)
                .add(R.id.content, boutiquesFragment)
                .commit();
        hideAllFragment();
        getSupportFragmentManager()
                .beginTransaction().show(mWordHomeFragment).commit();
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .hide(dashboardFragment)
                .hide(mWordHomeFragment)
                .hide(jtFragment)
                .hide(boutiquesFragment)
                .commit();
    }

}
