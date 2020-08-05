package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.adapter.RcWordStudySummaryListAdapter;
import com.messi.languagehelper.adapter.RcWordStudySummaryMenuListAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.views.DividerGridItemDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WordStudySummaryListActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    @BindView(R.id.menulistview)
    RecyclerView menulistview;
    @BindView(R.id.listview)
    RecyclerView listview;
    private List<AVObject> mAVObjectList;
    private List<AVObject> mAVObjectMenuList;
    private SharedPreferences sharedPreferences;
    private RcWordStudySummaryListAdapter mAdapter;
    private RcWordStudySummaryMenuListAdapter mMenuAdapter;
    private GridLayoutManager layoutManager;
    private int skip = 0;
    private int pageSize = 60;
    private String category;
    private String list_type;
    private String type;
    private boolean loading;
    private boolean hasMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_summary_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initViews();
        getDataTask();
    }

    private void initViews() {
        String title = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
        getSupportActionBar().setTitle(title);
        category = getIntent().getStringExtra(KeyUtil.Category);
        list_type = getIntent().getStringExtra(KeyUtil.list_type);
        sharedPreferences = Setings.getSharedPreferences(this);

        mAVObjectList = new ArrayList<AVObject>();
        mAVObjectMenuList = new ArrayList<AVObject>();
        mMenuAdapter = new RcWordStudySummaryMenuListAdapter(this);
        mMenuAdapter.setItems(mAVObjectMenuList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        menulistview.setLayoutManager(mLinearLayoutManager);
        menulistview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        menulistview.setAdapter(mMenuAdapter);

        mAdapter = new RcWordStudySummaryListAdapter();
        mAdapter.setItems(mAVObjectList);
        mAdapter.setFooter(new Object());
        hideFooterview();
        layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        listview.setHasFixedSize(true);
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
                        getDataTask();
                    }
                }
            }
        });
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        hideFooterview();
        skip = 0;
        getDataTask();
    }

    private void getDataTask() {
        loading = true;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                getMenuServiceData();
                getServiceData();
                e.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                loading = false;
                if(hasMore){
                    showFooterview();
                }else {
                    hideFooterview();
                }
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                mAdapter.notifyDataSetChanged();
                mMenuAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getServiceData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.HJWordStudyCList.HJWordStudyCList);
        query.whereEqualTo(AVOUtil.HJWordStudyCList.category,category);
        query.orderByAscending(AVOUtil.HJWordStudyCList.createdAt);
        if(!TextUtils.isEmpty(type) && !type.equals("1111")){
            query.whereEqualTo(AVOUtil.HJWordStudyCList.type,type);
        }
        query.skip(skip);
        query.limit(pageSize);
        List<AVObject> sentences = null;
        try{
            sentences = query.find();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (sentences != null && sentences.size() > 0) {
            if(skip == 0){
                mAVObjectList.clear();
            }
            mAVObjectList.addAll(sentences);
            skip += pageSize;
            hasMore = true;
        }else {
            hasMore = false;
        }
    }

    private void getMenuServiceData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.HJWordStudyCategory.HJWordStudyCategory);
        query.whereEqualTo(AVOUtil.HJWordStudyCategory.category, "0");
        query.whereEqualTo(AVOUtil.HJWordStudyCategory.isValid, "1");
        query.whereEqualTo(AVOUtil.HJWordStudyCategory.ltype, list_type);
        query.orderByAscending(AVOUtil.HJWordStudyCategory.order);
        List<AVObject> sentences = null;
        try{
            sentences = query.find();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (sentences != null && sentences.size() > 0) {
            mAVObjectMenuList.clear();
            mAVObjectMenuList.addAll(sentences);
        }
    }

    public void getDataByType(String type_code){
        type = type_code;
        skip = 0;
        getDataTask();
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

}
