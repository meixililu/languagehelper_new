package com.messi.languagehelper;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyDuYinXuanCiActivity extends BaseActivity {

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
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private MediaPlayer mPlayer;
    private String audioPath;
    private String fullName;

    private List<Integer> randomPlayIndex;
    private int index;
    private int position;
    private int playTimes;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                playMp3();
            } else if (msg.what == MyThread.EVENT_PLAY_OVER) {
                replay();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_duyinxuanci_activity);
        ButterKnife.bind(this);
        initViews();
        setData();
    }

    private void playDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playSound();
            }
        }, 800);
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.dancitingxuan) + "(" + (index + 1) + "/" + WordStudyPlanDetailActivity.itemList.size() + ")");
        mPlayer = new MediaPlayer();
        class_name = getIntent().getStringExtra(KeyUtil.ClassName);
        class_id = getIntent().getStringExtra(KeyUtil.ClassId);
        course_id = getIntent().getIntExtra(KeyUtil.CourseId, 1);
        course_num = getIntent().getIntExtra(KeyUtil.CourseNum, 0);
        if (!TextUtils.isEmpty(class_id)) {
            audioPath = SDCardUtil.WordStudyPath + class_id + SDCardUtil.Delimiter + String.valueOf(course_id) + SDCardUtil.Delimiter;
        }
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(WordStudyPlanDetailActivity.itemList.size() - 1, 0);
        index = 0;
    }

    private void setData() {
        if (index < randomPlayIndex.size()) {
            setActionBarTitle(this.getResources().getString(R.string.dancitingxuan) + "(" + (index + 1) + "/" + WordStudyPlanDetailActivity.itemList.size() + ")");
            position = randomPlayIndex.get(index);
            List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(WordStudyPlanDetailActivity.itemList.size(),
                    0, 3, position);
            if (tv_list.size() == 4) {
                selection1.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(0)).getName());
                selection2.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(1)).getName());
                selection3.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(2)).getName());
                selection4.setText(WordStudyPlanDetailActivity.itemList.get(tv_list.get(3)).getName());
                selection1.setTextColor(getResources().getColor(R.color.text_black1));
                selection2.setTextColor(getResources().getColor(R.color.text_black1));
                selection3.setTextColor(getResources().getColor(R.color.text_black1));
                selection4.setTextColor(getResources().getColor(R.color.text_black1));
            }
            playDelay();
        }
    }

    private void replay() {
        playTimes++;
        if (playTimes < 2) {
            playSound();
        }else {
            playTimes = 0;
            wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
        }
    }

    private void playSound() {
        if (isPlaying()) {
            wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
            stopPlay();
        } else {
            wordPlayImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_stop_white_48dp));
            playItem(WordStudyPlanDetailActivity.itemList.get(position));
        }
    }

    private void playItem(WordDetailListItem mAVObject) {
        if (TextUtils.isEmpty(mAVObject.getSound()) || mAVObject.getSound().equals("http://app1.showapi.com/en_word")) {
            playWithSpeechSynthesizer(mAVObject);
        } else {
            String mp3Name = mAVObject.getSound().substring(mAVObject.getSound().lastIndexOf("/") + 1);
            fullName = SDCardUtil.getDownloadPath(audioPath) + mp3Name;
            if (!SDCardUtil.isFileExist(fullName)) {
                DownLoadUtil.downloadFile(this, mAVObject.getSound(), audioPath, mp3Name, mHandler);
            } else {
                playMp3();
            }
        }
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
        String filepath = SDCardUtil.getDownloadPath(audioPath) + mAVObject.getItem_id() + ".pcm";
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
                        PlayUtil.onFinishPlay();
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

    @OnClick({R.id.word_play_layout, R.id.selection_1_layout, R.id.selection_2_layout, R.id.selection_3_layout, R.id.selection_4_layout})
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
        }
    }

    private void checkResultThenGoNext(TextView tv) {
        String text = tv.getText().toString();
        if (index < WordStudyPlanDetailActivity.itemList.size()) {
            if (!WordStudyPlanDetailActivity.itemList.get(position).getName().equals(text)) {
                WordStudyPlanDetailActivity.itemList.get(position).setSelect_Time();
                tv.setTextColor(getResources().getColor(R.color.material_color_red));
            }else {
                tv.setTextColor(getResources().getColor(R.color.material_color_green));
                tv.setText(text + "\n" + WordStudyPlanDetailActivity.itemList.get(position).getDesc());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index++;
                        setData();
                    }
                },1500);
            }
        } else {
//            resultLayout.setVisibility(View.VISIBLE);
//            countScoreAndShowResult();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordStudyPlanDetailActivity.clearSign();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        PlayUtil.stopPlay();
    }
}
