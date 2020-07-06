package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.adapter.WordStudySpellAdapter;
import com.messi.languagehelper.bean.WordSpellCharacter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.impl.AdapterListener;
import com.messi.languagehelper.impl.MyPlayerListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyDanCiPinXieActivity extends BaseActivity implements AdapterListener {


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

    private int playTimes;
    private int totalSum;
    private StringBuilder sb;
    private SharedPreferences sharedPreferences;
    private WordStudySpellAdapter mWordStudySpellAdapter;
    private List<WordSpellCharacter> mWordSpellCharacter;

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
        totalSum = WordStudyFragment.itemList.size();
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0);
        index = 0;
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.pinxie));
        sharedPreferences = Setings.getSharedPreferences(this);
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
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        listview.setAdapter(adapter);
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
            wordDes.setText(WordStudyFragment.itemList.get(position).getDesc());
            setWordName();
            wordToCharacter();
        }
        if(isPlaySound){
            playDelay();
        }
    }

    private void setWordName(){
        int count = WordStudyFragment.itemList.get(position).getName().length() - sb.length();
        wordName.setText(sb.toString());
        for(int i = 0; i<count; i++){
            wordName.append(" __");
        }
    }

    private void wordToCharacter(){
        mWordSpellCharacter.clear();
        for(Character cha : WordStudyFragment.itemList.get(position).getName().trim().toCharArray()){
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
        new Handler().postDelayed(() -> playSound(), 500);
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
            MyPlayer.getInstance(this).stop();
        } else {
            playItem(WordStudyFragment.itemList.get(position));
        }
    }

    private void playItem(WordDetailListItem mAVObject) {
        MyPlayer.getInstance(this).setListener(new MyPlayerListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                replay();
            }
        });
        MyPlayer.getInstance(this).start(mAVObject.getName(),mAVObject.getSound());
    }


    private boolean isPlaying() {
        return MyPlayer.getInstance(this).isPlaying();
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
        WordStudyFragment.itemList.get(position).setSelect_Time();
        wordName.setTextColor(getResources().getColor(R.color.white));
        wordName.setText(WordStudyFragment.itemList.get(position).getName());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData(false);
            }
        },1000*2);
    }

    private void changeVolumeImg() {
        Setings.saveSharedPreferences(sharedPreferences, KeyUtil.IsWordStudySpellPlaySound,
                !sharedPreferences.getBoolean(KeyUtil.IsWordStudySpellPlaySound, true));
        setVolumeImg();
    }

    private void tryAgain() {
        getTestOrder();
        WordStudyFragment.clearSign();
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
        if (index < WordStudyFragment.itemList.size()) {
            if (!WordStudyFragment.itemList.get(position).getName().equals(text)) {
                playSoundPool(false);
                wordName.setTextColor(getResources().getColor(R.color.red));
                WordStudyFragment.itemList.get(position).setSelect_Time();
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
        MyPlayer.getInstance(this).onDestroy();
    }

    @Override
    public void OnItemClick(Object mObject, int index) {
        ((WordSpellCharacter)mObject).setSelected(true);
        sb.append(((WordSpellCharacter)mObject).getCharacter());
        mWordStudySpellAdapter.notifyDataSetChanged();
        setWordName();
        if(WordStudyFragment.itemList.get(position).getName().length() == sb.length()){
            checkResultThenGoNext();
        }
    }
}
