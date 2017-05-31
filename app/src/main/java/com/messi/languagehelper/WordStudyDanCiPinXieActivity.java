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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.adapter.WordStudySpellAdapter;
import com.messi.languagehelper.bean.WordSpellCharacter;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.AdapterListener;
import com.messi.languagehelper.impl.OnFinishListener;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyDanCiPinXieActivity extends BaseActivity implements OnFinishListener,AdapterListener {


    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.retry_tv)
    TextView retry_tv;
    @BindView(R.id.prompt_tv)
    TextView prompt_tv;
    @BindView(R.id.word_des)
    TextView wordDes;
    @BindView(R.id.word_name)
    TextView wordName;
    @BindView(R.id.volume_img)
    ImageView volumeImg;
    @BindView(R.id.volume_layout)
    FrameLayout volumeLayout;
    @BindView(R.id.word_layout)
    RelativeLayout wordLayout;
    @BindView(R.id.word_spell_gv)
    GridView wordSpellGv;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.try_again_layout)
    FrameLayout tryAgainLayout;
    @BindView(R.id.finish_test_layout)
    FrameLayout finishTestLayout;
    @BindView(R.id.result_layout)
    LinearLayout resultLayout;
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
    private StringBuilder sb;
    private SharedPreferences sharedPreferences;
    private WordStudySpellAdapter mWordStudySpellAdapter;
    private List<WordSpellCharacter> mWordSpellCharacter;

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
        setContentView(R.layout.word_study_dancipinxie_activity);
        ButterKnife.bind(this);
        getTestOrder();
        initViews();
        initializeSoundPool();
        setData(true);
    }

    public void getTestOrder() {
        totalSum = WordStudyPlanDetailActivity.itemList.size();
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0);
        index = 0;
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.pinxie));
        sharedPreferences = Settings.getSharedPreferences(this);
        mPlayer = new MediaPlayer();
        sb = new StringBuilder();
        mWordSpellCharacter = new ArrayList<WordSpellCharacter>();
        class_name = getIntent().getStringExtra(KeyUtil.ClassName);
        class_id = getIntent().getStringExtra(KeyUtil.ClassId);
        course_id = getIntent().getIntExtra(KeyUtil.CourseId, 1);
        course_num = getIntent().getIntExtra(KeyUtil.CourseNum, 0);
        mWordStudySpellAdapter = new WordStudySpellAdapter(this,mWordSpellCharacter,this);
        wordSpellGv.setAdapter(mWordStudySpellAdapter);
        resultList = new ArrayList<WordDetailListItem>();
        adapter = new RcWordStudyCiYiXuanCiAdapter();
        adapter.setItems(resultList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(adapter);
        PlayUtil.setOnFinishListener(this);
        setVolumeImg();
    }

    private void setVolumeImg() {
        if (sharedPreferences.getBoolean(KeyUtil.IsWordStudySpellPlaySound, true)) {
            volumeImg.setImageResource(R.drawable.ic_volume_on);
        } else {
            volumeImg.setImageResource(R.drawable.ic_volume_off);
        }
    }

    private void setData(boolean isPlaySound) {
        wordName.setTextColor(getResources().getColor(R.color.white));
        resultLayout.setVisibility(View.GONE);
        setActionBarTitle(this.getResources().getString(R.string.pinxie) + "(" + (index + 1) + "/" + totalSum + ")");
        if (index < randomPlayIndex.size()) {
            position = randomPlayIndex.get(index);
            sb.setLength(0);
            wordDes.setText(WordStudyPlanDetailActivity.itemList.get(position).getDesc());
            setWordName();
            wordToCharacter();
        }
        if(isPlaySound){
            playDelay();
        }
    }

    private void setWordName(){
        int count = WordStudyPlanDetailActivity.itemList.get(position).getName().length() - sb.length();
        wordName.setText(sb.toString());
        for(int i = 0; i<count; i++){
            wordName.append(" __");
        }
    }

    private void wordToCharacter(){
        mWordSpellCharacter.clear();
        for(Character cha : WordStudyPlanDetailActivity.itemList.get(position).getName().trim().toCharArray()){
            mWordSpellCharacter.add(new WordSpellCharacter(cha));
        }
        Collections.shuffle(mWordSpellCharacter);
        mWordStudySpellAdapter.notifyDataSetChanged();
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
        if (playTimes < 2) {
            playSound();
        } else {
            playTimes = 0;
        }
    }

    private void playSound() {
        if (sharedPreferences.getBoolean(KeyUtil.IsWordStudySpellPlaySound, true)) {
            startToPlay();
        }
    }

    private void startToPlay() {
        if (isPlaying()) {
            stopPlay();
        } else {
            playItem(WordStudyPlanDetailActivity.itemList.get(position));
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
                            ToastUtil.diaplayMesShort(WordStudyDanCiPinXieActivity.this,
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

    @OnClick({R.id.try_again_layout, R.id.finish_test_layout, R.id.volume_layout, R.id.word_layout,
            R.id.prompt_tv,R.id.retry_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again_layout:
                tryAgain();
                break;
            case R.id.finish_test_layout:
                WordStudyDanCiPinXieActivity.this.finish();
                break;
            case R.id.volume_layout:
                changeVolumeImg();
                break;
            case R.id.word_layout:
                startToPlay();
                break;
            case R.id.prompt_tv:
                prompt();
                break;
            case R.id.retry_tv:
                setData(false);
                break;
        }
    }

    private void prompt(){
        WordStudyPlanDetailActivity.itemList.get(position).setSelect_Time();
        wordName.setTextColor(getResources().getColor(R.color.white));
        wordName.setText(WordStudyPlanDetailActivity.itemList.get(position).getName());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData(false);
            }
        },1000*2);
    }

    private void changeVolumeImg() {
        Settings.saveSharedPreferences(sharedPreferences, KeyUtil.IsWordStudySpellPlaySound,
                !sharedPreferences.getBoolean(KeyUtil.IsWordStudySpellPlaySound, true));
        setVolumeImg();
    }

    private void tryAgain() {
        getTestOrder();
        WordStudyPlanDetailActivity.clearSign();
        setData(true);
    }

    private void playSoundPool(boolean isRight) {
        if(isRight){
            ourSounds.play(answer_right, 1, 1, 1, 0, 1);
        }else {
            ourSounds.play(answer_wrong, 1, 1, 1, 0, 1);
        }
    }

    private void checkResultThenGoNext() {
        String text = wordName.getText().toString().trim();
        if (index < WordStudyPlanDetailActivity.itemList.size()) {
            if (!WordStudyPlanDetailActivity.itemList.get(position).getName().equals(text)) {
                playSoundPool(false);
                wordName.setTextColor(getResources().getColor(R.color.red));
                WordStudyPlanDetailActivity.itemList.get(position).setSelect_Time();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setData(false);
                    }
                }, 800);
            } else {
                playSoundPool(true);
                wordName.setTextColor(getResources().getColor(R.color.green));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (index == totalSum - 1) {
                            resultLayout.setVisibility(View.VISIBLE);
                            countScoreAndShowResult();
                        } else {
                            index++;
                            setData(true);
                        }
                    }
                }, 800);
            }
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

    @Override
    public void OnItemClick(Object mObject, int index) {
        ((WordSpellCharacter)mObject).setSelected(true);
        sb.append(((WordSpellCharacter)mObject).getCharacter());
        mWordStudySpellAdapter.notifyDataSetChanged();
        setWordName();
        if(WordStudyPlanDetailActivity.itemList.get(position).getName().length() == sb.length()){
            checkResultThenGoNext();
        }
    }
}
