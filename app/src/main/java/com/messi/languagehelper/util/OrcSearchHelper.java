package com.messi.languagehelper.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.core.content.FileProvider;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.YoudaoPhotoBean;
import com.messi.languagehelper.dialog.OCRDialog;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import java.io.File;
import java.io.IOException;

import cn.leancloud.json.JSON;
import okhttp3.FormBody;

/**
 * Created by luli on 06/07/2017.
 */

public class OrcSearchHelper {

    private int orc_api_retry_times = 2;
    private Activity context;
    private String mCurrentPhotoPath;
    private FragmentProgressbarListener mProgressbarListener;

    public OrcSearchHelper(Activity context){
        this.context = context;
    }

    public void photoSelectDialog(){
        orc_api_retry_times = 2;
        String[] titles = {context.getResources().getString(R.string.leisure_photo_search),context.getResources().getString(R.string.photo_album)};
        OCRDialog mPhonoSelectDialog = new OCRDialog(context,titles,true);
        mPhonoSelectDialog.setListener(new OCRDialog.PopViewItemOnclickListener() {
            @Override
            public void onSecondClick(View v) {
                getImageFromAlbum();
            }
            @Override
            public void onFirstClick(View v) {
                getImageFromCamera();
            }
        });
        mPhonoSelectDialog.show();
    }

    public void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");//相片类型
        context.startActivityForResult(intent, CameraUtil.REQUEST_CODE_PICK_IMAGE);
    }

    public void getImageFromCamera() {
        LogUtil.DefalutLog("has permission");
        try {
            startCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCamera() throws Exception{
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                File photoFile = CameraUtil.createImageFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                LogUtil.DefalutLog("img uri:"+mCurrentPhotoPath);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri imageUri = FileProvider.getUriForFile(context, Setings.getProvider(context), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }
                context.startActivityForResult(takePictureIntent, CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA);
            } else {
                ToastUtil.diaplayMesShort(context, "请确认已经插入SD卡");
            }
        }
    }

    public void doCropPhoto(Uri uri) {
        File photoTemp = null;
        try {
            photoTemp = new File(CameraUtil.createTempFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Uri mOutUri = Uri.fromFile(photoTemp);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutUri);
        intent.putExtra("noFaceDetection",  false);
        context.startActivityForResult(intent, CameraUtil.PHOTO_PICKED_WITH_DATA);
    }

    public void sendYoudaoOCR(){
        try {
            loadding();
            String path = CameraUtil.createTempFile();
            String base64 = "data:image/png;base64,"+CameraUtil.getImageBase64(path,600,600,1024);
            LogUtil.DefalutLog("YoudaoOCR-base64:"+base64);
            FormBody formBody = new FormBody.Builder()
                    .add("imgBase", base64)
                    .build();
            LanguagehelperHttpClient.postWithHeader(Setings.YDOcrQuestion,formBody, new UICallback(context){
                @Override
                public void onResponsed(String responseString){
                    if(JsonParser.isJson(responseString)){
                        LogUtil.DefalutLog("YoudaoOCR:"+responseString);
                        YoudaoPhotoBean bean = JSON.parseObject(responseString,YoudaoPhotoBean.class);
                        LiveEventBus.get(KeyUtil.YoudaoPhotoBean, YoudaoPhotoBean.class).post(bean);
                    }else{
                        showToast(context.getResources().getString(R.string.server_error));
                    }
                }
                @Override
                public void onFailured() {
                    showToast(context.getResources().getString(R.string.network_error));
                }
                @Override
                public void onFinished() {
                    finishLoadding();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtil.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null){
                Uri uri = data.getData();
                if(uri != null){
                    doCropPhoto(uri);
                }
            }
        } else if (requestCode == CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA && resultCode == Activity.RESULT_OK) {
            Uri contentUri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                contentUri = FileProvider.getUriForFile(context, Setings.getProvider(context), new File(mCurrentPhotoPath));
            }else {
                contentUri = Uri.fromFile(new File(mCurrentPhotoPath));
            }
            doCropPhoto(contentUri);
        }else if (requestCode == CameraUtil.PHOTO_PICKED_WITH_DATA && resultCode == Activity.RESULT_OK) {
            sendYoudaoOCR();
        }
    }

    private void loadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.showProgressbar();
        }
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void finishLoadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.hideProgressbar();
        }
    }

    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(context, toastString);
    }

}
