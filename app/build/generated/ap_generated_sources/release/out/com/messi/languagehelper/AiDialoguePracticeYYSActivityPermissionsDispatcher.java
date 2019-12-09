// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import android.support.v4.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class AiDialoguePracticeYYSActivityPermissionsDispatcher {
  private static final int REQUEST_SHOWIATDIALOG = 2;

  private static final String[] PERMISSION_SHOWIATDIALOG = new String[] {"android.permission.RECORD_AUDIO"};

  private AiDialoguePracticeYYSActivityPermissionsDispatcher() {
  }

  static void showIatDialogWithPermissionCheck(AiDialoguePracticeYYSActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWIATDIALOG)) {
      target.showIatDialog();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWIATDIALOG)) {
        target.onShowRationale(new AiDialoguePracticeYYSActivityShowIatDialogPermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_SHOWIATDIALOG, REQUEST_SHOWIATDIALOG);
      }
    }
  }

  static void onRequestPermissionsResult(AiDialoguePracticeYYSActivity target, int requestCode,
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

  private static final class AiDialoguePracticeYYSActivityShowIatDialogPermissionRequest implements PermissionRequest {
    private final WeakReference<AiDialoguePracticeYYSActivity> weakTarget;

    private AiDialoguePracticeYYSActivityShowIatDialogPermissionRequest(
        AiDialoguePracticeYYSActivity target) {
      this.weakTarget = new WeakReference<AiDialoguePracticeYYSActivity>(target);
    }

    @Override
    public void proceed() {
      AiDialoguePracticeYYSActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_SHOWIATDIALOG, REQUEST_SHOWIATDIALOG);
    }

    @Override
    public void cancel() {
      AiDialoguePracticeYYSActivity target = weakTarget.get();
      if (target == null) return;
      target.onPerDenied();
    }
  }
}
