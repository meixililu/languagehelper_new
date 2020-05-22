package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.TranLijuResult;
import com.messi.languagehelper.util.ViewUtil;

public class RcJuhaiListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, TranLijuResult, Object> {


	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.juhai_list_item, parent, false);
		return new RcJuhaiItemViewHolder(characterView);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		TranLijuResult mAVObject = getItem(position);
		RcJuhaiItemViewHolder mItemViewHolder = (RcJuhaiItemViewHolder)holder;
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
