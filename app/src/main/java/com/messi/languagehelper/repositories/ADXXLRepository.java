package com.messi.languagehelper.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.BDADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TXADUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class ADXXLRepository<T> {

    public T mADObject;
    public int counter;
    public boolean isLoading;
    public boolean isShowAd;
    private WeakReference<Context> mContext;
    public String XFADID = ADUtil.XXLAD;
    public String BDADID = BDADUtil.BD_BANNer;
    public String CSJADID = CSJADUtil.CSJ_XXL;
    public int TXADType = 2;
    public IFLYNativeAd nativeAd;
    public List<NativeExpressADView> mTXADList;
    public List avObjects;
    public MutableLiveData<RespoADData> mRespoData = new MutableLiveData<>();

    public ADXXLRepository(Context context, List avObjects){
        mTXADList = new ArrayList<NativeExpressADView>();
        this.avObjects = avObjects;
        this.mContext = new WeakReference<>(context);
    }

    public void showAd(boolean isShowAd){
        if(ADUtil.IsShowAD){
            isLoading = true;
            this.isShowAd = isShowAd;
            getXXLAd();
        }
    }

    public void getXXLAd(){
        try {
            String currentAD = ADUtil.getAdProvider(counter);
            if(!TextUtils.isEmpty(currentAD)){
                LogUtil.DefalutLog("------ad-------"+currentAD);
                if(ADUtil.GDT.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.CSJ.equals(currentAD)){
                    loadCSJAD();
                }else if(ADUtil.XF.equals(currentAD)){
                    loadXFAD();
                }else if(ADUtil.XBKJ.equals(currentAD)){
                    loadXBKJ();
                }else {
                    loadTXAD();
                }
            }else {
                isLoading = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLoadAdFaile(){
        counter++;
        getXXLAd();
    }

    public void loadXFAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(getContext(), XFADID, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onAdLoaded(NativeDataRef nativeDataRef) {
                addXFAD(nativeDataRef);
                addAD();
            }

            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("XXLModel-onAdFailed"+arg0.getErrorCode());
                onLoadAdFaile();
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd();
    }

    public abstract void addXFAD(NativeDataRef nad);

    public void loadTXAD() {
        TXADUtil.showXXLAD(getContext(),TXADType, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog("TX-onNoAD");
                onLoadAdFaile();
            }
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("TX-onADLoaded");
                if(list != null && list.size() > 0 && mTXADList != null){
                    mTXADList.add(list.get(0));
                    addTXAD(list.get(0));
                    addAD();
                }
            }
            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onRenderFail");
            }
            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onRenderSuccess");
            }
            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADExposure");
            }
            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClicked");
            }
            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADClosed");
            }
            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADLeftApplication");
            }
            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADOpenOverlay");
            }
            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
                LogUtil.DefalutLog("onADCloseOverlay");
            }
        });
    }

    public abstract void addTXAD(NativeExpressADView mADView);

    public void loadXBKJ() {
        if (ADUtil.isHasLocalAd()) {
            addXFAD(ADUtil.getRandomAd(getContext()));
        }
    }

    public void loadBDAD(){
        AdView adView = new AdView(getContext(),BDADID);
        adView.setListener(new AdViewListener(){
            @Override
            public void onAdReady(AdView adView) {
                LogUtil.DefalutLog("BDAD-onAdReady");
            }
            @Override
            public void onAdShow(JSONObject jsonObject) {
                LogUtil.DefalutLog("BDAD-onAdShow");
            }
            @Override
            public void onAdClick(JSONObject jsonObject) {
                LogUtil.DefalutLog("BDAD-onAdClick");
            }
            @Override
            public void onAdFailed(String s) {
                LogUtil.DefalutLog("BDAD-onAdFailed");
                onLoadAdFaile();
            }
            @Override
            public void onAdSwitch() {
                LogUtil.DefalutLog("BDAD-onAdSwitch");
            }
            @Override
            public void onAdClose(JSONObject jsonObject) {
                LogUtil.DefalutLog("BDAD-onAdClose");
                onLoadAdFaile();
            }
        });
        addBDAD(adView);
        addAD();
    }

    public abstract void addBDAD(AdView adView);

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD");
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(690, 388)
                .build();
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadCSJAD-onError:"+s);
                onLoadAdFaile();
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    onLoadAdFaile();
                    return;
                }
                addCSJAD(ads.get(0));
                addAD();
            }
        });
    }

    public abstract void addCSJAD(TTFeedAd ad);

    public void addAD(){
        isLoading = false;
        if (isShowAd) {
            if (mADObject != null && NullUtil.isNotEmpty(avObjects) && mRespoData != null) {
                int pos = getIndex();
                avObjects.add(pos, mADObject);
                RespoADData mData = new RespoADData(1,pos);
                mRespoData.setValue(mData);
                mADObject = null;
            }
        }
    }

    public void onDestroy(){
        if(mTXADList != null){
            for(NativeExpressADView adView : mTXADList){
                adView.destroy();
            }
            mTXADList = null;
        }
    }

    public void setXFADID(String XFADID) {
        this.XFADID = XFADID;
    }

    public int getIndex(){
        int index = 0;
        if (avObjects != null) {
            index = avObjects.size() - Setings.page_size + NumberUtil.randomNumberRange(1, 3);
            if (index < 0) {
                index = 0;
            }
        }
        LogUtil.DefalutLog("insert list index:"+index);
        return index;
    }

    public Context getContext() {
        return mContext.get();
    }

}
