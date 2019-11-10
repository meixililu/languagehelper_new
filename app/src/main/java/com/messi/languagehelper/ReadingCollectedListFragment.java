package com.messi.languagehelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcReadingCollectedListAdapter;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.service.PlayerService;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ReadingCollectedListFragment extends BaseFragment {

	private RecyclerView listview;
	private RcReadingCollectedListAdapter mAdapter;
	private List<Reading> avObjects;
	private int page = 0;
	private int pageSize = 30;
	private boolean loading;
	private boolean hasMore = true;
	private LinearLayoutManager mLinearLayoutManager;

	public static Fragment newInstance(){
		ReadingCollectedListFragment fragment = new ReadingCollectedListFragment();
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerBroadcast();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.composition_fragment, container, false);
		initViews(view);
		new QueryTask(this).execute();
		return view;
	}
	
	private void initViews(View view){
		listview = (RecyclerView) view.findViewById(R.id.listview);
		avObjects = new ArrayList<Reading>();
		initSwipeRefresh(view);
		mAdapter = new RcReadingCollectedListAdapter(getContext(),avObjects);
		mAdapter.setItems(avObjects);
		mAdapter.setFooter(new Object());
		hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		listview.setLayoutManager(mLinearLayoutManager);
		listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.list_divider_size)
						.marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
						.build());
		listview.setAdapter(mAdapter);
		setListOnScrollListener();
	}
	
	public void setListOnScrollListener(){
		listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int visible  = mLinearLayoutManager.getChildCount();
				int total = mLinearLayoutManager.getItemCount();
				int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
				if(!loading && hasMore){
					if ((visible + firstVisibleItem) >= total){
						new QueryTask(ReadingCollectedListFragment.this).execute();
					}
				}
			}
		});
	}

	private void hideFooterview(){
		mAdapter.hideFooter();
	}

	private void showFooterview(){
		mAdapter.showFooter();
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		page = 0;
		hideFooterview();
		avObjects.clear();
		mAdapter.notifyDataSetChanged();
		new QueryTask(this).execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, List<Reading>> {

		private WeakReference<ReadingCollectedListFragment> mainActivity;

		public QueryTask(ReadingCollectedListFragment mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading = true;
		}
		
		@Override
		protected List<Reading> doInBackground(Void... params) {
			return BoxHelper.getReadingCollectedList(page,pageSize);
		}

		@Override
		protected void onPostExecute(List<Reading> avObject) {
			if(mainActivity.get() != null){
				loading = false;
				onSwipeRefreshLayoutFinish();
				if(avObject != null){
					if(avObject.size() == 0){
						hideFooterview();
						hasMore = false;
					}else{
						if(avObjects != null && mAdapter != null){
							if(page == 0){
								avObjects.clear();
							}
							avObjects.addAll(avObject);
							mAdapter.notifyDataSetChanged();
							if(avObject.size() == pageSize){
								page += pageSize;
								showFooterview();
								hasMore = true;
							}else {
								hideFooterview();
								hasMore = false;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateUI(String music_action) {
		if(music_action.equals(PlayerService.action_loading)){
			showProgressbar();
		}else if(music_action.equals(PlayerService.action_finish_loading)){
			hideProgressbar();
		}else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode < avObjects.size()){
				Reading mReading = avObjects.get(requestCode);
				if(TextUtils.isEmpty(mReading.getIsCollected())){
					avObjects.remove(requestCode);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterBroadcast();
	}
}
