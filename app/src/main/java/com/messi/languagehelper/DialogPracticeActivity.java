package com.messi.languagehelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.messi.languagehelper.bean.DialogBean;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;

import java.util.List;

public class DialogPracticeActivity extends BaseActivity implements OnClickListener {

    private TextView record_question, record_answer, practice_prompt, record_animation_text;
    private LinearLayout record_layout, record_animation_layout;
    private ProgressBar mProgressbar;
    private FrameLayout voice_btn;
    private ListView dialog_lv;
    private List<DialogBean> beans;

    private String vedioPath;
    private String content;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_practice_activity);
        initData();
        initView();
    }

    private void initData() {
        vedioPath = getIntent().getStringExtra(KeyUtil.SDcardPathKey);
        content = getIntent().getStringExtra(KeyUtil.ContextKey);
        type = getIntent().getStringExtra(KeyUtil.StudyDialogAction);
        LogUtil.DefalutLog("content---:" + content);
    }

    private void initView() {
        mProgressbar = (ProgressBar) findViewById(R.id.progressBarCircularIndetermininate);
        record_animation_layout = (LinearLayout) findViewById(R.id.record_animation_layout);
        record_animation_text = (TextView) findViewById(R.id.record_animation_text);
        record_layout = (LinearLayout) findViewById(R.id.record_layout);
        practice_prompt = (TextView) findViewById(R.id.practice_prompt);
        voice_btn = (FrameLayout) findViewById(R.id.voice_btn);
        dialog_lv = (ListView) findViewById(R.id.dialog_lv);
    }

    @Override
    public void onClick(View v) {

    }

}
