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
    public void addXFAD(NativeDataRef nad) {
        mADObject = new Reading();
        mADObject.setmNativeADDataRef(nad);
        mADObject.setAd(true);
    }

    @Override
    public void addTXAD(NativeExpressADView mADView) {
        mADObject = new Reading();
        mADObject.setmTXADView(mADView);
    }

    @Override
    public void addBDAD(AdView adView) {
        int height = (int)(SystemUtil.SCREEN_WIDTH / 2);
        mADObject = new Reading();
        mADObject.setBdHeight(height);
        mADObject.setBdAdView(adView);
    }

    @Override
    public void addCSJAD(TTFeedAd ad) {
        mADObject = new Reading();
        mADObject.setCsjTTFeedAd(ad);
    }
}
