package com.messi.languagehelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLAVObjectModel;
import com.messi.languagehelper.adapter.RcMomentsListAdapter;
import com.messi.languagehelper.box.BoxHelper;
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
import butterknife.OnClick;

public class MomentsActivity extends BaseActivity {

    @BindView(R.id.moments_add)
    LinearLayout momentsAdd;
    @BindView(R.id.listview)
    RecyclerView listview;
    private RcMomentsListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLAVObjectModel mXXLModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moments_activity);
        ButterKnife.bind(this);
        initViews();
        new QueryTask().execute();
    }


    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_moments));
        avObjects = new ArrayList<AVObject>();
        mXXLModel = new XXLAVObjectModel(this);
        initSwipeRefresh();
        mAdapter = new RcMomentsListAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects, mAdapter);
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.padding_10)
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
                        new QueryTask().execute();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        if (avObjects.size() > 3) {
            for (int i = first; i < (first + vCount); i++) {
                if (i < avObjects.size() && i > 0) {
                    AVObject mAVObject = avObjects.get(i);
                    if (mAVObject != null && mAVObject.get(KeyUtil.ADKey) != null) {
                        if (!(Boolean) mAVObject.get(KeyUtil.ADIsShowKey)) {
                            NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.get(KeyUtil.ADKey);
                            boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i % vCount));
                            LogUtil.DefalutLog("isExposure:" + isExposure);
                            if (isExposure) {
                                mAVObject.put(KeyUtil.ADIsShowKey, isExposure);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        skip = 0;
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask().execute();
    }

    private void loadAD() {
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    @OnClick(R.id.moments_add)
    public void onClick() {
        Intent intent = new Intent(this,MomentsAddActivity.class);
        startActivityForResult(intent,20010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20010 && resultCode == RESULT_OK){
            onSwipeRefreshLayoutRefresh();
        }
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mXXLModel != null) {
                mXXLModel.loading = true;
            }
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Moments.Moments);
            query.addDescendingOrder(AVOUtil.Moments.createdAt);
            query.skip(skip);
            query.limit(Setings.page_size);
            try {
                return query.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            LogUtil.DefalutLog("onPostExecute---");
            mXXLModel.loading = false;
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            if (avObject != null) {
                if (avObject.size() == 0) {
                    ToastUtil.diaplayMesShort(MomentsActivity.this, "没有了！");
                    hideFooterview();
                } else {
                    if (avObjects != null && mAdapter != null) {
                        setLikeStatus(avObject);
                        avObjects.addAll(avObject);
                        mAdapter.notifyDataSetChanged();
                        loadAD();
                        skip += Setings.page_size;
                        showFooterview();
                    }
                }
            }
            if (avObject.size() < Setings.page_size) {
                mXXLModel.hasMore = false;
                hideFooterview();
            } else {
                mXXLModel.hasMore = true;
            }
        }
    }

    private void setLikeStatus(List<AVObject> avObjects){
        for (AVObject item : avObjects){
            item.put(KeyUtil.MomentLike, BoxHelper.isLike(item.getObjectId()));
        }
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mXXLModel != null) {
            mXXLModel.onDestroy();
        }
    }
}
