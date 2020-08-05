package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.messi.languagehelper.adapter.ReadingJuheListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Setings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class ReadingJuheActivity extends BaseActivity implements FragmentProgressbarListener{

	private TabLayout tablayout;
	private ViewPager viewpager;
	private ReadingJuheListAdapter pageAdapter;
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
			long lastTimeSave = spf.getLong(KeyUtil.SaveLastTime_ReadingJuheActivity, 0);
			if(System.currentTimeMillis() - lastTimeSave > 1000*60*60*24*10){
				SaveData.deleteObject(this, "ReadingJuheActivity");
				LogUtil.DefalutLog("deleteObject   ReadingJuheActivity");
				new QueryTask(this).execute();
			}else{
				List<String> listStr =  (ArrayList<String>) SaveData.getObject(this, "ReadingJuheActivity");
				if(listStr == null || listStr.size() == 0){
					LogUtil.DefalutLog("avObjects is null");
					new QueryTask(this).execute();
				}else{
					LogUtil.DefalutLog("avObjects is not null");
					for(String str : listStr){
						avObjects.add(AVObject.parseAVObject(str));
					}
					initTabTitle();
				}
			}
		} catch (Exception e) {
			new QueryTask(this).execute();
			e.printStackTrace();
		}
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<ReadingJuheActivity> mainActivity;

		public QueryTask(ReadingJuheActivity mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Category.Category);
			query.whereEqualTo(AVOUtil.Category.isvalid, "1");
			query.orderByAscending(AVOUtil.Category.order);
			List<AVObject> avObject  = null;
			try{
				avObject = query.find();
			}catch (Exception e){
				e.printStackTrace();
			}
			if(NullUtil.isNotEmpty(avObject)){
				avObjects.clear();
				avObjects.addAll(avObject);
				isNeedSaveData = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(mainActivity.get() != null){
				hideProgressbar();
				initTabTitle();
			}
		}
	}
	
	private void initTabTitle(){
		pageAdapter = new ReadingJuheListAdapter(getSupportFragmentManager(),this,avObjects);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOffscreenPageLimit(7);
		tablayout.setupWithViewPager(viewpager);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(avObjects != null && isNeedSaveData){
			List<String> listStr = new ArrayList<String>();
			for(AVObject item : avObjects){
				listStr.add(item.toString());
			}
			SaveData.saveObject(this, "ReadingJuheActivity", listStr);
			Setings.saveSharedPreferences(spf, KeyUtil.SaveLastTime_ReadingJuheActivity,
					System.currentTimeMillis());
			LogUtil.DefalutLog("saveObject   ReadingJuheActivity");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//		super.onSaveInstanceState(outState, outPersistentState);
	}
	
}
