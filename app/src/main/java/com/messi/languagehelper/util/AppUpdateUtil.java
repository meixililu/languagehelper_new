package com.messi.languagehelper.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.WebFilter;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager;

import java.util.List;

public class AppUpdateUtil {

    public static void runCheckUpdateTask(final Activity mActivity) {
        checkUpdate(mActivity);
        initXMLY(mActivity);
    }

    public static void initXMLY(Activity mActivity){
        XmPlayerManager.getInstance(mActivity).init();
        XmPlayerManager.getInstance(mActivity).setCommonBusinessHandle(XmDownloadManager.getInstance());

        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        SystemUtil.SCREEN_WIDTH = dm.widthPixels;
        SystemUtil.SCREEN_HEIGHT = dm.heightPixels;
        SystemUtil.screen = SystemUtil.SCREEN_WIDTH + "x" + SystemUtil.SCREEN_HEIGHT;
    }

    public static void getWebFilter(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.AdFilter.AdFilter);
                query.whereContains(AVOUtil.AdFilter.category, "ca_novel");
                List<AVObject> list = null;
                try {
                    list = query.find();
                    if(list != null){
                        List<WebFilter> beans = DataUtil.toWebFilter(list);
                        BoxHelper.updateWebFilter(beans);
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void checkUpdate(final Activity mActivity) {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.UpdateInfo.UpdateInfo);
        if(mActivity.getPackageName().equals(Setings.application_id_zyhy)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zyhy");
        }else if(mActivity.getPackageName().equals(Setings.application_id_zyhy_google)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zyhy_google");
        }else if(mActivity.getPackageName().equals(Setings.application_id_yys)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yys");
        }else if(mActivity.getPackageName().equals(Setings.application_id_yys_google)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yys_google");
        }else if(mActivity.getPackageName().equals(Setings.application_id_yyj)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yyj");
        }else if(mActivity.getPackageName().equals(Setings.application_id_yyj_google)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "yyj_google");
        }else if(mActivity.getPackageName().equals(Setings.application_id_ywcd)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "ywcd");
        }else if(mActivity.getPackageName().equals(Setings.application_id_xbky)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "xbky");
        }else if(mActivity.getPackageName().equals(Setings.application_id_xbtl)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "xbtl");
        }else if(mActivity.getPackageName().equals(Setings.application_id_qmzj)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "qmzj");
        }else if(mActivity.getPackageName().equals(Setings.application_id_zrhy)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zrhy");
        }else if(mActivity.getPackageName().equals(Setings.application_id_zhhy)){
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "zhhy");
        }else{
            query.whereEqualTo(AVOUtil.UpdateInfo.AppCode, "noupdate");
        }
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (avObjects != null && avObjects.size() > 0) {
                    final AVObject mAVObject = avObjects.get(0);
                    saveSetting(mActivity,mAVObject);
                }
            }
        });
    }

    public static void saveSetting(Activity mActivity,AVObject mAVObject){
        SharedPreferences mSharedPreferences = Setings.getSharedPreferences(mActivity);
        LogUtil.DefalutLog(mAVObject.getString(AVOUtil.UpdateInfo.AppName));
        String app_advertiser = mAVObject.getString(AVOUtil.UpdateInfo.ad_type);
        String wyyx_url = mAVObject.getString(AVOUtil.UpdateInfo.wyyx_url);
        String uctt_url = mAVObject.getString(AVOUtil.UpdateInfo.uctt_url);
        String ucsearch_url = mAVObject.getString(AVOUtil.UpdateInfo.ucsearch_url);
        String ad_ids = mAVObject.getString(AVOUtil.UpdateInfo.ad_ids);
        String no_ad_channel = mAVObject.getString(AVOUtil.UpdateInfo.no_ad_channel);
        String trankey = mAVObject.getString(AVOUtil.UpdateInfo.trankey);
        String adConf = mAVObject.getString(AVOUtil.UpdateInfo.adConf);
        ADUtil.setAdConfig(adConf);
        initTranBdIdKey(trankey);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.APP_Advertiser,app_advertiser);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.Lei_DVideo,uctt_url);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.Lei_Novel,wyyx_url);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.Lei_UCSearch,ucsearch_url);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.Ad_Ids,ad_ids);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.No_Ad_Channel,no_ad_channel);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.AdConfig,adConf);
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.VersionCode,
                mAVObject.getInt(AVOUtil.UpdateInfo.VersionCode));
        Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.UpdateBean, mAVObject.toString());
    }

    public static void initTranBdIdKey(String idkey){
        if (!TextUtils.isEmpty(idkey)) {
            String[] keys = idkey.split("#");
            if(keys != null && keys.length > 1){
                Setings.baidu_appid = keys[0];
                Setings.baidu_secretkey = keys[1];
                LogUtil.DefalutLog("baidu_appidkey:"+Setings.baidu_appid+"-"+Setings.baidu_secretkey);
            }
        }
    }

    public static void isNeedUpdate(final Activity mActivity){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showUpdateDialog(mActivity);
            }
        }, 4000);
    }

    public static void showUpdateDialog(final Activity mActivity) {
        try {
            SharedPreferences sp = Setings.getSharedPreferences(mActivity);
            final AVObject mAVObject = AVObject.parseAVObject(sp.getString(KeyUtil.UpdateBean,""));
            String isValid = mAVObject.getString(AVOUtil.UpdateInfo.IsValid);
            if(!TextUtils.isEmpty(isValid) && isValid.equals("3")){
                int newVersionCode = mAVObject.getInt(AVOUtil.UpdateInfo.VersionCode);
                int oldVersionCode = Setings.getVersion(mActivity);
                if (newVersionCode > oldVersionCode) {
                    String updateInfo = mAVObject.getString(AVOUtil.UpdateInfo.AppUpdateInfo);
                    String apkUrl = mAVObject.getString(AVOUtil.UpdateInfo.APPUrl);
                    final String downloadUrl = apkUrl;
                    LogUtil.DefalutLog("apkUrl:" + apkUrl);

                    View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_update_info,null);
                    TextView updage_info = (TextView) view.findViewById(R.id.updage_info);
                    ImageView cancel_btn = (ImageView) view.findViewById(R.id.cancel_btn);
                    TextView update_btn = (TextView) view.findViewById(R.id.update_btn);
                    final AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
                    dialog.setView(view);
                    dialog.setCancelable(false);
                    updage_info.setText(updateInfo);
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    update_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            new AppDownloadUtil(mActivity,
                                    downloadUrl,
                                    mAVObject.getString(AVOUtil.UpdateInfo.AppName),
                                    mAVObject.getObjectId(),
                                    SDCardUtil.apkUpdatePath
                            ).DownloadFile();
                        }
                    });
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
