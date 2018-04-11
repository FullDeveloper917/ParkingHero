package com.emraz.parkinghero.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.emraz.parkinghero.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yousuf on 7/10/2017.
 */

public class PermissionRequest {


  private static String TAG = "PermissionRequest";


  private final Activity mActivity;
  private View mView;
  private int mRationaleMsgResId;
  private String[] mPermissions;
  private int mRequestCode;
  private OnPermissionResult mCallback;


  public PermissionRequest(Activity activity, int requestCode){
    mActivity = activity;
    this.mRequestCode = requestCode;

  }


  public PermissionRequest snackbar(View view){

    if (mView != null) {
      throw new IllegalArgumentException("A snackbar View is already set");
    }
    mView = view;
    return this;

  }

  public PermissionRequest rationale(@StringRes int resId){
    mRationaleMsgResId = resId;
    return this;
  }

  public PermissionRequest permissions(String... permissions){
    if (mPermissions != null) {
      throw new IllegalArgumentException("Permission have been already set");
    }

    mPermissions = permissions;
    return this;
  }

  public PermissionRequest callback(OnPermissionResult callback){
    mCallback = callback;
    return this;
  }





  public boolean isAllPermissionGranted(int[] grantResults){
    for (int i = 0; i < grantResults.length; i++) {
      if (grantResults[i] != PackageManager.PERMISSION_GRANTED  ) {
        return false;
      }
    }
    return true;
  }



  public void submit(){

    if (mActivity == null) {
      throw new NullPointerException("An activity must be set.");
    }

    if (mView == null) {
      throw new NullPointerException("Snackbar View is null");
    }

    if (mPermissions == null) {
      throw  new NullPointerException("Permissions are null");
    }


    checkPermission();
  }


  // @TargetApi(Build.VERSION_CODES.M)
  private void checkPermission(){
    int rationalPermission = 0;
    List<String> permissionRequest = new ArrayList<>();
    for(String permission : mPermissions){
      if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
        permissionRequest.add(permission);
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
          rationalPermission++;
        }
      }
    }


    if( permissionRequest.isEmpty() ){
      // All Permission are granted
      Log.d(TAG, "All permission granted");
      if (mCallback != null) {
        mCallback.onPermissionsGranted(mRequestCode, mPermissions);
      }
    }else {

      // show direct permission
      if( rationalPermission == 0 ){
        ActivityCompat.requestPermissions(mActivity, mPermissions, mRequestCode );
      }else {
        // show rationale permission
        showRationale();
      }

    }





  }


  private void showRationale(){

    String msg = mActivity.getString(mRationaleMsgResId);

    Snackbar snackbar =
        Snackbar.make(
            mView,
            msg,
            Snackbar.LENGTH_LONG);

    snackbar.setAction(
        mActivity.getString(R.string.OK),
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            /// Submit the request.
            ActivityCompat.requestPermissions(
                mActivity,mPermissions , mRequestCode);
          }
        });

    snackbar.show();
  }


  private void manuallyChangePermission(){
    Snackbar.make(mActivity.findViewById(android.R.id.content), mActivity.getString(R.string.change_permission_manual_message),
        Snackbar.LENGTH_SHORT).setAction(mActivity.getString(R.string.enable),
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            mActivity.startActivity(intent);
          }
        }).show();



  }


  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

    switch (requestCode) {
      case Constants.LOCATION_ACCESS_PERMISSION_ID:
        if (isAllPermissionGranted(grantResults)) {
          if (mCallback != null) {
            mCallback.onPermissionsGranted(mRequestCode, mPermissions);
          }
        }else {
          manuallyChangePermission();
        }
        break;
      default:
        break;
    }
  }


}
