// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class WebViewWithMicActivityPermissionsDispatcher {
  private static final int REQUEST_SHOWIATDIALOG = 20;

  private static final String[] PERMISSION_SHOWIATDIALOG = new String[] {"android.permission.RECORD_AUDIO"};

  private WebViewWithMicActivityPermissionsDispatcher() {
  }

  static void showIatDialogWithPermissionCheck(@NonNull WebViewWithMicActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWIATDIALOG)) {
      target.showIatDialog();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWIATDIALOG)) {
        target.onShowRationale(new WebViewWithMicActivityShowIatDialogPermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_SHOWIATDIALOG, REQUEST_SHOWIATDIALOG);
      }
    }
  }

  static void onRequestPermissionsResult(@NonNull WebViewWithMicActivity target, int requestCode,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_SHOWIATDIALOG:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.showIatDialog();
      } else {
        target.onPerDenied();
      }
      break;
      default:
      break;
    }
  }

  private static final class WebViewWithMicActivityShowIatDialogPermissionRequest implements PermissionRequest {
    private final WeakReference<WebViewWithMicActivity> weakTarget;

    private WebViewWithMicActivityShowIatDialogPermissionRequest(
        @NonNull WebViewWithMicActivity target) {
      this.weakTarget = new WeakReference<WebViewWithMicActivity>(target);
    }

    @Override
    public void proceed() {
      WebViewWithMicActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_SHOWIATDIALOG, REQUEST_SHOWIATDIALOG);
    }

    @Override
    public void cancel() {
      WebViewWithMicActivity target = weakTarget.get();
      if (target == null) return;
      target.onPerDenied();
    }
  }
}
