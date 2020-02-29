package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.messi.languagehelper.adapter.RcTranZhYueListAdapter;
import com.messi.languagehelper.dao.TranResultZhYue;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OnTranZhYueFinishListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainTabTranZhYue extends BaseFragment {

    private RecyclerView recent_used_lv;
    private TranResultZhYue currentDialogBean;
    private RcTranZhYueListAdapter mAdapter;
    private List<TranResultZhYue> beans;
    private String lastSearch;

    public static MainTabTranZhYue getInstance(FragmentProgressbarListener listener) {
        MainTabTranZhYue mMainFragment = new MainTabTranZhYue();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_tran, null);
        LogUtil.DefalutLog("MainTabTranZhYue-onCreateView");
        initLowVersionData();
        init(view);
        return view;
    }

    private void initLowVersionData(){
        SharedPreferences sp = Setings.getSharedPreferences(getContext());
        if(!sp.getBoolean(KeyUtil.IsYYSHasTransafeData,false)){
            boolean isNeedTransfeData = false;
            List<record> oldBeans = DataBaseUtil.getInstance().getDataListRecord();
            for(record bean : oldBeans){
                if(!TextUtils.isEmpty(bean.getBackup3()) && "yue".equals(bean.getBackup3())){
                    isNeedTransfeData = true;
                }
            }
            if(isNeedTransfeData){
                for(int i = oldBeans.size(); i >= 0; i--){
                    record bean = oldBeans.get(i);
                    TranResultZhYue mResult = new TranResultZhYue();
                    mResult.setChinese(bean.getChinese());
                    mResult.setEnglish(bean.getEnglish());
                    mResult.setIscollected(bean.getIscollected());
                    mResult.setQuestionAudioPath(bean.getQuestionAudioPath());
                    mResult.setQuestionVoiceId(bean.getQuestionVoiceId());
                    mResult.setResultAudioPath(bean.getResultAudioPath());
                    mResult.setResultVoiceId(bean.getResultVoiceId());
                    mResult.setSpeak_speed(bean.getSpeak_speed());
                    mResult.setVisit_times(bean.getVisit_times());
                    mResult.setBackup1(bean.getBackup1());
                    mResult.setBackup2(bean.getBackup2());
                    mResult.setBackup3(bean.getBackup3());
                    DataBaseUtil.getInstance().insert(mResult);
                }
            }
            DataBaseUtil.getInstance().clearAllTran();
            Setings.saveSharedPreferences(sp,KeyUtil.IsYYSHasTransafeData,true);
        }
    }

    private void init(View view) {
        recent_used_lv = (RecyclerView) view.findViewById(R.id.recent_used_lv);
        beans = new ArrayList<TranResultZhYue>();
        beans.addAll(DataBaseUtil.getInstance().getDataListZhYue(0, Setings.RecordOffset));
        boolean IsHasShowBaiduMessage = PlayUtil.getSP().getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
        if (!IsHasShowBaiduMessage) {
            initSample();
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsHasShowBaiduMessage, true);
        }
        mAdapter = new RcTranZhYueListAdapter(beans);
        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(getContext()));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        mAdapter.setFooter(new Object());
        recent_used_lv.setAdapter(mAdapter);
    }

    private void initSample() {
        TranResultZhYue sampleBean = new TranResultZhYue("点击咪讲嘢", "点击话筒说话","yue");
        DataBaseUtil.getInstance().insert(sampleBean);
        beans.add(0, sampleBean);
    }


    private void translateController(){
        lastSearch = Setings.q;
        if(NetworkUtil.isNetworkConnected(getContext())){
            LogUtil.DefalutLog("online");
            try {
                RequestTask();
            } catch (Exception e) {
                LogUtil.DefalutLog("online exception");
                e.printStackTrace();
            }
        }else {
            showToast(getContext().getResources().getString(R.string.network_offline));
        }
    }

    /**
     * send translate request
     */
    private void RequestTask() throws Exception{
        showProgressbar();
        TranslateUtil.TranslateZhYue(new OnTranZhYueFinishListener() {
            @Override
            public void OnFinishTranslate(TranResultZhYue mTranResultZhYue) {
                hideProgressbar();
                if(mTranResultZhYue == null){
                    showToast(getContext().getResources().getString(R.string.network_error));
                }else {
                    currentDialogBean = mTranResultZhYue;
                    insertData();
                    autoClearAndautoPlay();
                }
            }
        });
    }

    public void insertData() {
        mAdapter.addEntity(0, currentDialogBean);
        recent_used_lv.scrollToPosition(0);
        long newRowId = DataBaseUtil.getInstance().insert(currentDialogBean);
    }

    public void autoClearAndautoPlay() {
        EventBus.getDefault().post(new FinishEvent());
        if (PlayUtil.getSP().getBoolean(KeyUtil.AutoPlayResult, false)) {
            new AutoPlayWaitTask().execute();
        }
    }

    private void autoPlay() {
        View mView = recent_used_lv.getChildAt(0);
        final FrameLayout record_answer_cover = (FrameLayout) mView.findViewById(R.id.record_answer_cover);
        record_answer_cover.post(new Runnable() {
            @Override
            public void run() {
                record_answer_cover.performClick();
            }
        });
    }

    public void refresh() {
        if (Setings.isMainFragmentNeedRefresh) {
            Setings.isMainFragmentNeedRefresh = false;
            reloadData();
        }

    }

    /**
     * toast message
     * @param toastString
     */
    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(getContext(), toastString);
    }

    /**
     * submit request task
     */
    public void submit() {
        translateController();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void reloadData() {
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                beans.clear();
                beans.addAll(DataBaseUtil.getInstance().getDataListZhYue(0, Setings.RecordOffset));
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
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }

    class AutoPlayWaitTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            autoPlay();
        }
    }


}

