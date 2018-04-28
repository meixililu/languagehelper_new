package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.messi.languagehelper.util.LogUtil;
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
    @BindView(R.id.ad_img)
    SimpleDraweeView adImg;
    private NativeADDataRef mNativeADDataRef;
    private NativeExpressADView mTXADView;
    private long lastLoadAd;
    private boolean exposureXFAD;
    private boolean exposureTXAD;
    private String currentAD = ADUtil.Advertiser;

    public static LeisureFragment getInstance() {
        return new LeisureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.leisure_fragment, null);
        ButterKnife.bind(this, view);
        if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
            loadXFAD();
        }else {
            loadTXAD();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("LeisureFragment-setUserVisibleHint:"+isVisibleToUser);
        if(isVisibleToUser){
            exposedAd();
        }
    }

    private void loadXFAD() {
        LogUtil.DefalutLog("loadAD---leisure---xiu xian da tu");
        IFLYNativeAd nativeAd = new IFLYNativeAd(getContext(), ADUtil.XiuxianYSNRLAd, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
                if(ADUtil.Advertiser.equals(ADUtil.Advertiser_XF)){
                    loadTXAD();
                }else {
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

    private void onXFADFaile(){
        if(ADUtil.isHasLocalAd()){
            mNativeADDataRef = ADUtil.getRandomAd();
            setAd();
        }
    }

    private void loadTXAD(){
        TXADUtil.showCDT(getActivity(), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog(adError.getErrorMsg());
                if(ADUtil.Advertiser.equals(ADUtil.Advertiser_TX)){
                    loadXFAD();
                }else {
                    onXFADFaile();
                }
            }
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if(list != null && list.size() > 0){
                    exposureTXAD = false;
                    currentAD = ADUtil.Advertiser_TX;
                    if(mTXADView != null){
                        mTXADView.destroy();
                    }
                    xx_ad_layout.setVisibility(View.VISIBLE);
                    xx_ad_layout.removeAllViews();
                    mTXADView = list.get(0);
                    lastLoadAd = System.currentTimeMillis();
                    exposedTXADView();
                }
            }
            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onRenderFail");
            }
            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onRenderSuccess");
            }
            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADExposure");
                exposureTXAD = true;
            }
            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClicked");
            }
            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClosed");
                if(xx_ad_layout != null){
                    xx_ad_layout.removeAllViews();
                    xx_ad_layout.setVisibility(View.GONE);
                }
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

    private void exposedTXADView(){
        if(misVisibleToUser && !exposureTXAD && mTXADView != null){
            xx_ad_layout.addView(mTXADView);
            mTXADView.render();
        }
    }

    private void setAd() {
        if(mNativeADDataRef != null){
            lastLoadAd = System.currentTimeMillis();
            xx_ad_layout.setVisibility(View.VISIBLE);
            adImg.setImageURI(mNativeADDataRef.getImage());
            if(misVisibleToUser){
                exposedXFAD();
            }
        }
    }

    private void exposedXFAD(){
        if(!exposureXFAD && mNativeADDataRef != null){
            exposureXFAD = mNativeADDataRef.onExposured(xx_ad_layout);
            LogUtil.DefalutLog("exposedAd-exposureXFAD:"+ exposureXFAD);
        }
    }

    private void exposedAd() {
        if(currentAD.equals(ADUtil.Advertiser_XF)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exposedXFAD();
                }
            },500);

        }else {
            if(!exposureTXAD){
                exposedTXADView();
            }
        }
        if(misVisibleToUser && lastLoadAd > 0){
            if(System.currentTimeMillis() - lastLoadAd > 45000){
                loadTXAD();
            }
        }
    }

    @OnClick({R.id.xx_ad_layout, R.id.cailing_layout, R.id.baidu_layout, R.id.sougou_layout, R.id.yuedu_layout, R.id.twists_layout, R.id.game_layout, R.id.shenhuifu_layout, R.id.news_layout, R.id.app_layout, R.id.invest_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xx_ad_layout:
                if(mNativeADDataRef != null){
                    boolean onClicked = mNativeADDataRef.onClicked(view);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
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
                toActivity(EnglishWebsiteRecommendActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_websiterecommend");
                break;
            case R.id.app_layout:
                toChineseDictionaryActivity();
                break;
            case R.id.invest_layout:
                toInvestorListActivity();
                break;
        }
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

    private void toYueduActivity() {
        toActivity(ReadingAndNewsActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_news_page");
    }

    private void toChineseDictionaryActivity() {
        toActivity(ChineseDictionaryActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_chinese_dic");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTXADView != null){
            mTXADView.destroy();
        }
    }
}
