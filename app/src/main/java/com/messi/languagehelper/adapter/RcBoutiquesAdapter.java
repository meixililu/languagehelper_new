package com.messi.languagehelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

/**
 * Created by luli on 10/23/16.
 */

public class RcBoutiquesAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, BoutiquesBean, Object> {

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.boutiques_list_item, parent, false);
        return new RcBoutiquesViewHolder(characterView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiquesBean mAVObject = getItem(position);
        RcBoutiquesViewHolder itemViewHolder = (RcBoutiquesViewHolder)holder;
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
