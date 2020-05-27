package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.databinding.SettingBinding;
import com.messi.languagehelper.event.TranAndDicRefreshEvent;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BaseActivity {

    private SettingBinding binding;
    private SharedPreferences mSharedPreferences;
    private String[] paths = {"/zyhy/audio/","/zyhy/img/","/zyhy/apps/","/zyhy/lrc/"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
        initData();
    }

    private void init() {
        getSupportActionBar().setTitle(getString(R.string.title_settings));
        mSharedPreferences = Setings.getSharedPreferences(this);
    }

    private void initData() {
        binding.settingAutoPlayCb.setChecked( mSharedPreferences.getBoolean(KeyUtil.AutoPlayResult, false) );
        binding.settingAutoClearCb.setChecked( mSharedPreferences.getBoolean(KeyUtil.AutoClearInput, true) );
        binding.settingAutoPlay.setOnClickListener(view -> {
            binding.settingAutoPlayCb.setChecked(!binding.settingAutoPlayCb.isChecked());
            Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoPlayResult, binding.settingAutoPlayCb.isChecked());
        });
        binding.settingAutoClear.setOnClickListener(view -> {
            binding.settingAutoClearCb.setChecked(!binding.settingAutoClearCb.isChecked());
            Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoClearInput, binding.settingAutoClearCb.isChecked());
        });
        binding.settingClearAllExceptFavorite.setOnClickListener(view -> {
            BoxHelper.clearExceptFavorite();
            EventBus.getDefault().post(new TranAndDicRefreshEvent());
            ToastUtil.diaplayMesShort(SettingActivity.this, this.getResources().getString(R.string.clear_success));
        });
        binding.settingClearAll.setOnClickListener(view -> {
            BoxHelper.clearAllData();
            EventBus.getDefault().post(new TranAndDicRefreshEvent());
            SDCardUtil.deleteOldFile();
            ToastUtil.diaplayMesShort(SettingActivity.this, this.getResources().getString(R.string.clear_success));
        });
        setCacheSize();
        binding.cacheLayout.setOnClickListener(view -> {
            SDCardUtil.deleteFolderFiles(paths);
            setCacheSize();
        });
    }

    private void setCacheSize(){
        long size = SDCardUtil.getFolderSize(paths);
        binding.cacheTv.setText("清除音频缓存("+SDCardUtil.formetFileSize(size)+")");
    }

}
