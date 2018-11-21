package com.messi.languagehelper;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.messi.languagehelper.impl.ProgressListener;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ZipUtil;

import java.util.HashMap;

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

    private final static String TempFileName = "temp.zip";
    private final static String minimalistUrl = "http://www.mzxbkj.com/dictres/localdictjjb.zip";
    private final static String standarUrl = "http://www.mzxbkj.com/dictres/localdictxxb.zip";
    private final static String sentenceUrl = "http://www.mzxbkj.com/dictres/ce.zip";
    private final static String kuozhankuUrl = "http://www.mzxbkj.com/dictres/wordlib.zip";
    private final static String hhUrl = "http://www.mzxbkj.com/dictres/hh.zip";
    @BindView(R.id.dic_minimalist_img)
    ImageView dicMinimalistImg;
    @BindView(R.id.dic_minimalist)
    FrameLayout dicMinimalist;
    @BindView(R.id.dic_standard_img)
    ImageView dicStandardImg;
    @BindView(R.id.dic_standard)
    FrameLayout dicStandard;
    @BindView(R.id.dic_sentence_img)
    ImageView dicSentenceImg;
    @BindView(R.id.dic_sentence)
    FrameLayout dicSentence;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;
    @BindView(R.id.kz_img)
    ImageView kzImg;
    @BindView(R.id.kz_layout)
    FrameLayout kzLayout;
    @BindView(R.id.hh_img)
    ImageView hhImg;
    @BindView(R.id.hh_layout)
    FrameLayout hhLayout;

    //0 no dic, 1 minimalist, 2 standard
    private int status = 0;
    private String mOfflineDicPath;
    private HashMap<String,String> downloading;

    final ProgressListener progressListener = new ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            try {
                final int percent = (int) ((100 * bytesRead) / contentLength);
                if (percent != 100) {
                    OfflineDicDownloadActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            numberProgressBar.setVisibility(View.VISIBLE);
                            numberProgressBar.setProgress(percent);
                        }
                    });
                } else if (percent == 100) {
                    if (done) {
                        OfflineDicDownloadActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                numberProgressBar.setProgress(100);
                                numberProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_dic_download_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_offline_dic));
        downloading = new HashMap<>();
        setImage();
    }

    private void setImage() {
        try {
            mOfflineDicPath = SDCardUtil.getDownloadPath(SDCardUtil.OfflineDicPath);
            long size = SDCardUtil.getFileSize(mOfflineDicPath + "localdict.datx");
            if (size > 0) {
                if (size > (6 * 1048576)) {
                    status = 2;
                    dicStandardImg.setImageResource(R.drawable.ic_done);
                    dicMinimalistImg.setImageResource(R.drawable.ic_done);
                } else {
                    status = 1;
                    dicMinimalistImg.setImageResource(R.drawable.ic_done);
                }
            }
            if (isSentenceExist()) {
                dicSentenceImg.setImageResource(R.drawable.ic_done);
            }
            if (isHHExist()) {
                hhImg.setImageResource(R.drawable.ic_done);
            }
            if (isKZExist()) {
                kzImg.setImageResource(R.drawable.ic_done);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSentenceExist(){
        return SDCardUtil.isFileExist(mOfflineDicPath + "c2e/");
    }

    private boolean isHHExist(){
        return SDCardUtil.isFileExist(mOfflineDicPath + "hh");
    }

    private boolean isKZExist(){
        return SDCardUtil.isFileExist(mOfflineDicPath + "IrregularWords");
    }

    @OnClick({R.id.dic_minimalist, R.id.dic_standard,R.id.kz_layout, R.id.hh_layout,R.id.dic_sentence})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dic_minimalist:
                downloadMinimalistDic();
                break;
            case R.id.dic_standard:
                downloadStandardDic();
                break;
            case R.id.kz_layout:
                downloadKZ();
                break;
            case R.id.hh_layout:
                downloadHH();
                break;
            case R.id.dic_sentence:
                downloadSentence();
                break;
        }
    }

    private void downloadMinimalistDic() {
        if (status < 1) {
            downloadFile(minimalistUrl, TempFileName);
        } else {
            ToastUtil.diaplayMesShort(this, "已下载精简版离线词典");
        }
    }

    private void downloadStandardDic() {
        if (status < 2) {
            downloadFile(standarUrl, TempFileName);
        } else {
            ToastUtil.diaplayMesShort(this, "已下载标准版离线词典");
        }
    }

    private void downloadSentence() {
        if (!isSentenceExist()) {
            downloadFile(sentenceUrl, TempFileName);
        } else {
            ToastUtil.diaplayMesShort(this, "已下载离线句子翻译");
        }
    }

    private void downloadHH() {
        if (!isHHExist()) {
            downloadFile(hhUrl, TempFileName);
        } else {
            ToastUtil.diaplayMesShort(this, "已下载离线汉语词典");
        }
    }

    private void downloadKZ() {
        if (!isKZExist()) {
            downloadFile(kuozhankuUrl, TempFileName);
        } else {
            ToastUtil.diaplayMesShort(this, "已下载离线词汇扩展包");
        }
    }

    private void downloadFile(final String url, final String fileName) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if(!downloading.containsKey(url)){
                    boolean result = DownLoadUtil.downloadFile(OfflineDicDownloadActivity.this, url, SDCardUtil.OfflineDicPath,
                            fileName, progressListener, "");
                    downloading.put(url,"yes");
                    if(result){
                        e.onNext("unzip");
                        ZipUtil.Unzip(mOfflineDicPath + fileName, mOfflineDicPath, true);
                        e.onNext("finish");
                        e.onComplete();
                    }
                    downloading.remove(url);
                }else {
                    LogUtil.DefalutLog("downloading");
                }
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
                        prompt(s);
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

    private void prompt(String s) {
        if (s.equals("unzip")) {
            ToastUtil.diaplayMesShort(OfflineDicDownloadActivity.this, "离线包下载完成，开始解压");
            showProgressbar();
        } else if (s.equals("finish")) {
            ToastUtil.diaplayMesShort(OfflineDicDownloadActivity.this, "解压完成，可以使用离线翻译了");
            hideProgressbar();
        }
    }

}
