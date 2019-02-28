package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.messi.languagehelper.adapter.WordBookListAdapter;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

public class WordStudyThirdActivity extends BaseActivity {

    private ListView category_lv;
    private WordBookListAdapter mAdapter;
    private List<WordListItem> child_list;
    private XFYSAD mXFYSAD;
    private String play_sign;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_list_activity);
        registerBroadcast(BaseActivity.ActivityClose);
        initSwipeRefresh();
        initViews();
    }

    private void initViews() {
        child_list = (List<WordListItem>) Setings.dataMap.get(KeyUtil.DataMapKey);
        play_sign = getIntent().getStringExtra(KeyUtil.WordStudyPlan);
        Setings.dataMap.clear();
        if (child_list != null) {
            category_lv = (ListView) findViewById(R.id.studycategory_lv);
            View headerView = LayoutInflater.from(this).inflate(R.layout.xunfei_ysad_item, null);
            category_lv.addHeaderView(headerView);
            mAdapter = new WordBookListAdapter(this, child_list, play_sign);
            category_lv.setAdapter(mAdapter);

            mXFYSAD = new XFYSAD(this, headerView, ADUtil.SecondaryPage);
            mXFYSAD.showAd();
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        mAdapter.notifyDataSetChanged();
        onSwipeRefreshLayoutFinish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }
}
