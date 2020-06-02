package com.messi.languagehelper.adapter;

import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.WordDetailListItem;

import java.util.List;

public class RcCollectTranslateListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, WordDetailListItem, Object> {

	private List<WordDetailListItem> beans;
	private SharedPreferences mSharedPreferences;
	private RcCollectTranslateLiatItemViewHolder mItemViewHolder;

	public RcCollectTranslateListAdapter(SharedPreferences mSharedPreferences,
										 List<WordDetailListItem> beans){
		this.beans = beans;
		this.mSharedPreferences = mSharedPreferences;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_new_word, parent, false);
		return new RcCollectTranslateLiatItemViewHolder(characterView,beans,mSharedPreferences,this);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		WordDetailListItem mAVObject = getItem(position);
		mItemViewHolder = (RcCollectTranslateLiatItemViewHolder)holder;
		mItemViewHolder.render(mAVObject);
	}

	@Override
	protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View footerView = inflater.inflate(R.layout.footerview, parent, false);
		return new RcLmFooterViewHolder(footerView);
	}

	@Override
	protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
		super.onBindFooterViewHolder(holder, position);
	}

	private LayoutInflater getLayoutInflater(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext());
	}
}
