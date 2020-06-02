package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.leancloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXVideoAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {


    private List<AVObject> mAVObjects;
    private String category;

    public RcXVideoAdapter(List<AVObject> mAVObjects,String category){
        this.mAVObjects = mAVObjects;
        this.category = category;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.xvideo_list_item, parent, false);
        return new RcXVideoListItemViewHolder(characterView, mAVObjects, category);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcXVideoListItemViewHolder itemViewHolder = (RcXVideoListItemViewHolder)holder;
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
