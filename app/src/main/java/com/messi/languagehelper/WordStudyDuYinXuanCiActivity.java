package com.messi.languagehelper;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.db.DataBaseUtil;
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

public class WordStudyDuYinXuanCiActivity extends BaseActivity implements OnFinishListener {

    @BindView(R.id.word_play_img)
    ImageView wordPlayImg;
    @BindView(R.id.word_play_layout)
    LinearLayout wordPlayLayout;
    @BindView(R.id.selection_1)
    TextView selection1;
    @BindView(R.id.selection_1_layout)
    FrameLayout selection1Layout;
    @BindView(R.id.selection_2)
    TextView selection2;
    @BindView(R.id.selection_2_layout)
    FrameLayout selection2Layout;
    @BindView(R.id.selection_3)
    TextView selection3;
    @BindView(R.id.selection_3_layout)
    FrameLayout selection3Layout;
    @BindView(R.id.selection_4)
    TextView selection4;
    @BindView(R.id.selection_4_layout)
    FrameLayout selection4Layout;
    @BindView(R.id.word_test_layout)
    LinearLayout wordTestLayout;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.try_again_layout)
    FrameLayout tryAgainLayout;
    @BindView(R.id.finish_test_layout)
    FrameLayout finishTestLayout;
    @BindView(R.id.result_layout)
    LinearLayout resultLayout;
    @BindView(R.id.progressBarCircularIndetermininate)
    ProgressBar progressBarCircularIndetermininate;
    @BindView(R.id.my_awesome_toolbar)
    Toolbar myAwesomeToolbar;
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
    private SoundPool ourSounds;
    private int answer_right;
    private int answer_wrong;
    private int totalSum;

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
        setContentView(R.layout.word_study_duyinxuanci_activity);
        ButterKnife.bind(this);
        initOrder();
        initViews();
        initializeSoundPool();
        setData();
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.dancitingxuan) + "(" + (index + 1) + "/" + totalSum + ")");
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
    }

    private void initOrder(){
        totalSum = WordStudyPlanDetailActivity.itemList.size();
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0);
        index = 0;
    }

    private void setData() {
        wordTestLayout.setVisibility(View.VISIBLE);
        resultLayout.setVisibility(View.GONE);
        if (index < randomPlayIndex.size()) {
            setActionBarTitle(this.getResources().getString(R.string.dancitingxuan) + "(" + (index + 1) + "/" + totalSum + ")");
            position = randomPlayIndex.get(index);
            List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
                    totalSum < 4 ? 10 : totalSum,
                    0, 3, position);
            if (tv_list.size() == 4) {
                if(totalSum > tv_list.get(0)){
                    selection1.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(0)).getName());
                }else {
                    selection1.setText(DataBaseUtil.getInstance().getBench().getName());
                }
                if(totalSum > tv_list.get(1)){
                    selection2.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(1)).getName());
                }else {
                    selection2.setText(DataBaseUtil.getInstance().getBench().getName());
                }
                if(totalSum > tv_list.get(2)){
                    selection3.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(2)).getName());
                }else {
                    selection3.setText(DataBaseUtil.getInstance().getBench().getName());
                }
                if(totalSum > tv_list.get(3)){
                    selection4.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(3)).getName());
                }else {
                    selection4.setText(DataBaseUtil.getInstance().getBench().getName());
                }
                selection1.setTextColor(getResources().getColor(R.color.text_black1));
                selection2.setTextColor(getResources().getColor(R.color.text_black1));
                selection3.setTextColor(getResources().getColor(R.color.text_black1));
                selection4.setTextColor(getResources().getColor(R.color.text_black1));
            }
            playDelay();
        }
    }

    private void playDelay() {
        if(mPlayer != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playSound();
                }
            }, 500);
        }
    }

    private void initializeSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            ourSounds = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            ourSounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        }
        answer_right = ourSounds.load(this, R.raw.answer_right, 1);
        answer_wrong = ourSounds.load(this, R.raw.answer_wrong, 1);
    }

    private void replay() {
        playTimes++;
        if (playTimes < 2) {
            playSound();
        } else {
            playTimes = 0;
            wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
        }
    }

    private void playSound() {
        try {
            if (isPlaying()) {
                wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
                stopPlay();
            } else {
                wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_pause_white));
                playItem(WordStudyPlanDetailActivity.itemList.get(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playItem(WordDetailListItem mAVObject) {
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

    private boolean isPlaying() throws Exception{
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
                            ToastUtil.diaplayMesShort(WordStudyDuYinXuanCiActivity.this,
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
        wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
    }

    @OnClick({R.id.word_play_layout, R.id.selection_1_layout, R.id.selection_2_layout,
            R.id.selection_3_layout, R.id.selection_4_layout,
            R.id.try_again_layout, R.id.finish_test_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.word_play_layout:
                playSound();
                break;
            case R.id.selection_1_layout:
                checkResultThenGoNext(selection1);
                break;
            case R.id.selection_2_layout:
                checkResultThenGoNext(selection2);
                break;
            case R.id.selection_3_layout:
                checkResultThenGoNext(selection3);
                break;
            case R.id.selection_4_layout:
                checkResultThenGoNext(selection4);
                break;
            case R.id.try_again_layout:
                tryAgain();
                break;
            case R.id.finish_test_layout:
                finish();
                break;
        }
    }

    private void tryAgain(){
        initOrder();
        WordStudyPlanDetailActivity.clearSign();
        setData();
    }

    private void checkResultThenGoNext(TextView tv) {
        String text = tv.getText().toString();
        if (index < WordStudyPlanDetailActivity.itemList.size()) {
            if (!WordStudyPlanDetailActivity.itemList.get(position).getName().equals(text)) {
                playSoundPool(false);
                WordStudyPlanDetailActivity.itemList.get(position).setSelect_Time();
                tv.setTextColor(getResources().getColor(R.color.material_color_red));
            } else {
                playSoundPool(true);
                tv.setTextColor(getResources().getColor(R.color.material_color_green));
                tv.setText(text + "\n" + WordStudyPlanDetailActivity.itemList.get(position).getDesc());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(index == totalSum - 1){
                            wordTestLayout.setVisibility(View.GONE);
                            resultLayout.setVisibility(View.VISIBLE);
                            countScoreAndShowResult();
                        }else {
                            index++;
                            setData();
                        }
                    }
                }, 1000);
            }
        }
    }

    private void playSoundPool(boolean isRight) {
        if(isRight){
            ourSounds.play(answer_right, 1, 1, 1, 0, 1);
        }else {
            ourSounds.play(answer_wrong, 1, 1, 1, 0, 1);
        }
    }

    private void countScoreAndShowResult() {
        setActionBarTitle(this.getResources().getString(R.string.word_test_result));
        double wrongCount = 0;
        resultList.clear();
        for (WordDetailListItem item : WordStudyPlanDetailActivity.itemList) {
            if (item.getSelect_time() > 0) {
                wrongCount++;
                resultList.add(item);
            }
        }
        DataBaseUtil.getInstance().saveList(resultList,true);
        for (WordDetailListItem item : WordStudyPlanDetailActivity.itemList) {
            if (item.getSelect_time() == 0) {
                resultList.add(item);
            }
        }
        int scoreInt = (int) ((totalSum - wrongCount) / totalSum * 100);
        score.setText(String.valueOf(scoreInt) + "分");
        if (scoreInt > 59) {
            score.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            score.setTextColor(this.getResources().getColor(R.color.red));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordStudyPlanDetailActivity.clearSign();
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
