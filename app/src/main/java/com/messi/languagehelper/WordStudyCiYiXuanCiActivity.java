package com.messi.languagehelper;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messi.languagehelper.adapter.RcWordStudyCiYiXuanCiAdapter;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.ChangeDataTypeUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyCiYiXuanCiActivity extends BaseActivity {

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
    private int totalSum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_ciyixuanci_activity);
        ButterKnife.bind(this);
        getTestOrder();
        initViews();
        initializeSoundPool();
        setData();
    }

    public void getTestOrder() {
        totalSum = WordStudyPlanDetailActivity.itemList.size();
        randomPlayIndex = NumberUtil.getNumberOrderNotRepeat(totalSum - 1, 0);
        index = 0;
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.ciyixuanci));
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
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(adapter);
    }

    private void setData() {
        resultLayout.setVisibility(View.GONE);
        setActionBarTitle(this.getResources().getString(R.string.ciyixuanci) + "(" + (index + 1) + "/" + totalSum + ")");
        if (index < randomPlayIndex.size()) {
            position = randomPlayIndex.get(index);
            wordTv.setText(WordStudyPlanDetailActivity.itemList.get(position).getDesc());
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

    @OnClick({R.id.selection_1_layout, R.id.selection_2_layout, R.id.selection_3_layout,
            R.id.selection_4_layout,R.id.try_again_layout, R.id.finish_test_layout})
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
                WordStudyCiYiXuanCiActivity.this.finish();
                break;
        }
    }

    private void tryAgain(){
        getTestOrder();
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
                tv.setText(getCorretContent(text));
            }else {
                playSoundPool(true);
                tv.setTextColor(getResources().getColor(R.color.material_color_green));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(index == totalSum - 1){
                            resultLayout.setVisibility(View.VISIBLE);
                            countScoreAndShowResult();
                        }else {
                            index++;
                            setData();
                        }
                    }
                }, 500);
            }
        }
    }

    private String getCorretContent(String text){
        for(WordDetailListItem item : WordStudyPlanDetailActivity.itemList){
            if(item.getName().equals(text)){
                return text + "\n" + item.getDesc();
            }
        }
        return text;
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
    }

}
