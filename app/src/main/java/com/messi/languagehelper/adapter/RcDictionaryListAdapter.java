package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.util.ViewUtil;

import java.util.List;

public class RcDictionaryListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Dictionary, Object> {

	public Context context;
	private List<Dictionary> beans;
	private DictionaryTranslateListener mDictionaryTranslateListener;

	public RcDictionaryListAdapter(Context context,List<Dictionary> beans,
								   DictionaryTranslateListener mDictionaryTranslateListener){
		this.mDictionaryTranslateListener = mDictionaryTranslateListener;
		this.context = context;
		this.beans = beans;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_dictionary, parent, false);
		return new RcDictionaryListItemViewHolder(characterView,context,beans,this,
				mDictionaryTranslateListener);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		Dictionary mAVObject = getItem(position);
		RcDictionaryListItemViewHolder mItemViewHolder = (RcDictionaryListItemViewHolder)holder;
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
