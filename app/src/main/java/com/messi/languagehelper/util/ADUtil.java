package com.messi.languagehelper.util;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.bean.NativeADDataRefForZYHY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ADUtil {
	public final static String GDT = "GDT";
	public final static String BD = "BD";
	public final static String CSJ = "CSJ";
	public final static String XF = "XF";
	public final static String XBKJ = "XBKJ";
	public static String[] adConfigs = null;

	public static String Advertiser_XF = "ad_xf";

	public static String Advertiser_TX = "ad_tx";

	public static String Advertiser = Advertiser_XF;

	public static boolean IsShowAD = true;

	public static List<NativeDataRef> localAd = new ArrayList<NativeDataRef>();

	public static final String KaiPingADId = "E170E50B2CBFE09CFE53F6D0A446560C";
	public static final String BannerADId = "A16A4713FB525DECF20126886F957534";
	public static final String ChaPingADId = "484C6E8F51357AFF26AEDB2441AB1847";
	public static final String QuanPingADId = "72C0E6B1042EA9F06A5A9B76235626CF";
	public static final String ListADId = "8FCA7E5106A3DB7DBC97B3B357E8A57F";
	public static final String XiuxianBanner = "8067D0538A5CC32E32550CCC816A23D2";
	public static final String XiuxianYSNRLAd = "912060B856C82B204149D8DF3DD65F6E";
	public static final String MRYJYSNRLAd = "ED72385915DAC4C681891487523EBE87";
	public static final String KaiPingYSAD = "F707B4276F3977F1F28BF110A8A20A74";

	public static final String NewsDetail = "345E72CB69CE01B8B0F55F855863F82A";
	public static final String SecondaryPage = "7AF25604C9C9826781DF4B7B04949B0F";

	public static final String XXLAD = "4A1AA609B79E04759A00E75CA6C2CAAE";
	public static final String VideoAD = "165D1FE5D5D872794849A23BDC430B8C";
	public static final String SanTuYiWen = "C69B39C7D8D20854DA5E8DF03E5049A0";

	public static final boolean IsShowAdImmediately = false;
	public static final int adCount = 1;
	public static final int adInterval = 4500;

	public static void initTXADID(Context mContext){
		try {
			ADUtil.IsShowAD = true;
			SharedPreferences sp = Setings.getSharedPreferences(mContext);
			String ad = sp.getString(KeyUtil.APP_Advertiser,"");
			if(ad.equals(KeyUtil.No_Ad)){
				ADUtil.IsShowAD = false;
			}else {
				String noAdChannel = sp.getString(KeyUtil.No_Ad_Channel,"huawei");
				String channel = Setings.getMetaData(mContext,"UMENG_CHANNEL");
				int versionCode = Setings.getVersion(mContext);
				int lastCode = sp.getInt(KeyUtil.VersionCode,-1);
				LogUtil.DefalutLog("lastCode:"+lastCode+"--noAdChannel:"+noAdChannel+"--channel:"+channel);
				if(versionCode >= lastCode){
					if(!TextUtils.isEmpty(noAdChannel) && !TextUtils.isEmpty(channel)){
						if(noAdChannel.equals(channel)){
							ADUtil.IsShowAD = false;
						}
					}
				}
			}
			LogUtil.DefalutLog("IsShowAD:"+ADUtil.IsShowAD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void initAdConfig(SharedPreferences sp){
		String configStr = sp.getString(KeyUtil.AdConfig, "CSJ#GDT#XF#BD#XBKJ");
		setAdConfig(configStr);
	}

	public static void setAdConfig(String config){
		if(!TextUtils.isEmpty(config) && config.contains("#")){
			adConfigs = null;
			adConfigs = config.split("#");
		}
	}

	public static String getAdProvider(int position){
		try {
			if(adConfigs != null && adConfigs.length > 0 && position < adConfigs.length){
				if(GDT.equals(adConfigs[position])){
					return GDT;
				}else if(BD.equals(adConfigs[position])){
					return BD;
				}else if(CSJ.equals(adConfigs[position])){
					return CSJ;
				}else if(XF.equals(adConfigs[position])){
					return GDT;
				}else if(XBKJ.equals(adConfigs[position])){
					return XBKJ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String randomAd(){
		if(new Random().nextInt(2) > 0){
			return ADUtil.XXLAD;
		}else {
			return ADUtil.SanTuYiWen;
		}
	}

	public static void toAdView(Context mContext, String type, String url){
		try {
			if(!TextUtils.isEmpty(type) && !type.equals("web")){
				Uri uri = null;
				if(type.equals("taobao")){
					if(url.contains("https")){
						uri = Uri.parse(url.replace("https",type));
					}else if(url.contains("http")){
						uri = Uri.parse(url.replace("http",type));
					}else {
						uri = Uri.parse(url);
					}
				}else if(type.equals("openapp.jdmobile")){
					uri = Uri.parse(url);
				}
				toAdActivity(mContext,uri);
			}else {
				toAdWebView(mContext,url,"什么值得买");
			}
		} catch (Exception e) {
			toAdWebView(mContext,url,"什么值得买");
			e.printStackTrace();
		}
	}

	public static void toAdActivity(Context mContext,Uri uri){
		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(uri);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toAdWebView(Context mContext,String url,String title){
		try {
			Intent intent = new Intent(mContext, WebViewActivity.class);
			intent.putExtra(KeyUtil.URL, url);
			intent.putExtra(KeyUtil.ActionbarTitle, title);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadAd(final Context context){
		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> e) throws Exception {
				getZYHYAd(context);
				e.onComplete();
			}
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<String>() {
					@Override
					public void onSubscribe(Disposable d) {}

					@Override
					public void onNext(String s) {}

					@Override
					public void onError(Throwable e) {}

					@Override
					public void onComplete() {
					}
				});
	}

	public static void getZYHYAd(Context context){
		try {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.AdList.AdList);
			query.whereEqualTo(AVOUtil.AdList.isValid, "1");
			if(context.getPackageName().equals(Setings.application_id_zyhy) ||
					context.getPackageName().equals(Setings.application_id_zyhy_google)){
				query.whereContains(AVOUtil.AdList.app, "zyhy");
			}else if(context.getPackageName().equals(Setings.application_id_yys) ||
					context.getPackageName().equals(Setings.application_id_yys_google)){
				query.whereContains(AVOUtil.AdList.app, "yys");
			}else if(context.getPackageName().equals(Setings.application_id_yyj) ||
					context.getPackageName().equals(Setings.application_id_yyj_google)){
				query.whereContains(AVOUtil.AdList.app, "yyj");
			}else if(context.getPackageName().equals(Setings.application_id_ywcd)){
				query.whereContains(AVOUtil.AdList.app, "ywcd");
			}else if(context.getPackageName().equals(Setings.application_id_xbky)){
				query.whereContains(AVOUtil.AdList.app, "xbky");
			}else if(context.getPackageName().equals(Setings.application_id_xbtl)){
				query.whereContains(AVOUtil.AdList.app, "xbtl");
			}else if(context.getPackageName().equals(Setings.application_id_qmzj)){
				query.whereContains(AVOUtil.AdList.app, "qmzj");
			}else if(context.getPackageName().equals(Setings.application_id_zrhy)){
				query.whereContains(AVOUtil.AdList.app, "zrhy");
			}else if(context.getPackageName().equals(Setings.application_id_zhhy)){
				query.whereContains(AVOUtil.AdList.app, "zhhy");
			}else{
				query.whereContains(AVOUtil.AdList.app, "zyhy");
			}
			query.addDescendingOrder(AVOUtil.AdList.createdAt);
			query.limit(20);
			List<AVObject> list = query.find();
			localAd.clear();
			if(list != null && !list.isEmpty()){
				for (AVObject object : list){
					localAd.add( NativeADDataRefForZYHY.build(context,object) );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static NativeDataRef getRandomAd(Context mActivity){
		if(localAd != null && !localAd.isEmpty()){
			NativeADDataRefForZYHY mNad = (NativeADDataRefForZYHY)localAd.get( new Random().nextInt(localAd.size()) );
			if(mNad != null){
				mNad.setContext(mActivity);
				return mNad;
			}
		}
		return null;
	}

	public static NativeDataRef getRandomAdList(Activity mActivity){
		if(localAd != null && !localAd.isEmpty()){
			NativeADDataRefForZYHY local = (NativeADDataRefForZYHY)localAd.get( new Random().nextInt(localAd.size()) );
			local.setContext(mActivity);
			return local;
		}else {
			return null;
		}
	}

	public static boolean isHasLocalAd(){
		if(localAd != null && !localAd.isEmpty()){
			return true;
		}else {
			return false;
		}
	}

	public static void showDownloadAppDialog(final Context mContext,final String url){
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert);
			builder.setTitle("");
			builder.setMessage("是要安装吗？");
			builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					new AppDownloadUtil(mContext,
							url,
							"",
							System.currentTimeMillis()+"",
							SDCardUtil.apkUpdatePath
					).DownloadFile();
				}
			});
			builder.setNegativeButton("不是", null);
			AlertDialog dialog = builder.create();
			dialog.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
