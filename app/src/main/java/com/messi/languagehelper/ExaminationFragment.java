package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.messi.languagehelper.adapter.ExaminationListAdapter;
import com.messi.languagehelper.util.AVAnalytics;
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

public class ExaminationFragment extends BaseFragment {

	private TabLayout tablayout;
	private ViewPager viewpager;
	private ExaminationListAdapter pageAdapter;
	private List<AVObject> avObjects;
	private SharedPreferences spf;
	private boolean isNeedSaveData;

	public static ExaminationFragment getInstance() {
		return new ExaminationFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.tablayout_fragment, container, false);
		initViews(view);
		initData();
		return view;
	}
	
	private void initViews(View view){
		spf = Setings.getSharedPreferences(getContext());
		tablayout = (TabLayout) view.findViewById(R.id.tablayout);
		viewpager = (ViewPager) view.findViewById(R.id.viewpager);
		avObjects = new ArrayList<AVObject>();
	}
	
	private void initData(){
		try {
			long lastTimeSave = spf.getLong(KeyUtil.SaveLastTime_ExaminationType, 0);
			if(System.currentTimeMillis() - lastTimeSave > 1000*60*60*24*10){
				SaveData.deleteObject(getContext(), "ExaminationActivity");
				LogUtil.DefalutLog("deleteObject   ExaminationActivity");
				new QueryTask(this).execute();
			}else{
				List<String> listStr =  (ArrayList<String>) SaveData.getObject(getContext(), "ExaminationActivity");
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
			new QueryTask(ExaminationFragment.this).execute();
			e.printStackTrace();
		}
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<ExaminationFragment> mainActivity;

		public QueryTask(ExaminationFragment mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

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
		pageAdapter = new ExaminationListAdapter(getChildFragmentManager(),getContext(),avObjects);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOffscreenPageLimit(5);
		tablayout.setupWithViewPager(viewpager);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.search, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.action_search:
//				toMoreActivity();
//				break;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	private void toMoreActivity() {
		toActivity(SearchActivity.class, null);
		AVAnalytics.onEvent(getContext(), "index_pg_to_morepg");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(avObjects != null && isNeedSaveData){
			List<String> listStr = new ArrayList<String>();
			for(AVObject item : avObjects){
				listStr.add(item.toString());
			}
			SaveData.saveObject(getContext(), "ExaminationActivity", listStr);
			Setings.saveSharedPreferences(spf, KeyUtil.SaveLastTime_ExaminationType,
					System.currentTimeMillis());
			LogUtil.DefalutLog("saveObject   ExaminationActivity");
		}
	}
	
	
}
