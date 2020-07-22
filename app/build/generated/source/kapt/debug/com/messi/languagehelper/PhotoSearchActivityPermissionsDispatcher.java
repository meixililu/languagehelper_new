// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class PhotoSearchActivityPermissionsDispatcher {
  private static final int REQUEST_SHOWORCDIALOG = 13;

  private static final String[] PERMISSION_SHOWORCDIALOG = new String[] {"android.permission.CAMERA"};

  private PhotoSearchActivityPermissionsDispatcher() {
  }

  static void showORCDialogWithPermissionCheck(@NonNull PhotoSearchActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWORCDIALOG)) {
      target.showORCDialog();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWORCDIALOG)) {
        target.showRationaleFoCamera(new PhotoSearchActivityShowORCDialogPermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_SHOWORCDIALOG, REQUEST_SHOWORCDIALOG);
      }
    }
  }

  static void onRequestPermissionsResult(@NonNull PhotoSearchActivity target, int requestCode,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_SHOWORCDIALOG:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.showORCDialog();
      } else {
        target.showCameraDenied();
      }
      break;
      default:
      break;
    }
  }

  private static final class PhotoSearchActivityShowORCDialogPermissionRequest implements PermissionRequest {
    private final WeakReference<PhotoSearchActivity> weakTarget;

    private PhotoSearchActivityShowORCDialogPermissionRequest(@NonNull PhotoSearchActivity target) {
      this.weakTarget = new WeakReference<PhotoSearchActivity>(target);
    }

    @Override
    public void proceed() {
      PhotoSearchActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_SHOWORCDIALOG, REQUEST_SHOWORCDIALOG);
    }

    @Override
    public void cancel() {
      PhotoSearchActivity target = weakTarget.get();
      if (target == null) return;
      target.showCameraDenied();
    }
  }
}
