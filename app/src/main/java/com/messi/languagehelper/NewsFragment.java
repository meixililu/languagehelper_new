package com.messi.languagehelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.adapter.RcNewsAdapter;
import com.messi.languagehelper.bean.TXNewsItem;
import com.messi.languagehelper.bean.TXNewsResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements OnClickListener {

    public static final String social = "social/";
    public static final String guonei = "guonei/";
    public static final String yule = "huabian/";
    public static final String qiwen = "qiwen/";
    public static final String tiyu = "tiyu/";
    public static final String keji = "keji/";
    public static final String guoji = "world/";
    public static final String health = "health/";
    private RecyclerView listview;
    private RcNewsAdapter mAdapter;
    private List<TXNewsItem> mWechatJXItem;
    private int currentPage = 1;
    private String type;
    private String title;
    private Activity mContext;
    private boolean loading;
    private boolean hasMore = true;
    private IFLYNativeAd nativeAd;
    private TXNewsItem mADObject;
    private LinearLayoutManager mLinearLayoutManager;

    public static NewsFragment getInstance(String type, String title, Activity mContext) {
        NewsFragment fragment = new NewsFragment();
        fragment.setType(type);
        fragment.setTitle(title);
        fragment.setmContext(mContext);
        return fragment;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
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
        loadAD();
        requestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.joke_picture_fragment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mWechatJXItem = new ArrayList<TXNewsItem>();
        listview = (RecyclerView) view.findViewById(R.id.listview);
        initSwipeRefresh(view);
        mAdapter = new RcNewsAdapter();
        mAdapter.setItems(mWechatJXItem);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
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
                        requestData();
                    }
                }
                isADInList(recyclerView, firstVisibleItem, visible);
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        try {
            if (mWechatJXItem.size() > 2) {
                for (int i = first; i < (first + vCount); i++) {
                    if (i < mWechatJXItem.size() && i > 0) {
                        TXNewsItem mAVObject = mWechatJXItem.get(i);
                        if (mAVObject != null && mAVObject.getmNativeADDataRef() != null) {
                            if (!mAVObject.isHasShowAD()) {
                                NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                                boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i % vCount));
                                LogUtil.DefalutLog("isExposure:" + isExposure);
                                if(isExposure){
                                    mAVObject.setHasShowAD(isExposure);
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

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        currentPage = 1;
        loadAD();
        requestData();
    }

    private void requestData() {
        loading = true;
        showProgressbar();
        LanguagehelperHttpClient.get(Setings.TXNewsApi + type + Setings.TXNewsApiEnd + currentPage, new UICallback(mContext) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    loading = false;
                    if (JsonParser.isJson(responseString)) {
                        TXNewsResult mRoot = JSON.parseObject(responseString, TXNewsResult.class);
                        if (mRoot.getCode() == 200) {
                            if (currentPage == 1) {
                                mWechatJXItem.clear();
                            }
                            mWechatJXItem.addAll(mRoot.getNewslist());
                            currentPage++;
                            showFooterview();
                            if (addAD()) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(getContext(), getActivity().getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onSwipeRefreshLayoutFinish();
                hideProgressbar();
            }
        });
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
                if(ADUtil.isHasLocalAd()){
                    onAdLoaded(ADUtil.getRandomAdList(getActivity()));
                }
            }
            @Override
            public void onAdLoaded(NativeDataRef nativeDataRef) {
                if(nativeDataRef != null){
                    mADObject = new TXNewsItem();
                    mADObject.setmNativeADDataRef(nativeDataRef);
                    if (!loading) {
                        addAD();
                    }
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd();
    }

    private boolean addAD() {
        if (mADObject != null && mWechatJXItem != null && mWechatJXItem.size() > 0) {
            int index = mWechatJXItem.size() - 15 + NumberUtil.randomNumberRange(1, 2);
            if (index < 0) {
                index = 0;
            }
            LogUtil.DefalutLog("Index:" + index);
            mWechatJXItem.add(index, mADObject);
            mAdapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        } else {
            return true;
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onClick(View v) {

    }


}
