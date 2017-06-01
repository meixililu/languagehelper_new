package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVAnalytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.GytUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.contentx.ContExManager;

public class LeisureFragment extends BaseFragment {

    @BindView(R.id.yuedu_layout)
    FrameLayout yueduLayout;
    @BindView(R.id.twists_layout)
    FrameLayout twistsLayout;
    @BindView(R.id.xx_ad_layout)
    RelativeLayout xx_ad_layout;
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
    public LeisureFragment mMainFragment;
    private long lastTimeLoadAd;
    private NativeADDataRef mNativeADDataRef;
    private long lastLoadAd;
    private boolean exposure;

    public static LeisureFragment getInstance() {
        return new LeisureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.leisure_fragment, null);
        ButterKnife.bind(this, view);
        loadAD();
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

    private void loadAD() {
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
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if (adList != null && adList.size() > 0) {
                    mNativeADDataRef = adList.get(0);
                    setAd();
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private void setAd() {
        lastLoadAd = System.currentTimeMillis();
        xx_ad_layout.setVisibility(View.VISIBLE);
        adImg.setImageURI(mNativeADDataRef.getImage());
        if(misVisibleToUser){
            exposure = mNativeADDataRef.onExposured(xx_ad_layout);
            LogUtil.DefalutLog("setAd-exposure:"+exposure);
        }
    }

    private void exposedAd() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!exposure && mNativeADDataRef != null){
                    exposure = mNativeADDataRef.onExposured(xx_ad_layout);
                    LogUtil.DefalutLog("exposedAd-exposure:"+exposure);
                }else {
                    if(misVisibleToUser && lastLoadAd > 0){
                        if(System.currentTimeMillis() - lastLoadAd > 45000){
                            loadAD();
                        }
                    }
                }
            }
        },500);

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
                toCailingActivity();
                break;
            case R.id.baidu_layout:
                toJokeActivity();
                break;
            case R.id.sougou_layout:
                toSougoActivity();
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
                ContExManager.initWithAPPId(getActivity(), "f9136944-bc17-4cb1-9b14-ece9de91b39d", "w1461Eub");
                GytUtil.showHtml(getActivity(), getActivity().getResources().getString(R.string.leisuer_gyt));
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

    private void toCailingActivity() {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(KeyUtil.URL, Settings.CaiLingUrl);
        intent.putExtra(KeyUtil.ActionbarTitle, getActivity().getResources().getString(R.string.title_cailing));
        getActivity().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_tocailingpg_btn");
    }

    private void toSougoActivity() {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(KeyUtil.URL, Settings.SougoUrl);
        intent.putExtra(KeyUtil.ActionbarTitle, getActivity().getResources().getString(R.string.title_sougo_wechat));
        getActivity().startActivity(intent);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_sougo_wechat");
    }

    private void toYueduActivity() {
        toActivity(ReadingAndNewsActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_news_page");
    }

    private void toChineseDictionaryActivity() {
        toActivity(ChineseDictionaryActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_chinese_dic");
    }

}
