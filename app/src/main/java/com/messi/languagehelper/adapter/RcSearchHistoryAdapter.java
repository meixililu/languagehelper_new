package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.impl.AdapterStringListener;
import com.messi.languagehelper.util.XFYSAD;

/**
 * Created by luli on 10/23/16.
 */

public class RcSearchHistoryAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {

    private AdapterStringListener mlistener;

    public RcSearchHistoryAdapter(AdapterStringListener mlistener){
        this.mlistener = mlistener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.search_history_list, parent, false);
        return new RcSeachHistoryListItemViewHolder(characterView,mlistener);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcSeachHistoryListItemViewHolder itemViewHolder = (RcSeachHistoryListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
