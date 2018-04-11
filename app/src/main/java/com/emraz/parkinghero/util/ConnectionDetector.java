package com.emraz.parkinghero.util;

/**
 * Created by Yousuf on 7/5/2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Required permission
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */

public class ConnectionDetector {

  private Context context;

  public ConnectionDetector(Context _context) {
    this.context = _context;
  }

  public boolean isConnectedToInternet() {

    ConnectivityManager mConnectivity = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    if (mConnectivity != null) {
      NetworkInfo[] info = mConnectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {
          if (info[i].getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
        }
      }
    }

    return false;
  }

}