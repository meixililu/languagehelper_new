package com.messi.languagehelper.util;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.config.AdError;
import com.iflytek.voiceads.config.AdKeys;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.iflytek.voiceads.listener.IFLYNativeListener;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewModel.XXLRootModel;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class XFYSAD {

	private WeakReference<Context> mContext;
	private View parentView;
	private FrameLayout ad_layout;
	private TextView ad_sign;
	private IFLYNativeAd nativeAd;
	private NativeDataRef mNativeADDataRef;
	private NativeExpressADView mTXADView;
	private SimpleDraweeView ad_img;
	private LayoutInflater mInflater;
	private String adId;
	public long lastLoadAdTime;
	private boolean isDirectExPosure = true;
	private boolean exposure;
	private HeaderFooterRecyclerViewAdapter mAdapter;

	public int counter;
	public String BDADID = BDADUtil.BD_BANNer;
    private String CSJADID = CSJADUtil.CSJ_XXLSP;
	private AdView adView;
	private TTAdNative mTTAdNative;

	public XFYSAD(Context mContext, View parentView, String adId){
		this.mContext = new WeakReference<>(mContext);
		this.parentView = parentView;
		this.adId = adId;
		mInflater = LayoutInflater.from(mContext);
		ad_img = (SimpleDraweeView)parentView.findViewById(R.id.ad_img);
		ad_layout = (FrameLayout)parentView.findViewById(R.id.ad_layout);
		ad_sign = (TextView)parentView.findViewById(R.id.ad_sign);
		ad_sign.setVisibility(View.GONE);
	}

	public XFYSAD(Context mContext,String adId){
		this.mContext = new WeakReference<>(mContext);
		this.adId = adId;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setParentView(View parentView){
		LogUtil.DefalutLog("XFYSAD---setParentView");
		if(System.currentTimeMillis() - lastLoadAdTime > 1000*30){
			this.parentView = parentView;
			ad_img = (SimpleDraweeView)parentView.findViewById(R.id.ad_img);
			ad_layout = (FrameLayout)parentView.findViewById(R.id.ad_layout);
			ad_sign = (TextView)parentView.findViewById(R.id.ad_sign);
			ad_sign.setVisibility(View.GONE);
		}
	}
	
	public void isNeedReload(){
		if(System.currentTimeMillis() - lastLoadAdTime > 1000*45){
			showAd();
		}
	}

	public void showAd(){
		if(ADUtil.IsShowAD){
			if(System.currentTimeMillis() - lastLoadAdTime > 1000*30){
				parentView.setVisibility(View.VISIBLE);
				lastLoadAdTime = System.currentTimeMillis();
				getXXLAd();
			}
		}else {
			hideADView();
		}
	}
	
	public void getXXLAd(){
		try {
			String currentAD = ADUtil.getAdProvider(counter);
			if(!TextUtils.isEmpty(currentAD)){
				LogUtil.DefalutLog("------ad-------"+currentAD);
				if(ADUtil.GDT.equals(currentAD)){
					loadTXAD();
				}else if(ADUtil.BD.equals(currentAD)){
					loadTXAD();
				}else if(ADUtil.CSJ.equals(currentAD)){
					loadCSJAD();
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

	public void onLoadAdFaile(){
		counter++;
		getXXLAd();
	}

	private void loadTXAD(){
		LogUtil.DefalutLog("---load TXAD Data---");
		TXADUtil.showBigImg(getContext(), new NativeExpressAD.NativeExpressADListener() {
			@Override
			public void onNoAD(com.qq.e.comm.util.AdError adError) {
				onLoadAdFaile();
			}

			@Override
			public void onADLoaded(List<NativeExpressADView> list) {
				if(mTXADView != null){
					mTXADView.destroy();
				}
				initFeiXFAD();
				mTXADView = list.get(0);
				ad_layout.addView(mTXADView);
				mTXADView.render();
			}

			@Override
			public void onRenderFail(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onRenderFail");
			}
			@Override
			public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onRenderSuccess");
			}
			@Override
			public void onADExposure(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADExposure");
			}
			@Override
			public void onADClicked(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADClicked");
			}
			@Override
			public void onADClosed(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADClosed");
				if(ad_layout != null){
					ad_layout.removeAllViews();
					ad_layout.setVisibility(View.GONE);
				}
			}
			@Override
			public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADLeftApplication");
			}
			@Override
			public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADOpenOverlay");
			}
			@Override
			public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
				LogUtil.DefalutLog("onADCloseOverlay");
			}
		});
	}
	
	private void loadXFAD(){
		LogUtil.DefalutLog("---load XFAD Data---");
		nativeAd = new IFLYNativeAd(getContext(), adId, new IFLYNativeListener() {
			@Override
			public void onConfirm() {
			}
			@Override
			public void onCancel() {
			}
			@Override
			public void onAdFailed(AdError arg0) {
				onLoadAdFaile();
			}
			@Override
			public void onAdLoaded(NativeDataRef nativeDataRef) {
				if(nativeDataRef != null){
					mNativeADDataRef = nativeDataRef;
					setAdData(mNativeADDataRef);
				}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd();
	}

	public void loadXBKJ() {
		if (ADUtil.isHasLocalAd()) {
			setAdData(ADUtil.getRandomAd(getContext()));
		}
	}
	
	private void setAdData(NativeDataRef mNativeADDataRef){
		try {
			if(mNativeADDataRef != null){
				ad_sign.setVisibility(View.VISIBLE);
				parentView.setVisibility(View.VISIBLE);
				ad_layout.setVisibility(View.GONE);
				ad_img.setVisibility(View.VISIBLE);
				DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
						.setAutoPlayAnimations(true)
						.setUri(Uri.parse(mNativeADDataRef.getImgUrl()))
						.build();
				ad_img.setController(mDraweeController);
				if(isDirectExPosure){
					exposure = mNativeADDataRef.onExposure(parentView);
					LogUtil.DefalutLog("XFYSAD-setAdData-exposure:"+exposure);
				}
				parentView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						boolean click = mNativeADDataRef.onClick(view);
						LogUtil.DefalutLog("XFYSAD-onClick:"+click);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadBDAD(){
		initFeiXFAD();
		adView = new AdView(getContext(),BDADID);
		adView.setListener(new AdViewListener(){
			@Override
			public void onAdReady(AdView adView) {
				LogUtil.DefalutLog("BDAD-onAdReady");
			}
			@Override
			public void onAdShow(JSONObject jsonObject) {
				LogUtil.DefalutLog("BDAD-onAdShow");
			}
			@Override
			public void onAdClick(JSONObject jsonObject) {
				LogUtil.DefalutLog("BDAD-onAdClick");
			}
			@Override
			public void onAdFailed(String s) {
				LogUtil.DefalutLog("BDAD-onAdFailed:"+s);
				onLoadAdFaile();
			}
			@Override
			public void onAdSwitch() {
				LogUtil.DefalutLog("BDAD-onAdSwitch");
			}
			@Override
			public void onAdClose(JSONObject jsonObject) {
				LogUtil.DefalutLog("BDAD-onAdClose");
			}
		});
		int height = (int)(SystemUtil.SCREEN_WIDTH / 2);
		LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(SystemUtil.SCREEN_WIDTH, height);
		ad_layout.addView(adView,rllp);
	}

	public void initFeiXFAD(){
		ad_sign.setVisibility(View.GONE);
		ad_img.setVisibility(View.GONE);
		ad_layout.setVisibility(View.VISIBLE);
		ad_layout.removeAllViews();
	}

    public void loadCSJAD(){
        LogUtil.DefalutLog("loadCSJAD");
        TTAdNative mTTAdNative = CSJADUtil.get().createAdNative(getContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJADID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(690, 388)
                .build();
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("loadCSJAD-onError:"+s);
                onLoadAdFaile();
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    onLoadAdFaile();
                    return;
                }
                TTFeedAd mTTFeedAd = ads.get(0);
                initFeiXFAD();
                XXLRootModel.setCSJDView(getContext(),mTTFeedAd,ad_layout);
            }
        });
    }

	public void hideADView(){
		try {
			if(parentView != null){
				parentView.setVisibility(View.GONE);
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(mAdapter != null){
						mAdapter.hideHeader();
					}
				}
			},100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ExposureAD(){
		if(!exposure){
			exposure = mNativeADDataRef.onExposure(parentView);
			LogUtil.DefalutLog("XFYSAD-ExposureAD-exposure:"+exposure);
		}
	}

	public void setDirectExPosure(boolean directExPosure) {
		isDirectExPosure = directExPosure;
	}

	public void setAdapter(HeaderFooterRecyclerViewAdapter adapter){
		this.mAdapter = adapter;
	}

	public Context getContext() {
		return mContext.get();
	}

	public void onDestroy(){
		if(parentView != null){
			((ViewGroup)parentView).removeAllViews();
			parentView = null;
		}
		if(ad_layout != null){
			ad_layout.removeAllViews();
			ad_layout = null;
		}
		if(mTXADView != null){
			mTXADView.destroy();
			mTXADView = null;
		}
		if(mNativeADDataRef != null){
			mNativeADDataRef = null;
		}
		if(mAdapter != null){
			mAdapter = null;
		}
		if(mTTAdNative != null){
			mTTAdNative = null;
		}
		if(adView != null){
			adView.destroy();
			adView = null;
		}
	}
}
