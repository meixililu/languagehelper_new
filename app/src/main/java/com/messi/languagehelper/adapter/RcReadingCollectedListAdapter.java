package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.Reading;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcReadingCollectedListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Reading, Object> {

    private List<Reading> avObjects;
    private Context context;

    public RcReadingCollectedListAdapter(Context context, List<Reading> avObjects){
        this.context = context;
        this.avObjects = avObjects;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.composition_list_item, parent, false);
        return new RcReadingCollectedListItemViewHolder(context,characterView,avObjects);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Reading mAVObject = getItem(position);
        RcReadingCollectedListItemViewHolder itemViewHolder = (RcReadingCollectedListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
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
