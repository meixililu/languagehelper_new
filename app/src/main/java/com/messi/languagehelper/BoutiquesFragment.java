package com.messi.languagehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.languagehelper.adapter.RcBoutiquesAdapter;
import com.messi.languagehelper.databinding.BoutiquesFragmentBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BoutiquesFragment extends BaseFragment {

	private RcBoutiquesAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private List<AVObject> avObjects;
	private String type;
	private String category;
	private String title;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;
    private BoutiquesFragmentBinding binding;

	public static BoutiquesFragment getInstance(Builder builder) {
		BoutiquesFragment fragment = new BoutiquesFragment();
		Bundle args = new Bundle();
		args.putString(KeyUtil.Category,builder.category);
		args.putString(KeyUtil.Type,builder.type);
		args.putString(KeyUtil.FragmentTitle,builder.title);
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
			type = getArguments().getString(KeyUtil.Type);
			title = getArguments().getString(KeyUtil.FragmentTitle);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = BoutiquesFragmentBinding.inflate(inflater);
		initSwipeRefresh(binding.getRoot());
		init(binding.getRoot());
		return binding.getRoot();
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		new QueryTask(this).execute();
	}
	
	private void init(View view){
		avObjects = new ArrayList<AVObject>();
		if(!TextUtils.isEmpty(title)){
			binding.myAwesomeToolbar.setVisibility(View.VISIBLE);
			binding.myAwesomeToolbar.setTitle(title);
		}
		mAdapter = new RcBoutiquesAdapter();
        mAdapter.setFooter(new Object());
		mAdapter.setItems(avObjects);
        hideFooterview();
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		binding.listview.setLayoutManager(mLinearLayoutManager);
		binding.listview.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(getContext())
						.colorResId(R.color.text_tint)
						.sizeResId(R.dimen.padding_7)
						.marginResId(R.dimen.padding_2, R.dimen.padding_2)
						.build());
		binding.listview.setAdapter(mAdapter);
		setListOnScrollListener();

	}

    public void setListOnScrollListener(){
		binding.listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
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
				if(!TextUtils.isEmpty(category)){
					query.whereEqualTo(AVOUtil.Boutiques.category,category);
				}
				if(!TextUtils.isEmpty(type)){
					query.whereEqualTo(AVOUtil.Boutiques.type,type);
				}
				query.orderByAscending(AVOUtil.Boutiques.order);
				query.orderByDescending(AVOUtil.Boutiques.views);
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
		if(binding != null && binding.myAwesomeToolbar.isShown()){
			binding.progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideProgressbar() {
		super.hideProgressbar();
		if(binding != null && binding.myAwesomeToolbar.isShown()){
			binding.progressBarCircularIndetermininate.setVisibility(View.GONE);
		}
	}

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }


	public static final class Builder {
		private String type;
		private String category;
		private String title;

		public Builder() {
		}

		public Builder type(String val) {
			type = val;
			return this;
		}

		public Builder category(String val) {
			category = val;
			return this;
		}

		public Builder title(String val) {
			title = val;
			return this;
		}

		public BoutiquesFragment build() {
			return BoutiquesFragment.getInstance(this);
		}
	}
}