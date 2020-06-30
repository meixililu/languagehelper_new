package com.messi.languagehelper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.messi.languagehelper.R
import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter

/**
 * Created by luli on 10/23/16.
 */
class RcBiliSearchResultAdapter : HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder?, Any?, BoutiquesBean?, Any?>() {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = getLayoutInflater(parent)
        val characterView = inflater.inflate(R.layout.boutiques_list_item, parent, false)
        return RcBiliSearchResultViewHolder(characterView)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val mAVObject = getItem(position)
        val itemViewHolder = holder as RcBiliSearchResultViewHolder
        itemViewHolder.render(mAVObject!!)
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = getLayoutInflater(parent)
        val footerView = inflater.inflate(R.layout.footerview, parent, false)
        return RcLmFooterViewHolder(footerView)
    }

    override fun onBindFooterViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        super.onBindFooterViewHolder(holder, position)
    }

    private fun getLayoutInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

}