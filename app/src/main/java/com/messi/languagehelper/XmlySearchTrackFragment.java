package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcXimalayaTrackListAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.SearchTrackList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlySearchTrackFragment extends BaseFragment implements OnClickListener,IXmPlayerStatusListener {

    RecyclerView listview;
    private RcXimalayaTrackListAdapter mAdapter;
    private List<Track> avObjects;
    private int skip = 1;
    private String search_text;
    private boolean loading;
    private boolean hasMore = true;
    private LinearLayoutManager mLinearLayoutManager;

    public static Fragment newInstance(String search_text) {
        XmlySearchTrackFragment fragment = new XmlySearchTrackFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyUtil.SearchKey, search_text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.search_text = mBundle.getString(KeyUtil.SearchKey);
        registerBroadcast();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.xmly_search_reasult_fragment, container, false);
        initSwipeRefresh(view);
        listview = (RecyclerView)view.findViewById(R.id.listview);
        initViews();
        return view;
    }

    @Override
    public void loadDataOnStart() {
        QueryTask();
    }

    private void initViews() {
        avObjects = new ArrayList<Track>();
        mAdapter = new RcXimalayaTrackListAdapter(avObjects);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(mAdapter);
        setListOnScrollListener();
        XmPlayerManager.getInstance(getContext()).addPlayerStatusListener(this);
    }

    public void setListOnScrollListener() {
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        QueryTask();
                    }
                }
            }
        });
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }


    public void Refresh() {
        skip = 1;
        hideFooterview();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        QueryTask();
    }


    private void QueryTask() {
        loading = true;
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, search_text);
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Setings.page_size));
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getSearchedTracks(map, new IDataCallBack<SearchTrackList>() {
            @Override
            public void onSuccess(@Nullable SearchTrackList trackList) {
                onFinishLoadData();
                LogUtil.DefalutLog(trackList.toString());
                if (trackList != null && trackList.getTracks() != null) {
                    LogUtil.DefalutLog(trackList.toString());
                    for (Track track : trackList.getTracks()){
                        track.setUpdateStatus(false);
                    }
                    avObjects.addAll(trackList.getTracks());
                    skip += 1;
                    mAdapter.notifyDataSetChanged();
                    if (skip > trackList.getTotalPage()) {
//                        ToastUtil.diaplayMesShort(getContext(), "没有了！");
                        hideFooterview();
                        hasMore = false;
                    } else {
                        hasMore = true;
                        showFooterview();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                onFinishLoadData();
                LogUtil.DefalutLog(s);
            }
        });
    }

    private void onFinishLoadData() {
        loading = false;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    @Override
    public void updateUI(String music_action) {
        if (music_action.equals(PlayerService.action_loading)) {
            showProgressbar();
        } else if (music_action.equals(PlayerService.action_finish_loading)) {
            hideProgressbar();
        } else {
            dataChange();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dataChange();
    }

    private void dataChange(){
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XmPlayerManager.getInstance(getContext()).removePlayerStatusListener(this);
        unregisterBroadcast();
    }

    @Override
    public void onPlayStart() {
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(getContext()).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(true);
                dataChange();
            }
        }
    }

    @Override
    public void onPlayPause() {
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(getContext()).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(false);
                dataChange();
            }
        }
    }

    @Override
    public void onPlayStop() {
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(getContext()).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(false);
                dataChange();
            }
        }
    }

    @Override
    public void onSoundPlayComplete() {
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(getContext()).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(false);
                dataChange();
            }
        }
        LogUtil.DefalutLog("onSoundPlayComplete");
    }

    @Override
    public void onSoundPrepared() {

    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        dataChange();
    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int i) {

    }

    @Override
    public void onPlayProgress(int i, int i1) {

    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
}
