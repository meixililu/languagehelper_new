package com.messi.languagehelper;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLRootModel;
import com.messi.languagehelper.bean.ADBean;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.faxian.CharadesFragment;
import com.messi.languagehelper.faxian.ConjectureFragment;
import com.messi.languagehelper.faxian.EssayFragment;
import com.messi.languagehelper.faxian.HistoryFragment;
import com.messi.languagehelper.faxian.ProverbFragment;
import com.messi.languagehelper.faxian.RiddleFragment;
import com.messi.languagehelper.faxian.TongueTwisterFragment;
import com.messi.languagehelper.faxian.TwistsFragment;
import com.messi.languagehelper.faxian.XHYFragment;
import com.messi.languagehelper.faxian.YZDDFragment;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ContextUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.viewmodels.SingleBigBannerViewModel;

import java.util.List;

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

public class LeisureFragment extends BaseFragment {

    @BindView(R.id.ad_sign)
    TextView ad_sign;
    @BindView(R.id.yuedu_layout)
    FrameLayout yueduLayout;
    @BindView(R.id.twists_layout)
    FrameLayout twistsLayout;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.ad_layout)
    FrameLayout ad_layout;
    @BindView(R.id.cailing_layout)
    FrameLayout cailing_layout;
    @BindView(R.id.baidu_layout)
    FrameLayout baidu_layout;
    @BindView(R.id.app_layout)
    FrameLayout app_layout;
    @BindView(R.id.news_layout)
    FrameLayout news_layout;
    @BindView(R.id.game_layout)
    FrameLayout game_layout;
    @BindView(R.id.invest_layout)
    FrameLayout invest_layout;
    @BindView(R.id.sougou_layout)
    FrameLayout sougou_layout;
    @BindView(R.id.shenhuifu_layout)
    FrameLayout shenhuifuLayout;
    @BindView(R.id.layout_riddle)
    FrameLayout layout_riddle;
    @BindView(R.id.search_layout)
    FrameLayout search_layout;
    @BindView(R.id.ad_img)
    SimpleDraweeView adImg;
    @BindView(R.id.novel_layout)
    FrameLayout novelLayout;
    @BindView(R.id.caricature_layout)
    FrameLayout caricatureLayout;
    @BindView(R.id.jd_layout)
    FrameLayout jdLayout;
    @BindView(R.id.english_essay_layout)
    FrameLayout english_essay_layout;
    @BindView(R.id.layout_whyy)
    FrameLayout layout_whyy;
    @BindView(R.id.layout_conjecture)
    FrameLayout layout_conjecture;
    @BindView(R.id.layout_history)
    FrameLayout layout_history;
    @BindView(R.id.layout_xiehouyu)
    FrameLayout layout_xiehouyu;
    @BindView(R.id.layout_tongue_twister)
    FrameLayout layout_tongue_twister;
    @BindView(R.id.ksearch_layout)
    FrameLayout ksearch_layout;
    @BindView(R.id.cnk_layout)
    LinearLayout cnk_layout;
    @BindView(R.id.root_view)
    NestedScrollView rootView;

    private SharedPreferences sp;
    private boolean exposureXFAD;
    private SingleBigBannerViewModel viewModel;
    private NativeDataRef mNativeADDataRef;
    public long lastLoadAd;

    public static LeisureFragment getInstance() {
        return new LeisureFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("LeisureFragment-setUserVisibleHint:" + isVisibleToUser);
        if (isVisibleToUser) {
            exposedAd();
            changeAd();
        }
    }

    private void changeAd(){
        if (lastLoadAd > 0) {
            if (System.currentTimeMillis() - lastLoadAd > 15*1000) {
                if(viewModel != null){
                    mNativeADDataRef = null;
                    viewModel.loadData();
                    lastLoadAd = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SingleBigBannerViewModel.class);
        viewModel.init();
        lastLoadAd = System.currentTimeMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = null;
        if (getContext().getPackageName().equals(Setings.application_id_zyhy) ||
                getContext().getPackageName().equals(Setings.application_id_zyhy_google) ||
                getContext().getPackageName().equals(Setings.application_id_yyj) ||
                getContext().getPackageName().equals(Setings.application_id_yyj_google)) {
            view = inflater.inflate(R.layout.leisure_fragment, null);
        } else if (getContext().getPackageName().equals(Setings.application_id_ywcd) ) {
            view = inflater.inflate(R.layout.leisure_for_ywcd_fragment, null);
        } else {
            if("baidu".equals(Setings.appChannel)){
                view = inflater.inflate(R.layout.leisure_for_ywcd_fragment, null);
            }else {
                view = inflater.inflate(R.layout.leisure_for_yys_fragment, null);
            }
        }
        ButterKnife.bind(this, view);
        bandAd();
        showNovel();
        return view;
    }

    private void bandAd(){
        sp = Setings.getSharedPreferences();
        if(ADUtil.IsShowAD){
            xx_ad_layout.setVisibility(View.VISIBLE);
            viewModel.getADBean().observe(this,adBean -> onReceiveAd(adBean));
        }else {
            xx_ad_layout.setVisibility(View.GONE);
        }
    }

    private void onReceiveAd(ADBean adBean){
        LogUtil.DefalutLog("---onReceiveAd---");
        if (adBean != null && adBean.getStatus() == 1) {
            if(ADUtil.GDT.equals(adBean.getType())){
                initFeiXFAD();
                ad_layout.addView(adBean.getTXADView());
                adBean.getTXADView().render();
            }else if(ADUtil.CSJ.equals(adBean.getType())){
                initFeiXFAD();
                XXLRootModel.setCSJDView(ContextUtil.get().getContext(),adBean.getTTFeedAd(),ad_layout);
            }else if(ADUtil.XF.equals(adBean.getType())){
                exposureXFAD = false;
                setAd(adBean.mNativeADDataRef);
            }else if(ADUtil.XBKJ.equals(adBean.getType())){
                setAd(adBean.getNativeADDataRef());
            }
        } else {
            xx_ad_layout.setVisibility(View.GONE);
        }
    }

    public void setAd(NativeDataRef mADDataRef) {
        if (mADDataRef != null) {
            mNativeADDataRef = mADDataRef;
            ad_sign.setVisibility(View.VISIBLE);
            adImg.setVisibility(View.VISIBLE);
            ad_layout.setVisibility(View.GONE);
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse(mNativeADDataRef.getImgUrl()))
                    .build();
            adImg.setController(mDraweeController);
            xx_ad_layout.setOnClickListener(view -> onXFADClick(mADDataRef));
            exposedXFAD(mADDataRef);
        }
    }

    public void onXFADClick(NativeDataRef mADDataRef){
        if (mADDataRef != null) {
            boolean onClicked = mADDataRef.onClick(xx_ad_layout);
            LogUtil.DefalutLog("onClicked:" + onClicked);
        }
    }

    public void exposedXFAD(NativeDataRef mADDataRef) {
        if (!exposureXFAD) {
            exposureXFAD = mADDataRef.onExposure(xx_ad_layout);
            LogUtil.DefalutLog("exposedAd-exposureXFAD:" + exposureXFAD);
        }
    }

    public void exposedAd() {
        LogUtil.DefalutLog("exposedAd");
        if (mNativeADDataRef != null) {
            new Handler().postDelayed(() -> exposedXFAD(mNativeADDataRef),500);
        }
    }

    public void initFeiXFAD(){
        ad_sign.setVisibility(View.GONE);
        adImg.setVisibility(View.GONE);
        ad_layout.setVisibility(View.VISIBLE);
        ad_layout.removeAllViews();
    }

    @OnClick({R.id.cailing_layout, R.id.baidu_layout, R.id.sougou_layout, R.id.yuedu_layout,
            R.id.twists_layout, R.id.game_layout, R.id.shenhuifu_layout, R.id.news_layout, R.id.app_layout,
            R.id.invest_layout, R.id.layout_riddle, R.id.search_layout,R.id.novel_layout, R.id.caricature_layout,
            R.id.jd_layout,R.id.english_essay_layout,R.id.layout_whyy,R.id.layout_conjecture,R.id.layout_history,
            R.id.layout_xiehouyu,R.id.layout_tongue_twister,R.id.layout_meiriyiju,R.id.ksearch_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cailing_layout:
                toShoppingActivity();
                break;
            case R.id.baidu_layout:
                toJokeActivity();
                break;
            case R.id.sougou_layout:
                toTuringActivity();
                break;
            case R.id.yuedu_layout:
                toXVedio();
                break;
            case R.id.twists_layout:
                toTwists();
                break;
            case R.id.game_layout:
                toYZDD();
                break;
            case R.id.shenhuifu_layout:
                toGodReply();
                break;
            case R.id.news_layout:
                toEnglishRecommendWebsite();
                break;
            case R.id.app_layout:
                toLearnToCode();
                break;
            case R.id.invest_layout:
                toCharades();
                break;
            case R.id.layout_riddle:
                toRiddle();
                break;
            case R.id.search_layout:
                toUCSearch();
                break;
            case R.id.novel_layout:
                toNovelActivity();
                break;
            case R.id.ksearch_layout:
                toKSearch();
                break;
            case R.id.caricature_layout:
                toCaricatureActivity();
                break;
            case R.id.layout_meiriyiju:
                toMeiriyiju();
                break;
            case R.id.jd_layout:
                toDVideo();
                break;
            case R.id.english_essay_layout:
                toEssay();
                break;
            case R.id.layout_conjecture:
                toConjecture();
                break;
            case R.id.layout_history:
                toHistory();
                break;
            case R.id.layout_xiehouyu:
                toXHY();
                break;
            case R.id.layout_tongue_twister:
                toTongueTwister();
                break;
            case R.id.layout_whyy:
                toProverb();
                break;
        }
    }

    private void toMeiriyiju(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KeyUtil.FragmentName,DailySentenceFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.dailysentence));
        toActivity(EmptyActivity.class,bundle);
    }

    private void toTongueTwister(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, TongueTwisterFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisuer_tongue_twister));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toXHY(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, XHYFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisuer_xhy));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toHistory(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, HistoryFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisure_history));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toProverb(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, ProverbFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisure_whyy));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toRiddle(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, RiddleFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisure_riddle));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toConjecture(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, ConjectureFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisure_conjecture));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }


    private void toTwists(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, TwistsFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisuer_twists));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toGodReply(){
        toActivity(MoreActivity.class,null);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_god_reply");
    }

    private void toEssay(){
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, EssayFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.english_essay));
        toActivity(EmptyActivity.class,bundle);
    }

    private void toXVedio() {
        Intent intent = new Intent(getContext(), XVideoHomeActivity.class);
        if (getContext().getPackageName().equals(Setings.application_id_yys)){
            intent.putExtra(KeyUtil.Category, "推荐");
            intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.title_xvideo_yys));
        }else {
            intent.putExtra(KeyUtil.Category, "英语");
            intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.title_xvideo));
        }
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_xvideo");
    }

    private void toEnglishRecommendWebsite() {
        Intent intent = new Intent(getContext(), WebsiteListActivity.class);
        intent.putExtra(KeyUtil.Category, "english");
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.title_website));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_english_website");
    }

    private void toDVideo() {
        Intent intent = new Intent(getContext(), PhotoSearchActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisure_photo_search));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_dvideo");
    }

    private void toJokeActivity() {
        toActivity(JokeActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_joke_page");
    }

    private void toKSearch() {
        toActivity(CNSearchActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_ksearch");
    }

    private void toCharades() {
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, CharadesFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisuer_charades));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_toinvestpg_btn");
    }

    private void toYZDD() {
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.StyleKey,"black");
        bundle.putSerializable(KeyUtil.FragmentName, YZDDFragment.class);
        bundle.putString(KeyUtil.ActionbarTitle,getResources().getString(R.string.leisuer_yizhandaodi));
        toActivity(EmptyActivity.class,bundle);
//        AVAnalytics.onEvent(getActivity(), "leisure_pg_toyizhandaodi_btn");
    }

    private void toShoppingActivity() {
        Intent intent = new Intent(getActivity(), AdEnglishActivity.class);
        getActivity().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_toshopping");
    }

    private void toTuringActivity() {
        toActivity(AiUCXYActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_turing");
    }

    private void toUCSearch() {
        Intent intent = new Intent(getContext(), WebViewWithMicActivity.class);
        intent.putExtra(KeyUtil.URL, getUCSearchUrl());
        intent.putExtra(KeyUtil.SearchUrl, Setings.UCSearchUrl);
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_uc_search");
    }

    private void toLearnToCode() {
        toActivity(CollegeActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_learn_coding");
    }

    private void toNovelActivity() {
        Intent intent = new Intent(getContext(), WebViewForNovelActivity.class);
        intent.putExtra(KeyUtil.URL, getNovelUrl());
        intent.putExtra(KeyUtil.FilterName, "小米小说");
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_novel");
    }

    private void toCaricatureActivity() {
        toActivity(CaricatureMainActivity.class,null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_caricature");
    }

    private String getDVideoUrl(){
        return sp.getString(KeyUtil.Lei_DVideo,Setings.DVideo);
    }

    private String getNovelUrl(){
        return sp.getString(KeyUtil.Lei_Novel,Setings.XMNovel);
    }

    private String getUCSearchUrl(){
        return sp.getString(KeyUtil.Lei_UCSearch,Setings.UCSearch);
    }


    private void showNovel(){
        int type = sp.getInt(KeyUtil.ShowCNK,0);
        if(type == 1) {
            cnk_layout.setVisibility(View.VISIBLE);
        }else if(type == 2){
            cnk_layout.setVisibility(View.GONE);
        }else {
            showNovelBackground();
        }
    }

    private void showNovelBackground(){
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    String result = "hide";
                    String history_str = sp.getString(KeyUtil.CaricatureSearchHistory,"");
                    if(!TextUtils.isEmpty(history_str)){
                        result = "show";
                    }else {
                        List<CNWBean> list = BoxHelper.getCollectedList(AVOUtil.Caricature.Caricature,
                                0, 5);
                        if(NullUtil.isNotEmpty(list)){
                            result = "show";
                        }
                    }
                    e.onNext(result);
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
                            if("show".equals(s)){
                                Setings.saveSharedPreferences(sp,KeyUtil.ShowCNK,1);
                                cnk_layout.setVisibility(View.VISIBLE);
                            }else {
                                Setings.saveSharedPreferences(sp,KeyUtil.ShowCNK,2);
                                cnk_layout.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }

}
