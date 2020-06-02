package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLAVObjectModel;
import com.messi.languagehelper.adapter.RcJokeListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class JokeFragment extends BaseFragment implements OnClickListener {

    private RecyclerView listview;
    private RcJokeListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private String category;
    private int maxRandom;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLAVObjectModel mXXLModel;

    public static JokeFragment newInstance(String category){
        JokeFragment fragment = new JokeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category",category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.category = mBundle.getString("category");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.joke_picture_fragment, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        new QueryTask(this).execute();
        getMaxPageNumberBackground();
    }

    private void initViews(View view) {
        avObjects = new ArrayList<AVObject>();
        mXXLModel = new XXLAVObjectModel(getActivity());
        listview = (RecyclerView) view.findViewById(R.id.listview);
        initSwipeRefresh(view);
        mAdapter = new RcJokeListAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mXXLModel.setAdapter(avObjects,mAdapter);
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                        new HorizontalDividerItemDecoration.Builder(getContext())
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
                        new QueryTask(JokeFragment.this).execute();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view,int first, int vCount){
        if(avObjects.size() > 3){
            for(int i=first;i< (first+vCount);i++){
                if(i < avObjects.size() && i > 0){
                    AVObject mAVObject = avObjects.get(i);
                    if(mAVObject != null && mAVObject.get(KeyUtil.ADKey) != null){
                        if(!(Boolean) mAVObject.get(KeyUtil.ADIsShowKey) && misVisibleToUser){
                            NativeDataRef mNativeADDataRef = (NativeDataRef) mAVObject.get(KeyUtil.ADKey);
                            boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
                            LogUtil.DefalutLog("isExposure:"+isExposure);
                            if(isExposure){
                                mAVObject.put(KeyUtil.ADIsShowKey, isExposure);
                            }
                        }
                    }
                }
            }
        }
    }

    private void random(){
        skip = (int) Math.round(Math.random()*maxRandom);
        LogUtil.DefalutLog("skip:"+skip);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        random();
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

        private WeakReference<JokeFragment> mainActivity;

        public QueryTask(JokeFragment mActivity){
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
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Joke.Joke);
            if(!TextUtils.isEmpty(category)){
                if(category.equals("img")){
                    query.whereNotEqualTo(AVOUtil.Joke.category,"103");
                    query.whereEqualTo(AVOUtil.Joke.type, "1");
                    query.whereEqualTo(AVOUtil.Joke.type, "3");
                }else if(category.equals("video")){
                    query.whereNotEqualTo(AVOUtil.Joke.category,"103");
                    query.whereEqualTo(AVOUtil.Joke.type, "4");
                }else if(category.equals("103")){
                    query.whereEqualTo(AVOUtil.Joke.category,"103");
                }
            }else {
                query.whereNotEqualTo(AVOUtil.Joke.category,"103");
            }
            query.addDescendingOrder(AVOUtil.Joke.createdAt);
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
                LogUtil.DefalutLog("onPostExecute---");
                mXXLModel.loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if(avObject != null){
                    if(avObject.size() == 0){
                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        hideFooterview();
                    }else{
                        if(avObjects != null && mAdapter != null){
                            avObjects.addAll(avObject);
                            mAdapter.notifyDataSetChanged();
                            loadAD();
                            skip += Setings.page_size;
                            showFooterview();
                        }
                    }
                }
                if(skip == maxRandom){
                    mXXLModel.hasMore = false;
                }else {
                    mXXLModel.hasMore = true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void getMaxPageNumberBackground(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Joke.Joke);
                    if(!TextUtils.isEmpty(category)){
                        if(category.equals("img")){
                            query.whereNotEqualTo(AVOUtil.Joke.category,"103");
                            query.whereEqualTo(AVOUtil.Joke.type, "1");
                            query.whereEqualTo(AVOUtil.Joke.type, "3");
                        }else if(category.equals("video")){
                            query.whereNotEqualTo(AVOUtil.Joke.category,"103");
                            query.whereEqualTo(AVOUtil.Joke.type, "4");
                        }else if(category.equals("103")){
                            query.whereEqualTo(AVOUtil.Joke.category,"103");
                        }
                    }else {
                        query.whereNotEqualTo(AVOUtil.Joke.category,"103");
                    }
                    maxRandom =  query.count() / Setings.page_size;
                    LogUtil.DefalutLog("category:"+category+"---maxRandom:"+maxRandom);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onTabReselected(int index) {
        listview.scrollToPosition(0);
        onSwipeRefreshLayoutRefresh();
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
