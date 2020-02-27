package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.messi.languagehelper.bean.YoudaoPhotoBean;
import com.messi.languagehelper.bean.YoudaoPhotouestions;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.OrcSearchHelper;
import com.messi.languagehelper.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PhotoSearchActivity extends BaseActivity {

    @BindView(R.id.camera_btn)
    Button cameraBtn;
    @BindView(R.id.camera_layout)
    LinearLayout cameraLayout;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.content_tv)
    WebView content_tv;
    private OrcSearchHelper mOrcHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_search_activity);
        ButterKnife.bind(this);
        isRegisterBus = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onClick();
            }
        },350);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showORCDialog() {
        if (mOrcHelper == null) {
            mOrcHelper = new OrcSearchHelper(this);
        }
        mOrcHelper.photoSelectDialog();
        showProgressbar();
        MobclickAgent.onEvent(this,"test_yd_photosearch");
    }

    @OnClick(R.id.camera_layout)
    public void onClick() {
        PhotoSearchActivityPermissionsDispatcher.showORCDialogWithPermissionCheck(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(YoudaoPhotoBean bean){
        hideProgressbar();
        LogUtil.DefalutLog("YoudaoPhotoBean:"+bean.getErrorCode());
        content_tv.loadData("","","");
        if(bean != null && "0".equals(bean.getErrorCode())){
            StringBuilder sb = new StringBuilder();
            for(YoudaoPhotouestions item : bean.getData().getQuestions()){
                sb.append(item.getContent());
                sb.append("<br />");
                sb.append(item.getAnswer());
                sb.append("<br />");
                sb.append(item.getAnalysis());
                sb.append("<br />");
                sb.append(item.getKnowledge());
                sb.append("<br /><br />");
            }
            content_tv.loadData(sb.toString(),"","utf-8");
        }else {
            content_tv.loadData("sorry，没有找到！<br /><br />"+bean.getData().getText(),"","utf-8");
        }
    }

    @Override
    public void onBackPressed() {
        hideProgressbar();
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mOrcHelper != null){
            mOrcHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PhotoSearchActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleFoCamera(final PermissionRequest request) {
        showRationaleDialog(request);
    }

    public void showRationaleDialog(PermissionRequest request) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("温馨提示")
                .setMessage("需要授权才能使用。")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).show();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showCameraDenied() {
        ToastUtil.diaplayMesShort(this, "拒绝授权，将无法使用部分功能！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressbar();
    }
}
