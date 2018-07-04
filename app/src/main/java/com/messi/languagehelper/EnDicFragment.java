package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.mindorks.nybus.NYBus;

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

public class EnDicFragment extends BaseFragment {

    @BindView(R.id.dic_content)
    TextView dic_content;
    @BindView(R.id.dic_scrollview)
    ScrollView dic_scrollview;

    private String lastSearch;

    public static EnDicFragment getInstance(FragmentProgressbarListener listener) {
        EnDicFragment mMainFragment = new EnDicFragment();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_en_dic, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private void translateController() {
        lastSearch = Settings.q;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String url = Settings.EndicApi + lastSearch;
                LogUtil.DefalutLog(url);
                Response mResponse = LanguagehelperHttpClient.get(url);
                if (mResponse != null && mResponse.isSuccessful()) {
                    e.onNext(HtmlParseUtil.parseEndicHtml(mResponse.body().string()));
                }
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
                    public void onNext(String content) {
                        dic_content.setText(lastSearch+"\n"+content);
                        dic_scrollview.scrollTo(0,0);
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
        translateController();
    }

}
