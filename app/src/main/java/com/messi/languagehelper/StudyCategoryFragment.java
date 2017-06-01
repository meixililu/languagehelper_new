package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.bean.EssayData;
import com.messi.languagehelper.bean.EssayRoot;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyCategoryFragment extends BaseFragment {


    @BindView(R.id.symbol_study_cover)
    FrameLayout symbolStudyCover;
    @BindView(R.id.study_listening_layout)
    FrameLayout studyListeningLayout;
    @BindView(R.id.study_test)
    FrameLayout studyTest;
    @BindView(R.id.en_examination_layout)
    FrameLayout enExaminationLayout;
    @BindView(R.id.study_composition)
    FrameLayout studyComposition;
    @BindView(R.id.instagram_layout)
    FrameLayout instagramLayout;
    @BindView(R.id.collected_layout)
    FrameLayout collectedLayout;
    @BindView(R.id.word_study_change_plan)
    FrameLayout wordStudyChangePlan;
    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;
    @BindView(R.id.word_study_plan)
    RelativeLayout wordStudyPlan;
    @BindView(R.id.word_study_view_all)
    FrameLayout wordStudyViewAll;
    @BindView(R.id.word_study_daily)
    FrameLayout wordStudyDaily;
    @BindView(R.id.word_study_new_word)
    FrameLayout wordStudyNewWord;
    @BindView(R.id.word_study_book_name)
    TextView wordStudyBookName;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.imgs_1)
    SimpleDraweeView imgs1;
    @BindView(R.id.imgs_2)
    SimpleDraweeView imgs2;
    @BindView(R.id.imgs_3)
    SimpleDraweeView imgs3;
    @BindView(R.id.imgs_layout)
    LinearLayout imgsLayout;
    @BindView(R.id.source_name)
    TextView sourceName;
    @BindView(R.id.type_name)
    TextView typeName;
    @BindView(R.id.ad_layout)
    FrameLayout adLayout;
    @BindView(R.id.essay_tv)
    TextView essayTv;
    @BindView(R.id.essay_prompt)
    TextView essayPrompt;
    @BindView(R.id.essay_layout)
    FrameLayout essayLayout;

    private WordListItem wordListItem;
    private NativeADDataRef mNativeADDataRef;
    private long lastLoadAd;
    private EssayData mEssayData;
    private boolean exposure;

    public static StudyCategoryFragment getInstance() {
        return new StudyCategoryFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            registerBroadcast();
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.study_category_fragment, null);
        ButterKnife.bind(this, view);
        setBookName();
        requestData();
        return view;
    }

    @Override
    public void updateUI(String music_action) {
        if (music_action.equals(PlayerService.action_loading)) {
            showProgressbar();
        } else if (music_action.equals(PlayerService.action_finish_loading)) {
            hideProgressbar();
        }
    }

    private void setBookName() {
        wordListItem = SaveData.getDataFonJson(getContext(), KeyUtil.WordStudyUnit, WordListItem.class);
        if (wordListItem != null && wordStudyBookName != null) {
            wordStudyBookName.setText(wordListItem.getTitle());
            arcProgress.setMax(wordListItem.getCourse_num());
            arcProgress.setProgress(wordListItem.getCourse_id());
        }
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        loadAD();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.DefalutLog("StudyCategoryFragment-setUserVisibleHint:"+isVisibleToUser);
        if(isVisibleToUser){
            exposedAd();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setBookName();
    }

    @OnClick({R.id.word_study_view_all, R.id.word_study_daily, R.id.symbol_study_cover,
            R.id.word_study_new_word,
            R.id.study_listening_layout, R.id.word_study_change_plan,
            R.id.word_study_plan, R.id.study_test,
            R.id.en_examination_layout, R.id.study_composition, R.id.instagram_layout,
            R.id.collected_layout, R.id.ad_layout,R.id.essay_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.word_study_view_all:
                toActivity(WordStudyActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_view_all");
                break;
            case R.id.word_study_daily:
                toReadingActivity(getContext().getResources().getString(R.string.title_word_vocabulary),
                        AVOUtil.Category.word);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_daily");
                break;
            case R.id.word_study_change_plan:
                toActivity(WordStudyPlanActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_change_plan");
                break;
            case R.id.word_study_new_word:
                toActivity(WordStudyNewWordActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_new_word");
                break;
            case R.id.word_study_plan:
                toWordStudyDetailActivity();
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_detail");
                break;
            case R.id.symbol_study_cover:
                toActivity(SymbolListActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
                break;
            case R.id.study_listening_layout:
                toActivity(ListenActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_listening");
                break;
            case R.id.study_test:
                toActivity(JichuxiulianAndKouyulianxiActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_evaluation");
                break;
            case R.id.en_examination_layout:
                toExaminationActivity(getContext().getResources().getString(R.string.examination));
                AVAnalytics.onEvent(getContext(), "tab3_to_examination");
                break;
            case R.id.study_composition:
                toActivity(CompositionActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_composition");
                break;
            case R.id.instagram_layout:
                toActivity(EnglishWebsiteRecommendActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_websiterecommend");
                break;
            case R.id.collected_layout:
                toActivity(CollectedActivity.class, null);
                AVAnalytics.onEvent(getContext(), "index_pg_to_collectedpg");
                break;
            case R.id.ad_layout:
                if (mNativeADDataRef != null) {
                    boolean onClicked = mNativeADDataRef.onClicked(view);
                    LogUtil.DefalutLog("onClicked:"+onClicked);
                }
                break;
            case R.id.essay_layout:
                if(mEssayData != null){
                    if (!mEssayData.isShowResult()) {
                        essayTv.setText(mEssayData.getEnglish() + "\n" + mEssayData.getChinese());
                        essayPrompt.setText("轻触更新下一条");
                        mEssayData.setShowResult(true);
                    } else {
                        requestData();
                    }
                }
                break;
        }
    }

    private void toWordStudyDetailActivity() {
        if (wordListItem != null) {
            Intent intent = new Intent(getContext(), WordStudyPlanDetailActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle, wordListItem.getTitle());
            getActivity().startActivity(intent);
        } else {
            toActivity(WordStudyPlanActivity.class, null);
        }
    }

    private void toReadingActivity(String title, String category) {
        Intent intent = new Intent(getContext(), ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        intent.putExtra(KeyUtil.Category, category);
        getContext().startActivity(intent);
    }

    private void toExaminationActivity(String title) {
        Intent intent = new Intent(getContext(), ExaminationActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getContext().startActivity(intent);
    }

    private void loadAD() {
        LogUtil.DefalutLog("loadAD---study---san wen yi tu");
        IFLYNativeAd nativeAd = new IFLYNativeAd(getContext(), ADUtil.SanTuYiWen, new IFLYNativeListener() {
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
        adLayout.setVisibility(View.VISIBLE);
        title.setText(mNativeADDataRef.getTitle());
        typeName.setText(mNativeADDataRef.getSubTitle());
        sourceName.setText("VoiceAds广告");
        if (mNativeADDataRef.getImgUrls() != null && mNativeADDataRef.getImgUrls().size() > 2) {
            imgs1.setImageURI(mNativeADDataRef.getImgUrls().get(0));
            imgs2.setImageURI(mNativeADDataRef.getImgUrls().get(1));
            imgs3.setImageURI(mNativeADDataRef.getImgUrls().get(2));
        }
        exposure = mNativeADDataRef.onExposured(adLayout);
        LogUtil.DefalutLog("setAd-exposure:"+exposure);
    }

    private void exposedAd() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!exposure && mNativeADDataRef != null){
                    exposure = mNativeADDataRef.onExposured(adLayout);
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

    private void requestData() {
        final RequestBody formBody = new FormEncodingBuilder()
                .add("showapi_appid", Settings.showapi_appid)
                .add("showapi_sign", Settings.showapi_secret)
                .add("showapi_timestamp", String.valueOf(System.currentTimeMillis()))
                .add("showapi_res_gzip", "1")
                .add("count", "1")
                .build();
        LanguagehelperHttpClient.post(Settings.EssayApi, formBody, new UICallback(getActivity()) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    if (JsonParser.isJson(responseString)) {
                        EssayRoot mRoot = JSON.parseObject(responseString, EssayRoot.class);
                        if (mRoot != null && mRoot.getShowapi_res_code() == 0 && mRoot.getShowapi_res_body() != null) {
                            if (mRoot.getShowapi_res_body().getData() != null && mRoot.getShowapi_res_body().getData().size() > 0) {
                                essayLayout.setVisibility(View.VISIBLE);
                                mEssayData = mRoot.getShowapi_res_body().getData().get(0);
                                essayTv.setText(mEssayData.getEnglish());
                                essayPrompt.setText("轻触看中文");
                            }
                        }else {
                            essayLayout.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailured() {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcast();
    }

}
