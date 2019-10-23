package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.WordListType;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudySecondAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, WordListType, Object> {

    private String play_sign;

    public RcWordStudySecondAdapter(String play_sign){
        this.play_sign = play_sign;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.word_study_root_list_item, parent, false);
        return new RcWordStudySecondViewHolder(characterView,play_sign);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        WordListType mAVObject = getItem(position);
        RcWordStudySecondViewHolder itemViewHolder = (RcWordStudySecondViewHolder)holder;
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
