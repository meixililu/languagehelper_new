package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.messi.languagehelper.databinding.HelpBinding;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

public class TranStyleActivity extends BaseActivity {

    private SharedPreferences mSharedPreferences;
    private HelpBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HelpBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_style));
        mSharedPreferences = Setings.getSharedPreferences(this);
        binding.styleOne.setOnClickListener(view -> onClick(view));
        binding.styleTwo.setOnClickListener(view -> onClick(view));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            //classic
            case R.id.style_one:
                toNext(true);
                break;
            //minimalist
            case R.id.style_two:
                toNext(false);
                break;
        }
    }

    private void toNext(boolean style){
        Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsUseOldStyle, style);
        ToastUtil.diaplayMesShort(this,this.getString(R.string.restart_success));
        finish();
    }
}
