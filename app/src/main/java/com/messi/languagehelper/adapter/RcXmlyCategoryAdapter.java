package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;
import com.ximalaya.ting.android.opensdk.model.category.Category;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyCategoryAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, Category, Object> {

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.xmly_vategory_list_item, parent, false);
        return new RcXmlyCategoryItemViewHolder(characterView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Category mAVObject = getItem(position);
        RcXmlyCategoryItemViewHolder itemViewHolder = (RcXmlyCategoryItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
