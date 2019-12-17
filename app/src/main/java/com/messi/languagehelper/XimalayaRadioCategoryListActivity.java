package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.ViewModel.XXLForXMLYRadioModel;
import com.messi.languagehelper.adapter.RcXmlyRadioListAdapter;
import com.messi.languagehelper.bean.RadioForAd;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioListByCategory;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XimalayaRadioCategoryListActivity extends BaseActivity implements IXmPlayerStatusListener {

    @BindView(R.id.listview)
    RecyclerView listview;
    private RcXmlyRadioListAdapter adapter;
    private List<Radio> radios;
    private int skip = 1;
    private int max_page = 1;
    private String category;
    private LinearLayoutManager mLinearLayoutManager;
    private XXLForXMLYRadioModel mXXLModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xmly_radio_list_avtivity);
        ButterKnife.bind(this);
        registerBroadcast();
        initView();
        getRankRadios();
    }

    private void initView() {
        category = getIntent().getStringExtra(KeyUtil.Category);
        radios = new ArrayList<Radio>();
        mXXLModel = new XXLForXMLYRadioModel(this);
        adapter = new RcXmlyRadioListAdapter(radios);
        adapter.setItems(radios);
        adapter.setFooter(new Object());
        mXXLModel.setAdapter(radios,adapter);
        hideFooterview();
        XmPlayerManager.getInstance(this).addPlayerStatusListener(this);
        initSwipeRefresh();
        listview.setAdapter(adapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                isADInList(recyclerView, firstVisibleItem, visible);
                if (!mXXLModel.loading && mXXLModel.hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        getRankRadios();
                    }
                }
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        if (radios.size() > 3) {
            for (int i = first; i < (first + vCount); i++) {
                if (i < radios.size() && i > 0) {
                    Radio mAVObject = radios.get(i);
                    if (mAVObject instanceof RadioForAd) {
                        if(((RadioForAd) mAVObject).getmNativeADDataRef() != null){
                            if (!((RadioForAd) mAVObject).isAdShow()) {
                                NativeDataRef mNativeADDataRef = ((RadioForAd) mAVObject).getmNativeADDataRef();
                                boolean isExposure = mNativeADDataRef.onExposure(view.getChildAt(i % vCount));
                                LogUtil.DefalutLog("isExposure:" + isExposure);
                                if(isExposure){
                                    ((RadioForAd) mAVObject).setAdShow(isExposure);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void getRankRadios() {
        if(mXXLModel != null){
            mXXLModel.loading = true;
        }
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.RADIO_CATEGORY_ID , category);
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Setings.page_size));
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getRadiosByCategory(map, new IDataCallBack<RadioListByCategory>() {
            @Override
            public void onSuccess(@Nullable RadioListByCategory radioList) {
                finishLoading();
                if (radioList != null && radioList.getRadios() != null) {
                    initStatus(radioList.getRadios());
                    radios.addAll(radioList.getRadios());
                    skip += 1;
                    adapter.notifyDataSetChanged();
                    loadAD();
                    if (skip > radioList.getTotalPage()) {
//                        ToastUtil.diaplayMesShort(XimalayaRadioCategoryListActivity.this, "没有了！");
                        hideFooterview();
                        mXXLModel.hasMore = false;
                    } else {
                        mXXLModel.hasMore = true;
                        showFooterview();
                    }
                    max_page = radioList.getTotalPage();
                }
            }

            @Override
            public void onError(int i, String s) {
                finishLoading();
            }
        });
    }

    private void initStatus(List<Radio> radios) {
        if (XmPlayerManager.getInstance(this).isPlaying()) {
            if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Radio) {
                Radio mRadio = (Radio) XmPlayerManager.getInstance(this).getCurrSound();
                for (Radio radio : radios) {
                    if (radio.getDataId() == mRadio.getDataId()) {
                        radio.setActivityLive(true);
                    } else {
                        radio.setActivityLive(false);
                    }
                }
            }
        }
    }

    private void upStatus(Radio mRadio, boolean isTrue) {
        for (Radio radio : radios) {
            if (radio.getDataId() == mRadio.getDataId()) {
                radio.setActivityLive(isTrue);
            } else {
                radio.setActivityLive(false);
            }
        }
    }

    private void finishLoading() {
        mXXLModel.loading = false;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        radios.clear();
        random();
        getRankRadios();
    }

    private void loadAD(){
        if (mXXLModel != null) {
            mXXLModel.showAd();
        }
    }

    private void random() {
        if (max_page > 1) {
            skip = new Random().nextInt(max_page) + 1;
        } else {
            skip = 1;
        }
        LogUtil.DefalutLog("random:" + skip);
    }

    @Override
    public void onResume() {
        super.onResume();
        dataChange();

    }

    private void hideFooterview() {
        adapter.hideFooter();
    }

    private void showFooterview() {
        adapter.showFooter();
    }

    private void dataChange() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void reset() {
        for (Radio mRadio : radios) {
            mRadio.setActivityLive(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        XmPlayerManager.getInstance(this).removePlayerStatusListener(this);
        if(mXXLModel != null){
            mXXLModel.onDestroy();
        }
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
    public void onPlayStart() {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Radio) {
            Radio mRadio = (Radio) XmPlayerManager.getInstance(this).getCurrSound();
            upStatus(mRadio, true);
            dataChange();
        }
    }

    @Override
    public void onPlayPause() {
        setPauseStatus();
    }

    private void setPauseStatus() {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Radio) {
            Radio mRadio = (Radio) XmPlayerManager.getInstance(this).getCurrSound();
            upStatus(mRadio, false);
            dataChange();
        }
    }

    @Override
    public void onPlayStop() {
        setPauseStatus();
    }

    @Override
    public void onSoundPlayComplete() {
        setPauseStatus();
    }

    @Override
    public void onSoundPrepared() {

    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        if (XmPlayerManager.getInstance(this).getCurrSound() instanceof Track) {
            reset();
            dataChange();
        }
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
