package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLCNWBeanModel;
import com.messi.languagehelper.adapter.RcNovelResultListAdapter;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class NovelResultListActivity extends BaseActivity implements OnClickListener {

    private RecyclerView category_lv;
    private RcNovelResultListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<CNWBean> mList;
    private String url;
    private XXLCNWBeanModel mXXLModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novel_result_list_activity);
        init();
        initSwipeRefresh();
        getData();
    }

    private void init() {
        url = getIntent().getStringExtra(KeyUtil.URL);
        mList = new ArrayList<CNWBean>();
        mXXLModel = new XXLCNWBeanModel(this,2);
        initSwipeRefresh();
        category_lv = (RecyclerView) findViewById(R.id.listview);
        mAdapter = new RcNovelResultListAdapter();
        mAdapter.setItems(mList);
        mXXLModel.setAdapter(mList,mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        category_lv.setLayoutManager(mLinearLayoutManager);
        category_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.padding_10)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
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
                            NativeDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                            boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i%vCount));
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
        showProgressbar();
        LanguagehelperHttpClient.get(url,new UICallback(this){
            @Override
            public void onFailured() {
            }
            @Override
            public void onFinished() {
                mXXLModel.loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
            }
            @Override
            public void onResponsed(String responseString) {
                if(!TextUtils.isEmpty(responseString)){
                    List<CNWBean> tlist = HtmlParseUtil.parseOwllookListHtml(url,responseString);
                    for (CNWBean bean : tlist){
                        LogUtil.DefalutLog("responseString:"+bean.toString());
                    }
                    setData(tlist);
                }
            }
        });
    }

    private void setData(List<CNWBean> list){
        if(list != null && !list.isEmpty()){
            if(mList != null && mAdapter != null){
                mList.clear();
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                loadAD();
            }
        }else {
            ToastUtil.diaplayMesShort(this, "没有找到");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
    }
}
