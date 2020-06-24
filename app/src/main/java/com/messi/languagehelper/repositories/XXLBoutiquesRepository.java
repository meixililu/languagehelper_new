package com.messi.languagehelper.repositories;

import android.content.Context;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.bean.AdData;
import com.messi.languagehelper.bean.BoutiquesBean;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

public class XXLBoutiquesRepository extends ADXXLRepository<BoutiquesBean>{

    public XXLBoutiquesRepository(Context context, List avObjects) {
        super(context,avObjects);
    }

    @Override
    public void addXFAD(NativeDataRef nad) {
        mADObject = new BoutiquesBean();
        AdData mAdData = new AdData();
        mAdData.setMNativeADDataRef(nad);
        mAdData.setAdShow(false);
        mADObject.setAd(true);
        mADObject.setAdData(mAdData);
    }

    @Override
    public void addTXAD(NativeExpressADView mADView) {
        mADObject = new BoutiquesBean();
        AdData mAdData = new AdData();
        mAdData.setMTXADView(mADView);
        mADObject.setAd(true);
        mADObject.setAdData(mAdData);
    }

    @Override
    public void addBDAD(AdView adView) {
        int height = SystemUtil.SCREEN_WIDTH / 2;
        mADObject = new BoutiquesBean();
        AdData mAdData = new AdData();
        mAdData.setBdAdView(adView);
        mAdData.setBdHeight(height);
        mADObject.setAd(true);
        mADObject.setAdData(mAdData);
    }

    @Override
    public void addCSJAD(TTFeedAd ad) {
        mADObject = new BoutiquesBean();
        AdData mAdData = new AdData();
        mAdData.setCsjTTFeedAd(ad);
        mADObject.setAd(true);
        mADObject.setAdData(mAdData);
    }

}
