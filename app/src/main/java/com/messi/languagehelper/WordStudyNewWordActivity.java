package com.messi.languagehelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.adapter.RcCollectTranslateListAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.databinding.WordStudyNewWordActivityBinding;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
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

import static androidx.recyclerview.widget.DividerItemDecoration.HORIZONTAL;

public class WordStudyNewWordActivity extends BaseActivity {

    private int skip = 0;
    private int pagesize = 100;
    private boolean loading;
    private boolean hasMore = true;
    private ArrayList<WordDetailListItem> itemList;
    private WordStudyNewWordActivityBinding binding;
    private RcCollectTranslateListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WordStudyNewWordActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
        getDataTask();
    }

    private void init() {
        SharedPreferences sp = Setings.getSharedPreferences(this);
        setActionBarTitle(getString(R.string.new_and_wrong));
        itemList = new ArrayList<WordDetailListItem>();
        mLinearLayoutManager = new LinearLayoutManager(this);
        binding.collectedListview.setLayoutManager(mLinearLayoutManager);
        binding.collectedListview.addItemDecoration(new DividerItemDecoration(this, HORIZONTAL));
        mAdapter = new RcCollectTranslateListAdapter(sp, itemList);
        mAdapter.setItems(itemList);
        binding.collectedListview.setAdapter(mAdapter);
        binding.bottom.setOnClickListener(view -> toWordStudyActivity());
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        binding.collectedListview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        getDataTask();
                    }
                }
            }
        });
    }

    private void refresh(){
        skip = 0;
        hasMore = true;
        loading = false;
        itemList.clear();
        mAdapter.notifyDataSetChanged();
        getDataTask();
    }

    private void getDataTask() {
        if (!hasMore && loading) {
            return;
        }
        LogUtil.DefalutLog("getDataTask");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                loadData(e);
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
                mAdapter.notifyDataSetChanged();
                ToastUtil.diaplayMesShort(WordStudyNewWordActivity.this,"没有需要学习的生词");
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadData(ObservableEmitter<String> e) {
        loading = true;
        List<WordDetailListItem> list = BoxHelper.getNewWordList(skip,pagesize);
        if (NullUtil.isNotEmpty(list)) {
            skip += pagesize;
            itemList.addAll(list);
            hasMore = true;
            e.onComplete();
        } else {
            if (skip == 0) {
                e.onNext("");
            }
            hasMore = false;
        }
        loading = false;
    }

    private void toWordStudyActivity() {
        if (NullUtil.isNotEmpty(itemList)) {
            Intent intent = new Intent(this, WordDetailActivity.class);
            if (itemList.size() > 20) {
                ArrayList<WordDetailListItem> listItems = new ArrayList<>();
                for(int i = 0; i < 20; i++){
                    listItems.add(itemList.get(i));
                }
                intent.putParcelableArrayListExtra(KeyUtil.List, listItems);
            } else {
                intent.putParcelableArrayListExtra(KeyUtil.List, itemList);
            }
            startActivityForResult(intent,1);
        }else {
            ToastUtil.diaplayMesShort(this,"没有需要学习的生词");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }
}
