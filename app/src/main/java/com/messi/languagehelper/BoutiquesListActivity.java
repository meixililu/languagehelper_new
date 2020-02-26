package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DataUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class BoutiquesListActivity extends BaseActivity {

    private RecyclerView listview;
    private RcReadingListAdapter mAdapter;
    private List<Reading> avObjects;
    private int skip = 0;
    private String bcdoe;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLModel mXXLModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutiques_list_activity);
        initViews();
        new QueryTask(this).execute();
    }

    private void initViews() {
        this.bcdoe = getIntent().getStringExtra(KeyUtil.BoutiqueCode);
        avObjects = new ArrayList<Reading>();
        mXXLModel = new XXLModel(this);
        listview = (RecyclerView) findViewById(R.id.listview);
        initSwipeRefresh();
        mAdapter = new RcReadingListAdapter(avObjects);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects,mAdapter);
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                        new HorizontalDividerItemDecoration.Builder(this)
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
                        new QueryTask(BoutiquesListActivity.this).execute();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view,int first, int vCount){
        if(avObjects.size() > 3){
            for(int i=first;i< (first+vCount);i++){
                if(i < avObjects.size() && i > 0){
                    Reading mAVObject = avObjects.get(i);
                    if(mAVObject != null && mAVObject.isAd()){
                        if(!mAVObject.isAdShow()){
                            NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                            boolean isShow = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
                            LogUtil.DefalutLog("onExposure:"+isShow);
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
    public void onSwipeRefreshLayoutRefresh() {
        Jzvd.releaseAllVideos();
        hideFooterview();
        skip = 0;
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask(this).execute();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        private WeakReference<BoutiquesListActivity> mainActivity;

        public QueryTask(BoutiquesListActivity mActivity){
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
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.BoutiquesList.BoutiquesList);
            query.whereEqualTo(AVOUtil.BoutiquesList.bcdoe, bcdoe);
            query.addAscendingOrder(AVOUtil.BoutiquesList.order);
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
            if(mainActivity.get() != null){
                mXXLModel.loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if(avObject != null){
                    if(avObject.size() == 0){
                        hideFooterview();
                        mXXLModel.hasMore = false;
                    }else{
                        if(avObjects != null && mAdapter != null){
                            DataUtil.changeBoutiquesListToReading(avObject,avObjects,false);
                            mAdapter.notifyDataSetChanged();
                            loadAD();
                            skip += Setings.page_size;
                            if(avObject.size() < Setings.page_size){
                                mXXLModel.hasMore = false;
                                hideFooterview();
                            }else {
                                mXXLModel.hasMore = true;
                                showFooterview();
                            }
                        }
                    }
                }
            }
        }
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
