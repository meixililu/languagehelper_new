package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.adapter.RcXmlyRadioHomeAdapter;
import com.messi.languagehelper.bean.RadioForAd;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.service.PlayerService;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SaveData;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategory;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategoryList;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class XimalayaRadioHomeFragment extends BaseFragment implements FragmentProgressbarListener,
        IXmPlayerStatusListener {

    Unbinder unbinder;
    @BindView(R.id.listview)
    RecyclerView listview;

    private IFLYNativeAd nativeAd;
    private RadioForAd mADObject;
    private RcXmlyRadioHomeAdapter adapter;
    private boolean loading;
    private List<Radio> radios;
    private List<RadioCategory> radioCategories;
    private LinearLayoutManager mLinearLayoutManager;

    public static Fragment newInstance(FragmentProgressbarListener listener) {
        XimalayaRadioHomeFragment fragment = new XimalayaRadioHomeFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
        radios = new ArrayList<Radio>();
        radioCategories = new ArrayList<RadioCategory>();
        adapter = new RcXmlyRadioHomeAdapter(radioCategories,radios);
        adapter.setItems(radios);
        adapter.setHeader(new Object());
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        radios.clear();
        loadAD();
        getTagsData();
        getRankRadios();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.xmly_radio_home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view){
        XmPlayerManager.getInstance(getContext()).addPlayerStatusListener(this);
        initSwipeRefresh(view);
        listview.setAdapter(adapter);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
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
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                isADInList(recyclerView, firstVisibleItem, visible);

            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount) {
        if (radios.size() > 3) {
            for (int i = first; i < (first + vCount); i++) {
                if (i < radios.size() && i > 0) {
                    Radio mAVObject = radios.get(i);
                    if (mAVObject instanceof RadioForAd) {
                        if (!((RadioForAd) mAVObject).isAdShow()) {
                            NativeADDataRef mNativeADDataRef = ((RadioForAd) mAVObject).getmNativeADDataRef();
                            boolean isExposure = mNativeADDataRef.onExposured(view.getChildAt(i % vCount));
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

    private void getTagsData() {
        showProgressbar();
        Type type = new TypeToken<List<RadioCategory>>() {}.getType();
        List<RadioCategory> tagList = SaveData.getDataListFonJson(getContext(), KeyUtil.XmlyRadioCategory, type);
        if (tagList != null) {
            radioCategories.clear();
            radioCategories.addAll(tagList);
            adapter.notifyDataSetChanged();
        } else {
            getRadioType();
        }
    }

    private void getRadioType() {
        Map<String, String> map = new HashMap<String, String>();
        CommonRequest.getRadioCategory(map, new IDataCallBack<RadioCategoryList>() {
            @Override
            public void onSuccess(RadioCategoryList object) {
                if (object != null && object.getRadioCategories() != null) {
                    radioCategories.clear();
                    radioCategories.addAll(object.getRadioCategories());
                    adapter.notifyDataSetChanged();
                    saveData();
                }
            }
            @Override
            public void onError(int code, String message) {
            }
        });
    }

    private void saveData(){
        SaveData.saveDataListAsJson(getContext(), KeyUtil.XmlyRadioCategory,radioCategories);
    }

    private void getRankRadios() {
        loading = true;
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.RADIO_COUNT, "30");
        CommonRequest.getRankRadios(map, new IDataCallBack<RadioList>() {
            @Override
            public void onSuccess(@Nullable RadioList radioList) {
                finishLoading();
                if (radioList != null && radioList.getRadios() != null) {
                    radios.addAll(radioList.getRadios());
                    initStatus();
                    if (addAD()) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(int i, String s) {
                finishLoading();
            }
        });
    }

    private void initStatus(){
        if(XmPlayerManager.getInstance(getContext()).isPlaying()){
            if(XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Radio){
                Radio mRadio = (Radio)XmPlayerManager.getInstance(getContext()).getCurrSound();
                upStatus(mRadio,true);
            }
        }
    }

    private void upStatus(Radio mRadio,boolean isTrue){
        for (Radio radio : radios){
            if(radio.getDataId() == mRadio.getDataId()){
                radio.setActivityLive(isTrue);
            }else {
                radio.setActivityLive(false);
            }
        }
    }

    private void finishLoading(){
        loading = false;
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        radios.clear();
        loadAD();
        getTagsData();
        getRankRadios();
    }

    private void loadAD() {
        nativeAd = new IFLYNativeAd(getContext(), ADUtil.XXLAD, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---" + arg0.getErrorCode() + "---" + arg0.getErrorDescription());
                if(ADUtil.isHasLocalAd()){
                    onADLoaded(ADUtil.getRandomAdList());
                }
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                if (adList != null && adList.size() > 0) {
                    NativeADDataRef nad = adList.get(0);
                    mADObject = new RadioForAd();
                    mADObject.setmNativeADDataRef(nad);
                    mADObject.setAd(true);
                    if (!loading) {
                        addAD();
                    }
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private boolean addAD() {
        if (mADObject != null && radios != null && radios.size() > 0) {
            int index = NumberUtil.randomNumberRange(1, 2);
            if (index < 0) {
                index = 0;
            }
            radios.add(index, mADObject);
            adapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dataChange();
    }

    private void dataChange(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    public void setListener(FragmentProgressbarListener listener) {
        mProgressbarListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcast();
        unbinder.unbind();
    }

    private void reset(){
        for (Radio mRadio : radios){
            mRadio.setActivityLive(false);
        }
    }

    @Override
    public void updateUI(String music_action) {
        if(music_action.equals(PlayerService.action_loading)){
            showProgressbar();
        }else if(music_action.equals(PlayerService.action_finish_loading)){
            hideProgressbar();
        }else {
            dataChange();
        }
    }

    @Override
    public void onPlayStart() {
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Radio) {
            Radio mRadio = (Radio)XmPlayerManager.getInstance(getContext()).getCurrSound();
            upStatus(mRadio,true);
            dataChange();
        }
    }

    @Override
    public void onPlayPause() {
        setPauseStatus();
    }

    private void setPauseStatus(){
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Radio) {
            Radio mRadio = (Radio)XmPlayerManager.getInstance(getContext()).getCurrSound();
            upStatus(mRadio,false);
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
        if (XmPlayerManager.getInstance(getContext()).getCurrSound() instanceof Track) {
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
