package com.messi.languagehelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.GytUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.XFYSAD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.contentx.ContExManager;

public class LeisureFragment extends BaseFragment implements OnClickListener {

    public static final long IntervalTime = 1000 * 20;
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
    public static LeisureFragment mMainFragment;
    @BindView(R.id.shenhuifu_layout)
    FrameLayout shenhuifuLayout;
    private XFYSAD mXFYSAD;
    private boolean misVisibleToUser;

    public static LeisureFragment getInstance() {
        if (mMainFragment == null) {
            mMainFragment = new LeisureFragment();
        }
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leisure_fragment, null);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        cailing_layout.setOnClickListener(this);
        invest_layout.setOnClickListener(this);
        baidu_layout.setOnClickListener(this);
        app_layout.setOnClickListener(this);
        game_layout.setOnClickListener(this);
        news_layout.setOnClickListener(this);
        yueduLayout.setOnClickListener(this);
        sougou_layout.setOnClickListener(this);
        shenhuifuLayout.setOnClickListener(this);
        mXFYSAD = new XFYSAD(getActivity(), xx_ad_layout, ADUtil.XiuxianYSNRLAd);
        mXFYSAD.setStopPlay(true);
        mXFYSAD.showAD();
        if (misVisibleToUser) {
            mXFYSAD.startPlayImg();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("LeisureFragment---setUserVisibleHint:" + isVisibleToUser);
        misVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (mXFYSAD != null) {
                mXFYSAD.startPlayImg();
            }
        } else {
            if (mXFYSAD != null) {
                mXFYSAD.canclePlayImg();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (misVisibleToUser) {
            LogUtil.DefalutLog("LeisureFragment---onResume");
            if (mXFYSAD != null) {
                mXFYSAD.startPlayImg();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (misVisibleToUser) {
            LogUtil.DefalutLog("LeisureFragment---onPause");
            if (mXFYSAD != null) {
                mXFYSAD.canclePlayImg();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cailing_layout) {
            toCailingActivity();
        } else if (v.getId() == R.id.yuedu_layout) {
            toYueduActivity();
        } else if (v.getId() == R.id.sougou_layout) {
            toSougoActivity();
        } else if (v.getId() == R.id.app_layout) {
            toChineseDictionaryActivity();
        } else if (v.getId() == R.id.invest_layout) {
            toInvestorListActivity();
        } else if (v.getId() == R.id.game_layout) {
            toGameCenterActivity();
        } else if (v.getId() == R.id.baidu_layout) {
            toJokeActivity();
        } else if (v.getId() == R.id.news_layout) {
            ContExManager.initWithAPPId(getActivity(), "f9136944-bc17-4cb1-9b14-ece9de91b39d", "w1461Eub");
            GytUtil.showHtml(getActivity(), getActivity().getResources().getString(R.string.leisuer_gyt));
        } else if (v.getId() == R.id.shenhuifu_layout) {
            toGodReplyActivity();
        }
    }

    @OnClick(R.id.twists_layout)
    public void onClick() {
        toActivity(BrainTwistsActivity.class, null);
        AVAnalytics.onEvent(getActivity(), "leisure_pg_to_braintwists");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mXFYSAD != null) {
            mXFYSAD.canclePlayImg();
            mXFYSAD = null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
