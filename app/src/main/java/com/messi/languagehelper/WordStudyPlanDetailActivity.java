package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.google.gson.Gson;
import com.messi.languagehelper.adapter.WordStudyUnitListAdapter;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.AdapterListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ChangeDataTypeUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SaveData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WordStudyPlanDetailActivity extends BaseActivity implements AdapterListener {

    @BindView(R.id.bottom)
    CardView bottom;
    @BindView(R.id.renzhi_layout)
    FrameLayout renzhiLayout;
    @BindView(R.id.duyinxuanci_layout)
    FrameLayout duyinxuanciLayout;
    @BindView(R.id.danciceshi_layout)
    CardView danciceshiLayout;
    @BindView(R.id.ciyixuanci_layout)
    CardView ciyixuanciLayout;
    @BindView(R.id.unit_list)
    GridView unitList;
    @BindView(R.id.dancisuji_layout)
    CardView dancisujiLayout;
    @BindView(R.id.pinxie_layout)
    CardView pinxieLayout;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private boolean isShowAllUnit;
    private WordListItem avObjects;
    private WordStudyUnitListAdapter mUnitAdapter;
    public static List<WordDetailListItem> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_plan_detail_activity);
        ButterKnife.bind(this);
        init();
        getDataTask();
    }

    private void init() {
        avObjects = SaveData.getDataFonJson(this, KeyUtil.WordStudyUnit, WordListItem.class);
        itemList = new ArrayList<WordDetailListItem>();
        class_name = avObjects.getTitle();
        class_id = avObjects.getClass_id();
        course_id = avObjects.getCourse_id();
        course_num = avObjects.getCourse_num();
        setActionBarTitle(class_name + "第" + course_id + "单元");
        mUnitAdapter = new WordStudyUnitListAdapter(this, avObjects, this);
        unitList.setAdapter(mUnitAdapter);
    }

    @OnClick({R.id.renzhi_layout, R.id.duyinxuanci_layout, R.id.danciceshi_layout, R.id.ciyixuanci_layout,
            R.id.dancisuji_layout, R.id.pinxie_layout, R.id.bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.renzhi_layout:
                ToAvtivity(WordStudyDanCiRenZhiActivity.class);
                break;
            case R.id.duyinxuanci_layout:
                ToAvtivity(WordStudyDuYinXuanCiActivity.class);
                break;
            case R.id.danciceshi_layout:
                ToAvtivity(WordStudyDanCiXuanYiActivity.class);
                break;
            case R.id.ciyixuanci_layout:
                ToAvtivity(WordStudyCiYiXuanCiActivity.class);
                break;
            case R.id.dancisuji_layout:
                ToAvtivity(WordStudyDuYinSuJiActivity.class);
                break;
            case R.id.pinxie_layout:
                ToAvtivity(WordStudyDanCiPinXieActivity.class);
                break;
            case R.id.bottom:
                ToAvtivity(WordStudyFightActivity.class);
                break;
        }
    }

    private void ToAvtivity(Class toClass) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(KeyUtil.ClassName, class_name);
        intent.putExtra(KeyUtil.CourseId, course_id);
        intent.putExtra(KeyUtil.CourseNum, course_num);
        intent.putExtra(KeyUtil.ClassId, class_id);
        startActivity(intent);
    }

    private void getDataTask() {
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                loadData();
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        onFinishLoadData();
                    }
                });

    }

    private void loadData() {
        try {
            itemList.clear();
            List<WordDetailListItem> items = DataBaseUtil.getInstance().getList(class_id,course_id);
            if(items.size() > 0){
                itemList.addAll(items);
            }else {
                AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordStudyDetail.WordStudyDetail);
                query.whereEqualTo(AVOUtil.WordStudyDetail.class_id, class_id);
                query.whereEqualTo(AVOUtil.WordStudyDetail.course, course_id);
                query.orderByAscending(AVOUtil.WordStudyDetail.item_id);
                List<AVObject> avObjects = query.find();
                if (avObjects != null) {
                    for (AVObject mAVObject : avObjects) {
                        itemList.add(ChangeDataTypeUtil.changeData(mAVObject));
                    }
                }
                DataBaseUtil.getInstance().saveList(itemList,false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onFinishLoadData() {
        hideProgressbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.word_study_plan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all:
                unitList.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (unitList.isShown()) {
            unitList.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearData();
    }

    private void clearData() {
        if (itemList != null) {
            itemList.clear();
            itemList = null;
        }
    }

    public static void clearSign() {
        if (itemList != null && itemList.size() > 0) {
            for (WordDetailListItem mAVObject : itemList) {
                mAVObject.setSelect_time(0);
            }
        }
    }

    @Override
    public void OnItemClick(Object mObject, int index) {
        unitList.setVisibility(View.GONE);
        course_id = index + 1;
        setActionBarTitle(class_name + "第" + course_id + "单元");
        saveCourseId();
        getDataTask();
    }

    private void saveCourseId() {
        avObjects.setCourse_id(course_id);
        SaveData.saveDataAsJson(this, KeyUtil.WordStudyUnit, new Gson().toJson(avObjects));
    }

}
