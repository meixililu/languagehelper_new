package com.messi.languagehelper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;

public class EmptyActivity extends BaseActivity implements FragmentProgressbarListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_fragment);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
        String style = bundle.getString(KeyUtil.StyleKey);
        if("black".equals(style)){
            setStatusbarColor(R.color.black);
            if(toolbar != null){
                toolbar.setBackgroundColor(Color.BLACK);
            }
        }
        Class fName = (Class)bundle.getSerializable(KeyUtil.FragmentName);
        try {
            Fragment fragment = (Fragment) fName.newInstance();
            if(fragment != null){
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.contont_layout, fragment)
                        .commit();
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.DefalutLog("fragment not found.");
            finish();
        }
    }

}
