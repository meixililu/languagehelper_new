package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLAVObjectModel;
import com.messi.languagehelper.adapter.RcSubjectListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ColorUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SubjectFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 1;
    @BindView(R.id.studycategory_lv)
    RecyclerView category_lv;
    Unbinder unbinder;
    private RcSubjectListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private GridLayoutManager layoutManager;
    private String category;
    private String recentKey;
    private String level;
    private String order;
    private XXLAVObjectModel mXXLModel;


    public static SubjectFragment getInstance(String category, String recentKey, String level) {
        SubjectFragment fragment = new SubjectFragment();
        fragment.category = category;
        fragment.recentKey = recentKey;
        fragment.level = level;
        return fragment;
    }

    public static SubjectFragment getInstance(String category, String recentKey,
                                              String level, String order) {
        SubjectFragment fragment = new SubjectFragment();
        fragment.category = category;
        fragment.recentKey = recentKey;
        fragment.level = level;
        fragment.order = order;
        return fragment;
    }

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
    public void loadDataOnStart() {
        super.loadDataOnStart();
        new QueryTask(this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.symbol_list_fragment, container, false);
        initSwipeRefresh(view);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        avObjects = new ArrayList<AVObject>();
        mXXLModel = new XXLAVObjectModel(getActivity());
    }

    private void initAdapter(){
        if(mAdapter == null && category_lv != null){
            mAdapter = new RcSubjectListAdapter();
            mAdapter.setItems(avObjects);
            mAdapter.setFooter(new Object());
            mXXLModel.setAdapter(avObjects,mAdapter);
            hideFooterview();
            layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
            HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
            layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
            category_lv.setLayoutManager(layoutManager);
            category_lv.addItemDecoration(new DividerGridItemDecoration(1));
            category_lv.setAdapter(mAdapter);
            setListOnScrollListener();
        }
    }


    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (!mXXLModel.loading && mXXLModel.hasMore && isHasLoadData) {
                    if ((visible + firstVisibleItem) >= total) {
                        new QueryTask(SubjectFragment.this).execute();
                    }
                }
            }
        });
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        skip = 0;
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask(this).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                toMoreActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toMoreActivity() {
        toActivity(SearchActivity.class, null);
        AVAnalytics.onEvent(getContext(), "subject_to_search");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        private WeakReference<SubjectFragment> mainActivity;

        public QueryTask(SubjectFragment mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mXXLModel != null){
                mXXLModel.loading = true;
            }
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectList.SubjectList);
            if (!TextUtils.isEmpty(category)) {
                query.whereEqualTo(AVOUtil.SubjectList.category, category);
            }
            if (!TextUtils.isEmpty(level)) {
                query.whereEqualTo(AVOUtil.SubjectList.level, level);
            }
            if(!TextUtils.isEmpty(order)){
                query.orderByDescending(AVOUtil.SubjectList.order);
            }else {
                query.orderByAscending(AVOUtil.SubjectList.order);
            }
            query.skip(skip);
            query.limit(Setings.page_size);
            try {
                return query.find();
            } catch (AVException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null){
                mXXLModel.loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                initAdapter();
                if (avObject != null) {
                    if (avObject.size() == 0) {
                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        mXXLModel.hasMore = false;
                        hideFooterview();
                    } else {
                        if (avObjects != null && mAdapter != null) {
                            addBgColor(avObject);
                            avObjects.addAll(avObject);
                            if(avObject.size() == Setings.page_size){
                                skip += Setings.page_size;
                                showFooterview();
                                mXXLModel.hasMore = true;
                            }else {
                                mXXLModel.hasMore = false;
                                hideFooterview();
                            }
                        }
                    }
                }
                if(mAdapter != null){
                    mAdapter.notifyDataSetChanged();
                }
                loadAD();
            }
        }
    }

    private void addBgColor(List<AVObject> avObject){
        for (AVObject item : avObject){
            item.put(KeyUtil.ColorKey, ColorUtil.getRadomColor());
        }
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
