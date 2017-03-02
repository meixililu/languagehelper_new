package com.messi.languagehelper;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.WordStudyDetailTestAdapter;
import com.messi.languagehelper.dao.WordDetailListItem;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ChangeDataTypeUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ToastUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WordStudyDuYinXuanCiActivity extends BaseActivity implements OnClickListener {

	private GridView category_lv;
	private WordStudyDetailTestAdapter mAdapter;
	private FloatingActionButton playbtn;
	private String class_name;
	private String class_id;
	private int course_id;
	private int course_num;
	private MediaPlayer mPlayer;
	private String audioPath;
	
	private SpeechSynthesizer mSpeechSynthesizer;
	private SharedPreferences mSharedPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_study_detail_test_activity);
		initSwipeRefresh();
		initViews();
		getDataTask();
	}

	private void checkData(){
		if(WordStudyFourthActivity.itemList == null){
			WordStudyFourthActivity.itemList = new ArrayList<WordDetailListItem>();
		}
	}
	
	private void initViews(){
		setActionBarTitle(this.getResources().getString(R.string.dancitingxuan));
		mSharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
		mPlayer = new MediaPlayer();
		class_name = getIntent().getStringExtra(KeyUtil.ClassName);
		class_id = getIntent().getStringExtra(KeyUtil.ClassId);
		course_id = getIntent().getIntExtra(KeyUtil.CourseId, 1);
		course_num = getIntent().getIntExtra(KeyUtil.CourseNum, 0);
		if(!TextUtils.isEmpty(class_id)){
			audioPath = SDCardUtil.WordStudyPath + class_id + SDCardUtil.Delimiter + String.valueOf(course_id) + SDCardUtil.Delimiter;
		}else{
			finish();
		}
		playbtn = (FloatingActionButton) findViewById(R.id.playbtn);
		category_lv = (GridView) findViewById(R.id.studycategory_lv);
		checkData();
		mAdapter = new WordStudyDetailTestAdapter(this, mSharedPreferences, mSpeechSynthesizer,
				WordStudyFourthActivity.itemList, audioPath, mPlayer);
		category_lv.setAdapter(mAdapter);
		
		playbtn.setOnClickListener(this);
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		super.onSwipeRefreshLayoutRefresh();
		getDataTask();
	}

	private void getDataTask() {
		if(WordStudyFourthActivity.itemList == null || WordStudyFourthActivity.itemList.size() == 0){
			showProgressbar();
			Observable.create(new Observable.OnSubscribe<String>() {
				@Override
				public void call(Subscriber<? super String> subscriber) {
					loadData();
					subscriber.onCompleted();
				}
			})
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Observer<String>() {
				@Override
				public void onCompleted() {
					onFinishLoadData();
				}

				@Override
				public void onError(Throwable e) {
				}

				@Override
				public void onNext(String s) {
				}
			});
		}else {
			clearPlaySign();
			onFinishLoadData();
		}
	}

	private void clearPlaySign(){
		for(WordDetailListItem mAVObject : WordStudyFourthActivity.itemList){
			mAVObject.setBackup1("");
		}
	}

	private void loadData(){
		try {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordStudyDetail.WordStudyDetail);
			query.whereEqualTo(AVOUtil.WordStudyDetail.class_id, class_id);
			query.whereEqualTo(AVOUtil.WordStudyDetail.course, course_id);
			query.orderByAscending(AVOUtil.WordStudyDetail.item_id);
			List<AVObject> avObjects  = query.find();
			if(avObjects != null){
				WordStudyFourthActivity.itemList.clear();
				for(AVObject mAVObject : avObjects){
					WordStudyFourthActivity.itemList.add( ChangeDataTypeUtil.changeData(mAVObject) );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onFinishLoadData(){
		hideProgressbar();
		onSwipeRefreshLayoutFinish();
		mAdapter.notifyDataSetChanged();
		mAdapter.getPlayOrder();
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
	
	private void playSound(){
		if(mAdapter.isPlaying()){
			playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_stop_white_48dp));
			mAdapter.onPlayBtnClick();
		}else{
			playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
		}
	}
	
	public void stopPlay(){
		if (mPlayer != null) {   
			mPlayer.stop();  
        } 
		playbtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		WordStudyFourthActivity.clearSign();
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
        }
		if(mSpeechSynthesizer != null){
			mSpeechSynthesizer.stopSpeaking();
			mSpeechSynthesizer = null;
		}
	}
	
}
