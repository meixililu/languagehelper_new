package com.messi.languagehelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.messi.languagehelper.adapter.RcStudyListAdapter;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.db.DataBaseUtil;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StudyFragment extends BaseFragment implements OnClickListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    private RcStudyListAdapter mAdapter;
    private List<Reading> avObjects;
    private List<AVObject> tempList;
    private int skip = 0;
    private int maxRandom;
    private IFLYNativeAd nativeAd;
    private boolean loading;
    private boolean hasMore;
    private Reading mADObject;
    private LinearLayoutManager mLinearLayoutManager;

    public static StudyFragment getInstance() {
        return new StudyFragment();
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
        View view = inflater.inflate(R.layout.study_fragment, null);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        avObjects = new ArrayList<Reading>();
        avObjects.addAll(DataBaseUtil.getInstance().getReadingList(Settings.page_size,"","",""));
        skip = 0;
        initSwipeRefresh(view);
        mAdapter = new RcStudyListAdapter(avObjects,mProgressbarListener,getActivity());
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        mAdapter.setHeader(new Object());
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

    private void random() {
        skip = (int) Math.round(Math.random() * maxRandom);
        LogUtil.DefalutLog("skip:" + skip);
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
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        loadAD();
                        QueryTask();
                        LogUtil.DefalutLog("StudyFragment-setListOnScrollListener-QueryTask");
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
                    if(mAVObject != null && mAVObject.isAd()){
                        if(!mAVObject.isAdShow()){
                            NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                            mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
                            mAVObject.setAdShow(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            JCVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        skip = 0;
        loadAD();
        QueryTask();
        LogUtil.DefalutLog("StudyFragment-loadDataOnStart-QueryTask");
        getMaxPageNumberBackground();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        loadAD();
        hideFooterview();
        random();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        QueryTask();
        LogUtil.DefalutLog("StudyFragment-onSwipeRefreshLayoutRefresh-QueryTask");
    }

    private void QueryTask() {
        loading = true;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                getNetworkData();
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(String s) {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        onFinishLoadData();
                    }
                });

    }

    private void getNetworkData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
        query.addDescendingOrder(AVOUtil.Reading.publish_time);
        query.skip(skip);
        query.limit(Settings.page_size);
        try {
            tempList = query.find();
            LogUtil.DefalutLog("skip:" + skip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFinishLoadData() {
        loading = false;
        hasMore = true;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
        if (tempList != null) {
            if (tempList.size() == 0) {
                ToastUtil.diaplayMesShort(getContext(), "没有了！");
                hideFooterview();
                hasMore = false;
            } else {
                if (avObjects != null && mAdapter != null) {
                    if(skip == 0){
                        avObjects.clear();
                    }
                    changeData(tempList);
                    if (addAD()) {
                        mAdapter.notifyDataSetChanged();
                    }
                    skip += Settings.page_size;
                    showFooterview();
                }
            }
        }
    }

    private void loadAD() {
        nativeAd = new IFLYNativeAd(getContext(), ADUtil.XXLAD, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if (adList != null && adList.size() > 0) {
                    NativeADDataRef nad = adList.get(0);
                    mADObject = new Reading();
                    mADObject.setmNativeADDataRef(nad);
                    mADObject.setAd(true);
                    if(!loading){
                        addAD();
                    }
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private boolean addAD() {
        if (mADObject != null && avObjects != null && avObjects.size() > 0) {
            int index = avObjects.size() - Settings.page_size + NumberUtil.randomNumberRange(2, 4);
            if (index < 0) {
                index = 0;
            }
            avObjects.add(index, mADObject);
            mAdapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
    }

    private void getMaxPageNumberBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                maxRandom = (int)((Math.random()*800));
            }
        }).start();
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    public void onTabReselected(int index) {
        if(index == 2){
            listview.scrollToPosition(0);
        }
    }

    private void changeData(List<AVObject> avObjectlist){
        for (AVObject item : avObjectlist) {
            Reading mReading = new Reading();
            mReading.setObject_id(item.getObjectId());
            mReading.setCategory(item.getString(AVOUtil.Reading.category));
            mReading.setContent(item.getString(AVOUtil.Reading.content));
            mReading.setType_id(item.getString(AVOUtil.Reading.type_id));
            mReading.setType_name(item.getString(AVOUtil.Reading.type_name));
            mReading.setTitle(item.getString(AVOUtil.Reading.title));
            mReading.setItem_id(item.getString(AVOUtil.Reading.item_id));
            mReading.setImg_url(item.getString(AVOUtil.Reading.img_url));
            mReading.setPublish_time(item.getString(AVOUtil.Reading.publish_time));
            mReading.setImg_type(item.getString(AVOUtil.Reading.img_type));
            mReading.setSource_name(item.getString(AVOUtil.Reading.source_name));
            mReading.setSource_url(item.getString(AVOUtil.Reading.source_url));
            mReading.setType(item.getString(AVOUtil.Reading.type));
            mReading.setMedia_url(item.getString(AVOUtil.Reading.media_url));
            mReading.setContent_type(item.getString(AVOUtil.Reading.content_type));
            DataBaseUtil.getInstance().saveOrGetStatus(mReading);
            avObjects.add(mReading);
        }
    }
}
