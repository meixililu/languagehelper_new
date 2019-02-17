package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity {


    @BindView(R.id.style_one)
    RelativeLayout styleOne;
    @BindView(R.id.style_two)
    RelativeLayout styleTwo;
    private SharedPreferences mSharedPreferences;
    private boolean isFirstLoad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mSharedPreferences = Setings.getSharedPreferences(this);
        isFirstLoad = getIntent().getBooleanExtra(KeyUtil.IsFirstLoadStylePage,false);
    }


    @OnClick({R.id.style_one, R.id.style_two})
    public void onClick(View view) {
        switch (view.getId()) {
            //classic
            case R.id.style_one:
                Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsUseOldStyle, true);
                AVAnalytics.onEvent(this, "style_select_classic");
                toNext();
                break;
            //minimalist
            case R.id.style_two:
                Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsUseOldStyle, false);
                AVAnalytics.onEvent(this, "style_select_minimalist");
                toNext();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        toNext();
    }

    private void toNext(){
        if(isFirstLoad){
            toActivity(WXEntryActivity.class, null);
        }else {
            ToastUtil.diaplayMesShort(this,this.getString(R.string.restart_success));
        }
        finish();
    }
}
