package com.messi.languagehelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Connection;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ToastUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OfflineDicDownloadActivity extends BaseActivity {

    @BindView(R.id.dic_minimalist_img)
    ImageView dicMinimalistImg;
    @BindView(R.id.dic_minimalist)
    FrameLayout dicMinimalist;
    @BindView(R.id.dic_standard_img)
    ImageView dicStandardImg;
    @BindView(R.id.dic_standard)
    FrameLayout dicStandard;

    private final static String DicFileName = "localdict.datx";
    private final static String minimalistUrl = "http://ac-3fg5ql3r.clouddn.com/bdf289ac24c248becf42.datx";
    private final static String standarUrl = "http://ac-3fg5ql3r.clouddn.com/acf9b97ea30ef50f2501.datx";
    //0 no dic, 1 minimalist, 2 standard
    private int status = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_dic_download_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        getSupportActionBar().setTitle(getResources().getString(R.string.title_offline_dic));
        setImage();
    }

    private void setImage(){
        try {
            String mOfflineDicPath = SDCardUtil.getDownloadPath(SDCardUtil.OfflineDicPath);
            long size = SDCardUtil.getFileSize(mOfflineDicPath+DicFileName);
            LogUtil.DefalutLog("size:"+size);
            if(size > 0){
                if(size > (6*1048576)){
                    status = 2;
                    dicStandardImg.setImageResource(R.drawable.ic_done);
                }else {
                    status = 1;
                    dicMinimalistImg.setImageResource(R.drawable.ic_done);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.dic_minimalist, R.id.dic_standard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dic_minimalist:
                downloadMinimalistDic(minimalistUrl);
                break;
            case R.id.dic_standard:
                downloadStandardDic(standarUrl);
                break;
        }
    }

    private void downloadMinimalistDic(String url){
        if(status < 1){
            downloadFile(url);
        }else {
            ToastUtil.diaplayMesShort(this,"亲已经下载了离线词典");
        }
    }

    private void downloadStandardDic(String url){
        if(status < 2){
            downloadFile(url);
        }else {
            ToastUtil.diaplayMesShort(this,"亲已经下载了离线词典");
        }
    }

    private void downloadFile(final String url){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("showProgressbar");
                DownLoadUtil.downloadFile(OfflineDicDownloadActivity.this, url, SDCardUtil.OfflineDicPath, DicFileName);
                e.onNext("hideProgressbar");
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
                        if (s.equals("showProgressbar")) {
                            showProgressbar();
                        }
                        if (s.equals("hideProgressbar")) {
                            hideProgressbar();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        setImage();
                    }
                });
    }
}
