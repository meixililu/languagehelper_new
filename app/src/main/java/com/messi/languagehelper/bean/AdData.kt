package com.messi.languagehelper.bean

import com.baidu.mobads.AdView
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.iflytek.voiceads.conn.NativeDataRef
import com.qq.e.ads.nativ.NativeExpressADView

class AdData (
        var isAdShow: Boolean = false,
        var mNativeADDataRef: NativeDataRef? = null,
        var mTXADView: NativeExpressADView? = null,
        var csjTTFeedAd: TTFeedAd? = null,
        var bdAdView: AdView? = null,
        var bdHeight: Int = 0
)