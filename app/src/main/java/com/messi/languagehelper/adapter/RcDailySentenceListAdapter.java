package com.messi.languagehelper.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.EveryDaySentence;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcDailySentenceListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, EveryDaySentence, Object> {


    private Activity context;
    private List<EveryDaySentence> beans;
    private MediaPlayer mPlayer;
    private FragmentProgressbarListener mProgressbarListener;
    private XFYSAD mXFYSAD;

    public RcDailySentenceListAdapter(Activity context, List<EveryDaySentence> beans, MediaPlayer mPlayer,
                                      FragmentProgressbarListener mProgressbarListener, XFYSAD mXFYSAD) {
        this.context = context;
        this.beans = beans;
        this.mPlayer = mPlayer;
        this.mProgressbarListener = mProgressbarListener;
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
        View characterView = inflater.inflate(R.layout.daily_sentence_list_item, parent, false);
        return new RcDailySentenceListItemViewHolder(characterView, context, beans, mPlayer, mProgressbarListener, this);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        EveryDaySentence mAVObject = getItem(position);
        RcDailySentenceListItemViewHolder itemViewHolder = (RcDailySentenceListItemViewHolder) holder;
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
