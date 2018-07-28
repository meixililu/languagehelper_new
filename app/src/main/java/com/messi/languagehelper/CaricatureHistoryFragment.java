package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcCaricatureBookShelfAdapter;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CaricatureHistoryFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
    private Toolbar my_awesome_toolbar;
    private RcCaricatureBookShelfAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<AVObject> mList;
    private int skip = 0;
    private int page_size = 21;
    private boolean loading;
    private boolean hasMore = true;

    public static CaricatureHistoryFragment newInstance(){
        CaricatureHistoryFragment fragment = new CaricatureHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.caricature_web_list_fragment, null);
        initViews(view);
        initSwipeRefresh(view);
        RequestAsyncTask();
        return view;
    }

    private void initViews(View view) {
        mList = new ArrayList<AVObject>();
        my_awesome_toolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        my_awesome_toolbar.setTitle(R.string.title_bookshelf);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcCaricatureBookShelfAdapter();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        mAdapter.setItems(mList);
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

    private void RequestAsyncTask(){
        showProgressbar();
        loading = true;
        Observable.create(new ObservableOnSubscribe<List<AVObject>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AVObject>> e) throws Exception {
                e.onNext(getData());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AVObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<AVObject> s) {
                        onFinishLoadData(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<AVObject> getData(){
        List<AVObject> list = DataBaseUtil.getInstance().getCaricaturesList(AVOUtil.Caricature.Caricature,
                skip,page_size,true,false);
        return list;
    }

    private void onFinishLoadData(List<AVObject> list) {
        onSwipeRefreshLayoutFinish();
        hideProgressbar();
        loading = false;
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
            ToastUtil.diaplayMesShort(getContext(), "加载失败，下拉可刷新");
        }

    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

}
