package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcWordListAdapter;
import com.messi.languagehelper.dao.WordListItem;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class WordStudyFirstFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView category_lv;
    private TextView last_study_unit;
    private FrameLayout last_study_unit_layout;
    private RcWordListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;
    private SharedPreferences spf;
    private boolean isNeedSaveData;
    private WordListItem wordListItem;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_study_list_fragment, container, false);
        initSwipeRefresh(view);
        initViews(view);
        initData();
        setLast_study_unit();
        return view;
    }

    private void initViews(View view) {
        spf = Settings.getSharedPreferences(getContext());
        avObjects = new ArrayList<AVObject>();
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        last_study_unit = (TextView) view.findViewById(R.id.last_study_unit);
        last_study_unit_layout = (FrameLayout) view.findViewById(R.id.last_study_unit_layout);
        mXFYSAD = new XFYSAD(getContext(), ADUtil.SecondaryPage);
        mAdapter = new RcWordListAdapter(mXFYSAD);
        category_lv.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setHeader(new Object());
        category_lv.setAdapter(mAdapter);

        last_study_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick();
            }
        });
    }

    private void onItemClick() {
        BaseApplication.dataMap.put(KeyUtil.DataMapKey, wordListItem);
        Intent intent = new Intent(getContext(), WordStudyFourthActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, wordListItem.getTitle());
        getActivity().startActivity(intent);
    }

    private void initData() {
        try {
            long lastTimeSave = spf.getLong(KeyUtil.SaveLastTime_WordStudyCategoryList, 0);
            if (System.currentTimeMillis() - lastTimeSave > 1000 * 60 * 60 * 24 * 10) {
                SaveData.deleteObject(getActivity(), "WordStudyCategoryList");
                LogUtil.DefalutLog("deleteObject   WordStudyCategoryList");
                new QueryTask().execute();
            } else {
                List<String> listStr = (ArrayList<String>) SaveData.getObject(getActivity(), "WordStudyCategoryList");
                if (listStr == null || listStr.size() == 0) {
                    LogUtil.DefalutLog("avObjects is null");
                    new QueryTask().execute();
                } else {
                    LogUtil.DefalutLog("avObjects is not null");
                    for (String str : listStr) {
                        avObjects.add(AVObject.parseAVObject(str));
                    }
                    mAdapter.setItems(avObjects);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            new QueryTask().execute();
            e.printStackTrace();
        }
    }

    private void setLast_study_unit(){
        wordListItem = (WordListItem) SaveData.getObject(getContext(),KeyUtil.WordStudyUnit);
        if(wordListItem != null){
            last_study_unit.setText("上次学习至："+ wordListItem.getTitle() + "第" + wordListItem.getCourse_id() + "单元");
            last_study_unit_layout.setVisibility(View.VISIBLE);
        }else {
            last_study_unit_layout.setVisibility(View.GONE);
        }
        LogUtil.DefalutLog("setLast_study_unit:"+wordListItem);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            setLast_study_unit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mXFYSAD != null) {
            mXFYSAD.startPlayImg();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        new QueryTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (avObjects != null && isNeedSaveData) {
            List<String> listStr = new ArrayList<String>();
            for (AVObject item : avObjects) {
                listStr.add(item.toString());
            }
            SaveData.saveObject(getActivity(), "WordStudyCategoryList", listStr);
            Settings.saveSharedPreferences(spf, KeyUtil.SaveLastTime_WordStudyCategoryList,
                    System.currentTimeMillis());
            LogUtil.DefalutLog("saveObject   WordStudyCategoryList");
        }
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
            mXFYSAD = null;
        }
    }

    private class QueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordCategory.WordCategory);
                query.whereEqualTo(AVOUtil.WordCategory.isvalid, "1");
                query.orderByAscending(AVOUtil.WordCategory.order);
                List<AVObject> mAVObjects = query.find();
                if (avObjects != null) {
                    avObjects.clear();
                    avObjects.addAll(mAVObjects);
                    isNeedSaveData = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            mAdapter.setItems(avObjects);
            mAdapter.notifyDataSetChanged();
        }
    }

}
