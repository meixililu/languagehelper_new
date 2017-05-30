package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.WordStudyDetailAdapter;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ChangeDataTypeUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SDCardUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WordStudyDanCiRenZhiActivity extends BaseActivity implements OnClickListener {

    private ListView category_lv;
    private WordStudyDetailAdapter mAdapter;
    private FloatingActionButton playbtn;
    private String class_name;
    private String class_id;
    private int course_id;
    private int course_num;
    private MediaPlayer mPlayer;
    private int index;

    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_detail_activity);
        initSwipeRefresh();
        initViews();
        getDataTask();
    }

    private void checkData() {
        if (WordStudyPlanDetailActivity.itemList == null) {
            WordStudyPlanDetailActivity.itemList = new ArrayList<WordDetailListItem>();
        }
    }

    private void initViews() {
        setActionBarTitle(this.getResources().getString(R.string.dancirenzhi));
        mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        mPlayer = new MediaPlayer();

        class_name = getIntent().getStringExtra(KeyUtil.ClassName);
        class_id = getIntent().getStringExtra(KeyUtil.ClassId);
        course_id = getIntent().getIntExtra(KeyUtil.CourseId, 1);
        course_num = getIntent().getIntExtra(KeyUtil.CourseNum, 0);
        playbtn = (FloatingActionButton) findViewById(R.id.playbtn);
        category_lv = (ListView) findViewById(R.id.studycategory_lv);
        checkData();
        mAdapter = new WordStudyDetailAdapter(this, mSharedPreferences, mSpeechSynthesizer, category_lv,
                WordStudyPlanDetailActivity.itemList, mPlayer);
        category_lv.setAdapter(mAdapter);

        playbtn.setOnClickListener(this);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        getDataTask();
    }

    private void getDataTask() {
        if (WordStudyPlanDetailActivity.itemList == null || WordStudyPlanDetailActivity.itemList.size() == 0) {
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
            clearPlaySign();
            onFinishLoadData();
        }
    }

    private void clearPlaySign() {
        for (WordDetailListItem mAVObject : WordStudyPlanDetailActivity.itemList) {
            mAVObject.setBackup1("");
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
                WordStudyPlanDetailActivity.itemList.clear();
                for (AVObject mAVObject : avObjects) {
                    WordStudyPlanDetailActivity.itemList.add(ChangeDataTypeUtil.changeData(mAVObject));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onFinishLoadData() {
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
        mAdapter.notifyDataSetChanged();
        category_lv.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playbtn:
                playSound();
                break;
        }
    }

    private void playSound() {
        if (mAdapter.isPlaying()) {
            playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_pause_white));
            mAdapter.onPlayBtnClick(index);
        } else {
            playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
        }
    }

    public void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_white));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordStudyPlanDetailActivity.clearSign();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer = null;
        }
    }


}
