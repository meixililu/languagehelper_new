package com.messi.languagehelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcSpokenEndlishPracticeTypeListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SpokenEnglishPractiseFragment extends BaseFragment {

    private RecyclerView category_lv;
    private RcSpokenEndlishPracticeTypeListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;
    private boolean isHasLoadOnce;

    public static SpokenEnglishPractiseFragment getInstance() {
        SpokenEnglishPractiseFragment fragment = new SpokenEnglishPractiseFragment();
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.spoken_english_practice_fragment, container, false);
        initSwipeRefresh(view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        avObjects = new ArrayList<AVObject>();
        category_lv = (RecyclerView) view.findViewById(R.id.studycategory_lv);
        mXFYSAD = new XFYSAD(getActivity(), ADUtil.SecondaryPage);
        mAdapter = new RcSpokenEndlishPracticeTypeListAdapter(mXFYSAD);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        category_lv.setLayoutManager(mLinearLayoutManager);
        category_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        category_lv.setAdapter(mAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isHasLoadOnce && isVisibleToUser){
            isHasLoadOnce = true;
            new QueryTask().execute();
        }
        if (isVisibleToUser) {
            if (mXFYSAD != null) {
                mXFYSAD.startPlayImg();
            }
        } else {
            if (mXFYSAD != null) {
                mXFYSAD.canclePlayImg();
            }
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
        super.onSwipeRefreshLayoutRefresh();
        new QueryTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            LogUtil.DefalutLog("spokenenglish");
        }

        @Override
        protected Void doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationType.EvaluationType);
            query.whereEqualTo(AVOUtil.EvaluationType.ETIsValid, "1");
            query.orderByAscending(AVOUtil.EvaluationType.ETOrder);
            try {
                List<AVObject> avObject = query.find();
                if (avObject != null) {
                    avObjects.clear();
                    avObjects.addAll(avObject);
                }
            } catch (AVException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            if(mAdapter.getHeader() == null){
                mAdapter.setHeader(new Object());
            }
            mAdapter.setItems(avObjects);
            mAdapter.notifyDataSetChanged();
        }

    }
}
