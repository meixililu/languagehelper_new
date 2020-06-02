package com.messi.languagehelper;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.messi.languagehelper.databinding.ActivityBottomTabsBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

public class ComExamActivity extends BaseActivity implements FragmentProgressbarListener {

    private Fragment mCompositionFragment;
    private Fragment mExaminationFragment;
    private Fragment courseFragment;
    private ActivityBottomTabsBinding binding;

    public boolean onNavigationItemSelected(MenuItem item) {
                hideAllFragment();
        switch (item.getItemId()) {
            case R.id.navigation_home:
                showFragment(mCompositionFragment);
                return true;
            case R.id.navigation_examination:
                showFragment(mExaminationFragment);;
                return true;
            case R.id.navigation_course:
                showFragment(courseFragment);;
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
        binding.navigation.inflateMenu(R.menu.com_exam_tabs);
        binding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        binding.navigation.setOnNavigationItemSelectedListener((item) -> onNavigationItemSelected(item));
        mCompositionFragment = CompositionFragment.getInstance();
        mExaminationFragment = ExaminationFragment.getInstance();
        courseFragment = new BoutiquesFragment.Builder()
                .category("examination")
                .title(getString(R.string.selection))
                .build();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mCompositionFragment)
                .add(R.id.content, mExaminationFragment)
                .add(R.id.content, courseFragment)
                .commit();
        hideAllFragment();
        showFragment(courseFragment);
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .hide(mCompositionFragment)
                .hide(mExaminationFragment)
                .hide(courseFragment)
                .commit();
    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction().show(fragment).commit();
    }

}
