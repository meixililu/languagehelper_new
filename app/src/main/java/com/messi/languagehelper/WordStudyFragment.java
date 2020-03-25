package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcWordStudyAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WordStudyFragment extends BaseFragment {

	private RecyclerView category_lv;
	private Toolbar mToolbar;
	private ProgressBar progressBar;
	private RcWordStudyAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private List<AVObject> avObjects;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;

	@Override
	public void onAttach(Context activity) {
		super.onAttach(activity);
		try {
			mProgressbarListener = (FragmentProgressbarListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.word_home_fragment, container, false);
		initSwipeRefresh(view);
		init(view);
		return view;
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		new QueryTask(this).execute();
	}
	
	private void init(View view){
		avObjects = new ArrayList<AVObject>();
		category_lv = (RecyclerView) view.findViewById(R.id.listview);
		mToolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
		mToolbar.setTitle(getString(R.string.title_words));
		mAdapter = new RcWordStudyAdapter();
		mAdapter.setHeader(new Object());
        mAdapter.setFooter(new Object());
		mAdapter.setItems(avObjects);
        hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		category_lv.setLayoutManager(mLinearLayoutManager);
		category_lv.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.padding_7)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		category_lv.setAdapter(mAdapter);
		setListOnScrollListener();
	}

    public void setListOnScrollListener(){
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
                        new QueryTask(WordStudyFragment.this).execute();
                    }
                }
            }
        });
    }

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        skip = 0;
		new QueryTask(this).execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<WordStudyFragment> mainActivity;

		public QueryTask(WordStudyFragment mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Boutiques.Boutiques);
				query.whereEqualTo(AVOUtil.Boutiques.category,"word");
				query.orderByAscending(AVOUtil.Boutiques.order);
                query.skip(skip);
                query.limit(Setings.page_size);
				List<AVObject> items  = query.find();
				if(items != null){
				    if(skip == 0){
                        avObjects.clear();
                    }
                    avObjects.addAll(items);
					skip += Setings.page_size;
				    if(items.size() == Setings.page_size){
				        hasMore = true;
                    }else {
				        hasMore = false;
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
			if(mainActivity.get() != null){
				hideProgressbar();
				onSwipeRefreshLayoutFinish();
				mAdapter.notifyDataSetChanged();
				if(hasMore){
					showFooterview();
				}else {
					hideFooterview();
				}
			}
		}
	}

	@Override
	public void showProgressbar() {
		super.showProgressbar();
		if(mToolbar != null && mToolbar.isShown()){
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideProgressbar() {
		super.hideProgressbar();
		if(mToolbar != null && mToolbar.isShown()){
			progressBar.setVisibility(View.GONE);
		}
	}

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }
	

}