// This file was generated by PermissionsDispatcher. Do not modify!
package com.messi.languagehelper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class XimalayaRadioLocalActivityPermissionsDispatcher {
  private static final int REQUEST_NEEDLOCATION = 21;

  private static final String[] PERMISSION_NEEDLOCATION = new String[] {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

  private XimalayaRadioLocalActivityPermissionsDispatcher() {
  }

  static void needLocationWithPermissionCheck(@NonNull XimalayaRadioLocalActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_NEEDLOCATION)) {
      target.needLocation();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_NEEDLOCATION)) {
        target.onShowRationale(new XimalayaRadioLocalActivityNeedLocationPermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_NEEDLOCATION, REQUEST_NEEDLOCATION);
      }
    }
  }

  static void onRequestPermissionsResult(@NonNull XimalayaRadioLocalActivity target,
      int requestCode, int[] grantResults) {
    switch (requestCode) {
      case REQUEST_NEEDLOCATION:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.needLocation();
      } else {
        target.onPerDenied();
      }
      break;
      default:
      break;
    }
  }

  private static final class XimalayaRadioLocalActivityNeedLocationPermissionRequest implements PermissionRequest {
    private final WeakReference<XimalayaRadioLocalActivity> weakTarget;

    private XimalayaRadioLocalActivityNeedLocationPermissionRequest(
        @NonNull XimalayaRadioLocalActivity target) {
      this.weakTarget = new WeakReference<XimalayaRadioLocalActivity>(target);
    }

    @Override
    public void proceed() {
      XimalayaRadioLocalActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_NEEDLOCATION, REQUEST_NEEDLOCATION);
    }

    @Override
    public void cancel() {
      XimalayaRadioLocalActivity target = weakTarget.get();
      if (target == null) return;
      target.onPerDenied();
    }
  }
}