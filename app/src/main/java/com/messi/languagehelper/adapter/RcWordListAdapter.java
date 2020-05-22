package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;
import com.messi.languagehelper.util.XFYSAD;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {

    private XFYSAD mXFYSAD;
    private String sign;

    public RcWordListAdapter(XFYSAD mXFYSAD){
        this.mXFYSAD = mXFYSAD;
    }

    public RcWordListAdapter(XFYSAD mXFYSAD,String sign){
        this.mXFYSAD = mXFYSAD;
        this.sign = sign;
    }

//    @Override
//    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = getLayoutInflater(parent);
//        View headerView = inflater.inflate(R.layout.xunfei_ysad_item, parent, false);
//        return new RcAdHeaderViewHolder(headerView,mXFYSAD);
//    }
//
//    @Override
//    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//        super.onBindHeaderViewHolder(holder, position);
//    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.studylist_gridview_item, parent, false);
        return new RcWordListItemViewHolder(characterView,sign);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcWordListItemViewHolder itemViewHolder = (RcWordListItemViewHolder)holder;
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
