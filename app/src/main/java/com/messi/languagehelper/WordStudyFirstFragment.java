package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcWordListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WordStudyFirstFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView category_lv;
    private RcWordListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;

    @Override
    public void onAttach(Context activity) {
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
        new QueryTask(this).execute();
        return view;
    }

    private void initViews(View view) {
        avObjects = new ArrayList<AVObject>();
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        mXFYSAD = new XFYSAD(getActivity(), ADUtil.SecondaryPage);
        mAdapter = new RcWordListAdapter(mXFYSAD);
        category_lv.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setHeader(new Object());
        mAdapter.setItems(avObjects);
        mXFYSAD.setAdapter(mAdapter);
        category_lv.setAdapter(mAdapter);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        new QueryTask(this).execute();
    }

    private class QueryTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<WordStudyFirstFragment> mainActivity;

        public QueryTask(WordStudyFirstFragment mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(mainActivity.get() != null){
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
