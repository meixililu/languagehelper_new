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
        ADUtil.initTXADID(mActivity);
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
        SharedPreferences sp = Setings.getSharedPreferences(mActivity);
        LogUtil.DefalutLog(mAVObject.getString(AVOUtil.UpdateInfo.AppName));
        String app_advertiser = mAVObject.getString(AVOUtil.UpdateInfo.ad_type);
        String wyyx_url = mAVObject.getString(AVOUtil.UpdateInfo.wyyx_url);
        String uctt_url = mAVObject.getString(AVOUtil.UpdateInfo.uctt_url);
        String yuey_url = mAVObject.getString(AVOUtil.UpdateInfo.yuey_url);
        String ucsearch_url = mAVObject.getString(AVOUtil.UpdateInfo.ucsearch_url);
        String tran_order = mAVObject.getString(AVOUtil.UpdateInfo.tran_order);
        String ad_ids = mAVObject.getString(AVOUtil.UpdateInfo.ad_ids);
        String ad_csj = mAVObject.getString(AVOUtil.UpdateInfo.ad_csj);
        String ad_bd = mAVObject.getString(AVOUtil.UpdateInfo.ad_bd);
        String no_ad_channel = mAVObject.getString(AVOUtil.UpdateInfo.no_ad_channel);
        String trankey = mAVObject.getString(AVOUtil.UpdateInfo.trankey);
        String interceptUrls = mAVObject.getString(AVOUtil.UpdateInfo.interceptUrls);
        String adConf = mAVObject.getString(AVOUtil.UpdateInfo.adConf);
        String Caricature_channel = mAVObject.getString(AVOUtil.UpdateInfo.Caricature_channel);
        String domain = mAVObject.getString(AVOUtil.UpdateInfo.domain);
        int Caricature_version = mAVObject.getInt(AVOUtil.UpdateInfo.Caricature_version);
        String HjCookie = mAVObject.getString(AVOUtil.UpdateInfo.HjCookie);
        TranslateHelper.setHjCookie(HjCookie);
        ADUtil.setAdConfig(adConf);
        TXADUtil.setADData(ad_ids);
        CSJADUtil.setADData(ad_csj);
        BDADUtil.setADData(ad_bd);
        Setings.saveSharedPreferences(sp,KeyUtil.TranOrder,tran_order);
        Setings.saveSharedPreferences(sp,KeyUtil.InterceptUrls,interceptUrls);
        Setings.saveSharedPreferences(sp,KeyUtil.TranBDKey,trankey);
        Setings.saveSharedPreferences(sp,KeyUtil.APP_Advertiser,app_advertiser);
        Setings.saveSharedPreferences(sp,KeyUtil.Lei_DVideo,uctt_url);
        Setings.saveSharedPreferences(sp,KeyUtil.Lei_Novel,wyyx_url);
        Setings.saveSharedPreferences(sp,KeyUtil.YueYUrl,yuey_url);
        Setings.saveSharedPreferences(sp,KeyUtil.Lei_UCSearch,ucsearch_url);
        Setings.saveSharedPreferences(sp,KeyUtil.Ad_Ids,ad_ids);
        Setings.saveSharedPreferences(sp,KeyUtil.Ad_Csj,ad_csj);
        Setings.saveSharedPreferences(sp,KeyUtil.Ad_Bd,ad_bd);
        Setings.saveSharedPreferences(sp,KeyUtil.LeanCloudIPAddress,domain);
        Setings.saveSharedPreferences(sp,KeyUtil.No_Ad_Channel,no_ad_channel);
        Setings.saveSharedPreferences(sp,KeyUtil.AdConfig,adConf);
        Setings.saveSharedPreferences(sp,KeyUtil.VersionCode,
                mAVObject.getInt(AVOUtil.UpdateInfo.VersionCode));
        Setings.saveSharedPreferences(sp,KeyUtil.Caricature_version, Caricature_version);
        Setings.saveSharedPreferences(sp,KeyUtil.Caricature_channel, Caricature_channel);
        Setings.saveSharedPreferences(sp,KeyUtil.UpdateBean, mAVObject.toString());
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
//                    LogUtil.DefalutLog("apkUrl:" + apkUrl);

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
