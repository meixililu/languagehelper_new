// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class MainFragmentPermissionsDispatcher {
  private static final int REQUEST_SHOWIATDIALOG = 8;

  private static final String[] PERMISSION_SHOWIATDIALOG = new String[] {"android.permission.RECORD_AUDIO"};

  private static final int REQUEST_SHOWORCDIALOG = 9;

  private static final String[] PERMISSION_SHOWORCDIALOG = new String[] {"android.permission.CAMERA"};

  private MainFragmentPermissionsDispatcher() {
  }

  static void showORCDialogWithPermissionCheck(MainFragment target) {
    if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWORCDIALOG)) {
      target.showORCDialog();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWORCDIALOG)) {
        target.showRationaleFoCamera(new MainFragmentShowORCDialogPermissionRequest(target));
      } else {
        target.requestPermissions(PERMISSION_SHOWORCDIALOG, REQUEST_SHOWORCDIALOG);
      }
    }
  }

  static void showIatDialogWithPermissionCheck(MainFragment target) {
    if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWIATDIALOG)) {
      target.showIatDialog();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWIATDIALOG)) {
        target.showRationaleForRecord(new MainFragmentShowIatDialogPermissionRequest(target));
      } else {
        target.requestPermissions(PERMISSION_SHOWIATDIALOG, REQUEST_SHOWIATDIALOG);
      }
    }
  }

  static void onRequestPermissionsResult(MainFragment target, int requestCode, int[] grantResults) {
    switch (requestCode) {
      case REQUEST_SHOWORCDIALOG:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.showORCDialog();
      } else {
        target.showCameraDenied();
      }
      break;
      case REQUEST_SHOWIATDIALOG:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.showIatDialog();
      } else {
        target.showRecordDenied();
      }
      break;
      default:
      break;
    }
  }

  private static final class MainFragmentShowORCDialogPermissionRequest implements PermissionRequest {
    private final WeakReference<MainFragment> weakTarget;

    private MainFragmentShowORCDialogPermissionRequest(MainFragment target) {
      this.weakTarget = new WeakReference<MainFragment>(target);
    }

    @Override
    public void proceed() {
      MainFragment target = weakTarget.get();
      if (target == null) return;
      target.requestPermissions(PERMISSION_SHOWORCDIALOG, REQUEST_SHOWORCDIALOG);
    }

    @Override
    public void cancel() {
      MainFragment target = weakTarget.get();
      if (target == null) return;
      target.showCameraDenied();
    }
  }

  private static final class MainFragmentShowIatDialogPermissionRequest implements PermissionRequest {
    private final WeakReference<MainFragment> weakTarget;

    private MainFragmentShowIatDialogPermissionRequest(MainFragment target) {
      this.weakTarget = new WeakReference<MainFragment>(target);
    }

    @Override
    public void proceed() {
      MainFragment target = weakTarget.get();
      if (target == null) return;
      target.requestPermissions(PERMISSION_SHOWIATDIALOG, REQUEST_SHOWIATDIALOG);
    }

    @Override
    public void cancel() {
      MainFragment target = weakTarget.get();
      if (target == null) return;
      target.showRecordDenied();
    }
  }
}