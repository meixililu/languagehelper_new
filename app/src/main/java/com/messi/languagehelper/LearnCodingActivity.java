package com.messi.languagehelper;

import android.os.Bundle;


public class LearnCodingActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_dictionary);
        setStatusbarColor(R.color.style6_color1);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_learn_to_code));
    }




}
