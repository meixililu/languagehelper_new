package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcSymbolListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, SymbolListDao, Object> {

    private List<SymbolListDao> avObjects;
    private XFYSAD mXFYSAD;

    public RcSymbolListAdapter(List<SymbolListDao> avObjects,XFYSAD mXFYSAD){
        this.avObjects = avObjects;
        this.mXFYSAD = mXFYSAD;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.xunfei_ysad_item, parent, false);
        return new RcAdHeaderViewHolder(headerView,mXFYSAD);
    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.symbol_list_item, parent, false);
        return new RcSymbloListItemViewHolder(characterView,avObjects);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        SymbolListDao mAVObject = getItem(position);
        RcSymbloListItemViewHolder itemViewHolder = (RcSymbloListItemViewHolder)holder;
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
