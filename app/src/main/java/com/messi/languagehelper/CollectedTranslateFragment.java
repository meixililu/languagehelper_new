package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.RcCollectTranslateListAdapter;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CollectedTranslateFragment extends BaseFragment {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcCollectTranslateListAdapter mAdapter;
    private List<record> beans;
    private View view;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.collected_translate_fragment, null);
        init();
        return view;
    }

    private void init() {
        mInflater = LayoutInflater.from(getActivity());
        mSharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
        recent_used_lv = (RecyclerView) view.findViewById(R.id.collected_listview);

        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(getActivity()));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));

        beans = new ArrayList<record>();
        beans.addAll(DataBaseUtil.getInstance().getDataListCollected(0, Settings.offset));
        mAdapter = new RcCollectTranslateListAdapter(mSharedPreferences, beans);
        mAdapter.setItems(beans);
        recent_used_lv.setAdapter(mAdapter);
    }

}
