package com.messi.languagehelper;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcDailySentenceListAdapter;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.XFYSAD;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DailySentenceActivity extends BaseActivity implements OnClickListener {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcDailySentenceListAdapter mAdapter;
    private List<EveryDaySentence> beans;
    private MediaPlayer mPlayer;

    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_sentence_activity);
        init();
        getDataTask();
    }

    private void init() {
        try {
            getSupportActionBar().setTitle(getResources().getString(R.string.dailysentence));
            mPlayer = new MediaPlayer();
            mInflater = LayoutInflater.from(this);
            beans = new ArrayList<EveryDaySentence>();
            mXFYSAD = new XFYSAD(DailySentenceActivity.this, ADUtil.MRYJYSNRLAd);
            recent_used_lv = (RecyclerView) findViewById(R.id.listview);
            mAdapter = new RcDailySentenceListAdapter(this, beans, mPlayer, mProgressbar, mXFYSAD);
            mAdapter.setHeader(new Object());
            mAdapter.setItems(beans);
            recent_used_lv.setLayoutManager(new LinearLayoutManager(this));
            recent_used_lv.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(this)
                            .colorResId(R.color.text_tint)
                            .sizeResId(R.dimen.list_divider_size)
                            .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                            .build());
            recent_used_lv.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXFYSAD != null) {
            mXFYSAD.startPlayImg();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
            mXFYSAD = null;
        }
        LogUtil.DefalutLog("CollectedFragment-onDestroy");
    }

    @Override
    public void onClick(View v) {

    }

    private void getDataTask() {
        showProgressbar();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                getLocalData();
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                hideProgressbar();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
            }
        });
    }

    private void getLocalData(){
        List<EveryDaySentence> list = DataBaseUtil.getInstance().getDailySentenceList(Settings.offset);
        if(list != null && list.size() < 20){
            List<EveryDaySentence> sentences = getServiceData();
            if(sentences != null && sentences.size() > 0){
                list.addAll(sentences);
            }
        }
        beans.clear();
        beans.addAll(list);
    }

    private List<EveryDaySentence> getServiceData(){
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.DailySentence.DailySentence);
        query.orderByDescending(AVOUtil.DailySentence.dateline);
        query.limit(30);
        try {
            List<AVObject> sentences = query.find();
            if(sentences != null && sentences.size() > 0){
                List<EveryDaySentence> list = new ArrayList<EveryDaySentence>();
                for(AVObject bean : sentences){
                    list.add(changeData(bean));
                }
                DataBaseUtil.getInstance().saveEveryDaySentenceList(list);
                return list;
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }

    private EveryDaySentence changeData(AVObject mAVObject){
        EveryDaySentence bean = new EveryDaySentence();
        bean.setContent(mAVObject.getString(AVOUtil.DailySentence.content));
        bean.setNote(mAVObject.getString(AVOUtil.DailySentence.note));
        bean.setTts(mAVObject.getString(AVOUtil.DailySentence.tts));
        bean.setPicture2(mAVObject.getString(AVOUtil.DailySentence.picture2));
        String dateline = mAVObject.getString(AVOUtil.DailySentence.dateline);
        bean.setDateline(dateline);
        String temp = dateline.replaceAll("-", "");
        long cid = NumberUtil.StringToLong(temp);
        bean.setCid(cid);
        return bean;
    }

}
