package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.impl.AdapterStringListener;
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyRadioProvinceAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Radio, Object> {

    private List<Province> list;
    private List<Radio> radios;
    private AdapterStringListener listener;

    public RcXmlyRadioProvinceAdapter(List<Province> list,List<Radio> radios, AdapterStringListener listener){
        this.list = list;
        this.radios = radios;
        this.listener = listener;
    }

    public RcXmlyRadioProvinceAdapter(){
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.xmly_tags_list_header, parent, false);
        return new RcXmlyRadioProvinceHeaderViewHolder(headerView,listener);
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
        RcXmlyRadioProvinceHeaderViewHolder headerViewHolder = (RcXmlyRadioProvinceHeaderViewHolder)holder;
        headerViewHolder.setData(list);
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
