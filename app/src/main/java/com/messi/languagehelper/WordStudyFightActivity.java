package com.messi.languagehelper;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.databinding.WordStudyFightActivityBinding;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class WordStudyFightActivity extends BaseActivity {

    private RcWordStudyCiYiXuanCiAdapter adapter;
    private List<WordDetailListItem> resultList;
    private List<Integer> randomPlayIndex;
    private int index;
    private int position;
    private SoundPool ourSounds;
    private int answer_right;
    private int answer_wrong;

    private String wordTestType;
    private int totalSum;
    private ArrayList<WordDetailListItem> itemList;
    private WordStudyFightActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarTextColor(true);
        setStatusbarColor(R.color.white);
        binding = WordStudyFightActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initClickEvent();
        showView(2);
        initViews();
        getTestOrder();
        initializeSoundPool();
        setData();
    }

    public void getTestOrder() {
        totalSum = itemList.size();
        randomPlayIndex = new ArrayList<>();
        randomPlayIndex.addAll(NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0));
        randomPlayIndex.addAll(NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0));
        index = 0;
        binding.classProgress.setMax(totalSum * 2);
        initProgress();
    }

    private void initProgress(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.classProgress.setProgress(index, true);
        }else{
            binding.classProgress.setProgress(index);
        }
    }

    private void initViews() {
        wordTestType = getIntent().getStringExtra(KeyUtil.WordTestType);
        itemList = getIntent().getParcelableArrayListExtra(KeyUtil.List);
        if (itemList == null) {
            ToastUtil.diaplayMesShort(this,"没有单词，请退出重试！");
            finish();
        }
        resultList = new ArrayList<>();
        adapter = new RcWordStudyCiYiXuanCiAdapter();
        adapter.setItems(resultList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        binding.listview.setLayoutManager(mLinearLayoutManager);
        binding.listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
        binding.listview.setAdapter(adapter);
    }

    private void showView(int i) {
        binding.contentLayout.setVisibility(View.GONE);
        binding.resultLayout.setVisibility(View.GONE);
        if (i == 2) {
            binding.contentLayout.setVisibility(View.VISIBLE);
        } else {
            binding.resultLayout.setVisibility(View.VISIBLE);
        }
    }

    public void initClickEvent() {
        binding.closeBtn.setOnClickListener(view -> onBackPressed());
        binding.selection1.setOnClickListener(view -> checkResultThenGoNext(binding.selection1));
        binding.selection2.setOnClickListener(view -> checkResultThenGoNext(binding.selection2));
        binding.selection3.setOnClickListener(view -> checkResultThenGoNext(binding.selection3));
        binding.selection4.setOnClickListener(view -> checkResultThenGoNext(binding.selection4));
        binding.finishTestLayout.setOnClickListener(view -> quick());
        binding.wordTv.setOnClickListener(view -> {
            playSound();
        });
    }

    private void setData() {
        initProgress();
        binding.resultLayout.setVisibility(View.GONE);
        if (index < randomPlayIndex.size()) {
            position = randomPlayIndex.get(index);
            List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
                    totalSum < 4 ? 10 : totalSum,
                    0, 3, position);
            if (tv_list.size() == 4) {
                if (index < itemList.size()) {
                    binding.wordTv.setText(itemList.get(position).getName());
                    if(totalSum > tv_list.get(0)){
                        binding.selection1.setText(itemList.get(tv_list.get(0)).getDesc());
                    }else {
                        binding.selection1.setText(BoxHelper.getBench().getDesc());
                    }
                    if(totalSum > tv_list.get(1)){
                        binding.selection2.setText(itemList.get(tv_list.get(1)).getDesc());
                    }else {
                        binding.selection2.setText(BoxHelper.getBench().getDesc());
                    }
                    if(totalSum > tv_list.get(2)){
                        binding.selection3.setText(itemList.get(tv_list.get(2)).getDesc());
                    }else {
                        binding.selection3.setText(BoxHelper.getBench().getDesc());
                    }
                    if(totalSum > tv_list.get(3)){
                        binding.selection4.setText(itemList.get(tv_list.get(3)).getDesc());
                    }else {
                        binding.selection4.setText(BoxHelper.getBench().getDesc());
                    }
                    playSound();
                } else {
                    binding.wordTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    binding.selection1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    binding.selection2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    binding.selection3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    binding.selection4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    binding. wordTv.setText(itemList.get(position).getDesc());
                    if(totalSum > tv_list.get(0)){
                        binding.selection1.setText(itemList.get(tv_list.get(0)).getName());
                    }else {
                        binding.selection1.setText(BoxHelper.getBench().getName());
                    }
                    if(totalSum > tv_list.get(1)){
                        binding.selection2.setText(itemList.get(tv_list.get(1)).getName());
                    }else {
                        binding.selection2.setText(BoxHelper.getBench().getName());
                    }
                    if(totalSum > tv_list.get(2)){
                        binding.selection3.setText(itemList.get(tv_list.get(2)).getName());
                    }else {
                        binding.selection3.setText(BoxHelper.getBench().getName());
                    }
                    if(totalSum > tv_list.get(3)){
                        binding.selection4.setText(itemList.get(tv_list.get(3)).getName());
                    }else {
                        binding.selection4.setText(BoxHelper.getBench().getName());
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

    private void playSound() {
        new Handler().postDelayed(() -> {
            if (index < itemList.size()) {
                if (position < itemList.size()) {
                    playItem(itemList.get(position));
                }
            }
        },350);
    }

    private void playItem(WordDetailListItem mAVObject) {
        MyPlayer.getInstance(this).start(mAVObject.getName(),mAVObject.getSound());
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
                    tv.setBackgroundResource(R.drawable.border_shadow_wrong_oval);
                    itemList.get(position).setSelect_Time();
                } else {
                    playSoundPool(true);
                    tv.setBackgroundResource(R.drawable.border_shadow_right_oval);
                }
            } else {
                if (!itemList.get(position).getName().equals(text)) {
                    playSoundPool(false);
                    tv.setBackgroundResource(R.drawable.border_shadow_wrong_oval);
                    itemList.get(position).setSelect_Time();
                } else {
                    playSoundPool(true);
                    tv.setBackgroundResource(R.drawable.border_shadow_right_oval);
                }
            }
            new Handler().postDelayed(() -> {
                if (index == randomPlayIndex.size() - 1) {
                    countScoreAndShowResult();
                } else {
                    index++;
                    tv.setBackgroundResource(R.drawable.border_shadow_gray_oval_selecter);
                    setData();
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
        binding.classProgress.setProgress(binding.classProgress.getMax());
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
        binding.score.setText(String.valueOf(scoreInt) + "分");
        if (scoreInt > 79) {
            binding.score.setTextColor(this.getResources().getColor(R.color.green));
            binding.fightResutlTv.setTextColor(this.getResources().getColor(R.color.green));
            binding.fightResutlTv.setText(this.getResources().getString(R.string.fight_success));
            saveCourseId();
        } else {
            binding.score.setTextColor(this.getResources().getColor(R.color.red));
            binding.fightResutlTv.setTextColor(this.getResources().getColor(R.color.red));
            binding.fightResutlTv.setText(this.getResources().getString(R.string.fight_fail));
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
            LiveEventBus.get(KeyUtil.UpdateWordStudyPlan).post("update");
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.contentLayout.isShown()) {
            showQuickDialog();
        }else if(binding.resultLayout.isShown()){
            super.onBackPressed();
        }
    }

    private void showQuickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage("正在考试，确定要退出？");
        builder.setPositiveButton("确认", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            quick();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void quick(){
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
