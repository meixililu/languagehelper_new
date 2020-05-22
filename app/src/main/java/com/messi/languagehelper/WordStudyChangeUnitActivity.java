package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.messi.languagehelper.adapter.WordStudyUnitListAdapter;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.databinding.WordStudyChangeUnitActivityBinding;
import com.messi.languagehelper.event.UpdateWordStudyPlan;
import com.messi.languagehelper.impl.AdapterListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SaveData;

import org.greenrobot.eventbus.EventBus;

public class WordStudyChangeUnitActivity extends BaseActivity implements AdapterListener {

    private String class_name;
    private String class_id;
    private int course_id;
    private WordListItem avObjects;
    private WordStudyUnitListAdapter mUnitAdapter;
    private WordStudyChangeUnitActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WordStudyChangeUnitActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setActionBarTitle(getString(R.string.title_all_unit));
        avObjects = SaveData.getDataFonJson(this, KeyUtil.WordStudyUnit, WordListItem.class);
        class_name = avObjects.getTitle();
        class_id = avObjects.getClass_id();
        course_id = avObjects.getCourse_id();
        mUnitAdapter = new WordStudyUnitListAdapter(this, avObjects, this);
        binding.unitList.setAdapter(mUnitAdapter);
    }

    @Override
    public void OnItemClick(Object mObject, int index) {
        course_id = index + 1;
        setActionBarTitle(class_name + "第" + course_id + "单元");
        saveCourseId();
        finish();
    }

    private void saveCourseId() {
        avObjects.setCourse_id(course_id);
        SaveData.saveDataAsJson(this, KeyUtil.WordStudyUnit, new Gson().toJson(avObjects));
        EventBus.getDefault().post(new UpdateWordStudyPlan());
    }

}
