package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.messi.languagehelper.impl.FragmentProgressbarListener;


public class LearnCodingActivity extends BaseActivity implements FragmentProgressbarListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_to_code);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_learn_to_code));
        Fragment fragment = BoutiquesFragment.getInstance("code");
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contont_layout, fragment)
                    .commit();
        }
    }




}
