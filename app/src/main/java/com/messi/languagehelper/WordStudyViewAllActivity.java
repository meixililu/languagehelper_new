package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.WordStudyUnitListAdapter;
import com.messi.languagehelper.adapter.WordStudyViewAllAdapter;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.impl.AdapterListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ChangeDataTypeUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.transitionseverywhere.TransitionManager;

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

public class WordStudyViewAllActivity extends BaseActivity implements AdapterListener {

    @BindView(R.id.show_all_unit_layout)
    FrameLayout previousUnitLayout;
    @BindView(R.id.next_unit_layout)
    FrameLayout nextUnitLayout;
    @BindView(R.id.unit_list)
    GridView unitList;
    @BindView(R.id.transitions_container)
    RelativeLayout transitionsContainer;
    @BindView(R.id.studycategory_lv)
    ListView studycategoryLv;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private boolean isShowAllUnit;
    private WordStudyUnitListAdapter mUnitAdapter;
    private WordListItem avObjects;

    public List<WordDetailListItem> resultList;
    private WordStudyViewAllAdapter mWordListAdapter;
    private SharedPreferences mSharedPreferences;
    private SpeechSynthesizer mSpeechSynthesizer;
    private MediaPlayer mPlayer;
    private String audioPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_view_all_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        init();
        getDataTask();
    }

    private void init() {
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mPlayer = new MediaPlayer();
        resultList = new ArrayList<WordDetailListItem>();
        avObjects = (WordListItem) Settings.dataMap.get(KeyUtil.DataMapKey);
        Settings.dataMap.clear();
        class_name = avObjects.getTitle();
        class_id = avObjects.getClass_id();
        course_id = avObjects.getCourse_id();
        course_num = avObjects.getCourse_num();
        setUint();
        mUnitAdapter = new WordStudyUnitListAdapter(this, avObjects, this);
        unitList.setAdapter(mUnitAdapter);
        mWordListAdapter = new WordStudyViewAllAdapter(this, mSharedPreferences, mSpeechSynthesizer, studycategoryLv,
                resultList, mPlayer);
        mWordListAdapter.setAudioPath(audioPath);
        initLocalPath();
        studycategoryLv.setAdapter(mWordListAdapter);
    }

    private void initLocalPath(){
        if (!TextUtils.isEmpty(class_id)) {
            audioPath = SDCardUtil.WordStudyPath + class_id + SDCardUtil.Delimiter + String.valueOf(course_id) + SDCardUtil.Delimiter;
            if(mWordListAdapter != null){
                mWordListAdapter.setAudioPath(audioPath);
            }
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        getDataTask();
    }

    private void setUint() {
        setActionBarTitle(class_name + "第" + course_id + "单元");
        initLocalPath();
    }

    @OnClick({R.id.show_all_unit_layout, R.id.next_unit_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_all_unit_layout:
                isShowAllUnit();
                break;
            case R.id.next_unit_layout:
                if (course_id < course_num) {
                    course_id++;
                    setUint();
                    getDataTask();
                } else {
                    ToastUtil.diaplayMesShort(WordStudyViewAllActivity.this, "已经是最后一单元了");
                }
                break;
        }
    }

    @Override
    public void OnItemClick(Object mObject, int index) {
        course_id = index + 1;
        setUint();
        isShowAllUnit();
        getDataTask();
    }

    private void isShowAllUnit() {
        TransitionManager.beginDelayedTransition(transitionsContainer);
        isShowAllUnit = !isShowAllUnit;
        unitList.setVisibility(isShowAllUnit ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(unitList.isShown()){
            unitList.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
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
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordStudyDetail.WordStudyDetail);
            query.whereEqualTo(AVOUtil.WordStudyDetail.class_id, class_id);
            query.whereEqualTo(AVOUtil.WordStudyDetail.course, course_id);
            query.orderByAscending(AVOUtil.WordStudyDetail.item_id);
            List<AVObject> avObjects = query.find();
            if (avObjects != null) {
                resultList.clear();
                for (AVObject mAVObject : avObjects) {
                    resultList.add(ChangeDataTypeUtil.changeData(mAVObject));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFinishLoadData() {
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
        mWordListAdapter.notifyDataSetChanged();
        studycategoryLv.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
        }
    }

}
