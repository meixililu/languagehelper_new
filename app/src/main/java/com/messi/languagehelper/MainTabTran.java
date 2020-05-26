package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.messi.languagehelper.adapter.RcTranslateListAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.databinding.MainTabTranBinding;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.event.TranAndDicRefreshEvent;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.views.DividerItemDecoration;
import com.youdao.sdk.ydtranslate.Translate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainTabTran extends BaseFragment {

    private Record currentDialogBean;
    private RcTranslateListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SharedPreferences sp;
    private List<Record> beans;
    private String lastSearch;
    private int skip;
    private boolean noMoreData;
    private boolean isNeedRefresh;
    private View view;
    private MainTabTranBinding binding;

    public static MainTabTran getInstance(FragmentProgressbarListener listener) {
        MainTabTran mMainFragment = new MainTabTran();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (view != null) {
            ViewUtil.removeFromParentView(view);
            return view;
        }
        binding = MainTabTranBinding.inflate(inflater);
        view = binding.getRoot();
        initSwipeRefresh(binding.getRoot());
        init(view);
        return view;
    }

    private void init(View view) {
        sp = Setings.getSharedPreferences(getContext());
        isRegisterBus = true;
        beans = new ArrayList<Record>();
        loadData();
        boolean IsHasShowBaiduMessage = sp.getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
        if (!IsHasShowBaiduMessage) {
            initSample();
            Setings.saveSharedPreferences(sp, KeyUtil.IsHasShowBaiduMessage, true);
        }
        mAdapter = new RcTranslateListAdapter(beans);
        binding.recentUsedLv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        binding.recentUsedLv.setLayoutManager(mLinearLayoutManager);
        binding.recentUsedLv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        mAdapter.setFooter(new Object());
        binding.recentUsedLv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener(){
        binding.recentUsedLv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!noMoreData){
                    int visible  = mLinearLayoutManager.getChildCount();
                    int total = mLinearLayoutManager.getItemCount();
                    int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if ((visible + firstVisibleItem) >= total){
                        loadData();
                    }
                }
            }
        });
    }

    public void loadData(){
        List<Record> list = BoxHelper.getRecordList(skip, Setings.RecordOffset);
        if (NullUtil.isNotEmpty(list)) {
            beans.addAll(list);
            skip += list.size();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }else {
            noMoreData = true;
        }
    }

    private void initSample() {
        Record sampleBean = new Record("Click the mic to speak", "点击话筒说话");
        BoxHelper.insert(sampleBean);
        beans.add(0, sampleBean);
    }

    private void translateController(){
        lastSearch = Setings.q;
        if(NetworkUtil.isNetworkConnected(getContext())){
            LogUtil.DefalutLog("online");
            try {
                RequestJinShanNewAsyncTask();
            } catch (Exception e) {
                LogUtil.DefalutLog("online exception");
                e.printStackTrace();
            }
        }else {
            LogUtil.DefalutLog("offline");
            translateOffline();
        }
    }

    private void translateOffline(){
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<Translate>() {
            @Override
            public void subscribe(ObservableEmitter<Translate> e) throws Exception {
                TranslateUtil.offlineTranslate(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translate>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Translate translate) {
                        parseOfflineData(translate);
                    }
                    @Override
                    public void onError(Throwable e) {
                        onComplete();
                    }
                    @Override
                    public void onComplete() {
                        hideProgressbar();
                    }
                });
    }

    private void parseOfflineData(Translate translate){
        if(translate != null){
            if(translate.getErrorCode() == 0){
                StringBuilder sb = new StringBuilder();
                TranslateUtil.addSymbol(translate,sb);
                for(String tran : translate.getTranslations()){
                    sb.append(tran);
                    sb.append("\n");
                }
                currentDialogBean = new Record(sb.substring(0, sb.lastIndexOf("\n")), Setings.q);
                insertData();
                autoClearAndautoPlay();
            }
        }else{
            showToast("没找到离线词典，请到更多页面下载！");
        }
    }
    /**
     * send translate request
     */
    private void RequestJinShanNewAsyncTask() throws Exception{
        showProgressbar();
        TranslateUtil.Translate(mrecord -> onResult(mrecord));
    }

    private void onResult(Record mRecord){
        try {
            hideProgressbar();
            if(mRecord == null){
                showToast(getContext().getResources().getString(R.string.network_error));
            }else {
                currentDialogBean = mRecord;
                insertData();
                autoClearAndautoPlay();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertData() {
        mAdapter.addEntity(0, currentDialogBean);
        binding.recentUsedLv.scrollToPosition(0);
        BoxHelper.insert(currentDialogBean);
    }

    public void autoClearAndautoPlay() {
        EventBus.getDefault().post(new FinishEvent());
        if (sp.getBoolean(KeyUtil.AutoPlayResult, false)) {
            delayAutoPlay();
        }
    }

    private void autoPlay() {
        try {
            View mView = binding.recentUsedLv.getChildAt(0);
            final FrameLayout record_answer_cover = (FrameLayout) mView.findViewById(R.id.record_answer_cover);
            final FrameLayout record_question_cover = (FrameLayout) mView.findViewById(R.id.record_question_cover);
            Record item = beans.get(0);
            if (!TextUtils.isEmpty(item.getPh_en_mp3())) {
                if(record_question_cover != null){
                    record_question_cover.post(new Runnable() {
                        @Override
                        public void run() {
                            record_question_cover.performClick();
                        }
                    });
                }
            } else {
                if(record_answer_cover != null){
                    record_answer_cover.post(new Runnable() {
                        @Override
                        public void run() {
                            record_answer_cover.performClick();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void refresh() {
        if (isNeedRefresh && mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            isNeedRefresh = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TranAndDicRefreshEvent event){
        reloadData();
        LogUtil.DefalutLog("---TranAndDicRefreshEvent---onEvent");
    }

    private void reloadData() {
        isNeedRefresh = true;
        beans.clear();
        beans.addAll(BoxHelper.getRecordList(0, Setings.RecordOffset));
    }

    private void delayAutoPlay(){
        new Handler().postDelayed(() -> autoPlay(),100);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        if (beans != null) {
            beans.clear();
            beans.addAll(BoxHelper.getRecordList(0, Setings.RecordOffset));
            mAdapter.notifyDataSetChanged();
        }
        onSwipeRefreshLayoutFinish();
    }
}

