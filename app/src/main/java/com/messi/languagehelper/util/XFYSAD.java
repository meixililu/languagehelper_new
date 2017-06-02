package com.messi.languagehelper.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.adapter.ViewPagerAdapter;
import com.messi.languagehelper.views.ProportionalImageView;
import com.messi.languagehelper.views.WrapContentHeightViewPager;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class XFYSAD {
	
	private Context mContext;
	private View parentView;
	private IFLYNativeAd nativeAd;
	private NativeADDataRef mNativeADDataRef;
	private SimpleDraweeView ad_img;
	private LayoutInflater mInflater;
	private String adId;
	private int retryTime;
	private long lastLoadAdTime;
	private boolean isDirectExPosure = true;
	private boolean exposure;

	public XFYSAD(Context mContext,View parentView,String adId){
		this.mContext = mContext;
		this.parentView = parentView;
		this.adId = adId;
		mInflater = LayoutInflater.from(mContext);
		ad_img = (SimpleDraweeView)parentView.findViewById(R.id.ad_img);
		parentView.setVisibility(View.GONE);
	}

	public XFYSAD(Context mContext,String adId){
		this.mContext = mContext;
		this.adId = adId;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setParentView(View parentView){
		if(System.currentTimeMillis() - lastLoadAdTime > 3000){
			this.parentView = parentView;
			ad_img = (SimpleDraweeView)parentView.findViewById(R.id.ad_img);
			parentView.setVisibility(View.GONE);
		}
	}
	
	public void isNeedReload(){
		if(System.currentTimeMillis() - lastLoadAdTime > 1000*45){
			lastLoadAdTime = System.currentTimeMillis();
			showAD();
		}
	}
	
	public void showAD(){
		if(ADUtil.isShowAd(mContext)){
			if(System.currentTimeMillis() - lastLoadAdTime > 3000){
				loadData();
			}
		}else{
			parentView.setVisibility(View.GONE);
		}
	}
	
	private void loadData(){
		LogUtil.DefalutLog("---load ad Data---");
		lastLoadAdTime = System.currentTimeMillis();
		nativeAd = new IFLYNativeAd(mContext, adId, new IFLYNativeListener() {
			@Override
			public void onConfirm() {
			}
			@Override
			public void onCancel() {
			}
			@Override
			public void onAdFailed(AdError arg0) {
				parentView.setVisibility(View.GONE);
				LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
				if(retryTime < 1){
					retryTime ++;
					showAD();
				}
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> arg0) {
					try {
						if(arg0 != null && arg0.size() > 0){
							LogUtil.DefalutLog("---onADLoaded---");
							mNativeADDataRef = arg0.get(0);
							setAdData();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd(ADUtil.adCount);
	}
	
	private void setAdData() throws Exception{
		parentView.setVisibility(View.VISIBLE);
		ad_img.setImageURI(mNativeADDataRef.getImage());
		if(isDirectExPosure){
			exposure = mNativeADDataRef.onExposured(parentView);
			LogUtil.DefalutLog("XFYSAD-setAdData-exposure:"+exposure);
		}
		parentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean click = mNativeADDataRef.onClicked(view);
				LogUtil.DefalutLog("XFYSAD-onClick:"+click);
			}
		});
	}

	public void ExposureAD(){
		if(!exposure){
			exposure = mNativeADDataRef.onExposured(parentView);
		}
		LogUtil.DefalutLog("XFYSAD-ExposureAD-exposure:"+exposure);
	}

	public void setDirectExPosure(boolean directExPosure) {
		isDirectExPosure = directExPosure;
	}

	/**
	 * 判断view是否在屏幕范围内（是否曝光）。
	 * @param context
	 * @param view
	 * @return true view矩形在屏幕矩形内或相交
	 */
	public static boolean isInScreen(Context context,View view) {
		//获取屏幕宽高（px）以及矩形
		Rect screenRect = getScreenRect(context);
		//获取view矩形
		Rect viewRect=getViewRect(view);
		int viewHight= (int) ((viewRect.bottom-viewRect.top)*0.3);
		screenRect=new Rect(screenRect.left, screenRect.top+viewHight, screenRect.right, screenRect.bottom-viewHight);
		//判断是否相交或包含
		boolean intersects=Rect.intersects(screenRect, viewRect);
		boolean contains=screenRect.contains(viewRect);
		return intersects||contains;

	}

	///Ad_Android_SDK_v1/src/com/iflytek/voiceads/utils/AppState.java
	/**
	 * 获取屏幕矩形
	 * @param context
	 * @return screenRect
	 */
	public static Rect getScreenRect(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display=windowManager.getDefaultDisplay();
		int screenW=display.getWidth();
		int screenH=display.getHeight();
		Rect screenRect=new Rect(0,0,screenW,screenH);
		return screenRect;
	}

///Ad_Android_SDK_v1/src/com/iflytek/voiceads/utils/AppState.java
	/**
	 * 获取view矩形
	 * @param view
	 * @return viewRect
	 */
	public static Rect getViewRect(View view){
		int[] location=new int[2];
		view.getLocationOnScreen(location);
		Rect viewRect=new Rect(location[0],location[1],location[0]+view.getWidth(),location[1]+view.getHeight());
		return viewRect;
	}

}
