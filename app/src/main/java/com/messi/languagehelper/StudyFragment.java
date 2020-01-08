package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLModel;
import com.messi.languagehelper.adapter.RcStudyListAdapter;
import com.messi.languagehelper.bean.ReadingCategory;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.TablayoutOnSelectedListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StudyFragment extends BaseFragment implements TablayoutOnSelectedListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.search_btn)
    FrameLayout searchBtn;
    private int maxVideo = 20000;
    private RcStudyListAdapter mAdapter;
    private List<Reading> avObjects;
    private List<AVObject> tempList;
    private int skip = 0;
    private int maxRandom = 3000;
    private String category;
    private LinearLayoutManager mLinearLayoutManager;
    private List<ReadingCategory> categories;
    private boolean isNeedClear = true;
    private Reading xvideoItem;
    private XXLModel mXXLModel;

    public static StudyFragment getInstance() {
        return new StudyFragment();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            registerBroadcast();
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.study_fragment, null);
        ButterKnife.bind(this, view);
        initViews(view);
        loadData();
        getMaxPageNumberBackground();
        return view;
    }

    private void initViews(View view) {
        avObjects = new ArrayList<Reading>();
        mAdapter = new RcStudyListAdapter(avObjects);
        mXXLModel = new XXLModel(getActivity());
        initSwipeRefresh(view);
        avObjects.addAll(BoxHelper.getReadingList(0,Setings.page_size, "", "", ""));
        mXXLModel.setAdapter(avObjects,mAdapter);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        skip = 0;
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
        categories = getTabItem(getContext());
        initTablayout();
    }

    private void initTablayout() {
        for (ReadingCategory item : categories) {
            tablayout.addTab(tablayout.newTab().setText(item.getName()));
        }
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelectedListener(categories.get(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabReselectedListener(categories.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
        tablayout.getTabAt(0).select();
    }

    private void random() {
        skip = (int) Math.round(Math.random() * maxRandom);
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
                        QueryTask();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        try {
            if (avObjects.size() > 2) {
                for (int i = first; i < (first + vCount); i++) {
                    if (i < avObjects.size() && i > 0) {
                        Reading mAVObject = avObjects.get(i);
                        if (mAVObject != null && mAVObject.isAd()) {
                            if (!mAVObject.isAdShow() && mAVObject.getmNativeADDataRef() != null) {
                                NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                                boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i % vCount));
                                LogUtil.DefalutLog("isExposure:" + isExposure);
                                if(isExposure){
                                    mAVObject.setAdShow(isExposure);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void loadData() {
        skip = 0;
        QueryTask();
//        XVideoAsyncTask();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        isNeedClear = true;
        random();
        refresh();
    }

    private void refresh() {
        hideFooterview();
        QueryTask();
//        XVideoAsyncTask();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private void QueryTask() {
        mXXLModel.loading = true;
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
        if(!TextUtils.isEmpty(category)) {
            if(category.equals("video")) {
                query.whereEqualTo(AVOUtil.Reading.type, "video");
                skip = (int) Math.round(Math.random() * maxVideo);
            }else {
                query.whereEqualTo(AVOUtil.Reading.category, category);
            }
        }else {
            query.whereNotEqualTo(AVOUtil.Reading.type, "video");
        }
        query.addDescendingOrder(AVOUtil.Reading.publish_time);
        query.skip(skip);
        query.limit(Setings.page_size);
        try {
            tempList = query.find();
            LogUtil.DefalutLog("skip:" + skip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFinishLoadData() {
        mXXLModel.loading = false;
        mXXLModel.hasMore = true;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
        if (tempList != null) {
            if (tempList.size() == 0) {
                ToastUtil.diaplayMesShort(getContext(), "没有了！");
                hideFooterview();
                mXXLModel.hasMore = false;
            } else {
                if (avObjects != null && mAdapter != null) {
                    if (isNeedClear || skip == 0) {
                        isNeedClear = false;
                        avObjects.clear();
                    }
                    loadAD();
                    changeData(tempList, avObjects,false);
//                    if(xvideoItem != null){
//                        if(avObjects.size() > 9){
//                            avObjects.add(NumberUtil.randomNumberRange(3,6),xvideoItem);
//                        }else {
//                            avObjects.add(0,xvideoItem);
//                        }
//                        xvideoItem = null;
//                    }
                    mAdapter.notifyDataSetChanged();
                    skip += Setings.page_size;
                    showFooterview();
                }
            }
        }
    }

    @Override
    public void onTabSelectedListener(ReadingCategory mReadingCategory) {
        isNeedClear = true;
        listview.scrollToPosition(0);
        category = mReadingCategory.getCategory();
        skip = 0;
        refresh();
    }

    @Override
    public void onTabReselectedListener(ReadingCategory mReadingCategory) {
        listview.scrollToPosition(1);
        onSwipeRefreshLayoutRefresh();
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    public void onTabReselected(int index) {
        listview.scrollToPosition(0);
        onSwipeRefreshLayoutRefresh();
    }

    public static void changeData(List<AVObject> avObjectlist, List<Reading> avObjects, boolean isAddToHead) {
        for (AVObject item : avObjectlist) {
            Reading mReading = new Reading();
            mReading.setObject_id(item.getObjectId());
            if(item.has(AVOUtil.Reading.category)){
                mReading.setCategory(item.getString(AVOUtil.Reading.category));
            }
            if(item.has(AVOUtil.Reading.content)){
                mReading.setContent(item.getString(AVOUtil.Reading.content));
            }
            if(item.has(AVOUtil.Reading.type_id)){
                mReading.setType_id(item.getString(AVOUtil.Reading.type_id));
            }
            if(item.has(AVOUtil.Reading.type_name)){
                mReading.setType_name(item.getString(AVOUtil.Reading.type_name));
            }
            if(item.has(AVOUtil.Reading.title)){
                mReading.setTitle(item.getString(AVOUtil.Reading.title));
            }
            if(item.has(AVOUtil.Reading.item_id)){
                mReading.setItem_id(String.valueOf(item.getNumber(AVOUtil.Reading.item_id)));
            }
            if(item.has(AVOUtil.Reading.img_url)){
                mReading.setImg_url(item.getString(AVOUtil.Reading.img_url));
            }
            if(item.has(AVOUtil.Reading.publish_time)){
                mReading.setPublish_time(String.valueOf(item.getDate(AVOUtil.Reading.publish_time).getTime()));
            }
            if(item.has(AVOUtil.Reading.img_type)){
                mReading.setImg_type(item.getString(AVOUtil.Reading.img_type));
            }
            if(item.has(AVOUtil.Reading.source_name)){
                mReading.setSource_name(item.getString(AVOUtil.Reading.source_name));
            }
            if(item.has(AVOUtil.Reading.source_url)){
                mReading.setSource_url(item.getString(AVOUtil.Reading.source_url));
            }
            if(item.has(AVOUtil.Reading.type)){
                mReading.setType(item.getString(AVOUtil.Reading.type));
            }
            if(item.has(AVOUtil.Reading.media_url)){
                mReading.setMedia_url(item.getString(AVOUtil.Reading.media_url));
            }
            if(item.has(AVOUtil.Reading.content_type)){
                mReading.setContent_type(item.getString(AVOUtil.Reading.content_type));
            }
            if(item.has(AVOUtil.Reading.lrc_url)){
                mReading.setLrc_url(item.getString(AVOUtil.Reading.lrc_url));
            }
            BoxHelper.saveOrGetStatus(mReading);
            if (isAddToHead) {
                avObjects.add(0, mReading);
            } else {
                avObjects.add(mReading);
            }
        }
    }

    public static List<ReadingCategory> getTabItem(Context context) {
        List<ReadingCategory> readingCategories = new ArrayList<ReadingCategory>();
        readingCategories.add(new ReadingCategory(context.getString(R.string.recommend), ""));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_video), "video"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_listening), "listening"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.spoken_english_practice), "spoken_english"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.reading), "shuangyu_reading"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_word_study_vocabulary), "word"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_composition), "composition"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.examination), "examination"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_story), "story"));
        readingCategories.add(new ReadingCategory(context.getString(R.string.title_english_jokes), "jokes"));
        return readingCategories;
    }

    @OnClick(R.id.search_btn)
    public void onViewClicked() {
        toActivity(SearchActivity.class,null);
        AVAnalytics.onEvent(getContext(), "tab4_to_search");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }

    private void XVideoAsyncTask() {
//        getXVideoAsyncTask();
    }

    private void getXVideoAsyncTask(){
        int random = (int) Math.round(Math.random() * 6000);
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XVideo.XVideo);
        query.whereEqualTo(AVOUtil.XVideo.category,"english");
        query.orderByDescending(AVOUtil.XVideo.play_count);
        query.skip(random);
        query.limit(6);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list != null){
                    xvideoItem = new Reading();
                    xvideoItem.setXvideoList(list);
                }
            }
        });
    }

    private void getMaxPageNumberBackground(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Reading.Reading);
                    query.whereEqualTo(AVOUtil.Reading.type, "video");
                    query.addDescendingOrder(AVOUtil.Reading.publish_time);
                    maxVideo = query.count()-300;
                    LogUtil.DefalutLog("maxVideo:"+maxVideo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
