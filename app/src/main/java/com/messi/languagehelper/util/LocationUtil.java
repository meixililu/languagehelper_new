package com.messi.languagehelper.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.google.gson.Gson;
import com.messi.languagehelper.bean.BdLocationRoot;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.LocationResultListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Created by luli on 02/12/2017.
 */

public class LocationUtil {

    public Activity context;

    private LocationResultListener listener;

    public LocationUtil(Activity mContext) {
        this.context = mContext;
        requestLocation();
    }

    private void requestLocation() {
        LogUtil.DefalutLog("getlocation");
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //低精度
        criteria.setAltitudeRequired(false);    //不要求海拔
        criteria.setBearingRequired(false); //不要求方位
        criteria.setCostAllowed(false); //不允许有话费
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtil.DefalutLog("no permission");
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LogUtil.DefalutLog("Latitude"+location.getLatitude()+"---Longitude:"+location.getLongitude());
        changeLocation(location);
    }

    private void changeLocation(final Location location){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String url = Setings.BaiduLocationApi + location.getLatitude()+","+location.getLongitude();
                Response mResponse = LanguagehelperHttpClient.get(url);
                if (mResponse.isSuccessful()) {
                    BdLocationRoot mroot = new Gson().fromJson(mResponse.body().string(),BdLocationRoot.class);
                    if(mroot != null && mroot.getStatus() == 0){
                        e.onNext(mroot.getResult().getAddressComponent().getAdcode());
                        saveLocation(mroot,location);
                    }
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        if(listener != null){
                            listener.OnLocationSuccess(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public LocationResultListener getListener() {
        return listener;
    }

    public void setListener(LocationResultListener listener) {
        this.listener = listener;
    }

    private void saveLocation(final BdLocationRoot mroot,final Location location){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String uuid = Setings.getUUID(context);
                    AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.Location.Location);
                    query.whereEqualTo(AVOUtil.Location.uuid, uuid);
                    int count = query.count();
                    if(count == 0){
                        AVObject testObject = new AVObject(AVOUtil.Location.Location);
                        testObject.put(AVOUtil.Location.uuid, Setings.getUUID(context));
                        testObject.put(AVOUtil.Location.network, SystemUtil.getNetworkType(context));
                        testObject.put(AVOUtil.Location.screen, SystemUtil.screen);
                        testObject.put(AVOUtil.Location.address, mroot.getResult().getFormatted_address()+
                        ","+mroot.getResult().getSematic_description());
                        testObject.put(AVOUtil.Location.city, mroot.getResult().getAddressComponent().getCity());
                        testObject.put(AVOUtil.Location.longitude, location.getLongitude());
                        testObject.put(AVOUtil.Location.latitude, location.getLatitude());
                        testObject.put(AVOUtil.Location.province, mroot.getResult().getAddressComponent().getProvince());
                        testObject.put(AVOUtil.Location.country, mroot.getResult().getAddressComponent().getCountry());
                        testObject.put(AVOUtil.Location.adcode, mroot.getResult().getAddressComponent().getAdcode());
                        testObject.put(AVOUtil.Location.district, mroot.getResult().getAddressComponent().getDistrict());
                        testObject.put(AVOUtil.Location.sdk, SystemUtil.getSDK());
                        testObject.put(AVOUtil.Location.version, SystemUtil.getSystemVersion());
                        testObject.put(AVOUtil.Location.model, SystemUtil.getSystemModel());
                        testObject.put(AVOUtil.Location.brand, SystemUtil.getDeviceBrand());
                        testObject.saveInBackground();
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
