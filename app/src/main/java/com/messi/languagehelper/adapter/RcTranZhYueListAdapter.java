package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.TranResultZhYue;
import com.messi.languagehelper.util.ViewUtil;

import java.util.List;

public class RcTranZhYueListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, TranResultZhYue, Object> {

	private List<TranResultZhYue> beans;

	public RcTranZhYueListAdapter(List<TranResultZhYue> beans){
		this.beans = beans;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_recent_used, parent, false);
		return new RcTranZhYueListItemViewHolder(characterView,beans,this);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		TranResultZhYue mAVObject = getItem(position);
		RcTranZhYueListItemViewHolder mItemViewHolder = (RcTranZhYueListItemViewHolder)holder;
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

	public void addEntity(int position, TranResultZhYue entity) {
		beans.add(position, entity);
		notifyItemInserted(position);
	}
}
