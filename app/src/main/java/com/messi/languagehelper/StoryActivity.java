package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment mCompositionFragment;
    private Fragment mExaminationFragment;
    private Fragment courseFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(mCompositionFragment).commit();;
                    return true;
                case R.id.navigation_examination:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(mExaminationFragment).commit();;
                    return true;
                case R.id.navigation_course:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(courseFragment).commit();;
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
        navigation.inflateMenu(R.menu.tabs_grammer_story);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mCompositionFragment = SubjectFragment.getInstance(AVOUtil.Category.grammar,"","",getResources().getString(R.string.title_grammar));
        mExaminationFragment = SubjectFragment.getInstance(AVOUtil.Category.story,"","desc",getResources().getString(R.string.title_english_story));
        courseFragment = BoutiquesFragment.getInstance("composition",getString(R.string.selection));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mCompositionFragment)
                .add(R.id.content, mExaminationFragment)
                .add(R.id.content, courseFragment)
                .commit();
        hideAllFragment();
        getSupportFragmentManager()
                .beginTransaction().show(courseFragment).commit();
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .hide(mCompositionFragment)
                .hide(mExaminationFragment)
                .hide(courseFragment)
                .commit();
    }
}
