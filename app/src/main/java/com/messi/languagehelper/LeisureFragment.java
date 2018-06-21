package com.messi.languagehelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVAnalytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TXADUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeisureFragment extends BaseFragment {

    @BindView(R.id.yuedu_layout)
    FrameLayout yueduLayout;
    @BindView(R.id.twists_layout)
    FrameLayout twistsLayout;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
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
    @BindView(R.id.wyyx_layout)
    FrameLayout wyyx_layout;
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
    @BindView(R.id.root_view)
    NestedScrollView rootView;
    private NativeADDataRef mNativeADDataRef;
    private NativeExpressADView mTXADView;
    private long lastLoadAd;
    private boolean exposureXFAD;
    private String currentAD = ADUtil.Advertiser;
    private SharedPreferences sp;

    public static LeisureFragment getInstance() {
        return new LeisureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String channel = Settings.getMetaData(getContext(),"UMENG_CHANNEL");
        View view = null;
        if(!TextUtils.isEmpty(channel) && channel.equals("tencent")){
            view = inflater.inflate(R.layout.leisure_fragment_for_tx, null);
        }else {
            view = inflater.inflate(R.layout.leisure_fragment, null);
        }
        ButterKnife.bind(this, view);
        sp = Settings.getSharedPreferences(getContext());
        if(ADUtil.IsShowAD){
            if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
                loadXFAD();
            }else {
                loadTXAD();
            }
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("LeisureFragment-setUserVisibleHint:" + isVisibleToUser);
        if (isVisibleToUser) {
            exposedAd();
        }
    }

    private void loadXFAD() {
        LogUtil.DefalutLog("loadAD---leisure---xiu xian da tu");
        IFLYNativeAd nativeAd = new IFLYNativeAd(getContext(), ADUtil.MRYJYSNRLAd, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
                if (ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)) {
                    loadTXAD();
                } else {
                    onXFADFaile();
                }
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if (adList != null && adList.size() > 0) {
                    currentAD = ADUtil.Advertiser_XF;
                    exposureXFAD = false;
                    mNativeADDataRef = adList.get(0);
                    setAd();
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private void onXFADFaile() {
        if (ADUtil.isHasLocalAd()) {
            mNativeADDataRef = ADUtil.getRandomAd();
            setAd();
        }
    }

    private void loadTXAD() {
        TXADUtil.showCDT(getActivity(), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog(adError.getErrorMsg());
                if (ADUtil.Advertiser.equals(ADUtil.Advertiser_TX)) {
                    loadXFAD();
                } else {
                    onXFADFaile();
                }
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if (list != null && list.size() > 0) {
                    currentAD = ADUtil.Advertiser_TX;
                    if (mTXADView != null) {
                        mTXADView.destroy();
                    }
                    xx_ad_layout.setVisibility(View.VISIBLE);
                    xx_ad_layout.removeAllViews();
                    mTXADView = list.get(0);
                    lastLoadAd = System.currentTimeMillis();
                    xx_ad_layout.addView(mTXADView);
                    mTXADView.render();
                }
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                nativeExpressADView.render();
                LogUtil.DefalutLog("onRenderFail");
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onRenderSuccess");
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADExposure");
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClicked");
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClosed");
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADLeftApplication");
            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADOpenOverlay");
            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADCloseOverlay");
            }
        });
    }

    private void setAd() {
        if (mNativeADDataRef != null) {
            lastLoadAd = System.currentTimeMillis();
            xx_ad_layout.setVisibility(View.VISIBLE);
            adImg.setImageURI(mNativeADDataRef.getImage());
            if (misVisibleToUser) {
                exposedXFAD();
            }
        }
    }

    private void exposedXFAD() {
        if (!exposureXFAD && mNativeADDataRef != null) {
            exposureXFAD = mNativeADDataRef.onExposured(xx_ad_layout);
            LogUtil.DefalutLog("exposedAd-exposureXFAD:" + exposureXFAD);
        }
    }

    private void exposedAd() {
        LogUtil.DefalutLog("exposedAd");
        if (currentAD.equals(ADUtil.Advertiser_XF)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exposedXFAD();
                }
            }, 500);

        }
        if (misVisibleToUser && lastLoadAd > 0) {
            if (System.currentTimeMillis() - lastLoadAd > 45000) {
                loadTXAD();
            }
        }
    }

    @OnClick({R.id.xx_ad_layout, R.id.cailing_layout, R.id.baidu_layout, R.id.sougou_layout, R.id.yuedu_layout,
            R.id.twists_layout, R.id.game_layout, R.id.shenhuifu_layout, R.id.news_layout, R.id.app_layout,
            R.id.invest_layout, R.id.wyyx_layout, R.id.search_layout,R.id.novel_layout, R.id.caricature_layout,
            R.id.jd_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xx_ad_layout:
                if (mNativeADDataRef != null) {
                    boolean onClicked = mNativeADDataRef.onClicked(view);
                    LogUtil.DefalutLog("onClicked:" + onClicked);
                }
                break;
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
                toYueduActivity();
                break;
            case R.id.twists_layout:
                toActivity(BrainTwistsActivity.class, null);
                AVAnalytics.onEvent(getActivity(), "leisure_pg_to_braintwists");
                break;
            case R.id.game_layout:
                toGameCenterActivity();
                break;
            case R.id.shenhuifu_layout:
                toGodReplyActivity();
                break;
            case R.id.news_layout:
                toEnglishRecommendWebsite();
                AVAnalytics.onEvent(getContext(), "tab3_to_websiterecommend");
                break;
            case R.id.app_layout:
                toChineseDictionaryActivity();
                break;
            case R.id.invest_layout:
                toInvestorListActivity();
                break;
            case R.id.wyyx_layout:
                toWYYX();
                break;
            case R.id.search_layout:
                toUCSearch();
                break;
            case R.id.novel_layout:
                toNovelActivity();
                break;
            case R.id.caricature_layout:
                toCaricatureActivity();
                break;
            case R.id.jd_layout:
                toVideoActivity();
                break;
        }
    }

    private void toEnglishRecommendWebsite() {
        Intent intent = new Intent(getContext(), WebsiteListActivity.class);
        intent.putExtra(KeyUtil.Category, "english");
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.title_website));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_english_website");
    }

    private void toWYYX() {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(KeyUtil.URL, getWYYXUrl());
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisure_wyyx));
        getContext().startActivity(intent);
    }

    private void toJokeActivity() {
        toActivity(JokeActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_joke_page");
    }

    private void toGodReplyActivity() {
        toActivity(GodReplyActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_joke_page");
    }

    private void toInvestorListActivity() {
        toActivity(InvestListActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_toinvestpg_btn");
    }

    private void toGameCenterActivity() {
        toActivity(YiZhanDaoDiActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_toyizhandaodi_btn");
    }

    private void toShoppingActivity() {
        Intent intent = new Intent(getActivity(), AdEnglishActivity.class);
        getActivity().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_toshopping");
    }

    private void toTuringActivity() {
        toActivity(AiTuringActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_turing");
    }

    private void toUCSearch() {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(KeyUtil.URL, getUCSearchUrl());
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisure_search));
        getContext().startActivity(intent);
    }

    private void toYueduActivity() {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(KeyUtil.URL, getUCTTUrl());
        intent.putExtra(KeyUtil.IsReedPullDownRefresh, false);
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisuer_reading));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_news_page");
    }

    private void toChineseDictionaryActivity() {
        toActivity(LearnCodingActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_chinese_dic");
    }

    private void toNovelActivity() {
        Intent intent = new Intent(getContext(), WebsiteListActivity.class);
        intent.putExtra(KeyUtil.Category, "novel");
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisure_novel));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_novel");
    }

    private void toCaricatureActivity() {
        Intent intent = new Intent(getContext(), WebsiteListActivity.class);
        intent.putExtra(KeyUtil.Category, "caricature");
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisure_caricature));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_caricature");
    }

    private void toVideoActivity() {
        Intent intent = new Intent(getContext(), WebsiteListActivity.class);
        intent.putExtra(KeyUtil.Category, "dvideo");
        intent.putExtra(KeyUtil.ActionbarTitle, getContext().getResources().getString(R.string.leisure_dvideo));
        getContext().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_dvideo");
    }

    private String getWYYXUrl(){
        return sp.getString(KeyUtil.Lei_WYYX_URL,Settings.WYYX);
    }

    private String getUCTTUrl(){
        return sp.getString(KeyUtil.Lei_UCTT,Settings.UCTT);
    }

    private String getUCSearchUrl(){
        return sp.getString(KeyUtil.Lei_UCSearch,Settings.UCSearch);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTXADView != null) {
            mTXADView.destroy();
        }
    }

}
