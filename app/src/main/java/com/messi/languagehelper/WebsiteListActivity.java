package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcWebsiteListAdapter;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class WebsiteListActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView category_lv;
    private RcWebsiteListAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<AVObject> mList;
    private XFYSAD mXFYSAD;
    private int skip = 0;
    private int page_size = 20;
    private boolean loading;
    private boolean hasMore = true;
    private String category;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.website_list_activity);
        initSwipeRefresh();
        initViews();
        RequestAsyncTask();
    }

    private void initViews() {
        category = getIntent().getStringExtra(KeyUtil.Category);
        title = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
        if(!TextUtils.isEmpty(title)){
            getSupportActionBar().setTitle(title);
        }
        mXFYSAD = new XFYSAD(this, ADUtil.SecondaryPage);
        mList = new ArrayList<AVObject>();
        category_lv = (RecyclerView) findViewById(R.id.listview);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcWebsiteListAdapter(mXFYSAD);
        layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setHeader(new Object());
        mAdapter.setItems(mList);
        mXFYSAD.setAdapter(mAdapter);
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
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
                        RequestAsyncTask();
                    }
                }
            }
        });
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        skip = 0;
        hasMore = true;
        RequestAsyncTask();
    }


    private void RequestAsyncTask() {
        showProgressbar();
        loading = true;
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EnglishWebsite.EnglishWebsite);
        query.whereEqualTo(AVOUtil.EnglishWebsite.category, category);
        query.orderByDescending(AVOUtil.EnglishWebsite.Order);
        query.skip(skip);
        query.limit(page_size);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                hideProgressbar();
                loading = false;
                onSwipeRefreshLayoutFinish();
                if(list != null){
                    if(list.size() == 0){
                        hasMore = false;
                        hideFooterview();
                    }else {
                        if(skip == 0){
                            mList.clear();
                        }
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        if(list.size() < page_size){
                            hasMore = false;
                            hideFooterview();
                        }else {
                            skip += page_size;
                            hasMore = true;
                            showFooterview();
                        }
                    }
                }else{
                    ToastUtil.diaplayMesShort(WebsiteListActivity.this, "加载失败，下拉可刷新");
                }
            }
        });
    }


    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }
}
