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
import com.messi.languagehelper.adapter.RcReadingListAdapter;
import com.messi.languagehelper.adapter.RcStudyListAdapter;
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
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StudyFragment extends BaseFragment implements OnClickListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    private RcStudyListAdapter mAdapter;
    private List<AVObject> avObjects;
    private List<AVObject> tempList;
    private int skip = 0;
    private int maxRandom;
    private IFLYNativeAd nativeAd;
    private boolean loading;
    private boolean hasMore;
    private AVObject mADObject;
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
        avObjects = new ArrayList<AVObject>();
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
                    AVObject mAVObject = avObjects.get(i);
                    if (mAVObject != null && mAVObject.get(KeyUtil.ADKey) != null) {
                        if (!(Boolean) mAVObject.get(KeyUtil.ADIsShowKey) && misVisibleToUser) {
                            NativeADDataRef mNativeADDataRef = (NativeADDataRef) mAVObject.get(KeyUtil.ADKey);
                            mNativeADDataRef.onExposured(view.getChildAt(i % vCount));
                            mAVObject.put(KeyUtil.ADIsShowKey, true);
                        }
                    }
                }
            }
        }
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
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                getNetworkData();
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        onFinishLoadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
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
                    avObjects.addAll(tempList);
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
                    mADObject = new AVObject();
                    mADObject.put(KeyUtil.ADKey, nad);
                    mADObject.put(KeyUtil.ADIsShowKey, false);
                    if (!loading) {
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


}
