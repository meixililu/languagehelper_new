package com.messi.languagehelper.ViewModel;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.qq.e.ads.nativ.NativeExpressADView;

public class XXLCNWBeanModel extends XXLRootModel{

    public CNWBean mADObject;

    public XXLCNWBeanModel(Context mContext) {
        super(mContext);
        TXADType = 1;
    }

    public XXLCNWBeanModel(Context mContext,int adType) {
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
                        loadTXAD();
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
        mADObject = new CNWBean();
        mADObject.setmNativeADDataRef(nad);
        mADObject.setAdShow(false);
    }

    @Override
    public void addTXAD(NativeExpressADView mADView) {
        mADObject = new CNWBean();
        mADObject.setmTXADView(mADView);
    }

    @Override
    public void addBDAD(AdView adView) {

    }

    @Override
    public void addCSJAD(TTFeedAd ad) {

    }

    @Override
    public boolean addAD() {
        if (mADObject != null && NullUtil.isNotEmpty(avObjects) && mAdapter != null) {
            int index = avObjects.size() - Setings.page_size + NumberUtil.randomNumberRange(1, 2);
            if (index < 0) {
                index = 0;
            }
            avObjects.add(index, mADObject);
            mAdapter.notifyItemInserted(index);
            mADObject = null;
            return false;
        } else {
            return true;
        }
    }

}
