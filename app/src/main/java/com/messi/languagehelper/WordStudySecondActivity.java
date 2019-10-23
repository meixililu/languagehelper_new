package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcWordStudySecondAdapter;
import com.messi.languagehelper.bean.WordListType;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class WordStudySecondActivity extends BaseActivity {

	private RecyclerView category_lv;
	private RcWordStudySecondAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private List<WordListType> mWordTypeList;
	private String category_id;
	private String play_sign;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_type_activity);
		registerBroadcast(BaseActivity.ActivityClose);
		initSwipeRefresh();
		initViews();
	}
	
	private void initViews(){
		category_id = getIntent().getStringExtra(KeyUtil.Category);
		play_sign = getIntent().getStringExtra(KeyUtil.WordStudyPlan);
		if(!TextUtils.isEmpty(category_id)){
			mWordTypeList = new ArrayList<WordListType>();
			category_lv = (RecyclerView) findViewById(R.id.listview);
			mAdapter = new RcWordStudySecondAdapter(play_sign);
			mAdapter.setItems(mWordTypeList);
			mLinearLayoutManager = new LinearLayoutManager(this);
			category_lv.setLayoutManager(mLinearLayoutManager);
			category_lv.addItemDecoration(
					new HorizontalDividerItemDecoration.Builder(this)
							.colorResId(R.color.text_tint)
							.sizeResId(R.dimen.padding_10)
							.marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
							.build());
			category_lv.setAdapter(mAdapter);
			new QueryTask().execute();
		}else{
			finish();
		}
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		new QueryTask().execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.WordStudyType.WordStudyType);
				query.whereEqualTo(AVOUtil.WordStudyType.category_id,category_id);
				query.whereEqualTo(AVOUtil.WordStudyType.is_valid, "1");
				query.orderByAscending(AVOUtil.WordStudyType.type_id);
				List<AVObject> avObjects  = query.find();
				if(avObjects != null){
					mWordTypeList.clear();
					for(AVObject mAVObject : avObjects){
						mWordTypeList.add( changeData(mAVObject) );
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
			mAdapter.notifyDataSetChanged();
		}
	}
	
	private WordListType changeData(AVObject mAVObject){
		WordListType mType = new WordListType();
		mType.setType_id(mAVObject.getString(AVOUtil.WordStudyType.type_id));
		mType.setTitle(mAVObject.getString(AVOUtil.WordStudyType.title));
		mType.setCourse_num(mAVObject.getString(AVOUtil.WordStudyType.course_num));
		mType.setWord_num(mAVObject.getString(AVOUtil.WordStudyType.word_num));
		mType.setListJson(mAVObject.getString(AVOUtil.WordStudyType.child_list_json));
		mType.setList();
		AVFile mImgFile = mAVObject.getAVFile(AVOUtil.WordStudyType.img);
		if(mImgFile != null){
			mType.setImg_url(mImgFile.getUrl());
		}
		return mType;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}
}