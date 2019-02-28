package com.messi.languagehelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcSubjectReadingListAdapter;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReadingsBySubjectFragment extends BaseFragment {

    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    Unbinder unbinder;
    private RcSubjectReadingListAdapter mAdapter;
    private List<Reading> avObjects;
    private int lastTimeReadItemId;
    private boolean isLookUpData = false;
    private int skip = 0;
    private int skipUp = 0;
    private String subjectName;
    private String level;
    private String recentKey;
    private boolean hasMoreUp = true;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLModel mXXLModel;

    public static Fragment newInstance(String category_2, String recentKey, String level) {
        ReadingsBySubjectFragment fragment = new ReadingsBySubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.SubjectName, category_2);
        bundle.putString(KeyUtil.RecentKey, recentKey);
        if (!TextUtils.isEmpty(level)) {
            bundle.putString(KeyUtil.LevelKey, level);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
        Bundle mBundle = getArguments();
        subjectName = mBundle.getString(KeyUtil.SubjectName);
        level = mBundle.getString(KeyUtil.LevelKey);
        recentKey = mBundle.getString(KeyUtil.RecentKey);
        lastTimeReadItemId = Setings.getSharedPreferences(getContext()).getInt(
                recentKey+KeyUtil.SubjectLastTimeReadItemId,
                0
        );
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
    public void loadDataOnStart() {
        super.loadDataOnStart();
        if(TextUtils.isEmpty(subjectName)){
            if(emptyTv != null){
                emptyTv.setVisibility(View.VISIBLE);
            }
        }else {
            if(emptyTv != null){
                emptyTv.setVisibility(View.GONE);
            }
            new QueryTask().execute();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isHasLoadData) {
            String newSubject = Setings.getSharedPreferences(getContext()).
                    getString(recentKey, "");
            if (!subjectName.equals(newSubject)) {
                subjectName = newSubject;
                skip = 0;
                avObjects.clear();
                hideFooterview();
                mAdapter.notifyDataSetChanged();
                loadDataByType(false);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_reading_subject_list, container, false);
        initSwipeRefresh(view);
        initViews(view);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initViews(View view) {
        avObjects = new ArrayList<Reading>();
        mXXLModel = new XXLModel(getActivity());
        listview = (RecyclerView) view.findViewById(R.id.listview);
        mAdapter = new RcSubjectReadingListAdapter(avObjects,recentKey,true);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects,mAdapter);
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                isADInList(recyclerView, firstVisibleItem, visible);
                if (!mXXLModel.loading && mXXLModel.hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        loadDataByType(false);
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        if (avObjects.size() > 3) {
            for (int i = first; i < (first + vCount); i++) {
                if (i < avObjects.size() && i > 0) {
                    Reading mAVObject = avObjects.get(i);
                    if (mAVObject != null && mAVObject.isAd()) {
                        if (!mAVObject.isAdShow()) {
                            NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                            boolean isShow = mNativeADDataRef.onExposured(view.getChildAt(i % vCount));
                            LogUtil.DefalutLog("onExposured:" + isShow);
                            if(isShow){
                                mAVObject.setAdShow(isShow);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateUI(String music_action) {
        if (music_action.equals(PlayerService.action_loading)) {
            showProgressbar();
        } else if (music_action.equals(PlayerService.action_finish_loading)) {
            hideProgressbar();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        emptyTv.setVisibility(View.GONE);
        if(avObjects.size() == 0) {
            loadDataByType(false);
        }else {
            if(hasMoreUp){
                loadDataByType(true);
            }else {
                onSwipeRefreshLayoutFinish();
            }
        }
    }

    private void loadDataByType(boolean type){
        isLookUpData = type;
        new QueryTask().execute();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
            if(mXXLModel != null){
                mXXLModel.loading = true;
            }
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
            query.whereEqualTo(AVOUtil.Reading.category_2, subjectName);
            if (!TextUtils.isEmpty(level)) {
                query.whereEqualTo(AVOUtil.Reading.level, level);
            }
            query.limit(Setings.page_size);
            if(isLookUpData){
                query.whereLessThan(AVOUtil.Reading.item_id,lastTimeReadItemId);
                query.addDescendingOrder(AVOUtil.Reading.item_id);
                query.skip(skipUp);
            }else {
                query.whereGreaterThanOrEqualTo(AVOUtil.Reading.item_id,lastTimeReadItemId);
                query.addAscendingOrder(AVOUtil.Reading.item_id);
                query.skip(skip);
            }
            try {
                return query.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            emptyTv.setVisibility(View.GONE);
            mXXLModel.loading = false;
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            if (avObject != null) {
                if (avObject.size() == 0) {
                    ToastUtil.diaplayMesShort(getContext(), "没有了！");
                    if(!isLookUpData){
                        mXXLModel.hasMore = false;
                        hideFooterview();
                    }else {
                        hasMoreUp = false;
                    }
                } else {
                    StudyFragment.changeData(avObject, avObjects, isLookUpData);
                    mAdapter.notifyDataSetChanged();
                    loadAD();
                    if(avObject.size() == Setings.page_size){
                        if(isLookUpData){
                            hasMoreUp = true;
                            skipUp += Setings.page_size;
                        }else {
                            mXXLModel.hasMore = true;
                            skip += Setings.page_size;
                            showFooterview();
                        }
                    }else {
                        if(isLookUpData){
                            hasMoreUp = false;
                        }else {
                            mXXLModel.hasMore = false;
                            hideFooterview();
                        }
                    }

                }
            } else {
                ToastUtil.diaplayMesShort(getContext(), "加载失败，下拉可刷新");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unregisterBroadcast();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }

}
