package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.messi.languagehelper.util.XimalayaUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XmlyActivity extends AppCompatActivity {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Fragment tagsFragment;
    private Fragment ximalayaFragment;
    private Fragment ximalayaFragment1;
    private Fragment ximalayaFragment2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(tagsFragment).commit();;
                    return true;
                case R.id.navigation_dashboard:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(ximalayaFragment).commit();;
                    return true;
                case R.id.navigation_face:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(ximalayaFragment1).commit();;
                    return true;
                case R.id.navigation_history:
                    hideAllFragment();
                    getSupportFragmentManager().beginTransaction().show(ximalayaFragment2).commit();;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmly);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tagsFragment = XimalayaTagsFragment.newInstance(XimalayaUtil.Category_Eng,"");
        ximalayaFragment = XimalayaDashboardFragment.newInstance();
        ximalayaFragment1 = XmlyTagRecommendFragment.newInstance(XimalayaUtil.Category_Eng,null);
        ximalayaFragment2 = XimalayaFragment.newInstance("39","");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content,tagsFragment)
                .add(R.id.content,ximalayaFragment)
                .add(R.id.content,ximalayaFragment1)
                .add(R.id.content,ximalayaFragment2)
                .commit();
        hideAllFragment();
        getSupportFragmentManager()
                .beginTransaction().show(tagsFragment).commit();
    }

    private void hideAllFragment(){
        getSupportFragmentManager()
                .beginTransaction().hide(ximalayaFragment)
                .hide(ximalayaFragment1)
                .hide(ximalayaFragment2)
                .hide(tagsFragment)
                .commit();
    }

}
