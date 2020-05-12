package com.messi.languagehelper.repositories;

import android.content.Context;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

public class XXLAVObjectRepository extends ADXXLRepository<AVObject>{

    public XXLAVObjectRepository(Context context, List avObjects) {
        super(context,avObjects);
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

}
