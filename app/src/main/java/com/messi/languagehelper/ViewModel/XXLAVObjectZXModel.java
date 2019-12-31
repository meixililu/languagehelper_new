package com.messi.languagehelper.ViewModel;

import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

public class XXLAVObjectZXModel extends XXLZXRootModel{

    private AVObject mADObject;

    public XXLAVObjectZXModel(Context mContext) {
        super(mContext);
        TXADType = 1;
    }

    public XXLAVObjectZXModel(Context mContext, int adType) {
        super(mContext);
        TXADType = adType;
    }

    @Override
    public void getXXLAd(){
        try {
            String currentAD = ADUtil.getAdProvider(counter);
            if(!TextUtils.isEmpty(currentAD)){
                LogUtil.DefalutLog("------ad-------"+currentAD);
                if(ADUtil.GDT.equals(currentAD)){
                    loadTXAD();
                }else if(ADUtil.BD.equals(currentAD)){
                    if(TXADType == 1){
                        loadXFAD();
                    }else {
                        loadBDAD();
                    }
                }else if(ADUtil.CSJ.equals(currentAD)){
                    if(TXADType == 1){
                        loadTXAD();
                    }else {
                        loadCSJAD();
                    }
                }else if(ADUtil.XF.equals(currentAD)){
                    loadXFAD();
                }else if(ADUtil.XBKJ.equals(currentAD)){
                    loadXBKJ();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addXFAD(NativeDataRef nad) {
        mADObject = new AVObject();
        mADObject.put(KeyUtil.ADKey, nad);
        mADObject.put(KeyUtil.ADIsShowKey, false);
    }

    @Override
    public void addTXAD(NativeExpressADView mADView) {
        mADObject = new AVObject();
        mADObject.put(KeyUtil.TXADView, mADView);
    }

    @Override
    public void addBDAD(AdView adView) {
        int height = (int)(SystemUtil.SCREEN_WIDTH / 2);
        mADObject = new AVObject();
        mADObject.put(KeyUtil.BDADView, adView);
        mADObject.put(KeyUtil.BDADViewHeigh, height);
    }

    @Override
    public void addCSJAD(TTFeedAd ad) {
        mADObject = new AVObject();
        mADObject.put(KeyUtil.CSJADView, ad);
    }

    @Override
    public boolean addAD() {
        if (mADObject != null && NullUtil.isNotEmpty(avObjects) && mAdapter != null) {
            int index = avObjects.size() - Setings.page_size + NumberUtil.randomNumberRange(1, 2);
            if (index < 0) {
                index = 0;
            }
            avObjects.add(index, mADObject);
            mAdapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        } else {
            return true;
        }
    }

}
