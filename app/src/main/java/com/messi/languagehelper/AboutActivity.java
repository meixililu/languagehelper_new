package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.messi.languagehelper.databinding.AboutBinding;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

public class AboutActivity extends BaseActivity {

    private int clickTime;
    public AboutBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AboutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setActionBarTitle(getResources().getString(R.string.title_about));
        binding.appVersion.setText(Setings.getVersionName(this));
        binding.emailLayout.setOnClickListener(view -> onEmailClick());
        binding.imgLogo.setOnClickListener(view -> onImgClick());
    }

    public void onEmailClick() {
        Setings.contantUs(AboutActivity.this);
        AVAnalytics.onEvent(this, "about_pg_send_email");
    }

    public void onImgClick() {
        clickTime++;
        if(clickTime > 2){
            ToastUtil.diaplayMesShort(this,"ok");
            Setings.saveSharedPreferences(Setings.getSharedPreferences(this), KeyUtil.ShowCNK,1);
        }
    }
}
