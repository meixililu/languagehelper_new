package com.messi.languagehelper;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.event.UpdateWordStudyPlan;
import com.messi.languagehelper.impl.OnFinishListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyFightActivity extends BaseActivity implements OnFinishListener {



    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.finish_test_layout)
    FrameLayout finishTestLayout;
    @BindView(R.id.result_layout)
    LinearLayout resultLayout;
    @BindView(R.id.word_tv)
    TextView wordTv;
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
    @BindView(R.id.fight_resutl_tv)
    TextView fightResutlTv;

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
    private SharedPreferences sharedPreferences;
    private String wordTestType;
    private int totalSum;
    private ArrayList<WordDetailListItem> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_fight_activity);
        ButterKnife.bind(this);
        showView(2);
        initViews();
        getTestOrder();
        initializeSoundPool();
        setData();
    }

    public void getTestOrder() {
        totalSum = itemList.size();
        randomPlayIndex = new ArrayList<Integer>();
        randomPlayIndex.addAll(NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0));
        randomPlayIndex.addAll(NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0));
        index = 0;
    }

    private void initViews() {
        sharedPreferences = Setings.getSharedPreferences(this);
        mPlayer = new MediaPlayer();
        wordTestType = getIntent().getStringExtra(KeyUtil.WordTestType);
        itemList = getIntent().getParcelableArrayListExtra(KeyUtil.List);
        if (itemList == null) {
            ToastUtil.diaplayMesShort(this,"没有单词，请退出重试！");
            finish();
        }
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

    private void showView(int i) {
        contentLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.GONE);
        if (i == 2) {
            contentLayout.setVisibility(View.VISIBLE);
        } else {
            resultLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setData() {
        resultLayout.setVisibility(View.GONE);
        if (index < randomPlayIndex.size()) {
            position = randomPlayIndex.get(index);
            List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
                    totalSum < 4 ? 10 : totalSum,
                    0, 3, position);
            if (tv_list.size() == 4) {
                if (index < itemList.size()) {
                    wordTv.setText(itemList.get(position).getName());
                    if(totalSum > tv_list.get(0)){
                        selection1.setText(itemList.get(tv_list.get(0)).getDesc());
                    }else {
                        selection1.setText(BoxHelper.getBench().getDesc());
                    }
                    if(totalSum > tv_list.get(1)){
                        selection2.setText(itemList.get(tv_list.get(1)).getDesc());
                    }else {
                        selection2.setText(BoxHelper.getBench().getDesc());
                    }
                    if(totalSum > tv_list.get(2)){
                        selection3.setText(itemList.get(tv_list.get(2)).getDesc());
                    }else {
                        selection3.setText(BoxHelper.getBench().getDesc());
                    }
                    if(totalSum > tv_list.get(3)){
                        selection4.setText(itemList.get(tv_list.get(3)).getDesc());
                    }else {
                        selection4.setText(BoxHelper.getBench().getDesc());
                    }
                } else {
                    wordTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    selection1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    selection2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    selection3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    selection4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    wordTv.setText(itemList.get(position).getDesc());
                    if(totalSum > tv_list.get(0)){
                        selection1.setText(itemList.get(tv_list.get(0)).getName());
                    }else {
                        selection1.setText(BoxHelper.getBench().getName());
                    }
                    if(totalSum > tv_list.get(1)){
                        selection2.setText(itemList.get(tv_list.get(1)).getName());
                    }else {
                        selection2.setText(BoxHelper.getBench().getName());
                    }
                    if(totalSum > tv_list.get(2)){
                        selection3.setText(itemList.get(tv_list.get(2)).getName());
                    }else {
                        selection3.setText(BoxHelper.getBench().getName());
                    }
                    if(totalSum > tv_list.get(3)){
                        selection4.setText(itemList.get(tv_list.get(3)).getName());
                    }else {
                        selection4.setText(BoxHelper.getBench().getName());
                    }
                }
            }
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
        if (position < itemList.size()) {
            playItem(itemList.get(position));
        }
    }

    private void playItem(WordDetailListItem mAVObject) {
        if (TextUtils.isEmpty(mAVObject.getSound())) {
            playWithSpeechSynthesizer(mAVObject);
        } else {
            playMp3(mAVObject.getSound());
        }
    }

    public void playMp3(String url) {
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(mp -> {
                mPlayer.start();
            });
            mPlayer.setOnCompletionListener(mp -> {
                    replay();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAudioPath(WordDetailListItem mAVObject){
        return SDCardUtil.WordStudyPath + mAVObject.getClass_id() + SDCardUtil.Delimiter +
                String.valueOf(mAVObject.getCourse()) + SDCardUtil.Delimiter;
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
                        ToastUtil.diaplayMesShort(WordStudyFightActivity.this,
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
            R.id.selection_4_layout, R.id.finish_test_layout, R.id.word_tv})
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
            case R.id.finish_test_layout:
                quick();
                break;
            case R.id.word_tv:
                if (index < itemList.size()) {
                    playSound();
                }
                break;
        }
    }

    private void checkResultThenGoNext(TextView tv) {
        if (Setings.isFastClick(this)) {
            return;
        }
        String text = tv.getText().toString();
        if (index < randomPlayIndex.size()) {
            if (index < itemList.size()) {
                if (!itemList.get(position).getDesc().equals(text)) {
                    playSoundPool(false);
                    itemList.get(position).setSelect_Time();
                } else {
                    playSoundPool(true);
                }
            } else {
                if (!itemList.get(position).getName().equals(text)) {
                    playSoundPool(false);
                    itemList.get(position).setSelect_Time();
                } else {
                    playSoundPool(true);
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index == randomPlayIndex.size() - 1) {
                        countScoreAndShowResult();
                    } else {
                        index++;
                        setData();
                    }
                }
            }, 500);
        }
    }

    private void playSoundPool(boolean isRight) {
        if (isRight) {
            ourSounds.play(answer_right, 1, 1, 1, 0, 1);
        } else {
            ourSounds.play(answer_wrong, 1, 1, 1, 0, 1);
        }
    }

    private void countScoreAndShowResult() {
        showView(3);
        double wrongCount = 0;
        resultList.clear();
        for (WordDetailListItem item : itemList) {
            if (item.getSelect_time() > 0 ) {
                wrongCount++;
                resultList.add(item);
                item.setIs_know(false);
            }
        }
        BoxHelper.updateList(resultList,true);
        for (WordDetailListItem item : itemList) {
            if (item.getSelect_time() == 0) {
                resultList.add(item);
                BoxHelper.update(item,false);
            }
        }
        int scoreInt = (int) ((totalSum - wrongCount) / totalSum * 100);
        score.setText(String.valueOf(scoreInt) + "分");
        if (scoreInt > 79) {
            score.setTextColor(this.getResources().getColor(R.color.green));
            fightResutlTv.setTextColor(this.getResources().getColor(R.color.green));
            fightResutlTv.setText(this.getResources().getString(R.string.fight_success));
            saveCourseId();
        } else {
            score.setTextColor(this.getResources().getColor(R.color.red));
            fightResutlTv.setTextColor(this.getResources().getColor(R.color.red));
            fightResutlTv.setText(this.getResources().getString(R.string.fight_fail));
        }
        adapter.notifyDataSetChanged();
    }

    private void saveCourseId() {
        if ("learn".equals(wordTestType)) {
            WordListItem wordListItem =  SaveData.getDataFonJson(this, KeyUtil.WordStudyUnit, WordListItem.class);
            if(wordListItem != null){
                wordListItem.setCourse_id(wordListItem.getCourse_id()+1);
                SaveData.saveDataAsJson(this, KeyUtil.WordStudyUnit, new Gson().toJson(wordListItem));
            }
            EventBus.getDefault().post(new UpdateWordStudyPlan());
        }
    }

    @Override
    public void onBackPressed() {
        if (contentLayout.isShown()) {
            showQuickDialog();
        }else if(resultLayout.isShown()){
            super.onBackPressed();
        }
    }

    private void showQuickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage("正在考试，确定要退出？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                WordStudyFightActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void quick(){
        this.finish();
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
