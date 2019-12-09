// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import android.support.v4.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.GrantableRequest;
import permissions.dispatcher.PermissionUtils;

final class InstallActivityPermissionsDispatcher {
  private static final int REQUEST_INSTALLAPK = 7;

  private static final String[] PERMISSION_INSTALLAPK = new String[] {"android.permission.REQUEST_INSTALL_PACKAGES"};

  private static GrantableRequest PENDING_INSTALLAPK;

  private InstallActivityPermissionsDispatcher() {
  }

  static void installApkWithPermissionCheck(InstallActivity target, String filePath) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_INSTALLAPK)) {
      target.installApk(filePath);
    } else {
      PENDING_INSTALLAPK = new InstallActivityInstallApkPermissionRequest(target, filePath);
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_INSTALLAPK)) {
        target.onShowRationale(PENDING_INSTALLAPK);
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_INSTALLAPK, REQUEST_INSTALLAPK);
      }
    }
  }

  static void onRequestPermissionsResult(InstallActivity target, int requestCode,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_INSTALLAPK:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        if (PENDING_INSTALLAPK != null) {
          PENDING_INSTALLAPK.grant();
        }
      } else {
        target.onPerDenied();
      }
      PENDING_INSTALLAPK = null;
      break;
      default:
      break;
    }
  }

  private static final class InstallActivityInstallApkPermissionRequest implements GrantableRequest {
    private final WeakReference<InstallActivity> weakTarget;

    private final String filePath;

    private InstallActivityInstallApkPermissionRequest(InstallActivity target, String filePath) {
      this.weakTarget = new WeakReference<InstallActivity>(target);
      this.filePath = filePath;
    }

    @Override
    public void proceed() {
      InstallActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_INSTALLAPK, REQUEST_INSTALLAPK);
    }

    @Override
    public void cancel() {
      InstallActivity target = weakTarget.get();
      if (target == null) return;
      target.onPerDenied();
    }

    @Override
    public void grant() {
      InstallActivity target = weakTarget.get();
      if (target == null) return;
      target.installApk(filePath);
    }
  }
}
