package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.adapter.RcXimalayaTrackListAdapter;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XimalayaTrackListActivity extends BaseActivity implements OnClickListener,IXmPlayerStatusListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.item_img)
    SimpleDraweeView itemImg;
    @BindView(R.id.track_title)
    TextView trackTitle;
    @BindView(R.id.track_info)
    TextView trackInfo;
    @BindView(R.id.btn_sort)
    FrameLayout btnSort;
    @BindView(R.id.btn_download)
    FrameLayout btnDownload;
    @BindView(R.id.img_sort)
    ImageView imgSort;
    @BindView(R.id.page_count)
    TextView pageCount;
    private RcXimalayaTrackListAdapter mAdapter;
    private List<Track> avObjects;
    private int skip = 1;
    private String album_id;
    private long play_times;
    private long track_count;
    private boolean loading;
    private boolean hasMore = true;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean init_info;
    private String sort = "asc";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ximalaya_tracklist_activity);
        ButterKnife.bind(this);
        registerBroadcast();
        album_id = getIntent().getStringExtra("album_id");
        play_times = getIntent().getLongExtra("play_times", 100000);
        track_count = getIntent().getLongExtra("track_count", 30);
        initViews();
        QueryTask();
    }

    private void initViews() {
        avObjects = new ArrayList<Track>();
        mAdapter = new RcXimalayaTrackListAdapter(avObjects);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(mAdapter);
        setListOnScrollListener();
        XmPlayerManager.getInstance(this).addPlayerStatusListener(this);
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
        map.put(DTransferConstants.ALBUM_ID, album_id);
        map.put(DTransferConstants.SORT, sort);
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Setings.page_size));
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
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
                        ToastUtil.diaplayMesShort(XimalayaTrackListActivity.this, "没有了！");
                        hideFooterview();
                        hasMore = false;
                    } else {
                        hasMore = true;
                        showFooterview();
                    }
                    if (!init_info) {
                        itemImg.setImageURI(trackList.getCoverUrlLarge());
                        trackTitle.setText(trackList.getAlbumTitle());
                        trackInfo.setText("播放：" + StringUtils.numToStrTimes(play_times));
                        pageCount.setText(" " + String.valueOf(track_count) + "集");
                        init_info = true;
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
        unregisterBroadcast();
    }

    @OnClick({R.id.btn_sort, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sort:
                if (sort.equals(XimalayaUtil.Sort_asc)) {
                    sort = XimalayaUtil.Sort_desc;
                    imgSort.setImageResource(R.drawable.main_album_sort_desc);
                } else {
                    sort = XimalayaUtil.Sort_asc;
                    imgSort.setImageResource(R.drawable.main_album_sort_asc);
                }
                Refresh();
                break;
            case R.id.btn_download:
                break;
        }
    }

    @Override
    public void onPlayStart() {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(this).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(true);
                dataChange();
            }
        }
    }

    @Override
    public void onPlayPause() {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(this).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(false);
                dataChange();
            }
        }
    }

    @Override
    public void onPlayStop() {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(this).getCurrentIndex();
            if(avObjects.size() > position){
                avObjects.get(position).setUpdateStatus(false);
                dataChange();
            }
        }
    }

    @Override
    public void onSoundPlayComplete() {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            int position = XmPlayerManager.getInstance(this).getCurrentIndex();
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
