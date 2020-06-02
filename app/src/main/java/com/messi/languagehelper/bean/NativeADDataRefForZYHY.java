package com.messi.languagehelper.bean;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import cn.leancloud.AVObject;
import com.iflytek.voiceads.bean.AdAudio;
import com.iflytek.voiceads.bean.AdImage;
import com.iflytek.voiceads.bean.AudioMonitor;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luli on 14/12/2017.
 */

public class NativeADDataRefForZYHY extends NativeDataRef {

    private WeakReference<Context> context;
    private String title;
    private String sub_title;
    private String type;
    private String url;
    private String img;
    private String ad_type;
    private String adObjectId;
    private ArrayList<String> imgs;

    public NativeADDataRefForZYHY(Context context,
                                  String title,
                                  String sub_title,
                                  String type,
                                  String url,
                                  String img,
                                  ArrayList<String> imgs,
                                  String ad_type,
                                  String adObjectId){
        this.context = new WeakReference<>(context);
        this.title = title;
        this.sub_title = sub_title;
        this.type = type;
        this.url = url;
        this.img = img;
        this.imgs = imgs;
        this.ad_type = ad_type;
        this.adObjectId = adObjectId;
    }

    public static NativeADDataRefForZYHY build(Context context, AVObject adObject){
        return NativeADDataRefForZYHY.create()
                .setContext(context)
                .setTitile(adObject.getString(AVOUtil.AdList.title))
                .setSub_title(adObject.getString(AVOUtil.AdList.sub_title))
                .setType(adObject.getString(AVOUtil.AdList.type))
                .setUrl(adObject.getString(AVOUtil.AdList.url))
                .setImg(adObject.getString(AVOUtil.AdList.img))
                .setImgs((ArrayList<String>)adObject.get(AVOUtil.AdList.imgs))
                .setAd_Type(adObject.getString(AVOUtil.AdList.ad_type))
                .setAVObjectId(adObject.getObjectId())
                .build();
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    public String getRequestID() {
        return null;
    }

    @Override
    public int getTemplateID() {
        return 0;
    }

    @Override
    public int getActionType() {
        return 0;
    }

    @Override
    public String getAdSourceMark() {
        return "广告";
    }

    @Override
    public String getImgUrl() {
        return img;
    }

    @Override
    public AdImage getAdImg() {
        return null;
    }

    @Override
    public String getIconUrl() {
        return null;
    }

    @Override
    public AdImage getAdIcon() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDesc() {
        return sub_title;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public String getBrand() {
        return null;
    }

    @Override
    public String getCtatext() {
        return null;
    }

    @Override
    public boolean onClick(View view) {
        LogUtil.DefalutLog("NativeADDataRefForZYHY---onClick");
        if("app".equals(ad_type)){
            ADUtil.showDownloadAppDialog(getContext(),url);
        }else {
            ADUtil.toAdView(getContext(),type,url);
        }
        if(!TextUtils.isEmpty(adObjectId)){
            updateDownloadTime();
            return true;
        }
        return false;
    }

    @Override
    public boolean onExposure(View view) {
        LogUtil.DefalutLog("NativeADDataRefForZYHY---onExposure");
        return true;
    }

    @Override
    public boolean isExposured() {
        LogUtil.DefalutLog("NativeADDataRefForZYHY---isExposured");
        return true;
    }

    @Override
    public void showIntroduce() {
    }

    @Override
    public void downloadApp() {
    }

    private void updateDownloadTime(){
        AVObject post = AVObject.createWithoutData(AVOUtil.AdList.AdList, adObjectId);
        post.increment(AVOUtil.AdList.click_time);
        post.saveInBackground();
    }

    public static ADBuilder create(){
        return new ADBuilder();
    }

    @Override
    public List<String> getImgList() {
        return null;
    }

    @Override
    public List<AdImage> getAdImgList() {
        return null;
    }

    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public int getDownloads() {
        return 0;
    }

    @Override
    public String getRating() {
        return null;
    }

    @Override
    public String getAppVer() {
        return null;
    }

    @Override
    public double getAppSize() {
        return 0;
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public int getLikes() {
        return 0;
    }

    @Override
    public double getOriginalPrice() {
        return 0;
    }

    @Override
    public double getCurrentPrice() {
        return 0;
    }

    @Override
    public String getSponsored() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public ArrayList<String> getDisplayLabels() {
        return null;
    }

    @Override
    public AdAudio getAdAudio() {
        return null;
    }

    @Override
    public AudioMonitor getAudioMonitor() {
        return null;
    }


    public static class ADBuilder{
        private Context context;
        private String title;
        private String sub_title;
        private String type;
        private String url;
        private String img;
        private ArrayList<String> imgs;
        private String ad_type;
        private String adObjectId;

        public ADBuilder setContext(Context context){
            this.context = context;
            return this;
        }
        public ADBuilder setTitile(String title){
            this.title = title;
            return this;
        }
        public ADBuilder setSub_title(String sub_title){
            this.sub_title = sub_title;
            return this;
        }
        public ADBuilder setType(String type){
            this.type = type;
            return this;
        }
        public ADBuilder setUrl(String url){
            this.url = url;
            return this;
        }
        public ADBuilder setImg(String img){
            this.img = img;
            return this;
        }
        public ADBuilder setAd_Type(String ad_type){
            this.ad_type = ad_type;
            return this;
        }
        public ADBuilder setImgs(ArrayList<String> imgs){
            this.imgs = imgs;
            return this;
        }
        public ADBuilder setAVObjectId(String adObjectId){
            this.adObjectId = adObjectId;
            return this;
        }
        public NativeADDataRefForZYHY build(){
            return new NativeADDataRefForZYHY(context,title,sub_title,type,url,img,imgs,ad_type,adObjectId);
        }
    }

    private Context getContext(){
        return context.get();
    }

}
