package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.UserSpeakBean;
import com.messi.languagehelper.impl.PractisePlayUserPcmListener;

/**
 * Created by luli on 10/23/16.
 */

public class RcPractiseListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, UserSpeakBean, Object> {

    private PractisePlayUserPcmListener mPractisePlayUserPcmListener;

    public RcPractiseListAdapter(PractisePlayUserPcmListener mPractisePlayUserPcmListener){
        this.mPractisePlayUserPcmListener = mPractisePlayUserPcmListener;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.practice_activity_lv_item, parent, false);
        return new RcPracticeListItemViewHolder(characterView,mPractisePlayUserPcmListener);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserSpeakBean mAVObject = getItem(position);
        RcPracticeListItemViewHolder itemViewHolder = (RcPracticeListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
