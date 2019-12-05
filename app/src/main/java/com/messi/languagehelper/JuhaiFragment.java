package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcJuhaiListAdapter;
import com.messi.languagehelper.bean.JuhaiBean;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.event.ProgressEvent;
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class JuhaiFragment extends BaseFragment {


    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;

    private List<JuhaiBean> beans;
    private RcJuhaiListAdapter mAdapter;
    private String lastSearch;

    public static JuhaiFragment getInstance(FragmentProgressbarListener listener) {
        JuhaiFragment mMainFragment = new JuhaiFragment();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_juhai, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init(){
        isRegisterBus = true;
        beans = new ArrayList<JuhaiBean>();
        mAdapter = new RcJuhaiListAdapter();
        recent_used_lv.setLayoutManager(new LinearLayoutManager(getContext()));
        recent_used_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        mAdapter.setFooter(new Object());
        mAdapter.setItems(beans);
        recent_used_lv.setAdapter(mAdapter);
    }

    private void translateController() {
        lastSearch = Setings.q;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<List<JuhaiBean>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<JuhaiBean>> e) throws Exception {
                String url = Setings.JukuApi.replace("{0}",lastSearch);
                LanguagehelperHttpClient.get(url,new BgCallback(){
                    @Override
                    public void onResponsed(String responseString) {
                        List<JuhaiBean> list = HtmlParseUtil.parseJukuHtml(responseString);
                        e.onNext(list);
                    }

                    @Override
                    public void onFailured() {
                        getDataTask();
                    }
                });
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<JuhaiBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(List<JuhaiBean> juhaiBeans) {
                if(juhaiBeans != null){
                    setData(juhaiBeans);
                }else {
                    getDataTask();
                }
            }
            @Override
            public void onError(Throwable e) {
                getDataTask();
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private void setData(List<JuhaiBean> juhaiBeans){
        hideProgressbar();
        if(juhaiBeans != null && !juhaiBeans.isEmpty()){
            beans.clear();
            beans.addAll(juhaiBeans);
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new FinishEvent());
        }else {
            ToastUtil.diaplayMesShort(getContext(),"未找到相关例句");
        }
    }

    private void getDataTask() {
        lastSearch = Setings.q;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<List<JuhaiBean>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<JuhaiBean>> e) throws Exception {
                String url = Setings.JuhaiApi.replace("{0}",lastSearch);
                LanguagehelperHttpClient.get(url,new BgCallback(){
                    @Override
                    public void onResponsed(String responseString) {
                        List<JuhaiBean> list = HtmlParseUtil.parseJuhaiHtml(responseString);
                        e.onNext(list);
                    }

                    @Override
                    public void onFailured() {
                        e.onError(null);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<JuhaiBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(List<JuhaiBean> juhaiBeans) {
                        setData(juhaiBeans);
                    }
                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                        setData(null);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void submit() {
        translateController();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ProgressEvent event){
        LogUtil.DefalutLog("ProgressEvent");
        if(event.getStatus() == 0){
            showProgressbar();
        }else {
            hideProgressbar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
