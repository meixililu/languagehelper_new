package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;

/**
 * Created by luli on 10/23/16.
 */

public class RcPybsMenuAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, String, Object> {

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.word_study_summary_list_item, parent, false);
        return new RcPybsMenuItemViewHolder(characterView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        String mAVObject = getItem(position);
        RcPybsMenuItemViewHolder itemViewHolder = (RcPybsMenuItemViewHolder) holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
