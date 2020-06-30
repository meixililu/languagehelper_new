package com.messi.languagehelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.EveryDaySentence;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyCategoryFragment extends BaseFragment {

    @BindView(R.id.symbol_study_cover)
    FrameLayout symbolStudyCover;
    @BindView(R.id.study_listening_layout)
    FrameLayout studyListeningLayout;
    @BindView(R.id.en_examination_layout)
    FrameLayout enExaminationLayout;
    @BindView(R.id.study_composition)
    FrameLayout studyComposition;
    @BindView(R.id.study_word_layout)
    FrameLayout studyWordLayout;

    @BindView(R.id.collected_layout)
    FrameLayout collectedLayout;
    @BindView(R.id.study_spoken_english)
    FrameLayout studySpokenEnglish;
    @BindView(R.id.en_grammar)
    FrameLayout enGrammar;
    @BindView(R.id.en_story_layout)
    FrameLayout enStoryLayout;
    @BindView(R.id.xmly_layout)
    FrameLayout xmlyLayout;

    @BindView(R.id.dailysentence_txt)
    TextView dailysentence_txt;
    @BindView(R.id.daily_sentence_unread_dot)
    ImageView daily_sentence_unread_dot;

    @BindView(R.id.study_daily_sentence)
    FrameLayout study_daily_sentence;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.daily_sentence_item_img)
    SimpleDraweeView daily_sentence_item_img;

    private EveryDaySentence mEveryDaySentence;
    private SharedPreferences sp;

    public static StudyCategoryFragment getInstance() {
        return new StudyCategoryFragment();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
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
        init();
        return view;
    }

    private void init(){
        sp = Setings.getSharedPreferences(getContext());
        getDailySentence();
        isLoadDailySentence();
        if (sp.getBoolean(KeyUtil.IsShowDailyEnglishUnread,true)) {
            daily_sentence_unread_dot.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.symbol_study_cover, R.id.study_listening_layout,
            R.id.en_examination_layout, R.id.study_composition, R.id.collected_layout,
            R.id.study_spoken_english, R.id.en_grammar, R.id.en_story_layout,
            R.id.xmly_layout, R.id.study_word_layout, R.id.study_daily_sentence})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.symbol_study_cover:
                toActivity(SymbolActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_symbol");
                break;
            case R.id.study_listening_layout:
                toActivity(ListenActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_listening");
                break;
            case R.id.en_examination_layout:
                toActivity(AiChatActivity.class,null);
                AVAnalytics.onEvent(getContext(), "tab3_to_examination");
                break;
            case R.id.study_composition:
                toActivity(ComExamActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_composition");
                break;
            case R.id.collected_layout:
                toActivity(CollectedActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_collected");
                break;
            case R.id.study_spoken_english:
                toActivity(SpokenActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_spoken_english");
                break;
            case R.id.en_grammar:
                toActivity(MomentsActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_grammar");
                break;
            case R.id.en_story_layout:
                toActivity(StoryActivity.class, null);
                AVAnalytics.onEvent(getContext(), "tab3_to_story");
                break;
            case R.id.xmly_layout:
                toActivity(XmlyActivity.class,null);
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_home");
                break;
            case R.id.study_word_layout:
                toActivity(WordsActivity.class,null);
                AVAnalytics.onEvent(getContext(), "tab3_to_ximalaya_home");
                break;
            case R.id.study_daily_sentence:
                toDailyEnglishActivity();
                break;
        }
    }

    private void toDailyEnglishActivity(){
        daily_sentence_unread_dot.setVisibility(View.GONE);
        Setings.saveSharedPreferences(sp, KeyUtil.IsShowDailyEnglishUnread,false);
        toActivity(DailyEnglishActivity.class,null);
        AVAnalytics.onEvent(getContext(), "tab3_to_dailysentence");
    }

    private void getDailySentence(){
        LogUtil.DefalutLog("StudyCategoryFragment-getDailySentence()");
        List<EveryDaySentence> mList = BoxHelper.getEveryDaySentenceList(1);
        if(mList != null){
            if(!mList.isEmpty()){
                mEveryDaySentence = mList.get(0);
                setSentence();
            }
        }
    }

    private void isLoadDailySentence(){
        String todayStr = TimeUtil.getTimeDateLong(System.currentTimeMillis());
        long cid = NumberUtil.StringToLong(todayStr);
        boolean isExist = BoxHelper.isEveryDaySentenceExist(cid);
        if(!isExist){
            requestDailysentence();
        }
        LogUtil.DefalutLog("StudyCategoryFragment-isLoadDailySentence()");
    }

    private void requestDailysentence(){
        LogUtil.DefalutLog("StudyCategoryFragment-requestDailysentence()");
        LanguagehelperHttpClient.get(Setings.DailySentenceUrl, new UICallback(getActivity()){
            public void onResponsed(String responseString) {
                if(JsonParser.isJson(responseString)){
                    mEveryDaySentence = JsonParser.parseEveryDaySentence(responseString);
                    setSentence();
                }
            }
        });
    }

    private void setSentence(){
        LogUtil.DefalutLog("StudyCategoryFragment-setSentence()");
        if(mEveryDaySentence != null){
            dailysentence_txt.setText(mEveryDaySentence.getContent());
            daily_sentence_item_img.setImageURI(Uri.parse(mEveryDaySentence.getPicture2()));
        }
    }

//    private void getXMLYRecommandData(){
//        Map<String,String> map = new HashMap<>();
//        map.put("page",String.valueOf(1));
//        map.put("count",String.valueOf(10));
//        CommonRequest.baseGetRequest(Setings.XMLYApiRoot + "/operation/recommend_albums", map, new IDataCallBack<AlbumList>() {
//            @Override
//            public void onSuccess(@Nullable AlbumList list) {
//                LogUtil.DefalutLog("list:"+list.getAlbums());
//            }
//            @Override
//            public void onError(int i, String s) {
//                LogUtil.DefalutLog("onError:"+s);
//            }
//        }, new CommonRequest.IRequestCallBack<AlbumList>() {
//            @Override
//            public AlbumList success(String s) throws Exception {
//                return JSON.parseObject(s,AlbumList.class);
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
