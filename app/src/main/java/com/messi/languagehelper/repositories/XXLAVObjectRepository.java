package com.messi.languagehelper.repositories;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.bean.RespoADData;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

public class XXLAVObjectRepository extends ADXXLRepository<AVObject>{

    public XXLAVObjectRepository(List avObjects) {
        super(avObjects);
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
}
