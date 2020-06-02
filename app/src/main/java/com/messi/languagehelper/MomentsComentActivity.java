package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.adapter.RcMomentsCommentListAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.callback.SaveCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class MomentsComentActivity extends BaseActivity {

    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.content)
    TextView content_tv;
    @BindView(R.id.no_comment)
    TextView noComment;
    @BindView(R.id.input_et)
    AppCompatEditText inputEt;
    @BindView(R.id.submit_btn)
    TextView submitBtn;
    private RcMomentsCommentListAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private String content;
    private String momid;
    private boolean isNeedClear;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moments_comment_activity);
        ButterKnife.bind(this);
        initViews();
        new QueryTask(this).execute();
    }


    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.comment));
        content = getIntent().getStringExtra(KeyUtil.ContextKey);
        momid = getIntent().getStringExtra(KeyUtil.Id);
        avObjects = new ArrayList<AVObject>();
        mAdapter = new RcMomentsCommentListAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        noComment.setVisibility(View.GONE);
        content_tv.setText(content);
        mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.line)
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
                if ((visible + firstVisibleItem) >= total) {
                    new QueryTask(MomentsComentActivity.this).execute();
                }
            }
        });
    }

    @OnClick(R.id.submit_btn)
    public void onClick() {
        checkUidAndPublish();
    }

    private void checkUidAndPublish(){
        AVQuery<AVObject> avQuery = new AVQuery<>(AVOUtil.MomentsFilter.MomentsFilter);
        avQuery.whereEqualTo(AVOUtil.MomentsFilter.uid,SystemUtil.getDev_id(this));
        avQuery.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avObjects != null && avObjects.size() > 0){
                    ToastUtil.diaplayMesShort(MomentsComentActivity.this,"很抱歉，您已被禁言！");
                }else {
                    publish();
                }
            }
        }));
    }

    private void publish() {
        String content = inputEt.getText().toString().trim();
        if(isCanPublish(content)){
            AVObject object = new AVObject(AVOUtil.MomentsComment.MomentsComment);
            object.put(AVOUtil.MomentsComment.content, content);
            object.put(AVOUtil.MomentsComment.momid, momid);
            object.put(AVOUtil.MomentsComment.uid, SystemUtil.getDev_id(this));
            object.saveInBackground().subscribe(ObserverBuilder.buildSingleObserver(new SaveCallback<AVObject>() {
                @Override
                public void done(AVException e) {
                    if(e == null){
                        ToastUtil.diaplayMesShort(MomentsComentActivity.this,"发布成功");
                        skip = 0;
                        isNeedClear = true;
                        new QueryTask(MomentsComentActivity.this).execute();
                        inputEt.setText("");
                    }else{
                        ToastUtil.diaplayMesShort(MomentsComentActivity.this,"发布失败，请重试！");
                    }
                }
            }));
        }
    }

    private boolean isCanPublish(String content){
        boolean isCanPublish = true;
        if(TextUtils.isEmpty(content)){
            isCanPublish = false;
            ToastUtil.diaplayMesShort(MomentsComentActivity.this,"请输入评论！");
        }else if(!StringUtils.isAllEnglish(content)){
            isCanPublish = false;
            ToastUtil.diaplayMesShort(MomentsComentActivity.this,"只能输入英文！");
        }
        return isCanPublish;
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        private WeakReference<MomentsComentActivity> mainActivity;

        public QueryTask(MomentsComentActivity mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.MomentsComment.MomentsComment);
            query.whereEqualTo(AVOUtil.MomentsComment.momid, momid);
            query.addDescendingOrder(AVOUtil.MomentsComment.createdAt);
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
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                if (avObject != null) {
                    if (avObject.size() == 0) {
                        if(skip == 0){
                            noComment.setVisibility(View.VISIBLE);
                        }
                        hideFooterview();
                    } else {
                        if (avObjects != null && mAdapter != null) {
                            noComment.setVisibility(View.GONE);
                            if(isNeedClear){
                                avObjects.clear();
                            }
                            avObjects.addAll(avObject);
                            mAdapter.notifyDataSetChanged();
                            skip += Setings.page_size;
                            if (avObject.size() < Setings.page_size) {
                                hideFooterview();
                            }else {
                                showFooterview();
                            }
                        }
                    }
                }
            }
        }
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
    }
}
