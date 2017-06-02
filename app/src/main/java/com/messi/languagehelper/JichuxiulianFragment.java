package com.messi.languagehelper;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcJichuxiulianListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class JichuxiulianFragment extends BaseFragment implements OnClickListener{

	private RecyclerView category_lv;
	private RcJichuxiulianListAdapter mAdapter;
	private List<AVObject> avObjects;
	private XFYSAD mXFYSAD;
	private boolean isHasLoadOnce;

	public static JichuxiulianFragment getInstance(){
		JichuxiulianFragment fragment = new JichuxiulianFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
		}
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		initViews();
		new QueryTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.study_category_activity, container, false);
		initSwipeRefresh(view);
		return view;
	}
	
	private void initViews(){
		avObjects = new ArrayList<AVObject>();
		category_lv = (RecyclerView) getView().findViewById(R.id.studycategory_lv);
		mXFYSAD = new XFYSAD(getContext(), ADUtil.SecondaryPage);
		mAdapter = new RcJichuxiulianListAdapter(mXFYSAD);
		LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
		category_lv.setLayoutManager(mLinearLayoutManager);
		category_lv.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
						.build());
		mAdapter.setHeader(new Object());
		category_lv.setAdapter(mAdapter);
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		super.onSwipeRefreshLayoutRefresh();
		new QueryTask().execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
			LogUtil.DefalutLog("jichuxiulian");
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.PracticeCategory.PracticeCategory);
			query.whereEqualTo(AVOUtil.PracticeCategory.PCIsValid, "1");
			query.orderByDescending(AVOUtil.PracticeCategory.PCOrder);
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
			hideProgressbar();
			onSwipeRefreshLayoutFinish();
			mAdapter.setItems(avObjects);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		default:
			break;
		}
	}
	
}
