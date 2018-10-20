package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.seekbar_text)
    TextView seekbarText;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.setting_auto_play_cb)
    CheckBox settingAutoPlayCb;
    @BindView(R.id.setting_auto_play)
    FrameLayout settingAutoPlay;
    @BindView(R.id.setting_auto_clear_cb)
    CheckBox settingAutoClearCb;
    @BindView(R.id.setting_auto_clear)
    FrameLayout settingAutoClear;
    @BindView(R.id.setting_clear_all_except_favorite)
    FrameLayout settingClearAllExceptFavorite;
    @BindView(R.id.setting_clear_all)
    FrameLayout settingClearAll;

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);
        init();
        initData();
    }

    private void init() {
        getSupportActionBar().setTitle(getString(R.string.title_settings));
        mSharedPreferences = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        seekbarText = (TextView) findViewById(R.id.seekbar_text);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        seekbarText.setText(this.getResources().getString(R.string.play_speed_text) +
                mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50));
        seekbar.setProgress(mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50));
        settingAutoPlayCb.setChecked( mSharedPreferences.getBoolean(KeyUtil.AutoPlayResult, false) );
        settingAutoClearCb.setChecked( mSharedPreferences.getBoolean(KeyUtil.AutoClearInput, true) );
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekbarText.setText(this.getResources().getString(R.string.play_speed_text) + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Setings.saveSharedPreferences(mSharedPreferences,
                getString(R.string.preference_key_tts_speed),
                seekBar.getProgress());
        AVAnalytics.onEvent(SettingActivity.this, "tran_tools_change_speed");
    }

    @OnClick({R.id.setting_auto_play, R.id.setting_auto_clear, R.id.setting_clear_all_except_favorite, R.id.setting_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_auto_play:
                settingAutoPlayCb.setChecked(!settingAutoPlayCb.isChecked());
                Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoPlayResult, settingAutoPlayCb.isChecked());
                AVAnalytics.onEvent(this, "setting_pg_auto_play");
                break;
            case R.id.setting_clear_all_except_favorite:
                Setings.isMainFragmentNeedRefresh = true;
                Setings.isDictionaryFragmentNeedRefresh = true;
                DataBaseUtil.getInstance().clearExceptFavorite();
                ToastUtil.diaplayMesShort(SettingActivity.this, this.getResources().getString(R.string.clear_success));
                AVAnalytics.onEvent(this, "setting_pg_clear_all_except");
                break;
            case R.id.setting_clear_all:
                DataBaseUtil.getInstance().clearAllData();
                Setings.isMainFragmentNeedRefresh = true;
                Setings.isDictionaryFragmentNeedRefresh = true;
                SDCardUtil.deleteOldFile();
                ToastUtil.diaplayMesShort(SettingActivity.this, this.getResources().getString(R.string.clear_success));
                AVAnalytics.onEvent(this, "setting_pg_clear_all");
                break;
            case R.id.setting_auto_clear:
                settingAutoClearCb.setChecked(!settingAutoClearCb.isChecked());
                Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoClearInput, settingAutoClearCb.isChecked());
                AVAnalytics.onEvent(this, "setting_pg_auto_clear_input");
                break;
        }
    }
}
