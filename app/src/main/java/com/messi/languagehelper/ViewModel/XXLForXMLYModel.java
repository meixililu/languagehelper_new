package com.messi.languagehelper.ViewModel;

import android.app.Activity;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.bean.AlbumForAd;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

public class XXLForXMLYModel extends XXLRootModel{

    public AlbumForAd mADObject;

    public XXLForXMLYModel(Activity mContext) {
        super(mContext);
    }

    @Override
    public void addXFAD(NativeADDataRef nad) {
        mADObject = new AlbumForAd();
        mADObject.setmNativeADDataRef(nad);
        mADObject.setAd(true);
    }

    @Override
    public void addTXAD(NativeExpressADView mADView) {
        mADObject = new AlbumForAd();
        mADObject.setmTXADView(mADView);
    }

    @Override
    public void addBDAD(AdView adView) {
        int height = (int)(SystemUtil.SCREEN_WIDTH / 2);
        mADObject = new AlbumForAd();
        mADObject.setBdHeight(height);
        mADObject.setBdAdView(adView);
    }

    @Override
    public void addCSJAD(TTFeedAd ad) {
        mADObject = new AlbumForAd();
        mADObject.setCsjTTFeedAd(ad);
    }

    @Override
    public boolean addAD() {
        if (mADObject != null && avObjects != null && avObjects.size() > 0) {
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
