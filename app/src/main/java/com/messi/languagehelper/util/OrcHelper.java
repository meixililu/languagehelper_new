package com.messi.languagehelper.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.BaiduOcrRoot;
import com.messi.languagehelper.dialog.OCRDialog;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.impl.OrcResultListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by luli on 06/07/2017.
 */

public class OrcHelper {

    private int orc_api_retry_times = 2;
    private Fragment fragment;
    private Activity context;
    private String mCurrentPhotoPath;
    private OrcResultListener mOrcResultListener;
    private FragmentProgressbarListener mProgressbarListener;

    public OrcHelper(Fragment fragment,OrcResultListener mOrcResultListener,FragmentProgressbarListener mProgressbarListener){
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.mOrcResultListener = mOrcResultListener;
        this.mProgressbarListener = mProgressbarListener;
    }

    public void photoSelectDialog(){
        orc_api_retry_times = 2;
        String[] titles = {context.getResources().getString(R.string.take_photo),context.getResources().getString(R.string.photo_album)};
        OCRDialog mPhonoSelectDialog = new OCRDialog(context,titles);
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
        fragment.startActivityForResult(intent, CameraUtil.REQUEST_CODE_PICK_IMAGE);
    }

    public void getImageFromCamera() {
        LogUtil.DefalutLog("check permission");
        AndPermission.with(context)
                .permission(Permission.CAMERA)
                .requestCode(300)
                .callback(this)
                .start();
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
                    Uri imageUri = FileProvider.getUriForFile(context, SDCardUtil.Provider, photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }
                fragment.startActivityForResult(takePictureIntent, CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA);
            } else {
                ToastUtil.diaplayMesShort(context, "请确认已经插入SD卡");
            }
        }
    }

    @PermissionYes(300)
    private void getPermissionYes(List<String> grantedPermissions) {
        LogUtil.DefalutLog("has permission");
        try {
            startCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PermissionNo(300)
    private void getPermissionNo(List<String> deniedPermissions) {
        LogUtil.DefalutLog("permission deny");
        AndPermission.defaultSettingDialog(context, 400).show();
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
        fragment.startActivityForResult(intent, CameraUtil.PHOTO_PICKED_WITH_DATA);
    }

    public void sendBaiduOCR(){
        try {
            loadding();
            LanguagehelperHttpClient.postBaiduOCR(context,CameraUtil.createTempFile(), new UICallback(context){
                @Override
                public void onResponsed(String responseString){
                    if(JsonParser.isJson(responseString)){
                        LogUtil.DefalutLog("BaiduOCR:"+responseString);
                        BaiduOcrRoot mBaiduOcrRoot = JSON.parseObject(responseString, BaiduOcrRoot.class);
                        if(mBaiduOcrRoot.getWords_result_num() > 0){
                            if(mOrcResultListener!= null){
                                mOrcResultListener.ShowResult(mBaiduOcrRoot);
                            }
                        }else{
                            if(mBaiduOcrRoot.getError_code() > 90 && mBaiduOcrRoot.getError_code() < 120){
                                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessToken, "");
                                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessTokenExpires, (long)0);
                                Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.BaiduAccessTokenCreateAt, (long)0);
                                if(orc_api_retry_times > 0){
                                    orc_api_retry_times--;
                                    sendBaiduOCR();
                                }
                            }else {
                                showToast(mBaiduOcrRoot.getError_msg());
                            }
                        }
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
                contentUri = FileProvider.getUriForFile(context, SDCardUtil.Provider, new File(mCurrentPhotoPath));
            }else {
                contentUri = Uri.fromFile(new File(mCurrentPhotoPath));
            }
            doCropPhoto(contentUri);
        }else if (requestCode == CameraUtil.PHOTO_PICKED_WITH_DATA && resultCode == Activity.RESULT_OK) {
            sendBaiduOCR();
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
