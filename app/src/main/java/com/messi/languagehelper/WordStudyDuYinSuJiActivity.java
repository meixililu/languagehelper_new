package com.messi.languagehelper;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.impl.OnFinishListener;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyDuYinSuJiActivity extends BaseActivity implements OnFinishListener {


    @BindView(R.id.word_name)
    TextView wordName;
    @BindView(R.id.word_mean)
    TextView wordMean;
    @BindView(R.id.word_play_img)
    ImageView wordPlayImg;
    @BindView(R.id.word_suji_layout)
    FrameLayout wordSujiLayout;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.try_again_layout)
    FrameLayout tryAgainLayout;
    @BindView(R.id.finish_test_layout)
    FrameLayout finishTestLayout;
    @BindView(R.id.result_layout)
    LinearLayout resultLayout;
    @BindView(R.id.word_symbol)
    TextView wordSymbol;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private MediaPlayer mPlayer;
    private String fullName;
    private int playTimes;

    private RcWordStudyCiYiXuanCiAdapter adapter;
    private List<WordDetailListItem> resultList;

    private List<Integer> randomPlayIndex;
    private int index;
    private int position;
    private boolean isPlaying;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                playMp3();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_duyinsuji_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.light_green);
        initViews();
        setData();
        isPlaying = true;
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.dancisuji) + "(" + (index + 1) + "/" + WordStudyFragment.itemList.size() + ")");
        mPlayer = new MediaPlayer();
        class_name = getIntent().getStringExtra(KeyUtil.ClassName);
        class_id = getIntent().getStringExtra(KeyUtil.ClassId);
        course_id = getIntent().getIntExtra(KeyUtil.CourseId, 1);
        course_num = getIntent().getIntExtra(KeyUtil.CourseNum, 0);
        resultList = new ArrayList<WordDetailListItem>();
        adapter = new RcWordStudyCiYiXuanCiAdapter();
        adapter.setItems(resultList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        listview.setAdapter(adapter);
        PlayUtil.setOnFinishListener(this);
        initOrder();
    }

    private void initOrder() {
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(WordStudyFragment.itemList.size() - 1, 0);
        index = 0;
    }

    private void setData() {
        wordSujiLayout.setVisibility(View.VISIBLE);
        resultLayout.setVisibility(View.GONE);
        if (index < randomPlayIndex.size()) {
            setActionBarTitle(this.getResources().getString(R.string.dancisuji) + "(" + (index + 1) + "/" + WordStudyFragment.itemList.size() + ")");
            position = randomPlayIndex.get(index);
            clearWord();
            playDelay();
        }
    }

    private void clearWord() {
        wordName.setText("");
        wordSymbol.setText("");
        wordMean.setText("");
    }

    private void playDelay() {
        if (mPlayer != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playSound();
                }
            }, 500);
        }
    }

    private void replay() {
        playTimes++;
        if (playTimes < 3) {
            playSound();
        } else {
            playTimes = 0;
            checkResultThenGoNext();
        }
    }

    private void playSound() {
        if (isPlaying) {
            wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_pause_white));
            playItem(WordStudyFragment.itemList.get(position));
        }
    }

    private void playItem(WordDetailListItem mAVObject) {
        if (playTimes == 1) {
            wordName.setText(mAVObject.getName());
            wordSymbol.setText(mAVObject.getSymbol());
            wordMean.setText("");
        } else if (playTimes == 2) {
            wordName.setText(mAVObject.getName());
            wordSymbol.setText(mAVObject.getSymbol());
            wordMean.setText(mAVObject.getDesc());
        } else {
            clearWord();
        }
        if (TextUtils.isEmpty(mAVObject.getSound()) || mAVObject.getSound().equals("http://app1.showapi.com/en_word")) {
            playWithSpeechSynthesizer(mAVObject);
        } else {
            String mp3Name = mAVObject.getSound().substring(mAVObject.getSound().lastIndexOf("/") + 1);
            fullName = SDCardUtil.getDownloadPath(getAudioPath(mAVObject)) + mp3Name;
            if (!SDCardUtil.isFileExist(fullName)) {
                DownLoadUtil.downloadFile(this, mAVObject.getSound(), getAudioPath(mAVObject), mp3Name, mHandler);
            } else {
                playMp3();
            }
        }
    }

    private String getAudioPath(WordDetailListItem mAVObject){
        return SDCardUtil.WordStudyPath + mAVObject.getClass_id() + SDCardUtil.Delimiter +
                String.valueOf(mAVObject.getCourse()) + SDCardUtil.Delimiter;

    }

    public void playMp3() {
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            Uri uri = Uri.parse(fullName);
            mPlayer.setDataSource(this, uri);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    replay();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPlaying() {
        return mPlayer.isPlaying() || PlayUtil.isPlaying;
    }

    private void playWithSpeechSynthesizer(WordDetailListItem mAVObject) {
        String filepath = SDCardUtil.getDownloadPath(getAudioPath(mAVObject)) + mAVObject.getItem_id() + ".pcm";
        PlayUtil.play(filepath, mAVObject.getName(), null,
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
                        PlayUtil.onStartPlay();
                    }

                    @Override
                    public void onCompleted(SpeechError arg0) {
                        if (arg0 != null) {
                            ToastUtil.diaplayMesShort(WordStudyDuYinSuJiActivity.this,
                                    arg0.getErrorDescription());
                        }
                        stopPlay();
                        replay();
                    }

                    @Override
                    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                    }

                    @Override
                    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                    }
                });
    }

    public void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
        }
        PlayUtil.stopPlay();
        wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
    }

    @OnClick({R.id.word_suji_layout, R.id.try_again_layout, R.id.finish_test_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.word_suji_layout:
                isPlaying = !isPlaying;
                if (!isPlaying) {
                    stopPlay();
                } else {
                    playSound();
                }
                break;
            case R.id.try_again_layout:
                tryAgain();
                break;
            case R.id.finish_test_layout:
                finish();
                break;
        }
    }

    private void tryAgain() {
        initOrder();
        WordStudyFragment.clearSign();
        setData();
    }

    private void checkResultThenGoNext() {
        if (index < WordStudyFragment.itemList.size()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index == WordStudyFragment.itemList.size() - 1) {
                        wordPlayImg.setImageDrawable(WordStudyDuYinSuJiActivity.this.getResources().
                                getDrawable(R.drawable.ic_play_arrow_white_48dp));
                        wordSujiLayout.setVisibility(View.GONE);
                        resultLayout.setVisibility(View.VISIBLE);
                        countScoreAndShowResult();
                    } else {
                        index++;
                        setData();
                    }
                }
            }, 400);
        }
    }

    private void countScoreAndShowResult() {
        setActionBarTitle(this.getResources().getString(R.string.dancisuji));
        resultList.clear();
        for (WordDetailListItem item : WordStudyFragment.itemList) {
            resultList.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        PlayUtil.stopPlay();
        PlayUtil.clearFinishListener();
    }

    @Override
    public void OnFinish() {
        replay();
    }

}
