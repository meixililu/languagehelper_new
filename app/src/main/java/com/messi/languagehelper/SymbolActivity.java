package com.messi.languagehelper;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SymbolActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment mWordHomeFragment;
    private Fragment practiceFragment;
    private Fragment dashboardFragment;
    private Fragment shadowFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(mWordHomeFragment).commit();;
                    AVAnalytics.onEvent(SymbolActivity.this, "spoken_home");
                    return true;
                case R.id.navigation_shadow:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(shadowFragment).commit();;
                    AVAnalytics.onEvent(SymbolActivity.this, "spoken_shadow");
                    return true;
                case R.id.navigation_practice:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(practiceFragment).commit();;
                    AVAnalytics.onEvent(SymbolActivity.this, "spoken_practice");
                    return true;
                case R.id.navigation_word_study:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(dashboardFragment).commit();;
                    AVAnalytics.onEvent(SymbolActivity.this, "spoken_course");
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
        navigation.inflateMenu(R.menu.tabs_symbol);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mWordHomeFragment = new BoutiquesFragment.Builder()
                .category("symbol")
                .title(getString(R.string.selection))
                .build();
        shadowFragment = SymbolListFragment.getInstance();
        practiceFragment = XmlySearchAlbumFragment.newInstance("音标",getString(R.string.title_study_category));
        dashboardFragment = SubjectFragment.getInstance(AVOUtil.Category.symbol,"","",getString(R.string.title_course));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mWordHomeFragment)
                .add(R.id.content, practiceFragment)
                .add(R.id.content, dashboardFragment)
                .add(R.id.content, shadowFragment)
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
                .hide(mWordHomeFragment)
                .hide(shadowFragment)
                .commit();
    }
}
