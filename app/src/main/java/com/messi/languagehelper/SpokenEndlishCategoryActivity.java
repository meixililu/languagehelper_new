package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.adapter.RcSpokenEndlishCategoryAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class SpokenEndlishCategoryActivity extends BaseActivity {

    private RecyclerView category_lv;
    private RcSpokenEndlishCategoryAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private String code;
    private boolean loading;
    private boolean hasMore = true;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spoken_englis_category_activity);
        initSwipeRefresh();
        initViews();
        new QueryTask(this).execute();
    }

    private void initViews() {
        code = getIntent().getStringExtra(AVOUtil.EvaluationType.ETCode);
        avObjects = new ArrayList<AVObject>();
        category_lv = (RecyclerView) findViewById(R.id.listview);
        mAdapter = new RcSpokenEndlishCategoryAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(this);
        category_lv.setLayoutManager(mLinearLayoutManager);
        category_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        new QueryTask(SpokenEndlishCategoryActivity.this).execute();
                    }
                }
            }
        });
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        skip = 0;
        hideFooterview();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask(this).execute();
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        private WeakReference<SpokenEndlishCategoryActivity> mainActivity;

        public QueryTask(SpokenEndlishCategoryActivity mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationCategory.EvaluationCategory);
            query.whereEqualTo(AVOUtil.EvaluationCategory.ECIsValid, "1");
            query.whereEqualTo(AVOUtil.EvaluationCategory.ETCode, code);
            query.orderByDescending(AVOUtil.EvaluationCategory.ECOrder);
            query.skip(skip);
            query.limit(Setings.page_size);
            return query.find();
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null){
                LogUtil.DefalutLog("SpokenEndlishCategoryActivity---onPostExecute");
                loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if (avObject != null) {
                    if (avObject.size() == 0) {
                        ToastUtil.diaplayMesShort(SpokenEndlishCategoryActivity.this, "没有了！");
                        hideFooterview();
                    } else {
                        avObjects.addAll(avObject);
                        mAdapter.notifyDataSetChanged();
                        if (skip == 0 && avObject.size() < Setings.page_size) {
                            hasMore = false;
                            hideFooterview();
                        } else {
                            showFooterview();
                            skip += Setings.page_size;
                        }
                    }
                } else {
                    ToastUtil.diaplayMesShort(SpokenEndlishCategoryActivity.this, "加载失败，下拉可刷新");
                }
            }
        }

    }

}
