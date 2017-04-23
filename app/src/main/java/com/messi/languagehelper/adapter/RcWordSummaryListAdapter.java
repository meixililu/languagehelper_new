package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.XFYSAD;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordSummaryListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {

    private XFYSAD mXFYSAD;

    public RcWordSummaryListAdapter(XFYSAD mXFYSAD){
        this.mXFYSAD = mXFYSAD;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.xunfei_ysad_item, parent, false);
        return new RcAdHeaderViewHolder(headerView);
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
        RcAdHeaderViewHolder mRcAdHeaderViewHolder = (RcAdHeaderViewHolder)holder;
        mRcAdHeaderViewHolder.setXFYSAD(mXFYSAD);
        mRcAdHeaderViewHolder.start();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.studylist_gridview_item, parent, false);
        return new RcWordSummaryListItemViewHolder(characterView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcWordSummaryListItemViewHolder itemViewHolder = (RcWordSummaryListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

//    @Override
//    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = getLayoutInflater(parent);
//        View footerView = inflater.inflate(R.layout.footerview, parent, false);
//        return new RcLmFooterViewHolder(footerView);
//    }
//
//    @Override
//    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
//        super.onBindFooterViewHolder(holder, position);
//    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
