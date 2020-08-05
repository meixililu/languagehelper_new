package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcAdEnglishAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class AdEnglishActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 1;
    private static final int page_size = 20;
    @BindView(R.id.studycategory_lv)
    RecyclerView category_lv;
    private RcAdEnglishAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;
    private GridLayoutManager layoutManager;
//    private String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initViews();
        new QueryTask(this).execute();
    }

    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_one));
//        code = getIntent().getStringExtra(KeyUtil.SubjectName);
        avObjects = new ArrayList<AVObject>();
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new RcAdEnglishAdapter();
            mAdapter.setItems(avObjects);
            mAdapter.setFooter(new Object());
            hideFooterview();
            layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
            HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
            layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
            category_lv.setLayoutManager(layoutManager);
            category_lv.addItemDecoration(new DividerGridItemDecoration(1));
            category_lv.setAdapter(mAdapter);
            setListOnScrollListener();
        }
    }

    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        new QueryTask(AdEnglishActivity.this).execute();
                    }
                }
            }
        });
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        skip = 0;
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask(this).execute();
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        private WeakReference<AdEnglishActivity> mainActivity;

        public QueryTask(AdEnglishActivity mActivity){
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
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.AdList.AdList);
//            if (!TextUtils.isNotEmpty(code)) {
//                query.whereEqualTo(AVOUtil.SubjectList.code, code);
//            }
            query.whereEqualTo(AVOUtil.AdList.subject, "english");
            query.whereContains(AVOUtil.AdList.app,"zyhy");
            query.orderByAscending(AVOUtil.SubjectList.order);
            query.skip(skip);
            query.limit(page_size);
            try {
                return query.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null){
                loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                initAdapter();
                if (avObject != null) {
                    if (avObject.size() == 0) {
                        ToastUtil.diaplayMesShort(AdEnglishActivity.this, "没有了！");
                        hasMore = false;
                        hideFooterview();
                    } else {
                        if (avObjects != null && mAdapter != null) {
                            avObjects.addAll(avObject);
                            if (avObject.size() == page_size) {
                                skip += page_size;
                                showFooterview();
                                hasMore = true;
                            } else {
                                hasMore = false;
                                hideFooterview();
                            }
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }
}
