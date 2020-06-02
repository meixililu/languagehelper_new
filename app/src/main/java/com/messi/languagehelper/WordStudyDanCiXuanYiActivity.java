package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.impl.OnFinishListener;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MD5;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyDanCiXuanYiActivity extends BaseActivity implements OnFinishListener {

    @BindView(R.id.word_tv)
    TextView wordTv;
    @BindView(R.id.selection_1)
    TextView selection1;
    @BindView(R.id.selection_2)
    TextView selection2;
    @BindView(R.id.selection_3)
    TextView selection3;
    @BindView(R.id.selection_4)
    TextView selection4;
    @BindView(R.id.selection_1_layout)
    FrameLayout selection1Layout;
    @BindView(R.id.selection_2_layout)
    FrameLayout selection2Layout;
    @BindView(R.id.selection_3_layout)
    FrameLayout selection3Layout;
    @BindView(R.id.selection_4_layout)
    FrameLayout selection4Layout;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.result_layout)
    LinearLayout resultLayout;
    @BindView(R.id.try_again_layout)
    FrameLayout tryAgainLayout;
    @BindView(R.id.finish_test_layout)
    FrameLayout finishTestLayout;
    @BindView(R.id.volume_img)
    ImageView volumeImg;
    @BindView(R.id.volume_layout)
    FrameLayout volumeLayout;
    @BindView(R.id.word_layout)
    RelativeLayout wordLayout;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private RcWordStudyCiYiXuanCiAdapter adapter;
    private List<WordDetailListItem> resultList;

    private List<Integer> randomPlayIndex;
    private int index;
    private int position;
    private SoundPool ourSounds;
    private int answer_right;
    private int answer_wrong;

    private MediaPlayer mPlayer;
    private String fullName;
    private int playTimes;
    private int totalSum;
    private SharedPreferences sharedPreferences;

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
        setContentView(R.layout.word_study_dancixuanyi_activity);
        ButterKnife.bind(this);
        getTestOrder();
        initViews();
        initializeSoundPool();
        setData();
    }

    public void getTestOrder() {
        totalSum = WordStudyFragment.itemList.size();
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0);
        index = 0;
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.danciceshi));
        sharedPreferences = Setings.getSharedPreferences(this);
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
        setVolumeImg();
    }

    private void setVolumeImg(){
        if(sharedPreferences.getBoolean(KeyUtil.IsWordStudyPlaySound,true)){
            volumeImg.setImageResource(R.drawable.ic_volume_on);
        }else {
            volumeImg.setImageResource(R.drawable.ic_volume_off);
        }
    }

    private void setData() {
        resultLayout.setVisibility(View.GONE);
        setActionBarTitle(this.getResources().getString(R.string.danciceshi) + "(" + (index + 1) + "/" + totalSum + ")");
        if (index < randomPlayIndex.size()) {
            position = randomPlayIndex.get(index);
            wordTv.setText(WordStudyFragment.itemList.get(position).getName());
            List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
                    totalSum < 4 ? 10 : totalSum,
                    0, 3, position);
            if (tv_list.size() == 4) {
                if(totalSum > tv_list.get(0)){
                    selection1.setText(WordStudyFragment.itemList.get(tv_list.get(0)).getDesc());
                }else {
                    selection1.setText(BoxHelper.getBench().getDesc());
                }
                if(totalSum > tv_list.get(1)){
                    selection2.setText(WordStudyFragment.itemList.get(tv_list.get(1)).getDesc());
                }else {
                    selection2.setText(BoxHelper.getBench().getDesc());
                }
                if(totalSum > tv_list.get(2)){
                    selection3.setText(WordStudyFragment.itemList.get(tv_list.get(2)).getDesc());
                }else {
                    selection3.setText(BoxHelper.getBench().getDesc());
                }
                if(totalSum > tv_list.get(3)){
                    selection4.setText(WordStudyFragment.itemList.get(tv_list.get(3)).getDesc());
                }else {
                    selection4.setText(BoxHelper.getBench().getDesc());
                }
                selection1.setTextColor(getResources().getColor(R.color.text_black1));
                selection2.setTextColor(getResources().getColor(R.color.text_black1));
                selection3.setTextColor(getResources().getColor(R.color.text_black1));
                selection4.setTextColor(getResources().getColor(R.color.text_black1));
            }
        }
        playDelay();
    }

    private void playDelay() {
        if(mPlayer != null){
            new Handler().postDelayed(() -> playSound(),500);
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
        }
    }

    private void playSound() {
        if(sharedPreferences.getBoolean(KeyUtil.IsWordStudyPlaySound,true)){
            startToPlay();
        }
    }

    private void startToPlay(){
        if (isPlaying()) {
            stopPlay();
        } else {
            playItem(WordStudyFragment.itemList.get(position));
        }
    }

    private void playItem(WordDetailListItem mAVObject) {
        if (TextUtils.isEmpty(mAVObject.getSound())) {
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
        String filepath = SDCardUtil.getDownloadPath(getAudioPath(mAVObject)) + MD5.encode(mAVObject.getName()) + ".pcm";
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
                            ToastUtil.diaplayMesShort(WordStudyDanCiXuanYiActivity.this,
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
    }

    @OnClick({R.id.selection_1_layout, R.id.selection_2_layout, R.id.selection_3_layout,
            R.id.selection_4_layout, R.id.try_again_layout, R.id.finish_test_layout,
            R.id.volume_layout,R.id.word_layout})
    public void onClick(View view) {
        switch (view.getId()) {
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
                WordStudyDanCiXuanYiActivity.this.finish();
                break;
            case R.id.volume_layout:
                changeVolumeImg();
                break;
            case R.id.word_layout:
                startToPlay();
                break;
        }
    }

    private void changeVolumeImg(){
        Setings.saveSharedPreferences(sharedPreferences,KeyUtil.IsWordStudyPlaySound,
                !sharedPreferences.getBoolean(KeyUtil.IsWordStudyPlaySound,true));
        setVolumeImg();
    }

    private void tryAgain() {
        getTestOrder();
        WordStudyFragment.clearSign();
        setData();
    }

    private void checkResultThenGoNext(TextView tv) {
        if (Setings.isFastClick(this)) {
            return;
        }
        String text = tv.getText().toString();
        if (index < WordStudyFragment.itemList.size()) {
            if (!WordStudyFragment.itemList.get(position).getDesc().equals(text)) {
                playSoundPool(false);
                WordStudyFragment.itemList.get(position).setSelect_Time();
                tv.setTextColor(getResources().getColor(R.color.material_color_red));
                tv.setText(getCorretContent(text));
            } else {
                playSoundPool(true);
                tv.setTextColor(getResources().getColor(R.color.material_color_green));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (index == totalSum - 1) {
                            resultLayout.setVisibility(View.VISIBLE);
                            countScoreAndShowResult();
                        } else {
                            index++;
                            setData();
                        }
                    }
                }, 800);
            }
        }
    }

    private String getCorretContent(String text){
        for(WordDetailListItem item : WordStudyFragment.itemList){
            if(item.getDesc().equals(text)){
                return text + "\n" + item.getName();
            }
        }
        return text;
    }

    private void playSoundPool(boolean isRight) {
        if (isRight) {
            ourSounds.play(answer_right, 1, 1, 1, 0, 1);
        } else {
            ourSounds.play(answer_wrong, 1, 1, 1, 0, 1);
        }
    }

    private void countScoreAndShowResult() {
        setActionBarTitle(this.getResources().getString(R.string.word_test_result));
        double wrongCount = 0;
        resultList.clear();
        for (WordDetailListItem item : WordStudyFragment.itemList) {
            if (item.getSelect_time() > 0) {
                wrongCount++;
                resultList.add(item);
            }
        }
        BoxHelper.updateList(resultList,true);
        for (WordDetailListItem item : WordStudyFragment.itemList) {
            if (item.getSelect_time() == 0) {
                resultList.add(item);
            }
        }
        int scoreInt = (int) ((totalSum - wrongCount) / totalSum * 100);
        score.setText(String.valueOf(scoreInt) + "分");
        if (scoreInt > 79) {
            score.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            score.setTextColor(this.getResources().getColor(R.color.red));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordStudyFragment.clearSign();
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
