package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcWordListAdapter;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordStudyPlanActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    @BindView(R.id.listview)
    RecyclerView listview;
    private RcWordListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_plan_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initViews();
        new QueryTask().execute();
    }

    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_word_study_change_plan));
        avObjects = new ArrayList<AVObject>();
        mXFYSAD = new XFYSAD(this, ADUtil.SecondaryPage);
        mAdapter = new RcWordListAdapter(mXFYSAD,"study_plan");
        listview.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        listview.setLayoutManager(layoutManager);
        listview.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setHeader(new Object());
        mAdapter.setItems(avObjects);
        mXFYSAD.setAdapter(mAdapter);
        listview.setAdapter(mAdapter);
    }

    private class QueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordCategory.WordCategory);
                query.whereEqualTo(AVOUtil.WordCategory.isvalid, "1");
                query.orderByAscending(AVOUtil.WordCategory.order);
                List<AVObject> mAVObjects = query.find();
                if (avObjects != null) {
                    avObjects.clear();
                    avObjects.addAll(mAVObjects);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        new QueryTask().execute();
    }

}
