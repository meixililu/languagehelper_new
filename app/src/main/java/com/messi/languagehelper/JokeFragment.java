package com.messi.languagehelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcJokeListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class JokeFragment extends BaseFragment implements OnClickListener {

    private RecyclerView listview;
    private RcJokeListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private String category;
    private IFLYNativeAd nativeAd;
    private int maxRandom;
    private boolean loading;
    private boolean hasMore = true;
    private AVObject mADObject;
    private LinearLayoutManager mLinearLayoutManager;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        JZVideoPlayer.releaseAllVideos();
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
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.joke_picture_fragment, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        loadAD();
        new QueryTask().execute();
        getMaxPageNumberBackground();
    }

    private void initViews(View view) {
        avObjects = new ArrayList<AVObject>();
        listview = (RecyclerView) view.findViewById(R.id.listview);
        initSwipeRefresh(view);
        mAdapter = new RcJokeListAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
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
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        loadAD();
                        new QueryTask().execute();
                    }
                }
                isADInList(recyclerView, firstVisibleItem, visible);
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
                            NativeADDataRef mNativeADDataRef = (NativeADDataRef) mAVObject.get(KeyUtil.ADKey);
                            boolean isExposure = mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
                            LogUtil.DefalutLog("isExposure:"+isExposure);
                            mAVObject.put(KeyUtil.ADIsShowKey, isExposure);
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
        loadAD();
        hideFooterview();
        random();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask().execute();
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
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
            query.limit(Settings.page_size);
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
            loading = false;
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            if(avObject != null){
                if(avObject.size() == 0){
                    ToastUtil.diaplayMesShort(getContext(), "没有了！");
                    hideFooterview();
                }else{
                    if(avObjects != null && mAdapter != null){
                        avObjects.addAll(avObject);
                        if(addAD()){
                            mAdapter.notifyDataSetChanged();
                        }
                        skip += Settings.page_size;
                        showFooterview();
                    }
                }
            }
            if(skip == maxRandom){
                hasMore = false;
            }
        }
    }

    private void loadAD(){
        nativeAd = new IFLYNativeAd(getContext(), ADUtil.XXLAD, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
            }
            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if(adList != null && adList.size() > 0){
                    NativeADDataRef nad = adList.get(0);
                    mADObject = new AVObject();
                    mADObject.put(KeyUtil.ADKey, nad);
                    mADObject.put(KeyUtil.ADIsShowKey, false);
                    if(!loading){
                        addAD();
                    }
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private boolean addAD(){
        if(mADObject != null && avObjects != null && avObjects.size() > 0){
            int index = avObjects.size() - Settings.page_size + NumberUtil.randomNumberRange(2, 4);
            if(index < 0){
                index = 0;
            }
            avObjects.add(index,mADObject);
            mAdapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        }else{
            return true;
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
                    maxRandom =  query.count() / Settings.page_size;
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

}
