package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.HtmlParseUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ShareUtil;
import com.messi.languagehelper.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;


public class ChineseDictionaryFragment extends BaseFragment {

    @BindView(R.id.btn_bushou)
    TextView btnBushou;
    @BindView(R.id.btn_pinyin)
    TextView btnPinyin;
    @BindView(R.id.question_tv)
    TextView question_tv;
    @BindView(R.id.question_tv_cover)
    FrameLayout question_tv_cover;
    @BindView(R.id.result_tv)
    TextView result_tv;
    @BindView(R.id.result_tv_cover)
    FrameLayout result_tv_cover;
    @BindView(R.id.copy_btn)
    FrameLayout copy_btn;
    @BindView(R.id.share_btn)
    FrameLayout share_btn;
    @BindView(R.id.chdic_sv)
    ScrollView chdic_sv;

    private View view;

    public static ChineseDictionaryFragment getInstance(FragmentProgressbarListener listener) {
        ChineseDictionaryFragment mMainFragment = new ChineseDictionaryFragment();
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
        view = inflater.inflate(R.layout.fragment_ch_dictionary, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getContext().getPackageName().equals(Setings.application_id_ywcd)) {
            btnBushou.setVisibility(View.GONE);
            btnPinyin.setVisibility(View.GONE);
        }
    }

    private void play(String content) {
        MyPlayer.getInstance(getContext()).start(content);
    }

    private void toPinyinActivity() {
        Intent intent = new Intent(getContext(), PyBsActivity.class);
        intent.putExtra(KeyUtil.CHDicType, 0);
        startActivity(intent);
    }

    private void toBushouActivity() {
        Intent intent = new Intent(getContext(), PyBsActivity.class);
        intent.putExtra(KeyUtil.CHDicType, 1);
        startActivity(intent);
    }

    private void copy() {
        String content = question_tv.getText().toString() +"\n"+ result_tv.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            ShareUtil.copy(getContext(), content);
        }
    }

    private void share() {
        String content = question_tv.getText().toString() +"\n"+ result_tv.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            ShareUtil.shareText(getContext(), content);
        }
    }

    private void onFinishRequest() {
        hideProgressbar();
    }

    private void RequestBaiduApi() {
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String url = Setings.CHDicBaiduApi + Setings.q;
                Response mResponse = LanguagehelperHttpClient.get(url);
                if (mResponse != null && mResponse.isSuccessful()) {
                    e.onNext(HtmlParseUtil.parseCHDicBaiduHtml(mResponse.body().string()));
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
                    public void onNext(String s) {
                        chdic_sv.scrollTo(0, 0);
                        question_tv.setText(Setings.q);
                        result_tv.setText(s);
                        EventBus.getDefault().post(new FinishEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        onFinishRequest();
                    }
                });

    }

    /**
     * submit request task
     */
    public void submit() {
        RequestBaiduApi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.question_tv_cover, R.id.result_tv_cover, R.id.copy_btn, R.id.share_btn, R.id.btn_bushou, R.id.btn_pinyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.question_tv_cover:
                play(question_tv.getText().toString());
                AVAnalytics.onEvent(getContext(), "chdic_play_question");
                break;
            case R.id.result_tv_cover:
                play(result_tv.getText().toString());
                AVAnalytics.onEvent(getContext(), "chdic_play_result");
                break;
            case R.id.copy_btn:
                copy();
                AVAnalytics.onEvent(getContext(), "chdic_copy");
                break;
            case R.id.share_btn:
                share();
                AVAnalytics.onEvent(getContext(), "chdic_share");
                break;
            case R.id.btn_bushou:
                toBushouActivity();
                AVAnalytics.onEvent(getContext(), "chdic_to_bushou");
                break;
            case R.id.btn_pinyin:
                toPinyinActivity();
                AVAnalytics.onEvent(getContext(), "chdic_to_pinyin");
                break;
        }
    }
}
