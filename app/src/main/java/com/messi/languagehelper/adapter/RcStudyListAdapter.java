package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcStudyListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Reading, Object> {

    private List<Reading> avObjects;

    public RcStudyListAdapter(List<Reading> avObjects){
        this.avObjects = avObjects;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.read_list_item, parent, false);
        return new RcReadingListItemViewHolder(characterView,avObjects);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Reading mAVObject = getItem(position);
        RcReadingListItemViewHolder itemViewHolder = (RcReadingListItemViewHolder)holder;
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
