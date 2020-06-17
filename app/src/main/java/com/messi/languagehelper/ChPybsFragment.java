package com.messi.languagehelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.adapter.RcPybsListAdapter;
import com.messi.languagehelper.adapter.RcPybsMenuAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.views.DividerGridItemDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class ChPybsFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 4;
    public String[] type_py = {"全部","A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q",
            "R","S","T","W","X","Y","Z"};
    public String[] type_bs = {"全部","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
    @BindView(R.id.menulistview)
    RecyclerView menulistview;
    @BindView(R.id.listview)
    RecyclerView listview;
    public static final String pinyin = "1";
    public static final String bushou = "2";
    public String type;
    public String code;
    private RcPybsMenuAdapter menuAdapter;
    private RcPybsListAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<AVObject> mList;
    private int skip = 0;
    private int page_size = 80;
    private boolean loading;
    private boolean hasMore = true;

    public static ChPybsFragment getInstance(String stype){
        ChPybsFragment fragment = new ChPybsFragment();
        fragment.type = stype;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.chdic_bushoupinyin_activity, null);
        ButterKnife.bind(this, view);
        initSwipeRefresh(view);
        initViews();
        return view;
    }

    @Override
    public void loadDataOnStart() {
        RequestAsyncTask();
    }

    private void initViews() {
        liveEventBus();
        mList = new ArrayList<AVObject>();
        menuAdapter = new RcPybsMenuAdapter();
        if(!TextUtils.isEmpty(type) && pinyin.equals(type)){
            menuAdapter.setItems(Arrays.asList(type_py));
        }else {
            menuAdapter.setItems(Arrays.asList(type_bs));
        }
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        menulistview.setLayoutManager(mLinearLayoutManager);
        menulistview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        menulistview.setAdapter(menuAdapter);

        mAdapter = new RcPybsListAdapter();
        mAdapter.setItems(mList);
        mAdapter.setFooter(new Object());
        hideFooterview();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
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
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XhDicList.XhDicList);
        if(!TextUtils.isEmpty(code)){
            query.whereEqualTo(AVOUtil.XhDicList.code, code);
        }
        query.whereEqualTo(AVOUtil.XhDicList.type, type);
        query.orderByAscending(AVOUtil.XhDicList.createdAt);
        query.skip(skip);
        query.limit(page_size);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
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
                    ToastUtil.diaplayMesShort(getContext(), "加载失败，下拉可刷新");
                }
            }
        }));
    }

    public void liveEventBus(){
        LiveEventBus.get(KeyUtil.ChPybsType,String.class).observe(getViewLifecycleOwner(), result -> {
            if("全部".equals(result)){
                this.code = "";
            }else {
                this.code = result;
            }
            onSwipeRefreshLayoutRefresh();
        });
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

}
