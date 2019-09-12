package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLCNWBeanModel;
import com.messi.languagehelper.adapter.RcNovelListAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NovelCollectedActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView category_lv;
    private TextView manage_btn;
    private LinearLayout delete_layout;
    private TextView empty_btn;
    private TextView delete_btn;
    private RcNovelListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<CNWBean> mList;
    private XXLCNWBeanModel mXXLModel;
    private boolean isDeleteModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owllook_list_activity);
        init();
        initSwipeRefresh();
        getData();
    }

    private void init() {
        mList = new ArrayList<CNWBean>();
        mXXLModel = new XXLCNWBeanModel(this,2);
        initSwipeRefresh();
        category_lv = (RecyclerView) findViewById(R.id.listview);
        manage_btn = (TextView) findViewById(R.id.manage_btn);
        delete_layout = (LinearLayout) findViewById(R.id.delete_layout);
        empty_btn = (TextView) findViewById(R.id.empty_btn);
        delete_btn = (TextView) findViewById(R.id.delete_btn);
        manage_btn.setOnClickListener(this);
        empty_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        mAdapter = new RcNovelListAdapter();
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
        Observable.create(new ObservableOnSubscribe<List<CNWBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CNWBean>> e) throws Exception {
                e.onNext(getCollectedList());
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CNWBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<CNWBean> list) {
                        setData(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mXXLModel.loading = false;
                        hideProgressbar();
                        onSwipeRefreshLayoutFinish();
                    }
                });
    }

    private List<CNWBean> getCollectedList(){
        return BoxHelper.getCollectedList(AVOUtil.Novel.Novel,0,0);
    }

    private void setData(List<CNWBean> list){
        if(list != null && list.size() > 0){
            if(mList != null && mAdapter != null){
                mList.clear();
                mList.addAll(list);
                initDeleteModel(list);
                mAdapter.notifyDataSetChanged();
                loadAD();
            }
        }else {
            ToastUtil.diaplayMesShort(this, "没有找到");
        }
    }

    private void manageBtn(){
        if(!isDeleteModel){
            delete_layout.setVisibility(View.VISIBLE);
            manage_btn.setText(getString(R.string.done));
            isDeleteModel = true;
            setDeleteModel(mList,"1");
        }else {
            delete_layout.setVisibility(View.GONE);
            manage_btn.setText(getString(R.string.manage));
            isDeleteModel = false;
            setDeleteModel(mList,"0");
        }
    }

    private void initDeleteModel(List<CNWBean> list){
        if(isDeleteModel){
            setDeleteModel(list,"1");
        }else {
            setDeleteModel(list,"0");
        }
    }

    private void setDeleteModel(List<CNWBean> list,String status){
        for(CNWBean mAVObject : list){
            mAVObject.setDelete_model(status);
            mAVObject.setIs_need_delete("0");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void reset(){
        delete_layout.setVisibility(View.GONE);
        manage_btn.setText(getString(R.string.manage));
        isDeleteModel = false;
        setDeleteModel(mList,"0");
    }

    private void deleteAll(){
        BoxHelper.deleteAllData(AVOUtil.Novel.Novel);
        mList.clear();
        reset();
    }

    private void deleteBooks(){
        List<CNWBean> newList = new ArrayList<CNWBean>();
        for(CNWBean mAVObject : mList){
            if(!TextUtils.isEmpty(mAVObject.getIs_need_delete())){
                if("1".equals(mAVObject.getIs_need_delete())){
                    BoxHelper.deleteCNWBean(mAVObject);
                }else {
                    newList.add(mAVObject);
                }
            }
        }
        mList.clear();
        mList.addAll(newList);
        reset();
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.manage_btn){
            manageBtn();
        } else if(view.getId() == R.id.empty_btn) {
            deleteAll();
        } else if(view.getId() == R.id.delete_btn) {
            deleteBooks();
        }
    }
}
