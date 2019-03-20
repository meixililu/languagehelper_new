package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.impl.AdapterStringListener;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcCaricatureCategoryAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, CNWBean, Object> {

    private List<AVObject> list;
    private AdapterStringListener listener;

    public RcCaricatureCategoryAdapter(List<AVObject> list,AdapterStringListener listener){
        this.list = list;
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.xmly_tags_list_header, parent, false);
        return new RcCaricatureTagHeaderViewHolder(headerView,listener);
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
        RcCaricatureTagHeaderViewHolder headerViewHolder = (RcCaricatureTagHeaderViewHolder)holder;
        headerViewHolder.setData(list);
    }
    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.caricature_home_list_item, parent, false);
        return new RcCaricatureHomeListItemViewHolder(characterView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        CNWBean mAVObject = getItem(position);
        RcCaricatureHomeListItemViewHolder itemViewHolder = (RcCaricatureHomeListItemViewHolder)holder;
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
