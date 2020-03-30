package com.messi.languagehelper.repositories;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

public class XXLReadingRepository  extends ADXXLRepository<Reading>{


    public XXLReadingRepository(List avObjects) {
        super(avObjects);
    }

    @Override
    public Reading addXFAD(NativeDataRef nad) {
        Reading mADObject = new Reading();
        mADObject.setmNativeADDataRef(nad);
        mADObject.setAd(true);
        return mADObject;
    }

    @Override
    public Reading addTXAD(NativeExpressADView mADView) {
        Reading mADObject = new Reading();
        mADObject.setmTXADView(mADView);
        return mADObject;
    }

    @Override
    public Reading addBDAD(AdView adView) {
        int height = (int)(SystemUtil.SCREEN_WIDTH / 2);
        Reading mADObject = new Reading();
        mADObject.setBdHeight(height);
        mADObject.setBdAdView(adView);
        return mADObject;
    }

    @Override
    public Reading addCSJAD(TTFeedAd ad) {
        Reading mADObject = new Reading();
        mADObject.setCsjTTFeedAd(ad);
        return mADObject;
    }

}
