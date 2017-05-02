package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudySummaryListActivity;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudySummaryMenuListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {

    private WordStudySummaryListActivity mWordStudySummaryListActivity;

    public RcWordStudySummaryMenuListAdapter(WordStudySummaryListActivity mWordStudySummaryListActivity){
        this.mWordStudySummaryListActivity = mWordStudySummaryListActivity;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.word_study_summary_list_item, parent, false);
        return new RcWordStudySummaryMenuListItemViewHolder(characterView,mWordStudySummaryListActivity);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcWordStudySummaryMenuListItemViewHolder itemViewHolder = (RcWordStudySummaryMenuListItemViewHolder) holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
