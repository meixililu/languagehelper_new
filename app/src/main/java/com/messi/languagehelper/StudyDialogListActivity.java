package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.StudyDialogCategoryListAdapter;
import com.messi.languagehelper.util.AVOUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
			try {
				List<AVObject> avObject  = query.find();
				if(avObject != null){
					avObjects.clear();
					avObjects.addAll(avObject);
				}
			} catch (AVException e) {
				e.printStackTrace();
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
