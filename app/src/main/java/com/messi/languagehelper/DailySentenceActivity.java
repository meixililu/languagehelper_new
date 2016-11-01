package com.messi.languagehelper;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.messi.languagehelper.adapter.RcDailySentenceListAdapter;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.XFYSAD;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DailySentenceActivity extends BaseActivity implements OnClickListener {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcDailySentenceListAdapter mAdapter;
    private List<EveryDaySentence> beans;
    private MediaPlayer mPlayer;

    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_sentence_activity);
        init();
        new GetDataTask().execute();
    }

    private void init() {
        try {
            getSupportActionBar().setTitle(getResources().getString(R.string.dailysentence));
            mPlayer = new MediaPlayer();
            mInflater = LayoutInflater.from(this);
            beans = new ArrayList<EveryDaySentence>();
            mXFYSAD = new XFYSAD(DailySentenceActivity.this, ADUtil.MRYJYSNRLAd);
            recent_used_lv = (RecyclerView) findViewById(R.id.listview);
            mAdapter = new RcDailySentenceListAdapter(this, beans, mPlayer, mProgressbar, mXFYSAD);
            mAdapter.setHeader(new Object());
            mAdapter.setItems(beans);
            recent_used_lv.setLayoutManager(new LinearLayoutManager(this));
            recent_used_lv.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(this)
                            .colorResId(R.color.text_tint)
                            .sizeResId(R.dimen.list_divider_size)
                            .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                            .build());
            recent_used_lv.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXFYSAD != null) {
            mXFYSAD.startPlayImg();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
            mXFYSAD = null;
        }
        LogUtil.DefalutLog("CollectedFragment-onDestroy");
    }

    @Override
    public void onClick(View v) {

    }

    class GetDataTask extends AsyncTask<Void, Void, List<EveryDaySentence>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
        }

        @Override
        protected List<EveryDaySentence> doInBackground(Void... params) {
            List<EveryDaySentence> list = DataBaseUtil.getInstance().getDailySentenceList(Settings.offset);
            return list;
        }

        @Override
        protected void onPostExecute(List<EveryDaySentence> list) {
            hideProgressbar();
            beans.clear();
            beans.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}
