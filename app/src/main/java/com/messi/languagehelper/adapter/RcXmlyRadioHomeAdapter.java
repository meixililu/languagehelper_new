package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategory;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyRadioHomeAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Radio, Object> {

    private List<RadioCategory> radioCategories;
    private List<Radio> radios;

    public RcXmlyRadioHomeAdapter(List<RadioCategory> radioCategories, List<Radio> radios){
        this.radioCategories = radioCategories;
        this.radios = radios;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.xmly_radio_home_header, parent, false);
        return new RcXmlyRadioHomeHeaderViewHolder(headerView);
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
        RcXmlyRadioHomeHeaderViewHolder headerViewHolder = (RcXmlyRadioHomeHeaderViewHolder)holder;
        headerViewHolder.setData(radioCategories);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.ximalaya_radio_list_item, parent, false);
        return new RcXmlyRadioHomeItemViewHolder(characterView,radios);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Radio mAVObject = getItem(position);
        RcXmlyRadioHomeItemViewHolder itemViewHolder = (RcXmlyRadioHomeItemViewHolder)holder;
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
