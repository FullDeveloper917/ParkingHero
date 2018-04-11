package com.emraz.parkinghero.util;

import com.emraz.parkinghero.BuildConfig;

/**
 * Created by Yousuf on 7/5/2017.
 */

public class Log {

  private static boolean isLogVisible = BuildConfig.DEBUG;

  private String TAG = getClass().getSimpleName();


  public void setLogVisible(boolean logVisible) {
    isLogVisible = logVisible;
  }


  public static void d(String tag, String message){
    if ( isLogVisible )
      android.util.Log.d(tag, message);
  }


  public static void e(String tag, String message){
    if ( isLogVisible )
      android.util.Log.e(tag, message);
  }

  public static void w(String tag, String message){
    if ( isLogVisible )
      android.util.Log.w(tag, message);
  }

  public static void i(String tag, String message){
    if ( isLogVisible )
      android.util.Log.i(tag, message);
  }

  public static void v(String tag, String message){
    if ( isLogVisible )
      android.util.Log.v(tag, message);
  }

  public static void wtf(String tag, String message){
    if ( isLogVisible )
      android.util.Log.wtf(tag, message);
  }


}