package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.messi.languagehelper.databinding.ActivityBottomTabsBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

public class CollegeActivity extends BaseActivity implements FragmentProgressbarListener {

    private Fragment mCategoryFragment;
    private Fragment mAllCourseFragment;
    private ActivityBottomTabsBinding binding;

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_category:
                hideAllFragment();
                showFragment(mCategoryFragment);
                return true;
            case R.id.navigation_all:
                hideAllFragment();
                showFragment(mAllCourseFragment);
                return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomTabsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initFragment();
    }

    private void initFragment(){
        binding.navigation.inflateMenu(R.menu.college_tabs);
        binding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        binding.navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        mCategoryFragment = CollegeCategoryFragment.getInstance();
        mAllCourseFragment = new BoutiquesFragment.Builder()
                .title(getString(R.string.title_all_fm))
                .build();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mCategoryFragment)
                .add(R.id.content, mAllCourseFragment)
                .commit();
        hideAllFragment();
        showFragment(mCategoryFragment);
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .hide(mCategoryFragment)
                .hide(mAllCourseFragment)
                .commit();
    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }
}
