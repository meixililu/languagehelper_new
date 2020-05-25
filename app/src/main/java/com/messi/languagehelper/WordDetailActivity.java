package com.messi.languagehelper;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.databinding.WordDetailActivityBinding;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class WordDetailActivity extends BaseActivity {

    private WordDetailActivityBinding binding;
    private int totalSum;
    private int position;
    private List<Integer> randomPlayIndex;
    private String wordTestType;
    private int index;
    private SoundPool ourSounds;
    private int answer_right;
    private int answer_wrong;
    private View recognizeView;
    private View checkIsKnowView;
    private ArrayList<WordDetailListItem> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WordDetailActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
        setData();
        initializeSoundPool();
    }

    private void init() {
        wordTestType = getIntent().getStringExtra(KeyUtil.WordTestType);
        itemList = getIntent().getParcelableArrayListExtra(KeyUtil.List);
        if (itemList == null) {
            ToastUtil.diaplayMesShort(this,"没有单词，请退出重试！");
            finish();
        }
        totalSum = itemList.size();
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0);
        index = 0;
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

    private void setData(){
        setActionBarTitle("已学 " + getHasLearnWordNum() + " / " + totalSum);
        if (index < randomPlayIndex.size()) {
            position = randomPlayIndex.get(index);
            WordDetailListItem item = itemList.get(position);
            index++;
            if (item != null && !item.isIs_know()) {
                playMp3(item.getName(),item.getSound());
                binding.contentLayout.removeAllViews();
                binding.contentLayout.addView(initRecognizeView(item),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }else {
                setData();
            }
        } else {
            if (getHasLearnWordNum() == totalSum) {
                binding.contentLayout.removeAllViews();
                binding.contentLayout.addView(toTest(),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }else {
                index = 0;
                setData();
            }
        }
    }

    public void playMp3(String content, String url) {
        MyPlayer.getInstance(this).start(content,url);
    }

    private View initRecognizeView(WordDetailListItem item){
        if (recognizeView == null) {
            recognizeView = LayoutInflater.from(this).inflate(R.layout.word_study_recognize_fragment, null, false);
        }
        TextView word_name = recognizeView.findViewById(R.id.word_name);
        TextView word_symbol = recognizeView.findViewById(R.id.word_symbol);
        TextView word_des = recognizeView.findViewById(R.id.word_des);
        ImageView voice_img = recognizeView.findViewById(R.id.voice_img);
        TextView recognize_know = recognizeView.findViewById(R.id.recognize_know);
        TextView recognize_unknow = recognizeView.findViewById(R.id.recognize_unknow);
        if (TextUtils.isEmpty(item.getSound())) {
            voice_img.setVisibility(View.GONE);
        } else {
            voice_img.setVisibility(View.VISIBLE);
        }
        word_name.setText(item.getName());
        word_des.setText("");
        word_symbol.setText(item.getSymbol());
        if (TextUtils.isEmpty(item.getSymbol())) {
            word_symbol.setVisibility(View.GONE);
        } else {
            word_symbol.setVisibility(View.VISIBLE);
        }
        recognize_know.setEnabled(true);
        recognize_unknow.setText(getString(R.string.recognize_unknow));
        recognize_know.setOnClickListener(v -> {
            binding.contentLayout.removeAllViews();
            binding.contentLayout.addView(initCheckIsKnowView(item),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        });
        recognize_unknow.setOnClickListener(v -> {
            String des = recognize_unknow.getText().toString();
            if(this.getResources().getString(R.string.practice_next).equals(des)){
                setData();
            }else {
                playMp3(item.getName(),item.getSound());
                word_des.setText(item.getDesc());
                recognize_know.setEnabled(false);
                recognize_unknow.setText(this.getResources().getString(R.string.practice_next));
            }
        });
        recognizeView.setOnClickListener(v -> {
            playMp3(item.getName(),item.getSound());
        });
        return recognizeView;
    }

    private View initCheckIsKnowView(WordDetailListItem item){
        if (checkIsKnowView == null) {
            checkIsKnowView = LayoutInflater.from(this).inflate(R.layout.word_study_dancixuanyi_activity, null, false);
        }
        View my_awesome_toolbar = checkIsKnowView.findViewById(R.id.my_awesome_toolbar);
        my_awesome_toolbar.setVisibility(View.GONE);
        RelativeLayout word_layout = checkIsKnowView.findViewById(R.id.word_layout);
        TextView word_tv = checkIsKnowView.findViewById(R.id.word_tv);
        FrameLayout selection_1_layout = checkIsKnowView.findViewById(R.id.selection_1_layout);
        FrameLayout selection_2_layout = checkIsKnowView.findViewById(R.id.selection_2_layout);
        FrameLayout selection_3_layout = checkIsKnowView.findViewById(R.id.selection_3_layout);
        FrameLayout selection_4_layout = checkIsKnowView.findViewById(R.id.selection_4_layout);
        TextView selection_1 = checkIsKnowView.findViewById(R.id.selection_1);
        TextView selection_2 = checkIsKnowView.findViewById(R.id.selection_2);
        TextView selection_3 = checkIsKnowView.findViewById(R.id.selection_3);
        TextView selection_4 = checkIsKnowView.findViewById(R.id.selection_4);
        word_tv.setText(item.getName());
        word_layout.setOnClickListener(v -> {
            playMp3(item.getName(),item.getSound());
        });
        List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
                totalSum < 4 ? 10 : totalSum,
                0, 3, position);
        if (tv_list.size() == 4) {
            if(totalSum > tv_list.get(0)){
                selection_1.setText(itemList.get(tv_list.get(0)).getDesc());
            }else {
                selection_1.setText(BoxHelper.getBench().getDesc());
            }
            if(totalSum > tv_list.get(1)){
                selection_2.setText(itemList.get(tv_list.get(1)).getDesc());
            }else {
                selection_2.setText(BoxHelper.getBench().getDesc());
            }
            if(totalSum > tv_list.get(2)){
                selection_3.setText(itemList.get(tv_list.get(2)).getDesc());
            }else {
                selection_3.setText(BoxHelper.getBench().getDesc());
            }
            if(totalSum > tv_list.get(3)){
                selection_4.setText(itemList.get(tv_list.get(3)).getDesc());
            }else {
                selection_4.setText(BoxHelper.getBench().getDesc());
            }
            selection_1.setTextColor(getResources().getColor(R.color.text_black1));
            selection_2.setTextColor(getResources().getColor(R.color.text_black1));
            selection_3.setTextColor(getResources().getColor(R.color.text_black1));
            selection_4.setTextColor(getResources().getColor(R.color.text_black1));
            selection_1_layout.setOnClickListener(v -> {checkResultThenGoNext(selection_1, item);});
            selection_2_layout.setOnClickListener(v -> {checkResultThenGoNext(selection_2, item);});
            selection_3_layout.setOnClickListener(v -> {checkResultThenGoNext(selection_3, item);});
            selection_4_layout.setOnClickListener(v -> {checkResultThenGoNext(selection_4, item);});
        }
        return checkIsKnowView;
    }

    private void checkResultThenGoNext(TextView tv,WordDetailListItem item) {
        if (Setings.isFastClick(this)) {
            return;
        }
        String text = tv.getText().toString();
        if (!item.getDesc().equals(text)) {
            playSoundPool(false);
            tv.setTextColor(getResources().getColor(R.color.material_color_red));
        } else {
            playSoundPool(true);
            tv.setTextColor(getResources().getColor(R.color.material_color_green));
            item.setIs_know(true);
        }
        new Handler().postDelayed(() -> {
            setData();
        }, 800);
        if (item.isIs_know()) {
            BoxHelper.insert(item);
        }
    }

    private View toTest(){
        View view = LayoutInflater.from(this).inflate(R.layout.word_study_to_test, null, false);
        TextView start_to_test = view.findViewById(R.id.start_to_test);
        start_to_test.setOnClickListener(v -> {
            ToAvtivity(WordStudyFightActivity.class);
            finish();
        });
        return view;
    }

    private void ToAvtivity(Class toClass) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(KeyUtil.WordTestType, wordTestType);
        intent.putParcelableArrayListExtra(KeyUtil.List, itemList);
        startActivity(intent);
    }

    private void playSoundPool(boolean isRight) {
        if (isRight) {
            ourSounds.play(answer_right, 1, 1, 1, 0, 1);
        } else {
            ourSounds.play(answer_wrong, 1, 1, 1, 0, 1);
        }
    }

    public int getHasLearnWordNum(){
        int count = 0;
        if (NullUtil.isNotEmpty(itemList)) {
            for(WordDetailListItem item : itemList){
                if (item.isIs_know()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null && binding.contentLayout != null) {
            binding.contentLayout.removeAllViews();
        }
        recognizeView = null;
        checkIsKnowView = null;
    }
}
