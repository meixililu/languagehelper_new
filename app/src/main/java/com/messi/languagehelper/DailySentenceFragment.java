package com.messi.languagehelper;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcDailySentenceListAdapter;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.XFYSAD;
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

public class DailySentenceFragment extends BaseFragment implements OnClickListener {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcDailySentenceListAdapter mAdapter;
    private List<EveryDaySentence> beans;
    private MediaPlayer mPlayer;
    private XFYSAD mXFYSAD;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.daily_sentence_activity, container, false);
        initSwipeRefresh(view);
        init(view);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        getDataTask();
    }

    private void init(View view) {
        try {
            mPlayer = new MediaPlayer();
            mInflater = LayoutInflater.from(getContext());
            beans = new ArrayList<EveryDaySentence>();
            mXFYSAD = new XFYSAD(getContext(), ADUtil.MRYJYSNRLAd);
            recent_used_lv = (RecyclerView) view.findViewById(R.id.listview);
            mAdapter = new RcDailySentenceListAdapter(getActivity(), beans, mPlayer, mProgressbarListener, mXFYSAD);
            mAdapter.setHeader(new Object());
            mAdapter.setItems(beans);
            recent_used_lv.setLayoutManager(new LinearLayoutManager(getContext()));
            recent_used_lv.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(getContext())
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
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        getDataTask();
    }

    private void getDataTask() {
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                getServiceData();
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
                        hideProgressbar();
                        onSwipeRefreshLayoutFinish();
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void getServiceData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.DailySentence.DailySentence);
        query.orderByDescending(AVOUtil.DailySentence.dateline);
        query.limit(30);
        try {
            List<AVObject> sentences = query.find();
            if (sentences != null && sentences.size() > 0) {
                beans.clear();
                for (AVObject bean : sentences) {
                    beans.add(changeData(bean));
                }
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    private EveryDaySentence changeData(AVObject mAVObject) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        LogUtil.DefalutLog("CollectedFragment-onDestroyView");
    }


    @Override
    public void onClick(View v) {

    }
}
