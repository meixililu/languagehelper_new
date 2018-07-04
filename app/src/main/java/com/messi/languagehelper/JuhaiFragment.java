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
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
import okhttp3.Response;

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

    private void translateController(final int page) {
        lastSearch = Settings.q;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<List<JuhaiBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<JuhaiBean>> e) throws Exception {
                String url = Settings.JuhaiApi.replace("{0}",lastSearch);
                url = url.replace("{1}",String.valueOf(page));
                LogUtil.DefalutLog(url);
                Response mResponse = LanguagehelperHttpClient.get(url);
                if (mResponse != null && mResponse.isSuccessful()) {
                    e.onNext(HtmlParseUtil.parseJuhaiHtml(mResponse.body().string()));
                }
                e.onComplete();
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
                        if(page == 1){
                            beans.clear();
                        }
                        beans.addAll(juhaiBeans);
                        mAdapter.notifyDataSetChanged();
                        if(page == 1){
                            recent_used_lv.scrollToPosition(0);
                            translateController(2);
                        }
                        NYBus.get().post(new FinishEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                    }

                    @Override
                    public void onComplete() {
                        hideProgressbar();
                    }
                });

    }

    public void submit() {
        translateController(1);
    }

    @Subscribe
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
