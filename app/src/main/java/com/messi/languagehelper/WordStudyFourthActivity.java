package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messi.languagehelper.adapter.WordStudyUnitListAdapter;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.impl.AdapterListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.transitionseverywhere.TransitionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStudyFourthActivity extends BaseActivity implements AdapterListener {

    @BindView(R.id.renzhi_layout)
    FrameLayout renzhiLayout;
    @BindView(R.id.duyinxuanci_layout)
    FrameLayout duyinxuanciLayout;
    @BindView(R.id.previous_unit_layout)
    FrameLayout previousUnitLayout;
    @BindView(R.id.next_unit_layout)
    FrameLayout nextUnitLayout;
    @BindView(R.id.show_all_unit)
    TextView showAllUnit;
    @BindView(R.id.unit_list)
    GridView unitList;
    @BindView(R.id.transitions_container)
    RelativeLayout transitionsContainer;
    @BindView(R.id.danciceshi_layout)
    CardView danciceshiLayout;
    @BindView(R.id.ciyixuanci_layout)
    CardView ciyixuanciLayout;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private boolean isShowAllUnit;
    private WordStudyUnitListAdapter mAdapter;
    private WordListItem avObjects;
    public static List<WordDetailListItem> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_fifth_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        avObjects = (WordListItem) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        WXEntryActivity.dataMap.clear();
        class_name = avObjects.getTitle();
        class_id = avObjects.getClass_id();
        course_id = avObjects.getCourse_id();
        course_num = avObjects.getCourse_num();
        setUint();
        showAllUnit.setText("共" + course_num + "单元");
        mAdapter = new WordStudyUnitListAdapter(this, avObjects, this);
        unitList.setAdapter(mAdapter);
    }

    private void setUint() {
        setActionBarTitle(class_name + "第" + course_id + "单元");
    }

    @OnClick({R.id.renzhi_layout, R.id.duyinxuanci_layout, R.id.previous_unit_layout,
            R.id.next_unit_layout, R.id.danciceshi_layout, R.id.ciyixuanci_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.renzhi_layout:
                toDancirenzhi();
                break;
            case R.id.duyinxuanci_layout:
                toDuyinxuanciActivity();
                break;
            case R.id.danciceshi_layout:
                toDanciceshiActivity();
                break;
            case R.id.ciyixuanci_layout:
                toCiYiXuanCiActivity();
                break;
            case R.id.previous_unit_layout:
                isShowAllUnit();
                break;
            case R.id.next_unit_layout:
                if (course_id < course_num) {
                    course_id++;
                    setUint();
                    clearData();
                } else {
                    ToastUtil.diaplayMesShort(WordStudyFourthActivity.this, "已经是最后一单元了");
                }
                break;
        }
    }

    private void isShowAllUnit() {
        TransitionManager.beginDelayedTransition(transitionsContainer);
        isShowAllUnit = !isShowAllUnit;
        unitList.setVisibility(isShowAllUnit ? View.VISIBLE : View.GONE);
    }

    private void toDancirenzhi() {
        Intent intent = new Intent(this, WordStudyDanCiRenZhiActivity.class);
        intent.putExtra(KeyUtil.ClassName, class_name);
        intent.putExtra(KeyUtil.CourseId, course_id);
        intent.putExtra(KeyUtil.CourseNum, course_num);
        intent.putExtra(KeyUtil.ClassId, class_id);
        startActivity(intent);
    }

    private void toDuyinxuanciActivity() {
        Intent intent = new Intent(this, WordStudyDuYinXuanCiActivity.class);
        intent.putExtra(KeyUtil.ClassName, class_name);
        intent.putExtra(KeyUtil.ClassId, class_id);
        intent.putExtra(KeyUtil.CourseId, course_id);
        intent.putExtra(KeyUtil.CourseNum, course_num);
        startActivity(intent);
    }

    private void toDanciceshiActivity() {
        Intent intent = new Intent(this, WordStudyDanCiCeShiActivity.class);
        intent.putExtra(KeyUtil.ClassName, class_name);
        intent.putExtra(KeyUtil.ClassId, class_id);
        intent.putExtra(KeyUtil.CourseId, course_id);
        intent.putExtra(KeyUtil.CourseNum, course_num);
        startActivity(intent);
    }

    private void toCiYiXuanCiActivity() {
        Intent intent = new Intent(this, WordStudyCiYiXuanCiActivity.class);
        intent.putExtra(KeyUtil.ClassName, class_name);
        intent.putExtra(KeyUtil.ClassId, class_id);
        intent.putExtra(KeyUtil.CourseId, course_id);
        intent.putExtra(KeyUtil.CourseNum, course_num);
        startActivity(intent);
    }

    @Override
    public void OnItemClick(Object mObject, int index) {
        course_id = index + 1;
        setUint();
        isShowAllUnit();
        clearData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        avObjects.setCourse_id(course_id);
        SaveData.saveObject(this, KeyUtil.WordStudyUnit, avObjects);
        clearData();
    }

    private void clearData() {
        if (itemList != null) {
            itemList.clear();
            itemList = null;
        }
    }

    public static void clearSign() {
        if (WordStudyFourthActivity.itemList != null && WordStudyFourthActivity.itemList.size() > 0) {
            for (WordDetailListItem mAVObject : WordStudyFourthActivity.itemList) {
                mAVObject.setBackup1("");
            }
        }
    }

}
