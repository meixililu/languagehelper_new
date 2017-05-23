package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.google.gson.Gson;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.bean.WordListItem;
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

public class WordStudyPlanDetailActivity extends BaseActivity {

    @BindView(R.id.renzhi_layout)
    FrameLayout renzhiLayout;
    @BindView(R.id.duyinxuanci_layout)
    FrameLayout duyinxuanciLayout;
    @BindView(R.id.danciceshi_layout)
    CardView danciceshiLayout;
    @BindView(R.id.ciyixuanci_layout)
    CardView ciyixuanciLayout;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private boolean isShowAllUnit;
    private WordListItem avObjects;
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
    }

    private void saveCourseId(){
        avObjects.setCourse_id(course_id);
        SaveData.saveDataAsJson(this, KeyUtil.WordStudyUnit, new Gson().toJson(avObjects));
    }

    @OnClick({R.id.renzhi_layout, R.id.duyinxuanci_layout,R.id.danciceshi_layout, R.id.ciyixuanci_layout})
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
        }
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
        Intent intent = new Intent(this, WordStudyDanCiXuanYiActivity.class);
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

    private void getDataTask() {
        if (itemList.size() == 0) {
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
        } else {
            clearSign();
        }
    }

    private void loadData() {
        try {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordStudyDetail.WordStudyDetail);
            query.whereEqualTo(AVOUtil.WordStudyDetail.class_id, class_id);
            query.whereEqualTo(AVOUtil.WordStudyDetail.course, course_id);
            query.orderByAscending(AVOUtil.WordStudyDetail.item_id);
            List<AVObject> avObjects = query.find();
            if (avObjects != null) {
                itemList.clear();
                for (AVObject mAVObject : avObjects) {
                    itemList.add(ChangeDataTypeUtil.changeData(mAVObject));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onFinishLoadData() {
        hideProgressbar();
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

}
