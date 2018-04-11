package com.emraz.parkinghero.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class Util {

  public static String getLocationAsString(LatLng latLng){

    String str = latLng.latitude + "," + latLng.longitude;
    return str;

  }




}
