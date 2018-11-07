package com.messi.languagehelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcCollectTranslateListAdapter;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.views.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectedTranslateFragment extends BaseFragment {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcCollectTranslateListAdapter mAdapter;
    private List<record> beans;
    private View view;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    private LinearLayoutManager mLinearLayoutManager;
    private int page = 0;
    private int page_size = 20;
    private boolean loading;
    private boolean hasMore = true;
    private StringBuilder sb = new StringBuilder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.collected_translate_fragment, null);
        setHasOptionsMenu(true);
        init();
        return view;
    }

    private void init() {
        mInflater = LayoutInflater.from(getActivity());
        mSharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
        recent_used_lv = (RecyclerView) view.findViewById(R.id.collected_listview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recent_used_lv.setLayoutManager(mLinearLayoutManager);
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        beans = new ArrayList<record>();

        mAdapter = new RcCollectTranslateListAdapter(mSharedPreferences, beans);
        mAdapter.setItems(beans);
        recent_used_lv.setAdapter(mAdapter);
        setListOnScrollListener();
        QueryTask();
    }

    public void setListOnScrollListener() {
        recent_used_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        QueryTask();
                    }
                }
            }
        });
    }

    private void QueryTask(){
        loading = true;
        List<record> list = DataBaseUtil.getInstance().getTranCollectedListByPage(page, page_size);
        if(list.size() == 0){
            hasMore = false;
        }else {
            beans.addAll(list);
            page += 1;
            mAdapter.notifyDataSetChanged();
            hasMore = true;
        }
        loading = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.symbol_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showXunFeiDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showXunFeiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage("下载复制所有的单词。");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void download(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(getData());
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String rusult) {
                Setings.copy(getContext(),sb.toString());
                openData(rusult);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private String getData(){
        sb.setLength(0);
        List<record> list = DataBaseUtil.getInstance().getAllCollectedData();
        for(record item : list){
            sb.append(item.getChinese() + "\n" + item.getEnglish());
            sb.append("\n\n");
        }
        String filePath = SDCardUtil.saveFile(getContext(),"","/words.txt",sb.toString());
        return filePath;
    }

    private void openData(String filePath){
        File file = new File(filePath);
        if (file != null && file.exists() && file.isFile()) {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getContext(), Setings.getProvider(getContext()), file);
            } else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(intent);
        }
    }
}
