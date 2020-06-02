package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.WordDetailListItem;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudyCiYiXuanCiAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, WordDetailListItem, Object> {

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.word_study_ciyixuanci_list_item, parent, false);
        return new RcWordStudyCiYiXuanCiViewHolder(characterView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        WordDetailListItem mAVObject = getItem(position);
        RcWordStudyCiYiXuanCiViewHolder itemViewHolder = (RcWordStudyCiYiXuanCiViewHolder)holder;
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
