package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLAVObjectModel;
import com.messi.languagehelper.adapter.RcSubjectListAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 1;
    @BindView(R.id.studycategory_lv)
    RecyclerView category_lv;
    private RcSubjectListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private GridLayoutManager layoutManager;
    private String code;
    private String recentKey;
    private XXLAVObjectModel mXXLModel;

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
        code = getIntent().getStringExtra(KeyUtil.SubjectName);
        recentKey = getIntent().getStringExtra(KeyUtil.RecentKey);
        avObjects = new ArrayList<AVObject>();
        mXXLModel = new XXLAVObjectModel(this);
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new RcSubjectListAdapter();
            mAdapter.setItems(avObjects);
            mAdapter.setFooter(new Object());
            mXXLModel.setAdapter(avObjects,mAdapter);
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
                if (!mXXLModel.loading && mXXLModel.hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        new QueryTask(SubjectActivity.this).execute();
                    }
                }
            }
        });
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
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

        private WeakReference<SubjectActivity> mainActivity;

        public QueryTask(SubjectActivity mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mXXLModel != null){
                mXXLModel.loading = true;
            }
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectList.SubjectList);
            if (!TextUtils.isEmpty(code)) {
                query.whereEqualTo(AVOUtil.SubjectList.code, code);
            }
            query.orderByAscending(AVOUtil.SubjectList.order);
            query.skip(skip);
            query.limit(Setings.page_size);
            try {
                return query.find();
            } catch (AVException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null){
                mXXLModel.loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                initAdapter();
                if (avObject != null) {
                    if (avObject.size() == 0) {
                        ToastUtil.diaplayMesShort(SubjectActivity.this, "没有了！");
                        mXXLModel.hasMore = false;
                        hideFooterview();
                    } else {
                        if (avObjects != null && mAdapter != null) {
                            addBgColor(avObject);
                            avObjects.addAll(avObject);
                            if (avObject.size() == Setings.page_size) {
                                skip += Setings.page_size;
                                showFooterview();
                                mXXLModel.hasMore = true;
                            } else {
                                mXXLModel.hasMore = false;
                                hideFooterview();
                            }
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                loadAD();
            }
        }

    }

    private void addBgColor(List<AVObject> avObject){
        for (AVObject item : avObject){
            item.put(KeyUtil.ColorKey, ColorUtil.getRadomColor());
        }
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
