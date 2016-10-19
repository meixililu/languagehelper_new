package com.messi.languagehelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyFragment extends Fragment implements OnClickListener {

    @BindView(R.id.symbol_study_cover)
    TextView symbol_study_cover;
    @BindView(R.id.daily_sentence_item_img)
    SimpleDraweeView daily_sentence_item_img;
    @BindView(R.id.daily_sentence_list_item_middle)
    LinearLayout daily_sentence_list_item_middle;
    @BindView(R.id.dailysentence_txt)
    TextView dailysentence_txt;
    @BindView(R.id.play_img)
    ImageView play_img;
    @BindView(R.id.study_daily_sentence)
    FrameLayout study_daily_sentence;
    @BindView(R.id.word_study_cover)
    FrameLayout word_study_cover;
    @BindView(R.id.study_listening_layout)
    FrameLayout study_listening_layout;
    @BindView(R.id.news_layout)
    FrameLayout news_layout;
    @BindView(R.id.study_spoken_english)
    FrameLayout study_spoken_english;
    @BindView(R.id.study_test)
    FrameLayout study_test;
    @BindView(R.id.en_examination_layout)
    FrameLayout en_examination_layout;
    @BindView(R.id.study_composition)
    FrameLayout study_composition;
    @BindView(R.id.juhe_layout)
    FrameLayout juhe_layout;
    @BindView(R.id.jokes_layout)
    FrameLayout jokes_layout;
    @BindView(R.id.instagram_layout)
    FrameLayout instagram_layout;
    @BindView(R.id.story_layout)
    FrameLayout story_layout;

    public static StudyFragment mMainFragment;
    private SharedPreferences mSharedPreferences;
    private EveryDaySentence mEveryDaySentence;
    private FragmentProgressbarListener mProgressbarListener;
    private MediaPlayer mPlayer;
    private String fileFullName;
    private boolean isInitMedia;

    public static StudyFragment getInstance() {
        if (mMainFragment == null) {
            mMainFragment = new StudyFragment();
        }
        return mMainFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_fragment, null);
        ButterKnife.bind(this, view);
        initViews();
        getDailySentence();
        isLoadDailySentence();
        return view;
    }

    private void initViews() {
        mPlayer = new MediaPlayer();
        mSharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        study_daily_sentence.setOnClickListener(this);
        study_spoken_english.setOnClickListener(this);
        instagram_layout.setOnClickListener(this);
        study_composition.setOnClickListener(this);
        study_test.setOnClickListener(this);
        symbol_study_cover.setOnClickListener(this);
        word_study_cover.setOnClickListener(this);
        news_layout.setOnClickListener(this);
        story_layout.setOnClickListener(this);
        jokes_layout.setOnClickListener(this);
        juhe_layout.setOnClickListener(this);
        study_listening_layout.setOnClickListener(this);
        en_examination_layout.setOnClickListener(this);
        play_img.setOnClickListener(this);
    }

    private void getDailySentence() {
        List<EveryDaySentence> mList = DataBaseUtil.getInstance().getDailySentenceList(1);
        if (mList != null) {
            if (mList.size() > 0) {
                mEveryDaySentence = mList.get(0);
            }
        }
        setSentence();
        LogUtil.DefalutLog("StudyFragment-getDailySentence()");
    }

    private void isLoadDailySentence() {
        String todayStr = TimeUtil.getTimeDateLong(System.currentTimeMillis());
        long cid = NumberUtil.StringToLong(todayStr);
        boolean isExist = DataBaseUtil.getInstance().isExist(cid);
        if (!isExist) {
            requestDailysentence();
        }
        LogUtil.DefalutLog("StudyFragment-isLoadDailySentence()");
    }

    private void requestDailysentence() {
        LogUtil.DefalutLog("StudyFragment-requestDailysentence()");
        LanguagehelperHttpClient.get(Settings.DailySentenceUrl, new UICallback(getActivity()) {
            public void onResponsed(String responseString) {
                if (JsonParser.isJson(responseString)) {
                    mEveryDaySentence = JsonParser.parseEveryDaySentence(responseString);
                    setSentence();
                }
            }
        });
    }

    private void setSentence() {
        LogUtil.DefalutLog("StudyFragment-setSentence()");
        if (mEveryDaySentence != null) {
            dailysentence_txt.setText(mEveryDaySentence.getContent());
            daily_sentence_item_img.setImageURI(Uri.parse(mEveryDaySentence.getPicture2()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symbol_study_cover:
                toSymbolListActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_to_symbol");
                break;
            case R.id.word_study_cover:
                toWordStudyListActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_to_wordstudy");
                break;
            case R.id.study_daily_sentence:
                toDailySentenceActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_to_dailysentence");
                break;
            case R.id.study_spoken_english:
                toStudyListActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_study_spokenenglish_basic");
                break;
            case R.id.study_composition:
                toStudyCompositionActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_to_composition");
                break;
            case R.id.study_test:
                toEvaluationActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_to_evaluation");
                break;
            case R.id.instagram_layout:
                toEnglishWebsiteRecommendActivity();
                AVAnalytics.onEvent(getActivity(), "tab3_to_websiterecommend");
                break;
            case R.id.story_layout:
                toReadingActivity(getActivity().getResources().getString(R.string.title_english_story),
                        AVOUtil.Category.story);
                AVAnalytics.onEvent(getActivity(), "tab3_to_story");
                break;
            case R.id.news_layout:
                toReadingActivity(getActivity().getResources().getString(R.string.reading),
                        AVOUtil.Category.shuangyu_reading);
                AVAnalytics.onEvent(getActivity(), "tab3_to_reading");
                break;
            case R.id.jokes_layout:
                toReadingActivity(getActivity().getResources().getString(R.string.title_english_video),
                        "", "video");
                AVAnalytics.onEvent(getActivity(), "tab3_to_videos");
                break;
            case R.id.study_listening_layout:
                toReadingActivity(getActivity().getResources().getString(R.string.title_listening),
                        AVOUtil.Category.listening);
                AVAnalytics.onEvent(getActivity(), "tab3_to_listening");
                break;
            case R.id.juhe_layout:
                toReadingJuheActivity(getActivity().getResources().getString(R.string.title_juhe));
                AVAnalytics.onEvent(getActivity(), "tab3_to_juhenews");
                break;
            case R.id.en_examination_layout:
                toExaminationActivity(getActivity().getResources().getString(R.string.examination));
                AVAnalytics.onEvent(getActivity(), "tab3_to_examination");
                break;
            case R.id.play_img:
                if (mEveryDaySentence != null) {
                    int pos = mEveryDaySentence.getTts().lastIndexOf(SDCardUtil.Delimiter) + 1;
                    String fileName = mEveryDaySentence.getTts().substring(pos, mEveryDaySentence.getTts().length());
                    fileFullName = SDCardUtil.getDownloadPath(SDCardUtil.DailySentencePath) + fileName;
                    LogUtil.DefalutLog("fileName:" + fileName + "---fileFullName:" + fileFullName);
                    if (SDCardUtil.isFileExist(fileFullName)) {
                        playMp3(fileFullName);
                        LogUtil.DefalutLog("FileExist");
                    } else {
                        LogUtil.DefalutLog("FileNotExist");
                        loadding();
                        DownLoadUtil.downloadFile(getContext(), mEveryDaySentence.getTts(), SDCardUtil.DailySentencePath, fileName, mHandler);
                    }
                }
                AVAnalytics.onEvent(getActivity(), "tab3_play_daily_sentence");
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                finishLoadding();
                playMp3(fileFullName);
            } else if (msg.what == 2) {
                finishLoadding();
            }
        }
    };

    private void playMp3(String uriPath) {
        try {
            if (mEveryDaySentence != null && !isInitMedia) {
                isInitMedia = true;
                play_img.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_48dp);
                Uri uri = Uri.parse(uriPath);
                mPlayer.setDataSource(getActivity(), uri);
                mPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_img.setBackgroundResource(R.drawable.ic_play_circle_outline_white_48dp);
                    }
                });
                mPlayer.prepare();
                mPlayer.start();
            } else {
                if (mPlayer.isPlaying()) {
                    play_img.setBackgroundResource(R.drawable.ic_play_circle_outline_white_48dp);
                    mPlayer.pause();
                } else {
                    play_img.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_48dp);
                    mPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toReadingActivity(String title, String category) {
        Intent intent = new Intent(getActivity(), ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        intent.putExtra(KeyUtil.Category, category);
        getActivity().startActivity(intent);
    }

    private void toReadingActivity(String title, String category, String type) {
        Intent intent = new Intent(getActivity(), ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        intent.putExtra(KeyUtil.Category, category);
        intent.putExtra(KeyUtil.NewsType, type);
        getActivity().startActivity(intent);
    }

    private void toReadingJuheActivity(String title) {
        Intent intent = new Intent(getActivity(), ReadingJuheActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getActivity().startActivity(intent);
    }

    private void toExaminationActivity(String title) {
        Intent intent = new Intent(getActivity(), ExaminationActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, title);
        getActivity().startActivity(intent);
    }

    private void toEnglishWebsiteRecommendActivity() {
        Intent intent = new Intent(getActivity(), EnglishWebsiteRecommendActivity.class);
        getActivity().startActivity(intent);
    }

    private void toSymbolListActivity() {
        Intent intent = new Intent(getActivity(), SymbolListActivity.class);
        startActivity(intent);
    }

    private void toWordStudyListActivity() {
        Intent intent = new Intent(getActivity(), WordStudyActivity.class);
        startActivity(intent);
    }

    private void toDailySentenceActivity() {
        Intent intent = new Intent(getActivity(), DailySentenceActivity.class);
        startActivity(intent);
    }

    private void toStudyListActivity() {
        Intent intent = new Intent(getActivity(), PracticeCategoryActivity.class);
        startActivity(intent);
    }

    private void toStudyCompositionActivity() {
        Intent intent = new Intent(getActivity(), CompositionActivity.class);
        startActivity(intent);
    }

    private void toEvaluationActivity() {
        Intent intent = new Intent(getActivity(), SpokenEnglishActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                play_img.setBackgroundResource(R.drawable.ic_play_circle_outline_white_48dp);
                mPlayer.pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    public void loadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.showProgressbar();
        }
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    public void finishLoadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.hideProgressbar();
        }
    }

}
