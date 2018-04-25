package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordStudySummaryDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.xx_ad_layout)
    RelativeLayout xx_ad_layout;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private AVObject mAVObject;
    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_summary_detail_activity);
        ButterKnife.bind(this);
        initData();
        setData();
    }

    private void initData() {
        mAVObject = (AVObject) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        WXEntryActivity.dataMap.clear();
        if (mAVObject == null) {
            finish();
        }
    }

    private void setData() {
        String titleStr = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
        toolbar_layout.setTitle(titleStr);
        title.setText(titleStr);
        scrollview.scrollTo(0, 0);
        TextHandlerUtil.handlerText(this, mProgressbar, content, mAVObject.getString(AVOUtil.HJWordStudyCList.word_des));

        mXFYSAD = new XFYSAD(this, xx_ad_layout, ADUtil.NewsDetail);
        mXFYSAD.showAD();
    }
}
