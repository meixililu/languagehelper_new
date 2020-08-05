package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcSubjectTypeListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class SubjectByTypeFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 1;
    @BindView(R.id.studycategory_lv)
    RecyclerView category_lv;
    Unbinder unbinder;
    private RcSubjectTypeListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;
    private GridLayoutManager layoutManager;
    private String category;
    private String recentKey;

    public static SubjectByTypeFragment getInstance(String category, String recentKey) {
        SubjectByTypeFragment fragment = new SubjectByTypeFragment();
        fragment.category = category;
        fragment.recentKey = recentKey;
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
        mXFYSAD = new XFYSAD(getActivity(), ADUtil.SecondaryPage);
    }

    private void initAdapter(){
        try {
            if(mAdapter == null){
                mAdapter = new RcSubjectTypeListAdapter(mXFYSAD, recentKey);
                mAdapter.setItems(avObjects);
                mAdapter.setHeader(new Object());
                mAdapter.setFooter(new Object());
                mXFYSAD.setAdapter(mAdapter);
                hideFooterview();
                layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
                HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
                layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
                category_lv.setLayoutManager(layoutManager);
                category_lv.addItemDecoration(new DividerGridItemDecoration(1));
                category_lv.setAdapter(mAdapter);
                setListOnScrollListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                if (!loading && hasMore && isHasLoadData) {
                    if ((visible + firstVisibleItem) >= total) {
                        new QueryTask(SubjectByTypeFragment.this).execute();
                    }
                }
            }
        });
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(mXFYSAD != null){
            mXFYSAD.onDestroy();
        }
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        private WeakReference<SubjectByTypeFragment> mainActivity;

        public QueryTask(SubjectByTypeFragment mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SubjectType.SubjectType);
            if (!TextUtils.isEmpty(category)) {
                query.whereEqualTo(AVOUtil.SubjectType.type_code, category);
            }
            query.orderByAscending(AVOUtil.SubjectType.order);
            query.skip(skip);
            query.limit(20);
            try {
                return query.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null){
                loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                initAdapter();
                if (avObject != null) {
                    if (avObject.size() == 0) {
                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        hasMore = false;
                        hideFooterview();
                    } else {
                        if (avObjects != null && mAdapter != null) {
                            avObjects.addAll(avObject);
                            if(avObject.size() == 20){
                                skip += 20;
                                showFooterview();
                                hasMore = true;
                            }else {
                                hasMore = false;
                                hideFooterview();
                            }
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }
}
