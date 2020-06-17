package com.messi.languagehelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.databinding.WordHomeFragmentBinding;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.ChangeDataTypeUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.SaveData;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WordStudyFragment extends BaseFragment {

	private WordListItem wordCourseItem;
	private WordHomeFragmentBinding binding;
	public static ArrayList<WordDetailListItem> itemList;
	private boolean isFinishWordBook;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = WordHomeFragmentBinding.inflate(inflater);
		init();
		setCourseName();
		getDataTask();
		return binding.getRoot();
	}
	
	private void init(){
		liveEventBus();
		itemList = new ArrayList<WordDetailListItem>();
		binding.myAwesomeToolbar.setTitle(getString(R.string.title_words));
		binding.startToStudy.setOnClickListener(view -> toWordStudyDetailActivity());
		binding.wordStudyChangePlan.setOnClickListener(view -> toActivity(WordStudyPlanActivity.class, null));
		binding.wordStudyNewWord.setOnClickListener(view -> toActivity(WordStudyNewWordActivity.class, null));
		binding.renzhiLayout.setOnClickListener(view -> ToAvtivity(WordStudyDanCiRenZhiActivity.class));
		binding.dancixuanyiLayout.setOnClickListener(view -> ToAvtivity(WordStudyDanCiXuanYiActivity.class));
		binding.ciyixuanciLayout.setOnClickListener(view -> ToAvtivity(WordStudyCiYiXuanCiActivity.class));
		binding.pinxieLayout.setOnClickListener(view -> ToAvtivity(WordStudyDanCiPinXieActivity.class));
		binding.duyinxuanciLayout.setOnClickListener(view -> ToAvtivity(WordStudyDuYinXuanCiActivity.class));
		binding.duyisujiLayout.setOnClickListener(view -> ToAvtivity(WordStudyDuYinSuJiActivity.class));
		binding.wordStudyChangeUnit.setOnClickListener(view -> ToAvtivity(WordStudyChangeUnitActivity.class));
	}

	private void setCourseName(){
		wordCourseItem = SaveData.getDataFonJson(getContext(), KeyUtil.WordStudyUnit, WordListItem.class);
		if (wordCourseItem != null) {
			binding.myAwesomeToolbar.setTitle(wordCourseItem.getTitle());
			binding.wordStudyBookName.setText("第" + wordCourseItem.getCourse_id() + "单元");
			binding.studyProgress.setMax(wordCourseItem.getCourse_num());
			binding.studyProgress.setProgress(wordCourseItem.getCourse_id());
			if (wordCourseItem.getCourse_id() > wordCourseItem.getCourse_num()) {
				binding.wordStudyBookName.setText("第" + wordCourseItem.getCourse_num() + "单元");
				isFinishWordBook = true;
			}else {
				isFinishWordBook = false;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setTask();
	}

	private void setTask(){
		if (binding != null && binding.wordSum != null) {
			if (isFinishWordBook) {
				binding.wordSum.setText("100%");
			} else {
				int task = (int) (getHasLearnWordNum() / 21.0 * 100);
				if (task > 0) {
					binding.startToStudy.setText(getText(R.string.word_study_continue));
				} else {
					binding.startToStudy.setText(getText(R.string.word_start));
				}
				binding.wordSum.setText( String.valueOf(task)+"%" );
			}
		}
	}

	private void toWordStudyDetailActivity() {
		Intent intent = new Intent();
		Class toClass = null;
		if (wordCourseItem != null) {
			if (itemList == null) {
				getDataTask();
				return;
			}
			if (isFinishWordBook) {
				showFinishDialog();
				return;
			}
			toClass = WordDetailActivity.class;
			intent.putExtra(KeyUtil.ActionbarTitle, wordCourseItem.getTitle());
			intent.putExtra(KeyUtil.WordTestType, "learn");
			intent.putParcelableArrayListExtra(KeyUtil.List, itemList);
		} else {
			toClass = WordStudyPlanActivity.class;
		}
		intent.setClass(getContext(),toClass);
		startActivity(intent);
	}

	private void getDataTask() {
		showProgressbar();
		Observable.create((ObservableOnSubscribe<String>) e -> {
			loadData();
			e.onComplete();
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

	private void onFinishLoadData() {
		setTask();
	}

	private void loadData() {
		try {
			if (wordCourseItem == null) {
				return;
			}
			LogUtil.DefalutLog("class_id:"+wordCourseItem.getClass_id()+"---course:"+wordCourseItem.getCourse_id());
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordStudyDetail.WordStudyDetail);
			query.whereEqualTo(AVOUtil.WordStudyDetail.class_id, wordCourseItem.getClass_id());
			query.whereEqualTo(AVOUtil.WordStudyDetail.course, wordCourseItem.getCourse_id());
			query.orderByAscending(AVOUtil.WordStudyDetail.item_id);
			List<AVObject> avObjects = query.find();
			if (NullUtil.isNotEmpty(avObjects)) {
				itemList.clear();
				for (AVObject mAVObject : avObjects) {
					itemList.add(ChangeDataTypeUtil.changeData(mAVObject));
				}
			}
			BoxHelper.saveAndGetStatusList(itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ToAvtivity(Class toClass) {
		if (wordCourseItem != null && NullUtil.isNotEmpty(itemList)) {
			Intent intent = new Intent(getContext(), toClass);
			intent.putExtra(KeyUtil.ClassName, wordCourseItem.getTitle());
			intent.putExtra(KeyUtil.CourseId, wordCourseItem.getCourse_id());
			intent.putExtra(KeyUtil.CourseNum, wordCourseItem.getCourse_num());
			intent.putExtra(KeyUtil.ClassId, wordCourseItem.getClass_id());
			startActivity(intent);
		}
	}

	public void liveEventBus(){
		LiveEventBus.get(KeyUtil.UpdateWordStudyPlan).observe(getViewLifecycleOwner(), result -> {
			LogUtil.DefalutLog("---UpdateWordStudyPlan---onEvent");
			setCourseName();
			getDataTask();
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		clearData();
	}

	private void clearData() {
		if (itemList != null) {
			itemList.clear();
			itemList = null;
		}
	}

	public int getHasLearnWordNum(){
		int count = 0;
		if(NullUtil.isNotEmpty(itemList)){
			BoxHelper.saveAndGetStatusList(itemList);
			for(WordDetailListItem item : itemList){
				if (item.isIs_know()) {
					count++;
				}
			}
		}
		return count;
	}

	public static void clearSign() {
		if (itemList != null && itemList.size() > 0) {
			for (WordDetailListItem mAVObject : itemList) {
				mAVObject.setSelect_time(0);
			}
		}
	}

	private void showFinishDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
		builder.setTitle("恭喜你");
		builder.setMessage("已经学完本课程所有单词，请更换单词书继续学习。");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
				wordCourseItem = null;
				toWordStudyDetailActivity();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}