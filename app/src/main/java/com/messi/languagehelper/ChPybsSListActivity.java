package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.messi.languagehelper.adapter.RcPybsTypeListAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChPybsSListActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 5;
    @BindView(R.id.listview)
    RecyclerView listview;
    private String type;//bushou,pinyin
    private String code;
    private RcPybsTypeListAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<AVObject> mList;
    private int skip = 0;
    private int page_size = 50;
    private boolean loading;
    private boolean hasMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chdic_bushoupinyin_list_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initViews();
        RequestAsyncTask();
    }

    private void initViews() {
        code = getIntent().getStringExtra(KeyUtil.CodeKey);
        type = getIntent().getStringExtra(KeyUtil.Type);
        mList = new ArrayList<AVObject>();
        mAdapter = new RcPybsTypeListAdapter();
        mAdapter.setItems(mList);
        mAdapter.setFooter(new Object());
        hideFooterview();
        layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        listview.setLayoutManager(layoutManager);
        listview.addItemDecoration(new DividerGridItemDecoration(1));
        setListOnScrollListener();
        listview.setAdapter(mAdapter);
    }

    public void setListOnScrollListener(){
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
                        RequestAsyncTask();
                    }
                }
            }
        });
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        skip = 0;
        hasMore = true;
        RequestAsyncTask();
    }

    private void RequestAsyncTask() {
        showProgressbar();
        loading = true;
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XhDicSList.XhDicSList);
        query.selectKeys(Arrays.asList("name"));
        if(type.equals("1")){
            query.whereEqualTo(AVOUtil.XhDicSList.code, code);
        }else {
            query.whereEqualTo(AVOUtil.XhDicSList.bs, code);
        }
        query.orderByAscending(AVOUtil.XhDicSList.createdAt);
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
                    ToastUtil.diaplayMesShort(ChPybsSListActivity.this, "加载失败，下拉可刷新");
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
