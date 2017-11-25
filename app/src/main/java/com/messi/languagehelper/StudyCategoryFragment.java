package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcXmlyTagsAdapter;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    @BindView(R.id.study_spoken_english)
    FrameLayout studySpokenEnglish;
    @BindView(R.id.en_grammar)
    FrameLayout enGrammar;
    @BindView(R.id.en_story_layout)
    FrameLayout enStoryLayout;
    @BindView(R.id.en_broadcast)
    FrameLayout enBroadcast;
    @BindView(R.id.en_business)
    FrameLayout enBusiness;
    @BindView(R.id.search_layout)
    FrameLayout searchLayout;
    @BindView(R.id.ai_unread)
    ImageView aiUnread;
    @BindView(R.id.ximalaya_layout)
    FrameLayout ximalaya_layout;
    @BindView(R.id.fm_layout)
    RecyclerView fmLayout;

    private WordListItem wordListItem;
    private SharedPreferences sp;

    private RcXmlyTagsAdapter mAdapter;
    private List<Album> avObjects;
    private int skip = 1;
    private int ad_try_times = 1;
    private int max_page = 1;
    private IFLYNativeAd nativeAd;
    private boolean loading;
    private boolean hasMore = true;
    private AlbumForAd mADObject;
    private LinearLayoutManager mLinearLayoutManager;

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
        initViews();
        setData();
        setBookName();
        return view;
    }

    private void setData() {
        sp = Settings.getSharedPreferences(getActivity());
        if (!sp.getBoolean(KeyUtil.HasClickNewFunAi, false)) {
            aiUnread.setVisibility(View.VISIBLE);
        } else {
            aiUnread.setVisibility(View.GONE);
        }
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
        QueryTask();
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
            R.id.en_examination_layout, R.id.study_composition, R.id.instagram_layout, R.id.collected_layout,
            R.id.study_spoken_english, R.id.en_grammar, R.id.en_story_layout,
            R.id.en_broadcast, R.id.en_business, R.id.search_layout,
            R.id.ximalaya_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.word_study_view_all:
                toActivity(WordStudyActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_wordstudy_view_all");
                break;
            case R.id.word_study_daily:
                toActivity(VocabularyStudyActivity.class, null);
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
                toActivity(SymbolActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
                break;
            case R.id.study_listening_layout:
                toActivity(ListenSubjectActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_listening");
                break;
            case R.id.study_test:
                toActivity(AiActivity.class, null);
                aiUnread.setVisibility(View.GONE);
                Settings.saveSharedPreferences(sp, KeyUtil.HasClickNewFunAi, true);
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
                AVAnalytics.onEvent(getContext(), "tab3_to_collected");
                break;
            case R.id.study_spoken_english:
                toActivity(SpokenEnglishActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_spoken_english");
                break;
            case R.id.en_grammar:
                toActivity(GrammarActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_grammar");
                break;
            case R.id.en_story_layout:
                toActivity(StoryActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_story");
                break;
            case R.id.en_broadcast:
                toActivity(BroadcastActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_broadcast");
                break;
            case R.id.en_business:
                toActivity(BusinessActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_business");
                break;
            case R.id.search_layout:
                toActivity(SearchActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_search");
                break;
            case R.id.ximalaya_layout:
                toXmlyTagsActiviry();
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya");
                break;
        }
    }

    private void toXmlyTagsActiviry(){
        Intent intent = new Intent(getContext(),XimalayaTagsActiviry.class);
        intent.putExtra(KeyUtil.Category,XimalayaUtil.Category_Eng);
        startActivity(intent);
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

    private void toExaminationActivity(String title) {
        Intent intent = new Intent(getContext(), ExaminationActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getContext().startActivity(intent);
    }

    private void initViews(){
        fmLayout.setNestedScrollingEnabled(false);
        avObjects = new ArrayList<Album>();
        mAdapter = new RcXmlyTagsAdapter();
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        fmLayout.setLayoutManager(mLinearLayoutManager);
        fmLayout.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        fmLayout.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener(){
        fmLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                isADInList(recyclerView,firstVisibleItem,visible);
                LogUtil.DefalutLog("firstVisibleItem:"+firstVisibleItem+"---visible:"+visible+"---total:"+total);
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
//                        loadAD();
//                        QueryTask();
                        LogUtil.DefalutLog("OnScrollListener");
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view,int first, int vCount){
        if(avObjects.size() > 3){
            for(int i=first;i< (first+vCount);i++){
                if(i < avObjects.size() && i > 0){
                    Album mAVObject = avObjects.get(i);
                    if (mAVObject instanceof AlbumForAd) {
                        if(!((AlbumForAd)mAVObject).isAdShow()){
                            NativeADDataRef mNativeADDataRef = ((AlbumForAd)mAVObject).getmNativeADDataRef();
                            boolean isExposure = mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
                            LogUtil.DefalutLog("isExposure:"+isExposure);
                            ((AlbumForAd)mAVObject).setAdShow(isExposure);
                        }
                    }
                }
            }
        }
    }

    private void random(){
        if(max_page > 1){
            skip = new Random().nextInt(max_page) + 1;
        }else {
            skip = 1;
        }
        LogUtil.DefalutLog("random:"+skip);
    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }

    private void QueryTask(){
        loading = true;
        showProgressbar();
        Map<String ,String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID , XimalayaUtil.Category_Eng);
        map.put(DTransferConstants.CALC_DIMENSION ,"3");
        map.put(DTransferConstants.PAGE_SIZE ,String.valueOf(30));
        map.put(DTransferConstants.PAGE ,String.valueOf(skip));
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>(){
            @Override
            public void onSuccess(@Nullable AlbumList albumList) {
                onFinishLoadData();
                if(albumList != null && albumList.getAlbums() != null){
                    LogUtil.DefalutLog(albumList.toString());
                    avObjects.addAll( albumList.getAlbums() );
                    skip += 1;
                    if(addAD()){
                        mAdapter.notifyDataSetChanged();
                    }
                    if(skip > albumList.getTotalPage()){
                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        hideFooterview();
                        hasMore = false;
                    }else {
                        hasMore = true;
                        showFooterview();
                    }
                    max_page = albumList.getTotalPage();
                }
            }
            @Override
            public void onError(int i, String s) {
                onFinishLoadData();
                LogUtil.DefalutLog(s);
            }
        });
    }

    private void onFinishLoadData(){
        loading = false;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    private void loadAD(){
        nativeAd = new IFLYNativeAd(getContext(), ADUtil.XXLAD, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                if(ad_try_times > 0){
                    ad_try_times -= 1;
                    loadAD();
                }
                LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
            }
            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if(adList != null && adList.size() > 0){
                    NativeADDataRef nad = adList.get(0);
                    mADObject = new AlbumForAd();
                    mADObject.setmNativeADDataRef(nad);
                    mADObject.setAd(true);
                    if(!loading){
                        addAD();
                    }
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private boolean addAD(){
        if(mADObject != null && avObjects != null && avObjects.size() > 0){
            int index = avObjects.size() - Settings.page_size + NumberUtil.randomNumberRange(2, 4);
            if(index < 0){
                index = 0;
            }
            avObjects.add(index,mADObject);
            mAdapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcast();
    }

}
