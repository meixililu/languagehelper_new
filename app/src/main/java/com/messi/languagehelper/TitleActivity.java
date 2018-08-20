package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Settings;

import butterknife.ButterKnife;

public class TitleActivity extends BaseActivity {

    private SharedPreferences mSharedPreferences;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_fragment);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mSharedPreferences = Settings.getSharedPreferences(this);
        Fragment fragment = null;
        type = getIntent().getIntExtra(KeyUtil.Type,0);
        if(type > 0){
            if(type == R.string.translate){
                if(mSharedPreferences.getBoolean(KeyUtil.IsUseOldStyle,true)){
                    fragment = MainFragmentOld.getInstance(null);
                }else {
                    fragment = MainFragment.getInstance(null);
                }
            }
        }
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contont_layout, fragment)
                    .commit();
        }
    }


}
