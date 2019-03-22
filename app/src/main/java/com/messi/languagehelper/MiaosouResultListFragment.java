package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.iflytek.voiceads.NativeADDataRef;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.ViewModel.XXLCNWBeanModel;
import com.messi.languagehelper.adapter.RcMiaosouListAdapter;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MiaosouResultListFragment extends BaseFragment implements OnClickListener {

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
    private RcMiaosouListAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private List<CNWBean> mList;
    private String url;
    private View view;
    private XXLCNWBeanModel mXXLModel;

    public static MiaosouResultListFragment newInstance(String url){
        MiaosouResultListFragment fragment = new MiaosouResultListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.url = mBundle.getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if(view != null){
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.joke_picture_fragment, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        loadAD();
        getData();
    }

    private void initViews(View view) {
        mList = new ArrayList<CNWBean>();
        mXXLModel = new XXLCNWBeanModel(getActivity());
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        initSwipeRefresh(view);
        mAdapter = new RcMiaosouListAdapter();
        mAdapter.setFooter(new Object());
        mAdapter.setItems(mList);
        mXXLModel.setAdapter(mList,mAdapter);
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                isADInList(recyclerView, firstVisibleItem, visible);
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount){
        if(mList.size() > 3){
            for(int i=first;i< (first+vCount);i++){
                if(i < mList.size() && i > 0){
                    CNWBean mAVObject = mList.get(i);
                    if(mAVObject != null && mAVObject.getmNativeADDataRef() != null){
                        if(!mAVObject.isAdShow()){
                            NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                            boolean isExposure = mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
                            mAVObject.setAdShow(isExposure);
                            LogUtil.DefalutLog("isExposure:"+isExposure);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        loadAD();
        hideFooterview();
        mList.clear();
        mAdapter.notifyDataSetChanged();
        getData();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private void getData(){
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        showFooterview();
        if(TextUtils.isEmpty(url)){
            hideFooterview();
            ToastUtil.diaplayMesShort(getContext(), "没有找到");
            return;
        }
        LanguagehelperHttpClient.get(url,new UICallback(getActivity()){
            @Override
            public void onFailured() {
            }
            @Override
            public void onFinished() {
                mXXLModel.loading = false;
                hideFooterview();
                onSwipeRefreshLayoutFinish();
            }
            @Override
            public void onResponsed(String responseString) {
                if(!TextUtils.isEmpty(responseString)){
                    LogUtil.DefalutLog("responseString:"+responseString);
                    List<CNWBean> tlist = HtmlParseUtil.parseMiaosouHtml(url,responseString);
                    setData(tlist);
                }
            }
        });
    }

    private void setData(List<CNWBean> list){
        if(list != null && list.size() > 0){
            if(mList != null && mAdapter != null){
                mList.clear();
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                loadAD();
            }
        }else {
            ToastUtil.diaplayMesShort(getContext(), "没有找到");
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void showFooterview() {
        if (mAdapter != null) {
            mAdapter.showFooter();
        }
    }

    private void hideFooterview() {
        if (mAdapter != null) {
            mAdapter.hideFooter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
