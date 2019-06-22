package com.messi.languagehelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.avos.avoscloud.AVObject;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.HeaderFooterRecyclerViewAdapter;

/**
 * Created by luli on 10/23/16.
 */

public class RcXVideoDetailListAdapter extends HeaderFooterRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {

    private SimpleExoPlayer player;
    private PlayerView player_view;
    private WebView mWebView;

    public RcXVideoDetailListAdapter(SimpleExoPlayer player,PlayerView player_view,WebView mWebView){
        this.player = player;
        this.player_view = player_view;
        this.mWebView = mWebView;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.xvideo_detail_list_item, parent, false);
        return new RcXVideoDetailListItemViewHolder(characterView,player,player_view,mWebView);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcXVideoDetailListItemViewHolder itemViewHolder = (RcXVideoDetailListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
