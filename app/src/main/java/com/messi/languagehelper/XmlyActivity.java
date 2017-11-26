package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.banner.Banner;
import com.ximalaya.ting.android.opensdk.model.banner.DiscoveryBannerList;
import com.ximalaya.ting.android.opensdk.model.column.Column;
import com.ximalaya.ting.android.opensdk.model.column.ColumnList;

import java.util.HashMap;
import java.util.Map;

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
        test();
    }

    private void initFragment(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tagsFragment = XimalayaTagsFragment.newInstance(XimalayaUtil.Category_Eng,"");
        ximalayaFragment = XimalayaFragment.newInstance("1","");
        ximalayaFragment1 = XimalayaLiveFragment.newInstance("6","");
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

    private void test(){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CHANNEL ,"");
        map.put(DTransferConstants.APP_VERSION ,"");
        map.put(DTransferConstants.IMAGE_SCALE ,"2");
        CommonRequest.getDiscoveryBannerList(map, new IDataCallBack<DiscoveryBannerList>(){
            @Override
            public void onSuccess(@Nullable DiscoveryBannerList discoveryBannerList) {
                for (Banner banner : discoveryBannerList.getBannerList()){
                    LogUtil.DefalutLog(banner.toString());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        Map<String, String> map1 = new HashMap<String, String>();
        CommonRequest.getColumnList(map1, new IDataCallBack<ColumnList>(){
            @Override
            public void onSuccess(@Nullable ColumnList columnList) {
                for (Column column : columnList.getColumns()){
                    LogUtil.DefalutLog(column.toString());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

}
