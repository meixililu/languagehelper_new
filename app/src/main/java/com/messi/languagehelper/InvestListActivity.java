package com.messi.languagehelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.InvestListAdapter;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.XFYSAD;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class InvestListActivity extends BaseActivity{

	private ListView category_lv;
	private InvestListAdapter mAdapter;
	private List<AVObject> avObjects;
	private XFYSAD mXFYSAD;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invest_list_activity);
		initSwipeRefresh();
		initViews();
		new QueryTask(this).execute();
	}
	
	private void initViews(){
		setActionBarTitle(this.getResources().getString(R.string.invest_activity_title));
		avObjects = new ArrayList<AVObject>();
		category_lv = (ListView) findViewById(R.id.studycategory_lv);
		View headerView = LayoutInflater.from(this).inflate(R.layout.xunfei_ysad_item, null);
		category_lv.addHeaderView(headerView);
		mAdapter = new InvestListAdapter(this, avObjects);
		category_lv.setAdapter(mAdapter);
		
		mXFYSAD = new XFYSAD(this, headerView, ADUtil.SecondaryPage);
		mXFYSAD.showAd();
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		super.onSwipeRefreshLayoutRefresh();
		new QueryTask(this).execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<InvestListActivity> mainActivity;

		public QueryTask(InvestListActivity mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.InvestList.InvestList);
			query.orderByAscending(AVOUtil.InvestList.createdAt);
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
