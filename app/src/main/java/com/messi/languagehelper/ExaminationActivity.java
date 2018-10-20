package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.ExaminationListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Setings;

import java.util.ArrayList;
import java.util.List;

public class ExaminationActivity extends BaseActivity implements FragmentProgressbarListener{

	private TabLayout tablayout;
	private ViewPager viewpager;
	private ExaminationListAdapter pageAdapter;
	private List<AVObject> avObjects;
	private SharedPreferences spf;
	private boolean isNeedSaveData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_activity);
		initViews();
		initData(); 
	}
	
	private void initViews(){
		spf = Setings.getSharedPreferences(this);
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		avObjects = new ArrayList<AVObject>();
	}
	
	private void initData(){
		try {
			long lastTimeSave = spf.getLong(KeyUtil.SaveLastTime_ExaminationType, 0);
			if(System.currentTimeMillis() - lastTimeSave > 1000*60*60*24*10){
				SaveData.deleteObject(this, "ExaminationActivity");
				LogUtil.DefalutLog("deleteObject   ExaminationActivity");
				new QueryTask().execute();
			}else{
				List<String> listStr =  (ArrayList<String>) SaveData.getObject(this, "ExaminationActivity");
				if(listStr == null || listStr.size() == 0){
					LogUtil.DefalutLog("avObjects is null");
					new QueryTask().execute();
				}else{
					LogUtil.DefalutLog("avObjects is not null");
					for(String str : listStr){
						avObjects.add(AVObject.parseAVObject(str));
					}
					initTabTitle();
				}
			}
		} catch (Exception e) {
			new QueryTask().execute();
			e.printStackTrace();
		}
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.ExaminationType.ExaminationType);
			query.whereEqualTo(AVOUtil.CompositionType.is_valid, "1");
			query.orderByAscending(AVOUtil.CompositionType.order);
			try {
				List<AVObject> avObject  = query.find();
				if(avObject != null){
					avObjects.clear();
					avObjects.addAll(avObject);
					isNeedSaveData = true;
				}
			} catch (AVException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			hideProgressbar();
			initTabTitle();
		}
	}
	
	private void initTabTitle(){
		pageAdapter = new ExaminationListAdapter(getSupportFragmentManager(),this,avObjects);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOffscreenPageLimit(5);
		tablayout.setTabsFromPagerAdapter(pageAdapter);
		tablayout.setupWithViewPager(viewpager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_search:
				toMoreActivity();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toMoreActivity() {
		toActivity(SearchActivity.class, null);
		AVAnalytics.onEvent(this, "index_pg_to_morepg");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(avObjects != null && isNeedSaveData){
			List<String> listStr = new ArrayList<String>();
			for(AVObject item : avObjects){
				listStr.add(item.toString());
			}
			SaveData.saveObject(this, "ExaminationActivity", listStr);
			Setings.saveSharedPreferences(spf, KeyUtil.SaveLastTime_ExaminationType,
					System.currentTimeMillis());
			LogUtil.DefalutLog("saveObject   ExaminationActivity");
		}
	}
	
	
}
