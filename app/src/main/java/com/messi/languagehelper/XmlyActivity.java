package com.messi.languagehelper;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XmlyActivity extends BaseActivity {

    @BindView(R.id.content)
    FrameLayout content;
//    @BindView(R.id.navigation)
//    BottomNavigationView navigation;
//    private Fragment engFragment;
    private Fragment dashboardFragment;
//    private Fragment radioHomeFragment;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    hideAllFragment();
//                    getSupportFragmentManager().beginTransaction().show(engFragment).commit();;
//                    return true;
//                case R.id.navigation_dashboard:
//                    hideAllFragment();
//                    getSupportFragmentManager().beginTransaction().show(dashboardFragment).commit();;
//                    return true;
////                case R.id.navigation_history:
////                    hideAllFragment();
////                    getSupportFragmentManager().beginTransaction().show(radioHomeFragment).commit();;
////                    return true;
//            }
//            return false;
//        }
//
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmly);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment(){
        dashboardFragment = XimalayaDashboardFragment.newInstance();
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        engFragment = XimalayaEngFragment.newInstance();
//        radioHomeFragment = XmlyTagRecommendFragment.newInstance(XimalayaUtil.Category_english,null);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, dashboardFragment)
//                .add(R.id.content, engFragment)
//                .add(R.id.content, radioHomeFragment)
                .commit();
//        hideAllFragment();
//        getSupportFragmentManager()
//                .beginTransaction().show(engFragment).commit();
    }

//    private void hideAllFragment(){
//        getSupportFragmentManager()
//                .beginTransaction()
//                .hide(dashboardFragment)
////                .hide(radioHomeFragment)
//                .hide(engFragment)
//                .commit();
//    }



}
