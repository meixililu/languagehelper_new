package com.messi.languagehelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcAiDialogCourseYYSListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AiDialogueCourseForYYSFragment extends BaseFragment implements View.OnClickListener{

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView category_lv;
    private FrameLayout random_cover;
    private RcAiDialogCourseYYSListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;
    private GridLayoutManager layoutManager;

    public static AiDialogueCourseForYYSFragment getInstance() {
        AiDialogueCourseForYYSFragment fragment = new AiDialogueCourseForYYSFragment();
        return fragment;
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
        LogUtil.DefalutLog("SpokenEnglishPractiseFragment-onCreateView");
        View view = inflater.inflate(R.layout.ai_dialog_course_yys_fragment, container, false);
        initSwipeRefresh(view);
        initViews(view);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        skip = 0;
        new QueryTask(this).execute();
    }

    private void initViews(View view) {
        avObjects = new ArrayList<AVObject>();
        random_cover = (FrameLayout) view.findViewById(R.id.random_cover);
        category_lv = (RecyclerView) view.findViewById(R.id.studycategory_lv);
        mXFYSAD = new XFYSAD(getActivity(), ADUtil.SecondaryPage);
        mAdapter = new RcAiDialogCourseYYSListAdapter(mXFYSAD);
        mAdapter.setItems(avObjects);
        mAdapter.setHeader(new Object());
        mAdapter.setFooter(new Object());
        mXFYSAD.setAdapter(mAdapter);
        hideFooterview();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
        random_cover.setOnClickListener(this);
    }

    public void setListOnScrollListener(){
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
                        new QueryTask(AiDialogueCourseForYYSFragment.this).execute();
                    }
                }
            }
        });
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        hideFooterview();
        skip = 0;
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask(this).execute();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.random_cover){
            random_select();
        }
    }


    private class QueryTask extends AsyncTask<Void, Void,  List<AVObject>> {

        private WeakReference<AiDialogueCourseForYYSFragment> mainActivity;

        public QueryTask(AiDialogueCourseForYYSFragment mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.CantoneseCategory.CantoneseCategory);
            query.whereEqualTo(AVOUtil.CantoneseCategory.ECIsValid, "1");
            query.orderByDescending(AVOUtil.CantoneseCategory.ECOrder);
            query.skip(skip);
            query.limit(20);
            try {
                return query.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null){
                loading = false;
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if(avObject != null){
                    if(avObject.size() == 0){
                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        hasMore = false;
                        hideFooterview();
                    }else{
                        if(avObjects != null && mAdapter != null){
                            avObjects.addAll(avObject);
                            skip += 20;
                            showFooterview();
                            hasMore = true;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void random_select(){
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<AVObject>() {
            @Override
            public void subscribe(ObservableEmitter<AVObject> e) throws Exception {
                e.onNext( getCourse() );
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AVObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AVObject mResult) {
                        hideProgressbar();
                        toPracticeActivity(mResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private AVObject getCourse(){
        int random = NumberUtil.randomNumber(25);
        LogUtil.DefalutLog("random:"+random);
        AVObject mAVObject = null;
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.CantoneseCategory.CantoneseCategory);
        query.whereEqualTo(AVOUtil.CantoneseCategory.ECIsValid, "1");
        query.orderByDescending(AVOUtil.CantoneseCategory.ECOrder);
        query.skip(random);
        query.limit(1);
        List<AVObject> list  = null;
        try{
            list = query.find();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(NullUtil.isNotEmpty(list)){
            mAVObject = list.get(0);
        }
        return mAVObject;
    }

    private void toPracticeActivity(AVObject mAVObject){
        Intent intent = new Intent(getContext(),AiDialoguePracticeYYSActivity.class);
        intent.putExtra(AVOUtil.CantoneseCategory.ECCode, mAVObject.getString(AVOUtil.CantoneseCategory.ECCode));
        startActivity(intent);
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mXFYSAD != null){
            mXFYSAD.onDestroy();
        }
    }
}
