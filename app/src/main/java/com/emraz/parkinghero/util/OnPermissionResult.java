package com.emraz.parkinghero.util;

/**
 * Created by Yousuf on 7/10/2017.
 */

public interface OnPermissionResult {

  // Callback after Permission Granted
  void onPermissionsGranted(int requestCode, String[] permissions);
  // Callback after Permission denied
  void onPermissionsDenied(int requestCode, String[] permissions);
}
