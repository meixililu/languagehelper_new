package com.messi.languagehelper.ViewModel;

import android.content.Context;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

public class XXLModel extends XXLRootModel{

    public Reading mADObject;

    public XXLModel(Context mContext) {
        super(mContext);
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
