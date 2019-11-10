package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcBoutiquesAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BoutiquesFragment extends BaseFragment {

	private RecyclerView category_lv;
	private RcBoutiquesAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private List<AVObject> avObjects;
	private String category;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;

	public static BoutiquesFragment getInstance(String category){
        BoutiquesFragment fragment = new BoutiquesFragment();
        Bundle args = new Bundle();
        args.putString(KeyUtil.Category,category);
        fragment.setArguments(args);
        return fragment;
    }

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
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			category = getArguments().getString(KeyUtil.Category);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.boutiques_fragment, container, false);
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
		mAdapter = new RcBoutiquesAdapter();
        mAdapter.setFooter(new Object());
		mAdapter.setItems(avObjects);
        hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		category_lv.setLayoutManager(mLinearLayoutManager);
		category_lv.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.padding_10)
						.marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
						.build());
		category_lv.setAdapter(mAdapter);
	}

    public void setListOnScrollListener(){
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                LogUtil.DefalutLog("visible:"+visible+"---total:"+total+"---firstVisibleItem:"+firstVisibleItem);
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
                        new QueryTask(BoutiquesFragment.this).execute();
                    }
                }
            }
        });
    }
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
        hideFooterview();
        skip = 0;
		new QueryTask(this).execute();
	}
	
	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<BoutiquesFragment> mainActivity;

		public QueryTask(BoutiquesFragment mActivity){
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
				query.whereEqualTo(AVOUtil.Boutiques.category,category);
				query.orderByAscending(AVOUtil.Boutiques.order);
                query.skip(skip);
                query.limit(Setings.page_size);
				List<AVObject> items  = query.find();
				if(items != null){
				    if(skip == 0){
                        avObjects.clear();
                    }
                    avObjects.addAll(items);
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

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }
	

}