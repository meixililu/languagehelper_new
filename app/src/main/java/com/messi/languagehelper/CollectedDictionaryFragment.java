package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcCollectDictionaryListAdapter;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CollectedDictionaryFragment extends BaseFragment {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcCollectDictionaryListAdapter mAdapter;
    private List<Dictionary> beans;
    private View view;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    private LinearLayoutManager mLinearLayoutManager;
    private int page = 0;
    private int page_size = 20;
    private boolean loading;
    private boolean hasMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.DefalutLog("CollectedDictionaryFragment-onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.DefalutLog("CollectedDictionaryFragment-onCreateView");
        view = inflater.inflate(R.layout.collected_translate_fragment, null);
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
        beans = new ArrayList<Dictionary>();

        mAdapter = new RcCollectDictionaryListAdapter(getActivity(), beans, mSharedPreferences);
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
        List<Dictionary> list = DataBaseUtil.getInstance().getDicCollectedListByPage(page, page_size);
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

}
