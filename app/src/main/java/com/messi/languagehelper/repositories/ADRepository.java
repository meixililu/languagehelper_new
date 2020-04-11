package com.messi.languagehelper.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.bean.ADBean;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.BDADUtil;
import com.messi.languagehelper.util.CSJADUtil;
import com.messi.languagehelper.util.ContextUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.TXADUtil;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

public class ADRepository {

    public int counter;
    public String currentAD;

    public String BDADID = BDADUtil.BD_BANNer;
    public String XFADID = ADUtil.MRYJYSNRLAd;
    public String CSJADID = CSJADUtil.CSJ_XXLSP;
    public MutableLiveData<ADBean> mItem = new MutableLiveData<ADBean>();

    public void getSBBAD(){
        try {
            loadData(mItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(MutableLiveData<ADBean> mItem) throws Exception{
        this.mItem = mItem;
        loadAD();
    }

    public void loadAD(){
        try {
            currentAD = ADUtil.getAdProvider(counter);
            if(!TextUtils.isEmpty(currentAD)){
                LogUtil.DefalutLog("------ad-------"+currentAD);
                if(ADUtil.GDT.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.BD.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.CSJ.equals(currentAD)){
                    loadCSJAD();
                }else if(ADUtil.XF.equals(currentAD)){
                    loadCSJAD();
                }else if(ADUtil.XBKJ.equals(currentAD)){
                    loadXBKJ();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAd(){
        counter++;
        loadAD();
    }

    private void loadTXAD() {
        TXADUtil.showBigImg(ContextUtil.get().getContext(), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                LogUtil.DefalutLog("loadTXAD0-onNoAD");
                getAd();
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                LogUtil.DefalutLog("onADLoaded");
                if (NullUtil.isNotEmpty(list)) {
                    ADBean mADBean = new ADBean(1,ADUtil.GDT);
                    mADBean.setTXADView(list.get(0));
                    mItem.setValue(mADBean);
                }
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                nativeExpressADView.render();
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
                getAd();
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

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD");
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(ContextUtil.get().getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(690, 388)
                .build();
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadCSJAD-onError:"+s);
                getAd();
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    getAd();
                    return;
                }
                ADBean mADBean = new ADBean(1,ADUtil.CSJ);
                mADBean.setTTFeedAd(ads.get(0));
                mItem.setValue(mADBean);
            }
        });
    }

    public void loadXFAD() {
        IFLYNativeAd nativeAd = new IFLYNativeAd(ContextUtil.get().getContext(), XFADID, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("LeisureModel-onAdFailed:"+arg0.getErrorDescription());
                getAd();
            }
            @Override
            public void onAdLoaded(NativeDataRef nativeDataRef) {
                if(nativeDataRef != null){
                    ADBean mADBean = new ADBean(1,ADUtil.XF);
                    mADBean.setNativeADDataRef(nativeDataRef);
                    mItem.setValue(mADBean);
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd();
    }

    public void loadXBKJ() {
        if (ADUtil.isHasLocalAd()) {
            ADBean mADBean = new ADBean(1,ADUtil.XBKJ);
            mADBean.setNativeADDataRef(ADUtil.getRandomAd(ContextUtil.get().getContext()));
            mItem.setValue(mADBean);
        }else {
            ADUtil.loadAd(ContextUtil.get().getContext());
        }
    }

}
