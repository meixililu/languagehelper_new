package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.messi.languagehelper.adapter.StudyDialogCategoryListAdapter;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.NullUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class StudyDialogListActivity extends BaseActivity {

	private ListView studylist_lv;
	private StudyDialogCategoryListAdapter mAdapter;
	private List<AVObject> avObjects;
	private String SDCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_list_fragment);
		initSwipeRefresh();
		initViews();
		new QueryTask(this).execute();
	}

	private void initViews(){
		SDCode = getIntent().getStringExtra(AVOUtil.StudyDialogCategory.SDCode);
		avObjects = new ArrayList<AVObject>();
		studylist_lv = (ListView) findViewById(R.id.studylist_lv);
		mAdapter = new StudyDialogCategoryListAdapter(this, avObjects, SDCode);
		studylist_lv.setAdapter(mAdapter);
	}

	@Override
	public void onSwipeRefreshLayoutRefresh() {
		super.onSwipeRefreshLayoutRefresh();
		new QueryTask(this).execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<StudyDialogListActivity> mainActivity;

		public QueryTask(StudyDialogListActivity mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.StudyDialogListCategory.StudyDialogListCategory);
			query.whereEqualTo(AVOUtil.StudyDialogListCategory.SDCode, SDCode);
			query.whereEqualTo(AVOUtil.StudyDialogListCategory.SDLIsValid, "1");
			query.orderByDescending(AVOUtil.StudyDialogListCategory.SDLOrder);
			List<AVObject> avObject  = null;
			try{
				avObject = query.find();
			}catch (Exception e){
				e.printStackTrace();
			}
			if(NullUtil.isNotEmpty(avObject)){
				avObjects.clear();
				avObjects.addAll(avObject);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(mainActivity.get() != null){
				hideProgressbar();
				onSwipeRefreshLayoutFinish();
				mAdapter.notifyDataSetChanged();
			}
		}
	}
}
