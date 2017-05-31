package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;

import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ToastUtil;

import java.util.ArrayList;

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

public class WordStudyNewWordActivity extends BaseActivity {

    @BindView(R.id.renzhi_layout)
    FrameLayout renzhiLayout;
    @BindView(R.id.duyinxuanci_layout)
    FrameLayout duyinxuanciLayout;
    @BindView(R.id.danciceshi_layout)
    CardView danciceshiLayout;
    @BindView(R.id.ciyixuanci_layout)
    CardView ciyixuanciLayout;
    @BindView(R.id.bottom)
    CardView bottom;
    @BindView(R.id.dancisuji_layout)
    CardView dancisujiLayout;
    @BindView(R.id.pinxie_layout)
    CardView pinxieLayout;
    private int page;
    private int pagesize = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_study_new_word_activity);
        ButterKnife.bind(this);
        init();
        getDataTask();
    }

    private void init() {
        int total = DataBaseUtil.getInstance().countNewWordNumber();
        if(total == 0){
            ToastUtil.diaplayMesLong(this,"没有需要挑战的生词");
            finish();
        }
        int pageNum = total / pagesize;
        if(pageNum == 0){
            pageNum = 1;
        }
        setActionBarTitle((page+1)+"/"+pageNum);
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
        intent.putExtra(KeyUtil.ClassName, "");
        intent.putExtra(KeyUtil.CourseId, 1);
        intent.putExtra(KeyUtil.CourseNum, 1);
        intent.putExtra(KeyUtil.ClassId, "");
        intent.putExtra(KeyUtil.isNewWordStudy, true);
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
        WordStudyPlanDetailActivity.itemList = new ArrayList<WordDetailListItem>();
        WordStudyPlanDetailActivity.itemList.addAll(DataBaseUtil.getInstance().getNewWordList(page,pagesize) );
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
        if (WordStudyPlanDetailActivity.itemList != null) {
            WordStudyPlanDetailActivity.itemList.clear();
            WordStudyPlanDetailActivity.itemList = null;
        }
    }

}
