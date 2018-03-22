package com.messi.languagehelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcDictionaryListAdapter;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.event.ProgressEvent;
import com.messi.languagehelper.impl.DicHelperListener;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.views.DividerItemDecoration;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.youdao.sdk.ydtranslate.Translate;

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

public class DictionaryFragmentOld extends BaseFragment implements
        DictionaryTranslateListener, DicHelperListener {

    public static Dictionary mDictionaryBean;

    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;
    @BindView(R.id.cidian_result_layout)
    ScrollView cidianResultLayout;
    @BindView(R.id.dic_result_layout)
    LinearLayout dicResultLayout;

    private RcDictionaryListAdapter mAdapter;
    private List<Dictionary> beans;
    private String lastSearch;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onFinishRequest();
            if (msg.what == 1) {
                setData();
            } else {
                showToast(getContext().getResources().getString(R.string.network_error));
            }
        }
    };

    public static DictionaryFragmentOld getInstance(FragmentProgressbarListener listener) {
        DictionaryFragmentOld mMainFragment = new DictionaryFragmentOld();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.DefalutLog("MainFragment-onCreateView");
        View view = inflater.inflate(R.layout.fragment_dictionary_old, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        isRegisterBus = true;
        beans = new ArrayList<Dictionary>();
        beans.addAll(DataBaseUtil.getInstance().getDataListDictionary(0, Settings.offset));
        mAdapter = new RcDictionaryListAdapter(getContext(), beans, this);

        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(getContext()));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        mAdapter.setFooter(new Object());
        recent_used_lv.setAdapter(mAdapter);
        isShowRecentList(true);
    }

    public void refresh() {
        if (Settings.isDictionaryFragmentNeedRefresh) {
            Settings.isDictionaryFragmentNeedRefresh = false;
            reloadData();
        }
    }

    private void isShowRecentList(boolean value) {
        if (value) {
            recent_used_lv.setVisibility(View.VISIBLE);
            cidianResultLayout.setVisibility(View.GONE);
        } else {
            cidianResultLayout.setVisibility(View.VISIBLE);
            recent_used_lv.setVisibility(View.GONE);
        }
    }

    private void translateController(){
        lastSearch = Settings.q;
        if(NetworkUtil.isNetworkConnected(getContext())){
            LogUtil.DefalutLog("online");
            RequestTranslateApiTask();
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
                mDictionaryBean = new Dictionary();
                boolean isEnglish = StringUtils.isEnglish(Settings.q);
                if(isEnglish){
                    mDictionaryBean.setFrom("en");
                    mDictionaryBean.setTo("zh");
                }else{
                    mDictionaryBean.setFrom("zh");
                    mDictionaryBean.setTo("en");
                }
                mDictionaryBean.setWord_name(Settings.q);
                mDictionaryBean.setResult(sb.substring(0, sb.lastIndexOf("\n")));
                DataBaseUtil.getInstance().insert(mDictionaryBean);
                setBean();
            }
        }else{
            showToast("没找到离线词典，请到更多页面下载！");
        }
    }

    /**
     * send translate request
     * showapi dictionary api
     */
    private void RequestTranslateApiTask() {
        showProgressbar();
        TranslateUtil.Translate_init(getContext(), mHandler);
    }

    private void setData() {
        mDictionaryBean = (Dictionary) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        WXEntryActivity.dataMap.clear();
        setBean();
        NYBus.get().post(new FinishEvent());
    }

    private void setBean() {
        isShowRecentList(false);
        cidianResultLayout.scrollTo(0, 0);
        DictionaryHelper.addDicContent(getContext(), dicResultLayout, mDictionaryBean, this);
    }

    private void onFinishRequest() {
        hideProgressbar();
    }

    /**
     * toast message
     *
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
    public void showItem(Dictionary word) {
        mDictionaryBean = word;
        setBean();
    }

    private void reloadData() {
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                beans.clear();
                beans.addAll(DataBaseUtil.getInstance().getDataListDictionary(0, Settings.offset));
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
    public void playPcm(Dictionary mObject, boolean isPlayResult, String result) {
        playResult(mObject, isPlayResult, result);
    }

    private void playResult(final Dictionary mBean, boolean isPlayResult, String result) {
        String filepath = "";
        String speakContent = "";
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        if (isPlayResult) {
            if (TextUtils.isEmpty(mBean.getResultVoiceId())) {
                mBean.setResultVoiceId(System.currentTimeMillis() + 5 + "");
            }
            filepath = path + mBean.getResultVoiceId() + ".pcm";
            speakContent = result;
        } else {
            if (TextUtils.isEmpty(mBean.getQuestionVoiceId())) {
                mBean.setQuestionVoiceId(System.currentTimeMillis() + "");
            }
            filepath = path + mBean.getQuestionVoiceId() + ".pcm";
            speakContent = mBean.getWord_name();
        }
        PlayUtil.play(filepath, speakContent, null,
                new SynthesizerListener() {
                    @Override
                    public void onSpeakResumed() {
                    }

                    @Override
                    public void onSpeakProgress(int arg0, int arg1, int arg2) {
                    }

                    @Override
                    public void onSpeakPaused() {
                    }

                    @Override
                    public void onSpeakBegin() {
                        hideProgressbar();
                    }

                    @Override
                    public void onCompleted(SpeechError arg0) {
                        LogUtil.DefalutLog("---onCompleted");
                        if (arg0 != null) {
                            ToastUtil.diaplayMesShort(getContext(), arg0.getErrorDescription());
                        }
                        DataBaseUtil.getInstance().update(mBean);
                    }

                    @Override
                    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                        if (arg0 < 10) {
                            showProgressbar();
                        }
                    }

                    @Override
                    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
