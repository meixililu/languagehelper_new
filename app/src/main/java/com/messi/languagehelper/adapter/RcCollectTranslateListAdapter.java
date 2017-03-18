package com.messi.languagehelper.adapter;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iflytek.cloud.SpeechSynthesizer;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.util.ViewUtil;

import java.util.List;

public class RcCollectTranslateListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, record, Object> {

	private List<record> beans;
	private SharedPreferences mSharedPreferences;
	private RcCollectTranslateLiatItemViewHolder mItemViewHolder;

	public RcCollectTranslateListAdapter(SharedPreferences mSharedPreferences,
										 List<record> beans){
		this.beans = beans;
		this.mSharedPreferences = mSharedPreferences;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_recent_used, parent, false);
		return new RcCollectTranslateLiatItemViewHolder(characterView,beans,mSharedPreferences,this);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		record mAVObject = getItem(position);
		mItemViewHolder = (RcCollectTranslateLiatItemViewHolder)holder;
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
