package com.messi.languagehelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcDictionaryListAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.event.ProgressEvent;
import com.messi.languagehelper.event.TranAndDicRefreshEvent;
import com.messi.languagehelper.impl.DicHelperListener;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MD5;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
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
    NestedScrollView cidianResultLayout;
    @BindView(R.id.dic_result_layout)
    LinearLayout dicResultLayout;

    private RcDictionaryListAdapter mAdapter;
    private List<Dictionary> beans;
    private String lastSearch;
    private LinearLayoutManager mLinearLayoutManager;
    private int skip;
    private boolean noMoreData;
    private boolean isNeedRefresh;
    private View view;

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
        super.onCreateView(inflater,container,savedInstanceState);
        if (view != null) {
            ViewUtil.removeFromParentView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_dictionary_old, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        isRegisterBus = true;
        beans = new ArrayList<Dictionary>();
        loadData();
        mAdapter = new RcDictionaryListAdapter(getContext(), beans, this);
        recent_used_lv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recent_used_lv.setLayoutManager(mLinearLayoutManager);
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        mAdapter.setFooter(new Object());
        recent_used_lv.setAdapter(mAdapter);
        isShowRecentList(true);
        setListOnScrollListener();
    }

    public void setListOnScrollListener(){
        recent_used_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!noMoreData){
                    int visible  = mLinearLayoutManager.getChildCount();
                    int total = mLinearLayoutManager.getItemCount();
                    int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if ((visible + firstVisibleItem) >= total){
                        LogUtil.DefalutLog("should load more data");
                        loadData();
                    }
                }
            }
        });
    }

    public void loadData(){
        List<Dictionary> list = BoxHelper.getDictionaryList(skip, Setings.RecordOffset);
        if (NullUtil.isNotEmpty(list)) {
            beans.addAll(list);
            skip += Setings.RecordOffset;
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }else {
            noMoreData = true;
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
        lastSearch = Setings.q;
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
                boolean isEnglish = StringUtils.isEnglish(Setings.q);
                if(isEnglish){
                    mDictionaryBean.setFrom("en");
                    mDictionaryBean.setTo("zh");
                }else{
                    mDictionaryBean.setFrom("zh");
                    mDictionaryBean.setTo("en");
                }
                mDictionaryBean.setWord_name(Setings.q);
                mDictionaryBean.setResult(sb.substring(0, sb.lastIndexOf("\n")));
                setBean();
                BoxHelper.insert(mDictionaryBean);
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
        TranslateUtil.Translate_init(mHandler);
    }

    private void setData() {
        mDictionaryBean = (Dictionary) Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        setBean();
        EventBus.getDefault().post(new FinishEvent());
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

    public void refresh() {
        if (isNeedRefresh && mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            isNeedRefresh = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TranAndDicRefreshEvent event){
        reloadData();
        LogUtil.DefalutLog("---DictionaryFragmentOld---onEvent");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDictionaryBean = null;
    }

    private void reloadData() {
        isNeedRefresh = true;
        beans.clear();
        beans.addAll(BoxHelper.getDictionaryList(0, Setings.RecordOffset));
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
    public void playPcm(Dictionary mObject, boolean isPlayResult, String result) {
        playResult(mObject, isPlayResult, result);
    }

    private void playResult(final Dictionary mBean, boolean isPlayResult, String result) {
        String filepath = "";
        String speakContent = "";
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        if (isPlayResult) {
            speakContent = result;
            if (TextUtils.isEmpty(mBean.getResultVoiceId())) {
                mBean.setResultVoiceId(MD5.encode(speakContent));
            }
            filepath = path + mBean.getResultVoiceId() + ".pcm";
        } else {
            speakContent = mBean.getWord_name();
            if (TextUtils.isEmpty(mBean.getQuestionVoiceId())) {
                mBean.setQuestionVoiceId(MD5.encode(speakContent));
            }
            filepath = path + mBean.getQuestionVoiceId() + ".pcm";
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
        BoxHelper.update(mBean);
    }
}
