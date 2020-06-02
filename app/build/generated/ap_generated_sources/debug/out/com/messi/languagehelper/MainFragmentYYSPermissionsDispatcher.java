// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import androidx.annotation.NonNull;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class MainFragmentYYSPermissionsDispatcher {
  private static final int REQUEST_SHOWORCDIALOG = 14;

  private static final String[] PERMISSION_SHOWORCDIALOG = new String[] {"android.permission.CAMERA"};

  private static final int REQUEST_SHOWRECORD = 15;

  private static final String[] PERMISSION_SHOWRECORD = new String[] {"android.permission.RECORD_AUDIO"};

  private MainFragmentYYSPermissionsDispatcher() {
  }

  static void showORCDialogWithPermissionCheck(@NonNull MainFragmentYYS target) {
    if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWORCDIALOG)) {
      target.showORCDialog();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWORCDIALOG)) {
        target.showRationaleFoCamera(new MainFragmentYYSShowORCDialogPermissionRequest(target));
      } else {
        target.requestPermissions(PERMISSION_SHOWORCDIALOG, REQUEST_SHOWORCDIALOG);
      }
    }
  }

  static void showRecordWithPermissionCheck(@NonNull MainFragmentYYS target) {
    if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWRECORD)) {
      target.showRecord();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWRECORD)) {
        target.onShowRationale(new MainFragmentYYSShowRecordPermissionRequest(target));
      } else {
        target.requestPermissions(PERMISSION_SHOWRECORD, REQUEST_SHOWRECORD);
      }
    }
  }

  static void onRequestPermissionsResult(@NonNull MainFragmentYYS target, int requestCode,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_SHOWORCDIALOG:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.showORCDialog();
      } else {
        target.showCameraDenied();
      }
      break;
      case REQUEST_SHOWRECORD:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.showRecord();
      } else {
        target.onPerDenied();
      }
      break;
      default:
      break;
    }
  }

  private static final class MainFragmentYYSShowORCDialogPermissionRequest implements PermissionRequest {
    private final WeakReference<MainFragmentYYS> weakTarget;

    private MainFragmentYYSShowORCDialogPermissionRequest(@NonNull MainFragmentYYS target) {
      this.weakTarget = new WeakReference<MainFragmentYYS>(target);
    }

    @Override
    public void proceed() {
      MainFragmentYYS target = weakTarget.get();
      if (target == null) return;
      target.requestPermissions(PERMISSION_SHOWORCDIALOG, REQUEST_SHOWORCDIALOG);
    }

    @Override
    public void cancel() {
      MainFragmentYYS target = weakTarget.get();
      if (target == null) return;
      target.showCameraDenied();
    }
  }

  private static final class MainFragmentYYSShowRecordPermissionRequest implements PermissionRequest {
    private final WeakReference<MainFragmentYYS> weakTarget;

    private MainFragmentYYSShowRecordPermissionRequest(@NonNull MainFragmentYYS target) {
      this.weakTarget = new WeakReference<MainFragmentYYS>(target);
    }

    @Override
    public void proceed() {
      MainFragmentYYS target = weakTarget.get();
      if (target == null) return;
      target.requestPermissions(PERMISSION_SHOWRECORD, REQUEST_SHOWRECORD);
    }

    @Override
    public void cancel() {
      MainFragmentYYS target = weakTarget.get();
      if (target == null) return;
      target.onPerDenied();
    }
  }
}
