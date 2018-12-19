package com.messi.languagehelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.util.ImgUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;

public class ImgShareActivity extends BaseActivity implements OnClickListener {

    protected SoundPool mSoundPoll;
    private FrameLayout material_color_1, material_color_2, material_color_3, material_color_4, material_color_5;
    private FrameLayout text_color_1, text_color_2, text_color_3, text_color_4, text_color_5;
    private FrameLayout style_1, style_2, style_3, style_4, style_5;
    private SeekBar seekbar;
    private EditText share_content;
    private FrameLayout share_btn_cover;
    private ScrollView parent_layout;
    private String shareContent;
    private int mSoundId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.share_layout);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Intent intent = getIntent();
        shareContent = intent.getStringExtra(KeyUtil.ShareContentKey);
        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_share_preview));
        }

        material_color_1 = (FrameLayout) findViewById(R.id.material_color_1);
        material_color_2 = (FrameLayout) findViewById(R.id.material_color_2);
        material_color_3 = (FrameLayout) findViewById(R.id.material_color_3);
        material_color_4 = (FrameLayout) findViewById(R.id.material_color_4);
        material_color_5 = (FrameLayout) findViewById(R.id.material_color_5);
        text_color_1 = (FrameLayout) findViewById(R.id.text_color_1);
        text_color_2 = (FrameLayout) findViewById(R.id.text_color_2);
        text_color_3 = (FrameLayout) findViewById(R.id.text_color_3);
        text_color_4 = (FrameLayout) findViewById(R.id.text_color_4);
        text_color_5 = (FrameLayout) findViewById(R.id.text_color_5);
        style_1 = (FrameLayout) findViewById(R.id.style_1);
        style_2 = (FrameLayout) findViewById(R.id.style_2);
        style_3 = (FrameLayout) findViewById(R.id.style_3);
        style_4 = (FrameLayout) findViewById(R.id.style_4);
        style_5 = (FrameLayout) findViewById(R.id.style_5);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        parent_layout = (ScrollView) findViewById(R.id.parent_layout);
        share_content = (EditText) findViewById(R.id.share_content);
        share_btn_cover = (FrameLayout) findViewById(R.id.share_btn_cover);
        mSoundPoll = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPoll.load(this, R.raw.camera, 1);

        if (!TextUtils.isEmpty(shareContent)) {
            share_content.setText(shareContent);
        }
        share_btn_cover.setOnClickListener(this);
        material_color_1.setOnClickListener(this);
        material_color_2.setOnClickListener(this);
        material_color_3.setOnClickListener(this);
        material_color_4.setOnClickListener(this);
        material_color_5.setOnClickListener(this);
        text_color_1.setOnClickListener(this);
        text_color_2.setOnClickListener(this);
        text_color_3.setOnClickListener(this);
        text_color_4.setOnClickListener(this);
        text_color_5.setOnClickListener(this);
        style_1.setOnClickListener(this);
        style_2.setOnClickListener(this);
        style_3.setOnClickListener(this);
        style_4.setOnClickListener(this);
        style_5.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.DefalutLog("progress:" + progress);
                share_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, (progress + 10));
            }
        });
    }

    private void shareWithImg(){
        int h = 0;
        Bitmap bitmap;
        for(int i = 0; i < parent_layout.getChildCount(); i++){
            h += parent_layout.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(SystemUtil.SCREEN_WIDTH, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getResources().getColor(R.color.white));
        parent_layout.draw(canvas);
        ImgUtil.addToBitmap(this,bitmap,R.drawable.qr_zyhy);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn_cover:
                share();
                AVAnalytics.onEvent(ImgShareActivity.this, "imgshare_pg_share_btn");
                break;
            case R.id.material_color_1:
                parent_layout.setBackgroundColor(ImgShareActivity.this.getResources().getColor(R.color.material_color_light_blue));
                break;
            case R.id.material_color_2:
                parent_layout.setBackgroundColor(ImgShareActivity.this.getResources().getColor(R.color.material_color_cyan));
                break;
            case R.id.material_color_3:
                parent_layout.setBackgroundColor(ImgShareActivity.this.getResources().getColor(R.color.material_color_deep_orange));
                break;
            case R.id.material_color_4:
                parent_layout.setBackgroundColor(ImgShareActivity.this.getResources().getColor(R.color.material_color_green));
                break;
            case R.id.material_color_5:
                parent_layout.setBackgroundColor(ImgShareActivity.this.getResources().getColor(R.color.material_color_indigo));
                break;
            case R.id.text_color_1:
                share_content.setTextColor(ImgShareActivity.this.getResources().getColor(R.color.white));
                break;
            case R.id.text_color_2:
                share_content.setTextColor(ImgShareActivity.this.getResources().getColor(R.color.text_tint));
                break;
            case R.id.text_color_3:
                share_content.setTextColor(ImgShareActivity.this.getResources().getColor(R.color.text_grey));
                break;
            case R.id.text_color_4:
                share_content.setTextColor(ImgShareActivity.this.getResources().getColor(R.color.content_txt_6));
                break;
            case R.id.text_color_5:
                share_content.setTextColor(ImgShareActivity.this.getResources().getColor(R.color.text_dark));
                break;
            case R.id.style_1:
                share_content.setGravity(Gravity.LEFT);
                break;
            case R.id.style_2:
                share_content.setGravity(Gravity.RIGHT);
                break;
            case R.id.style_3:
                share_content.setGravity(Gravity.CENTER);
                break;
            case R.id.style_4:
                share_content.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                break;
            case R.id.style_5:
                share_content.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                break;
            default:
                break;
        }
    }

    private void playMusic() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        mSoundPoll.play(mSoundId, volume, volume, 1, 0, 1f);
    }

    private void share() {
        try {
            playMusic();
            shareContent = share_content.getText().toString();
            if (!TextUtils.isEmpty(shareContent)) {
                shareWithImg();
            } else {
                ToastUtil.diaplayMesShort(ImgShareActivity.this, R.string.share_text_hint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
