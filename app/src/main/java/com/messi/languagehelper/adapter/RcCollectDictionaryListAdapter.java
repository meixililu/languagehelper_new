package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.util.ViewUtil;

import java.util.List;

public class RcCollectDictionaryListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Dictionary, Object> {

	public Activity context;
	private List<Dictionary> beans;
	private RcCollectDictionaryListItemViewHolder mItemViewHolder;
	private SharedPreferences mSharedPreferences;

	public RcCollectDictionaryListAdapter(Activity context,List<Dictionary> beans,
										  SharedPreferences mSharedPreferences){
		this.context = context;
		this.beans = beans;
		this.mSharedPreferences = mSharedPreferences;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_dictionary, parent, false);
		return new RcCollectDictionaryListItemViewHolder(characterView,context,beans,this,
				mSharedPreferences);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		Dictionary mAVObject = getItem(position);
		mItemViewHolder = (RcCollectDictionaryListItemViewHolder)holder;
		mItemViewHolder.render(mAVObject);
	}

	@Override
	protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
		return new RcLmFooterViewHolder(ViewUtil.getListFooterView(parent.getContext()));
	}

	@Override
	protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
		super.onBindFooterViewHolder(holder, position);
	}

	private LayoutInflater getLayoutInflater(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext());
	}
}
