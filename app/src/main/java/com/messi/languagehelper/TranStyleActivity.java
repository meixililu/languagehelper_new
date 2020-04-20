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
        String theme = mSharedPreferences.getString(KeyUtil.APPTheme,"blue");
        boolean style = mSharedPreferences.getBoolean(KeyUtil.IsUseOldStyle,true);
        binding.styleOne.setOnClickListener(view -> onClick(view));
        binding.styleTwo.setOnClickListener(view -> onClick(view));
        binding.themeLayout1.setOnClickListener(view -> onClick(view));
        binding.themeLayout2.setOnClickListener(view -> onClick(view));
        binding.themeLayout3.setOnClickListener(view -> onClick(view));
        binding.themeLayout4.setOnClickListener(view -> onClick(view));
        binding.themeLayout5.setOnClickListener(view -> onClick(view));
        binding.themeLayout6.setOnClickListener(view -> onClick(view));
        binding.themeLayout7.setOnClickListener(view -> onClick(view));
        binding.themeLayout8.setOnClickListener(view -> onClick(view));
        binding.themeLayout9.setOnClickListener(view -> onClick(view));
        binding.themeLayout10.setOnClickListener(view -> onClick(view));
        setThemeLayout(theme);
        setStyle(style);
    }

    private void setThemeLayout(String theme){
        Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.APPTheme, theme);
        resetCB();
        if ("blue".equals(theme)) {
            binding.themeCb1.setChecked(true);
        } else if ("green".equals(theme)) {
            binding.themeCb2.setChecked(true);
        } else if ("orange".equals(theme)) {
            binding.themeCb3.setChecked(true);
        } else if ("indigo".equals(theme)) {
            binding.themeCb4.setChecked(true);
        } else if ("red".equals(theme)) {
            binding.themeCb5.setChecked(true);
        } else if ("purple".equals(theme)) {
            binding.themeCb6.setChecked(true);
        } else if ("gray".equals(theme)) {
            binding.themeCb7.setChecked(true);
        } else if ("cyan".equals(theme)) {
            binding.themeCb8.setChecked(true);
        } else if ("teal".equals(theme)) {
            binding.themeCb9.setChecked(true);
        } else if ("pink".equals(theme)) {
            binding.themeCb10.setChecked(true);
        } else {
            binding.themeCb1.setChecked(true);
        }
    }

    private void setStyle(boolean style){
        if (style) {
            binding.styleCb1.setChecked(true);
            binding.styleCb2.setChecked(false);
        } else {
            binding.styleCb1.setChecked(false);
            binding.styleCb2.setChecked(true);
        }
    }

    private void resetCB(){
        binding.themeCb1.setChecked(false);
        binding.themeCb2.setChecked(false);
        binding.themeCb3.setChecked(false);
        binding.themeCb4.setChecked(false);
        binding.themeCb5.setChecked(false);
        binding.themeCb6.setChecked(false);
        binding.themeCb7.setChecked(false);
        binding.themeCb8.setChecked(false);
        binding.themeCb9.setChecked(false);
        binding.themeCb10.setChecked(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.style_one:
                toNext(true);
                break;
            case R.id.style_two:
                toNext(false);
                break;
            case R.id.theme_layout_1:
                setThemeLayout("blue");
                break;
            case R.id.theme_layout_2:
                setThemeLayout("green");
                break;
            case R.id.theme_layout_3:
                setThemeLayout("orange");
                break;
            case R.id.theme_layout_4:
                setThemeLayout("indigo");
                break;
            case R.id.theme_layout_5:
                setThemeLayout("red");
                break;
            case R.id.theme_layout_6:
                setThemeLayout("purple");
                break;
            case R.id.theme_layout_7:
                setThemeLayout("gray");
                break;
            case R.id.theme_layout_8:
                setThemeLayout("cyan");
                break;
            case R.id.theme_layout_9:
                setThemeLayout("teal");
                break;
            case R.id.theme_layout_10:
                setThemeLayout("pink");
                break;

        }
    }

    private void toNext(boolean style){
        if (style) {
            binding.styleCb1.setChecked(true);
            binding.styleCb2.setChecked(false);
        } else {
            binding.styleCb1.setChecked(false);
            binding.styleCb2.setChecked(true);
        }
        Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsUseOldStyle, style);
        ToastUtil.diaplayMesShort(this,this.getString(R.string.restart_success));
    }
}
