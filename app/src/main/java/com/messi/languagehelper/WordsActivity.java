package com.messi.languagehelper;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordsActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment xmlyFragment;
    private Fragment studyFragment;
    private Fragment radioHomeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_word_study:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(xmlyFragment).commit();;
                    AVAnalytics.onEvent(WordsActivity.this, "wordstudy_course");
                    return true;
                case R.id.navigation_word_course:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(studyFragment).commit();;
                    AVAnalytics.onEvent(WordsActivity.this, "wordstudy_course");
                    return true;
                case R.id.navigation_vovabulary:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(radioHomeFragment).commit();;
                    AVAnalytics.onEvent(WordsActivity.this, "wordstudy_vocabulary");
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment(){
        navigation.inflateMenu(R.menu.words_tabs);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        xmlyFragment = new BoutiquesFragment.Builder()
                .category("word")
                .title(getString(R.string.selection))
                .build();
        studyFragment = XmlySearchAlbumFragment.newInstance("单词",getString(R.string.title_study_category));
        radioHomeFragment = SubjectFragment.getInstance(AVOUtil.Category.word,"","",getString(R.string.title_course));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, xmlyFragment)
                .add(R.id.content, studyFragment)
                .add(R.id.content, radioHomeFragment)
                .commit();
        hideAllFragment();
        getSupportFragmentManager()
                .beginTransaction().show(xmlyFragment).commit();
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .hide(xmlyFragment)
                .hide(studyFragment)
                .hide(radioHomeFragment)
                .commit();
    }

}
