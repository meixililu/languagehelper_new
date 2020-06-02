package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.WordStudyDetailAdapter;
import com.messi.languagehelper.util.KeyUtil;

public class WordStudyDanCiRenZhiActivity extends BaseActivity implements OnClickListener {

    private ListView category_lv;
    private WordStudyDetailAdapter mAdapter;
    private FloatingActionButton playbtn;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private MediaPlayer mPlayer;
    private int index;

    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_detail_activity);
        initViews();
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.dancirenzhi));
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mPlayer = new MediaPlayer();

        class_name = getIntent().getStringExtra(KeyUtil.ClassName);
        class_id = getIntent().getStringExtra(KeyUtil.ClassId);
        course_id = getIntent().getIntExtra(KeyUtil.CourseId, 1);
        course_num = getIntent().getIntExtra(KeyUtil.CourseNum, 0);
        playbtn = (FloatingActionButton) findViewById(R.id.playbtn);
        category_lv = (ListView) findViewById(R.id.studycategory_lv);
        mAdapter = new WordStudyDetailAdapter(this, mSharedPreferences, mSpeechSynthesizer, category_lv,
                WordStudyFragment.itemList, mPlayer);
        category_lv.setAdapter(mAdapter);
        playbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playbtn:
                playSound();
                break;
        }
    }

    private void playSound() {
        if (mAdapter.isPlaying()) {
            playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_pause_white));
            mAdapter.onPlayBtnClick(index);
        } else {
            playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
        }
    }

    public void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordStudyFragment.clearSign();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer = null;
        }
    }


}
