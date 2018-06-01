package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Reading;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcStudyListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Reading, Object> {

    private Activity mContext;
    private List<Reading> avObjects;
    private FragmentProgressbarListener mProgressbarListener;
    private RcStudyHeaderViewHolder mRcStudyHeaderViewHolder;

    public RcStudyListAdapter(List<Reading> avObjects, FragmentProgressbarListener mProgressbarListener,
                              Activity mContext){
        this.avObjects = avObjects;
        this.mContext = mContext;
        this.mProgressbarListener = mProgressbarListener;
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
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.study_fragment_header, parent, false);
        mRcStudyHeaderViewHolder =  new RcStudyHeaderViewHolder(headerView,mProgressbarListener,mContext);
        return mRcStudyHeaderViewHolder;
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
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
