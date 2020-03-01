package com.messi.languagehelper.util;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;

import com.messi.languagehelper.BaseApplication;
import com.messi.languagehelper.ImgShareActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.dialog.PopDialog;
import com.messi.languagehelper.dialog.PopDialog.PopViewItemOnclickListener;
import com.messi.languagehelper.service.PlayerService;

import java.io.File;
import java.util.HashMap;

public class Setings {

	public static int appVersion;

	public static String appChannel;

	public static final int RequestCode = 1;

	public static boolean IsShowNovel = false;

	public static String UmengAPPId = "551e3853fd98c5403800122c";

	public static String PrivacyUrl = "http://www.mzxbkj.com/pp/privacyzh.html";

	public static String TermsUrl = "http://www.mzxbkj.com/pp/servicezh.html";

	/**baidu translate api**/
	public static String baiduTranslateUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate";

	public static String BaiduLocationApi = "http://api.map.baidu.com/geocoder/v2/?output=json&pois=1&ak=vCV6TTGRTI5QrckdYSKHQIhq&location=";

	public static String HjTranslateUrl = "https://dict.hjenglish.com/v10/dict/translation";

	public static String YoudaoApi = "http://fanyi.youdao.com/translate?doctype=json&vendor=youdaoweb&screen=1080x1920&network=wifi";

	public static String IcibaTranslateNewUrl = "http://fy.iciba.com/ajax.php?a=fy";

	public static String TranAiyueyuUrl = "https://yue.micblo.com/api.php";

	public static String BaiduAccessToken = "https://aip.baidubce.com/oauth/2.0/token";

	public static String BaiduOCRUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

	public static String JuhaiApi = "http://dj.iciba.com/{0}-1-1-%01-0-0-0-0.html";

	public static String JukuApi = "http://www.jukuu.com/show-{0}-0.html";

	public static String EndicApi = "https://en.oxforddictionaries.com/definition/";

	public static String YDOcrQuestion = "https://aidemo.youdao.com/ocr_question";

	public static String XMLYApiRoot = "https://api.ximalaya.com";

	/**tencent api**/
	public static final String QQAPPID = "2109225639";
	public static final String QQAPPKEY = "5H2dYDcfBTdrPjkm";
	public static String QQImgOrcApi = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_imagetranslate";
	public static String QQTranAILabApi = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttrans";
	public static String QQTranFYJApi = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttranslate";

	/**youdao web translate for jsoup catch**/
	public static String YoudaoWeb = "http://dict.youdao.com/w/";
	
	public static String YoudaoWebEnd = "/#keyfrom=dict.index";
	
	public static String BingyingWeb = "http://cn.bing.com/dict/";

	public static String HjiangWeb = "https://dict.hjenglish.com/w/";

	/**showapi dictionary api**/
	public static String ShowApiDictionaryUrl = "http://route.showapi.com/32-9";
	
	/**jinshan daily sentence api**/
	public static String DailySentenceUrl = "http://open.iciba.com/dsapi/";
	
	public static final String CaiLingUrl = "http://iring.diyring.cc/friendv2/135430af88bc3328#main";

	public static final String CHDicBaiduApi = "http://hanyu.baidu.com/s?ptype=zici&wd=";

	public static final String WechatJXUrl = "http://api.tianapi.com/wxnew/?key=18f7f9dbd7dfcd8ab45efdcfbc33826d&rand=1&num=15&page=";

	public static final String ToutiaoNewsUrl = "http://v.juhe.cn/toutiao/index";

	/** BrainTwists **/

	public static final String TXBrainTwistsApi = "http://api.tianapi.com/txapi/naowan/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TXGodreplyApi = "http://api.tianapi.com/txapi/godreply/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TXEssayApi = "http://api.tianapi.com/txapi/ensentence/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TXYiZhanDaoDiApi = "http://api.tianapi.com/txapi/wenda/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String HistoryApi = "http://api.tianapi.com/txapi/pitlishi/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TongueTwisterApi = "http://api.tianapi.com/txapi/rkl/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String XHYApi = "http://api.tianapi.com/txapi/xiehou/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String CaizimiApi = "http://api.tianapi.com/txapi/zimi/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String WHYYApi = "http://api.tianapi.com/txapi/proverb/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String ConjectureApi = "http://api.tianapi.com/txapi/caichengyu/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String RiddleApi = "http://api.tianapi.com/txapi/riddle/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TXNewsApi = "http://api.tianapi.com/";

	public static final String TXNewsApiEnd = "?key=18f7f9dbd7dfcd8ab45efdcfbc33826d&num=15&page=";

	public static final String AiBrainUrl = "http://api.acobot.net/get?bid=866&key=vIjpiPkChlZ4om2F&uid=";

	public static final String AiTuringApi = "http://www.tuling123.com/openapi/api";

	public static final String AiTuringApiKey = "9cab5ca560c7403c84d035196b6f3500";

	/**toutiao video parse api**/
	public static final String TTParseApi = "https://service.iiilab.com/video/download";

	public static final String XMNovel = "https://reader.browser.duokan.com/v2/#tab=store&mz=&_miui_orientation=portrait&_miui_fullscreen=1&source=browser-mz";
	// uc search
	public static final String DVideo = "https://hot.browser.miui.com/v7/#page=short-video-list&cid=rec&_miui_=";

	public static final String UCSearch = "https://yz.m.sm.cn/s?q=%E7%A5%9E%E9%A9%AC%E6%96%B0%E9%97%BB%E6%A6%9C%E5%8D%95&from=wm845578";

	public static final String UCAI = "https://ai.sm.cn/#?query=";

	public static final String UCSearchUrl = "https://yz.m.sm.cn/s?from=wm845578&q={0}";

	public static final String NovelSearchUrl = "https://www.owllook.net/search?wd={0}";

	public static final String CaricatureSearchUrl = "https://nyaso.com/man/{0}.html";

	public static final String NovelRank = "https://www.owllook.net/md/qidian";

	public static final String NovelShort = "https://lnovel.cc/";

	public static final String NPR_Url = "https://www.npr.org/";
	public static final String AmericanLife_Url = "https://www.thisamericanlife.org/";

	//应用静态常量：
	public static boolean isMainFragmentNeedRefresh;
	public static boolean isDictionaryFragmentNeedRefresh;

	public static String XMLYAppAppKey = "a167180a30ec21d09c1c78ccacdf5d43";
	public static String XMLYAppSecret = "c779eeb1873325a487e2956a2077f2bc";
	public static final String TTParseClientSecretKey = "95077da2aa9ade5058a41cd5bf96d9f8";
	public static final String TTParseClientId = "dcc76daf232aee45";
	public static final String Email = "mzxbkj@163.com";
	public static final String YoudaoApiKey = "64148bac216470a0";
	public static final String BaiduORCAK = "rOpNTQojXriwz14ol8COWTok";
	public static final String BaiduORCSK = "dh99lxHNNUGILNV0UwLx0xBeDVgAh7vN";
	public static final String showapi_appid = "11619";
	public static final String showapi_secret = "f27574671ec14eb4a97faacb2eee3ef2";

	public static final int ca_psize = 12;
	public static final int page_size = 10;
	public static String baidu_appid = "";
	public static String baidu_secretkey = "";
//	public static final String client_id = "vCV6TTGRTI5QrckdYSKHQIhq";
	public static String yue = "yue";
	public static String zh = "zh";
	public static String from = "auto";	
	public static String to = "auto";	
	public static String q = "";	
	public static String role = "vimary";	
	public static final int RecordOffset = 50;

	public static final String application_id_zyhy = "com.messi.languagehelper";
	public static final String application_id_zyhy_google = "com.messi.languagehelper.google";
	public static final String application_id_yyj = "com.messi.learnenglish";
	public static final String application_id_yyj_google = "com.messi.learnenglish.google";
	public static final String application_id_yys = "com.messi.cantonese.study";
	public static final String application_id_yys_google = "com.messi.cantonese.study.google";
	public static final String application_id_ywcd = "com.messi.languagehelper.chinese";
	public static final String application_id_xbky = "com.messi.languagehelper.spoken";
	public static final String application_id_xbtl = "com.messi.languagehelper.listen";
	public static final String application_id_qmzj = "com.messi.languagehelper.qmzj";
	public static final String application_id_zrhy = "com.messi.languagehelper_ja";
	public static final String application_id_zhhy = "com.messi.languagehelper_korean";

	public static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.ACCESS_FINE_LOCATION
	};
	public static String[] PERMISSIONS_RECORD_AUDIO = {
			Manifest.permission.RECORD_AUDIO
	};
    public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
	public static PlayerService musicSrv;

    public static void MPlayerPause(){
    	if(musicSrv != null){
			musicSrv.pause();
		}
	}

	public static boolean MPlayerIsPlaying(){
    	if(musicSrv != null){
			return musicSrv.isPlaying();
		}
		return true;
	}

	public static void MPlayerRestart(){
    	if(musicSrv != null){
			musicSrv.restart();
		}
	}

	public static void MPlayerSeekTo(int position){
    	if(musicSrv != null){
			musicSrv.seekTo(position);
		}
	}

	public static boolean MPlayerIsSameMp3(Reading song){
    	if(musicSrv != null){
			return musicSrv.isSameMp3(song);
		}
		return false;
	}

    /**is today already do something
	 * @param mSharedPreferences
	 * @return
	 */
	public static boolean isTodayShow(SharedPreferences mSharedPreferences){
		String today = TimeUtil.getTimeDate(System.currentTimeMillis());
		LogUtil.DefalutLog("---isTodayShow---today:"+today);
		String lastTime = mSharedPreferences.getString(KeyUtil.IsLoadingShowToday, "");
		if(today.equals(lastTime)){
			return true;
		}else{
			saveSharedPreferences(mSharedPreferences,KeyUtil.IsLoadingShowToday,today);
			return false;
		}
	}
	
	/**time interval
	 * @param mSharedPreferences
	 * @param interval
	 * @return
	 */
	public static boolean isEnoughTime(SharedPreferences mSharedPreferences, long interval){
		long now = System.currentTimeMillis();
		long lastTime = mSharedPreferences.getLong(KeyUtil.IsEnoughIntervalTime, 0);
		saveSharedPreferences(mSharedPreferences,KeyUtil.IsEnoughIntervalTime,now);
		if(now - lastTime > interval){
			return true;
		}else{
			return false;
		}
	}
	
	/**获取配置文件类
	 * @param context
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context){
		if(context != null){
			return context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		}
		return null;
	}
	
	/**
	 * 保存配置信息
	 *
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, String value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, boolean value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 保存配置信息
	 *
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(String key, boolean value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = PlayUtil.getSP().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, long value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, int value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void contantUs(Context mContext){
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("message/rfc822");
			emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] { Email });
			mContext.startActivity(emailIntent);
		} catch (Exception e) {
			copy(mContext,Email);
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制按钮
	 */
	public static void copy(Context mContext,String dstString){
		try {
			// 得到剪贴板管理器
			ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setPrimaryClip(ClipData.newPlainText("data",dstString));
			ToastUtil.diaplayMesShort(mContext, mContext.getResources().getString(R.string.copy_success));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getVersion(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static String getIpAddress(Context mContext){
		//获取wifi服务  
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
//        if (!wifiManager.isWifiEnabled()) {
//        	wifiManager.setWifiEnabled(true);
//        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
        return intToIp(ipAddress);   
	}
	
	public static String intToIp(int i) {       
      return (i & 0xFF ) + "." +       
      ((i >> 8 ) & 0xFF) + "." +       
      ((i >> 16 ) & 0xFF) + "." +       
      ( i >> 24 & 0xFF) ;  
   	}

	public static void shareImg(Context mContext,String filePath){
		File file = new File(filePath);
		if (file != null && file.exists() && file.isFile()) {
			Uri uri = null;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				uri = FileProvider.getUriForFile(mContext, getProvider(mContext), file);
			} else {
				uri = Uri.fromFile(file);
			}
			if (uri != null) {
				try {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("image/png");
					intent.putExtra(Intent.EXTRA_STREAM, uri);
					intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.share));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(Intent.createChooser(intent,
							mContext.getResources().getString(R.string.share)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 分享
	 */
	public static void share(final Context context,final String dstString){
		String[] tempText = new String[3];
		tempText[0] = context.getResources().getString(R.string.share_dialog_text_1);
		tempText[1] = context.getResources().getString(R.string.share_dialog_text_2);
		tempText[2] = context.getResources().getString(R.string.copy);
		PopDialog mPopDialog = new PopDialog(context,tempText);
		mPopDialog.setCanceledOnTouchOutside(true);
		mPopDialog.setListener(new PopViewItemOnclickListener() {
			@Override
			public void onFirstClick(View v) {
				toShareTextActivity(context,dstString);
				AVAnalytics.onEvent(context, "share_text");
			}
			@Override
			public void onSecondClick(View v) {
				toShareImageActivity(context,dstString);
				AVAnalytics.onEvent(context, "share_img");
			}
			@Override
			public void onThirdClick(View v) {
				Setings.copy(context, dstString);
				AVAnalytics.onEvent(context, "share_copy");
			}
		});
		mPopDialog.show();
	}
	
	public static void toShareTextActivity(Context context,String dstString){
		Intent intent = new Intent(Intent.ACTION_SEND);    
		intent.setType("text/plain"); // 纯文本     
		intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.share));    
		intent.putExtra(Intent.EXTRA_TEXT, dstString);    
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share)));    
	}
	
	public static void toShareImageActivity(Context context,String dstString){
		Intent intent = new Intent(context, ImgShareActivity.class); 
		intent.putExtra(KeyUtil.ShareContentKey, dstString);
		context.startActivity(intent); 
	}

	public static String getDeviceID(Context context){
		if(context == null){
			context = BaseApplication.mInstance;
		}
		SharedPreferences sp = Setings.getSharedPreferences(context);
		String device_id = sp.getString(KeyUtil.DeviceId,"");
		if(TextUtils.isEmpty(device_id)){
			device_id = getTryToGetDeviceId(context);
			if(TextUtils.isEmpty(device_id)){
				device_id = StringUtils.getRandomString(32);
			}
		}
		Setings.saveSharedPreferences(sp,KeyUtil.DeviceId,device_id);
		LogUtil.DefalutLog("device_id:"+device_id);
		return device_id;
	}

	public static String getTryToGetDeviceId(Context context){
		String uniqueId = "";
		try {
			uniqueId = Settings.System.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
		} catch (Exception e) {
			uniqueId = "";
			e.printStackTrace();
		}
		return uniqueId;
	}

	public static String getProvider(Context appContext) {
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
			if(applicationInfo == null){
				return appContext.getPackageName() + ".provider";
			}
			return applicationInfo.metaData.getString("ProviderId");
		} catch (Exception e) {
			e.printStackTrace();
			return appContext.getPackageName() + ".provider";
		}
	}

	public static String getMetaData(Context appContext,String name) {
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
			if(applicationInfo == null){
				return "";
			}
			return applicationInfo.metaData.getString(name);
		} catch (Exception e) {
			e.printStackTrace();
			return appContext.getPackageName() + ".provider";
		}
	}

	public static String getVersionName(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "x.x";
		}
	}
}
