package com.messi.languagehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenAIListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Reading, Object> {

    private List<Reading> avObjects;
    private Context mContext;
    private XFYSAD mXFYSAD;
    private boolean isPlayList;

    public RcSpokenAIListAdapter(List<Reading> avObjects,XFYSAD mXFYSAD,
                                 Context mContext){
        this.avObjects = avObjects;
        this.mXFYSAD = mXFYSAD;
        this.mContext = mContext;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.spoken_ai_ad_header, parent, false);
        return new RcSpokenAiHeaderViewHolder(headerView,mXFYSAD,mContext);
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.read_list_item, parent, false);
        return new RcReadingListItemViewHolder(characterView,avObjects,isPlayList);
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
